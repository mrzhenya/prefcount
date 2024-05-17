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

package net.curre.prefcount.event;

import java.awt.CheckboxMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JTextField;

import net.curre.prefcount.PrefCountRegistry;
import static net.curre.prefcount.PrefCountRegistry.AVAILABLE_LOCALES;
import static net.curre.prefcount.PrefCountRegistry.DEFAULT_LOCALE_ID;
import net.curre.prefcount.bean.GameResultBean;
import net.curre.prefcount.bean.PlayerStatistics;
import net.curre.prefcount.gui.DialogInnerPanel;
import net.curre.prefcount.gui.GuiPackageHelper;
import net.curre.prefcount.gui.LastInputPanel;
import net.curre.prefcount.gui.MainWindow;
import net.curre.prefcount.gui.PlayerDataPanel;
import net.curre.prefcount.gui.PlayersNamesPanel;
import net.curre.prefcount.gui.menu.MenuItemsBean;
import net.curre.prefcount.gui.type.Place;
import static net.curre.prefcount.gui.type.Place.EAST;
import static net.curre.prefcount.gui.type.Place.NORTH;
import static net.curre.prefcount.gui.type.Place.SOUTH;
import static net.curre.prefcount.gui.type.Place.WEST;
import static net.curre.prefcount.gui.type.WindowComponent.DIALOG_BACK;
import static net.curre.prefcount.gui.type.WindowComponent.DIALOG_FORWARD;
import net.curre.prefcount.service.ResultService;
import net.curre.prefcount.test.BaseTestCase;
import net.curre.prefcount.util.LocaleExt;

/**
 * This is a junit test for testing event classes.
 * <p/>
 * Created date: Dec 15, 2007
 *
 * @author Yevgeny Nyden
 */
public class AllEventsTest extends BaseTestCase {

  /** Private class logger. */
  private static Logger log = Logger.getLogger(AllEventsTest.class.getName());

  /** Reference to the main window. */
  private static MainWindow window = null;

  /**
   * Creates and sets the main window (class variable)
   * ans also initializes the result bean with a 3-player game data.
   */
  @Override
  public void setUp() throws Exception {
    super.setUp();
    synchronized (this) {
      if (window == null) {
        log.info("Initializing main window.");
        PrefCountRegistry.getInstance().setCurrentLocale(DEFAULT_LOCALE_ID);
        window = new MainWindow(false);
      }
      PrefCountRegistry.getInstance().setMainWindow(window);
    }
  }

  /** Tests the ChangeLanguageActionListener class. */
  public void testChangeLanguageActionListener() {

    log.info("Running testChangeLanguageActionListener()...");

    for (LocaleExt locale : AVAILABLE_LOCALES) {
      ChangeLanguageActionListener event = new ChangeLanguageActionListener(locale.getLocale().getLanguage());
      CheckboxMenuItem item = new CheckboxMenuItem("box");
      ItemEvent itEvent = new ItemEvent(item, ItemEvent.ITEM_FIRST, item, ItemEvent.SELECTED);
      event.itemStateChanged(itEvent);
      LocaleExt currLocale = PrefCountRegistry.getCurrentLocale();
      assertNotNull("Current locale is null");
      assertEquals("Current locale is wrong;", locale, currLocale);
    }
  }

