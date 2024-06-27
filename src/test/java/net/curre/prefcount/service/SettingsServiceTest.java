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

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.bean.Settings;
import net.curre.prefcount.gui.theme.LafThemeId;
import net.curre.prefcount.gui.type.PrefType;
import net.curre.prefcount.test.BaseTestCase;

import static net.curre.prefcount.service.LafThemeService.DEFAULT_LAF_THEME_ID;

/**
 * This is a junit test for testing settings service.
 * <p/>
 * Created date: Jul 1, 2007
 *
 * @author Yevgeny Nyden
 */
public class SettingsServiceTest extends BaseTestCase {

  /** Path to the test settings directory. */
  private static final String TEST_SETTINGS_PATH =
      "target" + File.separatorChar + "test" + File.separatorChar + "settings" + File.separatorChar;

  /** Value for the main window frame width. */
  private static final int SETTINGS_MAIN_FRAME_WIDTH = 615;

  /** Value for the main window frame height. */
  private static final int SETTINGS_MAIN_FRAME_HEIGHT = 655;

  /** Value for the LAF theme ID. */
  private static final LafThemeId DEFAULT_SETTINGS_LAF = LafThemeId.FLAT_LIGHT;

  /** Value for the locale ID (case-insensitive language name). */
  private static final String SETTINGS_LOCALE_ID = "us";

  /** Value for the Divisible By. */
  private static final boolean SETTINGS_DIVISIBLE_BY = true;

  /** Value for the Players Number. */
  private static final int SETTINGS_PLAYERS_NUMBER = 4;

  /** Value for the Pref Type. */
  private static final PrefType SETTINGS_PREF_TYPE = PrefType.SOCHI;

  /** Absolute path to the default test settings file. */
  private String testSettingsFilePath;

  /**
   * {@inheritDoc}
   * <p/>
   * Sets the settings file path in the PrefCountRegistry
   * to the test value and deletes this test settings file
   * if it exists.
   *
   * @throws Exception on error.
   */
  @SuppressWarnings("ResultOfMethodCallIgnored")
  @Override
  protected void setUp() throws Exception {
    super.setUp();

    File testSettingsDir = new File(TEST_SETTINGS_PATH);
    testSettingsDir.mkdirs();

    // Initialize the default test setting file path.
    File testSettingsFile = new File(TEST_SETTINGS_PATH + "testFile.ser");
    this.testSettingsFilePath = testSettingsFile.getAbsolutePath();

    // Delete the file if it exists.
    File file = new File(this.testSettingsFilePath);
    if (file.exists()) {
      file.delete();
    }

    PrefCountRegistry.getInstance().setMainWindow(null);
  }

  /**
   * Test main settings service functionality.
   */
  public void testDefaultTestSettings() {
    SettingsService service = new SettingsService(this.testSettingsFilePath);
    Settings settings = service.getSettings();
    checkSettings(settings, Settings.DEFAULT_MAIN_FRAME_WIDTH, Settings.DEFAULT_MAIN_FRAME_HEIGHT,
        DEFAULT_LAF_THEME_ID, PrefCountRegistry.DEFAULT_LOCALE_ID, Settings.DEFAULT_PREF_TYPE,
        Settings.DEFAULT_PLAYERS_NUMBER, Settings.DEFAULT_DIVISIBLE_BY);
  }

  /**
   * Test main settings service functionality.
   */
  public void testPersistSettings() {
    persistTestSettings(this.testSettingsFilePath);
    SettingsService service = new SettingsService(this.testSettingsFilePath);

    Settings settings = service.getSettings();
    checkSettings(settings, SETTINGS_MAIN_FRAME_WIDTH, SETTINGS_MAIN_FRAME_HEIGHT,
        DEFAULT_SETTINGS_LAF, SETTINGS_LOCALE_ID, SETTINGS_PREF_TYPE,
        SETTINGS_PLAYERS_NUMBER, SETTINGS_DIVISIBLE_BY);
  }

  /**
   * Tests the passed settings object.
   *
   * @param settings          Settings object to test.
   * @param mainFrameWidth    Expected main frame width.
   * @param mainFrameHeight   Expected main frame height.
   * @param lafThemeId         Expected LAF skin ID.
   * @param localeId          Expected locale ID.
   */
  private void checkSettings(Settings settings, int mainFrameWidth, int mainFrameHeight, LafThemeId lafThemeId,
                             String localeId, PrefType prefType, int playersNumber, boolean divisibleBy) {
    assertNotNull("Settings must not be null", settings);
    assertEquals("Settings has a wrong Main frame width", mainFrameWidth, settings.getMainWindowWidth());
    assertEquals("Settings has a wrong Main frame height", mainFrameHeight, settings.getMainWindowHeight());
    assertEquals("Settings has a wrong LAF skin ID", lafThemeId, settings.getLafThemeId());
    assertEquals("Settings has a wrong Locale ID", localeId, settings.getLocaleId());
    assertEquals("Settings has a wrong Pref Type", prefType, settings.getPrefType());
    assertEquals("Settings has a wrong Players Number", playersNumber, settings.getNumberOfPlayers());
//    assertEquals("Settings has a wrong Divisible By", divisibleBy, settings.getDivisibleByN());
  }

  /**
   * Creates and persists default test settings file in a default test directory.
   * @param settingsFilePath test settings file path
   */
  private static void persistTestSettings(String settingsFilePath) {
    Settings settings = new Settings();
//    settings.setDivisibleBy(SETTINGS_DIVISIBLE_BY);
    settings.setPrefType(SETTINGS_PREF_TYPE);
    settings.setNumberOfPlayers(SETTINGS_PLAYERS_NUMBER);
    settings.setLocaleId(SETTINGS_LOCALE_ID);
    settings.setLafThemeId(DEFAULT_SETTINGS_LAF);
    settings.setMainWindowHeight(SETTINGS_MAIN_FRAME_HEIGHT);
    settings.setMainWindowWidth(SETTINGS_MAIN_FRAME_WIDTH);

    try {
      File file = new File(settingsFilePath);
      FileOutputStream fStream = new FileOutputStream(file);
      ObjectOutputStream oStream = new ObjectOutputStream(fStream);
      oStream.writeObject(settings);
    } catch (Exception e) {
      throw new RuntimeException("Unable to persist default test settings", e);
    }
  }
}
