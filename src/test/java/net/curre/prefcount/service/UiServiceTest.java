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

import net.curre.prefcount.gui.aa.AAJTextField;
import net.curre.prefcount.test.BaseTestCase;

import javax.swing.ImageIcon;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import static org.junit.Assert.assertNotEquals;

/**
 * This is a junit test for testing <code>UiService</code> class.
 * <p/>
 * @author Yevgeny Nyden
 */
public class UiServiceTest extends BaseTestCase {

  /**
   * Tests the testValidateTextField() method.
   */
  public void testValidateTextField() {
    JTextField field = new AAJTextField();

    field.setText("123");
    boolean result = UiService.validateTextField(field, UiService.FieldType.INTEGER);
    assertTrue("String \"123\" should be tested as a valid integer", result);

    field.setText("123X");
    result = UiService.validateTextField(field, UiService.FieldType.INTEGER);
    assertFalse("String \"123X\" should not be tested as a valid integer", result);

    field.setText(" \t123 \n");
    result = UiService.validateTextField(field, UiService.FieldType.INTEGER);
    assertTrue("String \" \t123 \n\" should be tested as a valid integer", result);

    field.setText("123");
    try {
      result = UiService.validateTextField(field, UiService.FieldType.UNDEFINED);
      assertFalse("False must be returned for an unknown type", result);
    } catch (Exception e) {
      fail("Exception must not be thrown for an unknown type");
    }
  }

  /**
   * Tests the getFirstLetterFromField() method.
   */
  public void testGetFirstLetterFromField() {
    JTextField field = new AAJTextField();

    String letter = UiService.getFirstLetterFromField(field);
    assertNull("Null is expected for a null field", letter);

    field.setText("  \t");
    letter = UiService.getFirstLetterFromField(field);
    assertNull("Null is expected for an empty field", letter);

    field.setText("  dmitry ");
    letter = UiService.getFirstLetterFromField(field);
    assertNotNull("Return value must not be null for a non-empty string", letter);
    assertNotEquals("Returned letter must be capitalized", "d", letter);
    assertEquals("Returned letter is wrong", "D", letter);
  }

  /**
   * Tests the determineSizeOfString() method.
   */
  public void testDetermineSizeOfString() {
    // creating a dummy image to obtain a graphics object
    BufferedImage img = new BufferedImage(10, 10, 10);
    Graphics2D g2 = (Graphics2D) img.getGraphics();
    Dimension size = UiService.determineSizeOfString(g2, "");
    assertNotNull("Returned dimension is null", size);
    assertEquals("The width of an empty string should be 0", 0D, size.getWidth());
    assertTrue("The height of the string should be greater than 0", size.getHeight() > 0D);

    size = UiService.determineSizeOfString(g2, "abc");
    assertNotNull("Returned dimension is null", size);
    assertTrue("The width of the string should be greater than 0", size.getWidth() > 0D);
    assertTrue("The height of the string should be greater than 0", size.getHeight() > 0D);
  }

  /**
   * Tests miscellaneous Utilities methods.
   */
  public void testMiscellaneous() {
    ImageIcon image = UiService.createImage("ru");
    assertNotNull("Returned image must not be null", image);
    image = UiService.createImage("us");
    assertNotNull("Returned image must not be null", image);

    String str = UiService.underlineLetter("012345", 1);
    assertNotNull("Returned string must not be null", str);
    str = str.toUpperCase();
    assertTrue("Returned string must start with <HTML>", str.startsWith("<HTML>"));
    assertEquals("Wrong index of <U>", 7, str.indexOf("<U>"));
    assertEquals("Wrong index of </U>", 11, str.indexOf("</U>"));

    Color color = new Color(100, 100, 100);
    Color c = UiService.createDarkerColor(color, 10);
    assertNotNull("Returned color is null", c);
    assertEquals("Wrong value for red", 90, c.getRed());
    assertEquals("Wrong value for green", 90, c.getGreen());
    assertEquals("Wrong value for blue", 90, c.getBlue());
  }
}
