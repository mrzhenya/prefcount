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

package net.curre.prefcount.service;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.curre.prefcount.bean.GameResultBean;
import net.curre.prefcount.bean.PlayerStatistics;
import net.curre.prefcount.gui.type.Place;
import net.curre.prefcount.PrefCountRegistry;

/**
 * This service bean is responsible for computing game results.
 * <p/>
 * Created date: Jul 29, 2007
 *
 * @author Yevgeny Nyden
 */
public class ResultService {

  /** Private class logger. */
  private static final Logger log = Logger.getLogger(ResultService.class.toString());

  /**
   * Generates the final player scores. Note that the target
   * pool must be set to a valid (positive) value.
   *
   * @param rBean <code>GameResultBean</code> object that contains
   *              all necessary game data.
   */
  public static void generateFinalResults(GameResultBean rBean) {

    if (log.isLoggable(Level.FINE)) {
      log.fine("Generating final results for Bean: " + rBean);
    }

    // computing the average and minimum mountain
    int min = Integer.MAX_VALUE;
    int sum = 0;
    int playersNum = rBean.getPlayerStats().size();
    for (Map.Entry<Place, PlayerStatistics> entry : rBean.getPlayerStats().entrySet()) {
      PlayerStatistics stats = entry.getValue();
      int currMountain = stats.getNewMountain();
      sum += currMountain;
      if (currMountain < min) {
        min = currMountain;
      }

      // resetting mount fix and whist values maps
      stats.setMountFix(null);
      stats.getWhistFixesMap().clear();
      stats.getWhistSaldoMap().clear();
    }

    rBean.setMinMountain(min);

    // determining the mount fix for the "divisible by N" option
    int mountFix = 0;
    if (rBean.isMountDivisibleByN()) {
      final int remainder = (sum - min * playersNum) % playersNum;

      if (remainder != 0) {
        askForAdjustmentPlayer();
      }

      mountFix = doFixForDivisibleByN(remainder, rBean.getDivisibleByNPlayer(), rBean.getPlayerStats());
    }
    rBean.setAverageMountain((float) (sum + mountFix) / (float) playersNum - min);

    // computing the whists saldo/balances
    for (Map.Entry<Place, PlayerStatistics> currEntry : rBean.getPlayerStats().entrySet()) {
      PlayerStatistics currStats = currEntry.getValue();
      Place currPlace = currEntry.getKey();
      int totalSaldo = 0;
      for (Map.Entry<Place, PlayerStatistics> otherEntry : rBean.getPlayerStats().entrySet()) {
        Place otherPlace = otherEntry.getKey();
        PlayerStatistics otherStats = otherEntry.getValue();
        if (currPlace != otherPlace) {
          Integer whistSaldo = currStats.getWhistSaldoMap().get(otherPlace);
          if (whistSaldo == null) {
            int currWhist = currStats.getWhistsAgainstPlayer(otherPlace);
            Integer currwhistFix = currStats.getWhistFixAgainstPlayer(otherPlace);
            if (currwhistFix != null) {
              currWhist += currwhistFix;
            }
            int whistAgainst = otherStats.getWhistsAgainstPlayer(currPlace);
            Integer whistFixAgainst = otherStats.getWhistFixAgainstPlayer(currPlace);
            if (whistFixAgainst != null) {
              whistAgainst += whistFixAgainst;
            }
            whistSaldo = currWhist - whistAgainst;
            currStats.getWhistSaldoMap().put(otherPlace, whistSaldo);
            otherStats.getWhistSaldoMap().put(currPlace, -1 * whistSaldo);
          }
          totalSaldo += whistSaldo;
        }
      }
      // saving total saldo under the same (as the player's place) key
      currStats.getWhistSaldoMap().put(currEntry.getKey(), totalSaldo);
    }
    rBean.setFinalScoresReady();
  }

