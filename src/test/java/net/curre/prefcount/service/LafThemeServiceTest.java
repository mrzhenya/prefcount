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

package net.curre.prefcount.service;

import java.util.logging.Logger;

import net.curre.prefcount.gui.theme.skin.DefaultSkin;
import net.curre.prefcount.gui.theme.skin.PrefSkin;
import net.curre.prefcount.gui.theme.skin.AquaSkin;
import net.curre.prefcount.test.BaseTestCase;

/**
 * This is a junit test for testing LAF service.
 * <p/>
 * Created date: Jul 25, 2007
 *
 * @author Yevgeny Nyden
 */
public class LafThemeServiceTest extends BaseTestCase {

  /** Private class logger. */
  private static Logger log = Logger.getLogger(LafThemeServiceTest.class.toString());

  /**
   * Test main settings service functionality.
   *
   * @throws Exception on error.
   */
  public void testAll() throws Exception {

    log.info("Running testAll()...");

    PrefSkin[] skins = LafThemeService.AVAILABLE_SKINS;
    assertNotNull("PrefSkin array is null", skins);
    assertTrue("PrefSkin array is empty", skins.length > 0);
    for (PrefSkin skin : skins) {
      assertNotNull("PrefSkin \"" + skin + "\" is null", skin);
      assertNotNull("PrefSkin \"" + skin + "\" boardBackgroundPaint is null", skin.getBoardBackgroundPaint());
      assertNotNull("PrefSkin \"" + skin + "\" boardLineColor is null", skin.getBoardLineColor());
      assertNotNull("PrefSkin \"" + skin + "\" boardLineStroke is null", skin.getBoardLineStroke());
      assertNotNull("PrefSkin \"" + skin + "\" mainBackgroundColor is null", skin.getMainBackgroundColor());
      assertNotNull("PrefSkin \"" + skin + "\" nameResourceKey is null", skin.getNameResourceKey());
      assertNotNull("PrefSkin \"" + skin + "\" playerNameColor is null", skin.getPlayerNameColor());
      assertNotNull("PrefSkin \"" + skin + "\" playerNameFont is null", skin.getPlayerNameFont());
      assertNotNull("PrefSkin \"" + skin + "\" playerNameStroke is null", skin.getPlayerNameStroke());
      assertNotNull("PrefSkin \"" + skin + "\" playerTotalsColor is null", skin.getPlayerTotalsColor());
      assertNotNull("PrefSkin \"" + skin + "\" playerTotalsFont is null", skin.getPlayerTotalsFont());
      assertNotNull("PrefSkin \"" + skin + "\" playerTotalsStroke is null", skin.getPlayerTotalsStroke());
      if (skin instanceof DefaultSkin || skin instanceof AquaSkin) {
        assertNull("PrefSkin \"" + skin + "\" should have substanceSkinClassName set to null.", skin.getSubstanceSkinClassName());
      } else {
        assertNotNull("PrefSkin \"" + skin + "\" substanceSkinClassName is null", skin.getSubstanceSkinClassName());
      }
    }

    LafThemeService service = LafThemeService.getInstance();
    assertNotNull("LafThemeService is null", service);
    PrefSkin prefSkin = service.getCurrentSkin();
    assertNotNull("Current skin is null", prefSkin);
    assertTrue("Default current skin must be DefaultSkin", prefSkin instanceof DefaultSkin);

    // checking finding skin method
    PrefSkin skin = LafThemeService.findSkinById(skins[0].getNameResourceKey());
    assertNotNull("Unable to find skin by ID \"" + skins[0].getNameResourceKey() + "\"", skin);
    assertEquals("Wrong skin found", skins[0].getNameResourceKey(), skin.getNameResourceKey());
    try {
      LafThemeService.findSkinById("noSuchSkinId");
      fail("Expected ServiceException for ID \"noSuchSkinId\" parameter.");
    } catch (ServiceException e) {
      // expected
    }
  }

}
