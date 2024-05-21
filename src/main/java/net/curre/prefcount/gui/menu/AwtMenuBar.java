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

import java.awt.CheckboxMenuItem;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;

import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.bean.Settings;
import net.curre.prefcount.event.ChangeLanguageActionListener;
import net.curre.prefcount.event.LafThemeMenuItemListener;
import static net.curre.prefcount.gui.menu.PrefCountMenuBar.MenuBarType.MAIN_WINDOW;
import static net.curre.prefcount.gui.menu.PrefCountMenuBar.MenuBarType.PLAYER_DIALOG;
import static net.curre.prefcount.gui.type.WindowComponent.*;

import net.curre.prefcount.gui.theme.LafTheme;
import net.curre.prefcount.service.LafThemeService;
import net.curre.prefcount.util.LocaleExt;
import net.curre.prefcount.util.PlatformType;

/**
 * Object of this class represents a menu bar for macOS.
 * This menu bar is assumed to be  placed at the top fo the
 * screen. Other platforms are not supported by this class
 * just because swing menu bar is a better option when not
 * on macOS.
 * <p/>
 * Note that we are using awt menu bar because swing menu bar
 * is not compatible with the substance look-and-feel library
 * when running on Mac.
 * <p/>
 * Created date: Jan 24, 2008
 *
 * @author Yevgeny Nyden
 */
public class AwtMenuBar extends MenuBar implements PrefCountMenuBar {

  /** <code>serialVersionUID</code> value for serialization. */
  private static final long serialVersionUID = 9188705200836157181L;

  /** Menu item 'next' on the action menu. */
  private MenuItem actonNextMenuItem;

  /** Menu item 'back' on the action menu. */
  private MenuItem actonBackMenuItem;

  /** Reference to the dialog frame menu item. */
  private CheckboxMenuItem dialogFrameItem;

  /**
   * Constructor that sets the type of this menu bar,
   * initializes necessary data structures, and creates the menus.
   *
   * @param menuBarType Type of this menu bar (for which frame it was created).
   * @throws IllegalArgumentException      If menu bar type is not supported.
   * @throws UnsupportedOperationException When running on not macOS.
   */
  public AwtMenuBar(MenuBarType menuBarType) {

    if (menuBarType != MAIN_WINDOW && menuBarType != PLAYER_DIALOG) {
      throw new IllegalArgumentException("Unsupported menu bar type: " + menuBarType);
    }
    if (!PlatformType.isMacOs()) {
      throw new UnsupportedOperationException("AwtMenuBar should only be created for Mac OS");
    }

    createMenus(menuBarType);
  }

  /** Does nothing. */
  public void refreshLanguageIcon() {
  }

  /** {@inheritDoc} */
  public void toggleNextAction(boolean enabled) {
    actonNextMenuItem.setEnabled(enabled);
  }

  /** {@inheritDoc} */
  public void toggleBackAction(boolean enabled) {
    actonBackMenuItem.setEnabled(enabled);
  }

  /** {@inheritDoc} */
  public void toggleComputeAction(boolean enabled) {
  }

  /** {@inheritDoc} */
  public void setDialogFrameItemState(boolean isSelected) {
    this.dialogFrameItem.setState(isSelected);
  }

  /**
   * Creates all necessary menus and menu items.
   *
   * @param menuBarType Type of the menu bar to create
   *                    (indicates for which window).
   */
  private void createMenus(MenuBarType menuBarType) {

    // creating the main menu
    if (menuBarType == MAIN_WINDOW) {
      super.add(createMainMenu());
    }

    // creating the action menu
    super.add(createActionMenu(menuBarType));

    // creating the window menu
    super.add(createWindowMenu());

    // creating the help menu
    Menu languageMenu = createLanguageMenu(menuBarType);
    if (languageMenu != null) {
      super.add(languageMenu);
    }

    // creating the help menu
    super.add(createHelpMenu(menuBarType));
  }

