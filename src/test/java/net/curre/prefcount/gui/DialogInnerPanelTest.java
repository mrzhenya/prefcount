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

import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.gui.aa.AAJLabel;
import static net.curre.prefcount.gui.type.Place.EAST;
import net.curre.prefcount.test.BaseTestCase;
import net.curre.prefcount.util.LocaleExt;

import org.apache.commons.lang.StringUtils;

/**
 * This is a junit test for testing
 * dialog inner panel functionality.
 * <p/>
 * Created date: Dec 9, 2007
 *
 * @author Yevgeny Nyden
 */
public class DialogInnerPanelTest extends BaseTestCase {

  /** Private class logger. */
  private static Logger log = Logger.getLogger(DialogInnerPanelTest.class.toString());

  /**
   * Tests PlayersNamesPanel panel methods.
   *
   * @throws Exception on error.
   */
  public void testPlayersNamesPanel() throws Exception {

    log.info("Running testPlayersNamesPanel()...");

    // testing 3 player case
    PrefCountRegistry.getInstance().setCurrentLocale(PrefCountRegistry.DEFAULT_LOCALE_ID);
    MainWindow window = new MainWindow(false);
    PrefCountRegistry.getInstance().setMainWindow(window);
    window.players3Button.setSelected(true);
    window.initializeNumberOfPlayers();

    PlayersNamesPanel panel = new PlayersNamesPanel(window.playerDialogFrame, 3);
    checkDialogInnerPanel(panel, false, true);
    for (JTextField field : panel.playersFields.values()) {
      assertFalse("Validation should not succeed", panel.validateFields());
      field.setText("A");
    }
    assertTrue("Validation should succeed", panel.validateFields());

    // testing 4 player case
    LocaleExt.unregisterAllComponents();
    window = new MainWindow(false);
    PrefCountRegistry.getInstance().setMainWindow(window);
    window.players4Button.setSelected(true);
    window.initializeNumberOfPlayers();

    panel = new PlayersNamesPanel(window.playerDialogFrame, 4);
    checkDialogInnerPanel(panel, false, true);
    for (JTextField field : panel.playersFields.values()) {
      assertFalse("Validation should not succeed", panel.validateFields());
      field.setText("A");
    }
    assertTrue("Validation should succeed", panel.validateFields());
  }

  /**
   * Tests PlayerDataPanel panel methods.
   *
   * @throws Exception on error.
   */
  public void testPlayerDataPanel() throws Exception {

    log.info("Running testPlayerDataPanel()...");

    // testing 3 player case
    PrefCountRegistry.getInstance().setCurrentLocale(PrefCountRegistry.DEFAULT_LOCALE_ID);
    MainWindow window = new MainWindow(false);
    PrefCountRegistry.getInstance().setMainWindow(window);
    window.players3Button.setSelected(true);
    window.initializeNumberOfPlayers();

    PlayerDataPanel panel = new PlayerDataPanel(window.playerDialogFrame, 3, EAST);
    checkDialogInnerPanel(panel, false, false);
    checkIntegerValidation(panel, panel.mountField);
    checkIntegerValidation(panel, panel.poolField);
    JTextField[] textFields = panel.whistFields.values().toArray(new JTextField[2]);
    checkIntegerValidation(panel, textFields);

    // testing 4 player case
    LocaleExt.unregisterAllComponents();
    window = new MainWindow(false);
    PrefCountRegistry.getInstance().setMainWindow(window);
    window.players4Button.setSelected(true);
    window.initializeNumberOfPlayers();

    panel = new PlayerDataPanel(window.playerDialogFrame, 4, EAST);
    checkDialogInnerPanel(panel, false, false);
    checkIntegerValidation(panel, panel.mountField);
    checkIntegerValidation(panel, panel.poolField);
    textFields = panel.whistFields.values().toArray(new JTextField[3]);
    checkIntegerValidation(panel, textFields);
  }

  /**
   * Tests LastInputPanel panel methods.
   *
   * @throws Exception on error.
   */
  public void testLastInputPanel() throws Exception {

    log.info("Running testLastInputPanel()...");

    // testing 3 player case
    PrefCountRegistry.getInstance().setCurrentLocale(PrefCountRegistry.DEFAULT_LOCALE_ID);
    MainWindow window = new MainWindow(false);
    PrefCountRegistry.getInstance().setMainWindow(window);
    window.players3Button.setSelected(true);
    window.initializeNumberOfPlayers();

    LastInputPanel panel = new LastInputPanel(window.playerDialogFrame);
    checkDialogInnerPanel(panel, true, false);
    checkIntegerValidation(panel);

    // testing 4 player case
    LocaleExt.unregisterAllComponents();
    window = new MainWindow(false);
    PrefCountRegistry.getInstance().setMainWindow(window);
    window.players4Button.setSelected(true);
    window.initializeNumberOfPlayers();

    panel = new LastInputPanel(window.playerDialogFrame);
    checkDialogInnerPanel(panel, true, false);
    checkIntegerValidation(panel);
  }

  /** Private methods ***********************/

  /**
   * Tests the panel validation for a given numeric field.
   * This method assumes the panel is valid at start.
   *
   * @param panel  Panel to test.
   * @param fields Fields to test.
   */
  private void checkIntegerValidation(DialogInnerPanel panel, JTextField... fields) {
    for (JTextField field : fields) {
      assertTrue("Validation should succeed", panel.validateFields());
      field.setText("12Ax");
      assertFalse("Validation should not succeed", panel.validateFields());
      field.setText("12");
      assertTrue("Validation should succeed", panel.validateFields());
    }
  }

  /**
   * Tests the last input panel validation.
   *
   * @param panel Last input panel to test.
   */
  private void checkIntegerValidation(LastInputPanel panel) {
    panel.setPointCostText(" ");
    assertTrue("Validation should succeed", panel.validateFields());
    panel.setPointCostText("12Ax");
    assertFalse("Validation should not succeed", panel.validateFields());
    panel.setPointCostText("0");
    assertTrue("Validation should succeed", panel.validateFields());
    panel.setPointCostText("-1");
    assertFalse("Validation should not succeed", panel.validateFields());
    panel.setPointCostText("12");
    assertTrue("Validation should succeed", panel.validateFields());
    panel.setPointCostText(" 12 ");
    assertTrue("Validation should succeed", panel.validateFields());
  }

  /**
   * Tests the isFirstPanel(), isLastPanel(),
   * and getHeaderMessage() DialogInnerPanel methods.
   *
   * @param panel   DialogInnerPanel panel.
   * @param isLast  Expected isLast value.
   * @param isFirst Expected isFirst value.
   */
  private void checkDialogInnerPanel(DialogInnerPanel panel, boolean isLast, boolean isFirst) {
    assertEquals("Panel isLast flag is wrong,", isLast, panel.isLastPanel());
    assertEquals("Panel isFirst flag is wrong,", isFirst, panel.isFirstPanel());

    JLabel messageLabel = new AAJLabel();
    LocaleExt.registerComponent(messageLabel, "pref.dialog.message.default");
    panel.setHeaderMessage(messageLabel);
    assertTrue("Panel header message was not set", StringUtils.isNotBlank(messageLabel.getText()));
  }

}
