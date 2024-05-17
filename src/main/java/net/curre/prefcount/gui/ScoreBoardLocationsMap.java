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

package net.curre.prefcount.gui;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

import net.curre.prefcount.gui.type.Place;
import static net.curre.prefcount.gui.type.Place.EAST;
import static net.curre.prefcount.gui.type.Place.NORTH;
import static net.curre.prefcount.gui.type.Place.SOUTH;
import static net.curre.prefcount.gui.type.Place.WEST;
import net.curre.prefcount.gui.type.ScoreItem;
import static net.curre.prefcount.gui.type.ScoreItem.FINAL_MOUNT;
import static net.curre.prefcount.gui.type.ScoreItem.FINAL_SCORE;
import static net.curre.prefcount.gui.type.ScoreItem.PLAYER_MOUNT;
import static net.curre.prefcount.gui.type.ScoreItem.PLAYER_NAME;
import static net.curre.prefcount.gui.type.ScoreItem.PLAYER_POOL;
import static net.curre.prefcount.gui.type.ScoreItem.WHIST_EAST;
import static net.curre.prefcount.gui.type.ScoreItem.WHIST_EAST_SALDO;
import static net.curre.prefcount.gui.type.ScoreItem.WHIST_NORTH;
import static net.curre.prefcount.gui.type.ScoreItem.WHIST_NORTH_SALDO;
import static net.curre.prefcount.gui.type.ScoreItem.WHIST_SALDO_TOTAL;
import static net.curre.prefcount.gui.type.ScoreItem.WHIST_SOUTH;
import static net.curre.prefcount.gui.type.ScoreItem.WHIST_SOUTH_SALDO;
import static net.curre.prefcount.gui.type.ScoreItem.WHIST_WEST;
import static net.curre.prefcount.gui.type.ScoreItem.WHIST_WEST_SALDO;

/**
 * This class is a utility to determine locaitons of
 * all items on the score board given the current dimensions
 * of the table/board.
 * <p/>
 * Created date: Jun 16, 2007
 *
 * @author Yevgeny Nyden
 */
public class ScoreBoardLocationsMap {

  /** Number of pixels to leave near the borders. */
  public static final int MARGIN = 7;

  /** Current board width. */
  public int width;

  /** Current board height. */
  public int height;

  /** Center's current X coordinate. */
  public int centerX;

  /** Center's current X coordinate. */
  public int centerY;

  /** Computed 2/5 of half width value. */
  public int twoFifthX;

  /** Computed 3/5 of the half width value. */
  public int threeFifthX;

  /** Computed 1/5 of the half height value. */
  public int oneFifthY;

  /** Computed 2/5 of the half height value. */
  public int twoFifthY;

  /** Computed 3/5 of the half height value. */
  public int threeFifthY;

  /** Computed whist-pool divider X coordinate. */
  public int whistPoolDividerX;

  /** Computed whist-pool divider Y coordinate. */
  public int whistPoolDividerY;

  /** Computed pool-mount divider X coordinate. */
  public int poolMountDividerX;

  /** Computed pool-mount divider Y coordinate. */
  public int poolMountDividerY;

  /** Computed value for whist division lines. */
  public int whistDividerX1;

  /** Computed value for whist division lines. */
  public int whistDividerX2;

  /** Computed value for whist division lines. */
  public int whistDividerY1;

  /** Computed value for whist division lines. */
  public int whistDividerY2;

  /** Reference to the score board panel. */
  private ScoreBoardPanel scoreBoard;

  /**
   * Total number of players in the game
   * (supported values are 3 and 4)
   */
  private int numberOfPlayers;

  /**
   * Map for each player in the game that
   * store locations of all items on the score board.
   */
  private Map<Place, Map<ScoreItem, Point2D.Double>> locationMaps;

  /**
   * Map that holds the other player's whist score items
   * for each player currently in the game.
   */
  private Map<Place, ScoreItem[]> othersWhists;

  /**
   * Map that holds the other player's whist saldo score items
   * for each player currently in the game.
   */
  private Map<Place, ScoreItem[]> othersWhistSaldos;

  /**
   * Constructor. After the number of players in the
   * game is known (scoreBoard.results.getPlayerStats() is created),
   * the initialize() method must be called.
   *
   * @param scoreBoard Reference to the score board panel.
   */
  public ScoreBoardLocationsMap(ScoreBoardPanel scoreBoard) {
    this.scoreBoard = scoreBoard;
  }

