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

package net.curre.prefcount.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import javax.swing.ToolTipManager;

import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.bean.GameResultBean;
import net.curre.prefcount.bean.PlayerStatistics;
import net.curre.prefcount.bean.TooltipLocationsMap;
import net.curre.prefcount.gui.aa.AAJPanel;
import net.curre.prefcount.gui.theme.LafTheme;
import net.curre.prefcount.gui.type.Place;
import static net.curre.prefcount.gui.type.Place.EAST;
import static net.curre.prefcount.gui.type.Place.NORTH;
import static net.curre.prefcount.gui.type.Place.SOUTH;
import static net.curre.prefcount.gui.type.Place.WEST;
import net.curre.prefcount.gui.type.ScoreItem;
import static net.curre.prefcount.gui.type.ScoreItem.FINAL_MOUNT;
import static net.curre.prefcount.gui.type.ScoreItem.FINAL_SCORE;
import static net.curre.prefcount.gui.type.ScoreItem.PLAYER_AMNIST_MOUNT;
import static net.curre.prefcount.gui.type.ScoreItem.PLAYER_FIXED_MOUNT;
import static net.curre.prefcount.gui.type.ScoreItem.PLAYER_MOUNT;
import static net.curre.prefcount.gui.type.ScoreItem.PLAYER_NAME;
import static net.curre.prefcount.gui.type.ScoreItem.PLAYER_NEW_MOUNT;
import static net.curre.prefcount.gui.type.ScoreItem.PLAYER_POOL;
import static net.curre.prefcount.gui.type.ScoreItem.PLAYER_POOL_CLOSED;
import static net.curre.prefcount.gui.type.ScoreItem.WHIST_EAST_SALDO;
import static net.curre.prefcount.gui.type.ScoreItem.WHIST_NORTH_SALDO;
import static net.curre.prefcount.gui.type.ScoreItem.WHIST_SALDO_TOTAL;
import static net.curre.prefcount.gui.type.ScoreItem.WHIST_SOUTH_SALDO;
import static net.curre.prefcount.gui.type.ScoreItem.WHIST_WEST_SALDO;

import net.curre.prefcount.service.UiService;
import net.curre.prefcount.util.LocaleExt;

import org.apache.commons.lang3.StringUtils;

/**
 * Object of this class represents the score board panel
 * where all players scores and totals are drawn.
 * <p/>
 * Created date: Apr 8, 2007
 *
 * @author Yevgeny Nyden
 */
public class ScoreBoardPanel extends AAJPanel {

  /**
   * Reference to the map that stores/computes
   * locations for all items on the score board.
   */
  private final ScoreBoardLocationsMap locationsMap;

  /**
   * Reference to the map that stores locations (shapes)
   * for the tooltips of all items on the score board.
   */
  private final TooltipLocationsMap ttLocationsMap;

  /** Constructs a new <code>ScoreBoardPanel</code> object. */
  public ScoreBoardPanel() {
    this.locationsMap = new ScoreBoardLocationsMap(this);
    this.ttLocationsMap = new TooltipLocationsMap();
    ToolTipManager.sharedInstance().registerComponent(this);
  }

  /**
   * {@inheritDoc}
   *
   * @throws UnsupportedOperationException If number of player is not supported.
   */
  @Override
  public void paintComponent(Graphics g) {

    super.paintComponent(g);

    final int newWidth = getWidth();
    final int newHeight = getHeight();

    final LafTheme lafTheme = PrefCountRegistry.getInstance().getLafThemeService().getCurrentLafTheme();
    drawScoreBoard((Graphics2D) g, newWidth, newHeight, 0, 0, null, lafTheme);
  }

