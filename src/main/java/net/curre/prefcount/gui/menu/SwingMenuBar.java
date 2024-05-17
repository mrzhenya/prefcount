/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as 
 * published by the Free Software Foundation;
 */

package net.curre.prefcount.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;

import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.bean.Settings;
import net.curre.prefcount.event.ChangeLanguageActionListener;
import net.curre.prefcount.event.LafMenuItemListener;
import net.curre.prefcount.gui.theme.skin.PrefSkin;
import static net.curre.prefcount.gui.type.WindowComponent.ABOUT_ACTION;
import static net.curre.prefcount.gui.type.WindowComponent.DIVISIBLE_BY_N;
import static net.curre.prefcount.gui.type.WindowComponent.DIVISIBLE_IGNORE;
import static net.curre.prefcount.gui.type.WindowComponent.HELP_COMMON_ACTION;
import static net.curre.prefcount.gui.type.WindowComponent.HELP_COUNT_ACTION;
import static net.curre.prefcount.gui.type.WindowComponent.HELP_PREF_ACTION;
import static net.curre.prefcount.gui.type.WindowComponent.LENINGRAD;
import static net.curre.prefcount.gui.type.WindowComponent.MAIN_3_PLAYERS;
import static net.curre.prefcount.gui.type.WindowComponent.MAIN_4_PLAYERS;
import static net.curre.prefcount.gui.type.WindowComponent.PRINT_SCORES_ACTION;
import static net.curre.prefcount.gui.type.WindowComponent.PRINT_TEMPLATE3_ACTION;
import static net.curre.prefcount.gui.type.WindowComponent.PRINT_TEMPLATE4_ACTION;
import static net.curre.prefcount.gui.type.WindowComponent.QUIT_ACTION;
import static net.curre.prefcount.gui.type.WindowComponent.RESET_SETTINGS_ACTION;
import static net.curre.prefcount.gui.type.WindowComponent.SAVE_SETTINGS_ACTION;
import static net.curre.prefcount.gui.type.WindowComponent.SOCHINKA;
import net.curre.prefcount.service.LafThemeService;
import net.curre.prefcount.service.SettingsService;
import net.curre.prefcount.util.LocaleExt;
import net.curre.prefcount.util.Utilities;

/**
 * Object of this class represents a menu bar that for
 * a non-Mac OS platform. For the reason of why we can't
 * use the same menu bar for Mac OS, please, read the
 * <code>net.curre.prefcount.gui.menu</code> package
 * description and the <code>AwtMenuBar</code> javadoc.
 * <p/>
 * Created date: Jul 28, 2007
 *
 * @author Yevgeny Nyden
 * @see net.curre.prefcount.gui.menu
 * @see AwtMenuBar
 */
public class SwingMenuBar extends JMenuBar implements PrefCountMenuBar {

  /** Reference to the language menu. */
  private JMenu languageMenu;

  /** Reference to the dialog frame menu item. */
  private JRadioButtonMenuItem dialogFrameItem;

  /**
   * Constructor that initializes necessary
   * data structures, and creates the menus.
   *
   * @throws UnsupportedOperationException When running on Mac OS.
   */
  public SwingMenuBar() {
    if (Utilities.isMacOs()) {
      throw new UnsupportedOperationException("SwingMenuBar should not be created for Mac OS");
    }

    createMenus();
  }

  /** {@inheritDoc} */
  public void refreshLanguageIcon() {
    languageMenu.setIcon(PrefCountRegistry.getCurrentLocale().getLocaleIcon());
  }

  /**
   * Does nothing.
   *
   * @param enabled Enabled value.
   */
  public void toggleNextAction(boolean enabled) {
  }

  /**
   * Does nothing.
   *
   * @param enabled Enabled value.
   */
  public void toggleBackAction(boolean enabled) {
  }

  /**
   * Does nothing.
   *
   * @param enabled Enabled value.
   */
  public void toggleComputeAction(boolean enabled) {
  }

  /** {@inheritDoc} */
  public void setDialogFrameItemState(boolean isSelected) {
    this.dialogFrameItem.setSelected(isSelected);
  }

  /** Private methods ***********************/

  /** Creates all necessary menus and menu items. */
  private void createMenus() {

    // creating the main menu
    super.add(createMainMenu());

    // creating the action menu
    super.add(createActionMenu());

    // creating the window menu
    super.add(createWindowMenu());

    // creating the help menu
    super.add(createHelpMenu());

    // creating the language menu
    this.languageMenu = createLanguageMenu();
    super.add(Box.createHorizontalGlue());
    super.add(this.languageMenu);
  }

