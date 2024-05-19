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

package net.curre.prefcount.gui.menu;

/**
 * This is interface for the prefcount menu bar.
 * <p/>
 * Created date: Jul 28, 2007
 *
 * @author Yevgeny Nyden
 */
public interface PrefCountMenuBar {

  /** Enumeration that represents type of PrefCountMenuBar. */
  enum MenuBarType {
    MAIN_WINDOW, PLAYER_DIALOG
  }

  /** Resets menu language icon. */
  void refreshLanguageIcon();

  /**
   * Enables/disables the next menu item on the action menu.
   *
   * @param enabled True when the menu item should be enabled; false if disabled.
   * @throws UnsupportedOperationException If action menu does not exist.
   */
  void toggleNextAction(boolean enabled);

  /**
   * Enables/disables the back menu item on the action menu.
   *
   * @param enabled True when the menu item should be enabled; false if disabled.
   * @throws UnsupportedOperationException If action menu does not exist.
   */
  void toggleBackAction(boolean enabled);

  /**
   * Enables/disables the compute menu item on the action menu.
   *
   * @param enabled True when the menu item should be enabled; false if disabled.
   * @throws UnsupportedOperationException If action menu does not exist.
   */
  void toggleComputeAction(boolean enabled);

  /**
   * Sets the selected status of the
   * dialog frame menu bar menu item.
   *
   * @param isSelected true if the item should be selected; false otherwise.
   */
  void setDialogFrameItemState(boolean isSelected);
}
