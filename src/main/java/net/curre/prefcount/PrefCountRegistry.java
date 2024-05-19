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
import net.curre.prefcount.service.LafThemeService;
import net.curre.prefcount.service.ServiceException;
import net.curre.prefcount.service.SettingsService;
import net.curre.prefcount.util.LocaleExt;

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
  private static final Logger log = Logger.getLogger(PrefCountRegistry.class.toString());

  /** Location of the images' directory. */
  public static final String IMAGES_DIR = "images";

  /** Default value for the locale ID (case-insensitive language name). */
  public static final String DEFAULT_LOCALE_ID = "ru";

  /** Array of available locales in the application. */
  public static final LocaleExt[] AVAILABLE_LOCALES = new LocaleExt[]{
      new LocaleExt("ru", "RU", "\u0420\u0443\u0441\u0441\u043A\u0438\u0439"),
      new LocaleExt("us", "US", "English US")
  };

  /** Reference to the singleton instance of this class. */
  private static final PrefCountRegistry instance;

  /**
   * Current locale (the first locale on the
   * availableLocales array is the default).
   */
  private static LocaleExt currentLocale = AVAILABLE_LOCALES[0];

  /** Reference to the menu items bean. */
  private final MenuItemsBean menuItemsBean;

  /** Reference to the game settings service. */
  private final SettingsService settingsService;

  /** Reference to the Look and Feel service. */
  private final LafThemeService lafThemeService;

  /** Reference to the result bean */
  private final GameResultBean gameResultBean;

  static {
    instance = new PrefCountRegistry();
  }

  /** Reference to the main window. */
  private MainWindow mainWindow;

  /** Reference to the player dialog base frame. */
  private PlayerDialogBaseFrame playerDialogFrame;

  /** Reference to the last input panel. */
  private LastInputPanel lastInputPanel;

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
    this.settingsService = new SettingsService(null);
    this.menuItemsBean = new MenuItemsBean();
    this.gameResultBean = new GameResultBean();
    this.lafThemeService = new LafThemeService();
  }

  /**
   * Gets the settings service.
   *
   * @return reference to the settings service.
   */
  public SettingsService getSettingsService() {
    return this.settingsService;
  }

  /**
   * Gets a reference to the LAF theme service.
   *
   * @return reference to the LAF theme service
   */
  public LafThemeService getLafThemeService() {
    return this.lafThemeService;
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
   * @param localeId Identifier (case-insensitive language name)
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

  /**
   * Gets a <code>LocaleExt</code> object given its
   * corresponding language name (case-insensitive).
   *
   * @param localeId Locale identifier (case insensitive language name).
   * @return <code>LocaleExt</code> object given its
   *         corresponding language name (case-insensitive).
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
   * Creates a directory if it doesn't exist
   * (only the last one in the provided path).
   *
   * @param path Path to the directory.
   */
  @SuppressWarnings("ResultOfMethodCallIgnored")
  private static void createDirIfDoesntExist(StringBuilder path) {
    File dir = new File(path.toString());
    if (!dir.exists()) {
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
