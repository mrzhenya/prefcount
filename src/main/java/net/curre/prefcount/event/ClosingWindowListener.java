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


import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Basic window listener to handle run a specified task when window is closing.
 *
 * @author Yevgeny Nyden
 */
public class ClosingWindowListener implements WindowListener {

  /** Task to run on window closing. */
  private final Runnable task;

  /**
   * Ctor.
   * @param task task to run on window closing
   */
  public ClosingWindowListener(Runnable task) {
    this.task = task;
  }

  @Override
  public void windowOpened(WindowEvent e) {
  }

  @Override
  public void windowClosing(WindowEvent e) {
    this.task.run();
  }

  @Override
  public void windowClosed(WindowEvent e) {
  }

  @Override
  public void windowIconified(WindowEvent e) {
  }

  @Override
  public void windowDeiconified(WindowEvent e) {
  }

  @Override
  public void windowActivated(WindowEvent e) {
  }

  @Override
  public void windowDeactivated(WindowEvent e) {
  }
}
