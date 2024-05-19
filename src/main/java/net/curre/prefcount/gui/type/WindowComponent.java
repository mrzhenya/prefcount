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
 * This is enumeration to represent window components
 * (menu radio buttons and the corresponding menu bar menu items).
 * <p/>
 * Created date: Mar 8, 2008
 *
 * @author Yevgeny Nyden
 */
public enum WindowComponent implements UIItem {

  /** Represents the main window leningrad item. */
  LENINGRAD("pref.scoreboard.prefType.leningrad", null, null, "PREF_TYPE",
            "pref.scoreboard.prefType.leningrad.tooltip"),

  /** Represents the main window sochinka item. */
  SOCHINKA("pref.scoreboard.prefType.sochi", null, null, "PREF_TYPE",
           "pref.scoreboard.prefType.sochi.tooltip"),

  /** Represents the main window 3 players item. */
  MAIN_3_PLAYERS("pref.scoreboard.players3",
                 "pref.scoreboard.players3.shortcut",
                 "pref.scoreboard.players3.index",
                 "PLAYERS_NUMBER",
                 "pref.scoreboard.players3.tooltip"),

  /** Represents the main window 4 players item. */
  MAIN_4_PLAYERS("pref.scoreboard.players4",
                 "pref.scoreboard.players4.shortcut",
                 "pref.scoreboard.players4.index",
                 "PLAYERS_NUMBER",
                 "pref.scoreboard.players4.tooltip"),

  /** Represents the main window ignore divisibility item. */
  DIVISIBLE_IGNORE("pref.scoreboard.divisible.ignore",
                   null, null, "DIVISIBILITY",
                   "pref.scoreboard.divisible.ignore.tooltip"),

  /** Represents the main window divisible by N item. */
  DIVISIBLE_BY_N("pref.scoreboard.divisible.byN",
                 null, null, "DIVISIBILITY",
                 "pref.scoreboard.divisible.byN.tooltip"),

  /** Represents the dialog window forward item. */
  DIALOG_FORWARD("pref.dialog.nextButton.label",
                 "pref.dialog.nextButton.shortcut",
                 "pref.dialog.nextButton.index",
                 "DIALOG_FORWARD", null),

  /** Represents the dialog window back item. */
  DIALOG_BACK("pref.dialog.backButton.label",
              "pref.dialog.backButton.shortcut",
              "pref.dialog.backButton.index",
              "DIALOG_BACK", null),

  /** Represents the choose player dialog window forward item. */
  DIALOG_FORWARD2("pref.dialog.nextButton.label",
                 "pref.dialog.nextButton.shortcut",
                 "pref.dialog.nextButton.index",
                 "DIALOG_FORWARD2", null),

  /** Represents the save settings action item. */
  SAVE_SETTINGS_ACTION("pref.mainMenu.settings.save",
                       "pref.mainMenu.settings.save.shortcut",
                       null, "SAVE_SETTINGS_ACTION", null),

  /** Represents the reset settings action item. */
  RESET_SETTINGS_ACTION("pref.mainMenu.settings.reset",
                        "pref.mainMenu.settings.reset.shortcut",
                        null, "RESET_SETTINGS_ACTION", null),

  /** Represents the print action item. */
  PRINT_SCORES_ACTION("pref.mainMenu.print.scores",
                      "pref.mainMenu.print.scores.shortcut",
                      null, "PRINT_SCORES_ACTION", null),

  /** Represents the print action item. */
  PRINT_TEMPLATE3_ACTION("pref.mainMenu.print.template3",
                         null, null, "PRINT_TEMPLATE3_ACTION", null),

  /** Represents the print action item. */
  PRINT_TEMPLATE4_ACTION("pref.mainMenu.print.template4",
                         null, null, "PRINT_TEMPLATE4_ACTION", null),

  /** Represents the quit action item. */
  QUIT_ACTION("pref.mainMenu.quit",
              "pref.mainMenu.quit.shortcut",
              null, "QUIT_ACTION", null),

  /** Represents the count help action item. */
  HELP_COUNT_ACTION("pref.helpMenu.count",
                    null, null, "HELP_COUNT_ACTION", null),

  /**
   * Represents the count help action item.
   * We need the second enum for the second menu (when on Mac).
   */
  HELP_COUNT_ACTION2("pref.helpMenu.count",
                     null, null, "HELP_COUNT_ACTION2", null),

  /** Represents the preferance reference help action item. */
  HELP_PREF_ACTION("pref.helpMenu.prefRef",
                   null, null, "HELP_PREF_ACTION", null),

  /**
   * Represents the preferance reference help action item.
   * We need the second enum for the second menu (when on Mac).
   */
  HELP_PREF_ACTION2("pref.helpMenu.prefRef",
                    null, null, "HELP_PREF_ACTION2", null),

  /** Represents the common rules help action item. */
  HELP_COMMON_ACTION("pref.countHelp.commonRules",
                     null, null, "HELP_COMMON_ACTION", null),

  /**
   * Represents the common rules help action item.
   * We need the second enum for the second menu (when on Mac).
   */
  HELP_COMMON_ACTION2("pref.countHelp.commonRules",
                      null, null, "HELP_COMMON_ACTION2", null),

  /** Represents the about action item. */
  ABOUT_ACTION("pref.helpMenu.about",
               null, null, "ABOUT_ACTION", null),

  /**
   * Represents the about action item.
   * We need the second enum for the second menu (when on Mac).
   */
  ABOUT_ACTION2("pref.helpMenu.about",
                null, null, "ABOUT_ACTION2", null);

  /** Resource key for the item label. */
  private final String textKey;

  /** Resource key for the item shortcut. */
  private final String shortcutKey;

  /** Resource key for the item shortcut letter index (for underlining). */
  private final String shortcutIndexKey;

  /** Unique identifier for the button group. */
  public final String groupKey;

  /** Tooltip resource key for this item. */
  public final String tooltipKey;

  /**
   * Constructor.
   *
   * @param textKey          Resource key for the item label.
   * @param shortcutKey      Resource key for the item shortcut.
   * @param shortcutIndexKey Resource key for the item shortcut
   *                         letter index (for underlining).
   * @param groupKey         Unique identifier for the button group.
   * @param tooltipKey       Tooltip resource key for this item.
   */
  WindowComponent(String textKey, String shortcutKey,
                  String shortcutIndexKey, String groupKey,
                  String tooltipKey) {
    this.textKey = textKey;
    this.shortcutKey = shortcutKey;
    this.shortcutIndexKey = shortcutIndexKey;
    this.groupKey = groupKey;
    this.tooltipKey = tooltipKey;
  }

  /** {@inheritDoc} */
  public String getTextKey() {
    return this.textKey;
  }

  /** {@inheritDoc} */
  public String getShortcutKey() {
    return this.shortcutKey;
  }

  /** {@inheritDoc} */
  public String getShortcutIndexKey() {
    return this.shortcutIndexKey;
  }

}
