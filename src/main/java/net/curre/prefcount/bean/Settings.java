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

package net.curre.prefcount.bean;

import java.io.Serializable;

import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.gui.theme.skin.DefaultSkin;
import net.curre.prefcount.gui.type.WindowComponent;
import net.curre.prefcount.gui.type.Place;

/**
 * Object of this class represents a bean for storing
 * application settings (window size, theme, default locale, etc.).
 * <p/>
 * Created date: Jun 10, 2007
 *
 * @author Yevgeny Nyden
 */
public class Settings implements Serializable {

  /** Serial version number. */
  private static final long serialVersionUID = 39036452918252735L;

  /** Default value for the main window frame width. */
  public static final int DEFAULT_MAIN_FRAME_WIDTH = 520;

  /** Default value for the main window frame height. */
  public static final int DEFAULT_MAIN_FRAME_HEIGHT = 600;

  /** Default value for the dialog window frame width. */
  public static final int DEFAULT_DIALOG_FRAME_WIDTH = 300;

  /** Default value for the dialog window frame height. */
  public static final int DEFAULT_DIALOG_FRAME_HEIGHT = 340;

  /** Default value for the Look and Feel theme/skin ID. */
  public static final String DEFAULT_LAF_SKIN_ID = DefaultSkin.NAME_KEY;

  /** Default preferance type option. */
  public static final String DEFAULT_PREF_TYPE = WindowComponent.LENINGRAD.name();

  /** Default number of players option. */
  public static final String DEFAULT_PLAYERS_NUMBER = WindowComponent.MAIN_3_PLAYERS.name();

  /** Default divisible by option. */
  public static final String DEFAULT_DIVISIBLE_BY = WindowComponent.DIVISIBLE_IGNORE.name();

  /** Default player for the "Divisible by N" mount adjustment. */
  public static final Place DEFAULT_ADJ_PLAYER = Place.EAST;

  /** The main window frame width. */
  private int mainFrameWidth;

  /** The main window frame height. */
  private int mainFrameHeight;

  /** The dialog window frame width. */
  private int dialogFrameWidth;

  /** The dialog window frame height. */
  private int dialogFrameHeight;

  /** Look and Feel theme/skin ID (resource key). */
  private String lafSkinId;

  /** Locale identifier (case insensitive language name). */
  private String localeId;

  /** Preferance type (a WindowComponent enum name). */
  private String prefType;

  /** Number of players in the game (a WindowComponent enum name). */
  private String playersNumber;

  /** Divisible By option (a WindowComponent enum name). */
  private String divisibleBy;

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
  public int getMainFrameWidth() {
    return mainFrameWidth;
  }

  /**
   * Setter for the main window frame width.
   *
   * @param mainFrameWidth Main window frame width.
   */
  public void setMainFrameWidth(int mainFrameWidth) {
    this.mainFrameWidth = mainFrameWidth;
  }

  /**
   * Getter for the main window frame height.
   *
   * @return The main window frame height.
   */
  public int getMainFrameHeight() {
    return mainFrameHeight;
  }

  /**
   * Setter for the main window frame height.
   *
   * @param mainFrameHeight Main window frame height.
   */
  public void setMainFrameHeight(int mainFrameHeight) {
    this.mainFrameHeight = mainFrameHeight;
  }

  /**
   * Getter for the dialog window frame width.
   *
   * @return The dialog window frame width.
   */
  public int getDialogFrameWidth() {
    return dialogFrameWidth;
  }

  /**
   * Setter for the dialog window frame width.
   *
   * @param dialogFrameWidth Dialog window frame width.
   */
  public void setDialogFrameWidth(int dialogFrameWidth) {
    this.dialogFrameWidth = dialogFrameWidth;
  }

  /**
   * Getter for the dialog window frame height.
   *
   * @return The dialog window frame height.
   */
  public int getDialogFrameHeight() {
    return dialogFrameHeight;
  }

  /**
   * Setter for the dialog window frame height.
   *
   * @param dialogFrameHeight Dialog window frame height.
   */
  public void setDialogFrameHeight(int dialogFrameHeight) {
    this.dialogFrameHeight = dialogFrameHeight;
  }

