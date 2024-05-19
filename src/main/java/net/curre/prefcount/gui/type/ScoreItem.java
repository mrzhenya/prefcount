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

package net.curre.prefcount.gui.type;

import static net.curre.prefcount.gui.type.Place.EAST;
import static net.curre.prefcount.gui.type.Place.NORTH;
import static net.curre.prefcount.gui.type.Place.SOUTH;
import static net.curre.prefcount.gui.type.Place.WEST;

/**
 * This enum represents score item on the score board
 * to be used as keys in the score board location map.
 * <p/>
 * Created date: May 29, 2008
 *
 * @author Yevgeny Nyden
 */
public enum ScoreItem {

  /** Key for the name's location. */
  PLAYER_NAME(null, "pref.scoreboard.tooltip.player", false),

  /** Key for the mountain's location. */
  PLAYER_MOUNT(null, "pref.scoreboard.tooltip.mount", false),

  /** Key for the new mountain's location. */
  PLAYER_NEW_MOUNT(null, "pref.scoreboard.tooltip.newMount", false),

  /** Key for the amnisted mountain's location. */
  PLAYER_AMNIST_MOUNT(null, "pref.scoreboard.tooltip.amnistMount", false),

  /** Key for the fixed mountain's location. */
  PLAYER_FIXED_MOUNT(null, "pref.scoreboard.tooltip.mountFixed", false),

  /** Key for the pool's location. */
  PLAYER_POOL(null, "pref.scoreboard.tooltip.pool", false),

  /** Key for the closed pool's location. */
  PLAYER_POOL_CLOSED(null, "pref.scoreboard.tooltip.poolClosed", false),

  /** Key for the whist against the North location. */
  WHIST_NORTH(NORTH, "pref.scoreboard.tooltip.whist", true),

  /** Key for the whist against the East location. */
  WHIST_EAST(EAST, "pref.scoreboard.tooltip.whist", true),

  /** Key for the whist against the South location. */
  WHIST_SOUTH(SOUTH, "pref.scoreboard.tooltip.whist", true),

  /** Key for the whist against the West location. */
  WHIST_WEST(WEST, "pref.scoreboard.tooltip.whist", true),

  /** Key for the whist fix against the North location. */
  WHIST_FIX_NORTH(NORTH, "pref.scoreboard.tooltip.whistFix", true),

  /** Key for the whist fix against the East location. */
  WHIST_FIX_EAST(EAST, "pref.scoreboard.tooltip.whistFix", true),

  /** Key for the whist fix against the South location. */
  WHIST_FIX_SOUTH(SOUTH, "pref.scoreboard.tooltip.whistFix", true),

  /** Key for the whist fix against the West location. */
  WHIST_FIX_WEST(WEST, "pref.scoreboard.tooltip.whistFix", true),

  /** Key for the whist saldo with the North location. */
  WHIST_NORTH_SALDO(NORTH, "pref.scoreboard.tooltip.whistSaldo", true),

  /** Key for the whist saldo with the East location. */
  WHIST_EAST_SALDO(EAST, "pref.scoreboard.tooltip.whistSaldo", true),

  /** Key for the whist saldo with the South location. */
  WHIST_SOUTH_SALDO(SOUTH, "pref.scoreboard.tooltip.whistSaldo", true),

  /** Key for the whist saldo with the West location. */
  WHIST_WEST_SALDO(WEST, "pref.scoreboard.tooltip.whistSaldo", true),

  /** Key for the total whist saldo location. */
  WHIST_SALDO_TOTAL(null, "pref.scoreboard.tooltip.whistSaldoAll", false),

  /** Key for the final mountain location. */
  FINAL_MOUNT(null, "pref.scoreboard.tooltip.finalMount", false),

  /** Key for the final score location. */
  FINAL_SCORE(null, "pref.scoreboard.tooltip.finalScore", false);

  /**
   * This indicates that the item refers to a particular place
   * (i.e. whists against some player/place).
   */
  public final Place place;

  /** Resource key for this item. */
  public final String key;

  /**
   * Flag that indicates, when true, that other player's
   * name or place is required as an argument for the resource key;
   * false indicates that the current player's name or place
   * should be used as an argument with the resource key.
   */
  public final boolean isOtherPlace;

  /**
   * Constructs a new ScoreItem enum.
   *
   * @param place        optional place this item is associated with.
   * @param key          resource key for this item.
   * @param isOtherPlace other place flag value.
   */
  ScoreItem(Place place, String key, boolean isOtherPlace) {
    this.place = place;
    this.key = key;
    this.isOtherPlace = isOtherPlace;
  }

  /**
   * Determines the whist fix score item for the given place.
   *
   * @param whist whist place.
   * @return whist fix score item that corresponds to the given place.
   * @throws IllegalArgumentException when the passed argument is not supported.
   */
  public static ScoreItem getWhistFixForWhist(Place whist) {
    switch (whist) {
      case EAST:
        return WHIST_FIX_EAST;

      case SOUTH:
        return WHIST_FIX_SOUTH;

      case WEST:
        return WHIST_FIX_WEST;

      case NORTH:
        return WHIST_FIX_NORTH;

      default:
        throw new IllegalArgumentException("Unable to determine whist fix item for place: " + whist + "!");
    }
  }
}