  /**
   * Creates the main menu.
   *
   * @return created main menu.
   */
  private Menu createMainMenu() {

    Menu mainMenu = new Menu(LocaleExt.getString("pref.mainMenu.main"));
    LocaleExt.registerComponent(mainMenu, "pref.mainMenu.main");

    // ..... creating Look and Feel submenu
    PrefCountRegistry registry = PrefCountRegistry.getInstance();
    Settings settings = registry.getSettingsService().getSettings();
    Menu lafMenu = new Menu(LocaleExt.getString("pref.mainMenu.look"));
    LocaleExt.registerComponent(lafMenu, "pref.mainMenu.look");

    AwtCheckboxMenuGroup group = new AwtCheckboxMenuGroup();
    LafThemeService lafThemeService = registry.getLafThemeService();
    for (final LafTheme lafTheme : lafThemeService.getSupportedThemes()) {
      CheckboxMenuItem lafItem = new CheckboxMenuItem(LocaleExt.getString(lafTheme.getNameResourceKey()));
      LocaleExt.registerComponent(lafItem, lafTheme.getNameResourceKey());
      if (lafTheme.getId() == settings.getLafThemeId()) {
        lafItem.setState(true);
      }
      lafItem.addItemListener(new LafThemeMenuItemListener(lafTheme));
      group.addItemToGroup(lafItem);
      lafMenu.add(lafItem);
    }
    mainMenu.add(lafMenu);

    // .....  creating other menu items on the main menu
    MenuItemsBean menuItemsBean = registry.getMenuItemsBean();
    mainMenu.add(menuItemsBean.getMenuItem(PRINT_SCORES_ACTION));

    final Menu printMenu = new Menu(LocaleExt.getString("pref.mainMenu.print.templates"));
    LocaleExt.registerComponent(printMenu, "pref.mainMenu.print.templates");
    printMenu.add(menuItemsBean.getMenuItem(PRINT_TEMPLATE3_ACTION));
    printMenu.add(menuItemsBean.getMenuItem(PRINT_TEMPLATE4_ACTION));
    mainMenu.add(printMenu);

    return mainMenu;
  }

  /**
   * Creates the action menu.
   *
   * @param menuBarType Type of the menu bar to create
   *                    (indicates for which window).
   * @return created action menu.
   */
  private Menu createActionMenu(MenuBarType menuBarType) {

    Menu actionMenu = new Menu(LocaleExt.getString("pref.actionMenu.name"));
    LocaleExt.registerComponent(actionMenu, "pref.actionMenu.name");
    MenuItemsBean menuItemsBean = PrefCountRegistry.getInstance().getMenuItemsBean();

    // action menu is different depending on the window
    switch (menuBarType) {

      case MAIN_WINDOW:

        actionMenu.add(menuItemsBean.getRadioButtonMenuItem(LENINGRAD));
        actionMenu.add(menuItemsBean.getRadioButtonMenuItem(SOCHINKA));
        actionMenu.addSeparator();

        actionMenu.add(menuItemsBean.getRadioButtonMenuItem(MAIN_3_PLAYERS));
        actionMenu.add(menuItemsBean.getRadioButtonMenuItem(MAIN_4_PLAYERS));
        actionMenu.addSeparator();

        actionMenu.add(menuItemsBean.getRadioButtonMenuItem(DIVISIBLE_IGNORE));
        actionMenu.add(menuItemsBean.getRadioButtonMenuItem(DIVISIBLE_BY_N));
        break;

      case PLAYER_DIALOG:

        this.actonNextMenuItem = menuItemsBean.getMenuItem(DIALOG_FORWARD);
        actionMenu.add(this.actonNextMenuItem);

        this.actonBackMenuItem = menuItemsBean.getMenuItem(DIALOG_BACK);
        this.actonBackMenuItem.setEnabled(false);
        actionMenu.add(this.actonBackMenuItem);
        break;
    }

    return actionMenu;
  }

