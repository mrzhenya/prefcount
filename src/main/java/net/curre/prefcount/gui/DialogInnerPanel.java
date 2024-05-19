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

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.curre.prefcount.util.LocaleExt;

/**
 * This interface represents a set of common
 * methods for player dialog main/inner panel.
 * <p/>
 * Created date: May 17, 2007
 *
 * @author Yevgeny Nyden
 */
public abstract class DialogInnerPanel extends JPanel {

  /** Enumeration for inner panel relative position. */
  public enum PanelPosition {

    /** Indicates that this is the first panel. */
    FIRST,
    /** Indicates that this panel is in the middle. */
    MIDDLE,
    /** Indicates that this is the last panel. */
    LAST
  }

  /** Panel relative position (first, middle, last). */
  private final PanelPosition panelPosition;

  /** Header message resource key. */
  private final String headerMessageKey;

  /** Reference to the locale sensitive components. */
  protected Collection<Component> localeSensitiveComps;

  /**
   * Constructor.
   *
   * @param headerKey Panel header message resource key.
   * @param position  Panel relative position.
   */
  public DialogInnerPanel(String headerKey, PanelPosition position) {
    this.panelPosition = position;
    this.headerMessageKey = headerKey;
    this.localeSensitiveComps = new ArrayList<>();
    super.setOpaque(false);
  }

  /**
   * Transfers focus to the next input field or
   * to the first field that failed validation
   * if there was such a field.
   */
  public abstract void focusFirstInputField();

  /**
   * Validates input fields of the panel and returns
   * true if all fields are valid; returns false if
   * at least one field is invalid.
   *
   * @return True if all input fields are valid; false
   *         if at least one field is invalid.
   */
  public abstract boolean validateFields();

  /**
   * Performs necessary activity (if there is any)
   * when switching to this panel (entering it).
   */
  public abstract void doOnEntry();

  /**
   * Performs necessary activity (if there is any)
   * when switching from this panel (leaving it) -
   * i.e. when user presses the Next button.
   */
  public abstract void doOnLeave();

  /**
   * Returns true if this panel is the last panel
   * (in the stack of panels/cards in the card layout).
   *
   * @return True if this panel is the last panel; false otherwise.
   */
  public boolean isLastPanel() {
    return this.panelPosition == PanelPosition.LAST;
  }

  /**
   * Returns true if this panel is the first panel
   * (in the stack of panels/cards in the card layout).
   *
   * @return True if this panel is the first panel; false otherwise.
   */
  public boolean isFirstPanel() {
    return this.panelPosition == PanelPosition.FIRST;
  }

  /** Unregisters locale sensitive components with the LocaleExt bean. */
  public void unregisterLocaleSensitiveComponents() {
    LocaleExt.unregisterComponents(this.localeSensitiveComps);
  }

  /**
   * Sets the header message text on the passed
   * dialog frame header message label object.
   *
   * @param messageLabel reference to the header message label object.
   */
  public void setHeaderMessage(JLabel messageLabel) {
    messageLabel.setText(LocaleExt.getString(this.headerMessageKey));
    LocaleExt.reregisterComponent(messageLabel, this.headerMessageKey);
  }

}
