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

import java.awt.Component;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.JPanel;

import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.bean.GameResultBean;
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
import net.curre.prefcount.test.BaseTestCase;
import net.curre.prefcount.util.LocaleExt;

/**
 * This is a junit test for testing main
 * GUI functionality.
 * <p/>
 * Created date: Nov 27, 2007
 *
 * @author Yevgeny Nyden
 */
public class GeneralTest extends BaseTestCase {

  /** Private class logger. */
  private static Logger log = Logger.getLogger(GeneralTest.class.toString());

  /**
   * Test errors.
   *
   * @throws Exception on error.
   */
  public void testMiscErrors() throws Exception {

    log.info("Running testMiscErrors()...");

    PrefCountRegistry.getInstance().setCurrentLocale(PrefCountRegistry.DEFAULT_LOCALE_ID);
    MainWindow win = new MainWindow(false);
    PrefCountRegistry.getInstance().setMainWindow(win);

    win.initializeNumberOfPlayers();
    ScoreBoardLocationsMap map = win.scoreBoardPanel.getLocationsMap();
    assertNotNull("ScoreBoardLocationsMap is null", map);

    LocaleExt.unregisterAllComponents();
    MainWindow main = new MainWindow(false);
    try {
      main.playerDialogFrame = new PlayerDialogBaseFrame(5, main);
      fail("Exception is expected for the number of players 5");
    } catch (Exception e) {
      // expected
    }
  }

  /**
   * Test GUI for the 3 players settings.
   *
   * @throws Exception on error.
   */
  public void testAllForThreePlayers() throws Exception {

    log.info("Running testAllForThreePlayers()...");

    GameResultBean resultBean = PrefCountRegistry.getInstance().getGameResultBean();
    assertNotNull("Player result bean is null", resultBean);

    // CREATING THE MAIN WINDOW
    LocaleExt.unregisterAllComponents();
    PrefCountRegistry.getInstance().setCurrentLocale(PrefCountRegistry.DEFAULT_LOCALE_ID);
    MainWindow window = new MainWindow(false);
    PrefCountRegistry.getInstance().setMainWindow(window);

    assertNotNull("Top panel reference should be set", window.optionsPanel);
    assertNotNull("Player dialog frame reference should be set", window.playerDialogFrame);
    assertNotNull("Score board panel reference should be set", window.scoreBoardPanel);

    // STARTING PLAYER DIALOG FOR A 3-PLAYER GAME
    window.players3Button.setSelected(true);
    window.players4Button.setSelected(false);
    window.initializeNumberOfPlayers();

    // TESTING MAIN WINDOW REFERENCES
    checkWindowReferences(window);

    // testing the score board locations map
    checkScoreBoardLocationsMap(window.scoreBoardPanel.getLocationsMap(), 3);

    // testing dialog base frame (navigation buttons, main labels)
    // checking panels validation and navigation
    checkNavigationAndValidation(window.playerDialogFrame, 3);
  }

  /**
   * Test GUI for the 4 players settings.
   *
   * @throws Exception on error.
   */
  public void testAllForFourPlayers() throws Exception {

    log.info("Running testAllForFourPlayers()...");

    // CREATING THE MAIN WINDOW
    LocaleExt.unregisterAllComponents();
    PrefCountRegistry.getInstance().setCurrentLocale(PrefCountRegistry.DEFAULT_LOCALE_ID);
    MainWindow window = new MainWindow(false);
    PrefCountRegistry.getInstance().setMainWindow(window);

    // STARTING PLAYER DIALOG FOR A 4-PLAYER GAME
    window.players4Button.setSelected(true);
    window.players3Button.setSelected(false);
    window.initializeNumberOfPlayers();

    // TESTING MAIN WINDOW REFERENCES
    checkWindowReferences(window);

    // testing the score board locations map
    checkScoreBoardLocationsMap(window.scoreBoardPanel.getLocationsMap(), 4);

    // testing dialog base frame (navigation buttons, main labels)
    // checking panels validation and navigation
    checkNavigationAndValidation(window.playerDialogFrame, 4);
  }

  /** Private methods ***********************/