  /** {@inheritDoc} */
  @Override
  public String getToolTipText(MouseEvent event) {

    GameResultBean resultBean = PrefCountRegistry.getInstance().getGameResultBean();
    final Point point = event.getPoint();

    for (ScoreItem item : ScoreItem.values()) {
      for (Map.Entry<Place, Shape> entry : this.ttLocationsMap.get(item).entrySet()) {

        if (entry.getValue().contains(point)) {

          if (item == PLAYER_NAME) {
            final Place place = entry.getKey();
            PlayerStatistics stats = resultBean.getPlayerStats().get(place);
            String pName = StringUtils.isBlank(stats.getPlayerName())
                           ? "" : ": " + stats.getPlayerName();

            return "<HTML>&nbsp;" + LocaleExt.getString(item.key, LocaleExt.getString(place.longKey, "") + pName) + "&nbsp;";

          } else {
            Place place = item.isOtherPlace ? item.place : entry.getKey();
            PlayerStatistics stats = resultBean.getPlayerStats().get(place);
            String placeStr = LocaleExt.getString(place.longKey, "");
            String pName = StringUtils.isBlank(stats.getPlayerName()) ?
                           placeStr : stats.getPlayerName() + " (" + placeStr + ")";

            return "<HTML>&nbsp;" + LocaleExt.getString(item.key, pName) + "&nbsp;";
          }
        }
      }
    }

    return null;
  }

