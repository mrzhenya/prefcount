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

import java.util.HashMap;
import java.util.Map;

import net.curre.prefcount.bean.GameResultBean;
import net.curre.prefcount.bean.PlayerStatistics;
import net.curre.prefcount.gui.aa.AAJTextField;
import net.curre.prefcount.gui.type.Place;
import static net.curre.prefcount.gui.type.Place.EAST;
import static net.curre.prefcount.gui.type.Place.NORTH;
import static net.curre.prefcount.gui.type.Place.SOUTH;
import static net.curre.prefcount.gui.type.Place.WEST;
import net.curre.prefcount.test.BaseTestCase;

/**
 * This is a junit test for testing result service.
 * <p/>
 * Created date: Jul 29, 2007
 *
 * @author Yevgeny Nyden
 */
public class ResultServiceTest extends BaseTestCase {

  /**
   * Tests GameResultBean and PlayerStatistics beans methods
   * (simple code that contains simple computing functionality).
   */
  public void testGameResultBean() {
    GameResultBean rBean = new GameResultBean();
    rBean.setAverageMountain(47.0F);
    assertEquals("Wrong average mountain value", 47.0F, rBean.getAverageMountain());
    rBean.setMinMountain(12);
    assertEquals("Wrong min mountain value", Integer.valueOf(12), rBean.getMinMountain());

    PlayerStatistics player = new PlayerStatistics(rBean, EAST);
    assertNull("Player name should be null", player.getPlayerName());
    assertEquals("Player name letter should be empty", "", player.getPlayerNameLetter());
    player.setPlayerName("");
    assertEquals("Player name letter should be empty", "", player.getPlayerNameLetter());
    player.setPlayerName("boo");
    assertEquals("Wrong player name letter", "B", player.getPlayerNameLetter());
    assertNotNull("whists saldo map is not initialized!", player.getWhistSaldoMap());

    // testing pool related functionality
    player.setPool(10);
    assertEquals("Wrong pool value", Integer.valueOf(10), player.getPool());
    player.setPoolFromField(new AAJTextField("76"));
    assertEquals("Wrong pool value", Integer.valueOf(76), player.getPool());

    // testing mountain related functionality
    player.setMountain(74);
    assertEquals("Wrong mountain value", Integer.valueOf(74), player.getMountain());
    player.setMountainFromField(null);
    assertEquals("Wrong mountain value", Integer.valueOf(0), player.getMountain());
    player.setMountain(74);
    player.setMountainFromField(new AAJTextField(""));
    assertEquals("Wrong mountain value", Integer.valueOf(0), player.getMountain());
    player.setMountainFromField(new AAJTextField("74"));
    assertEquals("Wrong mountain value", Integer.valueOf(74), player.getMountain());
    assertEquals("Wrong max pool value", Integer.valueOf(0), rBean.getMaxPool());
    assertEquals("Wrong computed new mountain value for Leningradka", -78, player.getNewMountain());
    assertEquals("Wrong computed final mountain in whists value for Leningradka",
                 1370, player.getFinalMountainInWhists());

    // testing whists related functionality
    player.setWhistsForPlayerFromField(SOUTH, new AAJTextField("72"));
    assertEquals("Wrong whists for player 1", Integer.valueOf(72), player.getWhistsAgainstPlayer(SOUTH));
    assertEquals("Wrong whists string for player 2", "", player.getWhistsStringForPlayer(WEST));
    player.setWhistsForPlayerFromField(WEST, new AAJTextField("84"));
    assertEquals("Wrong whists string for player 2", "84.", player.getWhistsStringForPlayer(WEST));

    // remember that player's index is 0, so, his whists saldo is
    // stored in the same map under the key "0" (his index)
    player.getWhistSaldoMap().put(EAST, -28);
    assertEquals("Wrong computed final score value", 1342, player.getFinalScoreInWhists());

    rBean.setLeningradka(false);
    assertEquals("Wrong computed new mountain value for Sochinka", -2, player.getNewMountain());
  }

