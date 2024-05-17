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

package net.curre.prefcount;

import java.io.File;
import java.util.Locale;
import java.util.logging.Logger;

import net.curre.prefcount.bean.GameResultBean;
import net.curre.prefcount.event.MainController;
import net.curre.prefcount.gui.LastInputPanel;
import net.curre.prefcount.gui.MainWindow;
import net.curre.prefcount.gui.PlayerDialogBaseFrame;
import net.curre.prefcount.gui.ChoosePlayerDialog;
import net.curre.prefcount.gui.menu.MenuItemsBean;
import net.curre.prefcount.service.ServiceException;
import net.curre.prefcount.util.LocaleExt;
import net.curre.prefcount.util.Utilities;

/**
 * This is the central place for all prefcount
 * settings and configuration data.
 * <p/>
 * Created date: May 30, 2007
 *
 * @author Yevgeny Nyden
 */
public class PrefCountRegistry {

  /** Private class logger. */
  private static Logger log = Logger.getLogger(PrefCountRegistry.class.toString());

  /** Application name. */
  public static final String APPLICATION_NAME = "PrefCount";

  /** Location of the images directory. */
  public static final String IMAGES_DIR = "images";

  /** Default value for the locale ID (case insensitive language name). */
  public static final String DEFAULT_LOCALE_ID = "ru";

  /** Name of the settings file. */
  public static final String SETTINGS_FILE_NAME = "prefcount-settings.ser";

  /** Array of available locales in the application. */
  public static final LocaleExt[] AVAILABLE_LOCALES = new LocaleExt[]{
      new LocaleExt("ru", "RU", "\u0420\u0443\u0441\u0441\u043A\u0438\u0439"),
      new LocaleExt("us", "US", "English US")
  };

  /** Reference to the singelton instance of this class. */
  private static final PrefCountRegistry instance;

  /**
   * Current locale (the first locale on the
   * availableLocales array is the default).
   */
  private static LocaleExt currentLocale = AVAILABLE_LOCALES[0];

  /** Reference to the menu items bean. */
  private final MenuItemsBean menuItemsBean;

  /** Reference to the result bean */
  private GameResultBean gameResultBean;

  static {
    instance = new PrefCountRegistry();
  }

  /** Reference to the main window. */
  private MainWindow mainWindow;

  /** Reference to the player dialog base frame. */
  private PlayerDialogBaseFrame playerDialogFrame;

  /** Reference to the last input panel. */
  private LastInputPanel lastInputPanel;

  /** Absolute path to the settings file (including filename). */
  private String settingsFilePath;

  /** Reference to the main controller bean. */
  private MainController mainController;

  /** Reference to the choose player dialog window. */
  private ChoosePlayerDialog choosePlayerDialog;

  /**
   * Returns the singleton instance of this class.
   *
   * @return The singleton instance of this class.
   */
  public static PrefCountRegistry getInstance() {
    return instance;
  }

  /**
   * Private constructor. Sets settingsFilePath with
   * the result of the getSettingsFilePathHelper() method.
   */
  private PrefCountRegistry() {
    this.settingsFilePath = getSettingsFilePathHelper();
    this.menuItemsBean = new MenuItemsBean();
    this.gameResultBean = new GameResultBean();
  }

  /**
   * Returns the current locale.
   *
   * @return The current locale.
   */
  public static LocaleExt getCurrentLocale() {
    return currentLocale;
  }

  /**
   * Setter for the current locale.
   *
   * @param localeId Identifier (case insensitive language name)
   *                 for the locale to which to set.
   */
  public synchronized void setCurrentLocale(String localeId) {
    LocaleExt locale;
    try {
      locale = findLocaleById(localeId);
    } catch (ServiceException e) {
      log.warning("Error: " + e.getMessage() + " setting locale to default.");
      try {
        locale = findLocaleById(PrefCountRegistry.DEFAULT_LOCALE_ID);
      } catch (ServiceException e1) {
        log.severe("Error: " + e1.getMessage() + " - unable to set locale to default.");
        System.exit(1);
        throw new NullPointerException();
      }
    }
    Locale.setDefault(locale.getLocale());
    currentLocale = locale;

    if (this.mainWindow != null) {
      this.mainWindow.refreshComponents();
    }
  }

  /**
   * Getter for the reference to the menu items bean.
   *
   * @return reference to the menu items bean.
   */
  public MenuItemsBean getMenuItemsBean() {
    return this.menuItemsBean;
  }

  /**
   * Getter for the game result bean.
   *
   * @return reference to the game result bean.
   */
  public GameResultBean getGameResultBean() {
    return this.gameResultBean;
  }

  /**
   * Getter for the main window reference.
   *
   * @return The reference to the main window object.
   */
  public MainWindow getMainWindow() {
    return this.mainWindow;
  }

  /**
   * Setter for the main window reference.
   *
   * @param mainWindow Reference to the main window object.
   */
  public void setMainWindow(MainWindow mainWindow) {
    this.mainWindow = mainWindow;
  }

