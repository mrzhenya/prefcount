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

package net.curre.prefcount.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.gui.theme.PrintTheme;

/**
 * Object of this class represents a score board template.
 * <p/>
 * Created date: Jun 22, 2008
 *
 * @author Yevgeny Nyden
 */
public class Template implements Printable {

  /** Number of player for the template. */
  private final int numberOfPlayers;

  /**
   * Creates a new <code>Template</code> object.
   *
   * @param numberOfPlayers Number of player for the template.
   */
  public Template(int numberOfPlayers) {
    this.numberOfPlayers = numberOfPlayers;
  }

  /** {@inheritDoc} */
  public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
    if (pageIndex > 0) {
      return (NO_SUCH_PAGE);

    } else {
      Graphics2D g2 = (Graphics2D) g;
      g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

      final int height = (int) pageFormat.getImageableHeight();
      final int width = (int) pageFormat.getImageableWidth();

      // drawing the score board
      MainWindow mainWindow = PrefCountRegistry.getInstance().getMainWindow();
      final PrintTheme lafTheme = new PrintTheme();

      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                          RenderingHints.VALUE_ANTIALIAS_ON);
      mainWindow.scoreBoardPanel.drawScoreBoard(g2, width, height - 20, 0, 20,
                                                numberOfPlayers, lafTheme);
      return (PAGE_EXISTS);
    }
  }
}
