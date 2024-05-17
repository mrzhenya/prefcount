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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.curre.prefcount.event.AllEventsTest;
import net.curre.prefcount.gui.DialogInnerPanelTest;
import net.curre.prefcount.gui.GeneralTest;
import net.curre.prefcount.gui.PrefSkinsTest;
import net.curre.prefcount.service.LafThemeServiceTest;
import net.curre.prefcount.service.ResultServiceTest;
import net.curre.prefcount.service.SettingsServiceTest;
import net.curre.prefcount.util.LocaleExtTest;
import net.curre.prefcount.util.UtilitiesTest;

/**
 * Unit test suite for prefCount application.
 * <p/>
 * Created date: May 4, 2007
 *
 * @author Yevgeny Nyden
 */
public class AppTest extends TestCase {

  /**
   * Returns the suite of all unit tests.
   *
   * @return The suite of all tests being tested.
   */
  public static Test suite() {

    // creating test suite
    TestSuite suite = new TestSuite();
    suite.addTestSuite(PrefCountRegistryTest.class);
    suite.addTestSuite(SettingsServiceTest.class);
    suite.addTestSuite(ResultServiceTest.class);
    suite.addTestSuite(LafThemeServiceTest.class);
    suite.addTestSuite(LocaleExtTest.class);
    suite.addTestSuite(UtilitiesTest.class);
    suite.addTestSuite(GeneralTest.class);
    suite.addTestSuite(DialogInnerPanelTest.class);
    suite.addTestSuite(PrefSkinsTest.class);
    suite.addTestSuite(AllEventsTest.class);

    return suite;
  }

}