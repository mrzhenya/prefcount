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

import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Frame;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.curre.prefcount.gui.theme.skin.AquaSkin;
import net.curre.prefcount.gui.theme.skin.BusinessSkin;
import net.curre.prefcount.gui.theme.skin.CremeSkin;
import net.curre.prefcount.gui.theme.skin.DefaultSkin;
import net.curre.prefcount.gui.theme.skin.FieldOfWheatSkin;
import net.curre.prefcount.gui.theme.skin.FindingNemoSkin;
import net.curre.prefcount.gui.theme.skin.GreenMagicSkin;
import net.curre.prefcount.gui.theme.skin.MangoSkin;
import net.curre.prefcount.gui.theme.skin.ModerateSkin;
import net.curre.prefcount.gui.theme.skin.NebulaSkin;
import net.curre.prefcount.gui.theme.skin.OfficeBlue2007Skin;
import net.curre.prefcount.gui.theme.skin.PrefSkin;
import net.curre.prefcount.util.Utilities;

import org.jvnet.substance.SubstanceLookAndFeel;

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
  private static Logger log = Logger.getLogger(LafThemeService.class.toString());

  /** Array of available themes/skins. */
  public static final PrefSkin[] AVAILABLE_SKINS = {
      new DefaultSkin(), new AquaSkin(), new BusinessSkin(),
      new ModerateSkin(), new NebulaSkin(), new OfficeBlue2007Skin(),
      new GreenMagicSkin(), new MangoSkin(), new CremeSkin(),
      new FieldOfWheatSkin(), new FindingNemoSkin()
  };

  /** This holds a reference to the singleton instance. */
  private static LafThemeService instance = new LafThemeService();

  /** Reference to the substance LAF object. */
  private SubstanceLookAndFeel substanceLaf;

  /** Reference to the current skin; */
  private PrefSkin currentSkin;

  /** Reference to the pending skin; */
  private PrefSkin pendingSkin;

  /** Flag to indicate that Substance LaF is active. */
  private boolean substanceLafActive;

  /** Private constructor. */
  private LafThemeService() {
    this.currentSkin = AVAILABLE_SKINS[0];
  }

  /**
   * Returns an instance of this class to use.
   *
   * @return singleton instance of this class to use.
   */
  public static LafThemeService getInstance() {
    return instance;
  }

  /**
   * Returns reference to the current skin.
   *
   * @return reference to the current skin.
   */
  public PrefSkin getCurrentSkin() {
    return this.currentSkin;
  }

  /**
   * Returns reference to the pending skin -
   * the skin that was chosen but was activated yet.
   *
   * @return reference to the pending skin.
   */
  public PrefSkin getPendingSkin() {
    return this.pendingSkin;
  }

  /** Clear the pending skin change (sets the pendingSkin to null). */
  public void clearPendingSkin() {
    this.pendingSkin = null;
  }

  /**
   * Sets the Look and Feel according to the
   * passed theme/skin name.
   *
   * @param skinId     Theme/skin ID (resource key) to set the LaF to.
   * @param isFirstRun Flag to indicate the first run of this method (when true).
   */
  public void setLookAndFeel(final String skinId, boolean isFirstRun) {

    // fetch the skin object given its ID
    try {
      if (isFirstRun) {
        this.pendingSkin = null;

        // checking which Laf manager to set Substance or system default
        if (DefaultSkin.NAME_KEY.equals(skinId)) {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } else {
          UIManager.setLookAndFeel(getSubstanceLaf());
          setCurrentSkinHelper(skinId);
        }

      } else {

        // checking if the new skin is different
        if (this.currentSkin.getNameResourceKey().equals(skinId) == false) {

          // we can't switch from one theme to another if current  UI manager is Substance
          // and we are switching to the system default theme or to the default Substance theme (Aqua);
          // we need to restart application for this change take effect
          if (DefaultSkin.NAME_KEY.equals(skinId) ||
              this.substanceLafActive && AquaSkin.NAME_KEY.equals(skinId)) {
            if (this.pendingSkin == null ||
                this.pendingSkin.getNameResourceKey().equals(skinId) == false) {
              this.pendingSkin = findSkinById(skinId);
              if (Utilities.displayOkCancelMessage("pref.settings.skin.needsRestart",
                                                   "pref.dialog.buttons.yes",
                                                   "pref.dialog.buttons.no")) {
                MainService.doQuit();
              }
            }

          } else {
            if (this.substanceLafActive == false) {
              UIManager.setLookAndFeel(getSubstanceLaf());
            }
            setCurrentSkinHelper(skinId);
          }
        }
      }

    } catch (Exception e) {
      log.log(Level.WARNING, "Unable to set LAF", e);
    }
  }

  /**
   * Returns the skin given its ID (resource key).
   *
   * @param skinId Skin ID (resource key).
   * @return The skin with the given ID (resource key).
   * @throws ServiceException If skin with given ID was not found.
   */
  public static PrefSkin findSkinById(String skinId) throws ServiceException {
    for (PrefSkin skin : AVAILABLE_SKINS) {
      if (skin.getNameResourceKey().equals(skinId)) {
        return skin;
      }
    }
    throw new ServiceException("Skin with id \"" + skinId + "\" was not found!");
  }

  /**
   * Helper method to lazy-load the substance Laf.
   *
   * @return Substance Laf object.
   */
  private synchronized LookAndFeel getSubstanceLaf() {
    if (this.substanceLaf == null) {
      this.substanceLaf = new SubstanceLookAndFeel();
      this.substanceLafActive = true;
    }
    return this.substanceLaf;
  }

  /**
   * Assists with changing the skin (for Substance LaF only).
   *
   * @param skinId skin id.
   * @throws ServiceException on error.
   */
  private void setCurrentSkinHelper(String skinId) throws ServiceException {
    this.pendingSkin = null;
    this.currentSkin = findSkinById(skinId);

    // no need to set the skin for the default substance theme - Aqua
    if (AquaSkin.NAME_KEY.equals(skinId) == false) {
      SubstanceLookAndFeel.setSkin(this.currentSkin.getSubstanceSkinClassName());
    }

    // updating component tree for all frames
    for (Frame frame : Frame.getFrames()) {
      SwingUtilities.updateComponentTreeUI(frame);
    }
  }

}