  /**
   * Tests the generateFinalResults() method.
   */
  public void testGenerateFinalResults() {
    GameResultBean rBean = new GameResultBean();
    rBean.setLeningradka(true);
    rBean.setMountDivisibleByN(false);

    // creating game data for 4 players
    Map<Place, PlayerStatistics> stats = new HashMap<>();
    stats.put(EAST, createPlayerStatHelper(rBean, 4, EAST, "dima", 76, 74,
                                           Whist.n(NORTH, "34"),
                                           Whist.n(SOUTH, "84"),
                                           Whist.n(WEST, "136")));

    stats.put(SOUTH, createPlayerStatHelper(rBean, 4, SOUTH, "kolya", 74, 60,
                                            Whist.n(NORTH, "96"),
                                            Whist.n(EAST, "60"),
                                            Whist.n(WEST, "0")));

    stats.put(WEST, createPlayerStatHelper(rBean, 4, WEST, "fedya", 32, 34,
                                           Whist.n(NORTH, "108"),
                                           Whist.n(EAST, "220"),
                                           Whist.n(SOUTH, "192")));

    stats.put(NORTH, createPlayerStatHelper(rBean, 4, NORTH, "grisha", 22, 76,
                                            Whist.n(EAST, "80"),
                                            Whist.n(SOUTH, "44"),
                                            Whist.n(WEST, "48")));
    rBean.setPlayerStats(stats);

    // generating and testing final results
    ResultService.generateFinalResults(rBean);
    assertTrue("Wrong finalScoreReady value", rBean.isFinalScoresReady());
    assertEquals("Wrong number of players", 4, rBean.getNumberOfPlayers());
    assertEquals("Wrong computed average mountain value", 47.00F, rBean.getAverageMountain());
    assertEquals("Wrong computed minimum mountain value", Integer.valueOf(64), rBean.getMinMountain());
    checkPlayerStats(stats, EAST, 74, 370, -106, 264,
                     Whist.n(SOUTH, "24"), Whist.n(WEST, "-84"), Whist.n(NORTH, "-46"));
    checkPlayerStats(stats, SOUTH, 64, 470, -164, 306,
                     Whist.n(WEST, "-192"), Whist.n(NORTH, "52"), Whist.n(EAST, "-24"));
    checkPlayerStats(stats, WEST, 122, -110, 336, 226,
                     Whist.n(NORTH, "60"), Whist.n(EAST, "84"), Whist.n(SOUTH, "192"));
    checkPlayerStats(stats, NORTH, 184, -730, -66, -796,
                     Whist.n(EAST, "46"), Whist.n(SOUTH, "-52"), Whist.n(WEST, "-60"));

    // clearing results and testing it
    ResultService.clearFinalResults(rBean);
    for (Map.Entry<Place, PlayerStatistics> entry : rBean.getPlayerStats().entrySet()) {
      assertNotNull("whists saldo map for player " + entry.getKey().name() + " is null",
                    entry.getValue().getWhistSaldoMap());
      assertEquals("whists saldo map for player " + entry.getKey().name() + " is not empty",
                   0, entry.getValue().getWhistSaldoMap().size());
    }
    assertFalse("FinalScoreReady flag should be set to false", rBean.isFinalScoresReady());

    // creating game data for 3 players
    rBean = new GameResultBean();
    stats = new HashMap<>();
    stats.put(EAST, createPlayerStatHelper(rBean, 3, EAST, "dariya", 56, 22,
                                           Whist.n(SOUTH, "24"),
                                           Whist.n(WEST, "32")));

    stats.put(SOUTH, createPlayerStatHelper(rBean, 3, SOUTH, "kolya", 22, 22,
                                            Whist.n(EAST, "100"),
                                            Whist.n(WEST, "32")));

    stats.put(WEST, createPlayerStatHelper(rBean, 3, WEST, "fedya", 12, 34,
                                           Whist.n(EAST, "72"),
                                           Whist.n(SOUTH, "56")));
    rBean.setPlayerStats(stats);

    // generating and testing final results
    ResultService.generateFinalResults(rBean);
    assertTrue("Wrong finalScoreReady value", rBean.isFinalScoresReady());
    assertEquals("Wrong number of players", 3, rBean.getNumberOfPlayers());
    assertEquals("Wrong computed average mountain value", 56.00F, rBean.getAverageMountain());
    assertEquals("Wrong computed minimum mountain value", Integer.valueOf(22), rBean.getMinMountain());
    checkPlayerStats(stats, EAST, 22, 560, -116, 444,
                     Whist.n(SOUTH, "-76"), Whist.n(WEST, "-40"));
    checkPlayerStats(stats, SOUTH, 90, -120, 52, -68,
                     Whist.n(WEST, "-24"), Whist.n(EAST, "76"));
    checkPlayerStats(stats, WEST, 122, -440, 64, -376,
                     Whist.n(EAST, "40"), Whist.n(SOUTH, "24"));

    // changing the game type and testing again
    rBean.setLeningradka(false);
    ResultService.generateFinalResults(rBean);
    assertTrue("Wrong finalScoreReady value", rBean.isFinalScoresReady());
    assertEquals("Wrong number of players", 3, rBean.getNumberOfPlayers());
    assertEquals("Wrong computed average mountain value", 30.00F, rBean.getAverageMountain());
    assertEquals("Wrong computed minimum mountain value", Integer.valueOf(22), rBean.getMinMountain());
    checkPlayerStats(stats, EAST, 22, 300, -116, 184,
                     Whist.n(SOUTH, "-76"), Whist.n(WEST, "-40"));
    checkPlayerStats(stats, SOUTH, 56, -40, 52, 12,
                     Whist.n(WEST, "-24"), Whist.n(EAST, "76"));
    checkPlayerStats(stats, WEST, 78, -260, 64, -196,
                     Whist.n(EAST, "40"), Whist.n(SOUTH, "24"));
  }

