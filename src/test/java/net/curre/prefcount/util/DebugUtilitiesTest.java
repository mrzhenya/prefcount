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

import net.curre.prefcount.test.BaseTestCase;

/**
 * This is a junit test for testing <code>DebugUtilities</code> class.
 * <p/>
 * @author Yevgeny Nyden
 */
public class DebugUtilitiesTest extends BaseTestCase {

  /**
   * Tests DebugUtilities methods.
   */
  public void testMiscellaneous() {
    // the following methods are just tested for not throwing exceptions
    DebugUtilities.printTime("");
    DebugUtilities.printLookAndFeels();
    DebugUtilities.printAvailableFonts();
  }
}
