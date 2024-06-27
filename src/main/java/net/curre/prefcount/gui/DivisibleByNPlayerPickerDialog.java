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

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import net.curre.prefcount.event.ClickAndKeyAction;
import net.curre.prefcount.event.ClosingWindowListener;
import net.curre.prefcount.util.LocaleExt;
import net.curre.prefcount.gui.type.Place;
import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.bean.GameResultBean;
import net.curre.prefcount.bean.PlayerStatistics;

import java.util.ResourceBundle;
import java.util.Map;
import java.awt.Container;
import java.awt.BorderLayout;

import info.clearthought.layout.TableLayoutConstraints;
import info.clearthought.layout.TableLayout;
import net.curre.prefcount.util.PlatformType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A modal dialog to assist with selecting a user for the
 * mount divisibility setting.
 * <p/>
 * Created date: Oct 14, 2008
 *
 * @author Yevgeny Nyden
 */
public class DivisibleByNPlayerPickerDialog extends JDialog {

	/** Private class logger. */
	private static final Logger logger = LogManager.getLogger(DivisibleByNPlayerPickerDialog.class.getName());

	private JRadioButton playerEastButton;
	private JRadioButton playerSouthButton;
	private JRadioButton playerWestButton;
	private JRadioButton playerNorthButton;

	/** Ctor. */
	public DivisibleByNPlayerPickerDialog() {
		logger.info("Creating the DivisibleByNPlayerPickerDialog");

		if (PlatformType.isMacOs()) {
			// Remove application name for the frame panel.
			this.getRootPane().putClientProperty("apple.awt.windowTitleVisible", false);
		}

		setAlwaysOnTop(true);
		setModal(true);
		ResourceBundle bundle = ResourceBundle.getBundle("default");
		setTitle(bundle.getString("pref.dialog.choosePlayer.title"));

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new ClosingWindowListener(this::saveSettingsAndCloseDialog));