  /**
   * Tests the generateFinalResults() method
   * with more cases (using the divisible by N option).
   */
  public void testGenerateFinalResults3() {
    GameResultBean rBean = new GameResultBean();
    rBean.setLeningradka(true);
    rBean.setMountDivisibleByN(false);

    // creating game data for 3 players
    Map<Place, PlayerStatistics> stats = new HashMap<>();
    stats.put(EAST, createPlayerStatHelper(rBean, 3, EAST, "Zhenya", 14, 38,
                                           Whist.n(SOUTH, "148"),
                                           Whist.n(WEST, "10")));

    stats.put(SOUTH, createPlayerStatHelper(rBean, 3, SOUTH, "Petya", 26, 41,
                                            Whist.n(EAST, "28"),
                                            Whist.n(WEST, "0")));

    stats.put(WEST, createPlayerStatHelper(rBean, 3, WEST, "Vitya", 10, 28,
                                           Whist.n(SOUTH, "94"),
                                           Whist.n(EAST, "46")));
    rBean.setPlayerStats(stats);

    // generating and testing final results
    ResultService.generateFinalResults(rBean);
    assertTrue("Wrong finalScoreReady value", rBean.isFinalScoresReady());
    assertEquals("Wrong number of players", 3, rBean.getNumberOfPlayers());
    assertEquals("Wrong computed average mountain value", 13.33F, rBean.getAverageMountain());
    assertEquals("Wrong computed minimum mountain value", Integer.valueOf(41), rBean.getMinMountain());
    checkPlayerStats(stats, EAST, 62, -76, 84, 8,
                     Whist.n(SOUTH, "120"), Whist.n(WEST, "-36"));
    checkPlayerStats(stats, SOUTH, 41, 133, -214, -81,
                     Whist.n(EAST, "-120"), Whist.n(WEST, "-94"));
    checkPlayerStats(stats, WEST, 60, -56, 130, 74,
                     Whist.n(SOUTH, "94"), Whist.n(EAST, "36"));

    // testing the individual whist saldos
    rBean.setMountDivisibleByN(false);
    ResultService.generateFinalResults(rBean);
    checkFixValuesCleared(stats, 3);

    // now, the same but with the mount "divisible by N" option
    rBean.setMountDivisibleByN(true);
    ResultService.generateFinalResults(rBean);
    assertTrue("Wrong finalScoreReady value", rBean.isFinalScoresReady());
    assertEquals("Wrong number of players", 3, rBean.getNumberOfPlayers());
    assertEquals("Wrong computed average mountain value", 13.00F, rBean.getAverageMountain());
    assertEquals("Wrong computed minimum mountain value", Integer.valueOf(41), rBean.getMinMountain());
    checkPlayerStats(stats, EAST, 62, -70, 78, 8,
                     Whist.n(SOUTH, "117"), Whist.n(WEST, "-39"));
    checkPlayerStats(stats, SOUTH, 41, 130, -211, -81,
                     Whist.n(EAST, "-117"), Whist.n(WEST, "-94"));
    checkPlayerStats(stats, WEST, 60, -60, 133, 73,
                     Whist.n(SOUTH, "94"), Whist.n(EAST, "39"));

    // testing the fix values (mount fix and whist fixes)
    checkFixValuesSet(stats, EAST, null, null, -1);
    checkFixValuesSet(stats, SOUTH, EAST, 3, null);
    checkFixValuesSet(stats, WEST, EAST, 3, null);

    // testing that the fix values are cleared on a new count
    rBean.setMountDivisibleByN(false);
    ResultService.generateFinalResults(rBean);
    checkFixValuesCleared(stats, 3);
  }

