/**
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

import java.awt.CheckboxMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import net.curre.prefcount.gui.theme.skin.PrefSkin;
import net.curre.prefcount.service.LafThemeService;
import net.curre.prefcount.service.SettingsService;

/**
 * Object of this class represents a listener
 * for a L&F menu item (a <code>CheckboxMenuItem</code> object).
 * <p/>
 * Created date: Jan 22, 2008
 *
 * @author Yevgeny Nyden
 */
public class LafMenuItemListener implements ActionListener, ItemListener {

  /** The Pref skin this listener is for. */
  private PrefSkin skin;

  /**
   * Constructor that sets the skin this  listener is for.
   *
   * @param skin Pref skin to set.
   */
  public LafMenuItemListener(PrefSkin skin) {
    this.skin = skin;
  }

  /**
   * Changes the current L&F skin to the listener's skin
   * (this.skin). This ItemListener method is for awt menu only.
   * <p/>
   * {@inheritDoc}
   */
  public void itemStateChanged(ItemEvent itemEvent) {
    if (itemEvent.getStateChange() == ItemEvent.DESELECTED) {
      ((CheckboxMenuItem) itemEvent.getSource()).setState(true);
    }
    changeSkin();
  }

  /**
   * Changes the current L&F skin to the listener's skin
   * (this.skin). This ActionListener method is for swing
   * menu only.
   * <p/>
   * {@inheritDoc}
   */
  public void actionPerformed(ActionEvent actionEvent) {
    changeSkin();
  }

  /** Helper method that performs LAF skin change. */
  private void changeSkin() {
    LafThemeService.getInstance().setLookAndFeel(skin.getNameResourceKey(), false);
    SettingsService.updateSkin(skin);
  }

}
