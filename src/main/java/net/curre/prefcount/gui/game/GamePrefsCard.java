/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.curre.prefcount.gui.game;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstraints;
import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.bean.Settings;
import net.curre.prefcount.event.DivisibleByNActionListener;
import net.curre.prefcount.event.NumberOfPlayersActionListener;
import net.curre.prefcount.event.PrefTypeActionListener;
import net.curre.prefcount.gui.type.PrefType;
import net.curre.prefcount.util.LocaleExt;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;

/**
 * Represents a panel used to display game preferences inputs.
 *
 * @author Yevgeny Nyden
 */
public class GamePrefsCard extends DataCard {

  /** Reference to the "3 players" radio button. */
  private JRadioButton players3Button;

  /**
   * Ctor.
   *
   * @param inputDataPanel parent container panel.
   */
  public GamePrefsCard(DataCardsContainerPanel inputDataPanel) {
    this.setLayout(new TableLayout(new double[][] {
        {TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL}, // columns
        {TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL}})); // rows

    JPanel containerPanel = new JPanel(new FlowLayout());
    Settings settings = PrefCountRegistry.getInstance().getSettingsService().getSettings();

    // Creating the pref type options.
    boolean isLeningradka = settings.getPrefType() == PrefType.LENINGRAD;
    containerPanel.add(createPrefOptionsPanel(isLeningradka));

    // Creating the number of players options.
    boolean players3Selected = settings.getNumberOfPlayers() == 3;
    containerPanel.add(createPlayerNumberPanel(players3Selected, inputDataPanel));

    // Creating the divisibility options.
    containerPanel.add(createDivisibilityPanel());

    this.add(containerPanel, new TableLayoutConstraints(
        1, 1, 1, 1, TableLayout.CENTER, TableLayout.CENTER));
  }

  /** {@inheritDoc} */
  @Override
  public String getTitle() {
    return LocaleExt.getString("pref.datacard.pref.title");
  }

  /** {@inheritDoc} */
  @Override
  public void focusFirstField() {
  }

  /**
   * Always returns null.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public @Null String validateFields(boolean isForward) {
    return null;
  }

  /**
   * Does nothing.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public void doOnEntry() {
  }

  /** {@inheritDoc} */
  @Override
  public void doOnForwardLeave() {
  }

  /** @inheritDoc */
  @Override
  public void doOnBackwardLeave() {
  }

  /**
   * Determines if user entered some names data.
   *
   * @return false always
   */
  @Override
  public boolean isSomeDataEntered() {
    return false;
  }

  /**
   * Gets the current number of players selected.
   *
   * @return the current number of players.
   */
  public int getNumberOfPlayers() {
    return this.players3Button.isSelected() ? 3 : 4;
  }

  /**
   * Creates the pref type panel.
   *
   * @param isLeningradka true if Leningradka is currently selected.
   * @return the new panel.
   */
  private JPanel createPrefOptionsPanel(boolean isLeningradka) {
    ButtonGroup group = new ButtonGroup();
    JRadioButton leningradButton = createRadioButton("pref.scoreboard.prefType.leningrad",
        "pref.scoreboard.prefType.leningrad.tooltip", group, isLeningradka);
    JRadioButton sochiButton = createRadioButton("pref.scoreboard.prefType.sochi",
        "pref.scoreboard.prefType.sochi.tooltip", group, !isLeningradka);
    ActionListener prefTypeListener = new PrefTypeActionListener(leningradButton);
    leningradButton.addActionListener(prefTypeListener);
    sochiButton.addActionListener(prefTypeListener);
    return createOptionSubPanel("pref.scoreboard.prefType.title", leningradButton, sochiButton);
  }

  /**
   * Creates the number of players panel.
   *
   * @param players3Selected true if the 3 players option is currently selected.
   * @param inputDataPanel parent container panel.
   * @return the new panel.
   */
  private JPanel createPlayerNumberPanel(boolean players3Selected, DataCardsContainerPanel inputDataPanel) {
    ButtonGroup group = new ButtonGroup();
    this.players3Button = createRadioButton("pref.scoreboard.players3",
        "pref.scoreboard.players3.tooltip", group, players3Selected);
    JRadioButton players4Button = createRadioButton("pref.scoreboard.players4",
        "pref.scoreboard.players4.tooltip", group, !players3Selected);
    ActionListener playerCountListener = new NumberOfPlayersActionListener(this.players3Button, inputDataPanel);
    this.players3Button.addActionListener(playerCountListener);
    players4Button.addActionListener(playerCountListener);

    JPanel panel = createOptionSubPanel("pref.scoreboard.players.title", this.players3Button, players4Button);
    Dimension dimension = panel.getPreferredSize();
    panel.setPreferredSize(new Dimension(dimension.width + 20, dimension.height));
    return panel;
  }

  /**
   * Creates the Mount divisibility panel.
   *
   * @return the new panel.
   */
  private JPanel createDivisibilityPanel() {
    ButtonGroup group = new ButtonGroup();
    JRadioButton divisibleIgnoreButton = createRadioButton("pref.scoreboard.divisible.ignore",
        "pref.scoreboard.divisible.ignore.tooltip", group, true);
    JRadioButton divisibleByNButton = createRadioButton("pref.scoreboard.divisible.byN",
        "pref.scoreboard.divisible.byN.tooltip", group, false);
    ActionListener divisibilityListener = new DivisibleByNActionListener(divisibleByNButton);
    divisibleIgnoreButton.addActionListener(divisibilityListener);
    divisibleByNButton.addActionListener(divisibilityListener);
    return createOptionSubPanel("pref.scoreboard.divisible.title", divisibleIgnoreButton, divisibleByNButton);
  }

  /**
   * Creates an option panel (one of the three).
   *
   * @param titleKey header title key.
   * @param button1  radio button 1 to add.
   * @param button2  radio button 2 to add.
   * @return created panel.
   */
  private JPanel createOptionSubPanel(
      @NotNull String titleKey, @NotNull JRadioButton button1, @NotNull JRadioButton button2) {
    JPanel panel = new JPanel();

    TitledBorder titledBorder = BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(Color.DARK_GRAY), LocaleExt.getString(titleKey));
    Font titleFont = titledBorder.getTitleFont();
    titledBorder.setTitleFont(titleFont.deriveFont(Font.BOLD, titleFont.getSize()));
    CompoundBorder outerBorder = BorderFactory.createCompoundBorder(titledBorder, new EmptyBorder(10, 10, 10, 10));
    panel.setBorder(outerBorder);

    GroupLayout panelLayout = new GroupLayout(panel);
    panel.setLayout(panelLayout);
    panelLayout.setHorizontalGroup(panelLayout.createSequentialGroup()
        .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
            .addComponent(button1)
            .addComponent(button2)));
    panelLayout.setVerticalGroup(
        panelLayout.createSequentialGroup()
            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(button1)
            .addComponent(button2));
    return panel;
  }

  /**
   * Creates a new <code>JRadioButton</code> radio button
   * and initializes it with the passed parameters.
   *
   * @param labelKey resource key for the label.
   * @param tooltipKey resource key for the tooltip.
   * @param group button group to add this radio button to.
   * @param selected true if the button should be selected; false if otherwise.
   * @return the new radio button.
   */
  public JRadioButton createRadioButton(String labelKey, String tooltipKey, ButtonGroup group, boolean selected) {
    JRadioButton button = new JRadioButton(LocaleExt.getString(labelKey));
    button.setSelected(selected);
    if (tooltipKey != null) {
      button.setToolTipText(LocaleExt.getString(tooltipKey));
    }
    group.add(button);
    return button;
  }
}