  /**
   * Method that initializes properties and computes
   * the locations for the first time. This method must
   * be called as soon as the number of players is set
   * (scoreBoard.results.getPlayerStats() is created).
   *
   * @param numberOfPlayers Number of players in the game.
   * @throws IllegalArgumentException If number of players is not supported.
   */
  public void initialize(int numberOfPlayers) {
    if (numberOfPlayers != 3 && numberOfPlayers != 4) {
      throw new IllegalArgumentException(numberOfPlayers + " number of players is NOT supported!");
    }

    this.numberOfPlayers = numberOfPlayers;
    this.locationMaps = new HashMap<Place, Map<ScoreItem, Point2D.Double>>();
    for (Place place : Place.getPlaces(numberOfPlayers)) {
      this.locationMaps.put(place, new HashMap<ScoreItem, Point2D.Double>());
    }
    this.width = 0;
    this.height = 0;

    this.othersWhists = new HashMap<Place, ScoreItem[]>(numberOfPlayers);
    this.othersWhistSaldos = new HashMap<Place, ScoreItem[]>(numberOfPlayers);
    for (Place place : Place.getPlaces(numberOfPlayers)) {
      this.othersWhists.put(place, getOtherWhistItemsHelper(place));
      this.othersWhistSaldos.put(place, getOtherWhistSaldoItemsHelper(place));
    }

    final int newWidth = this.scoreBoard.getWidth();
    final int newHeight = this.scoreBoard.getHeight();

    computeLocations(newWidth, newHeight, 0, 0, true);
  }

  /**
   * Getter for the locations maps for the given player.
   *
   * @param place Players place.
   * @return The locations maps for the given player.
   */
  public Map<ScoreItem, Point2D.Double> getLocationsMap(Place place) {
    return this.locationMaps.get(place);
  }

