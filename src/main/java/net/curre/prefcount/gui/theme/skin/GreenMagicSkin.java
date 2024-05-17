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
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.Stroke;

import net.curre.prefcount.util.Utilities;

/**
 * An object of this class represents the prefcount GreenMagicSkin skin -
 * the Substance's <code>org.jvnet.substance.skin.GreenMagicSkin</code>.
 * <p/>
 * Created date: Jun 14, 2007
 *
 * @author Yevgeny Nyden
 */
public class GreenMagicSkin implements PrefSkin {

  /** Resource key for the skin name and the skins unique ID. */
  public static final String NAME_KEY = "pref.skinMenu.greenMagic";

  /** Main background color. */
  public static final Color COLOR_BACKGROUND_MAIN = new Color(188, 232, 177);

  /** Board background paint. */
  public static final Color PAINT_BACKGROUND_BOARD = new Color(205, 243, 184);

  /** Color for players names. */
  private static final Color COLOR_PLAYER_NAME = new Color(0, 0, 0);

  /** Font for players names. */
  private static final Font FONT_PLAYER_NAME = new Font("Arial Black", Font.BOLD, 16);

  /** Stroke for players names. */
  private static final Stroke STROKE_PLAYER_NAME = new BasicStroke(2);

  /** Color for players scores. */
  private static final Color COLOR_PLAYER_SCORE = new Color(0, 0, 0);

  /** Font for players scores. */
  private static final Font FONT_PLAYER_SCORE = new Font("Arial Black", Font.ITALIC, 15);

  /** Stroke for players scores. */
  private static final Stroke STROKE_PLAYER_SCORE = new BasicStroke(2);

  /** Color for players totals (scores). */
  private static final Color COLOR_PLAYER_TOTALS = new Color(0, 0, 0);

  /** Font for players totals (scores). */
  private static final Font FONT_PLAYER_TOTALS = new Font("Arial Black", Font.BOLD, 16);

  /** Stroke for players totals (scores). */
  private static final Stroke STROKE_PLAYER_TOTALS = new BasicStroke(2);

  /** Color for the board lines. */
  private static final Color COLOR_BOARD_LINES = new Color(35, 67, 28);

  /** Color for the board lines. */
  private static final Stroke STROKE_BOARD_LINES = new BasicStroke(1);

  /** Paint to heighlight final score polygon. */
  private static final Paint PAINT_FINAL_SCORE_BACKGROUND = new Color(35, 67, 28, 50);

  /** Paint for the main player section division lines. */
  private static final Paint PAINT_MAIN_SECTION_LINES = new Color(0, 0, 0);

  /** {@inheritDoc} */
  public String getSubstanceSkinClassName() {
    return org.jvnet.substance.skin.GreenMagicSkin.class.getName();
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
    return new GradientPaint(200f, 200f, PAINT_BACKGROUND_BOARD,
                             500f, 500f, Utilities.createDarkerColor(PAINT_BACKGROUND_BOARD, 50));
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
