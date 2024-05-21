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

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import java.awt.CheckboxMenuItem;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import net.curre.prefcount.event.MainController;
import net.curre.prefcount.gui.aa.AAJButton;
import net.curre.prefcount.gui.type.WindowComponent;
import static net.curre.prefcount.gui.type.WindowComponent.DIALOG_FORWARD2;

import net.curre.prefcount.service.UiService;
import net.curre.prefcount.util.LocaleExt;
import net.curre.prefcount.util.PlatformType;

/**
 * Object of this class represents menu items bean to
 * coordinate action events among menu bar menu items and
 * option panel radio buttons.
 * <p/>
 * Created date: Apr 9, 2008
 *
 * @author Yevgeny Nyden
 */
public class MenuItemsBean {

  /** Action listener to listen on all item action events. */
  private ActionListener actionListener;

  /** Map with radio button menu items. */
  private final Map<WindowComponent, JRadioButtonMenuItem> menuBarRadioItems;

  /** Map with awt check box menu items. */
  private final Map<WindowComponent, CheckboxMenuItem> menuBarCheckBoxItems;

  /** Menu bar menu items map. */
  private final Map<WindowComponent, JMenuItem> menuBarMenuItems;

  /** Awt menu bar menu items map. */
  private final Map<WindowComponent, MenuItem> menuAwtBarMenuItems;

  /** Menu bar groups map. */
  private final Map<String, ButtonGroup> menuBarGroups;

  /** Awt menu bar groups map. */
  private final Map<String, AwtCheckboxMenuGroup> menuBarAwtGroups;

  /** Radio buttons map. */
  private final Map<WindowComponent, JRadioButton> radioButtons;

  /** JButtons map. */
  private final Map<WindowComponent, JButton> jButtons;

  /** Radio buttons groups map. */
  private final Map<String, ButtonGroup> radioButtonGroups;

  /** Constructs a new menu items bean object. */
  public MenuItemsBean() {
    this.menuBarRadioItems = new HashMap<>();
    this.menuBarMenuItems = new HashMap<>();
    this.menuAwtBarMenuItems = new HashMap<>();
    this.menuBarCheckBoxItems = new HashMap<>();
    this.menuBarGroups = new HashMap<>();
    this.menuBarAwtGroups = new HashMap<>();
    this.radioButtons = new HashMap<>();
    this.radioButtonGroups = new HashMap<>();
    this.jButtons = new HashMap<>();
  }

  /**
   * Setter for the main items action listener. Note that when this
   * action triggers, the event's source will be set to the corresponding
   * item's <code>WindowComponent</code> enum.
   *
   * @param actionListener action listener to set.
   */
  public void setActionListener(ActionListener actionListener) {
    this.actionListener = actionListener;
  }

  /**
   * Fetches a <code>JRadioButtonMenuItem</code> menu item
   * for the given item enumeration. The item will be created
   * if it does not exist.
   *
   * @param itemEnum item enumeration that represents the required item.
   * @return radio button menu item that corresponds to the item enum.
   */
  public JRadioButtonMenuItem getJRadioButtonMenuItem(WindowComponent itemEnum) {
    JRadioButtonMenuItem item = this.menuBarRadioItems.get(itemEnum);
    if (item == null) {
      String text = LocaleExt.getString(itemEnum.getTextKey());
      item = new JRadioButtonMenuItem(text);
      LocaleExt.registerComponent(item, itemEnum);
      item.addActionListener(new ItemActionListener(itemEnum));
      this.menuBarRadioItems.put(itemEnum, item);

/*
      if (itemEnum.shortcutKey != null) {
        item.setToolTipText(LocaleExt.getString(itemEnum.shortcutKey));
        ToolTipManager.sharedInstance().registerComponent(item);
      }
*/

      if (itemEnum.groupKey != null) {
        ButtonGroup group = this.menuBarGroups.get(itemEnum.groupKey);
        if (group == null) {
          group = new ButtonGroup();
          this.menuBarGroups.put(itemEnum.groupKey, group);
        }
        group.add(item);
      }

      if (itemEnum.getShortcutKey() != null) {
        item.setAccelerator(KeyStroke.getKeyStroke(
            LocaleExt.getString(itemEnum.getShortcutKey()).charAt(0), InputEvent.CTRL_MASK));
      }
    }
    return item;
  }

