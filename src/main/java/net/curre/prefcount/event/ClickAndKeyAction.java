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

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.validation.constraints.NotNull;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Basic base action to use for button that support mouse click/action
 * and key press (enter). To create an action of this class, use the
 * <code>createAndAddAction</code> method, that also conveniently adds
 * this action to the passed button.
 *
 * @author Yevgeny Nyden
 * @see #createAndAddAction(JButton, Runnable)
 */
public class ClickAndKeyAction extends AbstractAction implements KeyListener {

  /**
   * Creates a new ClickAndKeyAction and adds it to the passed button
   * as an action handler and a key listener.
   * @param button button to add the action to
   * @param actionHandler runnable to execute for this action
   */
  public static void createAndAddAction(@NotNull JButton button, Runnable actionHandler) {
    ClickAndKeyAction action = new ClickAndKeyAction(actionHandler);
    // Saving button text since it will be erased by the setAction call.
    String tempText = button.getText();
    button.setAction(action);
    button.setText(tempText);
  }

  /** Runnable to execute on action. */
  private final Runnable actionHandler;

  /**
   * Ctor.
   * @param actionHandler runnable to execute on action
   */
  private ClickAndKeyAction(Runnable actionHandler) {
    this.actionHandler = actionHandler;
  }

  /**
   * Handles the mouse click action.
   * @param e the event to be processed
   */
  public void actionPerformed(ActionEvent e) {
    this.actionHandler.run();
  }

  @Override
  public void keyTyped(KeyEvent e) {}

  /**
   * Handles the key press event and handles action when Enter is pressed.
   * @param e the event to be processed
   */
  @Override
  public void keyPressed(@NotNull KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
      this.actionHandler.run();
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
  }
}
