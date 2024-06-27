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
import net.curre.prefcount.gui.type.Place;
import net.curre.prefcount.util.LocaleExt;
import org.apache.commons.lang3.StringUtils;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.validation.constraints.Null;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a panel used to display player names inputs.
 *
 * @author Yevgeny Nyden
 */
public class PlayerNamesCard extends DataCard {

  /** Reference to the game options cards panel (which contains all the data input fields). */
  private final DataCardsContainerPanel inputDataPanel;

  /** Map of references to the player names fields. */
  private final Map<Place, JTextField> playersFields;

  /** Map of player names mapped to their places. */
  private final Map<Place, String> playerNamesMap;

  /**
   * Ctor.
   *
   * @param inputDataPanel reference to the container panel.
   */
  public PlayerNamesCard(DataCardsContainerPanel inputDataPanel) {
    this.inputDataPanel = inputDataPanel;
    this.playersFields = new HashMap<>();
    this.playerNamesMap = new HashMap<>();

    this.initializePanel();
  }

  /** {@inheritDoc} */
  @Override
  public String getTitle() {
    return LocaleExt.getString("pref.dialog.playerNames.message");
  }

  /** {@inheritDoc} */
  @Override
  public void focusFirstField() {
    for (Component c : this.getComponents()) {
      if (c instanceof JTextField && c.isVisible()) {
        c.requestFocus();
        return;
      }
    }
  }

  /** {@inheritDoc} */
  @Override
  public @Null String validateFields(boolean isForward) {
    if (isForward) {
      for (Place place : Place.getPlaces(this.inputDataPanel.getNumberOfPlayers())) {
        JTextField inputField = this.playersFields.get(place);
        if (StringUtils.isBlank(inputField.getText())) {
          inputField.requestFocus();
          return LocaleExt.getString("pref.dialog.errorLabel.playerNames");
        }
      }
    }
    return null;
  }

  /**
   * Adjusts the number of input fields based on the currently selected players number.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public void doOnEntry() {
    this.playersFields.clear();
    this.removeAll();
    this.initializePanel();
  }

  /** {@inheritDoc} */
  @Override
  public void doOnForwardLeave() {
    this.refreshPlayerNamesMap();

    // Refreshing the players stats in the result bean.
    GameResultBean resultBean = PrefCountRegistry.getInstance().getGameResultBean();
    resultBean.updateNumberOfPlayers(this.playerNamesMap);
  }

  /** @inheritDoc */
  @Override
  public void doOnBackwardLeave() {
    this.refreshPlayerNamesMap();
  }

  /**
   * Determines if user entered some names data.
   *
   * @return true if any data is entered; false if otherwise.
   */
  @Override
  public boolean isSomeDataEntered() {

    // TODO - only return true if changing from 4 to 3 players.

    for (JTextField field : this.playersFields.values()) {
      if (StringUtils.isNotBlank(field.getText())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Gets the name of a player in a requested place.
   *
   * @param place place of the player to get the name for
   * @return player's name
   */
  public String getPlayerName(Place place) {
    JTextField inputField = this.playersFields.get(place);
    return inputField.getText().trim();
  }

  /**
   * Initializes the UI component of this panel.
   */
  private void initializePanel() {
    this.setLayout(new TableLayout(new double[][]{
        {TableLayout.FILL, TableLayout.PREFERRED, 12, TableLayout.PREFERRED, TableLayout.FILL},  // columns
        {TableLayout.FILL, TableLayout.PREFERRED, 12, TableLayout.PREFERRED, 12,
            TableLayout.PREFERRED, 12, TableLayout.PREFERRED, TableLayout.FILL}})); // rows

    // Create input fields for all 4 players by default.
    int ind = 0;
    for (Place place : Place.getPlaces(this.inputDataPanel.getNumberOfPlayers())) {
      createPlayerRow(place, ind);
      ind++;
    }
  }

  /**
   * Creates a player row UI and adds it to 'this' panel.
   *
   * @param place place of the player.
   * @param ind index of the player.
   */
  private void createPlayerRow(Place place, int ind) {
    JLabel label = new JLabel(LocaleExt.getString(place.longKey, ":"));
    label.setHorizontalAlignment(SwingConstants.RIGHT);
    final int row = 2 * ind + 1;
    this.add(label, new TableLayoutConstraints(1, row, 1, row, TableLayout.FULL, TableLayout.FULL));

    JTextField field = new JTextField();
    field.setColumns(7);
    field.setFocusable(true);
    String name = this.playerNamesMap.get(place);
    if (name != null) {
      field.setText(name);
    }
    this.add(field, new TableLayoutConstraints(3, row, 3, row, TableLayout.FULL, TableLayout.FULL));
    this.playersFields.put(place, field);
  }

  /**
   * Refreshes the player names map.
   */
  private void refreshPlayerNamesMap() {
    this.playerNamesMap.clear();
    for (Place place : Place.getPlaces(this.inputDataPanel.getNumberOfPlayers())) {
      String playerName = this.playersFields.get(place).getText().trim();
      this.playerNamesMap.put(place, playerName);
    }
  }
}
