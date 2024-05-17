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

package net.curre.prefcount.gui.menu;

import javax.swing.*;
import java.awt.CheckboxMenuItem;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Object of this class represents a checkbox menu item
 * group to provide radio-buton-like functionality for awt
 * <code>CheckboxMenuItem</code>.
 * <p/>
 * Created date: Jan 23, 2008
 *
 * @author Yevgeny Nyden
 */
public class AwtCheckboxMenuGroup {

  /** List of checkbox items included in this group. */
  private List<CheckboxMenuItem> menuItems;

  /** Default constructor. */
  public AwtCheckboxMenuGroup() {
    this.menuItems = new ArrayList<CheckboxMenuItem>();
  }

  /**
   * Adds a given checkbox menu item to the current group.
   *
   * @param item Item to add to the current group.
   */
  public void addItemToGroup(CheckboxMenuItem item) {
    this.menuItems.add(item);
    GroupItemListener itemListener = new GroupItemListener(this.menuItems.size() - 1);
    item.addItemListener(itemListener);
    item.addActionListener(itemListener);
  }

  /** Listener for the group's checkbox items. */
  private class GroupItemListener implements ItemListener, ActionListener {

    /** Index of the current item on the group's list. */
    private int index;

    /**
     * Constructor that sets the index of the current item.
     *
     * @param index Index to set.
     */
    public GroupItemListener(int index) {
      this.index = index;
    }

    /** {@inheritDoc} */
    public void itemStateChanged(ItemEvent itemEvent) {
      if (itemEvent.getStateChange() == ItemEvent.DESELECTED) {
        menuItems.get(index).setState(true);
      }
      unselectItemsHelper();
    }

    /** {@inheritDoc} */
    public void actionPerformed(ActionEvent event) {
      unselectItemsHelper();
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          menuItems.get(index).setState(true);
        }
      });
    }

    /** Does all the work for unselecting items other than the current one. */
    private void unselectItemsHelper() {
      for (int i = 0; i < menuItems.size(); ++i) {
        if (i != index) {
          menuItems.get(i).setState(false);
        }
      }
    }
  }

}

