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

package net.curre.prefcount.gui.theme;

import com.formdev.flatlaf.FlatLightLaf;

/**
 * LAF theme from <a href="https://www.formdev.com/flatlaf/">formdev</a>.
 *
 * @author Yevgeny Nyden
 */
public class FlatLightTheme extends LafTheme {

  /** Theme ID. */
  public static final LafThemeId LAF_THEME_ID = LafThemeId.FLAT_LIGHT;

  /** Class name of this LAF to use with UIManager. */
  public static final String LAF_CLASS_NAME = "com.formdev.flatlaf.FlatLightLaf";

  /** {@inheritDoc} */
  @Override
  public LafThemeId getId() {
    return LAF_THEME_ID;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isDarkTheme() {
    return false;
  }

  /** {@inheritDoc} */
  @Override
  public String getNameResourceKey() {
    return "pref.skinMenu.flatlight";
  }

  /** {@inheritDoc} */
  @Override
  public boolean activateTheme() {
    FlatLightLaf theme = new FlatLightLaf();
    boolean result = FlatLightLaf.setup(theme);
    this.initializeInternals(theme.getDefaults());
    return result;
  }
}