  /**
   * Paints the score board.
   *
   * @param g2            graphics context object to use.
   * @param newWidth      current width.
   * @param newHeight     current height.
   * @param offsetX       offset X coordinate.
   * @param offsetY       offset Y coordinate.
   * @param playersNumber when this value is not null, only the score board
   *                      template will be printed (for the specified number
   * @param lafTheme          pref skin to use.
   */
  protected void drawScoreBoard(Graphics2D g2, int newWidth, int newHeight,
                                int offsetX, int offsetY, Integer playersNumber, LafTheme lafTheme) {
    this.locationsMap.computeLocations(newWidth, newHeight, offsetX, offsetY, false);

    final int margin = ScoreBoardLocationsMap.MARGIN;
    final int width = this.locationsMap.width;
    final int height = this.locationsMap.height;
    final int centerX = this.locationsMap.centerX;
    final int centerY = this.locationsMap.centerY;
    final int twoFifthX = this.locationsMap.twoFifthX;
    final int threeFifthX = this.locationsMap.threeFifthX;
    final int twoFifthY = this.locationsMap.twoFifthY;
    final int threeFifthY = this.locationsMap.threeFifthY;
    final int whistPoolX = this.locationsMap.whistPoolDividerX;
    final int whistPoolY = this.locationsMap.whistPoolDividerY;
    final int poolMountX = this.locationsMap.poolMountDividerX;
    final int poolMountY = this.locationsMap.poolMountDividerY;

    // drawing nice borders
    Color bordColor = UiService.createDarkerColor(lafTheme.getMainBackgroundColor(), 20);
    g2.setColor(bordColor);
    g2.drawRect(4 + offsetX, 4 + offsetY, width - 8, height - 8);
    bordColor = UiService.createDarkerColor(bordColor, 20);
    g2.setColor(bordColor);
    g2.drawRect(5 + offsetX, 5 + offsetY, width - 10, height - 10);

    // painting score board background
    g2.setPaint(lafTheme.getBoardBackgroundPaint());
    g2.fillRect(6 + offsetX, 6 + offsetY, width - 11, height - 11);

    // player sections lines
    prepareBoardLinePen(g2, lafTheme);
    Ellipse2D e = new Ellipse2D.Double();
    e.setFrameFromCenter(centerX + 1d, centerY, centerX + 32d, centerY + 30d);
    g2.draw(e);

    // section borders and players information
    GameResultBean resultBean = PrefCountRegistry.getInstance().getGameResultBean();
    Map<Place, PlayerStatistics> statistics = resultBean.getPlayerStats();

    final int numPlayers = playersNumber == null ? statistics.size() : playersNumber;
    switch (numPlayers) {
      case 3:
        // drawing players main sections lines
        g2.setPaint(lafTheme.getMainSectionLinesPain());
        g2.drawLine(margin + offsetX, height - margin + offsetY, centerX, centerY);         // /
        g2.drawLine(centerX, centerY, width - margin + offsetX, height - margin + offsetY); // \
        g2.drawLine(centerX, centerY, centerX, margin + offsetY);                           // |
        g2.setColor(lafTheme.getBoardLineColor());

        // drawing field division lines (to separate mount from pool, etc.)
        g2.drawLine(twoFifthX, whistPoolY, whistPoolX, whistPoolY);             // - (south-bottom)
        g2.drawLine(twoFifthX, whistPoolY, twoFifthX, margin + offsetY);        // | (west-left)
        g2.drawLine(whistPoolX, margin + offsetY, whistPoolX, whistPoolY);      // | (east-right)
        g2.drawLine(threeFifthX, poolMountY, poolMountX, poolMountY);           // - (south-up)
        g2.drawLine(threeFifthX, poolMountY, threeFifthX, margin + offsetY);    // | (west-right)
        g2.drawLine(poolMountX, poolMountY, poolMountX, margin + offsetY);      // | (east-left)

        // drawing whist fields division lines
        final int upWhistDivY = (height - (twoFifthY / 2)) / 2 + offsetY;
        g2.drawLine(centerX, height - margin + offsetY, centerX, whistPoolY);         // | (south)
        g2.drawLine(margin + offsetX, upWhistDivY, twoFifthX, upWhistDivY);           // - (west)
        g2.drawLine(whistPoolX, upWhistDivY, width - margin + offsetX, upWhistDivY);  // - (east)

        if (playersNumber == null) {
          // drawing player 0 information
          drawPlayerScores(EAST, g2, lafTheme);

          // drawing player 1 information
          drawPlayerScores(SOUTH, g2, lafTheme);

          // drawing player 2 information
          drawPlayerScores(WEST, g2, lafTheme);
        }

        break;

      case 4:
        // drawing players main sections lines
        g2.setPaint(lafTheme.getMainSectionLinesPain());
        g2.drawLine(margin + offsetX, margin + offsetY, width - margin + offsetX, height - margin + offsetY); // \
        g2.drawLine(margin + offsetX, height - margin + offsetY, width - margin + offsetX, margin + offsetY); // /
        g2.setColor(lafTheme.getBoardLineColor());

        // drawing field division lines (to separate mount from pool, etc.)
        g2.drawLine(twoFifthX, whistPoolY, whistPoolX, whistPoolY);       // - (bottom whist-pool)
        g2.drawLine(twoFifthX, whistPoolY, twoFifthX, twoFifthY);         // | (left whist-pool)
        g2.drawLine(whistPoolX, whistPoolY, whistPoolX, twoFifthY);       // | (right whist-pool)
        g2.drawLine(twoFifthX, twoFifthY, whistPoolX, twoFifthY);         // - (top whist-pool)
        g2.drawLine(threeFifthX, poolMountY, poolMountX, poolMountY);     // - (bottom pool-mount)
        g2.drawLine(threeFifthX, poolMountY, threeFifthX, threeFifthY);   // | (west pool-mount)
        g2.drawLine(poolMountX, poolMountY, poolMountX, threeFifthY);     // | (right pool-mount)
        g2.drawLine(threeFifthX, threeFifthY, poolMountX, threeFifthY);   // - (south pool-mount)

        // drawing whist fields division lines
        g2.drawLine(margin + offsetX, this.locationsMap.whistDividerY1,
                    twoFifthX, this.locationsMap.whistDividerY1);                 // - (west-top)
        g2.drawLine(margin + offsetX, this.locationsMap.whistDividerY2,
                    twoFifthX, this.locationsMap.whistDividerY2);                 // - (west-bottom)
        g2.drawLine(whistPoolX, this.locationsMap.whistDividerY1,
                    width - margin + offsetX, this.locationsMap.whistDividerY1);  // - (east-top)
        g2.drawLine(whistPoolX, this.locationsMap.whistDividerY2,
                    width - margin + offsetX, this.locationsMap.whistDividerY2);  // - (east-bottom)

        g2.drawLine(this.locationsMap.whistDividerX1, twoFifthY,
                    this.locationsMap.whistDividerX1, margin + offsetY);          // | (north-left)
        g2.drawLine(this.locationsMap.whistDividerX2, twoFifthY,
                    this.locationsMap.whistDividerX2, margin + offsetY);          // | (north-right)
        g2.drawLine(this.locationsMap.whistDividerX1, height - margin + offsetY,
                    this.locationsMap.whistDividerX1, whistPoolY);                // | (south-left)
        g2.drawLine(this.locationsMap.whistDividerX2, height - margin + offsetY,
                    this.locationsMap.whistDividerX2, whistPoolY);                // | (south-right)

        if (playersNumber == null) {
          // displaying player 0 information
          drawPlayerScores(NORTH, g2, lafTheme);

          // displaying player 1 information
          drawPlayerScores(EAST, g2, lafTheme);

          // displaying player 2 information
          drawPlayerScores(SOUTH, g2, lafTheme);

          // displaying player 3 information
          drawPlayerScores(WEST, g2, lafTheme);
        }
        break;

      default:
        throw new UnsupportedOperationException(statistics.size() + " number of players is NOT supported!");
    }
  }