  /**
   * Clears the final player scores.
   *
   * @param rBean <code>GameResultBean</code> object that contains
   *              all necessary game data.
   */
  public static void clearFinalResults(GameResultBean rBean) {
    // clearing the player whist saldo maps
    for (PlayerStatistics stats : rBean.getPlayerStats().values()) {
      stats.getWhistSaldoMap().clear();
    }
    rBean.clearResults();
  }

  /**
   * Computes and sets the additional mount and whist fields
   * for the "Divisible by N" option.
   *
   * @param remainder   mount remainder.
   * @param adjustPlace adjustment player's place.
   * @param playerStats list of player statistics beans.
   * @return computed mount fix that will make the sum of mounts divisible by N.
   */
  private static int doFixForDivisibleByN(int remainder, Place adjustPlace,
                                          Map<Place, PlayerStatistics> playerStats) {
    int mountFix = 0;

    if (remainder != 0) {
      final int playersNum = playerStats.size();

      if (playersNum == 4) {
        if (remainder == 1) {
          // subtracting 1 from the adjustment player
          PlayerStatistics stats = playerStats.get(adjustPlace);
          stats.setMountFix(-1);
          mountFix = -1;
          // writing the whists against this player
          addWhistsFixAgainstSelf(playerStats, adjustPlace, 3);

        } else if (remainder == 2) {
          // nothing for the remainder 2

        } else if (remainder == 3) {
          // adding 1 to the adjustment player
          PlayerStatistics stats = playerStats.get(adjustPlace);
          stats.setMountFix(1);
          mountFix = 1;
          // writing the whists against other players
          addWhistsFixAgainstOthers(playerStats, adjustPlace, 3);
        }

      } else {
        if (remainder == 1) {
          // subtracting 1 from the adjustment player
          PlayerStatistics stats = playerStats.get(adjustPlace);
          stats.setMountFix(-1);
          mountFix = -1;
          // writing the whists against this player
          addWhistsFixAgainstSelf(playerStats, adjustPlace, 3);

        } else if (remainder == 2) {
          // adding 1 to the adjustment player
          PlayerStatistics stats = playerStats.get(adjustPlace);
          stats.setMountFix(1);
          mountFix = 1;
          // writing the whists against other players
          addWhistsFixAgainstOthers(playerStats, adjustPlace, 3);
        }
      }
    }
    return mountFix;
  }

  /**
   * Assists with choosing the adjustment player for the
   * mount divisibility "Divisible by N" option. Note, that in
   * debug mode (when MainWindow is not visible) this player is
   * always set to EAST.
   */
  private static void askForAdjustmentPlayer() {
    PrefCountRegistry reg = PrefCountRegistry.getInstance();
    boolean notDebug = reg.getMainWindow() != null && reg.getMainWindow().isVisible();
    if (notDebug) {
      PrefCountRegistry.getInstance().getChoosePlayerDialog();
    }
  }

  /**
   * Adds whist (additional whist fixes for the "divisible by N" option)
   * agains all other players provided the current player index.
   *
   * @param playerStats list of player statistics beans.
   * @param place       current player place.
   * @param value       whist value to add.
   */
  private static void addWhistsFixAgainstOthers(Map<Place, PlayerStatistics> playerStats,
                                                Place place, int value) {
    PlayerStatistics stats = playerStats.get(place);
    for (Map.Entry<Place, PlayerStatistics> entry : playerStats.entrySet()) {
      if (entry.getKey() != place) {
        stats.getWhistFixesMap().put(entry.getKey(), value);
      }
    }
  }

  /**
   * Adds whist (additional whist fixes for the "divisible by N" option)
   * for all players agains the given player.
   *
   * @param playerStats list of player statistics beans.
   * @param place       current player place.
   * @param value       whist value to add.
   */
  private static void addWhistsFixAgainstSelf(Map<Place, PlayerStatistics> playerStats,
                                              Place place, int value) {
    for (Map.Entry<Place, PlayerStatistics> entry : playerStats.entrySet()) {
      if (entry.getKey() != place) {
        entry.getValue().getWhistFixesMap().put(place, value);
      }
    }
  }
}
