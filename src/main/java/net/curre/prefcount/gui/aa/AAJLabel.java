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

package net.curre.prefcount.gui.aa;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;

/**
 * Object of this class represents a <code>JLabel</code>
 * that has font antialiasing enabled in the graphics context
 * when rendering this component.
 * <p/>
 * Created date: Jan 28, 2008
 *
 * @author Yevgeny Nyden
 */
public class AAJLabel extends JLabel {

  /** Ctor. */
  public AAJLabel() {}

  /**
   * Constructor that sets the label.
   *
   * @param string Label.
   */
  public AAJLabel(String string) {
    super(string);
  }

  /**
   * Constructor that sets the label.
   *
   * @param string Label.
   * @param i      int.
   */
  public AAJLabel(String string, int i) {
    super(string, i);
  }

  /**
   * Enables font anti-aliasing in the current graphics context.
   * <br />
   *
   * @param g Graphics context.
   */
  @Override
  public void paint(Graphics g) {
    ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                      RenderingHints.VALUE_ANTIALIAS_ON);
    super.paint(g);
  }
}