  /**
   * Getter for the Look and Feel theme/skin ID (resource key).
   *
   * @return The Look and Feel theme/skin ID (resource key).
   */
  public String getLafSkinId() {
    return lafSkinId;
  }

  /**
   * Setter for the Look and Feel theme/skin ID (resource key).
   *
   * @param lafSkinId The Look and Feel theme/skin ID (resource key).
   */
  public void setLafSkinId(String lafSkinId) {
    this.lafSkinId = lafSkinId;
  }

  /**
   * Getter for the locale identifier (case
   * insensitive language name).
   *
   * @return The locale identifier (language name).
   */
  public String getLocaleId() {
    return localeId;
  }

  /**
   * Setter for the locale identifier (case
   * insensitive language name).
   *
   * @param localeId Locale identifier (case insensitive language name).
   */
  public void setLocaleId(String localeId) {
    this.localeId = localeId;
  }

  /**
   * Getter for the the Preferance type option.
   *
   * @return WindowComponent enum name that represents the Preferance type option.
   */
  public String getPrefType() {
    return this.prefType;
  }

  /**
   * Setter for property 'prefType'.
   *
   * @param prefType WindowComponent enum name that represents the Preferance type option.
   */
  public void setPrefType(String prefType) {
    this.prefType = prefType;
  }

  /**
   * Getter for the Number of players option.
   *
   * @return WindowComponent enum name that represents the Number of players option.
   */
  public String getPlayersNumber() {
    return this.playersNumber;
  }

  /**
   * Setter for property 'playersNumber'.
   *
   * @param playersNumber WindowComponent enum name that represents the Number of players option.
   */
  public void setPlayersNumber(String playersNumber) {
    this.playersNumber = playersNumber;
  }

  /**
   * Getter for the Divisible By option (a WindowComponent enum name).
   *
   * @return WindowComponent enum name that represents the Divisible By option.
   */
  public String getDivisibleBy() {
    return this.divisibleBy;
  }

  /**
   * Setter for property 'divisibleBy'.
   *
   * @param divisibleBy WindowComponent enum name that represents the Divisible By option.
   */
  public void setDivisibleBy(String divisibleBy) {
    this.divisibleBy = divisibleBy;
  }

  /** Method to reset the settings to default values. */
  public void reset() {
    initializeToDefaults();
  }

  /**
   * Tests if settings values are null and sets them to the
   * default values if they are null. This is crucial when
   * a newer application uses an older version of serialized
   * settings object.
   */
  public void testSettings() {
    if (this.lafSkinId == null) {
      this.lafSkinId = DEFAULT_LAF_SKIN_ID;
    }
    if (this.localeId == null) {
      this.localeId = PrefCountRegistry.DEFAULT_LOCALE_ID;
    }
    if (this.prefType == null) {
      this.prefType = DEFAULT_PREF_TYPE;
    }
    if (this.playersNumber == null) {
      this.playersNumber = DEFAULT_PLAYERS_NUMBER;
    }
    if (this.divisibleBy == null) {
      this.divisibleBy = DEFAULT_DIVISIBLE_BY;
    }
  }

  /** Private methods ********************** */

  /**
   * Helper method to initialize all settings
   * properties to default values.
   */
  private void initializeToDefaults() {
    this.mainFrameWidth = DEFAULT_MAIN_FRAME_WIDTH;
    this.mainFrameHeight = DEFAULT_MAIN_FRAME_HEIGHT;
    this.dialogFrameWidth = DEFAULT_DIALOG_FRAME_WIDTH;
    this.dialogFrameHeight = DEFAULT_DIALOG_FRAME_HEIGHT;
    this.lafSkinId = DEFAULT_LAF_SKIN_ID;
    this.localeId = PrefCountRegistry.DEFAULT_LOCALE_ID;
    this.prefType = DEFAULT_PREF_TYPE;
    this.playersNumber = DEFAULT_PLAYERS_NUMBER;
    this.divisibleBy = DEFAULT_DIVISIBLE_BY;
  }

}
