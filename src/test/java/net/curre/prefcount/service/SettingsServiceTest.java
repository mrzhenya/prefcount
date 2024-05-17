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

package net.curre.prefcount.service;

import java.io.File;
import java.util.logging.Logger;

import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.bean.Settings;
import net.curre.prefcount.gui.theme.skin.PrefSkin;
import net.curre.prefcount.test.BaseTestCase;

/**
 * This is a junit test for testing settings service.
 * <p/>
 * Created date: Jul 1, 2007
 *
 * @author Yevgeny Nyden
 */
public class SettingsServiceTest extends BaseTestCase {

  /** Private class logger. */
  private static Logger log = Logger.getLogger(SettingsServiceTest.class.toString());

  /** Value for the main window frame width. */
  private static final int SETTINGS_MAIN_FRAME_WIDTH = 615;

  /** Value for the main window frame height. */
  private static final int SETTINGS_MAIN_FRAME_HEIGHT = 655;

  /** Value for the dialog window frame width. */
  private static final int SETTINGS_DIALOG_FRAME_WIDTH = 324;

  /** Value for the dialog window frame height. */
  private static final int SETTINGS_DIALOG_FRAME_HEIGHT = 333;

  /** Value for the Look and Feel theme/skin. */
  private static final PrefSkin SETTINGS_LAF_SKIN = LafThemeService.AVAILABLE_SKINS[2];

  /** Value for the locale ID (case insensitive language name). */
  private static final String SETTINGS_LOCALE_ID = "us";

  /**
   * {@inheritDoc}
   * <p/>
   * Sets the settings file path in the PrefCountRegistry
   * to the test value and deletes this test settings file
   * if it exists.
   *
   * @throws Exception on error.
   */
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    PrefCountRegistry.getInstance().setSettingsFilePath(SETTINGS_FILE);
    deleteTestSettingsFile();
    PrefCountRegistry.getInstance().setMainWindow(null);
  }

  /**
   * {@inheritDoc}
   * <p/>
   * Deletes test settings file.
   *
   * @throws Exception on error.
   */
  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    PrefCountRegistry.getInstance().setSettingsFilePath(SETTINGS_FILE);
    deleteTestSettingsFile();
  }

  /**
   * Test main settings service functionality.
   *
   * @throws Exception on error.
   */
  public void testAll() throws Exception {

    log.info("Running testAll()...");

    // testing default settings
    Settings settings = SettingsService.getSettings();
    checkSettings(settings, Settings.DEFAULT_MAIN_FRAME_WIDTH, Settings.DEFAULT_MAIN_FRAME_HEIGHT,
                  Settings.DEFAULT_DIALOG_FRAME_WIDTH, Settings.DEFAULT_DIALOG_FRAME_HEIGHT,
                  Settings.DEFAULT_LAF_SKIN_ID, PrefCountRegistry.DEFAULT_LOCALE_ID);
    File file = new File(SETTINGS_FILE);
    assertFalse("Settings file must not have been created", file.exists());

    // saving and testing new settings
    settings.setMainFrameWidth(SETTINGS_MAIN_FRAME_WIDTH);
    settings.setMainFrameHeight(SETTINGS_MAIN_FRAME_HEIGHT);
    settings.setDialogFrameWidth(SETTINGS_DIALOG_FRAME_WIDTH);
    settings.setDialogFrameHeight(SETTINGS_DIALOG_FRAME_HEIGHT);
    SettingsService.updateSkin(SETTINGS_LAF_SKIN);
    PrefCountRegistry.getInstance().setCurrentLocale(SETTINGS_LOCALE_ID);
    SettingsService.saveSettings();
    assertTrue("Settings file hasn't been created", file.exists());
    settings = SettingsService.loadSettings();
    checkSettings(settings, SETTINGS_MAIN_FRAME_WIDTH, SETTINGS_MAIN_FRAME_HEIGHT,
                  SETTINGS_DIALOG_FRAME_WIDTH, SETTINGS_DIALOG_FRAME_HEIGHT,
                  SETTINGS_LAF_SKIN.getNameResourceKey(), SETTINGS_LOCALE_ID);

    // testing settings reset functionality
    SettingsService.resetSettings();
    assertTrue("Settings file is not present", file.exists());
    settings = SettingsService.loadSettings();
    checkSettings(settings, Settings.DEFAULT_MAIN_FRAME_WIDTH, Settings.DEFAULT_MAIN_FRAME_HEIGHT,
                  Settings.DEFAULT_DIALOG_FRAME_WIDTH, Settings.DEFAULT_DIALOG_FRAME_HEIGHT,
                  Settings.DEFAULT_LAF_SKIN_ID, PrefCountRegistry.DEFAULT_LOCALE_ID);

    // testing errors
    assertTrue("Internal error - unable to set file read only", file.setReadOnly());
    try {
      SettingsService.saveSettings();
      fail("saveSettings() should have thrown a ServiceException!");
    } catch (ServiceException e) {
      // expected
    }
  }

  /**
   * Test settings service functionality using real frames.
   *
   * @throws Exception on error.
   */
