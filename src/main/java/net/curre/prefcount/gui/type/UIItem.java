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

package net.curre.prefcount.gui.type;

/**
 * This interface provides a common way to access text and
 * shortcut attributes of a UI item (i.e. button, menu item, etc.).
 * <p/>
 * Created date: May 8, 2008
 *
 * @author Yevgeny Nyden
 */
public interface UIItem {

  /**
   * Gets the item text resource key.
   *
   * @return the item text resource key.
   */
  String getTextKey();

  /**
   * Gets the item shortcut resource key.
   *
   * @return the item shortcut resource key or null if there
   *         is no shortcut for this item.
   */
  String getShortcutKey();

  /**
   * Gets the item shortcut index resource key. This is the
   * index of the shortcut letter in the item's text string.
   * Note, that "-1" indicates that the item's shortcut does
   * not appear in the text string.
   *
   * @return the item shortcut index resource key or null
   *         if there is no shortcut for this item.
   */
  String getShortcutIndexKey();
}
