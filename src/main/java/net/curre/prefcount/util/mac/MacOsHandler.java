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

package net.curre.prefcount.util.mac;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.curre.prefcount.PrefCountRegistry;
import net.curre.prefcount.service.MainService;

/**
 * This is a macOS helper class to assist with handling
 * of MACOS events using the <code>com.apple.eawt.Application</code>
 * class. This class will not work on a platform other than macOS.
 * <p/>
 * It's important to note, that in this class, we use classes
 * from the <code>com.apple.eawt</code> package, but since no such
 * package exist on a non-Mac platform, we must use reflection API
 * to create objects and invoke methods of those Mac-specific classes.
 * <p/>
 * Created date: Jan 22, 2008
 *
 * @author Yevgeny Nyden
 */
public class MacOsHandler {

  /** Private class logger. */
  private static final Logger log = Logger.getLogger(MacOsHandler.class.toString());

  /** Flag to indicate that this handler has been initialized. */
  private static boolean isInitialized = false;

  /**
   * Method to initialized macOS application handler, which functionality
   * is based upon the <code>com.apple.eawt.Application</code> class. Here
   * we add custom "about" and "quit" handlers to the Mac application menu bar.
   * <br /><br />
   * This method should be called once per application run - successive calls
   * will have no effect.
   */
  public static synchronized void initializeMacOsHandler() {
    if (isInitialized) {
      return;
    }
    try {
      // creating an Application object
      Class<?> appClass = Class.forName("com.apple.eawt.Application");
      Object application = appClass.newInstance();

      // getting the Application#addApplicationListener() method
      Class<?> listClass = Class.forName("com.apple.eawt.ApplicationListener");
      Method addAppListmethod = appClass.getDeclaredMethod("addApplicationListener", listClass);

      // creating and adding a custom adapter/listener to the Application
      Class<?> adapterClass = Class.forName("com.apple.eawt.ApplicationAdapter");
      Object listener = ListenerProxy.newInstance(adapterClass.newInstance());
      addAppListmethod.invoke(application, listener);

      isInitialized = true;

    } catch (Exception e) {
      log.log(Level.WARNING, "Exception is thrown when using reflection API on the classes " +
          "of the com.apple.eawt package! Are we on Mac OS? Exception: ", e);
    }
  }
}

/**
 * Class to assist with intercepting calls to
 * the handleAbout() and handleQuit() methods of the
 * <code>com.apple.eawt.ApplicationAdapter</code> class and
 * triggering appropriate 'about' and 'quit' actions for
 * these events.
 */
class ListenerProxy implements InvocationHandler {

  /** Private class logger. */
  private static final Logger log = Logger.getLogger(MacOsHandler.class.toString());

  /** Reference to the proxied object. */
  private final Object object;

  /**
   * Method to create a new proxy for the given object.
   *
   * @param obj Object to proxy.
   * @return Reference to the new proxy.
   */
  public static Object newInstance(Object obj) {
    return java.lang.reflect.Proxy.newProxyInstance(
        obj.getClass().getClassLoader(),
        obj.getClass().getInterfaces(),
        new ListenerProxy(obj));
  }

  /**
   * Constructor that sets the reference to the proxied object.
   *
   * @param obj Reference to the proxied object to set.
   */
  private ListenerProxy(Object obj) {
    this.object = obj;
  }

  /**
   * <p/>
   * Triggers appropriate events for the "handleAbout" and "handleQuit"
   * methods. Executes default (proxied) code for other methods.
   * </p>
   * {@inheritDoc}
   */
  public Object invoke(Object proxy, Method m, Object[] args) {
    Object result = null;
    try {
      if ("handleAbout".equals(m.getName())) {
        // handling about action
        log.fine("Processing handleAbout() method...");
        PrefCountRegistry.getInstance().getMainWindow().showAboutInfo();
        Object event = args[0];
        Method eventSetter = Class.forName("com.apple.eawt.ApplicationEvent").
            getDeclaredMethod("setHandled", Boolean.TYPE);
        eventSetter.invoke(event, true);

      } else if ("handleQuit".equals(m.getName())) {
        // handling quit action
        log.fine("Processing handleQuit() method...");
        MainService.quitApp();

      } else {
        // for now, we don't care about other methods
        result = m.invoke(object, args);
      }
    } catch (Exception e) {
      log.log(Level.WARNING, "Unexpected invocation exception!", e);
    }
    return result;
  }
}
