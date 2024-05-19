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

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Stroke;

/**
 * Interface to represent a PrefCount Look and Feel theme.
 *
 * @author Yevgeny Nyden
 */
public interface LafThemeInterface {

  /**
   * Gets a unique ID of this theme.
   *
   * @return theme ID enum
   */
  LafThemeId getId();

  /**
   * Determines if this is a dark of light theme.
   *
   * @return true if this is a dark theme; false if it's a light theme.
   */
  boolean isDarkTheme();

  /**
   * Loads the theme in the UIManager.
   *
   * @return true if activation was successful; false if otherwise.
   * @throws Exception on error
   */
  boolean activateTheme() throws Exception;

  /**
   * Returns the skin resource key, which also the skin's unique identifier.
   *
   * @return the skin resource key and it's unique ID.
   */
  String getNameResourceKey();

  /**
   * Returns the main background color.
   *
   * @return the main background color.
   */
  Color getMainBackgroundColor();

  /**
   * Returns the board background paint.
   *
   * @return the board background paint.
   */
  Paint getBoardBackgroundPaint();

  /**
   * Returns the color for players names.
   *
   * @return the player names color.
   */
  Color getPlayerNameColor();

  /**
   * Returns the font for players names.
   *
   * @return the player names font.
   */
  Font getPlayerNameFont();

  /**
   * Returns the stroke for players names.
   *
   * @return the player names stroke.
   */
  Stroke getPlayerNameStroke();

  /**
   * Returns the color for players scores.
   *
   * @return the color for players scores.
   */
  Color getPlayerScoreColor();

  /**
   * Returns the font for players scores.
   *
   * @return the font for players scores.
   */
  Font getPlayerScoreFont();

  /**
   * Returns the stroke for players scores.
   *
   * @return the stroke for players scores.
   */
  Stroke getPlayerScoreStroke();

  /**
   * Returns the color for player (scores) totals.
   *
   * @return the color for player (scores) totals.
   */
  Color getPlayerTotalsColor();

  /**
   * Returns the font for player (scores) totals.
   *
   * @return the font for player (scores) totals.
   */
  Font getPlayerTotalsFont();

  /**
   * Returns the stroke for player (scores) totals.
   *
   * @return the stroke for player (scores) totals.
   */
  Stroke getPlayerTotalsStroke();

  /**
   * Returns the color for the board lines.
   *
   * @return the color for the board lines.
   */
  Color getBoardLineColor();

  /**
   * Returns the stroke for the board lines.
   *
   * @return the stroke for the board lines.
   */
  Stroke getBoardLineStroke();

  /**
   * Returns paint for the final score background.
   *
   * @return Paint to highlight final score polygon.
   */
  Paint getFinalScoreBackgroundPaint();

  /**
   * Gets the main section lines paint.
   *
   * @return paint for the player section division lines.
   */
  Paint getMainSectionLinesPain();
}