  /**
   * Initializes number of players for the player dialog frame.
   *
   * @param numberOfPlayers number of players.
   */
  public void initializeNumberOfPlayers(int numberOfPlayers) {
    this.getLocationsMap().initialize(numberOfPlayers);
    this.ttLocationsMap.clear();
  }

  /**
   * Gets the location map object.
   *
   * @return the location map.
   */
  public synchronized ScoreBoardLocationsMap getLocationsMap() {
    return this.locationsMap;
  }

  /**
   * Method to draw all player scores.
   *
   * @param place Current player place.
   * @param g2    Graphics object to use.
   * @param lafTheme  Current skin.
   */
  private void drawPlayerScores(Place place, Graphics2D g2, LafTheme lafTheme) {

    GameResultBean resultBean = PrefCountRegistry.getInstance().getGameResultBean();
    PlayerStatistics stats = resultBean.getPlayerStats().get(place);
    Map<ScoreItem, Point2D.Double> locations = getLocationsMap().getLocationsMap(place);

    // drawing player's place letter (South, East...)
    preparePlayerNamePen(g2, lafTheme);
    Point.Double point = locations.get(PLAYER_NAME);
    String placeChar = LocaleExt.getString(place.shortKey);
    g2.drawString(placeChar, (float) point.getX(), (float) point.getY());
    this.ttLocationsMap.addRectangleLocation(PLAYER_NAME, place, g2, point, placeChar);

    // drawing player's mount value
    preparePlayerScorePen(g2, lafTheme);
    point = locations.get(PLAYER_MOUNT);
    final String mount = getStringFromInt(stats.getMountain());
    g2.drawString(mount, (float) point.getX(), (float) point.getY());
    this.ttLocationsMap.addRectangleLocation(PLAYER_MOUNT, place, g2, point, mount);

    // drawing player's pool value
    point = locations.get(PLAYER_POOL);
    final String pool = getStringFromInt(stats.getPool());
    g2.drawString(pool, (float) point.getX(), (float) point.getY());
    this.ttLocationsMap.addRectangleLocation(PLAYER_POOL, place, g2, point, pool);

    // drawing player's whist values
    drawWhistAndWhistFixes(g2, place, stats, locations);

    // drawing players computed score values if the final scores are ready
    if (resultBean.isFinalScoresReady()) {
      // whist saldo
      for (ScoreItem other : getLocationsMap().getOtherWhistSaldoItems(place)) {
        final int wSaldo = stats.getWhistSaldoMap().get(other.place);
        drawWhistSaldo(g2, wSaldo, locations.get(other), false, place, other);
      }

      // total whist saldo
      final int wSaldo = stats.getWhistSaldoAgainstPlayer(place);
      drawWhistSaldo(g2, wSaldo, locations.get(WHIST_SALDO_TOTAL), true, place, WHIST_SALDO_TOTAL);

      // new mountain and new (closed) pool
      final boolean isVertical = (place == EAST || place == WEST);
      drawNewMountain(g2, stats, locations.get(PLAYER_MOUNT), isVertical, place);
      drawClosedPool(g2, stats, locations.get(PLAYER_POOL), isVertical, place);

      // final mountain and final scores 
      preparePlayerTotalsPen(g2, lafTheme);
      drawFinalMountain(g2, stats, locations.get(FINAL_MOUNT), place);
      drawFinalScore(g2, stats, locations.get(FINAL_SCORE), lafTheme, place);

    } else {
      this.ttLocationsMap.removeLocation(place, WHIST_SALDO_TOTAL, WHIST_EAST_SALDO, WHIST_SOUTH_SALDO,
                                         WHIST_WEST_SALDO, WHIST_NORTH_SALDO, PLAYER_NEW_MOUNT,
                                         PLAYER_AMNIST_MOUNT, PLAYER_FIXED_MOUNT, PLAYER_POOL_CLOSED,
                                         FINAL_MOUNT, FINAL_SCORE);
    }
  }