  /**
   * Tests if the given component is a proper dialog inner panel.
   *
   * @param panel Component to test.
   * @param index Index of the panel in the question pane.
   * @param clazz Expected class of the panel.
   */
  private void checkDialogInnerPanel(Component panel, int index, Class clazz) {
    assertNotNull("Panel with index " + index + " is null", panel);
    assertTrue("Panel with index " + index + " is not a JPanel", panel instanceof JPanel);
    assertTrue("Panel with index " + index + " does not implement DialogInnerPanel", panel instanceof DialogInnerPanel);
    assertEquals("Wrong panel with index " + index, clazz.getName(), panel.getClass().getName());
  }

  /**
   * Checks panels validation and navigation.
   *
   * @param dialog          Player dialog base panel reference.
   * @param numberOfPlayers Number of players in the game (3 or 4).
   */
  private void checkNavigationAndValidation(PlayerDialogBaseFrame dialog,
                                            int numberOfPlayers) {
    final int correct = numberOfPlayers == 4 ? 1 : 0;

    // testing dialog base frame (navigation buttons, main labels)
    assertNotNull("Dialog panel main window reference is null", dialog.mainWindow);
    assertNotNull("Dialog panel back button is null", dialog.backButton);
    assertButtonValues("Back button error", dialog.backButton, false, false);
    assertNotNull("Dialog panel next button is null", dialog.nextButton);
    assertButtonValues("Next button error", dialog.nextButton, true, false);
    assertNotNull("Back button reference is null", dialog.backButton);
    assertNotNull("Next button reference is null", dialog.nextButton);

    assertNotBlankAndVisible("Dialog panel message label error", dialog.messageLabel);
    assertNotNull("Dialog panel error label is null", dialog.errorLabel);
    assertNotVisible("Dialog panel error label should not be visible", dialog.errorLabel);

    // expect 5 or 6 panels in the question pane: player names panel,
    // three or four player scores panels, and the last input panel
    assertNotNull("Dialog panel questions pane reference is null", dialog.questionsPane);
    Component[] comps = dialog.questionsPane.getComponents();
    assertEquals("Wrong number of inner panels in the question pane",
                 5 + correct, comps.length);
    checkDialogInnerPanel(comps[0], 0, PlayersNamesPanel.class);
    checkDialogInnerPanel(comps[1], 1, PlayerDataPanel.class);
    checkDialogInnerPanel(comps[2], 2, PlayerDataPanel.class);
    checkDialogInnerPanel(comps[3], 3, PlayerDataPanel.class);
    if (numberOfPlayers == 3) {
      checkDialogInnerPanel(comps[4], 4, LastInputPanel.class);
    } else {
      checkDialogInnerPanel(comps[4], 4, PlayerDataPanel.class);
      checkDialogInnerPanel(comps[5], 5, LastInputPanel.class);
    }

    assertEquals("Wrong number of players", dialog.playersNumber, numberOfPlayers);
    assertEquals("Wrong current player panel index", 0, dialog.getCurrPlayerPanel());

    // testing navigation and panel validation
    DialogInnerPanel panel = dialog.getCurrentInnerPanel();
    assertNotNull("The first panel is null", panel);
    assertTrue("The first panel is not a PlayersNamesPanel", panel instanceof PlayersNamesPanel);
    PlayersNamesPanel rPanel = (PlayersNamesPanel) panel;

    // we should not move, since the names are not entered
    dialog.nextQuestionEventHelper(true);
    assertEquals("Wrong current player panel index", 0, dialog.getCurrPlayerPanel());
    assertTextNotBlank("Dialog panel error is not blank", dialog.errorLabel);
    assertVisible("Dialog panel error label should be visible", dialog.errorLabel);
    panel = dialog.getCurrentInnerPanel();
    assertNotNull("The second panel is null", panel);
    assertTrue("The panel should not be changed", panel instanceof PlayersNamesPanel);
    assertButtonValues("Back button", dialog.backButton, false, false);
    assertButtonValues("Next button", dialog.nextButton, true, false);

    // now, setting the names and expect to succeed
    rPanel.playersFields.get(EAST).setText("A");
    rPanel.playersFields.get(SOUTH).setText("B");
    rPanel.playersFields.get(WEST).setText("C");
    if (numberOfPlayers == 4) {
      rPanel.playersFields.get(NORTH).setText("D");
    }
    dialog.nextQuestionEventHelper(true);
    assertEquals("Wrong current player panel index", 1, dialog.getCurrPlayerPanel());
    assertTextBlank("Dialog panel error is not blank", dialog.errorLabel);
    assertNotVisible("Dialog panel error label should not be visible", dialog.errorLabel);
    panel = dialog.getCurrentInnerPanel();
    assertNotNull("The second panel is null", panel);
    assertTrue("The second panel is not a PlayerDataPanel", panel instanceof PlayerDataPanel);
    assertButtonValues("Back button", dialog.backButton, true, false);
    assertButtonValues("Next button", dialog.nextButton, true, false);

    dialog.nextQuestionEventHelper(true);
    assertEquals("Wrong current player panel index", 2, dialog.getCurrPlayerPanel());
    panel = dialog.getCurrentInnerPanel();
    assertNotNull("The third panel is null", panel);
    assertTrue("The third panel is not a PlayerDataPanel", panel instanceof PlayerDataPanel);
    assertButtonValues("Back button", dialog.backButton, true, false);
    assertButtonValues("Next button", dialog.nextButton, true, false);

    dialog.nextQuestionEventHelper(true);
    assertEquals("Wrong current player panel index", 3, dialog.getCurrPlayerPanel());
    panel = dialog.getCurrentInnerPanel();
    assertNotNull("The fourth panel is null", panel);
    assertTrue("The fourth panel is not a PlayerDataPanel", panel instanceof PlayerDataPanel);
    assertButtonValues("Back button", dialog.backButton, true, false);
    assertButtonValues("Next button", dialog.nextButton, true, false);

    // we should not move since the data fields are not ints
    PlayerDataPanel dPanel = (PlayerDataPanel) panel;
    setTextFields(dPanel.whistFields.values(), "ERrr");
    dialog.nextQuestionEventHelper(true);
    assertEquals("Wrong current player panel index", 3, dialog.getCurrPlayerPanel());
    panel = dialog.getCurrentInnerPanel();
    assertNotNull("The panel is null", panel);
    assertTrue("The panel should not be changed", panel instanceof PlayerDataPanel);
    assertButtonValues("Back button", dialog.backButton, true, false);
    assertButtonValues("Next button", dialog.nextButton, true, false);

    // now, we set the data field to valid ints and expect to succeed
    setTextFields(dPanel.whistFields.values(), "5");
    dialog.nextQuestionEventHelper(true);
    assertEquals("Wrong current player panel index", 4, dialog.getCurrPlayerPanel());
    panel = dialog.getCurrentInnerPanel();
    assertNotNull("The fifth panel is null", panel);
    if (numberOfPlayers == 3) {
      assertTrue("The fifth panel is not a LastInputPanel", panel instanceof LastInputPanel);
      assertButtonValues("Next button", dialog.nextButton, false, false);
    } else {
      assertTrue("The fifth panel is not a PlayerDataPanel", panel instanceof PlayerDataPanel);
      assertButtonValues("Next button", dialog.nextButton, true, false);
    }
    assertButtonValues("Back button", dialog.backButton, true, false);

    if (numberOfPlayers == 4) {
      dialog.nextQuestionEventHelper(true);
      assertEquals("Wrong current player panel index", 5, dialog.getCurrPlayerPanel());
      panel = dialog.getCurrentInnerPanel();
      assertNotNull("The panel is null", panel);
      assertTrue("The panel is not a LastInputPanel", panel instanceof LastInputPanel);
      assertButtonValues("Back button", dialog.backButton, true, false);
      assertButtonValues("Next button", dialog.nextButton, false, false);
    }

    // this should have no effect
    dialog.nextQuestionEventHelper(true);
    assertEquals("Wrong current player panel index", 4 + correct, dialog.getCurrPlayerPanel());
    panel = dialog.getCurrentInnerPanel();
    assertNotNull("The panel is null", panel);
    assertTrue("The panel is not a LastInputPanel", panel instanceof LastInputPanel);

    // trying to navigate back
    dialog.nextQuestionEventHelper(false);
    assertEquals("Wrong current player panel index", 3 + correct, dialog.getCurrPlayerPanel());
    panel = dialog.getCurrentInnerPanel();
    assertNotNull("The panel is null", panel);
    assertTrue("The panel should not be changed", panel instanceof PlayerDataPanel);
    assertButtonValues("Back button", dialog.backButton, true, false);
    assertButtonValues("Next button", dialog.nextButton, true, false);

    // this should move us to the first panel again (player names panel)
    dialog.nextQuestionEventHelper(false);
    dialog.nextQuestionEventHelper(false);
    dialog.nextQuestionEventHelper(false);
    if (numberOfPlayers == 4) {
      dialog.nextQuestionEventHelper(false);
    }
    assertEquals("Wrong current player panel index", 0, dialog.getCurrPlayerPanel());
    panel = dialog.getCurrentInnerPanel();
    assertNotNull("The panel is null", panel);
    assertTrue("The panel should not be changed", panel instanceof PlayersNamesPanel);
    assertButtonValues("Back button", dialog.backButton, false, false);
    assertButtonValues("Next button", dialog.nextButton, true, false);
  }

