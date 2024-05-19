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

import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.bean.GameResultBean;
import net.curre.prefcount.bean.PlayerStatistics;
import net.curre.prefcount.gui.aa.AAJLabel;
import net.curre.prefcount.gui.aa.AAJTextField;
import net.curre.prefcount.gui.type.Place;
import net.curre.prefcount.util.LocaleExt;
import net.curre.prefcount.util.Utilities;
import static net.curre.prefcount.util.Utilities.FieldType.INTEGER;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstraints;

/**
 * Object of this class represents a player data
 * panel, where all player scores are entered.
 * <p/>
 * Created date: Apr 3, 2007
 *
 * @author Yevgeny Nyden
 */
public class PlayerDataPanel extends DialogInnerPanel {

  /** String for the player name. */
  private String playerName = "";

  /** Reference to the mount text field. */
  protected JTextField mountField;

  /** Reference to the pool text field. */
  protected JTextField poolField;

  /** Reference to the whists text fields. */
  protected Map<Place, JTextField> whistFields;

  /** Reference to the whists label fields. */
  private final HashMap<Place, JLabel> whistLabels;

  /** Player's place in the game. */
  private final Place playerPlace;

  /**
   * Current error field index - field that
   * failed validation (null if none).
   */
  private JTextField currErrorField;

  /** Reference to the parent dialog frame. */
  private final PlayerDialogBaseFrame dialogWindow;

