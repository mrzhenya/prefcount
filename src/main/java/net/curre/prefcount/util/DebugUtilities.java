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

package net.curre.prefcount.util;

import org.apache.commons.lang3.time.FastDateFormat;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import java.awt.*;
import java.util.Calendar;

/**
 * A set of debug utilities.
 * <p/>
 * Created date: Apr 7, 2007
 *
 * @author Yevgeny Nyden
 */
public class DebugUtilities {

  /** Helper object to be used in the printTime() method. */
  public static Calendar lastTime = Calendar.getInstance();

  /**
   * This method is used for debugging purposes
   * to print timestamps and the elapsed time since the last
   * call to this method.
   *
   * @param msg Message to add to the print statement.
   */
  public static void printTime(String msg) {
    Calendar currTime = Calendar.getInstance();
    Calendar diff = Calendar.getInstance();
    long currMls = currTime.getTimeInMillis();
    diff.setTimeInMillis(currMls - lastTime.getTimeInMillis());
    FastDateFormat f = FastDateFormat.getInstance("mm:ss:SSS");
    System.out.println("TIME:::::: " + f.format(currTime.getTime()) +
        " (" + f.format(diff) + ") - " + msg);
    lastTime.setTimeInMillis(currMls);
  }

  /** Prints available looks and feels. */
  public static void printLookAndFeels() {
    UIManager.LookAndFeelInfo[] laf = UIManager.getInstalledLookAndFeels();
    for (UIManager.LookAndFeelInfo lookAndFeelInfo : laf) {
      System.out.print("LAF Name: " + lookAndFeelInfo.getName() + "\t");
      System.out.println("  LAF Class name: " + lookAndFeelInfo.getClassName());
    }
  }

  /** Displays available fonts in a frame. */
  public static void printAvailableFonts() {
    JFrame f = new JFrame("Testing Fonts");
    f.setSize(400, 400);
    JPanel mainPanel = new JPanel();
    f.add(mainPanel);
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    String[] fontNames = ge.getAvailableFontFamilyNames();
    for (String fontName : fontNames) {
      JPanel panel = new JPanel();
      JLabel label = new JLabel(fontName);
      label.setFont(new Font(fontName, Font.PLAIN, 16));
      panel.add(label);
      mainPanel.add(panel);

      System.out.println(fontName);
    }
    f.pack();
    f.setVisible(true);
  }
}
