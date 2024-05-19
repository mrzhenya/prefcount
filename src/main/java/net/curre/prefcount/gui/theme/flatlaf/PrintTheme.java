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

package net.curre.prefcount.gui.theme.flatlaf;

import com.formdev.flatlaf.FlatDarkLaf;

/**
 * @author Yevgeny Nyden
 */
public class PrintTheme extends FlatDarkLaf {

  /** Name of this theme. */
  public static final String NAME = "PrintTheme";

  public static boolean setup() {
    return setup(new PrintTheme());
  }

  public static void installLafInfo() {
    installLafInfo(NAME, PrintTheme.class);
  }

  @Override
  public String getName() {
    return NAME;
  }
}