  /**
   * Getter for the player dialog frame.
   *
   * @return reference to the player dialog frame.
   */
  public PlayerDialogBaseFrame getPlayerDialogFrame() {
    return this.playerDialogFrame;
  }

  /**
   * Setter for the player dialog frame.
   *
   * @param playerDialogFrame reference to the player dialog frame.
   */
  public void setPlayerDialogFrame(PlayerDialogBaseFrame playerDialogFrame) {
    this.playerDialogFrame = playerDialogFrame;
  }

  /**
   * Getter for the past input panel reference.
   *
   * @return Reference to the last input panel.
   */
  public LastInputPanel getLastInputPanel() {
    return this.lastInputPanel;
  }

  /**
   * Setter for the last input panel reference.
   *
   * @param lastInputPanel Reference to the last input panel to set.
   */
  public void setLastInputPanel(LastInputPanel lastInputPanel) {
    this.lastInputPanel = lastInputPanel;
  }

  /**
   * Getter for property 'settingsFilePath'
   * (absolute path to the settings file, including filename).
   *
   * @return Value for property 'settingsFilePath'.
   */
  public String getSettingsFilePath() {
    return this.settingsFilePath;
  }

  /**
   * Setter for property 'settingsFilePath'
   * (absolute path to the settings file, including filename).
   *
   * @param settingsFilePath Value to set for property 'settingsFilePath'.
   */
  public void setSettingsFilePath(String settingsFilePath) {
    this.settingsFilePath = settingsFilePath;
  }

  /**
   * Gets reference to the main controller bean.
   *
   * @return reference to the main controller bean.
   */
  public MainController getMainController() {
    return this.mainController;
  }

  /**
   * Sets the main controller bean reference.
   *
   * @param mainController reference to the main controller bean.
   */
  public void setMainController(MainController mainController) {
    this.mainController = mainController;
  }

  /** Private methods ********************** */

  /**
   * Gets a <code>LocaleExt</code> object given it's
   * corresponding language name (case insensitive).
   *
   * @param localeId Locale identifier (case insensitive langiage name).
   * @return <code>LocaleExt</code> object given it's
   *         corresponding language name (case insensitive).
   * @throws ServiceException if locale with the given ID is not found.
   */
  private static LocaleExt findLocaleById(String localeId) throws ServiceException {
    for (LocaleExt locale : AVAILABLE_LOCALES) {
      if (locale.getLocale().getLanguage().equalsIgnoreCase(localeId)) {
        return locale;
      }
    }
    throw new ServiceException("Locale with ID (language) \"" + localeId + "\" is not found!");
  }

  /**
   * Returns a string that represents an absolute path
   * to the settings file including the file name. If there are
   * custom directories in the path that don't exist, they will be created.
   * Note that the path is platform-specific.
   *
   * @return Absolute path to the settings file including the file name.
   */
  private static String getSettingsFilePathHelper() {
    StringBuilder path = new StringBuilder(System.getProperties().getProperty("user.home"));
    path.append(File.separatorChar);
    switch (Utilities.getPlatformType()) {
      case MAC_OS:
        path.append("Library").append(File.separatorChar).append("Preferences").
            append(File.separatorChar).append(APPLICATION_NAME);
        createDirIfDoesntExist(path);
        path.append(File.separatorChar).append(SETTINGS_FILE_NAME);
        break;
      case LINUX:
        path.append('.').append(SETTINGS_FILE_NAME);
        break;
      case WINDOWS:
        path.append("UserData");
        createDirIfDoesntExist(path);
        path.append(File.separatorChar).append(SETTINGS_FILE_NAME);
        break;
      default:
        path.append(File.separatorChar).append(SETTINGS_FILE_NAME);
    }
    return path.toString();
  }

  /**
   * Creates a directory if it doesn't exist
   * (only the last one in the provided path).
   *
   * @param path Path to the directory.
   */
  private static void createDirIfDoesntExist(StringBuilder path) {
    File dir = new File(path.toString());
    if (dir.exists() == false) {
      dir.mkdir();
    }
  }

  /**
   * Gets the choose player dialog (note, that it will
   * be created if if doesn't exist).
   *  
   * @return reference to the choose player dialog.
   */
  public ChoosePlayerDialog getChoosePlayerDialog() {
    if (this.choosePlayerDialog == null) {
      this.choosePlayerDialog = new ChoosePlayerDialog(this.playerDialogFrame);
      this.choosePlayerDialog.setVisible(true);
    }

    return this.choosePlayerDialog;
  }

  /** Destroys the choose player dialog. */
  public void disposeChoosePlayerDialog() {
    if (this.choosePlayerDialog != null) {
      this.choosePlayerDialog.setVisible(false);
      this.choosePlayerDialog.dispose();
    }
    this.choosePlayerDialog = null;
  }

}
