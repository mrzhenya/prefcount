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

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import net.curre.prefcount.App;
import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.bean.GameResultBean;
import net.curre.prefcount.bean.Settings;
import net.curre.prefcount.event.ClosingWindowListener;
import net.curre.prefcount.gui.game.DataCardsContainerPanel;
import net.curre.prefcount.gui.menu.PrefCountMenuBar;
import net.curre.prefcount.gui.theme.LafTheme;
import net.curre.prefcount.gui.theme.PrintTheme;

import net.curre.prefcount.service.MainService;
import net.curre.prefcount.service.UiService;
import net.curre.prefcount.util.LocaleExt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class represent the main frame, which contains
 * the intro message and the score board.
 * <p/>
 * Creation date: Mar 8, 2007
 *
 * @author Yevgeny Nyden
 */
public class MainWindow extends JFrame implements Printable {

  /** Private class logger. */
  private static final Logger logger = LogManager.getLogger(MainWindow.class.getName());

  /** Reference to the game options cards panel (which contains all the data input fields). */
  protected DataCardsContainerPanel inputDataPanel;

  /** Reference to the score board panel (when the scores are drawn). */
  protected ScoreBoardPanel scoreBoardPanel;

  /** Reference to the menu bar. */
  private final PrefCountMenuBar prefCountMenuBar;

  /**
   * Constructor that sets frame visibility.
   */
  public MainWindow() {
    super(LocaleExt.getString("pref.scoreboard.title"));

    logger.info("Creating main window");
    PrefCountRegistry registry = PrefCountRegistry.getInstance();
    Settings settings = registry.getSettingsService().getSettings();

    super.setIconImage(Toolkit.getDefaultToolkit().createImage(
        App.class.getResource("images/PrefCount-16x16.png")));
    super.setSize(settings.getMainWindowWidth(), settings.getMainWindowHeight());
    super.setPreferredSize(new Dimension(settings.getMainWindowWidth(), settings.getMainWindowHeight()));
    super.setResizable(true);
    super.addWindowListener(new ClosingWindowListener(MainService::quitApp));

    Container contentPane = super.getContentPane();
    contentPane.setLayout(new BorderLayout());

    // Panel that contains player data input UI.
    this.inputDataPanel = new DataCardsContainerPanel(this);
    contentPane.add(this.inputDataPanel, BorderLayout.NORTH);

    // Panel where we draw the board and scores.
    this.scoreBoardPanel = new ScoreBoardPanel();
    contentPane.add(this.scoreBoardPanel, BorderLayout.CENTER);

    // Window's menu bar.
    this.prefCountMenuBar = new PrefCountMenuBar();
    this.setJMenuBar(this.prefCountMenuBar);

    // Update the scoreboard with the number of players read from the settings.
    updateNumberOfPlayers(settings.getNumberOfPlayers());
    this.setLocationRelativeTo(null);
    registry.getLafThemeService().registerUITreeForUpdates(this);
  }

  /**
   * Initializes number of players.
   *
   * @param numberOfPlayers current number of players.
   */
  public void updateNumberOfPlayers(int numberOfPlayers) {
    this.scoreBoardPanel.initializeNumberOfPlayers(numberOfPlayers);
    super.invalidate();
    super.validate();
    super.repaint();
  }

  /**
   * Refreshes locale sensitive components.
   * This method is called on a locale change event.
   */
  public void refreshComponents() {
    SwingUtilities.invokeLater(this::repaint);
  }

  /** Displays the about information pane. */
  public void showAboutInfo() {
    ImageIcon icon = new ImageIcon(Objects.requireNonNull(
        App.class.getResource("images/PrefCount-48x48.png")));
    JOptionPane.showMessageDialog(this,
                                  LocaleExt.getString("pref.aboutFrame.message"),
                                  LocaleExt.getString("pref.aboutFrame.title"),
                                  JOptionPane.INFORMATION_MESSAGE,
                                  icon);
  }

  /**
   * Enables the print scores menu button.
   *
   * @param enable true to enable the button; false if otherwise.
   */
  public void enablePrintingScores(boolean enable) {
    this.prefCountMenuBar.enablePrintingScores(enable);
  }

  /** {@inheritDoc} */
  public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
    if (pageIndex > 0) {
      return (NO_SUCH_PAGE);

    } else {
      Graphics2D g2 = (Graphics2D) g;
      g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
      PrefCountRegistry registry = PrefCountRegistry.getInstance();
      final LafTheme lafTheme = registry.getLafThemeService().getCurrentLafTheme();

      final int height = (int) pageFormat.getImageableHeight();
      final int width = (int) pageFormat.getImageableWidth();
      final int y = (int) pageFormat.getImageableY();

      // drawing the header
      String header = LocaleExt.getString("pref.print.header");
      g2.setPaint(lafTheme.getPlayerNameColor());
      g2.setStroke(lafTheme.getPlayerNameStroke());
      g2.setFont(lafTheme.getPlayerNameFont());
      Dimension headerSize = UiService.determineSizeOfString(g2, header);
      g2.drawString(header, UiService.computeCenterX(width, (int) headerSize.getWidth()), y);

      // drawing the score table only if the final scores are ready
      int nextY = y + g2.getFontMetrics().getHeight() + 5;
      g2.translate(0, nextY);
      GameResultBean resultBean = PrefCountRegistry.getInstance().getGameResultBean();
      if (resultBean.isFinalScoresReady()) {
        // TODO = fix this, read the final data from the new place
/*
        JPanel table = this.playerDialogFrame.lastInputPanel.tablePanel;
        int tableX = UiService.computeCenterX(width, table.getWidth());
        g2.translate(tableX, 0);
        table.paintComponents(g2);
        g2.translate(-tableX, table.getHeight() + 15);
*/
      }

      // drawing the score board
      final int minSize = Math.min(height, width) - 50;
      final LafTheme printTheme = new PrintTheme();
      this.scoreBoardPanel.drawScoreBoard(g2, minSize, minSize, UiService.computeCenterX(width, minSize),
                                          0, null, printTheme);
      g2.translate(0, minSize + 15);

      // drawing the date
      String date = new SimpleDateFormat("HH:mm, dd MMMM yyyy (EEEE)").format(new Date());
      g2.setStroke(new BasicStroke(1));
      g2.setFont(new Font("Arial", Font.ITALIC, 12));
      Dimension dateSize = UiService.determineSizeOfString(g2, date);
      g2.drawString(date, (width - (int) dateSize.getWidth() - 70), 30);

      return (PAGE_EXISTS);
    }
  }
}
