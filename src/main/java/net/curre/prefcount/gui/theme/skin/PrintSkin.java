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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Stroke;

/**
 * An object of this class represents
 * the prefcount skin used for printing the score board.
 * <p/>
 * Created date: Jun 23, 2008
 *
 * @author Yevgeny Nyden
 */
public class PrintSkin implements PrefSkin {

  /** Resource key for this skin is not available. */
  public static final String NAME_KEY = null;

  /** Main background color. */
  public static final Color COLOR_BACKGROUND_MAIN = Color.WHITE;

  /** Board background paint. */
  public static final Color PAINT_BACKGROUND_BOARD = Color.WHITE;

  /** Color for players names. */
  private static final Color COLOR_PLAYER_NAME = new Color(10, 10, 10);

  /** Font for players names. */
  private static final Font FONT_PLAYER_NAME = new Font("Arial Black", Font.BOLD, 14);

  /** Stroke for players names. */
  private static final Stroke STROKE_PLAYER_NAME = new BasicStroke(1);

  /** Color for players scores. */
  private static final Color COLOR_PLAYER_SCORE = new Color(30, 30, 30);

  /** Font for players scores. */
  private static final Font FONT_PLAYER_SCORE = new Font("SansSerif", Font.ITALIC, 13);

  /** Stroke for players scores. */
  private static final Stroke STROKE_PLAYER_SCORE = new BasicStroke(2);

  /** Color for players totals (scores). */
  private static final Color COLOR_PLAYER_TOTALS = new Color(0, 0, 0);

  /** Font for players totals (scores). */
  private static final Font FONT_PLAYER_TOTALS = new Font("SansSerif", Font.BOLD, 14);

  /** Stroke for players totals (scores). */
  private static final Stroke STROKE_PLAYER_TOTALS = new BasicStroke(1);

  /** Color for the board lines. */
  private static final Color COLOR_BOARD_LINES = new Color(200, 200, 200);

  /** Color for the board lines. */
  private static final Stroke STROKE_BOARD_LINES = new BasicStroke(.8F);

  /** Paint to heighlight final score polygon. */
  private static final Paint PAINT_FINAL_SCORE_BACKGROUND = new Color(150, 150, 150, 100);

  /** Paint for the main player section division lines. */
  private static final Paint PAINT_MAIN_SECTION_LINES = new Color(120, 120, 120);

  /**
   * Returns null since the default skin does not
   * have a corresponding class.
   *
   * @return null.
   */
  public String getSubstanceSkinClassName() {
    return null;
  }

  /** {@inheritDoc} */
  public String getNameResourceKey() {
    return NAME_KEY;
  }

  /** {@inheritDoc} */
  public Color getMainBackgroundColor() {
    return COLOR_BACKGROUND_MAIN;
  }

  /** {@inheritDoc} */
  public Paint getBoardBackgroundPaint() {
    return PAINT_BACKGROUND_BOARD;
  }

  /** {@inheritDoc} */
  public Color getPlayerNameColor() {
    return COLOR_PLAYER_NAME;
  }

  /** {@inheritDoc} */
  public Font getPlayerNameFont() {
    return FONT_PLAYER_NAME;
  }

  /** {@inheritDoc} */
  public Stroke getPlayerNameStroke() {
    return STROKE_PLAYER_NAME;
  }

  /** {@inheritDoc} */
  public Color getPlayerScoreColor() {
    return COLOR_PLAYER_SCORE;
  }

  /** {@inheritDoc} */
  public Font getPlayerScoreFont() {
    return FONT_PLAYER_SCORE;
  }

  /** {@inheritDoc} */
  public Stroke getPlayerScoreStroke() {
    return STROKE_PLAYER_SCORE;
  }

  /** {@inheritDoc} */
  public Color getPlayerTotalsColor() {
    return COLOR_PLAYER_TOTALS;
  }

  /** {@inheritDoc} */
  public Font getPlayerTotalsFont() {
    return FONT_PLAYER_TOTALS;
  }

  /** {@inheritDoc} */
  public Stroke getPlayerTotalsStroke() {
    return STROKE_PLAYER_TOTALS;
  }

  /** {@inheritDoc} */
  public Color getBoardLineColor() {
    return COLOR_BOARD_LINES;
  }

  /** {@inheritDoc} */
  public Stroke getBoardLineStroke() {
    return STROKE_BOARD_LINES;
  }

  /** {@inheritDoc} */
  public Paint getFinalScoreBackgroundPaint() {
    return PAINT_FINAL_SCORE_BACKGROUND;
  }

  /** {@inheritDoc} */
  public Paint getMainSectionLinesPain() {
    return PAINT_MAIN_SECTION_LINES;
  }

}