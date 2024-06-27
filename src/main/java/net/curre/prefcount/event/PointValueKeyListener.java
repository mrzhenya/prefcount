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

package net.curre.prefcount.event;

import net.curre.prefcount.gui.game.ScoreTableModel;

import javax.swing.JTable;
import javax.swing.JTextField;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Point value text input key listener to update the scores table.
 *
 * @author Yevgeny Nyden
 */
public class PointValueKeyListener implements KeyListener {

  private final JTextField pointCost;
  private final JTable scoreTable;

  public PointValueKeyListener(JTextField pointCost, JTable scoreTable) {
    this.pointCost = pointCost;
    this.scoreTable = scoreTable;
  }

  /** Does nothing. */
  public void keyTyped(KeyEvent event) {
  }

  /** Does nothing. */
  public void keyPressed(KeyEvent event) {
  }

  /** {@inheritDoc} */
  public void keyReleased(KeyEvent event) {
    try {
      String costStr = this.pointCost.getText().trim();
      double cost = 0.0;
      if (!costStr.isEmpty() &&
          !costStr.equals("0.") && !costStr.equals("0,")) {
        cost = Double.parseDouble(costStr);
      }
      ScoreTableModel tableModel = (ScoreTableModel) scoreTable.getModel();
      tableModel.refreshWinnings(cost);
    } catch (NumberFormatException e) {
      // Ignore the errors
    }
  }
}