  /**
   * Tests main window references.
   *
   * @param window Window to test.
   */
  private void checkWindowReferences(MainWindow window) {
    // testing components references (they still have to be set)
    assertNotNull("Top panel reference should be set", window.optionsPanel);
    assertNotNull("Score board panel reference should be set", window.scoreBoardPanel);
    GameResultBean resultBean = PrefCountRegistry.getInstance().getGameResultBean();
    assertNotNull("Player result bean is null", resultBean);
    assertNotNull("Player dialog frame reference should be set", window.playerDialogFrame);
  }

  /**
   * Tests the score board locations map.
   * Basically, we need to make sure that map has locations for
   * all necessary players' score items (pools, mounts, etc.).
   *
   * @param map             Map to test.
   * @param numberOfPlayers Number of players in the game (3 or 4).
   */
  private void checkScoreBoardLocationsMap(ScoreBoardLocationsMap map,
                                           int numberOfPlayers) {
    assertNotNull("ScoreBoardLocationsMap is null", map);
    Map<ScoreItem, Point2D.Double> m = map.getLocationsMap(EAST);
    assertNotNull("Locations map is not found for player " + EAST, m);
    List<ScoreItem> keys = Arrays.asList(PLAYER_NAME, FINAL_MOUNT, FINAL_SCORE,
                                         PLAYER_POOL, PLAYER_MOUNT, WHIST_SALDO_TOTAL);
    List<ScoreItem> notNulls = new ArrayList<ScoreItem>(keys);
    notNulls.addAll(Arrays.asList(WHIST_SOUTH_SALDO, WHIST_WEST_SALDO,
                                  WHIST_SOUTH, WHIST_WEST));
    if (numberOfPlayers == 4) {
      notNulls.add(WHIST_NORTH);
    }
    assertMapNotNullValues(m, notNulls);
    List<ScoreItem> nulls = Arrays.asList(WHIST_EAST_SALDO, WHIST_EAST);
    assertMapNullValues(m, nulls);

    m = map.getLocationsMap(SOUTH);
    assertNotNull("Locations map is not found for player " + SOUTH, m);
    notNulls = new ArrayList<ScoreItem>(keys);
    notNulls.addAll(Arrays.asList(WHIST_EAST_SALDO, WHIST_WEST_SALDO,
                                  WHIST_EAST, WHIST_WEST));
    if (numberOfPlayers == 4) {
      notNulls.add(WHIST_NORTH);
    }
    assertMapNotNullValues(m, notNulls);
    nulls = Arrays.asList(WHIST_SOUTH_SALDO, WHIST_SOUTH);
    assertMapNullValues(m, nulls);

    m = map.getLocationsMap(WEST);
    assertNotNull("Locations map is not found for player " + WEST, m);
    notNulls = new ArrayList<ScoreItem>(keys);
    notNulls.addAll(Arrays.asList(WHIST_EAST_SALDO, WHIST_SOUTH_SALDO,
                                  WHIST_EAST, WHIST_SOUTH));
    if (numberOfPlayers == 4) {
      notNulls.add(WHIST_NORTH);
    }
    assertMapNotNullValues(m, notNulls);
    nulls = Arrays.asList(WHIST_WEST_SALDO, WHIST_WEST);
    assertMapNullValues(m, nulls);

    if (numberOfPlayers == 4) {
      m = map.getLocationsMap(NORTH);
      assertNotNull("Locations map is not found for player " + NORTH, m);
      notNulls = new ArrayList<ScoreItem>(keys);
      notNulls.addAll(Arrays.asList(WHIST_EAST_SALDO, WHIST_SOUTH_SALDO, WHIST_WEST_SALDO,
                                    WHIST_EAST, WHIST_SOUTH, WHIST_WEST));
      assertMapNotNullValues(m, notNulls);
      nulls = Arrays.asList(WHIST_NORTH_SALDO, WHIST_NORTH);
      assertMapNullValues(m, nulls);
    }
  }

}
