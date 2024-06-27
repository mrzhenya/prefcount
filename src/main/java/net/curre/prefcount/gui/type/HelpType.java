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

import net.curre.prefcount.util.LocaleExt;

/** Type of help available. */
public enum HelpType {

  /** Help on how to count. */
  HOW_TO_COUNT("pref.helpMenu.count", "howToCount.rtf"),

  /** General Preferance information. */
  PREF_REFERENCE("pref.helpMenu.prefRef", "prefReference.rtf"),

  /** Help on Preferance rules. */
  PREF_RULES("pref.countHelp.commonRules", "commonRules.rtf");

  /** Dialog title resource key. */
  private final String titleKey;

  /** Name of the help file. */
  private final String filename;

  /**
   * Ctor.
   * @param titleKey dialog title resource key.
   * @param filename help filename.
   */
  HelpType(String titleKey, String filename) {
    this.titleKey = titleKey;
    this.filename = filename;
  }

  /**
   * Gets the dialog localized title.
   * @return dialog title.
   */
  public String getTitle() {
    return LocaleExt.getString(this.titleKey);
  }

  /**
   * Gets the name of the help file.
   *
   * @return filename.
   */
  public String getFilename() {
    return this.filename;
  }
}
