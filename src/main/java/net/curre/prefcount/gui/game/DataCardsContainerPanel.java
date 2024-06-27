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

package net.curre.prefcount.gui.game;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstraints;
import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.event.ClickAndKeyAction;
import net.curre.prefcount.gui.MainWindow;
import net.curre.prefcount.gui.theme.LafTheme;
import net.curre.prefcount.gui.type.Place;
import net.curre.prefcount.util.LocaleExt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A panel that wraps the game info input panels where user
 * enters data about the game.
 *
 * @author Yevgeny Nyden
 */
public class DataCardsContainerPanel extends JPanel {

  /** Private class logger. */
  private static final Logger logger = LogManager.getLogger(DataCardsContainerPanel.class.getName());

  private static final int PADDING = 10;

  /** Reference to the main window. */
  private final MainWindow mainWindow;

  /** Label that displays the current dialog/card's title. */
  private final JLabel cardTitleLabel;

  /** Container panel for various data input panels. */
  private final JPanel dataCardsPanel;

  /** Identifies the index of the currently displayed data input panel/card. */
  private int currDataCardInd;

  /** Known data card in the order they should appear. */
  private final List<DataCard> dataCards;

  /** Map of player places to the corresponding player names. */
  private final Map<Place, String> playerNamesMap;

  /** Reference to the game preferences card/panel. */
  private GamePrefsCard gamePrefsPanel;

  /** Reference to the player names card. */
  private PlayerNamesCard playerNamesCard;

  /** Label that displays validation errors. */
  private final JLabel errorLabel;

  /** Navigation - the back button. */
  private final JButton backButton;

  /** Navigation - the forward/next button. */
  private final JButton nextButton;

  /**
   * Ctor.
   */
  public DataCardsContainerPanel(MainWindow mainWindow) {
    this.mainWindow = mainWindow;
    this.playerNamesMap = new HashMap<>();
    this.setLayout(new TableLayout(new double[][] {
        {PADDING, TableLayout.FILL, PADDING}, // columns
        {PADDING, /* title */ TableLayout.PREFERRED,
            PADDING, TableLayout.FILL, /* input data */ TableLayout.PREFERRED,
            PADDING, TableLayout.FILL, /* error */ TableLayout.PREFERRED,
            PADDING, /* buttons */ TableLayout.PREFERRED, PADDING}})); // rows
    LafTheme lafTheme = PrefCountRegistry.getInstance().getLafThemeService().getCurrentLafTheme();

    // Current input card's title.
    this.cardTitleLabel = new JLabel();
    this.cardTitleLabel.setFont(lafTheme.getInputPanelTitleFont());
    this.add(this.cardTitleLabel, new TableLayoutConstraints(
        1, 1, 1, 1, TableLayout.CENTER, TableLayout.CENTER));

    // Panel with data input panels.
    this.dataCards = new ArrayList<>();
    this.dataCardsPanel = new JPanel(new CardLayout());
    this.initializeDataCardsPanel();
    this.add(this.dataCardsPanel, new TableLayoutConstraints(
        1, 4, 1, 4, TableLayout.CENTER, TableLayout.CENTER));

    // Error label.
    this.errorLabel = new JLabel();
    this.errorLabel.setForeground(Color.RED);
    this.errorLabel.setVisible(false);
    this.add(this.errorLabel, new TableLayoutConstraints(
        1, 7, 1, 7, TableLayout.CENTER, TableLayout.CENTER));

    // Navigation panel with back/forward buttons.
    this.backButton = new JButton(LocaleExt.getString("pref.dialog.buttons.back"));
    this.nextButton = new JButton(LocaleExt.getString("pref.dialog.buttons.next"));
    JPanel navigationPanel = this.initializeNavigationPanel();
    this.add(navigationPanel, new TableLayoutConstraints(
        1, 9, 1, 9, TableLayout.CENTER, TableLayout.CENTER));
  }

  /**
   * Gets the current number of players selected.
   *
   * @return the current number of players.
   */
  public int getNumberOfPlayers() {
    return this.gamePrefsPanel.getNumberOfPlayers();
  }

  /**
   * Gets a players name given the place.
   *
   * @param place player's place
   * @return player's name
   */
  public String getPlayerName(Place place) {
    return this.playerNamesMap.get(place);
  }

