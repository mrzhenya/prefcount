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

package net.curre.prefcount.gui;

import java.util.logging.Logger;

import net.curre.prefcount.gui.theme.skin.BusinessSkin;
import net.curre.prefcount.gui.theme.skin.CremeSkin;
import net.curre.prefcount.gui.theme.skin.DefaultSkin;
import net.curre.prefcount.gui.theme.skin.FieldOfWheatSkin;
import net.curre.prefcount.gui.theme.skin.FindingNemoSkin;
import net.curre.prefcount.gui.theme.skin.GreenMagicSkin;
import net.curre.prefcount.gui.theme.skin.MangoSkin;
import net.curre.prefcount.gui.theme.skin.ModerateSkin;
import net.curre.prefcount.gui.theme.skin.NebulaSkin;
import net.curre.prefcount.gui.theme.skin.OfficeBlue2007Skin;
import net.curre.prefcount.gui.theme.skin.PrefSkin;
import net.curre.prefcount.test.BaseTestCase;

import org.apache.commons.lang.StringUtils;

/**
 * This unit tests tests skin classes that implement
 * the PrefSkin interface.
 * <p/>
 * Created date: Dec 12, 2007
 *
 * @author Yevgeny Nyden
 */
public class PrefSkinsTest extends BaseTestCase {

  /** Private class logger. */
  private static Logger log = Logger.getLogger(PrefSkinsTest.class.toString());

  /** Skins classes to test. */
  private static final Class[] SKINS = {
      BusinessSkin.class, CremeSkin.class, DefaultSkin.class,
      FieldOfWheatSkin.class, FindingNemoSkin.class, GreenMagicSkin.class,
      MangoSkin.class, ModerateSkin.class, NebulaSkin.class,
      OfficeBlue2007Skin.class
  };

  /**
   * Test GUI for the 3 players settings.
   *
   * @throws Exception on error.
   */
  public void testAllSkins() throws Exception {

    log.info("Running testAllSkins()...");

    for (Class clazz : SKINS) {
      checkSkin(clazz);
    }
  }

  /** Private methods ***********************/

  /**
   * Checks a PrefSkin skin.
   *
   * @param clazz Class of the skin to test.
   */
  private void checkSkin(Class clazz) {
    assertNotNull("Skin class is null", clazz);
    final String name = clazz.getName();
    Class<?>[] interfaces = clazz.getInterfaces();
    boolean found = false;
    for (Class<?> interf : interfaces) {
      if (interf.isAssignableFrom(PrefSkin.class)) {
        found = true;
        break;
      }
    }
    assertTrue("Class " + name + " does not implement interface " +
               PrefSkin.class.getName(), found);
    PrefSkin skin = null;
    try {
      skin = (PrefSkin) clazz.newInstance();
    } catch (Exception e) {
      fail("Unable to instantiate class: \"" + name + "\"");
    }
    assertNotNull(name + " skin's boardBackgroundPaint is null", skin.getBoardBackgroundPaint());
    assertNotNull(name + " skin's boardLineColor is null", skin.getBoardLineColor());
    assertNotNull(name + " skin's boardLineStroke is null", skin.getBoardLineStroke());
    assertNotNull(name + " skin's mainBackgroundColor is null", skin.getMainBackgroundColor());
    assertValidResource(name + " skin's nameResourceKey", skin.getNameResourceKey());
    assertNotNull(name + " skin's playerNameColor is null", skin.getPlayerNameColor());
    assertNotNull(name + " skin's playerNameFont is null", skin.getPlayerNameFont());
    assertNotNull(name + " skin's playerNameStroke is null", skin.getPlayerNameStroke());
    assertNotNull(name + " skin's playerScoreColor is null", skin.getPlayerScoreColor());
    assertNotNull(name + " skin's playerScoreFont is null", skin.getPlayerScoreFont());
    assertNotNull(name + " skin's playerScoreStroke is null", skin.getPlayerScoreStroke());
    assertNotNull(name + " skin's playerTotalsColor is null", skin.getPlayerTotalsColor());
    assertNotNull(name + " skin's playerTotalsFont is null", skin.getPlayerTotalsFont());
    assertNotNull(name + " skin's playerTotalsStroke is null", skin.getPlayerTotalsStroke());
    String className = skin.getSubstanceSkinClassName();
    if (className != null) {
      assertTrue(name + " skin's substanceSkinClassName is empty", StringUtils.isNotBlank(className));
      try {
        Object o = Class.forName(className);
        assertNotNull(name + " skin error - unable to instantiate substanceSkinClass: \"" + className + "\"", o);
      } catch (ClassNotFoundException e) {
        fail(name + " skin error - unable to instantiate substanceSkinClass: \"" + className + "\"");
      }
    }
  }

}
