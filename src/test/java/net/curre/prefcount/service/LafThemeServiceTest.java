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

package net.curre.prefcount.service;

import net.curre.prefcount.gui.theme.LafTheme;
import net.curre.prefcount.gui.theme.LafThemeId;
import net.curre.prefcount.test.BaseTestCase;

import java.util.ArrayList;

/**
 * This is a junit test for testing LAF service.
 * <p/>
 * Created date: Jul 25, 2007
 *
 * @author Yevgeny Nyden
 */
public class LafThemeServiceTest extends BaseTestCase {

  /**
   * Tests initialization of the default object state.
   */
  public void testDefault() {
    LafThemeService testLafService = new LafThemeService();
    ArrayList<LafTheme> themes = testLafService.getSupportedThemes();
    assertNotNull("Null LAF themes list", themes);
    assertTrue("Should be at least 3 supported themes", themes.size() >= 3);

    LafTheme lafTheme = testLafService.getCurrentLafTheme();
    assertNotNull("Current theme is null", lafTheme);
    LafThemeId lafThemeId = testLafService.getCurrentLafThemeId();
    assertNotNull("Current theme Id is null", lafThemeId);
    assertEquals("Theme and theme id don't match", lafTheme.getId(), lafThemeId);
  }
}