  /**
   * Draws a number with an oval or a rectangle around it at the
   * given position (x, y) - used for displaying players whist saldo.
   *
   * @param g2         Graphics2D to use.
   * @param saldo      Number to draw.
   * @param point      Coordinates.
   * @param totalSaldo True indicates that this is a total saldo,
   *                   therefore, a rectangle is drawn around the number;
   *                   false draws an oval instead.
   * @param place      player's place.
   * @param item       score board item.
   */
  private void drawWhistSaldo(Graphics2D g2, final int saldo, final Point2D.Double point,
                              boolean totalSaldo, Place place, ScoreItem item) {
    final String str = String.valueOf(saldo);
    final int width = 16 + str.length() * 14;
    final int shapeX = (int) point.x - (10 + (saldo < 0 ? 4 : 0));
    final int shapeY = (int) point.y - 16;
    g2.drawString(str, (int) point.x, (int) point.y);
    Shape shape;
    if (totalSaldo) {
      shape = new Rectangle2D.Double(shapeX, shapeY, width, 20);
      g2.draw(shape);

    } else {
      shape = new Ellipse2D.Double(shapeX, shapeY, width, 20);
      g2.drawOval(shapeX, (int) point.y - 16, width, 20);
    }

    // adding the tooltip location
    this.ttLocationsMap.addShapeLocation(item, place, shape);
  }

  /**
   * Draws new pool value (the closed pool value).
   *
   * @param g2       Graphics2D to use.
   * @param stats    Player statistics object.
   * @param point    Coordinates of the original pool string.
   * @param vertical If true, indicates vertical positioning
   * @param place    player's place.
   */
  private void drawClosedPool(Graphics2D g2, PlayerStatistics stats,
                              final Point2D.Double point, final boolean vertical, Place place) {
    GameResultBean resultBean = PrefCountRegistry.getInstance().getGameResultBean();
    final String str = getStringFromInt(resultBean.getMaxPool());
    final float width = (float) UiService.determineSizeOfString(g2, stats.getPool().toString()).getWidth();
    final float x = (float) point.getX();
    final float y = (float) point.getY();
    Stroke tempStroke = g2.getStroke();
    g2.setStroke(new BasicStroke(2));
    g2.draw(new Line2D.Float(x, y - 2f, x + width + 2f, y - 8f));
    g2.setStroke(tempStroke);

    Point2D.Double bPoint;
    if (vertical) {
      g2.drawString(str, x, y + 20f);
      bPoint = new Point2D.Double(x, y + 20d);

    } else {
      final float mX = x + 7f + width;
      g2.drawString(str, mX, y);
      bPoint = new Point2D.Double(mX, y);
    }

    this.ttLocationsMap.addRectangleLocation(PLAYER_POOL_CLOSED, place, g2, bPoint, str);
  }

