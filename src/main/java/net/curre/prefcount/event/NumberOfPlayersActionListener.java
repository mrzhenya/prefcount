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

package net.curre.prefcount.event;

import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.bean.Settings;
import net.curre.prefcount.gui.game.DataCardsContainerPanel;
import net.curre.prefcount.service.UiService;

import javax.swing.JRadioButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Action listener to handle number of players options change.
 *
 * @author Yevgeny Nyden
 */
public class NumberOfPlayersActionListener implements ActionListener {

  /** Reference to the "3 players" radio button. */
  private final JRadioButton players3Button;

  /** Reference to the game options cards panel (which contains all the data input fields). */
  private final DataCardsContainerPanel inputDataPanel;

  public NumberOfPlayersActionListener(JRadioButton players3Button, DataCardsContainerPanel inputDataPanel) {
    this.players3Button = players3Button;
    this.inputDataPanel = inputDataPanel;
  }

  /** @inheritDoc */
  @Override
  public void actionPerformed(ActionEvent e) {
    PrefCountRegistry registry = PrefCountRegistry.getInstance();
    Settings settings = registry.getSettingsService().getSettings();
    boolean currPlayers3Selected = settings.getNumberOfPlayers() == 3;
    boolean newPlayers3Selected = this.players3Button.isSelected();

    if (currPlayers3Selected == newPlayers3Selected) {
      // No change, nothing to do.
      return;
    }

    // Tell the user, changing players number will erase the existing data.
    if (this.inputDataPanel.isSomeDataEntered()) {
      if (!UiService.displayOkCancelMessage("pref.dialog.warn.resetData",
          "pref.dialog.buttons.yes", "pref.dialog.buttons.cancel")) {
        return;
      }
    }

    // Update the new players number.
    int numberOfPlayers = newPlayers3Selected ? 3 : 4;
    settings.setNumberOfPlayers(numberOfPlayers);
    registry.getGameResultBean().resetNumberOfPlayers(numberOfPlayers);
    this.inputDataPanel.updateNumberOfPlayers(numberOfPlayers);
  }
}
