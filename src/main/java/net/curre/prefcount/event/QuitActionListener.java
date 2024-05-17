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

package net.curre.prefcount.event;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.curre.prefcount.service.MainService;

/**
 * Object of this class represents an action listener
 * to use for quitting application.
 * <p/>
 * Created date: Jan 22, 2008
 *
 * @author Yevgeny Nyden
 */
public class QuitActionListener extends AbstractAction implements ActionListener {

  /**
   * Disposes frames and quits application.
   *
   * @param actionEvent Action event.
   */
  public void actionPerformed(ActionEvent actionEvent) {
    MainService.doQuit();
  }

}