  /**
   * Draws new mountain values (mount after closed pool,
   * amnisted mountain, and the mount fix if any).
   *
   * @param g2       Graphics2D to use.
   * @param stats    Player statistics object.
   * @param point    Coordinates of the original mountain string.
   * @param vertical If true, indicates vertical positioning
   * @param place    player's place.
   */
  private void drawNewMountain(Graphics2D g2, PlayerStatistics stats,
                               final Point2D.Double point, final boolean vertical, Place place) {
    int newMount = stats.getNewMountain();
    final String newMountStr = getStringFromInt(newMount);
    final float width1 = (float) UiService.determineSizeOfString(g2, stats.getMountain().toString()).getWidth();
    final int amnistMount = newMount - stats.getMinMountain();
    final String amnistMountStr = amnistMount + ".";
    final String mountFixStr = stats.getMountFix() == null ? null :
                               (amnistMount + stats.getMountFix()) + ".";
    final float x = (float) point.getX();
    final float y = (float) point.getY();
    Stroke tempStroke = g2.getStroke();
    g2.setStroke(new BasicStroke(2));
    g2.draw(new Line2D.Float(x, y - 2f, x + width1 + 2f, y - 8f));
    g2.setStroke(tempStroke);

    Point2D.Double newMountPoint;
    Point2D.Double amnistMountPoint;
    Point2D.Double fixMountPoint = null;
    if (vertical) {
      g2.drawString(newMountStr, x, y + 20f);
      newMountPoint = new Point2D.Double(x, y + 20d);
      g2.drawString(amnistMountStr, x, y + 40f);
      amnistMountPoint = new Point2D.Double(x, y + 40d);
      if (mountFixStr != null) {
        g2.drawString(mountFixStr, x, y + 60f);
        fixMountPoint = new Point2D.Double(x, y + 60d);
      }
    } else {
      final float mountX = x + width1 + 7f;
      final float width2 = (float) UiService.determineSizeOfString(g2, newMountStr).getWidth();
      g2.drawString(newMountStr, mountX, y);
      newMountPoint = new Point2D.Double(mountX, y);
      final float amnX = mountX + width2 + 2f;
      g2.drawString(amnistMountStr, amnX, y);
      amnistMountPoint = new Point2D.Double(amnX, y);
      if (mountFixStr != null) {
        final float width3 = (float) UiService.determineSizeOfString(g2, mountFixStr).getWidth();
        final float fixX = mountX + width2 + width3 + 4f;
        g2.drawString(mountFixStr, fixX, y);
        fixMountPoint = new Point2D.Double(fixX, y);
      }
    }

    // adding tooltip locations
    this.ttLocationsMap.addRectangleLocation(PLAYER_NEW_MOUNT, place, g2, newMountPoint, newMountStr);
    this.ttLocationsMap.addRectangleLocation(PLAYER_AMNIST_MOUNT, place, g2, amnistMountPoint, amnistMountStr);
    if (fixMountPoint == null) {
      this.ttLocationsMap.removeLocation(place, PLAYER_FIXED_MOUNT);
    } else {
      this.ttLocationsMap.addRectangleLocation(PLAYER_FIXED_MOUNT, place, g2, fixMountPoint, mountFixStr);
    }
  }

  /**
   * Draws new mountain value.
   *
   * @param g2    Graphics2D to use.
   * @param stats Player statistics object.
   * @param point Point's coordinates.
   * @param place player's place.
   */
  private void drawFinalMountain(Graphics2D g2, PlayerStatistics stats,
                                 final Point2D.Double point, Place place) {
    int mount = stats.getFinalMountainInWhists();
    final String str = String.valueOf(mount);
    final int width = 16 + str.length() * 14;
    final int adjustX = 10 + (mount < 0 ? 4 : 0);
    final float x = (float) point.getX();
    final float y = (float) point.getY();
    g2.drawString(str, x, y);
    Shape shape = new Rectangle2D.Double(x - adjustX, y - 16d, width, 20d);
    g2.draw(shape);

    // adding the tooltip location
    this.ttLocationsMap.addShapeLocation(FINAL_MOUNT, place, shape);
  }

  /**
   * Draws final score and a thrombus around it
   * at the given location.
   *
   * @param g2    Graphics2D to use.
   * @param stats Player statistics object.
   * @param point Point's coordinates.
   * @param lafTheme  current skin.
   * @param place player's place.
   */
  private void drawFinalScore(Graphics2D g2, PlayerStatistics stats,
                              final Point2D.Double point, LafTheme lafTheme, Place place) {
    final float x = (float) point.getX();
    final float y = (float) point.getY();
    final String score = String.valueOf(stats.getFinalScoreInWhists());
    g2.drawString(score, x, y);

    final Dimension corrSize = UiService.determineSizeOfString(g2, score);
    final float halfWidth = (float) corrSize.getWidth() / 2;
    final float halfHeight = (float) corrSize.getHeight() / 2;
    final float realCenterX = x + halfWidth;
    final float realCenterY = y - halfHeight + 5f;
    final float leftX = realCenterX - halfWidth - 15f;
    final float rightX = realCenterX + halfWidth + 15f;
    final float upY = realCenterY - halfHeight - 15f;
    final float downY = realCenterY + halfHeight + 15f;

    GeneralPath polygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 4);
    polygon.moveTo(realCenterX, upY);
    polygon.lineTo(rightX, realCenterY);
    polygon.lineTo(realCenterX, downY);
    polygon.lineTo(leftX, realCenterY);
    polygon.closePath();
    g2.draw(polygon);
    g2.setPaint(lafTheme.getFinalScoreBackgroundPaint());
    g2.fill(polygon);

