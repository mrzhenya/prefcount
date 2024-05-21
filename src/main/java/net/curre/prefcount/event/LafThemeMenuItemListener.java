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

package net.curre.prefcount.event;

import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.gui.theme.LafThemeInterface;
import net.curre.prefcount.service.LafThemeService;
import net.curre.prefcount.service.SettingsService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.CheckboxMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Object of this class represents a listener
 * for a L&F menu item (a <code>CheckboxMenuItem</code> object).
 * <p/>
 * Created date: Jan 22, 2008
 *
 * @author Yevgeny Nyden
 */
public class LafThemeMenuItemListener implements ActionListener, ItemListener {

  /** Private class logger. */
  private static final Logger logger = LogManager.getLogger(LafThemeMenuItemListener.class.getName());

  /** The Pref skin this listener is for. */
  private final LafThemeInterface lafTheme;

  /**
   * Constructor that sets the skin this  listener is for.
   *
   * @param lafTheme Pref skin to set.
   */
  public LafThemeMenuItemListener(LafThemeInterface lafTheme) {
    this.lafTheme = lafTheme;
  }

  /**
   * Changes the current L&F skin to the listener's skin
   * (this.skin). This ItemListener method is for awt menu only.
   * <p/>
   * {@inheritDoc}
   */
  public void itemStateChanged(ItemEvent itemEvent) {
    if (itemEvent.getStateChange() == ItemEvent.DESELECTED) {
      ((CheckboxMenuItem) itemEvent.getSource()).setState(true);
    }
    changeLafTheme();
  }

  /**
   * Changes the current L&F skin to the listener's skin
   * (this.skin). This ActionListener method is for swing
   * menu only.
   * <p/>
   * {@inheritDoc}
   */
  public void actionPerformed(ActionEvent actionEvent) {
    changeLafTheme();
  }

  /** Helper method that performs LAF skin change. */
  private void changeLafTheme() {
    try {
      PrefCountRegistry registry = PrefCountRegistry.getInstance();
      LafThemeService lafService = registry.getLafThemeService();
      lafService.activateLafTheme(this.lafTheme.getId());
      SettingsService settingsService = registry.getSettingsService();
      settingsService.getSettings().setLafThemeId(lafService.getCurrentLafThemeId());
      settingsService.persistSettings();
      // TODO - update the registered UI components.
//      registry.getLandingUi().updateLandingUi();
      // TODO - show the restart dialog.
//      registry.getUiService().showRestartGameDialog();
    } catch (Exception e) {
      logger.log(Level.WARN,"Unable to save settings.", e);
    }
  }
}
