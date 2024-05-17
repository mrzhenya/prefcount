/**
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JTextField;

import net.curre.prefcount.gui.aa.AAJTextField;
import net.curre.prefcount.test.BaseTestCase;
import static net.curre.prefcount.util.Utilities.FieldType.INTEGER;
import static net.curre.prefcount.util.Utilities.FieldType.UNDEFINED;
import static net.curre.prefcount.util.Utilities.PlatformType.LINUX;
import static net.curre.prefcount.util.Utilities.PlatformType.MAC_OS;
import static net.curre.prefcount.util.Utilities.PlatformType.UNKNOWN;
import static net.curre.prefcount.util.Utilities.PlatformType.WINDOWS;

/**
 * This is a junit test for testing <code>Utilities</code> class.
 * <p/>
 * Created date: Nov 25, 2007
 *
 * @author Yevgeny Nyden
 */
public class UtilitiesTest extends BaseTestCase {

  /** Private class logger. */
  private static Logger log = Logger.getLogger(UtilitiesTest.class.toString());

  /**
   * Tests the testValidateTextField() method.
   *
   * @throws Exception on error.
   */
  public void testValidateTextField() throws Exception {

    log.info("Running testValidateTextField()...");
    JTextField field = new AAJTextField();

    field.setText("123");
    boolean result = Utilities.validateTextField(field, INTEGER);
    assertTrue("String \"123\" should be tested as a valid integer", result);

    field.setText("123X");
    result = Utilities.validateTextField(field, INTEGER);
    assertFalse("String \"123X\" should not be tested as a valid integer", result);

    field.setText(" \t123 \n");
    result = Utilities.validateTextField(field, INTEGER);
    assertTrue("String \" \t123 \n\" should be tested as a valid integer", result);

    field.setText("123");
    try {
      result = Utilities.validateTextField(field, UNDEFINED);
      assertFalse("False must be returned for an unknown type", result);
    } catch (Exception e) {
      fail("Exception must not be thrown for an unknown type");
    }
  }

  /**
   * Tests the getFirstLetterFromField() method.
   *
   * @throws Exception on error.
   */
  public void testGetFirstLetterFromField() throws Exception {

    log.info("Running testGetFirstLetterFromField()...");
    JTextField field = new AAJTextField();

    String letter = Utilities.getFirstLetterFromField(field);
    assertNull("Null is expected for a null field", letter);

    field.setText("  \t");
    letter = Utilities.getFirstLetterFromField(field);
    assertNull("Null is expected for an empty field", letter);

    field.setText("  dmitry ");
    letter = Utilities.getFirstLetterFromField(field);
    assertNotNull("Return value must not be null for a non-empty string", letter);
    assertFalse("Returned letter must be capitalized", "d".equals(letter));
    assertEquals("Returned letter is wrong", "D", letter);
  }

  /**
   * Tests the determineSizeOfString() method.
   *
   * @throws Exception on error.
   */
  public void testDetermineSizeOfString() throws Exception {

    log.info("Running testDetermineSizeOfString()...");

    // creating a dummy image to obtain a graphics object
    BufferedImage img = new BufferedImage(10, 10, 10);
    Graphics2D g2 = (Graphics2D) img.getGraphics();
    Dimension size = Utilities.determineSizeOfString(g2, "");
    assertNotNull("Returned dimension is null", size);
    assertEquals("The width of an empty string should be 0", 0D, size.getWidth());
    assertTrue("The height of the string should be greater than 0", size.getHeight() > 0D);

    size = Utilities.determineSizeOfString(g2, "abc");
    assertNotNull("Returned dimension is null", size);
    assertTrue("The width of the string should be greater than 0", size.getWidth() > 0D);
    assertTrue("The height of the string should be greater than 0", size.getHeight() > 0D);
  }

  /**
   * Tests miscellaneous Utilities methods.
   *
   * @throws Exception on error.
   */
  public void testGetPlatformType() throws Exception {
    log.info("Running testMiscellaneous()...");
    final String currOs = System.getProperty("os.name");
    final String currMrj = System.getProperty("mrj.version");
    try {
      System.clearProperty("mrj.version");
      checkPlatform(MAC_OS, "MAC OS");
      checkPlatform(LINUX, "linux");
      checkPlatform(LINUX, "LINUX");
      checkPlatform(WINDOWS, "windows");
      checkPlatform(UNKNOWN, "nothing");
      System.setProperty("mrj.version", "something");
      checkPlatform(MAC_OS, "linux");
      checkPlatform(MAC_OS, "nothing");

    } finally {
      System.setProperty("os.name", currOs);
      if (currMrj == null) {
        System.clearProperty("mrj.version");
      } else {
        System.setProperty("mrj.version", currMrj);
      }
    }
  }

  /**
   * Tests miscellaneous Utilities methods.
   *
   * @throws Exception on error.
   */
  public void testMiscellaneous() throws Exception {

    log.info("Running testMiscellaneous()...");

    ImageIcon image = Utilities.createImage("ru");
    assertNotNull("Returned image must not be null", image);
    image = Utilities.createImage("us");
    assertNotNull("Returned image must not be null", image);

    String str = Utilities.underlineLetter("012345", 1);
    assertNotNull("Returned string must not be null", str);
    str = str.toUpperCase();
    assertTrue("Returned string must start with <HTML>", str.startsWith("<HTML>"));
    assertEquals("Wrong index of <U>", 7, str.indexOf("<U>"));
    assertEquals("Wrong index of </U>", 11, str.indexOf("</U>"));

    Color color = new Color(100, 100, 100);
    Color c = Utilities.createDarkerColor(color, 10);
    assertNotNull("Returned color is null", c);
    assertEquals("Wrong value for red", 90, c.getRed());
    assertEquals("Wrong value for green", 90, c.getGreen());
    assertEquals("Wrong value for blue", 90, c.getBlue());

    // the following methods are just tested for not throwing exceptions
    Utilities.printTime("");
    Utilities.printLookAndFeels();
  }

  /**
   * Checks platform type.
   *
   * @param expectedType Type to expect.
   * @param osNameToSet  Os string name to set.
   */
  private static void checkPlatform(Utilities.PlatformType expectedType,
                                    String osNameToSet) {
    System.setProperty("os.name", osNameToSet);
    Utilities.PlatformType type = Utilities.getPlatformType();
    assertNotNull("Platform type is null", type);
    assertEquals("Wrong platform type", expectedType, type);
  }

}
