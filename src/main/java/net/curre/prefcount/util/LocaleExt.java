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
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import java.awt.CheckboxMenuItem;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import net.curre.prefcount.gui.type.WindowComponent;

import org.apache.commons.lang3.StringUtils;

/**
 * Object of this class represents locale
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

  /** Set for holding components and their resource keys. */
  private static final Set<Triple> resourceKeysSet = new HashSet<>();

  /** List of input maps and their shortcut resource keys. */
  private static final List<Object[]> inputMapList = new ArrayList<>();

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
    this.localeIcon = Utilities.createImage(language);
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

  /**
   * Registers a "locale-sensitive" component so
   * it's label is updated when the application locale changes.
   *
   * @param component reference to the component.
   * @param keyEnum   WindowComponent enum that represents the component.
   * @param keyArgs   array of arguments for the resource key (optional).
   */
  public static void registerComponent(Object component, WindowComponent keyEnum, String... keyArgs) {
    if (keyEnum == null) {
      throw new IllegalArgumentException("keyEnum is required!");
    }
    if (component == null) {
      throw new IllegalArgumentException("Component is required!");
    }

    final Triple triple = new Triple(component, keyEnum, keyArgs);
    if (!resourceKeysSet.add(triple)) {
      throw new IllegalArgumentException("Given component already exists!");
    }
  }

  /**
   * Registers a "locale-sensitive" component so
   * it's label is updated when the application locale changes.
   *
   * @param component reference to the component.
   * @param key       string resource key for the component.
   * @param keyArgs   array of arguments for the resource key (optional).
   */
  public static void registerComponent(Object component, String key, String... keyArgs) {
    if (key == null) {
      throw new IllegalArgumentException("Key is required!");
    }
    if (component == null) {
      throw new IllegalArgumentException("Component is required!");
    }

    final Triple triple = new Triple(component, key, keyArgs);
    if (!resourceKeysSet.add(triple)) {
      throw new IllegalArgumentException("Given component already exists!");
    }
  }

  /**
   * Reregisters a "locale-sensitive" component so
   * it's label is updated when the application locale changes.
   *
   * @param component reference to the component.
   * @param key       string resource key for the component.
   * @param keyArgs   array of arguments for the resource key (optional).
   */
  public static void reregisterComponent(Object component, String key, String... keyArgs) {
    if (key == null) {
      throw new IllegalArgumentException("Key is required!");
    }
    if (component == null) {
      throw new IllegalArgumentException("Component is required!");
    }

    final Triple triple = new Triple(component, key, keyArgs);
    if (resourceKeysSet.remove(triple)) {
      // need to remove and add because just adding will not
      // (may not) replace the already existing element in the set 
      resourceKeysSet.add(triple);
    } else {
      throw new IllegalArgumentException("Component is not found, maybe you want to register it first.");
    }
  }

  /** Unregisters all currently registered components. */
  public static void unregisterAllComponents() {
    resourceKeysSet.clear();
    inputMapList.clear();
  }

  /**
   * Unregisters locale sensitive components.
   *
   * @param components components to unregister.
   */
  public static void unregisterComponents(Collection<Component> components) {
    for (Component c : components) {
      if (resourceKeysSet.remove(new Triple(c, "")) == false) {
        throw new RuntimeException("Component " + c + " is not found!");
      }
    }
  }

  /**
   * Unregisters locale sensitive component.
   *
   * @param component component to unregister.
   */
  public static void unregisterComponent(Component component) {
    if (resourceKeysSet.remove(new Triple(component, "")) == false) {
      throw new RuntimeException("Component " + component + " is not found!");
    }
  }

  /**
   * Updates the text/label/title on all registered components
   * when the application locale changes.
   */
  @SuppressWarnings("unchecked")
  public static void fireLocaleChangeEvent() {
    for (Triple triple : resourceKeysSet) {
      final Object component = triple.component;

      if (component instanceof LocaleExec) {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            ((LocaleExec) component).doChange();
          }
        });

      } else if (component instanceof JRadioButton) {
        ((JRadioButton) component).setText(getButtonText(triple));

      } else if (component instanceof JLabel) {
        ((JLabel) component).setText(getText(triple));

      } else if (component instanceof JButton) {
        ((JButton) component).setText(getButtonText(triple));

      } else if (component instanceof JRadioButtonMenuItem) {
        ((JRadioButtonMenuItem) component).setText(getMenuItemText(triple));

      } else if (component instanceof Frame) {
        ((Frame) component).setTitle(getText(triple));

      } else if (component instanceof TitledBorder) {
        ((TitledBorder) component).setTitle(getText(triple));

      } else if (component instanceof JMenuItem) {
        ((JMenuItem) component).setText(getText(triple));

      } else if (component instanceof Menu) {
        ((Menu) component).setLabel(getText(triple));

      } else if (component instanceof MenuItem) {
        ((MenuItem) component).setLabel(getText(triple));

      } else if (component instanceof CheckboxMenuItem) {
        ((CheckboxMenuItem) component).setLabel(getText(triple));

      } else {
        throw new UnsupportedOperationException(
            "Component class " + triple.component.getClass() + " is not supported!");
      }

      // resetting the menu item accelerator key
      if (triple.keyEnum != null && triple.keyEnum.getShortcutKey() != null) {
        if (component instanceof JMenuItem) {
          ((JMenuItem) component).setAccelerator(KeyStroke.getKeyStroke(
              LocaleExt.getString(triple.keyEnum.getShortcutKey()).charAt(0), ActionEvent.CTRL_MASK));

        } else if (component instanceof CheckboxMenuItem) {
          ((CheckboxMenuItem) component).setShortcut(new MenuShortcut(
              LocaleExt.getString(triple.keyEnum.getShortcutKey()).charAt(0), false));

        } else if (component instanceof MenuItem) {
          ((MenuItem) component).setShortcut(new MenuShortcut(
              LocaleExt.getString(triple.keyEnum.getShortcutKey()).charAt(0), false));
        }
      }

      // resetting the item tooltip
      if (triple.keyEnum != null && triple.keyEnum.tooltipKey != null &&
          component instanceof JComponent &&
          StringUtils.isNotBlank(((JComponent) component).getToolTipText())) {
        ((JComponent) component).setToolTipText(LocaleExt.getString(triple.keyEnum.tooltipKey));
      }
    }

    // refreshing shorcuts
    for (Object[] currKeys : inputMapList) {
      InputMap map = (InputMap) currKeys[0];
      map.clear();
      for (String key : (List<String>) currKeys[1]) {
        String shortcut = LocaleExt.getString(key);
        map.put(KeyStroke.getKeyStroke("control " + shortcut), key);
      }
    }
  }

  /**
   * This method will register shortcuts with the given input map.
   * We support only Ctrl+ shortcuts here and assume that the shotcut
   * action has been added to the same component's action map using
   * the shortcut resource key as a map key.
   *
   * @param map          component's input map to add shortcuts to.
   * @param shortcutKeys array of shortcuts' resource keys.
   */
  @SuppressWarnings("unchecked")
  public static void registerShortcuts(InputMap map, String... shortcutKeys) {
    Object[] currKeys = null;
    for (Object[] anInputMapList : inputMapList) {
      if (anInputMapList[0] == map) {
        currKeys = anInputMapList;
        break;
      }
    }
    if (currKeys == null) {
      currKeys = new Object[]{map, new ArrayList<String>()};
      inputMapList.add(currKeys);
    }
    ((List<String>) currKeys[1]).addAll(Arrays.asList(shortcutKeys));
  }

  /** Helper interface to assist with changing locale-sensitive messages. */
  public interface LocaleExec {

    /** Performs change of locale. */
    void doChange();
  }

  /**
   * Gets the text using the current locale
   * from the component's triple (that encapsulates the resource data).
   *
   * @param triple component's <code>Triple</code> object to use.
   * @return component text for the current locale.
   */
  private static String getText(Triple triple) {
    String label;
    if (triple.keyEnum != null) {
      label = getString(triple.keyEnum.getTextKey(), triple.keyArgs);
    } else {
      label = getString(triple.keyStr, triple.keyArgs);
    }
    return label;
  }

  /**
   * Gets the text using the current locale
   * from the component's triple (that encapsulates the resource data).
   * This method will use the triple.keyEnum's generateButtonText()
   * method if the triple.keyEnum is set.
   *
   * @param triple component's <code>Triple</code> object to use.
   * @return component text for the current locale.
   */
  private static String getButtonText(Triple triple) {
    String label;
    if (triple.keyEnum != null) {
      label = Utilities.generateButtonText(triple.keyEnum);
    } else {
      label = getString(triple.keyStr, triple.keyArgs);
    }
    return label;
  }

  /**
   * Gets the text using the current locale
   * from the component's triple (that encapsulates the resource data).
   * This method will use the triple.keyEnum's generateMenuItemText()
   * method if the triple.keyEnum is set.
   *
   * @param triple component's <code>Triple</code> object to use.
   * @return component text for the current locale.
   */
  private static String getMenuItemText(Triple triple) {
    String label;
    if (triple.keyEnum != null) {
      label = LocaleExt.getString(triple.keyEnum.getTextKey());
    } else {
      label = getString(triple.keyStr, triple.keyArgs);
    }
    return label;
  }

  /**
   * Class to represent a "locale-sensitive" component with its key.
   * Even though at least one of keyStr or keyEnum is required, a
   * triple is considered to be unique if it's component reference is
   * unique. Components are compared using their references' values
   * (basically, simply by obj1 == obj2).
   */
  public static class Triple {

    /** Component reference. */
    final Object component;

    /** Component enum. */
    final WindowComponent keyEnum;

    /** Component resource key. */
    final String keyStr;

    /** Array of arguments for the resource key. */
    final String[] keyArgs;

    /**
     * Constructs a new Triple.
     *
     * @param component reference to the component (required).
     * @param keyEnum   component's enum (required).
     * @param keyArgs   array of arguments for the resource key (optional).
     */
    private Triple(Object component, WindowComponent keyEnum, String... keyArgs) {
      if (keyEnum == null) {
        throw new IllegalArgumentException("keyEnum is required!");
      }
      if (component == null) {
        throw new IllegalArgumentException("component is required!");
      }
      this.keyEnum = keyEnum;
      this.keyStr = null;
      this.component = component;
      this.keyArgs = keyArgs;
    }

    /**
     * Constructs a new Triple.
     *
     * @param component reference to the component (required).
     * @param keyStr    component's resource key (required).
     * @param keyArgs   array of arguments for the resource key (optional).
     */
    private Triple(Object component, String keyStr, String... keyArgs) {
      if (keyStr == null) {
        throw new IllegalArgumentException("keyStr is required!");
      }
      if (component == null) {
        throw new IllegalArgumentException("component is required!");
      }
      this.keyEnum = null;
      this.keyStr = keyStr;
      this.component = component;
      this.keyArgs = keyArgs;
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

      return this.component == ((Triple) o).component;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
      return component.hashCode();
    }
  }
}
