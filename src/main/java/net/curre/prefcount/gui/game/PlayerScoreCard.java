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
import net.curre.prefcount.bean.GameResultBean;
import net.curre.prefcount.bean.PlayerStatistics;
import net.curre.prefcount.gui.type.Place;
import net.curre.prefcount.service.UiService;
import net.curre.prefcount.util.LocaleExt;
import org.apache.commons.lang3.StringUtils;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.validation.constraints.Null;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a card with a single player's game scores.
 *
 * @author Yevgeny Nyden
 */
public class PlayerScoreCard extends DataCard {

  /** Reference to the game options cards panel (which contains all the data input fields). */
  private final DataCardsContainerPanel inputDataPanel;

  /** The player's name. */
  private String playerName;

  /** The player's place. */
  private final Place playerPlace;

  /** Reference to the mount text field. */
  protected JTextField mountField;

  /** Reference to the pool text field. */
  protected JTextField poolField;

  /** Reference to the whists text fields. */
  protected Map<Place, JTextField> whistFields;

  /** Reference to the whists label fields. */
  private final HashMap<Place, JLabel> whistLabels;

  /**
   * Ctor.
   *
   * @param inputDataPanel parent input data panel.
   * @param playerName player's name for whom the card is created.
   * @param playerPlace player's place for whom the card is created.
   */
  public PlayerScoreCard(DataCardsContainerPanel inputDataPanel, String playerName, Place playerPlace) {
    this.inputDataPanel = inputDataPanel;

    this.setLayout(new TableLayout(new double[][]{
        {TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL},  // columns
        {TableLayout.FILL, TableLayout.PREFERRED, 18, TableLayout.PREFERRED, 10,
            TableLayout.PREFERRED, TableLayout.FILL}})); // rows

    this.playerName = playerName;
    this.playerPlace = playerPlace;

    this.whistFields = new HashMap<>();
    this.whistLabels = new HashMap<>();

    // Pool and mount labels and text input fields.
    this.add(this.initializeScoresUi(), new TableLayoutConstraints(
        1, 1, 1, 1, TableLayout.CENTER, TableLayout.CENTER));

    // Whist label.
    this.add(new JLabel(LocaleExt.getString("pref.dialog.whistsFor")), new TableLayoutConstraints(
        1, 3, 1, 3, TableLayout.CENTER, TableLayout.CENTER));

    // Whists on players text input fields.
    this.add(this.initializeWhistsUi(), new TableLayoutConstraints(
        1, 5, 1, 5, TableLayout.CENTER, TableLayout.CENTER));
  }

  /**
   * Gets the unique id of this card.
   *
   * @return the card unique string id.
   */
  public String getId() {
    return this.getClass().getSimpleName() + this.playerPlace.name();
  }

  /** {@inheritDoc} */
  @Override
  public String getTitle() {
    return LocaleExt.getString("pref.dialog.namePrefix",
        LocaleExt.getString(this.playerPlace.longKey, " (" + this.playerName + ")"));
  }

  /** {@inheritDoc} */
  @Override
  public void focusFirstField() {
    this.mountField.requestFocus();
  }

    /** {@inheritDoc} */
  @Override
  public @Null String validateFields(boolean isForward) {
    if (!UiService.validateIntTextField(this.mountField)) {
      this.mountField.requestFocus();
      return LocaleExt.getString("pref.dialog.errorLabel.int");
    } else if (!UiService.validateIntTextField(this.poolField)) {
      this.poolField.requestFocus();
      return LocaleExt.getString("pref.dialog.errorLabel.int");
    } else {
      for (JTextField fieldWhist : this.whistFields.values()) {
        if (!UiService.validateIntTextField(fieldWhist)) {
          fieldWhist.requestFocus();
          return LocaleExt.getString("pref.dialog.errorLabel.int");
        }
      }
    }
    return null;
  }

  /** Does nothing. */
  @Override
  public void doOnEntry() {
    // Setting the current player's name.
    this.playerName = this.inputDataPanel.getPlayerName(this.playerPlace);

    // Setting players' names on the whists labels.
    this.whistLabels.forEach((key, value) -> {
      String placeNameStr = LocaleExt.getString(key.longKey,
          " (" + this.inputDataPanel.getPlayerName(key) + "): ");
      value.setText(placeNameStr);
    });
  }

  /** @inheritDoc */
  @Override
  public void doOnForwardLeave() {
    GameResultBean resultBean = PrefCountRegistry.getInstance().getGameResultBean();
    PlayerStatistics stats = resultBean.getPlayerStats().get(this.playerPlace);
    stats.setMountainFromField(this.mountField);
    stats.setPoolFromField(this.poolField);
    for (Map.Entry<Place, JTextField> entry : this.whistFields.entrySet()) {
      stats.setWhistsForPlayerFromField(entry.getKey(), entry.getValue());
    }
    PrefCountRegistry.getInstance().getMainWindow().repaint();
  }

  /** @inheritDoc */
  @Override
  public void doOnBackwardLeave() {
  }

  /**
   * Determines if user entered some scores data.
   *
   * @return true if any data is entered on a player scorecard; false if otherwise.
   */
  @Override
  public boolean isSomeDataEntered() {
    if (StringUtils.isNotBlank(this.mountField.getText())) {
      return true;
    }
    if (StringUtils.isNotBlank(this.poolField.getText())) {
      return true;
    }
    for (Map.Entry<Place, JTextField> entry : this.whistFields.entrySet()) {
      if (StringUtils.isNotBlank(entry.getValue().getText())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Initializes the players scores UI (mount and pool).
   */
  private JPanel initializeScoresUi() {
    JPanel scoresPanel = new JPanel();

    JLabel mountLabel = new JLabel(LocaleExt.getString("pref.dialog.mount"));
    scoresPanel.add(mountLabel);

    this.mountField = new JTextField();
    this.mountField.setColumns(3);
    this.mountField.setHorizontalAlignment(SwingConstants.RIGHT);
    scoresPanel.add(this.mountField);
    scoresPanel.add(Box.createRigidArea(new Dimension(5, 1)));

    JLabel poolLabel = new JLabel(LocaleExt.getString("pref.dialog.pool"));
    scoresPanel.add(poolLabel);

    this.poolField = new JTextField();
    this.poolField.setColumns(3);
    this.poolField.setHorizontalAlignment(SwingConstants.RIGHT);
    scoresPanel.add(this.poolField);

    return scoresPanel;
  }

  /**
   * Initializes the whists UI.
   *
   * @return UI to input whist values for other players.
   */
  private JPanel initializeWhistsUi() {
    JPanel whistsPanel = new JPanel();

    for (Place whistPlace : Place.getOtherPlayersWhistPlaces(
        this.playerPlace, this.inputDataPanel.getNumberOfPlayers())) {

      JLabel nameLabel = new JLabel();
      whistsPanel.add(nameLabel);
      this.whistLabels.put(whistPlace, nameLabel);

      JTextField inputField = new JTextField();
      inputField.setColumns(3);
      inputField.setHorizontalAlignment(SwingConstants.RIGHT);
      whistsPanel.add(inputField);
      whistsPanel.add(Box.createRigidArea(new Dimension(5, 1)));
      this.whistFields.put(whistPlace, inputField);
    }
    return whistsPanel;
  }
}
