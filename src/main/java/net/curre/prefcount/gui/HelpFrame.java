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
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import net.curre.prefcount.App;
import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.gui.type.WindowComponent;
import net.curre.prefcount.util.LocaleExt;

/**
 * Object of this class represents a frame with help information.
 * <p/>
 * Created date: Jun 18, 2008
 *
 * @author Yevgeny Nyden
 */
public class HelpFrame extends JFrame {

  /** Name of the how to count help file (w/o extension). */
  private static final String HOW_TO_COUNT_FILENAME = "howToCount";

  /** Name of the preference reference help file (w/o extension). */
  private static final String PREF_REFERENCE_FILENAME = "prefReference";

  /** Name of the common rules help file (w/o extension). */
  private static final String COMMON_RULES_FILENAME = "commonRules";

  /** Reference to the text pane object. */
  private final JTextPane textPane;

  /** Current help type enum. */
  private WindowComponent currHelpEnum;

  /** Constructs a new <code>HelpFrame</code> object. */
  public HelpFrame() {

    super.setLayout(new BorderLayout());

    this.textPane = new JTextPane();
    this.textPane.setMargin(new Insets(0, 20, 20, 10));
    this.textPane.setEditable(false);

    JScrollPane scrollPane = new JScrollPane(this.textPane);
    super.setPreferredSize(new Dimension(600, 600));
    super.add(scrollPane, BorderLayout.CENTER);

    // close action only makes the frame not visible
    super.addWindowListener(new WindowAdapter() {
      /** {@inheritDoc} */
      @Override
      public void windowClosing(WindowEvent event) {
        setVisible(false);
      }
    });

    LocaleExt.registerComponent(new LocaleExt.LocaleExec() {
      /** {@inheritDoc} */
      public void doChange() {
        if (HelpFrame.this.isVisible() && HelpFrame.this.currHelpEnum != null) {
          refreshText(HelpFrame.this.currHelpEnum);
        }
      }
    }, "HELP_FRAME_KEY");
  }

  /**
   * Refreshes the title and the text.
   *
   * @param itemEnum enum that represents type of help.
   * @throws IllegalArgumentException for an unsupported type of help.
   */
  public void refreshText(WindowComponent itemEnum) {

    final String title = LocaleExt.getString(itemEnum.getTextKey());
    super.setTitle(title);

    String fileName;
    switch (itemEnum) {
      case HELP_COUNT_ACTION:
      case HELP_COUNT_ACTION2:
        fileName = HOW_TO_COUNT_FILENAME;
        break;

      case HELP_PREF_ACTION:
      case HELP_PREF_ACTION2:
        fileName = PREF_REFERENCE_FILENAME;
        break;

      case HELP_COMMON_ACTION:
      case HELP_COMMON_ACTION2:
        fileName = COMMON_RULES_FILENAME;
        break;

      default:
        throw new IllegalArgumentException("Unsupported help type: " + itemEnum);
    }

    this.currHelpEnum = itemEnum;

    try {
      this.textPane.setContentType("text/rtf");
      String localeStr = PrefCountRegistry.getCurrentLocale().getLocale().getLanguage();
      InputStream resource = App.class.getResourceAsStream("help/" + localeStr + "/" + fileName + ".rtf");
      this.textPane.read(resource, null);
      resource.close();

    } catch (IOException e) {
      this.textPane.setText(LocaleExt.getString("pref.countHelp.error"));
    }

    super.pack();
    super.setVisible(true);
  }
}
