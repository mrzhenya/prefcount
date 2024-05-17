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

package net.curre.prefcount.gui;

import java.util.Map;
import javax.swing.JTextField;

import net.curre.prefcount.gui.type.Place;

/**
 * This class is a helper class to access protected
 * and package protected properties and methods in the
 * net.curre.prefcount.gui package.
 * <p/>
 * Created date: Jul 16, 2007
 *
 * @author Yevgeny Nyden
 */
public class GuiPackageHelper {

  /**
   * Gets the list of references to the player names fields
   * from the given <code>PlayersNamesPanel</code> object.
   *
   * @param panel <code>PlayersNamesPanel</code> object to use.
   * @return The list of references to the player names fields.
   */
  public static Map<Place, JTextField> getPlayerFields(PlayersNamesPanel panel) {
    return panel.playersFields;
  }

  /** Private methods ***********************/

}
