/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as 
 * published by the Free Software Foundation;
 */

package net.curre.prefcount.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.curre.prefcount.PrefCountRegistry;

/**
 * Object of this class represents the
 * "About PrefCount" menu item action listener.
 * <p/>
 * Created date: Jul 29, 2007
 *
 * @author Yevgeny Nyden
 */
public class AboutActionListener implements ActionListener {

  /**
   * Shows the About information.
   * <p/>
   * {@inheritDoc}
   */
  public void actionPerformed(ActionEvent actionEvent) {
    PrefCountRegistry.getInstance().getMainWindow().showAboutInfo();
  }

}
