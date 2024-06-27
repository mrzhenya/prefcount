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

package net.curre.prefcount.test;

import junit.framework.TestCase;
import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.bean.GameResultBean;
import net.curre.prefcount.bean.PlayerStatistics;
import net.curre.prefcount.gui.type.Place;
import net.curre.prefcount.util.LocaleExt;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.logging.*;

/**
 * Object of this class represents a base test case
 * for all unit tests and contains a set of prefcount
 * specific assertions and common utilities.
 * <p/>
 * Created date: Nov 27, 2007
 *
 * @author Yevgeny Nyden
 */
abstract public class BaseTestCase extends TestCase {

  /** Application test log file path. */
  protected static final String TEST_LOG_PATH = "target/prefcount-test.log";

  /** Application test log level. */
  protected static final Level TEST_LOG_LEVEL = Level.FINE;

  /** Flag to indicate that logging is initialized. */
  private static boolean loggingInitialized;

  /** {@inheritDoc} */
  @Override
  protected void setUp() throws Exception {
    initLogging();
    super.setUp();
  }

  /**
   * Asserts the resource key is not null, not empty,
   * and valid (has corresponding string for all available
   * locales).
   *
   * @param msg Error message.
   * @param key Resource key to test.
   */
  public static void assertValidResource(String msg, String key) {
    assertNotNull(msg + " - the key is null", msg);
    assertFalse(msg + " - the key is empty", msg.trim().isEmpty());
    LocaleExt origLocale = PrefCountRegistry.getCurrentLocale();
    assertNotNull("Current locale is null", origLocale);
    String currLang = PrefCountRegistry.AVAILABLE_LOCALES[0].getDisplayLanguage();
    try {
      for (LocaleExt locale : PrefCountRegistry.AVAILABLE_LOCALES) {
        currLang = locale.getDisplayLanguage();
        Locale.setDefault(locale.getLocale());
        String string = LocaleExt.getString(key);
        assertTrue("The string for the key \"" + key + "\" and language " +
                   currLang + " is blank", StringUtils.isNotBlank(string));
      }
    } catch (Exception ex) {
      fail("No string is found for the key \"" + key + "\" and language " + currLang);
    } finally {
      Locale.setDefault(origLocale.getLocale());
    }
  }

  /**
   * Asserts that given label is not null and
   * it's text field is not blank.
   *
   * @param message Error message to display.
   * @param label   Label object to test.
   */
  public static void assertNotBlankAndVisible(String message, JLabel label) {
    assertNotNull(message + " - label is null", label);
    assertFalse(message + " - text is blank", StringUtils.isBlank(label.getText()));
    assertTrue(message + " - label is not visible", label.isVisible());
  }

  /**
   * Asserts that the given JButton is not null,
   * is selected/unselected, and is enabled/disabled
   * according to the passed values.
   *
   * @param message    Message that identifies this button.
   * @param button     Button to test.
   * @param isEnabled  True if the button is expected to be enabled; false otherwise.
   * @param isSelected True if the button is expected to be selected; false otherwise.
   */
  public static void assertButtonValues(String message, JButton button,
                                        boolean isEnabled, boolean isSelected) {
    assertNotNull(message + " is null", button);
    assertTrue(message + " text is blank", StringUtils.isNotBlank(button.getText()));
    assertEquals(message + " is " + (isEnabled ? "disabled" : "enabled") + "; ",
                 isEnabled, button.isEnabled());
    assertEquals(message + " is " + (isSelected ? "not" : "") +
                 " selected; ", isSelected, button.isSelected());
  }

  /**
   * Asserts that the given map does not
   * have values for the given keys.
   *
   * @param map  Map to test.
   * @param keys Keys to test against the given map.
   */
  public static <T> void assertMapNullValues(Map map, List<T> keys) {
    for (T key : keys) {
      assertNull("Unexpected value is found for key: \"" + key + "\"", map.get(key));
    }
  }

  /**
   * Asserts that the given map has
   * not null values that for the given keys.
   *
   * @param map  Map to test.
   * @param keys Keys to test against the given map.
   */
  public static <T> void assertMapNotNullValues(Map map, List<T> keys) {
    for (T key : keys) {
      assertNotNull("No value is found for key: \"" + key + "\"", map.get(key));
    }
  }

  /**
   * Asserts that the given component is visible.
   *
   * @param component Component to test.
   * @param message   Error message to display.
   */
  public static void assertVisible(String message, Component component) {
    assertTrue(message, component.isVisible());
  }

