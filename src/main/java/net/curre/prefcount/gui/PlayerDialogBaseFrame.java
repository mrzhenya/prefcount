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

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;

import net.curre.prefcount.App;
import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.bean.GameResultBean;
import net.curre.prefcount.bean.Settings;
import net.curre.prefcount.event.QuitActionListener;
import net.curre.prefcount.gui.aa.AAJLabel;
import net.curre.prefcount.gui.aa.AAJPanel;
import net.curre.prefcount.gui.menu.MenuItemsBean;
import net.curre.prefcount.gui.menu.PrefCountMenuBar;
import net.curre.prefcount.gui.type.Place;
import static net.curre.prefcount.gui.type.Place.EAST;
import static net.curre.prefcount.gui.type.Place.NORTH;
import static net.curre.prefcount.gui.type.Place.SOUTH;
import static net.curre.prefcount.gui.type.Place.WEST;
import static net.curre.prefcount.gui.type.WindowComponent.DIALOG_BACK;
import static net.curre.prefcount.gui.type.WindowComponent.DIALOG_FORWARD;
import net.curre.prefcount.service.MainService;
import net.curre.prefcount.service.SettingsService;
import net.curre.prefcount.util.LocaleExt;
import net.curre.prefcount.util.Utilities;

/**
 * Object of this class represents a player question panel
 * wrapper - frame with navigation buttons and messages
 * fields common to all player input dialog windows;
 * <p/>
 * panel objects that represent individual input steps
 * go into the questionsPane, which has a card layout.
 * <p/>
 * Created date: May 6, 2007
 *
 * @author Yevgeny Nyden
 */
public class PlayerDialogBaseFrame extends JFrame {

  /** Reference to the main window object. */
  protected MainWindow mainWindow;

  /** Reference to the player dialog frame's menu bar. */
  protected PrefCountMenuBar menuBar;

  /** Navigation - back button. */
  JButton backButton;

  /** Navigation - forward/next button. */
  JButton nextButton;

  /** Reference to the (intro) message label. */
  JLabel messageLabel;

  /** Reference to the (validation) error label. */
  JLabel errorLabel;

  /** Reference to the questions pane. */
  JPanel questionsPane;

  /** Number of players in the game. */
  protected int playersNumber;

  /** Index of the current player panel. */
  private int currPlayerPanel;

  /** Reference to the players' names panel. */
  private PlayersNamesPanel playerNamesPanel;

  /** Reference to the last (score) panel. */
  LastInputPanel lastInputPanel;

  /**
   * Constructor with parameters. Note that the visibility of
   * this frame will depend on the visibility of the main window
   * frame. This frame will be visible if the main window frame
   * is visible; false otherwise.
   *
   * @param playersNumber Number of players.
   * @param mainWindow    Reference to the main window object.
   */
  public PlayerDialogBaseFrame(int playersNumber, MainWindow mainWindow) {

    super(LocaleExt.getString("pref.dialog.title"));
    LocaleExt.registerComponent(this, "pref.dialog.title");

    Image appImg = Toolkit.getDefaultToolkit().createImage(App.class.getResource("images/PrefCount-16x16.png"));
    super.setIconImage(appImg);

    this.playersNumber = playersNumber;
    this.mainWindow = mainWindow;

    this.messageLabel = new AAJLabel();
    LocaleExt.registerComponent(this.messageLabel, "pref.dialog.message.default");
    this.questionsPane = new AAJPanel();
    this.errorLabel = new AAJLabel();
    LocaleExt.registerComponent(this.errorLabel, "pref.dialog.message.default");

    LocaleExt.registerComponent(new LocaleExt.LocaleExec() {
      public void doChange() {
        getCurrentInnerPanel().setHeaderMessage(messageLabel);
      }
    }, "DIALOG_HEADER_UPDATER");

    initComponents();

    super.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    super.setLocationRelativeTo(getOwner());

    // focusing the first panel's first input text field
    super.addWindowListener(new WindowAdapter() {
      /** {@inheritDoc} */
      @Override
      public void windowOpened(WindowEvent e) {
        getCurrentInnerPanel().focusFirstInputField();
      }

      /** {@inheritDoc} */
      @Override
      public void windowClosing(WindowEvent event) {
        PlayerDialogBaseFrame.this.mainWindow.getPrefCountMenuBar().setDialogFrameItemState(false);
        if (PlayerDialogBaseFrame.this.menuBar != null) {
          PlayerDialogBaseFrame.this.menuBar.setDialogFrameItemState(false);
        }
      }
    });

    // adding a menu bar (only when running on Mac OS)
    this.menuBar = MainService.addPlayerDialogMenuBar(this);
  }

  /**
   * Initializes number of players. This method should be called
   * together with the MainWindow's initializeNumberOfPlayers()
   * method.
   *
   * @param numberOfPlayers number of players in the game.
   * @see net.curre.prefcount.gui.MainWindow#initializeNumberOfPlayers()
   */
  public void initializeNumberOfPlayers(int numberOfPlayers) {
    this.playersNumber = numberOfPlayers;

    initInputDialogs();
  }

