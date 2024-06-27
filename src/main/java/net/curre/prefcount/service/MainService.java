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

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.bean.Settings;
import net.curre.prefcount.gui.MainWindow;
import net.curre.prefcount.gui.Template;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
  private static final Logger logger = LogManager.getLogger(MainService.class.getName());

  /** Private constructor to prevent instantiation. */
  private MainService() {
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
        logger.error("Error while printing", e);
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
        logger.error("Error while printing template", e);
      }
    }
  }

  /** Disposes all frames and quits the application. */
  public static void quitApp() {
    PrefCountRegistry registry = PrefCountRegistry.getInstance();
    MainWindow window = registry.getMainWindow();

    // Storing the main window and the player dialog size settings,
    // then, disposing the components.
    Settings settings = registry.getSettingsService().getSettings();
    settings.setMainWindowHeight(window.getHeight());
    settings.setMainWindowWidth(window.getWidth());

    registry.getSettingsService().persistSettings();

    window.setVisible(false);
    window.dispose();

    System.exit(0);
  }
}
