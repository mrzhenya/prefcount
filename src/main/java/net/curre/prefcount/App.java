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

import net.curre.prefcount.bean.Settings;
import net.curre.prefcount.gui.MainWindow;
import net.curre.prefcount.service.SettingsService;
import net.curre.prefcount.util.Utilities;
import net.curre.prefcount.util.mac.MacOsHandler;

/**
 * Object of this class represents the driver
 * to run the PrefCount application.
 * <p/>
 * Created date: May 4, 2007
 *
 * @author Yevgeny Nyden
 */
public class App {

  /**
   * Main method to run the PrefCount application.
   *
   * @param args Argument array.
   */
  public static void main(String[] args) {

    // for Mac OS, moving menu to the top and initializing Mac helper class
    if (Utilities.isMacOs()) {
      System.setProperty("apple.laf.useScreenMenuBar", "true");
      System.setProperty("com.apple.eawt.CocoaComponent.CompatibilityMode", "false");
      MacOsHandler.initializeMacOsHandler();
    }

    Settings settings = SettingsService.getSettings();
    PrefCountRegistry.getInstance().setCurrentLocale(settings.getLocaleId());
    PrefCountRegistry.getInstance().setMainWindow(new MainWindow());
  }

}