  /**
   * Creates the main menu.
   *
   * @return created main menu.
   */
  private JMenu createMainMenu() {

    JMenu mainMenu = new JMenu(LocaleExt.getString("pref.mainMenu.main"));
    LocaleExt.registerComponent(mainMenu, "pref.mainMenu.main");

    // ..... creating Look and Feel submenu
    Settings settings = SettingsService.getSettings();
    final JMenu lafMenu = new JMenu(LocaleExt.getString("pref.mainMenu.look"));
    LocaleExt.registerComponent(lafMenu, "pref.mainMenu.look");

    ButtonGroup group = new ButtonGroup();
    for (final PrefSkin skin : LafThemeService.AVAILABLE_SKINS) {
      String name = LocaleExt.getString(skin.getNameResourceKey());
      JRadioButtonMenuItem lafItem = new JRadioButtonMenuItem(name);
      LocaleExt.registerComponent(lafItem, skin.getNameResourceKey());
      group.add(lafItem);
      if (skin.getNameResourceKey().equals(settings.getLafSkinId())) {
        lafItem.setSelected(true);
      }
      lafItem.addActionListener(new LafMenuItemListener(skin));
      lafMenu.add(lafItem);
    }
    mainMenu.add(lafMenu);

    // .....  creating other menu items on the main menu
    MenuItemsBean menuItemsBean = PrefCountRegistry.getInstance().getMenuItemsBean();
    mainMenu.add(menuItemsBean.getJMenuItem(PRINT_SCORES_ACTION));

    final JMenu printMenu = new JMenu(LocaleExt.getString("pref.mainMenu.print.templates"));
    LocaleExt.registerComponent(printMenu, "pref.mainMenu.print.templates");
    printMenu.add(menuItemsBean.getJMenuItem(PRINT_TEMPLATE3_ACTION));
    printMenu.add(menuItemsBean.getJMenuItem(PRINT_TEMPLATE4_ACTION));
    mainMenu.add(printMenu);

    mainMenu.addSeparator();

    mainMenu.add(menuItemsBean.getJMenuItem(SAVE_SETTINGS_ACTION));
    mainMenu.add(menuItemsBean.getJMenuItem(RESET_SETTINGS_ACTION));
    mainMenu.addSeparator();

    mainMenu.add(menuItemsBean.getJMenuItem(QUIT_ACTION));

    return mainMenu;
  }

  /**
   * Creates the action menu.
   *
   * @return created action menu.
   */
  private JMenu createActionMenu() {

    JMenu menu = new JMenu(LocaleExt.getString("pref.actionMenu.name"));
    LocaleExt.registerComponent(menu, "pref.actionMenu.name");
    MenuItemsBean menuItemsBean = PrefCountRegistry.getInstance().getMenuItemsBean();

    menu.add(menuItemsBean.getJRadioButtonMenuItem(LENINGRAD));
    menu.add(menuItemsBean.getJRadioButtonMenuItem(SOCHINKA));
    menu.addSeparator();

    menu.add(menuItemsBean.getJRadioButtonMenuItem(MAIN_3_PLAYERS));
    menu.add(menuItemsBean.getJRadioButtonMenuItem(MAIN_4_PLAYERS));
    menu.addSeparator();

    menu.add(menuItemsBean.getJRadioButtonMenuItem(DIVISIBLE_IGNORE));
    menu.add(menuItemsBean.getJRadioButtonMenuItem(DIVISIBLE_BY_N));

    return menu;
  }

  /**
   * Creates the window menu.
   *
   * @return created window menu.
   */
  private JMenu createWindowMenu() {
    final JMenu winMenu = new JMenu(LocaleExt.getString("pref.windowMenu.name"));
    LocaleExt.registerComponent(winMenu, "pref.windowMenu.name");

    final JRadioButtonMenuItem scoreboardItem = new JRadioButtonMenuItem(LocaleExt.getString("pref.windowMenu.scoreboard"));
    scoreboardItem.setSelected(true);
    scoreboardItem.setEnabled(false);
    LocaleExt.registerComponent(scoreboardItem, "pref.windowMenu.scoreboard");
    winMenu.add(scoreboardItem);

    this.dialogFrameItem = new JRadioButtonMenuItem(LocaleExt.getString("pref.windowMenu.dialog"));
    this.dialogFrameItem.setSelected(true);
    LocaleExt.registerComponent(this.dialogFrameItem, "pref.windowMenu.dialog");
    this.dialogFrameItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        boolean selected = SwingMenuBar.this.dialogFrameItem.isSelected();
        PrefCountRegistry.getInstance().getPlayerDialogFrame().setVisible(selected);
      }
    });
    winMenu.add(this.dialogFrameItem);
    return winMenu;
  }

  /**
   * Creates the help menu.
   *
   * @return created help menu.
   */
  private JMenu createHelpMenu() {
    final JMenu helpMenu = new JMenu(LocaleExt.getString("pref.helpMenu.name"));
    LocaleExt.registerComponent(helpMenu, "pref.helpMenu.name");

    MenuItemsBean menuItemsBean = PrefCountRegistry.getInstance().getMenuItemsBean();
    helpMenu.add(menuItemsBean.getJMenuItem(HELP_COUNT_ACTION));
    helpMenu.add(menuItemsBean.getJMenuItem(HELP_PREF_ACTION));
    helpMenu.add(menuItemsBean.getJMenuItem(HELP_COMMON_ACTION));
    helpMenu.add(menuItemsBean.getJMenuItem(ABOUT_ACTION));

    return helpMenu;
  }

  /**
   * Creates the language menu.
   *
   * @return created language menu.
   */
  private JMenu createLanguageMenu() {
    LocaleExt loc = PrefCountRegistry.getCurrentLocale();
    JMenu menu = new JMenu();
    menu.setIcon(loc.getLocaleIcon());
    ButtonGroup group = new ButtonGroup();
    for (final LocaleExt currLoc : PrefCountRegistry.AVAILABLE_LOCALES) {
      JRadioButtonMenuItem rbMenuItem = new JRadioButtonMenuItem(currLoc.getDisplayLanguage());
      rbMenuItem.setIcon(currLoc.getLocaleIcon());
      if (currLoc.equals(loc)) {
        rbMenuItem.setSelected(true);
      }
      group.add(rbMenuItem);
      rbMenuItem.addActionListener(
          new ChangeLanguageActionListener(currLoc.getLocale().getLanguage()));
      menu.add(rbMenuItem);
    }
    return menu;
  }

}
