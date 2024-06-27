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

package net.curre.prefcount.service;

import net.curre.prefcount.App;
import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.util.LocaleExt;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.Objects;

/**
 * Service responsible for common UI tasks like opening dialogs.
 *
 * @author Yevgeny Nyden
 */
public class UiService {

  /** Enumeration tha represents a field type. */
  public enum FieldType {
    UNDEFINED, INTEGER, DOUBLE
  }

  /** Private class logger. */
  private static final Logger logger = LogManager.getLogger(UiService.class.getName());

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
        logger.error("ERROR: validateTextField: Unknown type: {}", type);
    }
    return false;
  }

  /**
   * Validates if the passed text field value is a valid integer or an empty string.
   *
   * @param field Input text field to validate.
   * @return true If the component's text value is a valid integer or an empty string; false otherwise.
   */
  public static boolean validateIntTextField(JTextField field) {
    if (StringUtils.isBlank(field.getText())) {
      return true;
    }
    return StringUtils.isNumeric(field.getText().trim());
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
   * @param decrease Value to be subtracted from the RGB channels of the model color.
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
   *         false if the CANCEL options was chosen.
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
}
