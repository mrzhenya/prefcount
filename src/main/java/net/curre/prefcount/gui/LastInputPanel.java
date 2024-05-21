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

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.bean.GameResultBean;
import net.curre.prefcount.bean.PlayerStatistics;
import net.curre.prefcount.gui.aa.AAJLabel;
import net.curre.prefcount.gui.aa.AAJPanel;
import net.curre.prefcount.gui.aa.AAJTextField;
import net.curre.prefcount.gui.type.Place;
import net.curre.prefcount.service.ResultService;
import net.curre.prefcount.service.UiService;
import net.curre.prefcount.util.LocaleExt;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstraints;
import org.apache.commons.lang3.StringUtils;

/**
 * Object of this class represents the last input panel,
 * where point cost is entered and the final scores are
 * computed (initiated).
 * <p/>
 * Created date: Apr 8, 2007
 *
 * @author Yevgeny Nyden
 */
public class LastInputPanel extends DialogInnerPanel {

  /** Reference to the point cost text field. */
  private final JTextField pointCost;

  /** Reference to the parent dialog frame. */
  private final PlayerDialogBaseFrame dialogWindow;

  /** Reference to the JTable with players' scores. */
  private final JTable scoreTable;

  /** Reference to the panel that holds the table and it's header. */
  JPanel tablePanel;

  /**
   * Constructor.
   *
   * @param dialogWindow Reference to the dialog window/frame.
   */
  public LastInputPanel(final PlayerDialogBaseFrame dialogWindow) {
    super("pref.dialog.totalsMessage", PanelPosition.LAST);
    this.dialogWindow = dialogWindow;
    this.setLayout(new BorderLayout(10, 10));

    // ======== final score panel ========
    JPanel scorePanel = new AAJPanel();
    TableLayout layout = new TableLayout(new double[][]{
        {5d, TableLayout.FILL, 5d},
        {19d, TableLayout.PREFERRED, 15d, TableLayout.PREFERRED}});
    scorePanel.setLayout(layout);
    scorePanel.setOpaque(false);
    layout.setVGap(5);
    JPanel emptyPanel = new JPanel(null);
    emptyPanel.setOpaque(false);
    scorePanel.add(emptyPanel,
                   new TableLayoutConstraints(0, 0, 2, 0,
                                              TableLayoutConstraints.FULL,
                                              TableLayoutConstraints.FULL));

    this.tablePanel = new JPanel(new BorderLayout());
    this.tablePanel.setOpaque(false);
    this.scoreTable = new JTable(new ScoreTableModel());
    this.scoreTable.setOpaque(false);
    JTableHeader header = this.scoreTable.getTableHeader();
    this.tablePanel.add(header, BorderLayout.NORTH);
    this.tablePanel.add(this.scoreTable, BorderLayout.CENTER);


    scorePanel.add(this.tablePanel,
                   new TableLayoutConstraints(1, 1, 1, 1,
                                              TableLayoutConstraints.FULL,
                                              TableLayoutConstraints.FULL));
    this.add(scorePanel, BorderLayout.CENTER);

    emptyPanel = new JPanel(null);
    emptyPanel.setOpaque(false);
    scorePanel.add(emptyPanel,
                   new TableLayoutConstraints(0, 2, 2, 2,
                                              TableLayoutConstraints.FULL,
                                              TableLayoutConstraints.FULL));

    //---- point cost label ----
    JPanel costPanel = new JPanel();
    costPanel.setOpaque(false);
    JLabel pointCostLabel = new AAJLabel();
    pointCostLabel.setOpaque(false);
    pointCostLabel.setText(LocaleExt.getString("pref.dialog.pointCost"));
    pointCostLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    costPanel.add(pointCostLabel);
    this.pointCost = new AAJTextField();
    this.pointCost.setHorizontalAlignment(SwingConstants.RIGHT);
    this.pointCost.setColumns(4);
    this.pointCost.setOpaque(false);
    costPanel.add(this.pointCost);
    scorePanel.add(costPanel,
                   new TableLayoutConstraints(1, 3, 1, 3,
                                              TableLayoutConstraints.CENTER,
                                              TableLayoutConstraints.FULL));
    LocaleExt.registerComponent(pointCostLabel, "pref.dialog.pointCost");
    this.localeSensitiveComps.add(pointCostLabel);

    this.pointCost.addKeyListener(new KeyListener() {

      /** Does nothing. */
      public void keyTyped(KeyEvent event) {
      }

      /** Does nothing. */
      public void keyPressed(KeyEvent event) {
      }

      /** {@inheritDoc} */
      public void keyReleased(KeyEvent event) {
        try {
          String costStr = pointCost.getText().trim();
          double cost = 0.0;
          if (!costStr.isEmpty() &&
              !costStr.equals("0.") && !costStr.equals("0,")) {
            cost = Double.parseDouble(costStr);
          }
          dialogWindow.toggleErrorField(null);
          ScoreTableModel tableModel = (ScoreTableModel) scoreTable.getModel();
          tableModel.refreshWinnings(cost);
        } catch (NumberFormatException e) {
          dialogWindow.toggleErrorField("pref.dialog.errorLabel.int");
        }
      }
    });
  }

  /** {@inheritDoc} */
  @Override
  public void focusFirstInputField() {
    this.pointCost.requestFocus();
  }