  /**
   * Determines if user entered some scores data.
   *
   * @return true if any data is entered on a player scorecard; false if otherwise.
   */
  public boolean isSomeDataEntered() {
    for (DataCard dataCard : this.dataCards) {
      if (dataCard.isSomeDataEntered()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Performs the change of number of players preference.
   * @param numberOfPlayers number of players
   */
  public void updateNumberOfPlayers(int numberOfPlayers) {
    this.mainWindow.updateNumberOfPlayers(numberOfPlayers);
  }

  /**
   * Handles forward navigation action by moving to the previous card in the card layout.
   */
  private void handleBackAction() {
    logger.info("Handling the back action");
    if (this.validateCurrentCard(false)) {
      return;
    }

    this.dataCards.get(this.currDataCardInd).doOnBackwardLeave();
    this.currDataCardInd--;
    this.updateShownDataCardAndButtonsState();
  }

  /**
   * Validates the current card's inputs and if the data is valid,
   * navigates forward. If the data is not valid, displays the error.
   */
  private void handleForwardAction() {
    logger.info("Handling the forward action");
    if (this.validateCurrentCard(true)) {
      return;
    }
    DataCard currCard = this.dataCards.get(this.currDataCardInd);
    currCard.doOnForwardLeave();

    // Moving past the player names card, create players' scorecards.
    if (currCard instanceof PlayerNamesCard) {
      this.storePlayerNames();
      this.refreshPlayerScoreCards();
    }

    // Now, move to the next card and update the button states.
    this.currDataCardInd++;
    this.updateShownDataCardAndButtonsState();
  }

  /**
   * Validates the current card before a forward or a backwards navigation.
   *
   * @param isForward true if this is a forward navigation; false if a backwards one.
   * @return true if there is an error; false if validation succeeded.
   */
  private boolean validateCurrentCard(boolean isForward) {
    DataCard currCard = this.dataCards.get(this.currDataCardInd);
    String errorMsg = currCard.validateFields(isForward);
    if (errorMsg != null) {
      this.errorLabel.setText(errorMsg);
      this.errorLabel.setVisible(true);
      return true;
    }
    this.errorLabel.setText("");
    this.errorLabel.setVisible(false);
    return false;
  }

  /**
   * Updates the card layout to show the currently selected card.
   * Also, updates the enabled/disabled state of the navigation buttons.
   */
  private void updateShownDataCardAndButtonsState() {
    if (this.currDataCardInd < 0 || this.currDataCardInd >= this.dataCards.size()) {
      logger.error("Attempting to move to a non-existing card {}", this.currDataCardInd);
      return;
    }
    DataCard currentCard = this.dataCards.get(this.currDataCardInd);
    currentCard.doOnEntry();
    SwingUtilities.invokeLater(currentCard::focusFirstField);

    CardLayout clay = (CardLayout) this.dataCardsPanel.getLayout();
    this.backButton.setEnabled(this.currDataCardInd > 0);
    this.nextButton.setEnabled(
        this.currDataCardInd < 2 ||
        this.currDataCardInd + 1 < this.dataCards.size());
    clay.show(this.dataCardsPanel, currentCard.getId());
    this.cardTitleLabel.setText(currentCard.getTitle() + ":");
  }

  /**
   * Initializes the input data cards state and UI.
   */
  private void initializeDataCardsPanel() {
    // The game preferences card.
    this.gamePrefsPanel = new GamePrefsCard(this);
    this.dataCardsPanel.add(this.gamePrefsPanel, this.gamePrefsPanel.getId());
    this.dataCards.add(this.gamePrefsPanel);
    this.currDataCardInd = 0;
    this.cardTitleLabel.setText(this.gamePrefsPanel.getTitle() + ":");

    // The players names panel.
    this.playerNamesCard = new PlayerNamesCard(this);
    this.dataCardsPanel.add(this.playerNamesCard, this.playerNamesCard.getId());
    this.dataCards.add(this.playerNamesCard);

    // Player scorecards will be created later.
  }

  /**
   * Creates and initializes a panel with navigation buttons (back/forward).
   *
   * @return a panel with navigation buttons
   */
  private JPanel initializeNavigationPanel() {
    JPanel navigationPanel = new JPanel();
    navigationPanel.setLayout(new FlowLayout(FlowLayout.CENTER, PADDING, 0));
    ClickAndKeyAction.createAndAddAction(this.backButton, this::handleBackAction);
    this.backButton.setEnabled(false);
    navigationPanel.add(this.backButton);

    this.nextButton.setEnabled(true);
    ClickAndKeyAction.createAndAddAction(this.nextButton, this::handleForwardAction);
    navigationPanel.add(this.nextButton);
    return navigationPanel;
  }

  /**
   * After user selects the number of players, this method creates or updates
   * the corresponding player scorecards.
   */
  private void refreshPlayerScoreCards() {
    // First, count how many scorecards do we have.
    List<PlayerScoreCard> scoreCards = new ArrayList<>();
    for (DataCard dataCard : this.dataCards) {
      if (dataCard instanceof PlayerScoreCard) {
        scoreCards.add((PlayerScoreCard) dataCard);
      }
    }
    // Check if this is a repeated navigation and no cards should be updated.
    if (scoreCards.size() != this.getNumberOfPlayers()) {
      // Remove old UI and state if it has been initialized.
      List<DataCard> removeCards = new ArrayList<>();
      for (DataCard dataCard : this.dataCards) {
        if (dataCard instanceof PlayerScoreCard || dataCard instanceof FinalScoresCard) {
          this.dataCardsPanel.remove(dataCard);
          removeCards.add(dataCard);
        }
      }
      this.dataCards.removeAll(removeCards);

      // Now, create the new cards.
      for (Place place : Place.getPlaces(this.getNumberOfPlayers())) {
        String name = this.playerNamesCard.getPlayerName(place);
        PlayerScoreCard playerScoreCard = new PlayerScoreCard(this, name, place);
        this.dataCardsPanel.add(playerScoreCard, playerScoreCard.getId());
        this.dataCards.add(playerScoreCard);
      }
      FinalScoresCard finalCard = new FinalScoresCard();
      this.dataCardsPanel.add(finalCard, finalCard.getId());
      this.dataCards.add(finalCard);
    }
  }

  /**
   * Stores player names mapped to their place.
   */
  private void storePlayerNames() {
    this.playerNamesMap.clear();
    for (Place place : Place.getPlaces(this.getNumberOfPlayers())) {
      this.playerNamesMap.put(place, this.playerNamesCard.getPlayerName(place));
    }
  }
}
