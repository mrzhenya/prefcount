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

package net.curre.prefcount.bean;

import java.util.Map;
import java.util.TreeMap;
import javax.swing.JTextField;

import net.curre.prefcount.gui.type.Place;
import net.curre.prefcount.service.UiService;

/**
 * Object of this class represents various
 * game scores/data for a player.
 * <p/>
 * Created date: Apr 6, 2007
 *
 * @author Yevgeny Nyden
 */
public class PlayerStatistics {

  /** Player's name. */
  private String playerName;

  /**
   * Players place on the players statistics map
   * in the result bean object.
   */
  private final Place playerPlace;

  /** Player's mountain. */
  private Integer mountain;

  /** Player's pool. */
  private Integer pool;

  /** Player's extra mountain points (when "divisible by N"). */
  private Integer mountFix;

  /**
   * Map of whists that this player has for other players
   * (key = player's place, value = whists).
   */
  private final Map<Place, Integer> whistsMap;

  /**
   * Map that holds whists saldo/balances that this player
   * has against other players; the keys in this map are the
   * other players places and the values are the corresponding
   * whist saldo values;
   * <br /><br />
   * total player's whist saldo/balance is stored under the
   * same (as the player's place) key.
   */
  private final Map<Place, Integer> whistSaldoMap;

  /**
   * Map of whist fixes that this player has against other players
   * (key = player place, value = whist fix). These values may
   * be generated when applying the "Divisible By N" option.
   */
  private final Map<Place, Integer> whistFixesMap;

  /** Reference to the result bean. */
  private final GameResultBean resultBean;

  /**
   * Constructor with a result bean argument.
   *
   * @param resultBean  Reference to the result bean.
   * @param playerPlace Player's place.
   */
  public PlayerStatistics(GameResultBean resultBean, Place playerPlace) {
    this.whistsMap = new TreeMap<>();
    this.whistSaldoMap = new TreeMap<>();
    this.whistFixesMap = new TreeMap<>();
    this.resultBean = resultBean;
    this.playerPlace = playerPlace;
    this.playerName = "";
  }

  /** @inheritDoc */
  @Override
  public String toString() {
    return this.playerPlace + " (" + this.playerName + ") " +
        "pool=" + this.pool +
        "mount=" + this.mountain;
  }

  /**
   * Getter for the player's name.
   *
   * @return Player's name.
   */
  public String getPlayerName() {
    return this.playerName;
  }

  /**
   * Gets player's place.
   *
   * @return player's place.
   */
  public Place getPlayerPlace() {
    return playerPlace;
  }

  /**
   * Getter for the first letter of the player's name
   * (returned capitalized).
   *
   * @return Capitalized first letter of the player's name
   *         or an empty string if player's name is blank.
   */
  public String getPlayerNameLetter() {
    if (this.playerName != null) {
      return (!this.playerName.isEmpty() ?
              this.playerName.substring(0, 1).toUpperCase() :
              "");
    }
    return "";
  }

  /**
   * Setter for the player's name.
   *
   * @param playerName Player's name to set.
   */
  public void setPlayerName(String playerName) {
    this.playerName = playerName;
  }

  /**
   * Getter for the player's mountain.
   *
   * @return Player's mountain value.
   */
  public Integer getMountain() {
    return this.mountain;
  }

  /**
   * Setter for the player's mountain.
   *
   * @param mountain Player's new mountain value.
   */
  public void setMountain(Integer mountain) {
    this.mountain = mountain;
  }

  /**
   * Returns the new computed mountain value according
   * to the current maxPool value and the type of pref
   * (Leningradka or other).
   *
   * @return New computed mountain value.
   */
  public int getNewMountain() {
    return this.mountain - (this.resultBean.isLeningradka() ? 2 : 1)
                           * (this.pool - this.resultBean.getMaxPool());
  }

  /**
   * Returns the "final" computed mountain value in whists (x10),
   * which is = (averageMountain - (mountain + mountFix - minMountain)) x 10.
   * If mount divisibility is ignored, mountFix is always 0.
   *
   * @return Final computed mountain value in whists (x10).
   */
  public int getFinalMountainInWhists() {
    final int mountFix = this.mountFix == null ? 0 : this.mountFix;
    return (int) ((this.resultBean.getAverageMountain() -
                   (getNewMountain() + mountFix - this.resultBean.getMinMountain())) * 10);
  }

