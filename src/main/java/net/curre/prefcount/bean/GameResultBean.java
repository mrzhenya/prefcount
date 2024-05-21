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

import net.curre.prefcount.gui.type.Place;
import net.curre.prefcount.gui.type.WindowComponent;

/**
 * Object of this class represents a bean that holds all
 * score and results for a preferance card game.
 * <p/>
 * Created date: Apr 9, 2007
 *
 * @author Yevgeny Nyden
 */
public class GameResultBean {

  /** Indicates that all player data has been entered or not. */
  private boolean finalScoresReady;

  /** Average mountain for this game. */
  private Float averageMountain;

  /** Minimum mountain in this game. */
  private Integer minMountain;

  /**
   * Flag to indicate the Leningradka pref type.
   * This value matters for the pool/mount ratio value.
   * When this flag is true 1 pool = 2 mount points, when
   * it is false 1 pool = 1 mount.
   */
  private boolean leningradka;

  /** Flag to indicate user preferences for mount divisibility. */
  private boolean mountDivisibleByN;

  /** List with player statistics. */
  private Map<Place, PlayerStatistics> playerStats;

  /** Player used for the divisible by N adjustments. */
  private Place divisibleByNPlayer;

  /** Default ctor. */
  public GameResultBean() {
    this.leningradka = Settings.DEFAULT_PREF_TYPE.equals(WindowComponent.LENINGRAD.name());
    this.mountDivisibleByN = Settings.DEFAULT_DIVISIBLE_BY.equals(WindowComponent.DIVISIBLE_BY_N.name());
    this.divisibleByNPlayer = Settings.DEFAULT_ADJ_PLAYER;
    this.minMountain = 0;
  }

  /**
   * Getter for the flag that indicates that the player
   * final scores are ready for display.
   *
   * @return True if all the player final scores have been generated;
   *         false otherwise.
   */
  public boolean isFinalScoresReady() {
    return this.finalScoresReady;
  }

  /**
   * Sets the flag that indicates that the player
   * final scores are ready for display to true.
   */
  public void setFinalScoresReady() {
    this.finalScoresReady = true;
  }

  /**
   * Getter for the max pool value.
   *
   * @return The max pool.
   */
  public Integer getMaxPool() {
    int maxPool = 0;
    if (this.playerStats != null) {
      for (PlayerStatistics stat : this.playerStats.values()) {
        Integer pool = stat.getPool();
        if (pool != null && pool > maxPool) {
          maxPool = pool;
        }
      }
    }
    return maxPool;
  }

  /**
   * Getter for the player's average mountain.
   *
   * @return The player's average mountain.
   */
  public Float getAverageMountain() {
    return this.averageMountain;
  }

  /**
   * Setter for the player's average mountain.
   * This method will round the float number to the 2 digit after
   * the decimal.
   *
   * @param averageMountain Player's average mountain to set.
   */
  public void setAverageMountain(Float averageMountain) {
    if (averageMountain != null) {
      averageMountain = Math.round(averageMountain * 100) / 100F;
    }
    this.averageMountain = averageMountain;
  }

  /**
   * Returns the minimum mountain in the game.
   *
   * @return the minimum mountain in the game.
   */
  public Integer getMinMountain() {
    return this.minMountain;
  }

  /**
   * Setter for the minimum mountain in the game.
   *
   * @param minMountain Min mountain in the game to set.
   */
  public void setMinMountain(Integer minMountain) {
    this.minMountain = minMountain;
  }

  /**
   * Getter for the Leningradka pref type flag.
   * This value matters for the pool/mount ratio value.
   * When this flag is true 1 pool = 2 mount points, when
   * it is false 1 pool = 1 mount.
   *
   * @return true when the pref type is Leningradka; false otherwise.
   */
  public boolean isLeningradka() {
    return this.leningradka;
  }

  /**
   * Setter for property 'leningradka'.
   * This value matters for the pool/mount ratio value.
   * When this flag is true 1 pool = 2 mount points, when
   * it is false 1 pool = 1 mount.
   *
   * @param leningradka Value to set for property 'leningradka'.
   */
  public void setLeningradka(boolean leningradka) {
    this.leningradka = leningradka;
  }

  /**
   * Getter for property 'mountNotDivisible'.
   *
   * @return true if mount divisibility does not matter;
   *         false - assumes divisible by N (number of players).
   */
  public boolean isMountDivisibleByN() {
    return this.mountDivisibleByN;
  }

  /**
   * Setter for the mount divisibility flag.
   *
   * @param mountDivisibleByN true if divisibility does not matter;
   *                          false - divisible by N (players number).
   */
  public void setMountDivisibleByN(boolean mountDivisibleByN) {
    this.mountDivisibleByN = mountDivisibleByN;
  }

  /**
   * Getter for the map of player statistics.
   *
   * @return Map of player statistics.
   */
  public Map<Place, PlayerStatistics> getPlayerStats() {
    return this.playerStats;
  }

  /**
   * Setter for the player statistics map.
   *
   * @param playerStats Map of player statistics.
   */
  public void setPlayerStats(Map<Place, PlayerStatistics> playerStats) {
    this.playerStats = playerStats;
  }

  /**
   * Setter for divisibleByNPlayer.
   *
   * @param divisibleByNPlayer Player used for the divisible by N adjustments.
   */
  public void setDivisibleByNPlayer(Place divisibleByNPlayer) {
    this.divisibleByNPlayer = divisibleByNPlayer;
  }

  /**
   * Getter for property 'divisibleByNPlayer' -
   * player used for the divisible by N adjustments.
   *
   * @return Value for property 'divisibleByNPlayer'.
   */
  public Place getDivisibleByNPlayer() {
    return divisibleByNPlayer;
  }

  /**
   * Returns the number of players in the game.
   *
   * @return The number of players in the game.
   */
  public int getNumberOfPlayers() {
    return this.playerStats.size();
  }

  /** * Clears state associated with a game session. */
  public void clearResults() {
    divisibleByNPlayer = Settings.DEFAULT_ADJ_PLAYER;
    this.finalScoresReady = false;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return super.toString() + ": finalScoresReady=" + this.finalScoresReady +
        ", averageMountain=" + this.averageMountain +
        ", minMountain=" + this.minMountain +
        ", divisibleByNPlayer=" + this.divisibleByNPlayer + ';';
  }
}