  /**
   * Creates the window menu.
   *
   * @return created window menu.
   */
  private Menu createWindowMenu() {
    final Menu winMenu = new Menu(LocaleExt.getString("pref.windowMenu.name"));
    LocaleExt.registerComponent(winMenu, "pref.windowMenu.name");

    final CheckboxMenuItem scoreboardItem = new CheckboxMenuItem(LocaleExt.getString("pref.windowMenu.scoreboard"));
    scoreboardItem.setState(true);
    scoreboardItem.setEnabled(false);
    LocaleExt.registerComponent(scoreboardItem, "pref.windowMenu.scoreboard");
    winMenu.add(scoreboardItem);

    this.dialogFrameItem = new CheckboxMenuItem(LocaleExt.getString("pref.windowMenu.dialog"));
    this.dialogFrameItem.setState(true);
    LocaleExt.registerComponent(this.dialogFrameItem, "pref.windowMenu.dialog");
    this.dialogFrameItem.addItemListener(event -> {
      boolean selected = AwtMenuBar.this.dialogFrameItem.getState();
      PrefCountRegistry registry = PrefCountRegistry.getInstance();
      registry.getPlayerDialogFrame().setVisible(selected);
      registry.getMainWindow().getPrefCountMenuBar().setDialogFrameItemState(selected);
      PrefCountMenuBar menuBar = registry.getPlayerDialogFrame().getPrefCountMenuBar();
      if (menuBar != null) {
        menuBar.setDialogFrameItemState(selected);
      }
    });
    winMenu.add(this.dialogFrameItem);
    return winMenu;
  }

  /**
   * Creates the language menu.
   *
   * @param menuBarType Type of the menu bar to create
   *                    (indicates for which window).
   * @return created language menu.
   */
  private Menu createLanguageMenu(MenuBarType menuBarType) {
    if (menuBarType == MAIN_WINDOW) {
      Menu languageMenu = new Menu(LocaleExt.getString("pref.langMenu.name"));
      LocaleExt.registerComponent(languageMenu, "pref.langMenu.name");
      LocaleExt loc = PrefCountRegistry.getCurrentLocale();
      AwtCheckboxMenuGroup group = new AwtCheckboxMenuGroup();
      for (final LocaleExt currLoc : PrefCountRegistry.AVAILABLE_LOCALES) {
        CheckboxMenuItem rbMenuItem = new CheckboxMenuItem(currLoc.getDisplayLanguage());
        if (currLoc.equals(loc)) {
          rbMenuItem.setState(true);
        }
        rbMenuItem.addItemListener(new ChangeLanguageActionListener(currLoc.getLocale().getLanguage()));
        group.addItemToGroup(rbMenuItem);
        languageMenu.add(rbMenuItem);
      }
      return languageMenu;
    }
    return null;
  }

  /**
   * Creates the help menu.
   *
   * @param menuBarType type of the menu.
   * @return created help menu.
   */
  private Menu createHelpMenu(MenuBarType menuBarType) {

    Menu helpMenu = new Menu(LocaleExt.getString("pref.helpMenu.name"));
    LocaleExt.registerComponent(helpMenu, "pref.helpMenu.name");
    MenuItemsBean menuItemsBean = PrefCountRegistry.getInstance().getMenuItemsBean();

    if (menuBarType == MAIN_WINDOW) {
      helpMenu.add(menuItemsBean.getMenuItem(HELP_COUNT_ACTION));
      helpMenu.add(menuItemsBean.getMenuItem(HELP_PREF_ACTION));
      helpMenu.add(menuItemsBean.getMenuItem(HELP_COMMON_ACTION));
      helpMenu.add(menuItemsBean.getMenuItem(ABOUT_ACTION));

    } else {
      helpMenu.add(menuItemsBean.getMenuItem(HELP_COUNT_ACTION2));
      helpMenu.add(menuItemsBean.getMenuItem(HELP_PREF_ACTION2));
      helpMenu.add(menuItemsBean.getMenuItem(HELP_COMMON_ACTION2));
      helpMenu.add(menuItemsBean.getMenuItem(ABOUT_ACTION2));

    }

    return helpMenu;
  }
}