  /**
   * Tests the generateFinalResults() method
   * with even more cases (using the divisible by N option).
   */
  public void testGenerateFinalResults4() {
    GameResultBean rBean = new GameResultBean();
    rBean.setLeningradka(true);
    rBean.setMountDivisibleByN(false);

    // creating game data for 4 players
    Map<Place, PlayerStatistics> stats = new HashMap<>();
    stats.put(EAST, createPlayerStatHelper(rBean, 4, EAST, "Bob", 76, 74,
                                           Whist.n(SOUTH, "172"),
                                           Whist.n(WEST, "84"),
                                           Whist.n(NORTH, "236")));

    stats.put(SOUTH, createPlayerStatHelper(rBean, 4, SOUTH, "Dodge", 74, 60,
                                            Whist.n(WEST, "60"),
                                            Whist.n(NORTH, "88"),
                                            Whist.n(EAST, "234")));

    stats.put(WEST, createPlayerStatHelper(rBean, 4, WEST, "Anya", 32, 34,
                                           Whist.n(NORTH, "192"),
                                           Whist.n(EAST, "108"),
                                           Whist.n(SOUTH, "220")));

    stats.put(NORTH, createPlayerStatHelper(rBean, 4, NORTH, "Tim", 22, 76,
                                            Whist.n(EAST, "180"),
                                            Whist.n(SOUTH, "132"),
                                            Whist.n(WEST, "48")));
    rBean.setPlayerStats(stats);

    // generating and testing final results
    ResultService.generateFinalResults(rBean);
    assertTrue("Wrong finalScoreReady value", rBean.isFinalScoresReady());
    assertEquals("Wrong number of players", 4, rBean.getNumberOfPlayers());
    assertEquals("Wrong computed average mountain value", 47.0F, rBean.getAverageMountain());
    assertEquals("Wrong computed minimum mountain value", Integer.valueOf(64), rBean.getMinMountain());
    checkPlayerStats(stats, EAST, 74, 370, -30, 340,
                     Whist.n(SOUTH, "-62"), Whist.n(WEST, "-24"), Whist.n(NORTH, "56"));
    checkPlayerStats(stats, SOUTH, 64, 470, -142, 328,
                     Whist.n(EAST, "62"), Whist.n(WEST, "-160"), Whist.n(NORTH, "-44"));
    checkPlayerStats(stats, WEST, 122, -110, 328, 218,
                     Whist.n(NORTH, "144"), Whist.n(EAST, "24"), Whist.n(SOUTH, "160"));
    checkPlayerStats(stats, NORTH, 184, -730, -156, -886,
                     Whist.n(EAST, "-56"), Whist.n(SOUTH, "44"), Whist.n(WEST, "-144"));

    // testing the individual whist saldos
    rBean.setMountDivisibleByN(false);
    ResultService.generateFinalResults(rBean);
    checkFixValuesCleared(stats, 4);

    // now, the same but with the mount "divisible by N" option
    rBean.setMountDivisibleByN(true);
    ResultService.generateFinalResults(rBean);
    assertTrue("Wrong finalScoreReady value", rBean.isFinalScoresReady());
    assertEquals("Wrong number of players", 4, rBean.getNumberOfPlayers());
    assertEquals("Wrong computed average mountain value", 47.0F, rBean.getAverageMountain());
    assertEquals("Wrong computed minimum mountain value", Integer.valueOf(64), rBean.getMinMountain());
    checkPlayerStats(stats, EAST, 74, 370, -30, 340,
                     Whist.n(SOUTH, "-62"), Whist.n(WEST, "-24"), Whist.n(NORTH, "56"));
    checkPlayerStats(stats, SOUTH, 64, 470, -142, 328,
                     Whist.n(EAST, "62"), Whist.n(WEST, "-160"), Whist.n(NORTH, "-44"));
    checkPlayerStats(stats, WEST, 122, -110, 328, 218,
                     Whist.n(NORTH, "144"), Whist.n(EAST, "24"), Whist.n(SOUTH, "160"));
    checkPlayerStats(stats, NORTH, 184, -730, -156, -886,
                     Whist.n(EAST, "-56"), Whist.n(SOUTH, "44"), Whist.n(WEST, "-144"));
    checkFixValuesCleared(stats, 4);

    // changing the mount to make the mount sum not divisible by 4
    // by adding one to the NORTH player's mount
    final PlayerStatistics north = stats.get(NORTH);
    north.setMountain(77);
    ResultService.generateFinalResults(rBean);

    // testing the fix values (mount fix and whist fixes)
    checkFixValuesSet(stats, EAST, null, null, -1);
    checkFixValuesSet(stats, SOUTH, EAST, 3, null);
    checkFixValuesSet(stats, WEST, EAST, 3, null);
    checkFixValuesSet(stats, NORTH, EAST, 3, null);

    // testing the players' scores
    checkPlayerStats(stats, EAST, 74, 380, -39, 341,
                     Whist.n(SOUTH, "-65"), Whist.n(WEST, "-27"), Whist.n(NORTH, "53"));
    checkPlayerStats(stats, SOUTH, 64, 470, -139, 331,
                     Whist.n(EAST, "65"), Whist.n(WEST, "-160"), Whist.n(NORTH, "-44"));
    checkPlayerStats(stats, WEST, 122, -110, 331, 221,
                     Whist.n(NORTH, "144"), Whist.n(EAST, "27"), Whist.n(SOUTH, "160"));
    checkPlayerStats(stats, NORTH, 185, -740, -153, -893,
                     Whist.n(EAST, "-53"), Whist.n(SOUTH, "44"), Whist.n(WEST, "-144"));

    // adding one more point to the NORTH player's mount
    north.setMountain(78);
    ResultService.generateFinalResults(rBean);

    // testing the fix values (for 2 players, should be cleared)
    checkFixValuesCleared(stats, 4);

    // testing the players' scores
    checkPlayerStats(stats, EAST, 74, 375, -30, 345,
                     Whist.n(SOUTH, "-62"), Whist.n(WEST, "-24"), Whist.n(NORTH, "56"));
    checkPlayerStats(stats, SOUTH, 64, 475, -142, 333,
                     Whist.n(EAST, "62"), Whist.n(WEST, "-160"), Whist.n(NORTH, "-44"));
    checkPlayerStats(stats, WEST, 122, -105, 328, 223,
                     Whist.n(NORTH, "144"), Whist.n(EAST, "24"), Whist.n(SOUTH, "160"));
    checkPlayerStats(stats, NORTH, 186, -745, -156, -901,
                     Whist.n(EAST, "-56"), Whist.n(SOUTH, "44"), Whist.n(WEST, "-144"));

    // adding one more point to the NORTH player's mount
    north.setMountain(79);
    rBean.setDivisibleByNPlayer(SOUTH);
    ResultService.generateFinalResults(rBean);

    // testing the fix values (mount fix and whist fixes)
    checkFixValuesSet(stats, SOUTH, null, null, 1);
    PlayerStatistics s = stats.get(SOUTH);
    checkWhistFixesForPlayer(s, 3, EAST, WEST, NORTH);

    // testing the players' scores
    checkPlayerStats(stats, EAST, 74, 380, -33, 347,
                     Whist.n(SOUTH, "-65"), Whist.n(WEST, "-24"), Whist.n(NORTH, "56"));
    checkPlayerStats(stats, SOUTH, 64, 470, -133, 337,
                     Whist.n(EAST, "65"), Whist.n(WEST, "-157"), Whist.n(NORTH, "-41"));
    checkPlayerStats(stats, WEST, 122, -100, 325, 225,
                     Whist.n(NORTH, "144"), Whist.n(EAST, "24"), Whist.n(SOUTH, "157"));
    checkPlayerStats(stats, NORTH, 187, -750, -159, -909,
                     Whist.n(EAST, "-56"), Whist.n(SOUTH, "41"), Whist.n(WEST, "-144"));

    // testing that the fix values are cleared on a new count
    rBean.setMountDivisibleByN(false);
    ResultService.generateFinalResults(rBean);
    checkFixValuesCleared(stats, 4);
  }