    initComponents();
  }

	/**
	 * Gets selected player place.
	 *
	 * @return selected player place.
	 */
	public Place getSelectedPlayer() {
		if (this.playerEastButton.isSelected()) {
			return Place.EAST;

		} else if (this.playerSouthButton.isSelected()) {
			return Place.SOUTH;

		} else if (this.playerWestButton.isSelected()) {
			return Place.WEST;

		} else if (this.playerNorthButton.isSelected()) {
			return Place.NORTH;

		} else {
			return Place.EAST; // setting default player anyway
		}
	}

	/** Initializes components. */
  private void initComponents() {
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		JPanel dialogPane = new JPanel();
		dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
		dialogPane.setLayout(new BorderLayout());

		// The header.
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new TableLayout(new double[][] {
				{20, TableLayout.PREFERRED, 20}, // rows
				{TableLayout.FILL, 10}})); // columns
		((TableLayout) headerPanel.getLayout()).setHGap(5);
		((TableLayout) headerPanel.getLayout()).setVGap(5);

		JLabel headerLabel = new JLabel();
		headerLabel.setText(LocaleExt.getString("pref.dialog.choosePlayer.header"));
		headerLabel.setBackground(null);
		headerLabel.setFocusable(false);
		headerLabel.setRequestFocusEnabled(false);
		headerLabel.setBorder(null);
		headerPanel.add(headerLabel, new TableLayoutConstraints(
				1, 0, 1, 0, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
		dialogPane.add(headerPanel, BorderLayout.NORTH);


		// Main content with player choices.
		JPanel contentPanel = createPlayerChoicesPanel();
		dialogPane.add(contentPanel, BorderLayout.CENTER);

		// ******* Panel with the default action button.
		dialogPane.add(createButtonBar(), BorderLayout.SOUTH);

		contentPane.add(dialogPane, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(PrefCountRegistry.getInstance().getMainWindow());
	}

	/**
	 * Creates and initializes the panel with player choices.
	 *
	 * @return the new panel
	 */
	private JPanel createPlayerChoicesPanel() {
		JPanel contentPanel = new JPanel();

		this.playerEastButton = new JRadioButton();
		this.playerSouthButton = new JRadioButton();
		this.playerWestButton = new JRadioButton();
		this.playerNorthButton = new JRadioButton();

		contentPanel.setLayout(new TableLayout(new double[][] {
				{0.05, TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL, 0.05},
				{TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED}}));
		((TableLayout) contentPanel.getLayout()).setHGap(5);
		((TableLayout) contentPanel.getLayout()).setVGap(5);

		// East.
		this.playerEastButton.setSelected(true);
		this.playerEastButton.setText(getPlayerString(Place.EAST));
		contentPanel.add(this.playerEastButton, new TableLayoutConstraints(
				2, 0, 2, 0, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

		// South.
		this.playerSouthButton.setText(null);
		this.playerSouthButton.setText(getPlayerString(Place.SOUTH));
			contentPanel.add(this.playerSouthButton, new TableLayoutConstraints(
					2, 1, 2, 1, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

		// West.
		this.playerWestButton.setText(null);
		this.playerWestButton.setText(getPlayerString(Place.WEST));
		contentPanel.add(this.playerWestButton, new TableLayoutConstraints(
				2, 2, 2, 2, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

		// North (only when it's a 4 player game).
		this.playerNorthButton.setText(null);
		String northName = getPlayerString(Place.NORTH);
		if (northName == null) {
			this.playerNorthButton.setVisible(false);
		} else {
			this.playerNorthButton.setText(northName);
		}
		contentPanel.add(this.playerNorthButton, new TableLayoutConstraints(
				2, 3, 2, 3, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(this.playerEastButton);
		buttonGroup.add(this.playerSouthButton);
		buttonGroup.add(this.playerWestButton);
		buttonGroup.add(this.playerNorthButton);

		// Set the selected button.
		Place selectedPlayer = PrefCountRegistry.getInstance().getGameResultBean().getDivisibleByNPlayer();
		if (selectedPlayer != null) {
			switch (selectedPlayer) {
				case EAST:
					this.playerEastButton.setSelected(true);
					break;
				case SOUTH:
					this.playerSouthButton.setSelected(true);
					break;
				case WEST:
					this.playerWestButton.setSelected(true);
					break;
				case NORTH:
					this.playerNorthButton.setSelected(true);
					break;
			}
		}

		return contentPanel;
	}

	/** Creates the action button panel. */
	private JPanel createButtonBar() {
		JPanel buttonBar = new JPanel();
		buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
		buttonBar.setLayout(new TableLayout(new double[][] {
				{TableLayout.FILL, TableLayout.PREFERRED},
				{TableLayout.PREFERRED}}));
		((TableLayout) buttonBar.getLayout()).setHGap(5);
		((TableLayout) buttonBar.getLayout()).setVGap(5);

		JButton okButton = new JButton(LocaleExt.getString("pref.dialog.buttons.ok"));
		ClickAndKeyAction.createAndAddAction(okButton, this::saveSettingsAndCloseDialog);
		buttonBar.add(okButton, new TableLayoutConstraints(
				1, 0, 1, 0, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
		return buttonBar;
	}

	/**
	 * Method to create a radio button title.
	 *
	 * @param place player's place.
	 * @return player's radio button text or null if no such player exists in the game.
	 */
  private String getPlayerString(Place place) {
    GameResultBean rBean = PrefCountRegistry.getInstance().getGameResultBean();
    Map<Place, PlayerStatistics> statsMap = rBean.getPlayerStats();
    PlayerStatistics stats = statsMap.get(place);
    if (stats == null) {
      return null;
    }
    return LocaleExt.getString(place.longKey, ": " + stats.getPlayerName());
  }

	/**
	 * Saves the mount divisibility setting and closes the dialog.
	 */
	private void saveSettingsAndCloseDialog() {
		GameResultBean resultBean = PrefCountRegistry.getInstance().getGameResultBean();
		Place playerPlace = this.getSelectedPlayer();
		logger.info("Storing player {} for mount divisibility", playerPlace);
		resultBean.setMountDivisibleByN(playerPlace);
		logger.info("Closing the DivisibleByNPlayerPickerDialog");
		this.setVisible(false);
	}
}
