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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.bean.Settings;
import net.curre.prefcount.util.Utilities;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.validation.constraints.NotNull;

import static net.curre.prefcount.bean.Settings.*;
import static net.curre.prefcount.service.LafThemeService.DEFAULT_LAF_THEME_ID;

/**
 * This is a service bean that assists with
 * handling application settings.
 * <p/>
 * Created date: Jun 22, 2007
 *
 * @author Yevgeny Nyden
 */
public class SettingsService {

  /** Name of the temp settings file to store some info between the games. */
  private static final String SETTINGS_FILENAME = "prefcount-settings.ser";

  /** Directory name where the settings are going to be saved. */
  private static final String SETTINGS_DIR_NAME = "PrefCount";

  /** Private class logger. */
  private static final Logger logger = LogManager.getLogger(SettingsService.class.getName());

  /** Reference to the settings object. */
  private final Settings settings;

  /** Absolute path to the settings file. */
  private final String settingsFilePath;

  /**
   * Ctor.
   * @param settingsFilePath path to the settings file (for test) or null if default should be used
   */
  public SettingsService(String settingsFilePath) {
    if (settingsFilePath == null) {
      settingsFilePath = getVerifiedSettingsDirectoryPath() + File.separatorChar + SETTINGS_FILENAME;
    }
    this.settingsFilePath = settingsFilePath;
    this.settings = loadSettings(settingsFilePath);
  }

  /**
   * Returns application settings (current settings).
   * @return Reference to the application settings
   */
  public Settings getSettings() {
    return this.settings;
  }

  /**
   * Method to reset user settings.
   * The settings are recreated and saved.
   *
   * @throws ServiceException If there was an error when resetting settings.
   */
  public void resetSettings() throws ServiceException {
    this.settings.reset();
    persistSettings();
  }

  /** Persists the current settings. */
  public void persistSettings() {
    try {
      File file = new File(this.settingsFilePath);
      FileOutputStream fStream = new FileOutputStream(file);
      ObjectOutputStream oStream = new ObjectOutputStream(fStream);
      oStream.writeObject(this.settings);
    } catch (Exception e) {
      logger.log(Level.WARN, "Unable to save the settings!", e);
    }
  }

  /**
   * Returns a platform specific absolute path to the game settings directory.
   * All custom directories in the path that don't exist, will be created.
   * @return absolute path to the game settings directory
   */
  public static @NotNull String getVerifiedSettingsDirectoryPath() {
    StringBuilder path = new StringBuilder(System.getProperties().getProperty("user.home"));
    switch (Utilities.getPlatformType()) {
      case MAC_OS:
        path.append(File.separatorChar).append("Library").
            append(File.separatorChar).append("Application Support");
        break;
      case WINDOWS:
        path.append(File.separatorChar).append("AppData").
            append(File.separatorChar).append("Local");
        break;
      default:
        path.append(File.separatorChar).append("temp");
    }
    path.append(File.separatorChar).append(SETTINGS_DIR_NAME);
    createDirIfDoesntExist(path);
    return path.toString();
  }

  /**
   * Method to load settings stored on disk.
   * @param settingsFilePath path to the settings file
   * @return Settings, loaded from the settings file,
   *         or a new <code>Settings</code> object if no settings file is found
   */
  private Settings loadSettings(String settingsFilePath) {
    // Try loading the settings file.
    try {
      File file = new File(settingsFilePath);
      if (file.exists()) {
        FileInputStream fStream = new FileInputStream(file);
        ObjectInputStream oStream = new ObjectInputStream(fStream);
        Settings settings = (Settings) oStream.readObject();

        // Test each newer field for null values here
        // in case when an old settings file is loaded.
        verifySettings(settings);
        return settings;
      }
    } catch (Exception e) {
      logger.log(Level.WARN, "Unable to load a settings file. Creating a default one.", e);
    }
    return new Settings();
  }

  /**
   * Creates a directory if it doesn't exist (only the last one in the provided path).
   * @param path Path to the directory
   */
  private static void createDirIfDoesntExist(@NotNull StringBuilder path) {
    File dir = new File(path.toString());
    if (!dir.exists()) {
      if (!dir.mkdir()) {
        logger.log(Level.WARN, "Unable to create a directory: " + path);
      }
    }
  }

  /**
   * Tests if settings values are null and sets them to the default values if they
   * are null. This is crucial when a newer application uses an older version of
   * serialized settings object.
   * @param settings settings object to verify
   */
  private static void verifySettings(@NotNull Settings settings) {
    if (settings.getLafThemeId() == null) {
      settings.setLafThemeId(DEFAULT_LAF_THEME_ID);
    }
    if (settings.getLocaleId() == null) {
      settings.setLocaleId(PrefCountRegistry.DEFAULT_LOCALE_ID);
    }
    if (settings.getPrefType() == null) {
      settings.setPrefType(DEFAULT_PREF_TYPE);
    }
    if (settings.getPlayersNumber() == null) {
      settings.setPlayersNumber(DEFAULT_PLAYERS_NUMBER);
    }
    if (settings.getDivisibleBy() == null) {
      settings.setDivisibleBy(DEFAULT_DIVISIBLE_BY);
    }
  }
}