    // adding the tooltip location
    this.ttLocationsMap.addShapeLocation(FINAL_SCORE, place, polygon);
  }

  /**
   * Converts passed Integer to a String appending a '.'
   * ant the end; if the passed value is null an empty string
   * is returned.
   *
   * @param value Integer value to convert.
   * @return String representation of the passed integer
   *         with a period at the end or an empty string
   *         if the passed integer is null.
   */
  private static String getStringFromInt(final Integer value) {
    return value == null ? "" : value + ".";
  }

  /**
   * Sets the color and stroke on the graphics object
   * for the score board lines painting.
   *
   * @param g2   Graphics object.
   * @param lafTheme Current prefcount LAF theme.
   */
  private static void prepareBoardLinePen(Graphics2D g2, LafTheme lafTheme) {
    g2.setStroke(lafTheme.getBoardLineStroke());
    g2.setColor(lafTheme.getBoardLineColor());
  }

  /**
   * Sets the color, stroke and font on the graphics
   * object for the player name painting.
   *
   * @param g2   Graphics object.
   * @param lafTheme Current prefcount skin.
   */
  private static void preparePlayerNamePen(Graphics2D g2, LafTheme lafTheme) {
    g2.setColor(lafTheme.getPlayerNameColor());
    g2.setFont(lafTheme.getPlayerNameFont());
    g2.setStroke(lafTheme.getPlayerNameStroke());
  }

  /**
   * Sets the color, stroke and font on the graphics
   * object for the player score painting.
   *
   * @param g2   Graphics object.
   * @param lafTheme Current prefcount skin.
   */
  private static void preparePlayerScorePen(Graphics2D g2, LafTheme lafTheme) {
    g2.setColor(lafTheme.getPlayerScoreColor());
    g2.setFont(lafTheme.getPlayerScoreFont());
    g2.setStroke(lafTheme.getPlayerScoreStroke());
  }

  /**
   * Sets the color, stroke and font on the graphics
   * object for the player (score) totals painting.
   *
   * @param g2   Graphics object.
   * @param lafTheme Current prefcount skin.
   */
  private static void preparePlayerTotalsPen(Graphics2D g2, LafTheme lafTheme) {
    g2.setColor(lafTheme.getPlayerTotalsColor());
    g2.setFont(lafTheme.getPlayerTotalsFont());
    g2.setStroke(lafTheme.getPlayerTotalsStroke());
  }

  /**
   * Draws player's whists and whist fixes.
   *
   * @param g2        graphics object.
   * @param place     players place.
   * @param stats     players' statistics bean.
   * @param locations locations map.
   */
  private void drawWhistAndWhistFixes(Graphics2D g2, Place place, PlayerStatistics stats,
                                      Map<ScoreItem, Point2D.Double> locations) {
    GameResultBean resultBean = PrefCountRegistry.getInstance().getGameResultBean();
    Point.Double point;
    for (ScoreItem other : getLocationsMap().getOtherWhistItems(place)) {
      point = locations.get(other);
      String whistStr = stats.getWhistsStringForPlayer(other.place);
      g2.drawString(whistStr, (float) point.getX(), (float) point.getY());
      this.ttLocationsMap.addRectangleLocation(other, place, g2, point, whistStr);

      // drawing the whist fixes if any and if the final score is ready
      final ScoreItem otherFix = ScoreItem.getWhistFixForWhist(other.place);
      if (resultBean.isFinalScoresReady()) {
        Integer fix = stats.getWhistFixesMap().get(other.place);
        if (fix != null) {
          String fixStr = (stats.getWhistsAgainstPlayer(other.place) + fix) + ".";
          float fixX = (float) (point.getX() + 2 + UiService.determineSizeOfString(g2, whistStr).getWidth());
          g2.drawString(fixStr, fixX, (float) point.getY());
          Point2D.Double fPoint = new Point2D.Double(fixX, point.getY());
          this.ttLocationsMap.addRectangleLocation(otherFix, place, g2, fPoint, fixStr);

        } else {
          this.ttLocationsMap.removeLocation(place, otherFix);
        }

      } else {
        this.ttLocationsMap.removeLocation(place, otherFix);
      }
    }
  }
}
