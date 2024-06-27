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

package net.curre.prefcount.bean;

import java.io.Serializable;

import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.gui.theme.LafThemeId;
import net.curre.prefcount.gui.type.PrefType;
import net.curre.prefcount.gui.type.Place;

import static net.curre.prefcount.service.LafThemeService.DEFAULT_LAF_THEME_ID;

/**
 * Application default settings (window size, theme, default locale, etc.)
 * that are persisted to disk.
 * <p/>
 * Created date: Jun 10, 2007
 *
 * @author Yevgeny Nyden
 */
public class Settings implements Serializable {

  /** Serial version number. */
  private static final long serialVersionUID = 39036452918252737L;

  /** Default value for the main window frame width. */
  public static final int DEFAULT_MAIN_FRAME_WIDTH = 520;

  /** Default value for the main window frame height. */
  public static final int DEFAULT_MAIN_FRAME_HEIGHT = 600;

  /** Default preferance type option. */
  public static final PrefType DEFAULT_PREF_TYPE = PrefType.LENINGRAD;

  /** Default number of players option. */
  public static final int DEFAULT_PLAYERS_NUMBER = 3;

  /** Default divisible by N option. */
  public static final boolean DEFAULT_DIVISIBLE_BY = true;

  /** Default player for the "Divisible by N" mount adjustment. */
  public static final Place DEFAULT_ADJ_PLAYER = Place.EAST;

  /** The main window frame width. */
  private int mainWindowWidth;

  /** The main window frame height. */
  private int mainWindowHeight;

  /** Look and Feel theme/skin ID. */
  private LafThemeId lafThemeId;

  /** Locale identifier (case insensitive language name). */
  private String localeId;

  /** Preferance type. */
  private PrefType prefType;

  /** Number of players in the game. */
  private int numberOfPlayers;

  /**
   * Default constructor that initializes
   * all properties to the default values.
   */
  public Settings() {
    initializeToDefaults();
  }

  /**
   * Getter for the main window frame width.
   *
   * @return The main window frame width.
   */
  public int getMainWindowWidth() {
    return mainWindowWidth;
  }

  /**
   * Setter for the main window frame width.
   *
   * @param mainWindowWidth Main window frame width.
   */
  public void setMainWindowWidth(int mainWindowWidth) {
    this.mainWindowWidth = mainWindowWidth;
  }

  /**
   * Getter for the main window frame height.
   *
   * @return The main window frame height.
   */
  public int getMainWindowHeight() {
    return mainWindowHeight;
  }

  /**
   * Setter for the main window frame height.
   *
   * @param mainWindowHeight Main window frame height.
   */
  public void setMainWindowHeight(int mainWindowHeight) {
    this.mainWindowHeight = mainWindowHeight;
  }

  /**
   * Getter for the Look and Feel theme ID.
   *
   * @return The Look and Feel theme ID
   */
  public LafThemeId getLafThemeId() {
    return this.lafThemeId;
  }

  /**
   * Setter for the Look and Feel theme/skin ID (resource key).
   *
   * @param lafThemeId The Look and Feel theme/skin ID (resource key)
   */
  public void setLafThemeId(LafThemeId lafThemeId) {
    this.lafThemeId = lafThemeId;
  }

  /**
   * Getter for the locale identifier (case-insensitive language name).
   *
   * @return The locale identifier (language name).
   */
  public String getLocaleId() {
    return localeId;
  }

  /**
   * Setter for the locale identifier (case-insensitive language name).
   *
   * @param localeId Locale identifier (case-insensitive language name).
   */
  public void setLocaleId(String localeId) {
    this.localeId = localeId;
  }

  /**
   * Getter for the Preferance type option.
   *
   * @return the Preferance type option.
   */
  public PrefType getPrefType() {
    return this.prefType;
  }

  /**
   * Setter for property 'prefType'.
   *
   * @param prefType the new Preferance type option.
   */
  public void setPrefType(PrefType prefType) {
    this.prefType = prefType;
  }

  /**
   * Getter for the Number of players option.
   *
   * @return the number of players in the game.
   */
  public int getNumberOfPlayers() {
    return this.numberOfPlayers;
  }

  /**
   * Setter the number of players in the game
   *
   * @param numberOfPlayers the number of players in the game.
   */
  public void setNumberOfPlayers(int numberOfPlayers) {
    if (numberOfPlayers != 3 && numberOfPlayers != 4) {
      throw new IllegalArgumentException("Number of players " + numberOfPlayers + " is not supported!");
    }
    this.numberOfPlayers = numberOfPlayers;
  }

  /**
   * Helper method to initialize all settings
   * properties to default values.
   */
  private void initializeToDefaults() {
    this.mainWindowWidth = DEFAULT_MAIN_FRAME_WIDTH;
    this.mainWindowHeight = DEFAULT_MAIN_FRAME_HEIGHT;
    this.lafThemeId = DEFAULT_LAF_THEME_ID;
    this.localeId = PrefCountRegistry.DEFAULT_LOCALE_ID;
    this.prefType = DEFAULT_PREF_TYPE;
    this.numberOfPlayers = DEFAULT_PLAYERS_NUMBER;
  }
}
