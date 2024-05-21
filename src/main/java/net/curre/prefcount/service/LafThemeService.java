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

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.validation.constraints.NotNull;
import java.awt.Color;
import java.awt.Window;
import java.util.ArrayList;
import java.util.ResourceBundle;

import net.curre.prefcount.gui.theme.DefaultTheme;
import net.curre.prefcount.gui.theme.FlatDarkTheme;
import net.curre.prefcount.gui.theme.FlatLightTheme;
import net.curre.prefcount.gui.theme.FlatMacDarkTheme;
import net.curre.prefcount.gui.theme.LafTheme;
import net.curre.prefcount.gui.theme.LafThemeId;
import net.curre.prefcount.gui.theme.NimbusTheme;
import net.curre.prefcount.util.PlatformType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is a helper class to assist with
 * Look and Feel issues.
 * <p/>
 * Created date: Jun 11, 2007
 *
 * @author Yevgeny Nyden
 */
public class LafThemeService {

  /** Private class logger. */
  private static final Logger logger = LogManager.getLogger(LafThemeService.class.getName());

  /** Default LAF theme. */
  public static final LafThemeId DEFAULT_LAF_THEME_ID = LafThemeId.DEFAULT;

  /** List of available themes/skins. */
  private final ArrayList<LafTheme> supportedLafThemes;

  /**
   * Reference to the known Window UI components, UI tree roots to update.
   * We handle it "manually" because Frame.getFrames doesn't have them all.
   */
  private final ArrayList<Window> componentsRegistry;

  /** Current theme ID. */
  private LafThemeId currentLafThemeId;

  /** Private constructor. */
  public LafThemeService() {
    this.currentLafThemeId = DEFAULT_LAF_THEME_ID;
    this.componentsRegistry = new ArrayList<>();
    this.supportedLafThemes = new ArrayList<>();

    // For macOS, do a few extra things - https://www.formdev.com/flatlaf/macos/
    if (PlatformType.isMacOs()) {
      // Moves the menu bar to the top of the screen.
      System.setProperty("apple.laf.useScreenMenuBar", "true");
      ResourceBundle bundle = ResourceBundle.getBundle("default");
      System.setProperty("apple.awt.application.name", bundle.getString("pref.app.name"));

      // Support of the system dark/light theme.
      System.setProperty( "apple.awt.application.appearance", "system" );
    }
  }

  /**
   * Gets verified, supported LAF themes that could be used by the app.
   * @return supported LAF themes
   */
  public ArrayList<LafTheme> getSupportedThemes() {
    synchronized (this) {
      if (this.supportedLafThemes.isEmpty()) {
        this.supportedLafThemes.add(new DefaultTheme());
        this.supportedLafThemes.add(new FlatLightTheme());
        this.supportedLafThemes.add(new FlatDarkTheme());
        if (this.systemLafThemePresent(NimbusTheme.LAF_CLASS_NAME)) {
          this.supportedLafThemes.add(new NimbusTheme());
        }
        if (PlatformType.isMacOs()) {
          this.supportedLafThemes.add(new FlatMacDarkTheme());
        }
      }
    }
    return this.supportedLafThemes;
  }

  /**
   * Gets the current LAF theme ID.
   * @return current LAF theme ID
   */
  public LafThemeId getCurrentLafThemeId() {
    return this.currentLafThemeId;
  }

  /**
   * Gets the current LAF theme.
   * @return current LAF theme
   */
  public LafTheme getCurrentLafTheme() {
    try {
      return findLafThemeById(this.currentLafThemeId);
    } catch (ServiceException e) {
      // This should never occur, only at development time.
      logger.fatal("Unable to set LAF theme: {}", this.currentLafThemeId, e);
      System.exit(1);
    }
    return null;
  }

  /**
   * UI tree of the window components registered here will be updated on
   * LAF theme changes.
   * @param component window UI component to register
   */
  public void registerUITreeForUpdates(Window component) {
    this.componentsRegistry.add(component);
  }

  /**
   * Sets the Look and Feel to the passed theme and updates the UI.
   * @param lafThemeId Theme ID to set the LaF to.
   */
  public void activateLafTheme(final LafThemeId lafThemeId) {
    JFrame.setDefaultLookAndFeelDecorated(true);
    try {
      logger.info("Activating LAF theme {}", lafThemeId);
      if (findLafThemeById(lafThemeId).activateTheme()) {
        this.currentLafThemeId = lafThemeId;

        // Updating the UI of registered components (fyi, Frame.getFrames doesn't have them all).
        for (Window component : this.componentsRegistry) {
          SwingUtilities.updateComponentTreeUI(component);
          component.pack();
        }
      } else {
        logger.warn("Unable to set LAF theme: {}", lafThemeId);
      }
    } catch (Exception e) {
      logger.warn("Unable to set LAF theme: {}", lafThemeId, e);
    }
  }

  /**
   * Creates a new color that is lighter or darker than the passed color.
   * @param color model color
   * @param change value to be added to or subtracted from the RGB channels of the model color
   * @return the new, lighter color
   */
  public static @NotNull Color createAdjustedColor(@NotNull Color color, int change) {
    return new Color(getSafeColor(color.getRed() + change),
        getSafeColor(color.getGreen() + change),
        getSafeColor(color.getBlue() + change));
  }

  /**
   * Checks if the passed LAF theme is present in the UIManager.
   * @param lafClassName class name of the theme class
   * @return true if the passed theme is installed in UIManager; false if otherwise
   */
  private boolean systemLafThemePresent(String lafClassName) {
    for (UIManager.LookAndFeelInfo installedLaf : UIManager.getInstalledLookAndFeels()) {
      if (lafClassName.equals(installedLaf.getClassName())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns the LAF theme given its ID.
   * @param lafThemeId theme ID
   * @return The theme with the given ID
   * @throws ServiceException If theme with given ID was not found
   */
  private @NotNull LafTheme findLafThemeById(LafThemeId lafThemeId) throws ServiceException {
    for (LafTheme theme : this.getSupportedThemes()) {
      if (theme.getId().equals(lafThemeId)) {
        return theme;
      }
    }
    throw new ServiceException("Theme with id \"" + lafThemeId + "\" was not found!");
  }

  /**
   * Gets a safe color value withing the "0 >= value <= 255" bounds.
   * @param color color value to test
   * @return safe color value
   */
  private static int getSafeColor(int color) {
    if (color < 0) {
      return 0;
    } else if (color > 255) {
      return 255;
    }
    return color;
  }
}