  /**
   * Checks the whist fixes for the given player.
   *
   * @param s           player's statistics object.
   * @param expWhistFix expected whist fix value.
   * @param otherPlaces other players' places.
   */
  @SuppressWarnings("SameParameterValue")
  private static void checkWhistFixesForPlayer(PlayerStatistics s, Integer expWhistFix, Place... otherPlaces) {
    Place place = s.getPlayerPlace();
    Map<Place, Integer> whistFixes = s.getWhistFixesMap();
    assertNotNull("Whist fixes map is null", whistFixes);
    assertEquals("Whist fixes map size is wrong", otherPlaces.length, whistFixes.size());
    for (Place other : otherPlaces) {
      Integer whistFix = whistFixes.get(other);
      assertNotNull(place + " whist fix against player " + other + " is not found", whistFix);
      assertEquals(place + " whist fix against player " + other + " is wrong", expWhistFix, whistFix);
      whistFix = s.getWhistFixAgainstPlayer(EAST);
      assertNotNull(place + " whist fix against player " + other + " is not found", whistFix);
      assertEquals(place + " whist fix against player " + other + " is wrong", expWhistFix, whistFix);
    }
  }

  /**
   * Assists with testing the mount and whist
   * fixes for the given player.
   *
   * @param stats       player statistics map.
   * @param player      player's place.
   * @param otherPlayer other players place.
   * @param expWhistFix expected whist fix value.
   * @param expMountFix expected mount fix.
   */
  private static void checkFixValuesSet(Map<Place, PlayerStatistics> stats, Place player,
                                        Place otherPlayer, Integer expWhistFix, Integer expMountFix) {
    PlayerStatistics s = stats.get(player);
    final Integer mountFix = s.getMountFix();
    if (expMountFix == null) {
      assertNull("Mount fix for player " + player + " must not be set", mountFix);

      Map<Place, Integer> whistFixes = s.getWhistFixesMap();
      assertNotNull("Whist fixes map must not be null", whistFixes);
      assertEquals("Whist fixes map for " + player + " has wrong size", 1, whistFixes.size());
      Integer whistFix = whistFixes.get(otherPlayer);
      assertNotNull(player + " whist fix against player " + otherPlayer + " is not found", whistFix);
      assertEquals(player + " whist fix against player " + otherPlayer + " is wrong", expWhistFix, whistFix);
      whistFix = s.getWhistFixAgainstPlayer(otherPlayer);
      assertNotNull(player + " whist fix against player " + otherPlayer + " is not found", whistFix);
      assertEquals(player + " whist fix against player " + otherPlayer + " is wrong", expWhistFix, whistFix);

    } else {
      assertNotNull("Mount fix for player " + player + " is not set", mountFix);
      assertEquals("Mount fix for player " + player + " is wrong", expMountFix, mountFix);
      Map<Place, Integer> whistFixes = s.getWhistFixesMap();
      assertNotNull("Whist fixes map must not be null", whistFixes);

      if (expMountFix < 0) {
        assertEquals("Whist fixes map for " + player + " must be empty", 0, whistFixes.size());

      } else {
        final int numOfOthers = stats.size() - 1;
        assertEquals("Wrong number of whist fixes for " + player, numOfOthers, whistFixes.size());
      }
    }
  }

