/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as 
 * published by the Free Software Foundation;
 */

package net.curre.prefcount.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.Action;

import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.bean.GameResultBean;
import net.curre.prefcount.bean.Settings;
import net.curre.prefcount.gui.MainWindow;
import net.curre.prefcount.gui.PlayerDialogBaseFrame;
import net.curre.prefcount.gui.ChoosePlayerDialog;
import net.curre.prefcount.gui.menu.MenuItemsBean;
import net.curre.prefcount.gui.type.WindowComponent;
import static net.curre.prefcount.gui.type.WindowComponent.DIALOG_FORWARD;
import static net.curre.prefcount.gui.type.WindowComponent.DIVISIBLE_BY_N;
import static net.curre.prefcount.gui.type.WindowComponent.LENINGRAD;
import static net.curre.prefcount.gui.type.WindowComponent.MAIN_3_PLAYERS;
import static net.curre.prefcount.gui.type.WindowComponent.MAIN_4_PLAYERS;
import static net.curre.prefcount.gui.type.WindowComponent.SAVE_SETTINGS_ACTION;
import net.curre.prefcount.service.MainService;
import net.curre.prefcount.service.ResultService;
import net.curre.prefcount.service.UiService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is the main controller for handling
 * event actions.
 * <p/>
 * Created date: Apr 15, 2008
 *
 * @author Yevgeny Nyden
 */
public class MainController implements ActionListener {

  /** Private class logger. */
  private static final Logger logger = LogManager.getLogger(MainController.class.getName());

  /** Reference to the main window frame. */
  private final MainWindow mainWindow;

  /** Reference to the player dialog frame. */
  private final PlayerDialogBaseFrame playerDialog;

  /**
   * Constructs a new <code>MainController</code> object.
   *
   * @param mainWindow   reference to the main window.
   * @param playerDialog reference to the player dialog frame.
   */
  public MainController(MainWindow mainWindow, PlayerDialogBaseFrame playerDialog) {
    this.mainWindow = mainWindow;
    this.playerDialog = playerDialog;
  }

  /** {@inheritDoc} */
  public void actionPerformed(ActionEvent event) {
    logger.info("Handling action");
    selectMenuBarOptions((WindowComponent) event.getSource());
  }

  /**
   * Creates and returns an action object for the given
   * component (represented by a <code>WindowComponent</code> enum).
   *
   * @param compEnum component enum for which an action needs to be created.
   * @return a new action object.
   */
  public static Action getActionForComponent(final WindowComponent compEnum) {
    return new AbstractAction() {
      public void actionPerformed(ActionEvent event) {
        event.setSource(compEnum);
        PrefCountRegistry.getInstance().getMainController().actionPerformed(event);
      }
    };
  }

