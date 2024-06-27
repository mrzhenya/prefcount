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

package net.curre.prefcount.event;

import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.bean.GameResultBean;
import net.curre.prefcount.gui.DivisibleByNPlayerPickerDialog;

import javax.swing.JRadioButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Action listener to handle Divisible By N options change.
 *
 * @author Yevgeny Nyden
 */
public class DivisibleByNActionListener implements ActionListener {

  /** Reference to the Divisible By N radio button. */
  private final JRadioButton divisibleByNButton;

  public DivisibleByNActionListener(JRadioButton divisibleByNButton) {
    this.divisibleByNButton = divisibleByNButton;
  }

  /** @inheritDoc */
  @Override
  public void actionPerformed(ActionEvent e) {
    PrefCountRegistry registry = PrefCountRegistry.getInstance();
    GameResultBean resultBean = registry.getGameResultBean();
    boolean divisibleByN = this.divisibleByNButton.isSelected();

    if (divisibleByN) {
      DivisibleByNPlayerPickerDialog dialog = new DivisibleByNPlayerPickerDialog();
      dialog.setVisible(true);
    } else {
      resultBean.setMountDivisibleByN(null);
    }
  }
}
