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

import javax.swing.UIDefaults;
import javax.validation.constraints.NotNull;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Stroke;

/**
 * Base class for all LAF theme classes.
 *
 * @author Yevgeny Nyden
 */
public abstract class LafTheme implements LafThemeInterface {

  /** Main background color. */
  public static final Color COLOR_BACKGROUND_MAIN = new Color(238, 238, 238);

  /** Board background paint. */
  public static final Color PAINT_BACKGROUND_BOARD = new Color(252, 252, 252);

  /** Font for the input panel title. */
  private static final Font INPUT_TITLE_FONT = new Font("Arial Black", Font.BOLD, 14);

  /** Color for players names. */
  private static final Color COLOR_PLAYER_NAME = new Color(10, 10, 10);

  /** Font for players names. */
  private static final Font FONT_PLAYER_NAME = new Font("Arial Black", Font.BOLD, 16);

  /** Stroke for players names. */
  private static final Stroke STROKE_PLAYER_NAME = new BasicStroke(2);

  /** Color for players scores. */
  private static final Color COLOR_PLAYER_SCORE = new Color(30, 30, 30);

  /** Font for players scores. */
  private static final Font FONT_PLAYER_SCORE = new Font("SansSerif", Font.ITALIC, 16);

  /** Stroke for players scores. */
  private static final Stroke STROKE_PLAYER_SCORE = new BasicStroke(2);

  /** Color for players totals (scores). */
  private static final Color COLOR_PLAYER_TOTALS = new Color(0, 0, 0);

  /** Font for players totals (scores). */
  private static final Font FONT_PLAYER_TOTALS = new Font("SansSerif", Font.BOLD, 16);

  /** Stroke for players totals (scores). */
  private static final Stroke STROKE_PLAYER_TOTALS = new BasicStroke(2);

  /** Color for the board lines. */
  private static final Color COLOR_BOARD_LINES = new Color(200, 200, 200);

  /** Color for the board lines. */
  private static final Stroke STROKE_BOARD_LINES = new BasicStroke(1);

  /** Paint to highlight final score polygon. */
  private static final Paint PAINT_FINAL_SCORE_BACKGROUND = new Color(150, 150, 150, 100);

  /** Paint for the main player section division lines. */
  private static final Paint PAINT_MAIN_SECTION_LINES = new Color(120, 120, 120);

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object other) {
    if (other instanceof LafTheme) {
      return this.getId() == ((LafTheme) other).getId();
    }
    return false;
  }

  /** {@inheritDoc} */
  @Override
  public Font getInputPanelTitleFont() {
    return INPUT_TITLE_FONT;
  }

  /** {@inheritDoc} */
  @Override
  public Color getMainBackgroundColor() {
    return COLOR_BACKGROUND_MAIN;
  }

  /** {@inheritDoc} */
  @Override
  public Paint getBoardBackgroundPaint() {
    return PAINT_BACKGROUND_BOARD;
  }

  /** {@inheritDoc} */
  @Override
  public Color getPlayerNameColor() {
    return COLOR_PLAYER_NAME;
  }

  /** {@inheritDoc} */
  @Override
  public Font getPlayerNameFont() {
    return FONT_PLAYER_NAME;
  }

  /** {@inheritDoc} */
  @Override
  public Stroke getPlayerNameStroke() {
    return STROKE_PLAYER_NAME;
  }

  /** {@inheritDoc} */
  @Override
  public Color getPlayerScoreColor() {
    return COLOR_PLAYER_SCORE;
  }

  /** {@inheritDoc} */
  @Override
  public Font getPlayerScoreFont() {
    return FONT_PLAYER_SCORE;
  }

  /** {@inheritDoc} */
  @Override
  public Stroke getPlayerScoreStroke() {
    return STROKE_PLAYER_SCORE;
  }

  /** {@inheritDoc} */
  @Override
  public Color getPlayerTotalsColor() {
    return COLOR_PLAYER_TOTALS;
  }

  /** {@inheritDoc} */
  @Override
  public Font getPlayerTotalsFont() {
    return FONT_PLAYER_TOTALS;
  }

  /** {@inheritDoc} */
  @Override
  public Stroke getPlayerTotalsStroke() {
    return STROKE_PLAYER_TOTALS;
  }

  /** {@inheritDoc} */
  @Override
  public Color getBoardLineColor() {
    return COLOR_BOARD_LINES;
  }

  /** {@inheritDoc} */
  @Override
  public Stroke getBoardLineStroke() {
    return STROKE_BOARD_LINES;
  }

  /** {@inheritDoc} */
  @Override
  public Paint getFinalScoreBackgroundPaint() {
    return PAINT_FINAL_SCORE_BACKGROUND;
  }

  /** {@inheritDoc} */
  @Override
  public Paint getMainSectionLinesPain() {
    return PAINT_MAIN_SECTION_LINES;
  }

  /**
   * Initializes internal style values.
   * @param defaults UI defaults
   */
  protected void initializeInternals(@NotNull UIDefaults defaults) {
    // TODO - move common code here.
  }
}
