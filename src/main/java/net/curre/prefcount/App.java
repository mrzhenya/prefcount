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

package net.curre.prefcount;

import net.curre.prefcount.bean.Settings;
import net.curre.prefcount.gui.MainWindow;
import net.curre.prefcount.service.SettingsService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.layout.PatternLayout;

import javax.swing.SwingUtilities;
import java.io.File;

/**
 * Object of this class represents the driver
 * to run the PrefCount application.
 * <p/>
 * Created date: May 4, 2007
 *
 * @author Yevgeny Nyden
 */
public class App {

  /** Private class logger. */
  private static final Logger logger = LogManager.getLogger(App.class.getName());

  /** Name of the log file (will be created in the settings directory). */
  private static final String LOG_FILENAME = "prefcount.log";

  /**
   * Main method to run the PrefCount application.
   * @param args Argument array.
   */
  public static void main(String[] args) {

    // Add file appender to log4j configuration.
    updateLogConfiguration();
    logger.info("Starting application...");

    // Then, load and activate the stored settings (LAF theme, locale, game board size).
    PrefCountRegistry registry = PrefCountRegistry.getInstance();
    SettingsService settingsService = registry.getSettingsService();
    Settings settings = settingsService.getSettings();
    registry.getLafThemeService().activateLafTheme(settings.getLafThemeId());
    registry.setCurrentLocale(settings.getLocaleId());

    SwingUtilities.invokeLater(() -> {
      // Now, start the app by showing the main UI.
      registry.setMainWindow(new MainWindow()); // The Landing UI will get displayed shortly.
    });
  }

  /**
   * Adds a file appender to log4j configuration. It has to be done programmatically
   * (vs via log4j2.xml) because the log filepath needs to be determined at run time.
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  private static void updateLogConfiguration() {
    final LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
    final Configuration config = loggerContext.getConfiguration();
    final Layout layout = PatternLayout.newBuilder()
        .withPattern("%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n")
        .withConfiguration(config).build();
    String logFilePath = SettingsService.getVerifiedSettingsDirectoryPath() + File.separatorChar + LOG_FILENAME;
    FileAppender.Builder builder = FileAppender.newBuilder();
    builder.withFileName(logFilePath)
        .withAppend(false)
        .withLocking(false)
        .setIgnoreExceptions(true);
    builder.setName("PrefCountLogFile");
    builder.setBufferedIo(true);
    builder.setBufferSize(4000);
    builder.setLayout(layout);
    builder.setImmediateFlush(true);
    builder.setConfiguration(config);
    FileAppender appender = builder.build();
    appender.start();

    config.getRootLogger().addAppender(appender, Level.INFO, null);
    loggerContext.updateLoggers();
  }
}