  /**
   * Computes or recomputes if necessary locations of
   * all items on the score board and stores the values
   * in the locationsMap map.
   *
   * @param newWidth  new width of the score board.
   * @param newHeight new height of the score board.
   * @param offsetX   canvas X coordinate.
   * @param offsetY   canvas Y coordinate.
   * @param force     true will ensure that all locations will get recomputed;
   *                  when false, the method does nothing if the scoreboard width.
   */
  public void computeLocations(int newWidth, int newHeight, int offsetX, int offsetY, boolean force) {

    // if the frame size didn't change from last time,
    // returning - we can use the already computed values
    if (force == false && this.width == newWidth && this.height == newHeight) {
      return;
    }

    this.width = newWidth;
    this.height = newHeight;
    final int halfWidth = newWidth / 2;
    final int halfHeight = newHeight / 2;

    this.centerX = newWidth / 2 + offsetX;
    this.centerY = newHeight / 2 + offsetY;
    this.twoFifthX = 2 * halfWidth / 5 + offsetX;
    this.oneFifthY = halfHeight / 5 + offsetY;
    this.twoFifthY = 2 * halfHeight / 5 + 2 + offsetY;
    this.threeFifthX = 3 * halfWidth / 5 + 2 + offsetX;
    this.threeFifthY = 3 * halfHeight / 5 + 2 + offsetY;
    this.whistPoolDividerX = newWidth - (2 * halfWidth / 5) - 1 + offsetX;
    this.whistPoolDividerY = newHeight - (2 * halfHeight / 5) - 2 + offsetY;
    this.poolMountDividerX = newWidth - (3 * halfWidth / 5) - 2 + offsetX;
    this.poolMountDividerY = newHeight - (3 * halfHeight / 5) - 2 + offsetY;

    final int whistThirdX = 2 * halfWidth / 5;
    this.whistDividerX1 = this.twoFifthX + whistThirdX - (whistThirdX / 5);
    this.whistDividerX2 = this.whistPoolDividerX - whistThirdX + (whistThirdX / 5);
    final int whistThirdY = 2 * halfHeight / 5;
    this.whistDividerY1 = this.twoFifthY + whistThirdY - (whistThirdY / 5);
    this.whistDividerY2 = this.whistPoolDividerY - whistThirdY + (whistThirdY / 5);

    final int topY = MARGIN + 17 + offsetY;
    final int bottomY = newHeight - MARGIN - 5 + offsetY;

    // generating score board items locations
    switch (this.numberOfPlayers) {
      case (3):
        // computing the EAST player items locations
        Map<ScoreItem, Point2D.Double> m = this.locationMaps.get(EAST);
        m.put(PLAYER_NAME, new Point2D.Double(this.centerX + 11, this.centerY));
        m.put(PLAYER_MOUNT, new Point2D.Double(this.centerX + 14, topY));
        m.put(PLAYER_POOL, new Point2D.Double((newWidth / 2) + this.twoFifthX + 10, topY));
        m.put(WHIST_SOUTH, new Point2D.Double(this.whistPoolDividerX + 20, this.poolMountDividerY));
        m.put(WHIST_WEST, new Point2D.Double(this.whistPoolDividerX + 20, topY + 23));
        m.put(WHIST_SOUTH_SALDO, new Point2D.Double(this.whistPoolDividerX + 30, this.poolMountDividerY + 40));
        m.put(WHIST_WEST_SALDO, new Point2D.Double(this.whistPoolDividerX + 30, topY + 53));
        m.put(WHIST_SALDO_TOTAL, new Point2D.Double(this.whistPoolDividerX + 20, this.centerY));
        m.put(FINAL_MOUNT, new Point2D.Double(this.centerX + 40, (halfHeight / 2) + offsetY));
        m.put(FINAL_SCORE, new Point2D.Double(this.poolMountDividerX + 10, this.centerY - 30));

        // computing the SOUTH player items locations
        m = this.locationMaps.get(SOUTH);
        m.put(PLAYER_NAME, new Point2D.Double(this.centerX - 7, this.centerY + 24));
        m.put(PLAYER_MOUNT, new Point2D.Double(this.threeFifthX + 50, this.poolMountDividerY - 8));
        m.put(PLAYER_POOL, new Point2D.Double(this.twoFifthX + 50, this.whistPoolDividerY - 10));
        m.put(WHIST_EAST, new Point2D.Double(this.centerX + 20, bottomY));
        m.put(WHIST_WEST, new Point2D.Double(MARGIN + 50 + offsetX, bottomY));
        m.put(WHIST_EAST_SALDO, new Point2D.Double(this.whistPoolDividerX - 20, bottomY - 26));
        m.put(WHIST_WEST_SALDO, new Point2D.Double(this.twoFifthX - 5, bottomY - 26));
        m.put(WHIST_SALDO_TOTAL, new Point2D.Double(this.centerX + 20, bottomY - 30));
        m.put(FINAL_MOUNT, new Point2D.Double(this.centerX, (newHeight / 2) + this.oneFifthY + 10));
        m.put(FINAL_SCORE, new Point2D.Double(this.centerX, this.whistPoolDividerY - 10));

        // computing the WEST player items locations
        m = this.locationMaps.get(WEST);
        m.put(PLAYER_NAME, new Point2D.Double(this.centerX - 20, this.centerY));
        m.put(PLAYER_MOUNT, new Point2D.Double(this.threeFifthX + 12, topY));
        m.put(PLAYER_POOL, new Point2D.Double(this.twoFifthX + 10, topY));
        m.put(WHIST_EAST, new Point2D.Double(MARGIN + 20 + offsetX, topY + 23));
        m.put(WHIST_SOUTH, new Point2D.Double(MARGIN + 20 + offsetX, this.poolMountDividerY));
        m.put(WHIST_EAST_SALDO, new Point2D.Double(MARGIN + 40 + offsetX, topY + 53));
        m.put(WHIST_SOUTH_SALDO, new Point2D.Double(MARGIN + 40 + offsetX, this.poolMountDividerY + 40));
        m.put(WHIST_SALDO_TOTAL, new Point2D.Double(MARGIN + 44 + offsetX, this.centerY));
        m.put(FINAL_MOUNT, new Point2D.Double(this.threeFifthX + 20, halfHeight / 2 + offsetY));
        m.put(FINAL_SCORE, new Point2D.Double(this.twoFifthX + 10, this.centerY - 30));
        break;

      case (4):
        // computing the NORTH player items locations
        m = this.locationMaps.get(NORTH);
        m.put(PLAYER_NAME, new Point2D.Double(this.centerX - 5, this.centerY - 10));
        m.put(PLAYER_MOUNT, new Point2D.Double(this.threeFifthX + 40, this.threeFifthY + 22));
        m.put(PLAYER_POOL, new Point2D.Double(this.twoFifthX + 50, this.twoFifthY + 22));
        m.put(WHIST_EAST, new Point2D.Double(this.whistDividerX2 + 20, topY));
        m.put(WHIST_SOUTH, new Point2D.Double(this.whistDividerX1 + 20, topY));
        m.put(WHIST_WEST, new Point2D.Double(MARGIN + 40 + offsetX, topY));
        m.put(WHIST_EAST_SALDO, new Point2D.Double(this.whistDividerX2 + 60, topY + 20));
        m.put(WHIST_SOUTH_SALDO, new Point2D.Double(this.whistDividerX1 + 60, topY + 20));
        m.put(WHIST_WEST_SALDO, new Point2D.Double(MARGIN + 80 + offsetX, topY + 20));
        m.put(WHIST_SALDO_TOTAL, new Point2D.Double(this.centerX, topY + 44));
        m.put(FINAL_MOUNT, new Point2D.Double(this.centerX, this.threeFifthY + 46));
        m.put(FINAL_SCORE, new Point2D.Double(this.centerX, this.twoFifthY + 30));

        // computing the EAST player items locations
        m = this.locationMaps.get(EAST);
        m.put(PLAYER_NAME, new Point2D.Double(this.centerX + 14, this.centerY + 6));
        m.put(PLAYER_MOUNT, new Point2D.Double(this.poolMountDividerX - 44, this.threeFifthY + 56));
        m.put(PLAYER_POOL, new Point2D.Double(this.whistPoolDividerX - 32, this.twoFifthY + 56));
        m.put(WHIST_NORTH, new Point2D.Double(this.whistPoolDividerX + 20, this.twoFifthY + 10));
        m.put(WHIST_SOUTH, new Point2D.Double(this.whistPoolDividerX + 20, this.whistDividerY2 + 30));
        m.put(WHIST_WEST, new Point2D.Double(this.whistPoolDividerX + 20, this.whistDividerY1 + 30));
        m.put(WHIST_NORTH_SALDO, new Point2D.Double(this.whistPoolDividerX + 30, this.twoFifthY + 36));
        m.put(WHIST_SOUTH_SALDO, new Point2D.Double(this.whistPoolDividerX + 30, this.whistDividerY2 + 56));
        m.put(WHIST_WEST_SALDO, new Point2D.Double(this.whistPoolDividerX + 30, this.whistDividerY1 + 56));
        m.put(WHIST_SALDO_TOTAL, new Point2D.Double(this.whistPoolDividerX + 20, this.centerY + 20));
        m.put(FINAL_MOUNT, new Point2D.Double(this.centerX + 70, this.centerY + 34));
        m.put(FINAL_SCORE, new Point2D.Double(this.poolMountDividerX + 10, this.centerY - 10));

        // computing the SOUTH player items locations
        m = this.locationMaps.get(SOUTH);
        m.put(PLAYER_NAME, new Point2D.Double(this.centerX - 7, this.centerY + 24));
        m.put(PLAYER_MOUNT, new Point2D.Double(this.threeFifthX + 40, this.poolMountDividerY - 8));
        m.put(PLAYER_POOL, new Point2D.Double(this.twoFifthX + 50, this.whistPoolDividerY - 10));
        m.put(WHIST_EAST, new Point2D.Double(this.whistDividerX2 + 20, bottomY));
        m.put(WHIST_NORTH, new Point2D.Double(this.whistDividerX1 + 20, bottomY));
        m.put(WHIST_WEST, new Point2D.Double(MARGIN + 40 + offsetX, bottomY));
        m.put(WHIST_EAST_SALDO, new Point2D.Double(this.whistDividerX2 + 60, bottomY - 20));
        m.put(WHIST_NORTH_SALDO, new Point2D.Double(this.whistDividerX1 + 60, bottomY - 20));
        m.put(WHIST_WEST_SALDO, new Point2D.Double(MARGIN + 80 + offsetX, bottomY - 20));
        m.put(WHIST_SALDO_TOTAL, new Point2D.Double(this.centerX, bottomY - 44));
        m.put(FINAL_MOUNT, new Point2D.Double(this.centerX, this.centerY + (halfHeight / 5) + 16));
        m.put(FINAL_SCORE, new Point2D.Double(this.centerX, this.whistPoolDividerY - 10));

        // computing the WEST player items locations
        m = this.locationMaps.get(WEST);
        m.put(PLAYER_NAME, new Point2D.Double(this.centerX - 22, this.centerY + 6));
        m.put(PLAYER_MOUNT, new Point2D.Double(this.threeFifthX + 10, this.threeFifthY + 56));
        m.put(PLAYER_POOL, new Point2D.Double(this.twoFifthX + 10, this.twoFifthY + 56));
        m.put(WHIST_NORTH, new Point2D.Double(MARGIN + 20 + offsetX, this.twoFifthY + 10));
        m.put(WHIST_SOUTH, new Point2D.Double(MARGIN + 20 + offsetX, this.whistDividerY2 + 30));
        m.put(WHIST_EAST, new Point2D.Double(MARGIN + 20 + offsetX, this.whistDividerY1 + 30));
        m.put(WHIST_NORTH_SALDO, new Point2D.Double(MARGIN + 40 + offsetX, this.twoFifthY + 36));
        m.put(WHIST_SOUTH_SALDO, new Point2D.Double(MARGIN + 40 + offsetX, this.whistDividerY2 + 56));
        m.put(WHIST_EAST_SALDO, new Point2D.Double(MARGIN + 40 + offsetX, this.whistDividerY1 + 56));
        m.put(WHIST_SALDO_TOTAL, new Point2D.Double(MARGIN + 32 + offsetX, this.centerY + 20));
        m.put(FINAL_MOUNT, new Point2D.Double(this.threeFifthX + 20, this.centerY + 34));
        m.put(FINAL_SCORE, new Point2D.Double(this.twoFifthX + 10, this.centerY - 10));
        break;

      default:
        throw new UnsupportedOperationException(this.numberOfPlayers +
                                                " number of players is NOT supported!");
    }
  }

