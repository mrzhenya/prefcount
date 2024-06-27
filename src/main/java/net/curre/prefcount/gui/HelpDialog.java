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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import net.curre.prefcount.App;
import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.gui.type.HelpType;
import net.curre.prefcount.util.LocaleExt;

/**
 * A dialog that displays help information.
 * <p/>
 * Created date: Jun 18, 2008
 *
 * @author Yevgeny Nyden
 */
public class HelpDialog extends JDialog {

  /** Reference to the text pane object. */
  private final JTextPane textPane;

  /** Constructs a new <code>HelpFrame</code> object. */
  public HelpDialog() {
    super.setLayout(new BorderLayout());
    this.setModal(true);

    this.textPane = new JTextPane();
    this.textPane.setMargin(new Insets(0, 20, 20, 10));
    this.textPane.setEditable(false);
    this.textPane.setBackground(Color.WHITE);

    JScrollPane scrollPane = new JScrollPane(this.textPane);
    super.setPreferredSize(new Dimension(600, 600));
    super.add(scrollPane, BorderLayout.CENTER);

    // Сlose action only hides the dialog.
    super.addWindowListener(new WindowAdapter() {
      /** {@inheritDoc} */
      @Override
      public void windowClosing(WindowEvent event) {
        setVisible(false);
      }
    });
  }

  /**
   * Shows help dialog.
   *
   * @param type the type of help to display.
   */
  public void showHelp(HelpType type) {
    super.setTitle(type.getTitle());
    try {
      this.textPane.setContentType("text/rtf");
      String localeStr = PrefCountRegistry.getCurrentLocale().getLocale().getLanguage();
      InputStream resource = App.class.getResourceAsStream("help/" + localeStr + "/" + type.getFilename());
      this.textPane.read(resource, null);
      assert resource != null;
      resource.close();
    } catch (IOException e) {
      this.textPane.setText(LocaleExt.getString("pref.countHelp.error"));
    }
    super.pack();
    super.setLocationRelativeTo(PrefCountRegistry.getInstance().getMainWindow());
    super.setVisible(true);
  }
}