  /**
   * Getter for the window's menu bar.
   *
   * @return <code>PrefCountMenuBar</code> reference.
   */
  public PrefCountMenuBar getPrefCountMenuBar() {
    return this.menuBar;
  }

  /**
   * Next or Previous button action helper.
   *
   * @param isNextAction True if we are switching to the next panel;
   *                     false if previous.
   */
  public void nextQuestionEventHelper(boolean isNextAction) {
    if ((isNextAction && this.nextButton.isEnabled() == false) ||
        (isNextAction == false && this.backButton.isEnabled() == false)) {
      return;
    }
    DialogInnerPanel currPanel = getCurrentInnerPanel();

    // validating the current panel if necessary
    if (currPanel.validateFields() == false) {
      return;
    }

    // do necessary activity, and move to the next panel
    currPanel.doOnLeave();
    this.currPlayerPanel = this.currPlayerPanel + (isNextAction ? 1 : -1);
    currPanel = getCurrentInnerPanel();
    currPanel.doOnEntry();

    // enabling/disabling navigation buttons and menu items
    final boolean nextButtonEnabled = currPanel.isLastPanel() == false;
    final boolean backButtonEnabled = currPanel.isFirstPanel() == false;
    final boolean computeButtonEnabled = nextButtonEnabled == false;
    this.nextButton.setEnabled(nextButtonEnabled);
    this.backButton.setEnabled(backButtonEnabled);
    if (this.menuBar != null) {
      this.menuBar.toggleNextAction(nextButtonEnabled);
      this.menuBar.toggleBackAction(backButtonEnabled);
      this.menuBar.toggleComputeAction(computeButtonEnabled);
    }

    // changing the main label
    currPanel.setHeaderMessage(this.messageLabel);

    CardLayout clay = (CardLayout) this.questionsPane.getLayout();
    if (isNextAction) {
      clay.next(this.questionsPane);  // switching to the next panel
    } else {
      clay.previous(this.questionsPane); // switching to the previous panel
    }
    currPanel.focusFirstInputField();
    this.mainWindow.repaint();      // repainting the score board
  }

  /**
   * Returns reference to the current inner panel.
   *
   * @return Reference to the current inner panel.
   */
  public DialogInnerPanel getCurrentInnerPanel() {
    return (DialogInnerPanel) this.questionsPane.getComponents()[this.currPlayerPanel];
  }

  /**
   * Getter for property 'currPlayerPanel' -
   * index of the current player panel.
   *
   * @return Value for property 'currPlayerPanel'.
   */
  public int getCurrPlayerPanel() {
    return this.currPlayerPanel;
  }

  /**
   * This method reads values from the current Settings
   * object and changes the PlayerDialogBasePanel's settings accordingly.
   * It should be called together with the MainMainWindow's
   * readFromCurrentSettings() method.
   * Settings that are read and reset:
   * <ul>
   * <li>PlayerDialogBasePanel size;</li>
   * </ul>
   *
   * @see MainWindow#readFromCurrentSettings()
   */
  public void readFromCurrentSettings() {
    // this is the current settings
    Settings settings = SettingsService.getSettings();

    // setting the frame size
    super.setSize(settings.getDialogFrameWidth(), settings.getDialogFrameHeight());
  }

  /**
   * Checks if there is some data entered into the
   * player dialog (at least one player name is not blank).
   *
   * @return true if at least one player name is not blank; false otherwise.
   */
  public boolean isSomeDataEntered() {
    return playerNamesPanel != null && playerNamesPanel.isSomeDataEntered();
  }

  /**
   * Refreshes the score table - updates the header
   * labels according to the current locale.
   */
  public void refreshTable() {
    this.lastInputPanel.refreshTable();
  }

  /**
   * Sets/hides the error label.
   *
   * @param messageKey Error message key or null if the error
   *                   label should be hidden.
   */
  void toggleErrorField(String messageKey) {
    if (messageKey == null) {
      errorLabel.setText("");
      errorLabel.setVisible(false);
    } else {
      errorLabel.setText(LocaleExt.getString(messageKey));
      errorLabel.setVisible(true);
      LocaleExt.reregisterComponent(errorLabel, messageKey);
    }
  }

  /**
   * Sets players names in the GameResultBean bean.
   *
   * @param playersNames map with player names.
   */
  void setPlayersNames(Map<Place, String> playersNames) {
    GameResultBean resultBean = PrefCountRegistry.getInstance().getGameResultBean();
    for (Map.Entry<Place, String> entry : playersNames.entrySet()) {
      String name = entry.getValue();
      resultBean.getPlayerStats().get(entry.getKey()).setPlayerName(name);
    }
  }

  /** Private methods ********************** */