  /**
   * Constructor.
   *
   * @param dialogWindow Reference to the dialog (parent) window.
   * @param numPlayers   Number of players in the game.
   * @param playerPlace  Player's place in the game.
   */
  public PlayerDataPanel(PlayerDialogBaseFrame dialogWindow,
                         int numPlayers, Place playerPlace) {
    super(null, PanelPosition.MIDDLE);
    this.dialogWindow = dialogWindow;
    this.playerPlace = playerPlace;
    this.whistFields = new HashMap<>();
    this.whistLabels = new HashMap<>();

    TableLayout layout = new TableLayout(new double[][]{
        {TableLayout.PREFERRED, 90d, 4d, 50d, 4d, 30d, TableLayout.PREFERRED},
        {11d, TableLayout.PREFERRED, TableLayout.PREFERRED, 30d, TableLayout.PREFERRED,
         TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED}});
    this.setLayout(layout);
    layout.setHGap(5);
    layout.setVGap(5);
    this.add(new JPanel(null), new TableLayoutConstraints(2, 0, 2, 0,
                                                          TableLayoutConstraints.FULL,
                                                          TableLayoutConstraints.FULL));

    // ---- mount label and text field ----
    this.mountField = createFieldsHelper("pref.dialog.mount", 1, null);

    // ---- pool label and text field ----
    this.poolField = createFieldsHelper("pref.dialog.pool", 2, null);

    // ---- whists header label ----
    JLabel whistsLabel = new JLabel(LocaleExt.getString("pref.dialog.whistsFor"));
    whistsLabel.setHorizontalAlignment(SwingConstants.CENTER);
    LocaleExt.registerComponent(whistsLabel, "pref.dialog.whistsFor");
    this.localeSensitiveComps.add(whistsLabel);
    this.add(whistsLabel, new TableLayoutConstraints(0, 3, 6, 3,
                                                     TableLayoutConstraints.CENTER,
                                                     TableLayoutConstraints.CENTER));

    // ---- whists labels and text fields ----
    int fieldRow = 4;
    for (Place whistPlace : Place.getOtherPlayersWhistPlaces(this.playerPlace, numPlayers)) {
      createFieldsHelper(null, fieldRow++, whistPlace);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void focusFirstInputField() {
    if (this.currErrorField == null) {
      this.mountField.requestFocus();
    } else {
      this.currErrorField.requestFocus();
    }
  }

  /**
   * Validates numeric fields (mount, pool, whists);
   * if any field is not valid, an error message will
   * be displayed at the bottom of the panel.
   *
   * @return True if all numeric fields are valid; false otherwise.
   */
  @Override
  public boolean validateFields() {
    currErrorField = null;
    if (!Utilities.validateTextField(mountField, INTEGER)) {
      currErrorField = mountField;
    } else if (!Utilities.validateTextField(poolField, INTEGER)) {
      currErrorField = poolField;
    } else {
      for (JTextField fieldWhist : whistFields.values()) {
        if (!Utilities.validateTextField(fieldWhist, INTEGER)) {
          currErrorField = fieldWhist;
        }
      }
    }
    if (currErrorField == null) {
      dialogWindow.toggleErrorField(null);
      return true;
    } else {
      currErrorField.requestFocus(); // need to try transfering focus
      dialogWindow.toggleErrorField("pref.dialog.errorLabel.int");
      return false;
    }
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
    GameResultBean resultBean = PrefCountRegistry.getInstance().getGameResultBean();
    PlayerStatistics stats = resultBean.getPlayerStats().get(this.playerPlace);
    stats.setMountainFromField(this.mountField);
    stats.setPoolFromField(this.poolField);
    for (Map.Entry<Place, JTextField> entry : this.whistFields.entrySet()) {
      stats.setWhistsForPlayerFromField(entry.getKey(), entry.getValue());
    }
  }

  /** {@inheritDoc} */
  @Override
  public void setHeaderMessage(JLabel messageLabel) {
    String header = this.playerName + " (" + LocaleExt.getString(this.playerPlace.shortKey) + ")";
    messageLabel.setText(LocaleExt.getString("pref.dialog.namePrefix", header));
    LocaleExt.reregisterComponent(messageLabel, "pref.dialog.namePrefix", header);
  }

  /**
   * Adjusts players names on the panel.
   *
   * @param playersNames map with players names.
   */
  public void adjustPlayersNames(Map<Place, String> playersNames) {
    // setting the current player's name
    this.playerName = playersNames.get(this.playerPlace);

    // setting players' names on the whists labels
    for (Map.Entry<Place, JLabel> entry : this.whistLabels.entrySet()) {
      String nameStr = playersNames.get(entry.getKey()) + ":";
      entry.getValue().setText(nameStr);
    }
  }

  /**
   * Creates a label and a text filed and adds it to the panel.
   *
   * @param textKey Label message key or null if this label doesn't have a key.
   * @param row     Table row.
   * @param place   whist place associate with created label and field,
   *                or null when creating non-whist fields.
   * @return Created text field.
   */
  private JTextField createFieldsHelper(String textKey, int row, final Place place) {
    JLabel label = new AAJLabel();
    if (textKey != null) {
      label.setText(LocaleExt.getString(textKey));
      LocaleExt.registerComponent(label, textKey);
      this.localeSensitiveComps.add(label);
    } else {
      label.setText("placeholder");
    }
    label.setHorizontalAlignment(SwingConstants.RIGHT);

    this.add(label, new TableLayoutConstraints(1, row, 1, row,
                                               TableLayoutConstraints.FULL,
                                               TableLayoutConstraints.FULL));

    final JTextField field = new AAJTextField();
    field.setHorizontalAlignment(SwingConstants.RIGHT);
    this.add(field, new TableLayoutConstraints(3, row, 3, row,
                                               TableLayoutConstraints.FULL,
                                               TableLayoutConstraints.FULL));
    if (place != null) {
      this.whistLabels.put(place, label);
      this.whistFields.put(place, field);


      final JLabel whistPlaceLabel = new AAJLabel("(" + LocaleExt.getString(place.shortKey) + ")");
      this.add(whistPlaceLabel, new TableLayoutConstraints(5, row, 5, row,
                                                           TableLayoutConstraints.FULL,
                                                           TableLayoutConstraints.FULL));
      LocaleExt.registerComponent(new LocaleExt.LocaleExec() {
        public void doChange() {
          whistPlaceLabel.setText("(" + LocaleExt.getString(place.shortKey) + ")");
        }
      }, "WHIST_LABEL_" + row + "_" + place.name() + "_" + (int) (Math.random() * 10));
      // note, that the string we just constructed must be a unique key
      // that's why we add a random number here, to insure that multiple whists
      // will have different keys
    }

    return field;
  }
}
