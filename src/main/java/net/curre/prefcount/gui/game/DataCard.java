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

import javax.swing.JPanel;
import javax.validation.constraints.Null;

/**
 * This interface represents a set of common
 * methods for player dialog main/inner panel.
 * <p/>
 * Created date: May 17, 2007
 *
 * @author Yevgeny Nyden
 */
public abstract class DataCard extends JPanel {

  /**
   * Gets the localized title of this card.
   *
   * @return the card's localized title.
   */
  abstract String getTitle();

  /**
   * Gets the unique id of this card.
   *
   * @return the card unique string id.
   */
  public String getId() {
    return this.getClass().getSimpleName();
  }

  /** Requests focus to be sent to the first input field. */
  public abstract void focusFirstField();

  /**
   * Validates input fields of the panel before a forward or a backward navigation action.
   *
   * @param isForward true if this is a forward navigation; false if a backwards one.
   * @return null if all input fields are valid; a string error message if at least one field is invalid.
   */
  public abstract @Null String validateFields(boolean isForward);

  /**
   * Performs necessary activity (if there is any) when switching to this panel (entering it).
   */
  public abstract void doOnEntry();

  /**
   * Performs necessary activity (if there is any) when switching from this
   * panel (leaving it) during forward navigation (i.e. when user presses the Next button).
   */
  public abstract void doOnForwardLeave();

  /**
   * Performs necessary activity (if there is any) when switching from this
   * panel (leaving it) during backward navigation (i.e. when user presses the Back button).
   */
  public abstract void doOnBackwardLeave();

  /**
   * Determines if user entered some data.
   *
   * @return true if any data is entered; false if otherwise.
   */
  public abstract boolean isSomeDataEntered();
}
