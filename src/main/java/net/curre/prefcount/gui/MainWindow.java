/**
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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import net.curre.prefcount.App;
import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.bean.GameResultBean;
import net.curre.prefcount.bean.PlayerStatistics;
import net.curre.prefcount.bean.Settings;
import net.curre.prefcount.event.MainController;
import net.curre.prefcount.gui.menu.MenuItemsBean;
import net.curre.prefcount.gui.menu.PrefCountMenuBar;
import net.curre.prefcount.gui.theme.skin.PrefSkin;
import net.curre.prefcount.gui.theme.skin.PrintSkin;
import net.curre.prefcount.gui.type.Place;
import net.curre.prefcount.gui.type.WindowComponent;
import static net.curre.prefcount.gui.type.WindowComponent.DIVISIBLE_BY_N;
import static net.curre.prefcount.gui.type.WindowComponent.DIVISIBLE_IGNORE;
import static net.curre.prefcount.gui.type.WindowComponent.LENINGRAD;
import static net.curre.prefcount.gui.type.WindowComponent.MAIN_3_PLAYERS;
import static net.curre.prefcount.gui.type.WindowComponent.MAIN_4_PLAYERS;
import static net.curre.prefcount.gui.type.WindowComponent.SOCHINKA;
import net.curre.prefcount.service.LafThemeService;
import net.curre.prefcount.service.MainService;
import net.curre.prefcount.service.SettingsService;
import net.curre.prefcount.util.LocaleExt;
import net.curre.prefcount.util.Utilities;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstraints;

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
  private static Logger log = Logger.getLogger(MainWindow.class.toString());

  /** Reference to the player dialog frame. */
  public PlayerDialogBaseFrame playerDialogFrame;

  /** Reference to the main panel object. */
  protected JPanel mainPanel;

  /** Reference to the options panel object. */
  protected JPanel optionsPanel;

  /** Reference to the score board panel (when the scores are drawn). */
  protected ScoreBoardPanel scoreBoardPanel;

  /** Reference to the "3 players" radio buttons. */
  protected JRadioButton players3Button;

  /** Reference to the "4 players" radio buttons. */
  protected JRadioButton players4Button;

  /** Reference to the menu bar. */
  private PrefCountMenuBar prefCountMenuBar;

  /** Default constructor. */
  public MainWindow() {
    this(true);
  }

  /**
   * Constructor that sets frame visibility.
   *
   * @param isVisible True when the frame should be made visible by default;
   *                  false when the frame shoudl be invisible instead.
   */
  public MainWindow(boolean isVisible) {
    super(LocaleExt.getString("pref.scoreboard.title"));

    log.fine("Creating main window");

    LocaleExt.registerComponent(this, "pref.scoreboard.title");

    // creating and setting the windows icon
    Image appImg = Toolkit.getDefaultToolkit().createImage(App.class.getResource("images/PrefCount-16x16.png"));
    super.setIconImage(appImg);

    MenuItemsBean menuItemsBean = PrefCountRegistry.getInstance().getMenuItemsBean();
    super.setResizable(true);

    // creating the pref type options
    JRadioButton leningradButton = menuItemsBean.getJRadioButton(LENINGRAD);
    JRadioButton sochiButton = menuItemsBean.getJRadioButton(SOCHINKA);
    JPanel prefTypePanel = createOptionSubPanel("pref.scoreboard.prefType.title",
                                                new JRadioButton[]{leningradButton, sochiButton});

    // creating the number of players options
    players3Button = menuItemsBean.getJRadioButton(MAIN_3_PLAYERS);
    players4Button = menuItemsBean.getJRadioButton(MAIN_4_PLAYERS);
    JPanel playerNumberPanel = createOptionSubPanel("pref.scoreboard.players.title",
                                                    new JRadioButton[]{players3Button, players4Button});

    // creating the divisibility options
    JRadioButton divisibleBy3Button = menuItemsBean.getJRadioButton(DIVISIBLE_IGNORE);
    JRadioButton divisibleBy4Button = menuItemsBean.getJRadioButton(DIVISIBLE_BY_N);
    JPanel divisibilityPanel = createOptionSubPanel("pref.scoreboard.divisible.title",
                                                    new JRadioButton[]{divisibleBy3Button, divisibleBy4Button});

    this.optionsPanel = new JPanel(new FlowLayout());
    this.optionsPanel.add(prefTypePanel);
    this.optionsPanel.add(playerNumberPanel);
    this.optionsPanel.add(divisibilityPanel);

    this.scoreBoardPanel = new ScoreBoardPanel();

    super.getContentPane().add(this.optionsPanel, BorderLayout.NORTH);
    super.getContentPane().add(this.scoreBoardPanel, BorderLayout.CENTER);

    this.prefCountMenuBar = MainService.addMainWindowMenuBar(this);

    this.playerDialogFrame = new PlayerDialogBaseFrame(3, this);
    PrefCountRegistry.getInstance().setPlayerDialogFrame(this.playerDialogFrame);

    // action listener to save selection in the current settings
    MainController mainController = new MainController(this, this.playerDialogFrame);
    menuItemsBean.setActionListener(mainController);
    PrefCountRegistry.getInstance().setMainController(mainController);

    // reading the current settings
    readFromCurrentSettings();
    initializeNumberOfPlayers();

    // adding window close listener
    super.addWindowListener(new WindowAdapter() {
      /** {@inheritDoc} */
      @Override
      public void windowClosing(WindowEvent event) {
        MainService.doQuit();
      }
    });

    // setting the visibility of the two frames
    super.setVisible(isVisible);
    this.playerDialogFrame.setVisible(isVisible);
  }

  /** Initializes number of players. */
  public void initializeNumberOfPlayers() {

    // determining the number of players and
    // disabling the player numbers radio buttons
    int numberOfPlayers = 4;
    if (this.players3Button.isSelected()) {
      numberOfPlayers = 3;
    }

    this.scoreBoardPanel.initializeNumberOfPlayers(numberOfPlayers);

    GameResultBean resultBean = PrefCountRegistry.getInstance().getGameResultBean();
    resultBean.clearResults();

    // creating the stats
    Map<Place, PlayerStatistics> stats = new HashMap<Place, PlayerStatistics>();
    for (Place place : Place.getPlaces(numberOfPlayers)) {
      PlayerStatistics stat = new PlayerStatistics(resultBean, place);
      stat.setPlayerName("");
      stats.put(place, stat);
    }
    resultBean.setPlayerStats(stats);

    InputMap map = this.optionsPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    map.clear();

    this.playerDialogFrame.initializeNumberOfPlayers(numberOfPlayers);
    this.playerDialogFrame.setVisible(this.isVisible());

    super.invalidate();
    super.validate();
    super.repaint();
  }

  /**
   * Refreshes locale sensitive components.
   * This method is called on a locale change event.
   */
  public void refreshComponents() {
    SwingUtilities.invokeLater(new Runnable() {
      /** {@inheritDoc} */
      public void run() {
        LocaleExt.fireLocaleChangeEvent();
        prefCountMenuBar.refreshLanguageIcon();
        playerDialogFrame.refreshTable();
        repaint();
      }
    });
  }

  /** Displays the about information pane. */
  public void showAboutInfo() {
    ImageIcon icon = new ImageIcon(App.class.getResource("images/PrefCount-48x48.png"));
    JOptionPane.showMessageDialog(this,
                                  LocaleExt.getString("pref.aboutFrame.message"),
                                  LocaleExt.getString("pref.aboutFrame.title"),
                                  JOptionPane.INFORMATION_MESSAGE,
                                  icon);
  }

  /**
   * This method reads values from the current Settings
   * object, changes the MainWindow's settings accordingly,
   * and asks the PlayerDialogBasePanel to do the same
   * (calls PlayerDialogBasePanel.readFromCurrentSettings()).
   * Settings that are read and reset:
   * <ul>
   * <li>Current Look-and-Feel;</li>
   * <li>MainWindow size;</li>
   * <li>Option panel's radio buttons;</li>
   * <li>Menu bar option menu items;</li>
   * </ul>
   *
   * @see PlayerDialogBaseFrame#readFromCurrentSettings()
   */
  public void readFromCurrentSettings() {
    // this is the current settings
    Settings settings = SettingsService.getSettings();
    MenuItemsBean menuItemsBean = PrefCountRegistry.getInstance().getMenuItemsBean();

    // setting the LAF
    final String lafSkinId = settings.getLafSkinId();
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        LafThemeService.getInstance().setLookAndFeel(lafSkinId, true);
      }
    });

    // setting the frame size
    try {
      super.setSize(settings.getMainFrameWidth(), settings.getMainFrameHeight());
    } catch (Exception e) {
      super.setSize(Settings.DEFAULT_MAIN_FRAME_WIDTH, Settings.DEFAULT_MAIN_FRAME_HEIGHT);
    }

    // selecting the game options buttons in the options panel
    try {
      WindowComponent comp = WindowComponent.valueOf(settings.getPrefType());
      menuItemsBean.setSelected(comp, true);
    } catch (Exception e) {
      menuItemsBean.setSelected(WindowComponent.valueOf(Settings.DEFAULT_PREF_TYPE), true);
    }
    try {
      WindowComponent comp = WindowComponent.valueOf(settings.getPlayersNumber());
      menuItemsBean.setSelected(comp, true);
    } catch (Exception e) {
      menuItemsBean.setSelected(WindowComponent.valueOf(Settings.DEFAULT_PLAYERS_NUMBER), true);
    }
    try {
      WindowComponent comp = WindowComponent.valueOf(settings.getDivisibleBy());
      menuItemsBean.setSelected(comp, true);
    } catch (Exception e) {
      menuItemsBean.setSelected(WindowComponent.valueOf(Settings.DEFAULT_DIVISIBLE_BY), true);
    }

    // asking the player dialog window to read settings
    playerDialogFrame.readFromCurrentSettings();
  }

  /** {@inheritDoc} */
  public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
    if (pageIndex > 0) {
      return (NO_SUCH_PAGE);

    } else {
      Graphics2D g2 = (Graphics2D) g;
      g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
      final PrefSkin skin = LafThemeService.getInstance().getCurrentSkin();

      final int height = (int) pageFormat.getImageableHeight();
      final int width = (int) pageFormat.getImageableWidth();
      final int y = (int) pageFormat.getImageableY();

      // drawing the header
      String header = LocaleExt.getString("pref.print.header");
      g2.setPaint(skin.getPlayerNameColor());
      g2.setStroke(skin.getPlayerNameStroke());
      g2.setFont(skin.getPlayerNameFont());
      Dimension headerSize = Utilities.determineSizeOfString(g2, header);
      g2.drawString(header, Utilities.computeCenterX(width, (int) headerSize.getWidth()), y);

      // drawing the score table only if the final scores are ready
      int nextY = y + g2.getFontMetrics().getHeight() + 5;
      g2.translate(0, nextY);
      GameResultBean resultBean = PrefCountRegistry.getInstance().getGameResultBean();
      if (resultBean.isFinalScoresReady()) {
        JPanel table = this.playerDialogFrame.lastInputPanel.tablePanel;
        int tableX = Utilities.computeCenterX(width, table.getWidth());
        g2.translate(tableX, 0);
        table.paintComponents(g2);
        g2.translate(-tableX, table.getHeight() + 15);
      }

      // drawing the the score board
      final int minSize = Math.min(height, width) - 50;
      final PrefSkin printSkin = new PrintSkin();
      this.scoreBoardPanel.drawScoreBoard(g2, minSize, minSize, Utilities.computeCenterX(width, minSize),
                                          0, null, printSkin);
      g2.translate(0, minSize + 15);

      // drawing the date
      String date = new SimpleDateFormat("HH:mm, dd MMMM yyyy (EEEE)").format(new Date());
      g2.setStroke(new BasicStroke(1));
      g2.setFont(new Font("Arial", Font.ITALIC, 12));
      Dimension dateSize = Utilities.determineSizeOfString(g2, date);
      g2.drawString(date, (width - (int) dateSize.getWidth() - 70), 30);

      return (PAGE_EXISTS);
    }
  }

  /**
   * Getter for the main window's menu bar.
   *
   * @return <code>PrefCountMenuBar</code> reference.
   */
  public PrefCountMenuBar getPrefCountMenuBar() {
    return this.prefCountMenuBar;
  }

  /** *********** PRIVATE METHODS *************** */

  /**
   * Creates an option panel (one of the three).
   *
   * @param titleKey header title key.
   * @param buttons  radio buttons to add.
   * @return created panel.
   */
  private JPanel createOptionSubPanel(String titleKey,
                                      JRadioButton[] buttons) {
    String title = LocaleExt.getString(titleKey);
    JPanel outerPanel = new JPanel(
        new TableLayout(new double[][]{{TableLayout.FILL},
                                       {TableLayout.PREFERRED, TableLayout.PREFERRED}}));

    TitledBorder titledBorder = BorderFactory.createTitledBorder(title);
    LocaleExt.registerComponent(titledBorder, titleKey);
    titledBorder.setTitleFont(new Font("Arial", Font.PLAIN, 11));
    outerPanel.setBorder(titledBorder);

    int row = 0;
    for (JRadioButton button : buttons) {
      button.setFont(new Font("Arial", Font.PLAIN, 11));
      outerPanel.add(button, new TableLayoutConstraints(0, row, 0, row,
                                                        TableLayoutConstraints.LEFT,
                                                        TableLayoutConstraints.FULL));
      ++row;
    }

    return outerPanel;
  }

}
