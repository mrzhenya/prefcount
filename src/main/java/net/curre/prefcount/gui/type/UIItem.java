/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as 
 * published by the Free Software Foundation;
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
