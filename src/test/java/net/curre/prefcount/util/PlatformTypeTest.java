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
import static net.curre.prefcount.util.PlatformType.LINUX;
import static net.curre.prefcount.util.PlatformType.MAC_OS;
import static net.curre.prefcount.util.PlatformType.UNKNOWN;
import static net.curre.prefcount.util.PlatformType.WINDOWS;

/**
 * This is a junit test for testing <code>PlatformType</code> class.
 * <p/>
 * Created date: Nov 25, 2007
 *
 * @author Yevgeny Nyden
 */
public class PlatformTypeTest extends BaseTestCase {

  /**
   * Tests miscellaneous <code>getPlatformType</code> methods.
   */
  public void testGetPlatformType() {
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
   * Checks platform type.
   *
   * @param expectedType Type to expect.
   * @param osNameToSet  Os string name to set.
   */
  private static void checkPlatform(PlatformType expectedType,
                                    String osNameToSet) {
    System.setProperty("os.name", osNameToSet);
    PlatformType type = PlatformType.getPlatformType();
    assertNotNull("Platform type is null", type);
    assertEquals("Wrong platform type", expectedType, type);
  }
}