  /**
   * Setter for the player's mountain that is fetched
   * from the passed <code>JTextField</code> argument.
   *
   * @param field <code>JTextField</code> to use to fetch the mountain value.
   */
  public void setMountainFromField(JTextField field) {
    this.mountain = UiService.parseIntFromTextField(field);
  }

  /**
   * Getter for the player's pool value.
   *
   * @return Player's pool value.
   */
  public Integer getPool() {
    return this.pool;
  }

  /**
   * Setter for the player's pool value.
   *
   * @param pool Player's new pool value.
   */
  public void setPool(Integer pool) {
    this.pool = pool;
  }

  /**
   * Getter for property 'mountFix'.
   * Player's extra mountain points (when "divisible by N").
   *
   * @return Value for property 'mountFix'.
   */
  public Integer getMountFix() {
    return this.mountFix;
  }

  /**
   * Setter for property 'mountFix'.
   * Player's extra mountain points (when "divisible by N").
   *
   * @param mountFix Value to set for property 'mountFix'.
   */
  public void setMountFix(Integer mountFix) {
    this.mountFix = mountFix;
  }

  /**
   * Setter for the player's pool that is fetched
   * from the passed <code>JTextField</code> argument.
   *
   * @param field <code>JTextField</code> to use to fetch the pool value.
   */
  public void setPoolFromField(JTextField field) {
    this.pool = UiService.parseIntFromTextField(field);
  }

  /**
   * Setter for the player's whists that this player
   * has against the player referred by its index argument.
   *
   * @param place Other player's place that this player has whists against.
   * @param field <code>JTextField</code> to use to fetch the whists value.
   * @return Fetched whists value as an <code>Integer</code> object.
   */
  public Integer setWhistsForPlayerFromField(Place place, JTextField field) {
    int val = UiService.parseIntFromTextField(field);
    this.whistsMap.put(place, val);
    return val;
  }

  /**
   * Returns whist that the current player
   * has against the given player.
   *
   * @param place Player's place to fetch the whists for.
   * @return whist that this player has against the other player.
   */
  public Integer getWhistsAgainstPlayer(Place place) {
    return this.whistsMap.get(place);
  }

  /**
   * Returns whists for the given player (referred by its index);
   * value is returned as a string terminated with a period.
   *
   * @param place Player's place to fetch the whists for.
   * @return whists value that this player has against the other player.
   */
  public String getWhistsStringForPlayer(Place place) {
    Integer val = getWhistsAgainstPlayer(place);
    return val == null ? "" : val + ".";
  }

  /**
   * Returns computed final score (balance) in whists.
   *
   * @return Computed final score (balance) in whists.
   */
  public int getFinalScoreInWhists() {
    Integer wSaldo = this.whistSaldoMap.get(this.playerPlace);
    return getFinalMountainInWhists() + (wSaldo == null ? 0 : wSaldo);
  }

  /**
   * Gets the map that holds whist saldo/balances that this player
   * has against other players; the keys in this map are the
   * other players places and the values are the corresponding
   * whist saldo values;
   * <br /><br />
   * total player's whist saldo/balance is stored under the
   * same (as the player's place) key.
   *
   * @return Map of whist saldo values that the player has
   *         against other players.
   */
  public Map<Place, Integer> getWhistSaldoMap() {
    return this.whistSaldoMap;
  }

  /**
   * Gets whist saldo against the given player.
   *
   * @param place other player's place.
   * @return whist saldo against the given player.
   */
  public Integer getWhistSaldoAgainstPlayer(Place place) {
    return this.whistSaldoMap.get(place);
  }

  /**
   * Gets the map that holds whist fixes that this player has
   * against other players (key = player place, value = whist fix).
   * These values may be generated when applying the "Divisible By N"
   * option.
   *
   * @return Map that holds whist fixes that this player has
   *         against other players.
   */
  public Map<Place, Integer> getWhistFixesMap() {
    return this.whistFixesMap;
  }

  /**
   * Gets whist fix against the given player.
   *
   * @param place other player's place.
   * @return whist fix against the given player.
   */
  public Integer getWhistFixAgainstPlayer(Place place) {
    return this.whistFixesMap.get(place);
  }

  /**
   * Returns the minimum mountain in the game.
   *
   * @return the minimum mountain in the game;
   */
  public int getMinMountain() {
    if (this.resultBean != null) {
      return this.resultBean.getMinMountain();
    }
    return 0;
  }
}
