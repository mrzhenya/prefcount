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

import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.swing.JLabel;

import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.test.BaseTestCase;

import org.apache.commons.lang.StringUtils;

/**
 * This is a junit test for testing <code>LocaleExt</code> class.
 * <p/>
 * Created date: Nov 25, 2007
 *
 * @author Yevgeny Nyden
 */
public class LocaleExtTest extends BaseTestCase {

  /** Private class logger. */
  private static Logger log = Logger.getLogger(LocaleExtTest.class.toString());

  /**
   * Tests constructor.
   *
   * @throws Exception on error.
   */
  public void testConstructor() throws Exception {

    log.info("Running testConstructor()...");

    final String dispLang = "\u0420\u0443\u0441\u0441\u043A\u0438\u0439";
    LocaleExt loc = new LocaleExt("ru", "RU", dispLang);
    assertNotNull("Locale is null", loc.getLocale());
    assertEquals("Wrong locale's country", "RU", loc.getLocale().getCountry());
    assertEquals("Wrong locale's language", "ru", loc.getLocale().getLanguage());
    assertEquals("Wrong display language", dispLang, loc.getDisplayLanguage());
    assertNotNull("Enabled icon reference is null", loc.getLocaleIcon());
  }

  /**
   * Tests the equals method.
   *
   * @throws Exception on error.
   */
  public void testEquals() throws Exception {

    log.info("Running testEquals()...");

    LocaleExt loc0 = new LocaleExt("ru", "RU", "\u0420\u0443\u0441\u0441\u043A\u0438\u0439");
    assertNotNull("RU locale is null", loc0.getLocale());

    LocaleExt loc1 = new LocaleExt("us", "US", "English US");
    assertNotNull("First US locale is null", loc1.getLocale());

    LocaleExt loc2 = new LocaleExt("us", "US", "Another English US");
    assertNotNull("Second US locale is null", loc2.getLocale());

    assertFalse("RU and US locales should not be equal", loc0.equals(loc1));
    assertTrue("Locale should be equal to itself", loc0.equals(loc0));
    assertTrue("Different display language should not make locale different", loc1.equals(loc2));
    Object o = "TESTY";
    assertFalse("Locale should not be equal to a string", loc0.equals(o));
    o = null;
    assertFalse("Locale should not be equal to null", loc0.equals(o));
  }

  /**
   * Tests the hashCode method.
   *
   * @throws Exception on error.
   */
  public void testHashCode() throws Exception {

    log.info("Running testHashCode()...");

    LocaleExt loc = new LocaleExt("us", "US", "English US");
    assertNotNull("US locale is null", loc.getLocale());

    assertEquals("ExtLocale's hash code should be the same as of it's locale's",
                 loc.getLocale().hashCode(), loc.hashCode());
  }

  /**
   * Tests component registering/unregistering/reregistering
   * functionality.
   *
   * @throws Exception on error.
   */
  public void testRegistering() throws Exception {

    log.info("Running testRegistering()...");

    JLabel label1 = new JLabel();
    LocaleExt.registerComponent(label1, "pref.dialog.namePrefix");

    JLabel label2 = new JLabel();
    LocaleExt.registerComponent(label2, "pref.dialog.whistsFor");

    try {
      LocaleExt.registerComponent(label1, "pref.dialog.namePrefix");
      fail("Should not be able to register component twice!");
    } catch (Exception e) {
      // expected
    }

    PrefCountRegistry.getInstance().setCurrentLocale(PrefCountRegistry.DEFAULT_LOCALE_ID);
    LocaleExt.fireLocaleChangeEvent();

    ResourceBundle bundle = ResourceBundle.getBundle("default");
    final String whistText = bundle.getString("pref.dialog.namePrefix");

    assertEquals("Button's text is wrong (wasn't set?).", whistText, label1.getText());

    LocaleExt.reregisterComponent(label2, "pref.dialog.namePrefix", "Z");
    LocaleExt.fireLocaleChangeEvent();

    assertFalse("Button's text is not refreshed!", whistText.equals(label2.getText()));
  }

  /**
   * Tests miscellaneous functionality.
   *
   * @throws Exception on error.
   */
  public void testMiscellaneous() throws Exception {

    log.info("Running testMiscellaneous()...");

    LocaleExt loc = new LocaleExt("us", "US", "English US");
    assertTrue("toString() is blank", StringUtils.isNotBlank(loc.toString()));
  }

  /** Private methods ***********************/

}
