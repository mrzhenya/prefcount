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

package net.curre.prefcount.util;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.Calendar;
import java.util.Objects;
import java.util.logging.Logger;

import net.curre.prefcount.App;
import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.gui.aa.AAJLabel;
import net.curre.prefcount.gui.aa.AAJPanel;
import net.curre.prefcount.gui.type.UIItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

/**
 * Object of this class represents a set of
 * common utilities for prefcount.
 * <p/>
 * Created date: Apr 7, 2007
 *
 * @author Yevgeny Nyden
 */
public class Utilities {

  /** Private class logger. */
  private static final Logger log = Logger.getLogger(Utilities.class.toString());

  /** Enumeration tha represents a field type. */
  public enum FieldType {

    UNDEFINED, INTEGER, DOUBLE
  }

  /** Enumeration tha represents a platform/os type. */
  public enum PlatformType {

    MAC_OS, LINUX, WINDOWS, UNKNOWN
  }

  /** Helper object to be used in the printTime() method. */
  public static Calendar lastTime = Calendar.getInstance();

  /**
   * Validates if the passed component value is valid.
   *
   * @param field Input field, which value to validate.
   * @param type  Type of the value to check (i.e. Utilities.TYPE_INTEGER).
   * @return true If the component value is valid; false otherwise.
   */
  public static boolean validateTextField(JTextField field, FieldType type) {
    String str = field.getText().trim();
    switch (type) {
      case INTEGER:
        return StringUtils.isNumeric(str);
      case DOUBLE:
        try {
          Double.parseDouble(str);
          return true;
        } catch (NumberFormatException e) {
          return false;
        }
      default:
        log.severe("ERROR: validateTextField: Unknown type: " + type);
    }
    return false;
  }

  /**
   * Computes the X coordinate for the object so that it
   * gets centered in the container, which is basically:
   * (containerWidth - objectWidth) / 2;
   *
   * @param containerWidth container width (where the object is drawn).
   * @param objectWidth    the object width (what is being centered).
   * @return X coordinate for the object, so it is centered in the container.
   */
  public static int computeCenterX(int containerWidth, int objectWidth) {
    return (containerWidth - objectWidth) / 2;
  }

  /**
   * Returns capitalized first letter from the passed text field
   * or null if the text field is empty.
   *
   * @param field Text field to read.
   * @return Capitalized first letter from the passed text field
   *         or null if the text field is empty.
   */
  public static String getFirstLetterFromField(JTextField field) {
    String str = field.getText();
    if (StringUtils.isBlank(str)) {
      return null;
    }
    return str.trim().substring(0, 1).toUpperCase();
  }

  /**
   * Gets and parses text from the given text field.
   * If the text field is null or contains an empty
   * string or white space only, 0 if returned.
   *
   * @param field Text field to parse.
   * @return Parsed integer.
   * @throws NumberFormatException If text field contains an invalid integer.
   */
  public static int parseIntFromTextField(JTextField field) {
    if (field != null) {
      String value = field.getText().trim();
      if (!value.isEmpty()) {
        return Integer.parseInt(value);
      }
    }
    return 0;
  }

  /**
   * Computes the size of the passed String.
   *
   * @param g2  Graphics object to use.
   * @param str String to be measured.
   * @return The size of the passed string as a <code>Dimension</code> object.
   */
  public static Dimension determineSizeOfString(Graphics2D g2, String str) {
    FontMetrics metrics = g2.getFontMetrics(g2.getFont());
    int height = metrics.getHeight();
    int width = metrics.stringWidth(str);
    return new Dimension(width, height);
  }

  /**
   * Creates a gif image for the given file name
   * (image file is expected to be in the images/ directory
   * relative to the net.curre.prefcount.App class).
   *
   * @param fileName File name without extension.
   * @return The created ImageIcon object.
   */
  public static ImageIcon createImage(String fileName) {
    return new ImageIcon(Objects.requireNonNull(App.class.getResource("images/" + fileName + ".gif")));
  }

  /**
   * Underlines a letter in the given string at
   * the given position; note, that the string is
   * converted to html.
   *
   * @param str String to underline.
   * @param ind Letter index, which to underline.
   * @return A string with underlined letter at the given position.
   */
  public static String underlineLetter(String str, int ind) {
    return "<HTML>" + str.substring(0, ind) + "<U>" + str.charAt(ind) +
        "</U>" + str.substring(ind + 1);

  }

  /**
   * Creates a new color that is darker than the
   * passed color according to the passed int parameter.
   *
   * @param color    Model color.
   * @param decrease Value to be subtracted from the RGB chanels of the model color.
   * @return New darker color.
   */
  public static Color createDarkerColor(Color color, int decrease) {
    return new Color(color.getRed() - decrease,
                     color.getGreen() - decrease,
                     color.getBlue() - decrease);
  }