  /**
   * Fetches a <code>CheckboxMenuItem</code> menu item
   * for the given item enumeration. The item will be created
   * if it does not exist.
   *
   * @param itemEnum item enumeration that represents the required item.
   * @return awt check box button menu item that corresponds to the item enum.
   */
  public CheckboxMenuItem getRadioButtonMenuItem(WindowComponent itemEnum) {

    CheckboxMenuItem item = this.menuBarCheckBoxItems.get(itemEnum);
    if (item == null) {
      String text = LocaleExt.getString(itemEnum.getTextKey());
      item = new CheckboxMenuItem(text);
      LocaleExt.registerComponent(item, itemEnum);
      item.addItemListener(new ItemActionListener(itemEnum));
      this.menuBarCheckBoxItems.put(itemEnum, item);

/*
      if (itemEnum.shortcutKey != null) {
        item.setToolTipText(LocaleExt.getString(itemEnum.shortcutKey));
        ToolTipManager.sharedInstance().registerComponent(item);
      }
*/

      if (itemEnum.groupKey != null) {
        AwtCheckboxMenuGroup group = this.menuBarAwtGroups.get(itemEnum.groupKey);
        if (group == null) {
          group = new AwtCheckboxMenuGroup();
          this.menuBarAwtGroups.put(itemEnum.groupKey, group);
        }
        group.addItemToGroup(item);
      }

      if (itemEnum.getShortcutKey() != null) {
        item.setShortcut(new MenuShortcut(LocaleExt.getString(itemEnum.getShortcutKey()).charAt(0), false));
      }
    }
    return item;
  }

  /**
   * Fetches a <code>JRadioButton</code> radio button
   * for the given item enumeration. The item will be created
   * if it does not exist.
   *
   * @param itemEnum item enumeration that represents the required item.
   * @return radio button that corresponds to the item enum.
   */
  public JRadioButton getJRadioButton(WindowComponent itemEnum) {
    JRadioButton button = this.radioButtons.get(itemEnum);
    if (button == null) {
      button = new JRadioButton(UiService.generateButtonText(itemEnum));
      LocaleExt.registerComponent(button, itemEnum);
      button.addActionListener(new ItemActionListener(itemEnum));
      this.radioButtons.put(itemEnum, button);


      if (itemEnum.tooltipKey != null) {
        button.setToolTipText(LocaleExt.getString(itemEnum.tooltipKey));
      }

      if (itemEnum.groupKey != null) {
        ButtonGroup group = this.radioButtonGroups.get(itemEnum.groupKey);
        if (group == null) {
          group = new ButtonGroup();
          this.radioButtonGroups.put(itemEnum.groupKey, group);
        }
        group.add(button);
      }
    }

    return button;
  }

  /**
   * Fetches a <code>JButton</code> object for the given
   * item enumeration. The item will be created if it does
   * not exist.
   *
   * @param itemEnum item enumeration that represents the required item.
   * @param pane     pass this object to add a shortcut to it (of present on the item's enum).
   * @return button that corresponds to the item enum.
   */
  public JButton getJButton(WindowComponent itemEnum, JPanel pane) {
    JButton button = this.jButtons.get(itemEnum);
    if (button == null) {
      button = new AAJButton(UiService.generateButtonText(itemEnum));
      ItemActionListener listener = new ItemActionListener(itemEnum);
      button.addActionListener(listener);

      // win key listener produces "double click", so don't add it
      if (PlatformType.getPlatformType() != PlatformType.WINDOWS) {
        button.addKeyListener(listener);
      }
        
      final String shortcutKey = itemEnum.getShortcutKey();
      if (shortcutKey != null) {
        char mnemonicCode = LocaleExt.getString(itemEnum.getShortcutKey()).charAt(0);
        button.setMnemonic(mnemonicCode);

        // adding shortcuts if the pane object is present
        if (pane != null) {
          InputMap map = pane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
          String actionKey = LocaleExt.getString(shortcutKey);
          map.put(KeyStroke.getKeyStroke("control " + actionKey), shortcutKey);
          pane.getActionMap().put(shortcutKey, MainController.getActionForComponent(itemEnum));
          LocaleExt.registerShortcuts(map, shortcutKey);
        }
      }

      this.jButtons.put(itemEnum, button);
    }

    return button;
  }

