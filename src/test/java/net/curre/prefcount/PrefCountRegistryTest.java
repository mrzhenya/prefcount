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

package net.curre.prefcount;

import net.curre.prefcount.test.BaseTestCase;
import net.curre.prefcount.util.LocaleExt;

/**
 * This is a junit test for testing
 * <code>PrefCountRegistry</code> class.
 * <p/>
 * Created date: Dec 15, 2007
 *
 * @author Yevgeny Nyden
 */
public class PrefCountRegistryTest extends BaseTestCase {

  public void testLocale() {
    LocaleExt locale = PrefCountRegistry.getCurrentLocale();
    assertNotNull("Current locale is not set", locale);

    PrefCountRegistry.getInstance().setCurrentLocale(PrefCountRegistry.DEFAULT_LOCALE_ID);
    locale = PrefCountRegistry.getCurrentLocale();
    assertNotNull("Current locale is not set", locale);
    assertNotNull("Locale reference is null", locale.getLocale());
    assertEquals("Current locale seems to be wrong;",
                 PrefCountRegistry.DEFAULT_LOCALE_ID, locale.getLocale().getLanguage());

    PrefCountRegistry.getInstance().setCurrentLocale("NO_SUCH_LOCALE");
    locale = PrefCountRegistry.getCurrentLocale();
    assertNotNull("Current locale is not set", locale);
    assertNotNull("Locale reference is null", locale.getLocale());
    assertEquals("Current locale is expected to be set to default;",
                 PrefCountRegistry.DEFAULT_LOCALE_ID, locale.getLocale().getLanguage());
  }

}
