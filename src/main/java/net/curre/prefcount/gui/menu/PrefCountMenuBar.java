/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as 
 * published by the Free Software Foundation;
 */

package net.curre.prefcount.gui.menu;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.validation.constraints.NotNull;

import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.bean.Settings;
import net.curre.prefcount.event.ChangeLanguageActionListener;
import net.curre.prefcount.event.LafThemeMenuItemListener;

import net.curre.prefcount.gui.HelpDialog;
import net.curre.prefcount.gui.theme.LafTheme;
import net.curre.prefcount.gui.type.HelpType;
import net.curre.prefcount.service.LafThemeService;
import net.curre.prefcount.service.MainService;
import net.curre.prefcount.util.LocaleExt;

/**
 * A menu bar that for the main UI.
 * <p/>
 * Created date: Jul 28, 2007
 *
 * @author Yevgeny Nyden
 */
public class PrefCountMenuBar extends JMenuBar {

  /** Reference to the help dialog. */
  private HelpDialog helpDialog;

  /** Reference to the print scores menu item. */
  private JMenuItem printScoresItem;

  /**
   * Constructor that initializes necessary
   * data structures, and creates the menus.
   */
  public PrefCountMenuBar() {
    createMenus();
  }

  /**
   * Enables the print scores menu button.
   *
   * @param enable true to enable the button; false if otherwise.
   */
  public void enablePrintingScores(boolean enable) {
    this.printScoresItem.setEnabled(enable);
  }

  /** Creates all necessary menus and menu items. */
  private void createMenus() {
    super.add(createMainMenu());
    super.add(createHelpMenu());
  }

  /**
   * Creates the main menu.
   *
   * @return created main menu.
   */
  private JMenu createMainMenu() {
    JMenu mainMenu = new JMenu(LocaleExt.getString("pref.mainMenu.main"));

    PrefCountRegistry registry = PrefCountRegistry.getInstance();
    Settings settings = registry.getSettingsService().getSettings();

    mainMenu.add(createLocaleMenu());

    final JMenu lafMenu = new JMenu(LocaleExt.getString("pref.mainMenu.look"));

    // Create Look-and-Feel menu.
    ButtonGroup group = new ButtonGroup();
    LafThemeService lafThemeService = registry.getLafThemeService();
    for (final LafTheme lafTheme : lafThemeService.getSupportedThemes()) {
      String name = LocaleExt.getString(lafTheme.getNameResourceKey());
      JRadioButtonMenuItem lafItem = new JRadioButtonMenuItem(name);
      group.add(lafItem);
      if (lafTheme.getId() == settings.getLafThemeId()) {
        lafItem.setSelected(true);
      }
      lafItem.addActionListener(new LafThemeMenuItemListener(lafTheme));
      lafMenu.add(lafItem);
    }
    mainMenu.add(lafMenu);

    // Create other menu items on the main menu
    mainMenu.add(createPrintMenu());

    mainMenu.addSeparator();
    JMenuItem aboutItem = new JMenuItem(LocaleExt.getString("pref.helpMenu.about"));
    aboutItem.addActionListener(e -> registry.getMainWindow().showAboutInfo());
    mainMenu.add(aboutItem);
    mainMenu.addSeparator();

    JMenuItem quitItem = new JMenuItem(LocaleExt.getString("pref.mainMenu.quit"));
    quitItem.addActionListener(e -> MainService.quitApp());
    mainMenu.add(quitItem);

    return mainMenu;
  }

  /**
   * Creates the print menu group.
   *
   * @return menu for print.
   */
  private JMenu createPrintMenu() {
    final JMenu printMenu = new JMenu(LocaleExt.getString("pref.mainMenu.print.title"));
    this.printScoresItem = new JMenuItem(LocaleExt.getString("pref.mainMenu.print.scores"));
    this.printScoresItem.addActionListener(e -> MainService.doPrint());
    this.printScoresItem.setEnabled(false);
    printMenu.add(this.printScoresItem);

    JMenuItem print3item = new JMenuItem(LocaleExt.getString("pref.mainMenu.print.template3"));
    print3item.addActionListener(e -> MainService.doPrintTemplate(3));
    printMenu.add(print3item);

    JMenuItem print4item = new JMenuItem(LocaleExt.getString("pref.mainMenu.print.template4"));
    print4item.addActionListener(e -> MainService.doPrintTemplate(4));
    printMenu.add(print4item);

    return printMenu;
  }

  /**
   * Creates the help menu.
   *
   * @return created help menu.
   */
  private JMenu createHelpMenu() {
    final JMenu helpMenu = new JMenu(LocaleExt.getString("pref.helpMenu.name"));

    JMenuItem menuItem1 = new JMenuItem(HelpType.HOW_TO_COUNT.getTitle());
    menuItem1.addActionListener(e -> showHelp(HelpType.HOW_TO_COUNT));
    helpMenu.add(menuItem1);

    JMenuItem menuItem2 = new JMenuItem(HelpType.PREF_REFERENCE.getTitle());
    menuItem2.addActionListener(e -> showHelp(HelpType.PREF_REFERENCE));
    helpMenu.add(menuItem2);

    JMenuItem menuItem3 = new JMenuItem(HelpType.PREF_RULES.getTitle());
    menuItem3.addActionListener(e -> showHelp(HelpType.PREF_RULES));
    helpMenu.add(menuItem3);

    return helpMenu;
  }

  /**
   * Displays help information in a modal dialog.
   *
   * @param helpType help type enum.
   */
  private void showHelp(HelpType helpType) {
    if (this.helpDialog == null) {
      this.helpDialog = new HelpDialog();
      PrefCountRegistry.getInstance().getLafThemeService().registerUITreeForUpdates(this.helpDialog);
    }
    this.helpDialog.showHelp(helpType);
  }

  /**
   * Creates the Locale settings menu group.
   * @return locale menu
   */
  private @NotNull JMenu createLocaleMenu() {
    JMenu localeMenu = new JMenu(LocaleExt.getString("pref.mainMenu.locales"));
    LocaleExt loc = PrefCountRegistry.getCurrentLocale();
    ButtonGroup localesGroup = new ButtonGroup();
    for (final LocaleExt currLoc : PrefCountRegistry.AVAILABLE_LOCALES) {
      JRadioButtonMenuItem localeItem = new JRadioButtonMenuItem(currLoc.getDisplayLanguage());
      if (currLoc.equals(loc)) {
        localeItem.setSelected(true);
      }
      localesGroup.add(localeItem);
      localeItem.addActionListener(
          new ChangeLanguageActionListener(currLoc.getLocale().getLanguage()));
      localeMenu.add(localeItem);
    }
    return localeMenu;
  }
}