  /**
   * Opens a new frame and displays a warning message in it
   * (given its resource key) with two buttons OK and CANCEL.
   *
   * @param messageKey Message key.
   * @param yesKey     Yes button test key.
   * @param cancelKey  Cancel button text key.
   * @return return true if the user has chosen to continue (hit the OK button);
   *         false if the CANCEL optoins was chosen.
   */
  public static boolean displayOkCancelMessage(String messageKey, String yesKey, String cancelKey) {
    String msg = LocaleExt.getString(messageKey);
    String yes = LocaleExt.getString(yesKey);
    String cancel = LocaleExt.getString(cancelKey);

    int answer = JOptionPane.showOptionDialog(
        PrefCountRegistry.getInstance().getMainWindow(), msg, "Warning", JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.WARNING_MESSAGE, null, new Object[]{yes, cancel}, cancel);

    return answer == JOptionPane.OK_OPTION;
  }

  /**
   * This method is used for debugging purposes
   * to print timestamps and the elapsed time since the last
   * call to this method.
   *
   * @param msg Message to add to the print statement.
   */
  public static void printTime(String msg) {
    Calendar currTime = Calendar.getInstance();
    Calendar diff = Calendar.getInstance();
    long currMls = currTime.getTimeInMillis();
    diff.setTimeInMillis(currMls - lastTime.getTimeInMillis());
    FastDateFormat f = FastDateFormat.getInstance("mm:ss:SSS");
    System.out.println("TIME:::::: " + f.format(currTime.getTime()) +
        " (" + f.format(diff) + ") - " + msg);
    lastTime.setTimeInMillis(currMls);
  }

  /** Prints available looks and feels. */
  public static void printLookAndFeels() {
    UIManager.LookAndFeelInfo[] laf = UIManager.getInstalledLookAndFeels();
    for (UIManager.LookAndFeelInfo lookAndFeelInfo : laf) {
      System.out.print("LAF Name: " + lookAndFeelInfo.getName() + "\t");
      System.out.println("  LAF Class name: " + lookAndFeelInfo.getClassName());
    }
  }

  /** Displays available fonts in a frame. */
  public static void printAvailableFonts() {
    JFrame f = new JFrame("Testing Fonts");
    f.setSize(400, 400);
    JPanel mainPanel = new AAJPanel();
    f.add(mainPanel);
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    String[] fontNames = ge.getAvailableFontFamilyNames();
    for (String fontName : fontNames) {
      JPanel panel = new JPanel();
      JLabel label = new AAJLabel(fontName);
      label.setFont(new Font(fontName, Font.PLAIN, 16));
      panel.add(label);
      mainPanel.add(panel);

      System.out.println(fontName);
    }
    f.pack();
    f.setVisible(true);
  }

  /**
   * Determines the platform/os type we are running on.
   *
   * @return A PlatformType enumeration that represents the platform/os.
   */
  public static PlatformType getPlatformType() {
    if (System.getProperty("mrj.version") == null) {
      String osProp = System.getProperty("os.name").toLowerCase();
      if (osProp.startsWith("windows")) {
        return PlatformType.WINDOWS;
      } else if (osProp.startsWith("mac")) {
        return PlatformType.MAC_OS;
      } else if (osProp.startsWith("linux")) {
        return PlatformType.LINUX;
      } else {
        return PlatformType.UNKNOWN;
      }
    }
    return PlatformType.MAC_OS;
  }

  /**
   * Returns true if we are running on macOS; false otherwise.
   *
   * @return True if we are on macOS; false otherwise.
   */
  public static boolean isMacOs() {
    return getPlatformType() == PlatformType.MAC_OS;
  }

  /**
   * Computes preferred size.
   *
   * @param panel panel to compute the preferred size of.
   * @return dimension object that represent the size.
   */
  public static Dimension getPreferredSize(JPanel panel) {
    Dimension preferredSize = new Dimension();
    for (Component component : panel.getComponents()) {
      Rectangle bounds = component.getBounds();
      preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
      preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
    }
    Insets insets = panel.getInsets();
    preferredSize.width += insets.right;
    preferredSize.height += insets.bottom;
    return preferredSize;
  }

  /**
   * Generates button label text for the given UI item.
   *
   * @param item UI item to generate text for.
   * @return label according to the current locale.
   */
  public static String generateButtonText(UIItem item) {
    String label = LocaleExt.getString(item.getTextKey());

    String shortcutKey = item.getShortcutKey();
    if (shortcutKey != null) {
      String shortcut = LocaleExt.getString(shortcutKey);
      String shortcutIndexKey = item.getShortcutIndexKey();
      if (shortcutIndexKey != null) {
        String shortcutIndex = LocaleExt.getString(shortcutIndexKey);
        int index = Integer.parseInt(shortcutIndex);
        if (index < 0) {
          label += " (" + shortcut + ")";
          label = Utilities.underlineLetter(label, label.lastIndexOf(shortcut));

        } else {
          label = Utilities.underlineLetter(label, index);
        }
      }
    }

    return label;
  }
}