  /**
   * Asserts that the given component is visible.
   *
   * @param component Component to test.
   */
  public static void assertVisible(Component component) {
    assertTrue("Component is not visible", component.isVisible());
  }

  /**
   * Asserts that the given component is not visible.
   *
   * @param component Component to test.
   * @param message   Error message to display.
   */
  public static void assertNotVisible(String message, Component component) {
    assertFalse(message, component.isVisible());
  }

  /**
   * Asserts that the given component is not visible.
   *
   * @param component Component to test.
   */
  public static void assertNotVisible(Component component) {
    assertFalse("Component should not be visible", component.isVisible());
  }

  /**
   * Asserts that the given label is not null
   * and its text string is blank (null or empty).
   *
   * @param message Error message to display.
   * @param label   Label to test.
   */
  public static void assertTextBlank(String message, JLabel label) {
    assertNotNull("Label should not be null", label);
    assertTrue(message, StringUtils.isBlank(label.getText()));
  }

  /**
   * Asserts that the given label is not null
   * and its text string is not blank (not null and not empty).
   *
   * @param message Error message to display.
   * @param label   Label to test.
   */
  public static void assertTextNotBlank(String message, JLabel label) {
    assertNotNull("Label should not be null", label);
    assertTrue(message, StringUtils.isNotBlank(label.getText()));
  }

  /**
   * Sets all JTextField text fileds with the given value.
   *
   * @param fields Array of text fields.
   * @param text   Text to set.
   */
  protected static void setTextFields(Collection<JTextField> fields, String... text) {
    int curr = 0;
    for (JTextField field : fields) {
      field.setText(text[curr]);
      if (curr + 1 < text.length) {
        ++curr;
      }
    }
  }

  /**
   * Sets all JTextField text fileds with the given value.
   *
   * @param comps Array of components.
   * @param text  Text to set.
   */
  protected static void setTextFields(Component[] comps, String... text) {
    int curr = 0;
    for (Component comp : comps) {
      if (comp instanceof JTextField) {
        ((JTextField) comp).setText(text[curr]);
        if (curr + 1 < text.length) {
          ++curr;
        }
      }
    }
  }

  /**
   * Creates player statistics object and initializes it
   * with the passed data.
   *
   * @param rBean           Game result bean to set.
   * @param numberOfPlayers number of players in the game.
   * @param place           Player's place.
   * @param name            Player name.
   * @param pool            Player pool value.
   * @param mountain        Player mountain.
   * @param whistData       whists against other players.
   * @return Created and initialized player statistics object.
   */
  protected static PlayerStatistics createPlayerStatHelper(GameResultBean rBean,
                                                           int numberOfPlayers,
                                                           Place place,
                                                           String name,
                                                           int pool,
                                                           int mountain,
                                                           Whist... whistData) {
    // creating whist map
    Map<Place, String> whists = new HashMap<Place, String>();
    for (Whist whist : whistData) {
      whists.put(whist.place, whist.whist);
    }

    PlayerStatistics player = new PlayerStatistics(rBean, place);
    player.setPlayerName(name);
    player.setPool(pool);
    player.setMountain(mountain);
    for (Place other : Place.getOtherPlayersWhistPlaces(place, numberOfPlayers)) {
      player.setWhistsForPlayerFromField(other, new JTextField(whists.get(other)));
    }

    return player;
  }

  /** Class to represent whist against one player. */
  protected static class Whist {

    /** Player against whist this whist. */
    public final Place place;

    /** Whist value as a string. */
    public final String whist;

    /**
     * Creates a new <code>Whist</code> object.
     *
     * @param place player place.
     * @param whist whist value.
     */
    private Whist(Place place, String whist) {
      this.place = place;
      this.whist = whist;
    }

    /**
     * Creates a new <code>Whist</code> object.
     *
     * @param place player place.
     * @param whist whist value.
     * @return created <code>Whist</code> object.
     */
    public static Whist n(Place place, String whist) {
      return new Whist(place, whist);
    }

  }

  /** Method to initialize logging. It will only get executed once. */
  private synchronized static void initLogging() {
    if (loggingInitialized == false) {
      // setting test logger
      try {
        Handler fh = new FileHandler(TEST_LOG_PATH);
        fh.setFormatter(new SimpleFormatter());
        Logger.getLogger("").addHandler(fh);
        Logger.getLogger("").setLevel(TEST_LOG_LEVEL);

      } catch (IOException e) {
        System.out.println("ERROR setting test logger!");
        e.printStackTrace();
      }
      loggingInitialized = true;
    }
  }

}