  /**
   * Checks fix values for the given player stats map.
   *
   * @param stats           player statistics map.
   * @param numberOfPlayers number of players in the game.
   */
  private void checkFixValuesCleared(Map<Place, PlayerStatistics> stats, int numberOfPlayers) {
    PlayerStatistics s = stats.get(EAST);
    assertNull("Mount fix for EAST was not cleared!", s.getMountFix());
    Map<Place, Integer> whistFixes = s.getWhistFixesMap();
    assertNotNull("Whist fixes map must not be null", whistFixes);
    assertEquals("Whist fixes map for EAST must be empty", 0, whistFixes.size());
    s = stats.get(SOUTH);
    assertNull("Mount fix for SOUTH must not be set", s.getMountFix());
    whistFixes = s.getWhistFixesMap();
    assertNotNull("Whist fixes map must not be null", whistFixes);
    assertEquals("Whist fixes map for SOUTH was not cleared", 0, whistFixes.size());
    s = stats.get(WEST);
    assertNull("Mount fix for WEST must not be set", s.getMountFix());
    whistFixes = s.getWhistFixesMap();
    assertNotNull("Whist fixes map must not be null", whistFixes);
    assertEquals("Whist fixes map for WEST was not cleared", 0, whistFixes.size());
    if (numberOfPlayers == 4) {
      s = stats.get(NORTH);
      assertNull("Mount fix for NORTH must not be set", s.getMountFix());
      whistFixes = s.getWhistFixesMap();
      assertNotNull("Whist fixes map must not be null", whistFixes);
      assertEquals("Whist fixes map for NORTH was not cleared", 0, whistFixes.size());
    }
  }

