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

/**
 * Known LAF theme IDs.
 *
 * @author Yevgeny Nyden
 */
public enum LafThemeId {

  /** Game default theme based on the FlatLightLaf theme but with
   * a more vibrant set of colors. */
  DEFAULT,

  /** Based on javax.swing.plaf.nimbus.NimbusLookAndFeel. */
  NIMBUS,

  /** Based on com.formdev.flatlaf.FlatLightLaf. */
  FLAT_LIGHT,

  /** Based on com.formdev.flatlaf.FlatDarkLaf. */
  FLAT_DARK,

  /** Based on com.formdev.flatlaf.themes.FlatMacDarkLaf. */
  FLAT_MAC_DARK,

  /** Theme for printing. */
  PRINT
}