  /** Tests the DialogButtonNavigationListener class. */
  public void testDialogButtonNavigationListener() {

    log.info("Running testDialogButtonNavigationListener()...");

    try {
      // starting player dialog and populating the names
      window = new MainWindow(false);

      PrefCountRegistry.getInstance().setMainWindow(window);
      initializeResultBean();
      DialogInnerPanel prevPanel = window.playerDialogFrame.getCurrentInnerPanel();
      assertNotNull("Current DialogInnerPanel is null", prevPanel);
      assertTrue("Wrong current DialogInnerPanel", prevPanel instanceof PlayersNamesPanel);
      Map<Place, JTextField> fields = GuiPackageHelper.getPlayerFields((PlayersNamesPanel) prevPanel);
      fields.get(EAST).setText("A");
      fields.get(SOUTH).setText("B");
      fields.get(WEST).setText("C");
      if (fields.size() == 4) {
        fields.get(NORTH).setText("D");
      }

      // testing moving forward via actionPerformed()
      checkCurrentPanel(PlayersNamesPanel.class);

      MenuItemsBean menuItemsBean = PrefCountRegistry.getInstance().getMenuItemsBean();
      assertNotNull("Menu items bean is null", menuItemsBean);
      JButton forward = menuItemsBean.getJButton(DIALOG_FORWARD, null);
      forward.setEnabled(true);
      assertNotNull("Button " + DIALOG_FORWARD + " is null", forward);
      ActionListener[] listeners = forward.getActionListeners();
      assertNotNull("Listeners array is null", listeners);
      assertEquals("There should be only exactly one listener", 1, listeners.length);

      ActionEvent dummy = new ActionEvent(new Object(), 1, "none");
      listeners[0].actionPerformed(dummy);
      assertEquals("Event source is wrong", DIALOG_FORWARD, dummy.getSource());
      checkCurrentPanel(PlayerDataPanel.class);
      listeners[0].actionPerformed(dummy);
      assertEquals("Event source is wrong", DIALOG_FORWARD, dummy.getSource());
      checkCurrentPanel(PlayerDataPanel.class);
      listeners[0].actionPerformed(dummy);
      assertEquals("Event source is wrong", DIALOG_FORWARD, dummy.getSource());
      checkCurrentPanel(PlayerDataPanel.class);
      listeners[0].actionPerformed(dummy);
      if (fields.size() == 4) {
        assertEquals("Event source is wrong", DIALOG_FORWARD, dummy.getSource());
        checkCurrentPanel(PlayerDataPanel.class);
        listeners[0].actionPerformed(dummy);
      }
      assertEquals("Event source is wrong", DIALOG_FORWARD, dummy.getSource());
      checkCurrentPanel(LastInputPanel.class);

      // testing moving back via actionPerformed()
      checkCurrentPanel(LastInputPanel.class);

      forward.setEnabled(false);
      JButton back = menuItemsBean.getJButton(DIALOG_BACK, null);
      back.setEnabled(true);
      assertNotNull("Button " + DIALOG_BACK + " is null", back);
      listeners = back.getActionListeners();
      assertNotNull("Listeners array is null", listeners);
      assertEquals("There should be only exactly one listener", 1, listeners.length);

      listeners[0].actionPerformed(dummy);
      assertEquals("Event source is wrong", DIALOG_BACK, dummy.getSource());
      checkCurrentPanel(PlayerDataPanel.class);
      listeners[0].actionPerformed(dummy);
      assertEquals("Event source is wrong", DIALOG_BACK, dummy.getSource());
      checkCurrentPanel(PlayerDataPanel.class);
      listeners[0].actionPerformed(dummy);
      assertEquals("Event source is wrong", DIALOG_BACK, dummy.getSource());
      checkCurrentPanel(PlayerDataPanel.class);
      listeners[0].actionPerformed(dummy);
      if (fields.size() == 4) {
        assertEquals("Event source is wrong", DIALOG_BACK, dummy.getSource());
        checkCurrentPanel(PlayerDataPanel.class);
        listeners[0].actionPerformed(dummy);
      }
      assertEquals("Event source is wrong", DIALOG_BACK, dummy.getSource());
      checkCurrentPanel(PlayersNamesPanel.class);

    } finally {
      GameResultBean resultBean = PrefCountRegistry.getInstance().getGameResultBean();
      ResultService.clearFinalResults(resultBean);
    }
  }

  /** Tests the SaveResetSettingsActionListener class. */
  public void testSaveResetSettingsActionListener() {

    log.info("Running testSaveResetSettingsActionListener()...");
    try {
      PrefCountRegistry.getInstance().setSettingsFilePath(SETTINGS_FILE);
      deleteTestSettingsFile();
      File file = new File(SETTINGS_FILE);
      assertFalse("Settings file must not have been created", file.exists());

    } finally {
      deleteTestSettingsFile();
    }
  }

  /** Private methods ***********************/

  /**
   * Initializes the result bean with a
   * 3-player game data.
   */
  private static void initializeResultBean() {
    // creating game data for 3 players
    Map<Place, PlayerStatistics> stats = new HashMap<Place, PlayerStatistics>(3);
    GameResultBean resultBean = PrefCountRegistry.getInstance().getGameResultBean();
    stats.put(EAST, createPlayerStatHelper(resultBean, 4, EAST, "dariya", 56, 22,
                                           Whist.n(SOUTH, "24"),
                                           Whist.n(WEST, "32"),
                                           Whist.n(NORTH, "10")));

    stats.put(SOUTH, createPlayerStatHelper(resultBean, 4, SOUTH, "kolya", 22, 22,
                                            Whist.n(EAST, "100"),
                                            Whist.n(WEST, "32"),
                                            Whist.n(NORTH, "0")));

    stats.put(WEST, createPlayerStatHelper(resultBean, 4, WEST, "fedya", 12, 34,
                                           Whist.n(EAST, "72"),
                                           Whist.n(SOUTH, "56"),
                                           Whist.n(NORTH, "0")));

    stats.put(NORTH, createPlayerStatHelper(resultBean, 4, NORTH, "anton", 26, 41,
                                            Whist.n(EAST, "72"),
                                            Whist.n(SOUTH, "56"),
                                            Whist.n(WEST, "14")));
    resultBean.setPlayerStats(stats);
  }

  /**
   * Checks the current player dialog inner panel.
   *
   * @param clazz Expected panel class.
   */
  private static <T extends DialogInnerPanel> void checkCurrentPanel(Class<T> clazz) {
    DialogInnerPanel currPanel = window.playerDialogFrame.getCurrentInnerPanel();
    assertNotNull("Current DialogInnerPanel is null", currPanel);
    if (!clazz.isInstance(currPanel)) {
      fail("Wrong current DialogInnerPanel; expected: " +
           clazz.getName() + ", but was: " + currPanel.getClass().getName());
    }
  }

}