  /**
   * Checks given player statistics.
   *
   * @param stats       Players statistics map.
   * @param place       Player's place.
   * @param newMount    New mount value.
   * @param newFinMount New final mount value.
   * @param whistsSaldo whists saldo value.
   * @param finScore    Final score value.
   * @param wSaldos     array of whist saldos.
   */
  private static void checkPlayerStats(Map<Place, PlayerStatistics> stats,
                                       Place place,
                                       int newMount,
                                       int newFinMount,
                                       Integer whistsSaldo,
                                       int finScore,
                                       Whist... wSaldos) {
    PlayerStatistics player = stats.get(place);
    assertNotNull("Player is not present", player);
    assertEquals("Wrong computed new mountain value", newMount, player.getNewMountain());
    assertEquals("Wrong computed final mountain in whists value", newFinMount, player.getFinalMountainInWhists());
    assertEquals("Wrong computed whists saldo value", whistsSaldo, player.getWhistSaldoMap().get(place));
    assertEquals("Wrong computed final score value", finScore, player.getFinalScoreInWhists());
    Map<Place, Integer> whistSaldoMap = player.getWhistSaldoMap();
    assertNotNull("Whist saldo map is null", whistSaldoMap);
    for (Whist w : wSaldos) {
      Integer saldo = whistSaldoMap.get(w.place);
      assertNotNull(place + "'s whist saldo against " + w.place + " is not set", saldo);
      assertEquals(place + "'s whist saldo against " + w.place + " is wrong", w.whist, saldo.toString());
    }
  }
}
