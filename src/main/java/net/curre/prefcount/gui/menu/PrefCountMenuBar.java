/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as 
 * published by the Free Software Foundation;
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

  /** Enumeration that represents type of a PrefCountMenuBar. */
  static enum MenuBarType {

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
