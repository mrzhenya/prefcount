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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import net.curre.prefcount.PrefCountRegistry;

/**
 * Object of this class represents an action listener
 * to change current language.
 * <p/>
 * Created date: Jun 2, 2007
 *
 * @author Yevgeny Nyden
 */
public class ChangeLanguageActionListener
    implements ItemListener, ActionListener {

  /**
   * The locale identifier (case-insensitive language name)
   * to which to switch.
   */
  private final String localeId;

  /**
   * Constructor.
   *
   * @param localeId The locale identifier (case-insensitive language name).
   */
  public ChangeLanguageActionListener(String localeId) {
    this.localeId = localeId;
  }

  /**
   * Changes language only if the item event shows
   * that the item has been selected (and not deselected).
   * This ItemListener method is for awt menu only.
   * <p/>
   * {@inheritDoc}
   */
  public void itemStateChanged(ItemEvent itemEvent) {
    if (itemEvent.getStateChange() == ItemEvent.DESELECTED) {
      return;
    }
    changeLanguage();
  }

  /**
   * Changes language.
   * This ActionListener method is for swing menu only.
   * <p/>
   * {@inheritDoc}
   */
  public void actionPerformed(ActionEvent actionEvent) {
    changeLanguage();
  }

  /** Helper method that changes the language. */
  private void changeLanguage() {
    PrefCountRegistry.getInstance().setCurrentLocale(localeId);
  }
}
