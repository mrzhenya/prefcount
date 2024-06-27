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

import java.util.HashMap;
import java.util.Map;

import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.gui.type.Place;
import net.curre.prefcount.gui.type.PrefType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.validation.constraints.Null;

/**
 * A container for all players scores and results for a preferance card game.
 * <p/>
 * Created date: Apr 9, 2007
 *
 * @author Yevgeny Nyden
 */
public class GameResultBean {

  /** Private class logger. */
  private static final Logger logger = LogManager.getLogger(GameResultBean.class.getName());

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

  /** Player used for the divisible by N adjustments. */
  private Place divisibleByNPlayer;

  /** Map of players stats/results. */
  private Map<Place, PlayerStatistics> playerStats;

  /** Default ctor. */
  public GameResultBean() {
    Settings settings = PrefCountRegistry.getInstance().getSettingsService().getSettings();
    this.leningradka = settings.getPrefType() == PrefType.LENINGRAD;
    this.divisibleByNPlayer = null;
    this.minMountain = 0;

    this.playerStats = new HashMap<>();
    this.resetNumberOfPlayers(settings.getNumberOfPlayers());
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
   *
   * @param ready true if the scores are ready; false if otherwise
   */
  public void setFinalScoresReady(boolean ready) {
    this.finalScoresReady = ready;
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
   * Getter for property 'divisibleByNPlayer' -
   * player used for the divisible by N adjustments.
   *
   * @return Value for property 'divisibleByNPlayer'.
   */
  public Place getDivisibleByNPlayer() {
    return this.divisibleByNPlayer;
  }

  /**
   * Determines if mount should be divisible by N.
   *
   * @return true if mount is divisible by N; false if divisibility of mount doesn't matter.
   */
  public boolean isMountDivisibleByN() {
    return this.divisibleByNPlayer != null;
  }

  /**
   * Setter for the mount divisibility flag.
   *
   * @param divisibleByNPlayer Player's place when divisible by N; or null if divisibility does not matter.
   */
  public void setMountDivisibleByN(@Null Place divisibleByNPlayer) {
    this.divisibleByNPlayer = divisibleByNPlayer;
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
   * Updates player stats data given an updated set of players.
   *
   * @param playerNamesMap player names map.
   */
  public void updateNumberOfPlayers(Map<Place, String> playerNamesMap) {
    logger.info("Updating player stats from {}", this.playerNamesMapToString(playerNamesMap));
    // Update player names in the current stats map.
    for (Map.Entry<Place, PlayerStatistics> entry : this.playerStats.entrySet()) {
      String name = playerNamesMap.get(entry.getKey());
      if (name != null) {
        entry.getValue().setPlayerName(name);
      }
    }
    // Update the stats map according to the current number of players.
    if (playerNamesMap.size() == 4 && this.playerStats.size() == 3) {
      this.playerStats.put(Place.NORTH, new PlayerStatistics(this, Place.NORTH));
    } else if (playerNamesMap.size() == 3 && this.playerStats.size() == 4) {
      this.playerStats.remove(Place.NORTH);
    }

    logger.info("The new players infos: {}", this.playerStatsToString(this.playerStats));
  }

  /**
   * Resets player stats data give a new number of players.
   *
   * @param numberOfPlayers new number of players.
   */
  public void resetNumberOfPlayers(int numberOfPlayers) {
    this.playerStats.clear();
    for (Place place : Place.getPlaces(numberOfPlayers)) {
      this.playerStats.put(place, new PlayerStatistics(this, place));
    }
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

  /**
   * Sets the player stats directly.
   *
   * @param stats player stats to set.
   */
  public void setPlayerStats(Map<Place, PlayerStatistics> stats) {
    this.playerStats = stats;
  }

  /**
   * Converts given players map to string.
   *
   * @param playerNamesMap place-to-name players map.
   * @return a string that represents the given players map.
   */
  private String playerNamesMapToString(Map<Place, String> playerNamesMap) {
    StringBuilder out = new StringBuilder("[").append(playerNamesMap.size()).append("]: ");
    playerNamesMap.forEach((key, value) -> out.append(key).append("-").append(value).append(", "));
    return out.toString();
  }

  /**
   * Converts given player stats to string.
   *
   * @param playerStats player stats map to convert to string.
   * @return a string that represents the given stats map.
   */
  private String playerStatsToString(Map<Place, PlayerStatistics> playerStats) {
    StringBuilder out = new StringBuilder("[" + playerStats.size() + "]:\n");
    playerStats.forEach((key, value) -> out.append("   ").append(key).append(": ").append(value.toString()).append("\n"));
    return out.toString();
  }
}
