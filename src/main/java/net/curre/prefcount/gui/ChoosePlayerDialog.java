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

import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import javax.swing.ButtonGroup;
import javax.swing.border.EmptyBorder;

import net.curre.prefcount.util.LocaleExt;
import net.curre.prefcount.gui.type.Place;
import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.bean.GameResultBean;
import net.curre.prefcount.bean.PlayerStatistics;

import java.util.ResourceBundle;
import java.util.Map;
import java.awt.Frame;
import java.awt.Dialog;
import java.awt.Container;
import java.awt.BorderLayout;

import info.clearthought.layout.TableLayoutConstraints;
import info.clearthought.layout.TableLayout;
import net.curre.prefcount.util.PlatformType;

/**
 * Object of this class represents a modal dialog
 * to request a player choice.
 * <p/>
 * Created date: Oct 14, 2008
 *
 * @author Yevgeny Nyden
 */
public class ChoosePlayerDialog extends JDialog {
  public ChoosePlayerDialog(Frame owner) {
    super(owner);

		if (PlatformType.isMacOs()) {
			// Remove application name for the frame panel.
			this.getRootPane().putClientProperty("apple.awt.windowTitleVisible", false);
		}

    initComponents();
  }

  /**
   * Creates a new <code>ChoosePlayerDialog</code> object.
   *
   * @param owner owner/parent dialog.
   */
  public ChoosePlayerDialog(Dialog owner) {
		super(owner);
		initComponents();
	}

  /** Initializes components. */
  private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		ResourceBundle bundle = ResourceBundle.getBundle("default");
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    JPanel dialogPane = new JPanel();
		JPanel contentPanel = new JPanel();
		playerEastButton = new JRadioButton();
		playerSouthButton = new JRadioButton();
		playerWestButton = new JRadioButton();
		playerNorthButton = new JRadioButton();
		JPanel buttonBar = new JPanel();
		JButton okButton = PrefCountRegistry.getInstance().getMenuItemsBean().createJButtonForChoosePlayerDialog(dialogPane);
		JPanel headerPanel = new JPanel();
		JLabel headerMsg = new JLabel();

		//======== this ========
		setAlwaysOnTop(true);
		setModal(true);
		setTitle(bundle.getString("pref.dialog.choosePlayer.title"));
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== dialogPane ========
		{
			dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
			dialogPane.setLayout(new BorderLayout());

			//======== contentPanel ========
			{
				contentPanel.setLayout(new TableLayout(new double[][] {
					{0.05, TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL, 0.05},
					{TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED}}));
				((TableLayout)contentPanel.getLayout()).setHGap(5);
				((TableLayout)contentPanel.getLayout()).setVGap(5);

				//---- playerEastButton ----
				playerEastButton.setSelected(true);
				playerEastButton.setText(getPlayerString(Place.EAST));
				contentPanel.add(playerEastButton, new TableLayoutConstraints(2, 0, 2, 0, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

				//---- playerSouthButton ----
				playerSouthButton.setText(null);
				playerSouthButton.setText(getPlayerString(Place.SOUTH));
				contentPanel.add(playerSouthButton, new TableLayoutConstraints(2, 1, 2, 1, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

				//---- playerWestButton ----
				playerWestButton.setText(null);
				playerWestButton.setText(getPlayerString(Place.WEST));
				contentPanel.add(playerWestButton, new TableLayoutConstraints(2, 2, 2, 2, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

				//---- playerNorthButton ----
				playerNorthButton.setText(null);
				String northName = getPlayerString(Place.NORTH);
				if (northName == null) {
				  playerNorthButton.setVisible(false);
				} else {
				  playerNorthButton.setText(northName);
				}
				contentPanel.add(playerNorthButton, new TableLayoutConstraints(2, 3, 2, 3, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);

			//======== buttonBar ========
			{
				buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
				buttonBar.setLayout(new TableLayout(new double[][] {
					{TableLayout.FILL, TableLayout.PREFERRED},
					{TableLayout.PREFERRED}}));
				((TableLayout)buttonBar.getLayout()).setHGap(5);
				((TableLayout)buttonBar.getLayout()).setVGap(5);
				buttonBar.add(okButton, new TableLayoutConstraints(1, 0, 1, 0, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);

			//======== headerPanel ========
			{
				headerPanel.setLayout(new TableLayout(new double[][] {
					{20, TableLayout.PREFERRED, 20},
					{TableLayout.FILL, 10}}));
				((TableLayout)headerPanel.getLayout()).setHGap(5);
				((TableLayout)headerPanel.getLayout()).setVGap(5);

				//---- headerMsg ----
				headerMsg.setText(bundle.getString("pref.dialog.choosePlayer.header"));
				headerMsg.setBackground(null);
				headerMsg.setFocusable(false);
				headerMsg.setRequestFocusEnabled(false);
				headerMsg.setBorder(null);
				headerPanel.add(headerMsg, new TableLayoutConstraints(1, 0, 1, 0, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
			}
			dialogPane.add(headerPanel, BorderLayout.NORTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());

		//---- buttonGroup ----
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(playerEastButton);
		buttonGroup.add(playerSouthButton);
		buttonGroup.add(playerWestButton);
		buttonGroup.add(playerNorthButton);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

  private JRadioButton playerEastButton;
	private JRadioButton playerSouthButton;
	private JRadioButton playerWestButton;
	private JRadioButton playerNorthButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

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
}
