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

package net.curre.prefcount.gui;

import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import net.curre.prefcount.gui.aa.AAJLabel;
import net.curre.prefcount.gui.aa.AAJTextField;
import net.curre.prefcount.gui.type.Place;
import net.curre.prefcount.util.LocaleExt;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstraints;
import org.apache.commons.lang3.StringUtils;

/**
 * Object of this class represents a players names panel -
 * the first panel, where player names/acronyms are entered.
 * <p/>
 * Created date: Apr 8, 2007
 *
 * @author Yevgeny Nyden
 */
public class PlayersNamesPanel extends DialogInnerPanel {

  /** Map of references to the player names fields. */
  protected Map<Place, JTextField> playersFields;

  /** Reference to the parent dialog frame. */
  private final PlayerDialogBaseFrame dialogWindow;

  /**
   * Current error field index - field that
   * failed validation (null if none).
   */
  private JTextField currErrorField;

  /**
   * Constructor.
   *
   * @param dialogWindow Reference to the dialog window/frame.
   * @param numPlayers   Number of players in the game.
   * @throws IllegalArgumentException If number of players is not supported.
   */
  public PlayersNamesPanel(PlayerDialogBaseFrame dialogWindow, int numPlayers) {
    super("pref.dialog.playerNames.message", PanelPosition.FIRST);

    if (numPlayers != 3 && numPlayers != 4) {
      throw new IllegalArgumentException("Player names panel can only handle 3 or 4 players right now!");
    }

    this.dialogWindow = dialogWindow;
    this.playersFields = new HashMap<>();

    setLayout(new TableLayout(new double[][]{
        {TableLayout.FILL, TableLayout.PREFERRED, 12, TableLayout.PREFERRED, TableLayout.FILL},
        {TableLayout.FILL, TableLayout.PREFERRED, 12, TableLayout.PREFERRED, 12, TableLayout.PREFERRED, 12, TableLayout.PREFERRED, TableLayout.FILL}}));

    createNameFieldsHelper(numPlayers);
  }

  /** {@inheritDoc} */
  @Override
  public void focusFirstInputField() {
    if (this.currErrorField == null) {
      this.playersFields.get(Place.getPlaceForIndex(0)).requestFocus();
    } else {
      this.currErrorField.requestFocus();
    }
  }

  /**
   * Validates players names fields.
   *
   * @return True if all players names are entered; false otherwise.
   */
  @Override
  public boolean validateFields() {
    for (JTextField fieldName : this.playersFields.values()) {
      if (StringUtils.isBlank(fieldName.getText())) {
        this.dialogWindow.toggleErrorField("pref.dialog.errorLabel.playerNames");
        this.currErrorField = fieldName;
        fieldName.requestFocus(); // need to try to transfer focus from the Next button
        return false;
      }
    }
    this.dialogWindow.toggleErrorField(null);
    return true;
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
  public void doOnLeave() {
    // creating a list of players names/acronyms
    Map<Place, String> playersNames = new HashMap<>();
    for (Map.Entry<Place, JTextField> entry : this.playersFields.entrySet()) {
      playersNames.put(entry.getKey(), entry.getValue().getText().trim());
    }

    // setting/changing players names on the players input panels
    for (int i = 1; i <= this.playersFields.size(); ++i) {
      PlayerDataPanel panel = (PlayerDataPanel) this.dialogWindow.questionsPane.getComponent(i);
      panel.adjustPlayersNames(playersNames);
    }

    // setting players names in the game results bean
    this.dialogWindow.setPlayersNames(playersNames);
    this.dialogWindow.refreshTable();
  }

  /**
   * Checks if there is some data entered into the
   * player names panel.
   *
   * @return true if at least one player name is not blank; false otherwise.
   */
  public boolean isSomeDataEntered() {
    for (JTextField field : this.playersFields.values()) {
      if (StringUtils.isNotBlank(field.getText())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Helper method for creating and adding
   * player name label and text field components.
   *
   * @param numPlayers number of players in the game.
   */
  private void createNameFieldsHelper(int numPlayers) {
    Place[] sortedByIndex = new Place[numPlayers];
    for (Place place : Place.getPlaces(numPlayers)) {
      sortedByIndex[place.index] = place;
    }
    for (Place place : sortedByIndex) {
      JLabel label = new AAJLabel(LocaleExt.getString(place.longKey, ":"));
      LocaleExt.registerComponent(label, place.longKey, ":");
      label.setHorizontalAlignment(SwingConstants.RIGHT);
      final int row = 2 * place.index + 1;
      add(label, new TableLayoutConstraints(1, row, 1, row, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
      super.localeSensitiveComps.add(label);

      JTextField field = new AAJTextField();
      field.setColumns(7);
      field.setFont(new Font("Arial Black", Font.PLAIN, 12));
      field.setFocusable(true);
      add(field, new TableLayoutConstraints(3, row, 3, row, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
      this.playersFields.put(place, field);
    }
  }

}
