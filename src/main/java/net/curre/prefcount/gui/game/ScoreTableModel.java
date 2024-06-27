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

import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.bean.GameResultBean;
import net.curre.prefcount.bean.PlayerStatistics;
import net.curre.prefcount.gui.type.Place;
import net.curre.prefcount.util.LocaleExt;

import javax.swing.table.AbstractTableModel;
import java.util.Map;

/**
 * Represents the model for the players' score table.
 *
 * @author Yevgeny Nyden
 */
public class ScoreTableModel extends AbstractTableModel {

  /** Array of column resource keys. */
  private final static String[] COLUMN_NAMES = {
      null,
      "pref.dialog.table.player",
      "pref.dialog.table.score",
      "pref.dialog.table.money"
  };

  /** Cost of one point. */
  private double costOfOnePoint = 0.0;

  /** Refreshes table data and structure. */
  public void refreshTable() {
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
    return COLUMN_NAMES.length;
  }

  /**
   * Gets the row count.
   *
   * @return the number of players.
   */
  public int getRowCount() {
    GameResultBean resultBean = PrefCountRegistry.getInstance().getGameResultBean();
    if (resultBean == null) {
      return 0;
    }
    return resultBean.getNumberOfPlayers();
  }

  /** {@inheritDoc} */
  @Override
  public String getColumnName(int col) {
    String key = COLUMN_NAMES[col];
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
    GameResultBean resultBean = PrefCountRegistry.getInstance().getGameResultBean();
    if (resultBean != null && resultBean.isFinalScoresReady()) {
      Map<Place, PlayerStatistics> stats = resultBean.getPlayerStats();
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
    GameResultBean resultBean = PrefCountRegistry.getInstance().getGameResultBean();
    if (resultBean == null) {
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
