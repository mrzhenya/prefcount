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

package net.curre.prefcount.service;

import java.awt.MenuBar;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.gui.HelpFrame;
import net.curre.prefcount.gui.MainWindow;
import net.curre.prefcount.gui.Template;
import net.curre.prefcount.gui.menu.AwtMenuBar;
import net.curre.prefcount.gui.menu.PrefCountMenuBar;
import net.curre.prefcount.gui.menu.SwingMenuBar;
import net.curre.prefcount.gui.type.WindowComponent;
import net.curre.prefcount.util.Utilities;

/**
 * This service bean is responsible for
 * handling various general tasks such as quitting, printing, etc.
 * <p/>
 * Created date: Jun 15, 2008
 *
 * @author Yevgeny Nyden
 */
public class MainService {

  /** Private class logger. */
  private static final Logger log = Logger.getLogger(MainService.class.toString());

  /** Reference to the help frame. */
  private static HelpFrame helpFrame;

  /** Private constructor to prevent instantiation. */
  private MainService() {
  }

  /**
   * Displays help information in a separate window.
   *
   * @param itemEnum help type.
   */
  public static synchronized void doShowHelp(WindowComponent itemEnum) {

    if (helpFrame == null) {
      helpFrame = new HelpFrame();
    }

    helpFrame.refreshText(itemEnum);
  }

  /** Sends the game results to the OS printing system. */
  public static void doPrint() {
    MainWindow window = PrefCountRegistry.getInstance().getMainWindow();
    PrinterJob printJob = PrinterJob.getPrinterJob();
    printJob.setPrintable(window);
    if (printJob.printDialog()) {
      try {
        printJob.print();
      } catch (PrinterException e) {
        log.severe("Error printing: " + e);
      }
    }
  }

  /**
   * Sends the score board template to the OS printing system.
   *
   * @param numberOfPlayers number of players for the template.
   */
  public static void doPrintTemplate(int numberOfPlayers) {
    Template template = new Template(numberOfPlayers);
    PrinterJob printJob = PrinterJob.getPrinterJob();
    printJob.setPrintable(template);
    if (printJob.printDialog()) {
      try {
        printJob.print();
      } catch (PrinterException e) {
        log.severe("Error printing: " + e);
      }
    }
  }

  /** Disposes all frames and quits the application. */
  public static void quitApp() {
    if (helpFrame != null) {
      helpFrame.setVisible(false);
      helpFrame.dispose();
    }

    MainWindow window = PrefCountRegistry.getInstance().getMainWindow();
    window.setVisible(false);

    if (window.playerDialogFrame != null) {
      window.playerDialogFrame.setVisible(false);
      window.playerDialogFrame.dispose();
    }
    window.dispose();

    System.exit(0);
  }

  /**
   * Creates a menu bar for the player dialog window
   * and sets it on the mainWindow.playerDialogFrame object.
   * This menu bar is created and added only when running
   * on macOS platform.
   *
   * @param frame Reference to the frame to add this menu bar to.
   * @return Reference to the created PrefCount menu bar or
   *         null of running on not macOS platform.
   */
  public static PrefCountMenuBar addPlayerDialogMenuBar(JFrame frame) {
    PrefCountMenuBar menuBar = null;
    if (Utilities.isMacOs()) {
      menuBar = new AwtMenuBar(PrefCountMenuBar.MenuBarType.PLAYER_DIALOG);
      frame.setMenuBar((MenuBar) menuBar);
    }
    return menuBar;
  }

  /**
   * Creates an appropriate menu bar for the main window
   * and sets it on the mainWindow object. Since substance
   * LAF does not work with native Mac menu bar, an awt
   * menu bar is created for the Mac platform; for other
   * platforms, a swing menu bar is created.
   *
   * @param frame Frame to add menu bar to.
   * @return Reference to the created PrefCount menu bar.
   */
  public static PrefCountMenuBar addMainWindowMenuBar(JFrame frame) {
    PrefCountMenuBar menuBar;
    if (Utilities.isMacOs()) {
      menuBar = new AwtMenuBar(PrefCountMenuBar.MenuBarType.MAIN_WINDOW);
      frame.setMenuBar((MenuBar) menuBar);
    } else {
      menuBar = new SwingMenuBar();
      frame.setJMenuBar((JMenuBar) menuBar);
    }
    return menuBar;
  }
}