  /**
   * Creates a JButton for the choose player dialog.
   * This is a special case because the choose player window is
   * created and disposed every time (we don't hide it).
   *
   * @param pane panel object to add a shortcut to it (required).
   * @return created button object. 
   */
  public JButton createJButtonForChoosePlayerDialog(JPanel pane) {
    WindowComponent itemEnum = DIALOG_FORWARD2;
    JButton button = new AAJButton(UiService.generateButtonText(itemEnum));
    ItemActionListener listener = new ItemActionListener(itemEnum);
    button.addActionListener(listener);

    // win key listener produces "double click", so don't add it
    if (PlatformType.getPlatformType() != PlatformType.WINDOWS) {
      button.addKeyListener(listener);
    }

    final String shortcutKey = itemEnum.getShortcutKey();
    if (shortcutKey != null) {
      char mnemonicCode = LocaleExt.getString(itemEnum.getShortcutKey()).charAt(0);
      final String shortcut = (PlatformType.isMacOs() ? "meta " : "control ") + mnemonicCode;
      pane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
          .put(KeyStroke.getKeyStroke(shortcut), "moveForward");
      pane.getActionMap().put("moveForward", listener);
    }

    return button;
  }

  /**
   * Fetches a <code>JMenuItem</code> menu item
   * for the given item enumeration. The item will be created
   * if it does not exist.
   *
   * @param itemEnum item enumeration that represents the required item.
   * @return menu item that corresponds to the item enum.
   */
  public JMenuItem getJMenuItem(WindowComponent itemEnum) {
    JMenuItem menuItem = this.menuBarMenuItems.get(itemEnum);
    if (menuItem == null) {
      String text = LocaleExt.getString(itemEnum.getTextKey());
      menuItem = new JMenuItem(text);
      LocaleExt.registerComponent(menuItem, itemEnum);
      if (itemEnum.getShortcutKey() != null) {
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
            LocaleExt.getString(itemEnum.getShortcutKey()).charAt(0), InputEvent.CTRL_MASK));
      }
      menuItem.addActionListener(new ItemActionListener(itemEnum));
      this.menuBarMenuItems.put(itemEnum, menuItem);
    }
    return menuItem;
  }

  /**
   * Fetches a <code>MenuItem</code> menu item
   * for the given item enumeration. The item will be created
   * if it does not exist.
   *
   * @param itemEnum item enumeration that represents the required item.
   * @return menu item that corresponds to the item enum.
   */
  public MenuItem getMenuItem(WindowComponent itemEnum) {
    MenuItem menuItem = this.menuAwtBarMenuItems.get(itemEnum);
    if (menuItem == null) {
      menuItem = new MenuItem(LocaleExt.getString(itemEnum.getTextKey()));
      if (itemEnum.getShortcutKey() != null) {
        menuItem.setShortcut(new MenuShortcut(LocaleExt.getString(itemEnum.getShortcutKey()).charAt(0), false));
      }
      LocaleExt.registerComponent(menuItem, itemEnum);
      menuItem.addActionListener(new ItemActionListener(itemEnum));
      this.menuAwtBarMenuItems.put(itemEnum, menuItem);
    }
    return menuItem;
  }

  /**
   * Adds listener to provided items.
   *
   * @param listener  action listener to add to the provided items.
   * @param itemEnums item enumeration array.
   */
  public void addListener(Object listener, WindowComponent... itemEnums) {
    for (WindowComponent item : itemEnums) {
      addListenerHelper(this.radioButtons.get(item), listener);
      addListenerHelper(this.jButtons.get(item), listener);
      addListenerHelper(this.menuBarRadioItems.get(item), listener);
      addListenerHelper(this.menuBarMenuItems.get(item), listener);
    }
  }

  /**
   * Sets given radio button item selected status.
   *
   * @param itemEnum   item enumeration that represents the required radio item.
   * @param isSelected true if the item should be selected; false otherwise.
   */
  public void setSelected(WindowComponent itemEnum, boolean isSelected) {
    JRadioButtonMenuItem menuItem = MenuItemsBean.this.menuBarRadioItems.get(itemEnum);
    if (menuItem != null) {
      menuItem.setSelected(isSelected);
    }
    JRadioButton button = MenuItemsBean.this.radioButtons.get(itemEnum);
    if (button != null) {
      button.setSelected(isSelected);
    }

    // selecting an awt check box menu item if present
    CheckboxMenuItem checkItem = MenuItemsBean.this.menuBarCheckBoxItems.get(itemEnum);
    if (checkItem != null) {
      checkItem.dispatchEvent(new ActionEvent(itemEnum, ActionEvent.ACTION_PERFORMED, null));
    }
  }

  /**
   * Helper method to add a listener to a component.
   * Note that this method is null safe.
   *
   * @param component component to add a listener to (must be an AbstractButton).
   * @param listener  listener (supported are: ActionListener,
   *                  FocusListener, and KeyListener).
   */
  private void addListenerHelper(AbstractButton component, Object listener) {
    if (component != null && listener != null) {
      if (listener instanceof ActionListener) {
        component.addActionListener((ActionListener) listener);

      } else if (listener instanceof FocusListener) {
        component.addFocusListener((FocusListener) listener);

      } else if (listener instanceof KeyListener) {
        component.addKeyListener((KeyListener) listener);

      } else {
        throw new IllegalArgumentException("Illegal listener class: " + listener.getClass().getName());
      }
    }
  }

  /**
   * Item's action listener that assists with selecting
   * all items that refer to the same option (item enum) and
   * performing an action on the bean's action listener
   * (if it's set).
   */
  private class ItemActionListener extends AbstractAction implements ActionListener, ItemListener, KeyListener {

    /** This enum represent an item this listener belongs to. */
    private final WindowComponent itemEnum;

    /**
     * Constructs a new <code>ItemActionListener</code> object.
     *
     * @param itemEnum item enum to set.
     */
    private ItemActionListener(WindowComponent itemEnum) {
      this.itemEnum = itemEnum;
    }

    /** {@inheritDoc} */
    public void actionPerformed(ActionEvent event) {
      doAction(event);
    }

    /** {@inheritDoc} */
    public void itemStateChanged(ItemEvent event) {
      ActionEvent aEvent = new ActionEvent(this.itemEnum, ActionEvent.ACTION_PERFORMED, this.itemEnum.getTextKey());
      doAction(aEvent);
    }

    /** Does nothing. */
    public void keyTyped(KeyEvent event) {
    }

    /** Does nothing. */
    public void keyPressed(KeyEvent event) {
    }

    /** {@inheritDoc} */
    public void keyReleased(KeyEvent event) {
      if (KeyEvent.VK_ENTER == event.getKeyCode()) {
        ActionEvent aEvent = new ActionEvent(this.itemEnum, ActionEvent.ACTION_PERFORMED, this.itemEnum.getTextKey());
        doAction(aEvent);
      }
    }

    /**
     * Helper method to perform action.
     *
     * @param event event for this action.
     */
    private void doAction(ActionEvent event) {
      MenuItemsBean.this.setSelected(this.itemEnum, true);
      if (MenuItemsBean.this.actionListener != null) {
        event.setSource(this.itemEnum);
        MenuItemsBean.this.actionListener.actionPerformed(event);
      }
    }
  }
}