  /** Initializes the components. */
  private void initComponents() {

    MenuItemsBean menuItemsBean = PrefCountRegistry.getInstance().getMenuItemsBean();

    // adding Ctrl-Q - quit shortcut to the panel if not on Mac OS;
    // Mac OS will have a menu for every frame (with a Quit shortcut)
    JPanel mainContentPanel = new JPanel();
    mainContentPanel.setOpaque(false);
    if (Utilities.isMacOs() == false) {
      mainContentPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
          .put(KeyStroke.getKeyStroke("control Q"), "exitAppAction");
      mainContentPanel.getActionMap().put("exitAppAction", new QuitActionListener());
    }

    //======== this ========
    Container contentPaneOut = getContentPane();
    contentPaneOut.setLayout(new BorderLayout(5, 5));

    // creating and initializing the navigation panel
    JPanel navigationPanel = new JPanel();
    {
      navigationPanel.setOpaque(false);
      navigationPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));

      // adding the button shortcuts (only if we are not running on Mac OS);
      // we assume here, that for Mac OS, all button shortcuts are added to
      // the frame's menu bar
      JPanel pane = Utilities.isMacOs() ? null : this.questionsPane;

      //---- backButton ----
      this.backButton = menuItemsBean.getJButton(DIALOG_BACK, pane);
      this.backButton.setEnabled(false);
      LocaleExt.registerComponent(this.backButton, DIALOG_BACK);
      navigationPanel.add(this.backButton);
      navigationPanel.add(new JPanel(null));

      //---- nextButton ----
      this.nextButton = menuItemsBean.getJButton(DIALOG_FORWARD, pane);
      this.nextButton.setEnabled(true);
      LocaleExt.registerComponent(this.nextButton, DIALOG_FORWARD);
      navigationPanel.add(this.nextButton);
    }
    contentPaneOut.add(navigationPanel, BorderLayout.NORTH);

    //======== mainContentPanel ========
    {
      mainContentPanel.setLayout(new BorderLayout());

      //======== messagePanel ========
      JPanel messagePanel = new JPanel();
      {
        messagePanel.setOpaque(false);
        messagePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));

        //---- messageLabel ----
        messageLabel.setText(LocaleExt.getString("pref.dialog.message.default"));
        messagePanel.add(messageLabel);
      }
      mainContentPanel.add(messagePanel, BorderLayout.NORTH);

      JPanel contentPaneWrapper = new JPanel();
      //======== contentPane ========
      {
        contentPaneWrapper.setLayout(new FlowLayout());
        questionsPane.setLayout(new CardLayout());
        contentPaneWrapper.add(questionsPane);
      }
      mainContentPanel.add(contentPaneWrapper, BorderLayout.CENTER);
    }
    contentPaneOut.add(mainContentPanel, BorderLayout.CENTER);

    // creating and initializing the error panel and label
    JPanel errorPanel = new JPanel();
    {
      errorPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
      errorPanel.setOpaque(false);
      this.errorLabel.setText(LocaleExt.getString("pref.dialog.errorLabel.default"));
      this.errorLabel.setForeground(Color.RED);
      this.errorLabel.setVisible(false);
      this.errorLabel.setOpaque(false);
      errorPanel.add(this.errorLabel);
    }
    contentPaneOut.add(errorPanel, BorderLayout.SOUTH);

    setLocationRelativeTo(getOwner());
  }

  /**
   * Creates input dialog components and
   * adds them to the questionPane.
   */
  private void initInputDialogs() {
    // resetting buttons and labels; removing error message (if any)
    toggleErrorField(null);
    this.currPlayerPanel = 0;
    this.backButton.setEnabled(false);
    this.nextButton.setEnabled(true);
    if (this.questionsPane.getComponentCount() > 0) {
      for (Component c : this.questionsPane.getComponents()) {
        if (c instanceof DialogInnerPanel) {
          ((DialogInnerPanel) c).unregisterLocaleSensitiveComponents();
        }
      }
      this.questionsPane.removeAll();
    }

    this.playerNamesPanel = new PlayersNamesPanel(this, this.playersNumber);
    this.playerNamesPanel.setHeaderMessage(this.messageLabel);
    this.questionsPane.add(this.playerNamesPanel, "Player Names");
    this.questionsPane.add(new PlayerDataPanel(this, this.playersNumber, EAST), EAST.name());
    this.questionsPane.add(new PlayerDataPanel(this, this.playersNumber, SOUTH), SOUTH.name());
    this.questionsPane.add(new PlayerDataPanel(this, this.playersNumber, WEST), WEST.name());
    if (this.playersNumber == 4) {
      this.questionsPane.add(new PlayerDataPanel(this, this.playersNumber, NORTH), NORTH.name());
    }

    this.lastInputPanel = new LastInputPanel(this);
    PrefCountRegistry.getInstance().setLastInputPanel(this.lastInputPanel);
    this.questionsPane.add(this.lastInputPanel, "Last Input");
  }

}
