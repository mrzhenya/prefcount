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
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 * Object of this class represents a <code>JPanel</code>
 * that has font antialiasing enabled in the graphics context
 * when rendering this component.
 * <p/>
 * Created date: Jan 28, 2008
 *
 * @author Yevgeny Nyden
 */
public class AAJPanel extends JPanel {

  /** Ctor. */
  public AAJPanel() {}

  /**
   * Constructor.
   *
   * @param layoutManager Layout manager to set.
   */
  public AAJPanel(LayoutManager layoutManager) {
    super(layoutManager);
  }

  /**
   * Constructor.
   *
   * @param layoutManager Layout manager to set.
   * @param b             Boolean.
   */
  public AAJPanel(LayoutManager layoutManager, boolean b) {
    super(layoutManager, b);
  }

  /**
   * Constructor.
   *
   * @param b Boolean.
   */
  public AAJPanel(boolean b) {
    super(b);
  }

  /**
   * Enables font antialiasing in the current graphics context.
   * <br />
   *
   * @param g Graphics context.
   */
  @Override
  public void paintComponent(Graphics g) {
    ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                      RenderingHints.VALUE_ANTIALIAS_ON);
    super.paintComponent(g);
  }

}
