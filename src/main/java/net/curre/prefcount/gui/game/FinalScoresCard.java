/*
 * Copyright 2024 Yevgeny Nyden
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.curre.prefcount.gui.game;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstraints;
import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.bean.GameResultBean;
import net.curre.prefcount.event.PointValueKeyListener;
import net.curre.prefcount.service.ResultService;
import net.curre.prefcount.util.LocaleExt;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.validation.constraints.Null;
import java.awt.BorderLayout;

/**
 * The final card that displays the game scores.
 *
 * @author Yevgeny Nyden
 */
public class FinalScoresCard extends DataCard {

  /** Reference to the point cost text field. */
  private final JTextField pointCost;

  /** Reference to the JTable with players' scores. */
  private final JTable scoreTable;

  /** Ctor. */
  public FinalScoresCard() {
    this.setLayout(new BorderLayout(10, 10));

    JPanel scorePanel = new JPanel();
    TableLayout layout = new TableLayout(new double[][]{
        {5d, TableLayout.FILL, 5d},
        {19d, TableLayout.PREFERRED, 15d, TableLayout.PREFERRED}});
    scorePanel.setLayout(layout);
    scorePanel.setOpaque(false);
    layout.setVGap(5);
    JPanel emptyPanel = new JPanel(null);
    emptyPanel.setOpaque(false);
    scorePanel.add(emptyPanel, new TableLayoutConstraints(
        0, 0, 2, 0, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

    JPanel tablePanel = new JPanel(new BorderLayout());
    tablePanel.setOpaque(false);
    this.scoreTable = new JTable(new ScoreTableModel());
    this.scoreTable.setOpaque(false);
    JTableHeader header = this.scoreTable.getTableHeader();
    tablePanel.add(header, BorderLayout.NORTH);
    tablePanel.add(this.scoreTable, BorderLayout.CENTER);

    scorePanel.add(tablePanel, new TableLayoutConstraints(
        1, 1, 1, 1, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
    this.add(scorePanel, BorderLayout.CENTER);

    emptyPanel = new JPanel(null);
    emptyPanel.setOpaque(false);
    scorePanel.add(emptyPanel, new TableLayoutConstraints(
        0, 2, 2, 2, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

    // Point cost input UI.
    this.pointCost = new JTextField();
    JPanel costPanel = this.createPointCostPanel();
    scorePanel.add(costPanel, new TableLayoutConstraints(
        1, 3, 1, 3, TableLayoutConstraints.CENTER, TableLayoutConstraints.FULL));
  }

  /** {@inheritDoc} */
  @Override
  public String getTitle() {
    return LocaleExt.getString("pref.dialog.totalsMessage");
  }

  /** {@inheritDoc} */
  @Override
  public void focusFirstField() {
    this.pointCost.requestFocus();
  }

  /** {@inheritDoc} */
  @Override
  public @Null String validateFields(boolean isForward) {
    return null;
  }

  /** Does nothing. */
  @Override
  public void doOnEntry() {
    PrefCountRegistry registry = PrefCountRegistry.getInstance();
    GameResultBean resultBean = registry.getGameResultBean();
    ResultService.generateFinalResults(resultBean);
    registry.getMainWindow().repaint();
    registry.getMainWindow().enablePrintingScores(true);
    refreshTable();


  }

  /** @inheritDoc */
  @Override
  public void doOnForwardLeave() {
  }

  /** @inheritDoc */
  @Override
  public void doOnBackwardLeave() {
    PrefCountRegistry registry = PrefCountRegistry.getInstance();
    GameResultBean resultBean = registry.getGameResultBean();
    resultBean.setFinalScoresReady(false);
    registry.getMainWindow().repaint();
    registry.getMainWindow().enablePrintingScores(false);
  }

  /**
   * Determines if user entered some names data.
   *
   * @return false always
   */
  @Override
  public boolean isSomeDataEntered() {
    return false;
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

  /**
   * Creates and initializes point cost UI.
   *
   * @return panel with the point cost input field.
   */
  private JPanel createPointCostPanel() {
    JPanel costPanel = new JPanel();
    costPanel.setOpaque(false);
    JLabel pointCostLabel = new JLabel();
    pointCostLabel.setOpaque(false);
    pointCostLabel.setText(LocaleExt.getString("pref.dialog.pointCost"));
    pointCostLabel.setHorizontalAlignment(SwingConstants.RIGHT);
    costPanel.add(pointCostLabel);
    this.pointCost.setHorizontalAlignment(SwingConstants.RIGHT);
    this.pointCost.setColumns(4);
    this.pointCost.setOpaque(false);
    costPanel.add(this.pointCost);

    this.pointCost.addKeyListener(new PointValueKeyListener(this.pointCost, this.scoreTable));
    return costPanel;
  }
}