  /**
   * Performs an action according to the selected (passed)
   * component enum.
   *
   * @param itemEnum selected item.
   */
  private void selectMenuBarOptions(WindowComponent itemEnum) {
    PrefCountRegistry registry = PrefCountRegistry.getInstance();
    Settings settings = registry.getSettingsService().getSettings();
    GameResultBean resultBean = registry.getGameResultBean();

    String itemName = itemEnum.name();

    switch (itemEnum) {
      case LENINGRAD:
      case SOCHINKA:
        settings.setPrefType(itemName);
        resultBean.setLeningradka(LENINGRAD.equals(itemEnum));
        recomputeScoresHelper();
        break;

      case MAIN_3_PLAYERS:
      case MAIN_4_PLAYERS:
        numberOfPlayersHelper(itemEnum);
        break;

      case DIVISIBLE_IGNORE:
      case DIVISIBLE_BY_N:
        settings.setDivisibleBy(itemName);
        resultBean.setMountDivisibleByN(DIVISIBLE_BY_N.equals(itemEnum));
        recomputeScoresHelper();
        break;

      case DIALOG_BACK:
      case DIALOG_FORWARD:
        this.playerDialog.nextQuestionEventHelper(DIALOG_FORWARD.equals(itemEnum));
        break;

      case DIALOG_FORWARD2:
        ChoosePlayerDialog dialog = registry.getChoosePlayerDialog();
        resultBean.setDivisibleByNPlayer(dialog.getSelectedPlayer());
        registry.disposeChoosePlayerDialog();
        break;

      case SAVE_SETTINGS_ACTION:
      case RESET_SETTINGS_ACTION:
        saveResetActionHelper(SAVE_SETTINGS_ACTION.equals(itemEnum));
        break;

      case PRINT_SCORES_ACTION:
        MainService.doPrint();
        break;

      case QUIT_ACTION:
        MainService.quitApp();
        break;

      case ABOUT_ACTION:
      case ABOUT_ACTION2:
        PrefCountRegistry.getInstance().getMainWindow().showAboutInfo();
        break;

      case HELP_COUNT_ACTION:
      case HELP_COUNT_ACTION2:
      case HELP_PREF_ACTION:
      case HELP_PREF_ACTION2:
      case HELP_COMMON_ACTION:
      case HELP_COMMON_ACTION2:
        MainService.doShowHelp(itemEnum);
        break;

      case PRINT_TEMPLATE3_ACTION:
        MainService.doPrintTemplate(3);
        break;

      case PRINT_TEMPLATE4_ACTION:
        MainService.doPrintTemplate(4);
        break;

      default:
        // ignore for now...
    }
  }

  /**
   * Method to recompute the final scores and refresh the UI
   * (only when the final scores were ready).
   */
  private void recomputeScoresHelper() {
    GameResultBean resultBean = PrefCountRegistry.getInstance().getGameResultBean();
    if (resultBean.isFinalScoresReady()) {
      ResultService.generateFinalResults(resultBean);
      this.playerDialog.refreshTable();
      this.mainWindow.repaint();
    }
  }

  /**
   * Assists with the changing number of players actions.
   *
   * @param itemEnum enum that represents the required action
   *                 (MAIN_3_PLAYERS or MAIN_4_PLAYERS).
   */
  private void numberOfPlayersHelper(WindowComponent itemEnum) {
    // let's first see if the new value is different
    PrefCountRegistry registry = PrefCountRegistry.getInstance();
    Settings settings = registry.getSettingsService().getSettings();
    MenuItemsBean menuItemsBean = PrefCountRegistry.getInstance().getMenuItemsBean();
    final String currNumber = settings.getPlayersNumber();
    if (!itemEnum.name().equals(currNumber)) {

      // need to check if we are not erasing any data
      if (this.playerDialog.isSomeDataEntered()) {
        if (UiService.displayOkCancelMessage("pref.dialog.warn.resetData",
                                             "pref.dialog.buttons.yes",
                                             "pref.dialog.buttons.cancel")) {
          settings.setPlayersNumber(itemEnum.name());
          this.mainWindow.initializeNumberOfPlayers();

        } else {
          // canceling action
          if (MAIN_3_PLAYERS.equals(itemEnum)) {
            menuItemsBean.setSelected(MAIN_4_PLAYERS, true);
          } else {
            menuItemsBean.setSelected(MAIN_3_PLAYERS, true);
          }
        }
      } else {
        settings.setPlayersNumber(itemEnum.name());
        this.mainWindow.initializeNumberOfPlayers();
      }
    }
  }

  /**
   * Assists with the save/reset settings actions.
   *
   * @param isSave true if this is a save action; false - reset action.
   */
  private void saveResetActionHelper(boolean isSave) {
    // TODO - not sure, deprecate reset? what is save? why not automatically save?
/*
    try {
      if (isSave) {
        SettingsService.saveSettings();
      } else {

        // warn the user first tha we need to quit
        if (Utilities.displayOkCancelMessage("pref.settings.restart",
                                             "pref.dialog.buttons.yes",
                                             "pref.dialog.buttons.no")) {
          SettingsService.resetSettings();
          MainService.quitApp();
        }
      }
    } catch (ServiceException e) {
      log.log(Level.WARNING, "Unable to perform save/reset action!", e);
    }
*/
  }
}