/*
  public void testSettingsWithFrames() throws Exception {

    log.info("Running testSettingsWithFrames()...");

    MainWindow main = new MainWindow(false);
    main.setSize(SETTINGS_MAIN_FRAME_WIDTH, SETTINGS_MAIN_FRAME_HEIGHT);
    PrefCountRegistry.getInstance().setMainWindow(main);

    // saving and testing new settings
    SettingsService.updateSkin(SETTINGS_LAF_SKIN);
    PrefCountRegistry.setCurrentLocale(SETTINGS_LOCALE_ID);
    SettingsService.saveSettings();
    File file = new File(SETTINGS_FILE);
    assertTrue("Settings file can not be located", file.exists());
    Settings settings = SettingsService.loadSettings();
    checkSettings(settings, SETTINGS_MAIN_FRAME_WIDTH, SETTINGS_MAIN_FRAME_HEIGHT,
                  Settings.DEFAULT_DIALOG_FRAME_WIDTH, Settings.DEFAULT_DIALOG_FRAME_HEIGHT,
                  SETTINGS_LAF_SKIN.getNameResourceKey(), SETTINGS_LOCALE_ID);

    main.playerDialogFrame = new PlayerDialogBaseFrame(3, main);
    main.playerDialogFrame.setSize(SETTINGS_DIALOG_FRAME_WIDTH, SETTINGS_DIALOG_FRAME_HEIGHT);
    SettingsService.saveSettings();
    settings = SettingsService.loadSettings();
    checkSettings(settings, SETTINGS_MAIN_FRAME_WIDTH, SETTINGS_MAIN_FRAME_HEIGHT,
                  SETTINGS_DIALOG_FRAME_WIDTH, SETTINGS_DIALOG_FRAME_HEIGHT,
                  SETTINGS_LAF_SKIN.getNameResourceKey(), SETTINGS_LOCALE_ID);
  }
*/

  /**
   * Test settings service functionality in regards to
   * dealing with old and stale settings files.
   *
   * @throws Exception on error.
   */
/*
  public void testOldSettings() throws Exception {

    log.info("Running testOldSettings()...");

    // test loading old settings (without localeId field)
    PrefCountRegistry.getInstance().setSettingsFilePath(OLD_SETTINGS_FILE);
    File file = new File(OLD_SETTINGS_FILE);
    assertTrue("Old settings file can not be located", file.exists());
    Settings settings = SettingsService.loadSettings();
    checkSettings(settings, Settings.DEFAULT_MAIN_FRAME_WIDTH, Settings.DEFAULT_MAIN_FRAME_HEIGHT,
                  Settings.DEFAULT_DIALOG_FRAME_WIDTH, Settings.DEFAULT_DIALOG_FRAME_HEIGHT,
                  Settings.DEFAULT_LAF_SKIN_ID, PrefCountRegistry.DEFAULT_LOCALE_ID);

    // test loading stale settings (with a different serialVersionUID)
    PrefCountRegistry.getInstance().setSettingsFilePath(STALE_SETTINGS_FILE);
    file = new File(STALE_SETTINGS_FILE);
    assertTrue("Stale settings file can not be located", file.exists());
    settings = SettingsService.loadSettings();
    checkSettings(settings, Settings.DEFAULT_MAIN_FRAME_WIDTH, Settings.DEFAULT_MAIN_FRAME_HEIGHT,
                  Settings.DEFAULT_DIALOG_FRAME_WIDTH, Settings.DEFAULT_DIALOG_FRAME_HEIGHT,
                  Settings.DEFAULT_LAF_SKIN_ID, PrefCountRegistry.DEFAULT_LOCALE_ID);
  }
*/

  /** Private methods ***********************/

  /**
   * Tests the passed settings object.
   *
   * @param settings          Settings object to test.
   * @param mainFrameWidth    Expected main frame width.
   * @param mainFrameHeight   Expected main frame height.
   * @param dialogFrameWidth  Expected dialog frame width.
   * @param dialogFrameHeight Expected dialog frame height.
   * @param lafSkinId         Expected LAF skin ID.
   * @param localeId          Expected locale ID.
   * @throws Exception On error.
   */
  private void checkSettings(Settings settings, int mainFrameWidth, int mainFrameHeight,
                             int dialogFrameWidth, int dialogFrameHeight, String lafSkinId,
                             String localeId) throws Exception {
    assertNotNull("Settings must not be null", settings);
    assertEquals("Settings has a wrong Main frame width", mainFrameWidth, settings.getMainFrameWidth());
    assertEquals("Settings has a wrong Main frame height", mainFrameHeight, settings.getMainFrameHeight());
    assertEquals("Settings has a wrong Dialog frame width", dialogFrameWidth, settings.getDialogFrameWidth());
    assertEquals("Settings has a wrong Dialog frame height", dialogFrameHeight, settings.getDialogFrameHeight());
    assertEquals("Settings has a wrong LAF skin ID", lafSkinId, settings.getLafSkinId());
    assertEquals("Settings has a wrong Locale ID", localeId, settings.getLocaleId());
  }

}
