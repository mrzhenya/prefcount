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

import java.util.ResourceBundle;
import javax.swing.JLabel;

import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.test.BaseTestCase;

import org.apache.commons.lang3.StringUtils;

import static org.junit.Assert.assertNotEquals;

/**
 * This is a junit test for testing <code>LocaleExt</code> class.
 * <p/>
 * Created date: Nov 25, 2007
 *
 * @author Yevgeny Nyden
 */
public class LocaleExtTest extends BaseTestCase {

  /**
   * Tests the constructor.
   */
  public void testConstructor() {
    final String dispLang = "Русский";
    LocaleExt loc = new LocaleExt("ru", "RU", dispLang);
    assertNotNull("Locale is null", loc.getLocale());
    assertEquals("Wrong locale's country", "RU", loc.getLocale().getCountry());
    assertEquals("Wrong locale's language", "ru", loc.getLocale().getLanguage());
    assertEquals("Wrong display language", dispLang, loc.getDisplayLanguage());
    assertNotNull("Enabled icon reference is null", loc.getLocaleIcon());
  }

  /**
   * Tests the equals method.
   */
  public void testEquals() {
    LocaleExt loc0 = new LocaleExt("ru", "RU", "Русский");
    assertNotNull("RU locale is null", loc0.getLocale());

    LocaleExt loc1 = new LocaleExt("us", "US", "English US");
    assertNotNull("First US locale is null", loc1.getLocale());

    LocaleExt loc2 = new LocaleExt("us", "US", "Another English US");
    assertNotNull("Second US locale is null", loc2.getLocale());

    assertNotEquals("RU and US locales should not be equal", loc0, loc1);
    assertEquals("Locale should be equal to itself", loc0, loc0);
    assertEquals("Different display language should not make locale different", loc1, loc2);
    assertNotEquals("Locale should not be equal to null", null, loc0);
  }

  /**
   * Tests the hashCode method.
   */
  public void testHashCode() {
    LocaleExt loc = new LocaleExt("us", "US", "English US");
    assertNotNull("US locale is null", loc.getLocale());

    assertEquals("ExtLocale's hash code should be the same as of it's locale's",
                 loc.getLocale().hashCode(), loc.hashCode());
  }

  /**
   * Tests component registering/unregistering/re-registering
   * functionality.
   */
  public void testRegistering() {
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

    assertNotEquals("Button's text is not refreshed!", whistText, label2.getText());
  }

  /**
   * Tests miscellaneous functionality.
   */
  public void testMiscellaneous() {
    LocaleExt loc = new LocaleExt("us", "US", "English US");
    assertTrue("toString() is blank", StringUtils.isNotBlank(loc.toString()));
  }
}
