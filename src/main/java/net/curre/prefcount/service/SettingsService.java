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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.bean.Settings;
import net.curre.prefcount.gui.MainWindow;
import net.curre.prefcount.gui.theme.skin.PrefSkin;

/**
 * This is a service bean that assists with
 * handling application settings.
 * <p/>
 * Created date: Jun 22, 2007
 *
 * @author Yevgeny Nyden
 */
public class SettingsService {

  /** Private class logger. */
  private static Logger log = Logger.getLogger(SettingsService.class.toString());

  /** Reference to the settings object. */
  private static Settings settings;

  static {
    // initializing the settings object
    settings = loadSettings();
  }

  /**
   * Returns application settings (current settings).
   *
   * @return Reference to the application settings bean.
   */
  public static Settings getSettings() {
    return settings;
  }

  /**
   * Method to load settings stored on disk.
   *
   * @return Settings, loaded from the settings file,
   *         or a new <code>Settings</code> object if
   *         no settings file is found.
   */
  public static Settings loadSettings() {
    try {
      File file = new File(PrefCountRegistry.getInstance().getSettingsFilePath());
      if (file.exists()) {
        FileInputStream fStream = new FileInputStream(file);
        ObjectInputStream oStream = new ObjectInputStream(fStream);
        Settings settings = (Settings) oStream.readObject();

        // test each newer field for null values here
        // in case when an old settings file is loaded
        settings.testSettings();
        return settings;
      }
    } catch (Exception e) {
      log.log(Level.WARNING, "Caught exception while loading settings! Ignoring.");
    }
    return new Settings();
  }

  /**
   * Updates the skin on the current settings object.
   *
   * @param skin PrefSkin to set.
   */
  public static void updateSkin(PrefSkin skin) {
    settings.setLafSkinId(skin.getNameResourceKey());
  }

  /**
   * Method to save user settings. Application settings are
   * saved in the place and under the name specified in the
   * PrefCountRegistry bean.
   *
   * @throws ServiceException If there was an error when saving settings.
   */
  public static void saveSettings() throws ServiceException {
    MainWindow mainWindow = PrefCountRegistry.getInstance().getMainWindow();
    if (mainWindow != null) {
      settings.setMainFrameWidth(mainWindow.getWidth());
      settings.setMainFrameHeight(mainWindow.getHeight());
      if (mainWindow.playerDialogFrame != null) {
        settings.setDialogFrameWidth(mainWindow.playerDialogFrame.getWidth());
        settings.setDialogFrameHeight(mainWindow.playerDialogFrame.getHeight());
      }
    }
    settings.setLocaleId(PrefCountRegistry.getCurrentLocale().getLocale().getLanguage());

    // saving the pending skin if it is set
    PrefSkin skin = LafThemeService.getInstance().getPendingSkin();
    if (skin != null) {
      settings.setLafSkinId(skin.getNameResourceKey());
    }

    // settings LAF should be already set
    persistSettings();
  }

  /**
   * Method to reset user settings.
   * The settings are recreated and saved.
   *
   * @throws ServiceException If there was an error when resetting settings.
   */
  public static void resetSettings() throws ServiceException {
    LafThemeService.getInstance().clearPendingSkin(); 
    settings.reset();
    persistSettings();
  }

  /**
   * Method to persist the current settings.
   *
   * @throws ServiceException If there was an error when saving settings.
   */
  public static void persistSettings() throws ServiceException {
    try {
      File file = new File(PrefCountRegistry.getInstance().getSettingsFilePath());
      FileOutputStream fStream = new FileOutputStream(file);
      ObjectOutputStream oStream = new ObjectOutputStream(fStream);
      oStream.writeObject(settings);
    } catch (Exception e) {
      throw new ServiceException("ERROR while saving settings!", e);
    }
  }

  /** Private methods ********************** */

}
