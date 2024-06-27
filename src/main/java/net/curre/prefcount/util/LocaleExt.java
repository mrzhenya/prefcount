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

import javax.swing.ImageIcon;
import java.util.Locale;
import java.util.ResourceBundle;

import net.curre.prefcount.service.UiService;

/**
 * Object of this class represents a locale
 * with extended functionality/properties.
 * <p/>
 * Created date: Jun 2, 2007
 *
 * @author Yevgeny Nyden
 */
public class LocaleExt {

  /** Reference to the locale object. */
  private final Locale locale;

  /** Language display name. */
  private final String displayLanguage;

  /** Icon to represent this locale. */
  private final ImageIcon localeIcon;

  /**
   * Constructor.
   *
   * @param language        Locale language.
   * @param country         Locale country.
   * @param displayLanguage Language display name.
   */
  public LocaleExt(String language,
                   String country,
                   String displayLanguage) {
    this.locale = new Locale(language, country);
    this.displayLanguage = displayLanguage;
    this.localeIcon = UiService.createImage(language);
  }

  /**
   * Getter for the reference to the locale object.
   *
   * @return Reference to the locale object.
   */
  public Locale getLocale() {
    return this.locale;
  }

  /**
   * Getter for the language display name.
   *
   * @return Language display name.
   */
  public String getDisplayLanguage() {
    return this.displayLanguage;
  }

  /**
   * Gets the icon that represents this locale.
   *
   * @return Icon that represents this locale.
   */
  public ImageIcon getLocaleIcon() {
    return this.localeIcon;
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final LocaleExt localeExt = (LocaleExt) o;
    return locale.equals(localeExt.locale);
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return locale.hashCode();
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return this.displayLanguage;
  }

  /**
   * Gets the string resource for the given key. If any keyArgs
   * are passed, they will replace the "{0}", "{1}", etc. string
   * sequences in this resource (if they exist) in the order
   * they appear on the argument list. Note, that only maximum of
   * 10 arguments are supported (indexes 0 through 9).
   *
   * @param key     key for a resource to fetch.
   * @param keyArgs array of arguments for the resource key (optional).
   * @return string resource for the given key.
   */
  public static String getString(String key, String... keyArgs) {
    String str = ResourceBundle.getBundle("default").getString(key);
    if (keyArgs != null) {
      int argIndex = 0;
      StringBuilder buffer = new StringBuilder(str);
      for (int index = 0; index < keyArgs.length; ++index) {
        String currArg = "{" + index + "}";
        argIndex = buffer.indexOf(currArg, argIndex);
        if (argIndex >= 0) {
          buffer.replace(argIndex, argIndex + 3, keyArgs[index]);
        }
      }
      return buffer.toString();
    }
    return str;
  }
}