  /**
   * Gets whist score items for all other players
   * relative to the given player.
   *
   * @param place player's place.
   * @return whist score items for all opponents of the given player.
   */
  public ScoreItem[] getOtherWhistItems(Place place) {
    return this.othersWhists.get(place);
  }

  /**
   * Gets whist saldo score items for all other players
   * relative to the given player.
   *
   * @param place player's place.
   * @return whist saldo score items for all opponents of the given player.
   */
  public ScoreItem[] getOtherWhistSaldoItems(Place place) {
    return this.othersWhistSaldos.get(place);
  }

  /** Private methods ***********************/

  /**
   * Gets whist score items for all other players
   * relative to the given player.
   *
   * @param place player's place.
   * @return whist score items for all opponents of the given player.
   */
  private ScoreItem[] getOtherWhistItemsHelper(Place place) {
    ScoreItem[] items = new ScoreItem[this.numberOfPlayers - 1];
    switch (place) {
      case EAST:
        items[0] = WHIST_SOUTH;
        items[1] = WHIST_WEST;
        break;

      case SOUTH:
        items[0] = WHIST_EAST;
        items[1] = WHIST_WEST;
        break;

      case WEST:
        items[0] = WHIST_EAST;
        items[1] = WHIST_SOUTH;
        break;

      case NORTH:
        items[0] = WHIST_EAST;
        items[1] = WHIST_SOUTH;
        items[2] = WHIST_WEST;
        break;

      default:
        throw new IllegalArgumentException("Illegal place " + place + "!");
    }
    if (this.numberOfPlayers == 4 && place != NORTH) {
      items[2] = WHIST_NORTH;
    }

    return items;
  }

  /**
   * Gets whist saldo score items for all other players
   * relative to the given player.
   *
   * @param place player's place.
   * @return whist saldo score items for all opponents of the given player.
   */
  private ScoreItem[] getOtherWhistSaldoItemsHelper(Place place) {
    ScoreItem[] items = new ScoreItem[this.numberOfPlayers - 1];
    switch (place) {
      case EAST:
        items[0] = WHIST_SOUTH_SALDO;
        items[1] = WHIST_WEST_SALDO;
        break;

      case SOUTH:
        items[0] = WHIST_EAST_SALDO;
        items[1] = WHIST_WEST_SALDO;
        break;

      case WEST:
        items[0] = WHIST_EAST_SALDO;
        items[1] = WHIST_SOUTH_SALDO;
        break;

      case NORTH:
        items[0] = WHIST_EAST_SALDO;
        items[1] = WHIST_SOUTH_SALDO;
        items[2] = WHIST_WEST_SALDO;
        break;

      default:
        throw new IllegalArgumentException("Illegal place " + place + "!");
    }
    if (this.numberOfPlayers == 4 && place != NORTH) {
      items[2] = WHIST_NORTH_SALDO;
    }

    return items;
  }

}
