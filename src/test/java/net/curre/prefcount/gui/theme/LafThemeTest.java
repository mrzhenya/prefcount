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

package net.curre.prefcount.gui.theme;

import net.curre.prefcount.service.LafThemeService;
import net.curre.prefcount.test.BaseTestCase;

import java.util.ArrayList;

/**
 * Tests the laf theme code.
 *
 * @author Yevgeny Nyden
 */
public class LafThemeTest extends BaseTestCase {

  /** Test each theme for returning sane values. */
  public void testAll() {
    LafThemeService testLafService = new LafThemeService();
    ArrayList<LafTheme> lafThemes = testLafService.getSupportedThemes();

    for (LafTheme lafTheme : lafThemes) {
      String name = lafTheme.getId().name();
      assertNotNull(name + " skin's boardBackgroundPaint is null", lafTheme.getBoardBackgroundPaint());
      assertNotNull(name + " skin's boardLineColor is null", lafTheme.getBoardLineColor());
      assertNotNull(name + " skin's boardLineStroke is null", lafTheme.getBoardLineStroke());
      assertNotNull(name + " skin's mainBackgroundColor is null", lafTheme.getMainBackgroundColor());
      assertValidResource(name + " skin's nameResourceKey", lafTheme.getNameResourceKey());
      assertNotNull(name + " skin's playerNameColor is null", lafTheme.getPlayerNameColor());
      assertNotNull(name + " skin's playerNameFont is null", lafTheme.getPlayerNameFont());
      assertNotNull(name + " skin's playerNameStroke is null", lafTheme.getPlayerNameStroke());
      assertNotNull(name + " skin's playerScoreColor is null", lafTheme.getPlayerScoreColor());
      assertNotNull(name + " skin's playerScoreFont is null", lafTheme.getPlayerScoreFont());
      assertNotNull(name + " skin's playerScoreStroke is null", lafTheme.getPlayerScoreStroke());
      assertNotNull(name + " skin's playerTotalsColor is null", lafTheme.getPlayerTotalsColor());
      assertNotNull(name + " skin's playerTotalsFont is null", lafTheme.getPlayerTotalsFont());
      assertNotNull(name + " skin's playerTotalsStroke is null", lafTheme.getPlayerTotalsStroke());
    }
  }
}
