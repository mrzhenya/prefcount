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
import net.curre.prefcount.gui.type.PrefType;

import javax.swing.JRadioButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Action listener to handle preferance type options change.
 *
 * @author Yevgeny Nyden
 */
public class PrefTypeActionListener implements ActionListener {

  /** Reference to the Leningradka radio button. */
  private final JRadioButton leningradkaButton;

  public PrefTypeActionListener(JRadioButton leningradkaButton) {
    this.leningradkaButton = leningradkaButton;
  }

  /** @inheritDoc */
  @Override
  public void actionPerformed(ActionEvent e) {
    PrefCountRegistry registry = PrefCountRegistry.getInstance();
    Settings settings = registry.getSettingsService().getSettings();
    boolean currIsLeningradka = settings.getPrefType() == PrefType.LENINGRAD;
    boolean newIsLeningradka = this.leningradkaButton.isSelected();

    if (currIsLeningradka == newIsLeningradka) {
      // No change, nothing to do.
      return;
    }

    settings.setPrefType(newIsLeningradka ? PrefType.LENINGRAD : PrefType.SOCHI);
    registry.getGameResultBean().setLeningradka(newIsLeningradka);
  }
}