  /**
   * Validates point cost input field.
   *
   * @return True if point cost input filed is valid; false otherwise.
   */
  @Override
  public boolean validateFields() {
    // blank point field is considered to be valid
    if (StringUtils.isBlank(this.pointCost.getText())) {
      this.dialogWindow.toggleErrorField(null);
      return true;
    }
    // if there is text, it must be a double and be >= 0
    if (UiService.validateTextField(this.pointCost, UiService.FieldType.DOUBLE)) {
      double cost = Double.parseDouble(this.pointCost.getText().trim());
      if (cost < 0) {
        this.dialogWindow.toggleErrorField("pref.dialog.errorLabel.negPointCost");
      } else {
        this.dialogWindow.toggleErrorField(null);
        return true;
      }
    } else {
      this.dialogWindow.toggleErrorField("pref.dialog.errorLabel.int");
    }

    this.pointCost.requestFocus();
    return false;
  }

  /** Does nothing. */
  @Override
  public void doOnEntry() {
    MainWindow mainWindow = PrefCountRegistry.getInstance().getMainWindow();
    GameResultBean resultBean = PrefCountRegistry.getInstance().getGameResultBean();
    ResultService.generateFinalResults(resultBean);
    mainWindow.repaint();
    refreshTable();
  }

  /**
   * Clearing players game data in the results bean.
   * <p/>
   * {@inheritDoc}
   */
  @Override
  public void doOnLeave() {
    GameResultBean resultBean = PrefCountRegistry.getInstance().getGameResultBean();
    ResultService.clearFinalResults(resultBean);
    refreshTable();
  }

  /**
   * Sets point cost text value.
   *
   * @param value String value to set.
   */
  public void setPointCostText(String value) {
    this.pointCost.setText(value);
  }

  /**
   * Parses and returns the point cost text field value.
   * This method assumes that the field has been validated.
   *
   * @return Point cost text input field value.
   */
  public int getPointCostValue() {
    return UiService.parseIntFromTextField(this.pointCost);
  }

  /**
   * Refreshes the score table - updates the header
   * labels according to the current locale.
   */
  public void refreshTable() {
    ScoreTableModel tableModel = (ScoreTableModel) this.scoreTable.getModel();
    tableModel.refreshTable();

    this.scoreTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    TableColumn col = this.scoreTable.getColumnModel().getColumn(0);
    col.setPreferredWidth(20);
    col = this.scoreTable.getColumnModel().getColumn(1);
    col.setPreferredWidth(75);
    col = this.scoreTable.getColumnModel().getColumn(2);
    col.setPreferredWidth(65);
    col = this.scoreTable.getColumnModel().getColumn(3);
    col.setPreferredWidth(65);
  }

  /** Helper class that represents model for the players' score table. */
  private static class ScoreTableModel extends AbstractTableModel {

    /** Array of column resource keys. */
    private final String[] columnNames = {
        null,
        "pref.dialog.table.player",
        "pref.dialog.table.score",
        "pref.dialog.table.money"
    };

    /** Reference to the game result bean. */
    private GameResultBean resultBean;

    /** Cost of one point. */
    private double costOfOnePoint = 0.0;

    /** Refreshes table data and structure. */
    public void refreshTable() {
      this.resultBean = PrefCountRegistry.getInstance().getGameResultBean();
      super.fireTableDataChanged();
      super.fireTableStructureChanged();
    }

    /**
     * Refreshes winnings column in the main score table.
     *
     * @param cost cost of one point.
     */
    public void refreshWinnings(double cost) {
      this.costOfOnePoint = cost;
      super.fireTableDataChanged();
    }

    /**
     * Gets column count.
     *
     * @return column count.
     */
    public int getColumnCount() {
      return columnNames.length;
    }

    /**
     * Gets the row count.
     *
     * @return the number of players.
     */
    public int getRowCount() {
      if (this.resultBean == null) {
        return 0;
      }
      return resultBean.getNumberOfPlayers();
    }

    /** {@inheritDoc} */
    @Override
    public String getColumnName(int col) {
      String key = this.columnNames[col];
      return key == null ? " " : LocaleExt.getString(key);
    }

    /**
     * Gets value for the given row and the given column.
     *
     * @param row item's row.
     * @param col item's column.
     * @return item's value.
     */
    public Object getValueAt(int row, int col) {
      if (this.resultBean != null && this.resultBean.isFinalScoresReady()) {
        Map<Place, PlayerStatistics> stats = this.resultBean.getPlayerStats();
        Place place = Place.getPlaceForIndex(row);
        PlayerStatistics player = stats.get(place);
        switch (col) {
          case 0:
            return LocaleExt.getString(place.shortKey);
          case 1:
            return player.getPlayerName();
          case 2:
            return player.getFinalScoreInWhists();
          case 3:
            if (this.costOfOnePoint > 0.0) {
              return ((double) player.getFinalScoreInWhists()) * this.costOfOnePoint;
            }
        }
      }
      return 0;
    }

    /** {@inheritDoc} */
    @Override
    public Class<?> getColumnClass(int c) {
      if (this.resultBean == null) {
        return null;
      }
      return getValueAt(0, c).getClass();
    }

    /**
     * This method always returns false.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    public boolean isCellEditable(int row, int col) {
      return false;
    }

    /* This method does nothing. */
    @Override
    public void setValueAt(Object value, int row, int col) {
    }
  }
}
