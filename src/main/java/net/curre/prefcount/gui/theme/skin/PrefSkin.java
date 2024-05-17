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

package net.curre.prefcount.gui.theme.skin;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Stroke;

/**
 * This is an interface to represent a prefcount skin,
 * which represents a Substance skin from the
 * <code>org.jvnet.substance.skin</code> package.
 * <p/>
 * Created date: Jun 13, 2007
 *
 * @author Yevgeny Nyden
 */
public interface PrefSkin {

  /**
   * Returns Substance skin class name if this theme/skin
   * is a SubstanceLookAndFeel skin.
   *
   * @return Substance skin class name.
   */
  public String getSubstanceSkinClassName();

  /**
   * Returns the skin resource key, which
   * also the skin's unique identifier.
   *
   * @return the skin resource key and it's unique ID.
   */
  public String getNameResourceKey();

  /**
   * Returns the main background color.
   *
   * @return the main background color.
   */
  public Color getMainBackgroundColor();

  /**
   * Returns the board background paint.
   *
   * @return the board background paint.
   */
  public Paint getBoardBackgroundPaint();

  /**
   * Returns the color for players names.
   *
   * @return the player names color.
   */
  public Color getPlayerNameColor();

  /**
   * Returns the font for players names.
   *
   * @return the player names font.
   */
  public Font getPlayerNameFont();

  /**
   * Returns the stroke for players names.
   *
   * @return the player names stroke.
   */
  public Stroke getPlayerNameStroke();

  /**
   * Returns the color for players scores.
   *
   * @return the color for players scores.
   */
  public Color getPlayerScoreColor();

  /**
   * Returns the font for players scores.
   *
   * @return the font for players scores.
   */
  public Font getPlayerScoreFont();

  /**
   * Returns the stroke for players scores.
   *
   * @return the stroke for players scores.
   */
  public Stroke getPlayerScoreStroke();

  /**
   * Returns the color for player (scores) totals.
   *
   * @return the color for player (scores) totals.
   */
  public Color getPlayerTotalsColor();

  /**
   * Returns the font for player (scores) totals.
   *
   * @return the font for player (scores) totals.
   */
  public Font getPlayerTotalsFont();

  /**
   * Returns the stroke for player (scores) totals.
   *
   * @return the stroke for player (scores) totals.
   */
  public Stroke getPlayerTotalsStroke();

  /**
   * Returns the color for the board lines.
   *
   * @return the color for the board lines.
   */
  public Color getBoardLineColor();

  /**
   * Returns the stroke for the board lines.
   *
   * @return the stroke for the board lines.
   */
  public Stroke getBoardLineStroke();

  /**
   * Returns paint for the final score background.
   *
   * @return Paint to heighlight final score polygon.
   */
  public Paint getFinalScoreBackgroundPaint();

  /**
   * Gets the main section lines paint.
   *
   * @return paint fro the player section division lines.
   */
  public Paint getMainSectionLinesPain();

}
