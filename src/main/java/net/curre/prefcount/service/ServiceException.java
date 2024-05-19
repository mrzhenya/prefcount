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

/**
 * Object of this class represents a service exception.
 * <p/>
 * Created date: May 13, 2007
 *
 * @author Yevgeny Nyden
 */
public class ServiceException extends Exception {

  /**
   * Constructor that sets exception message.
   *
   * @param message Exception message to set.
   */
  public ServiceException(String message) {
    super(message);
  }

  /**
   * Constructor that sets exception message
   * and another exception to wrap.
   *
   * @param message Exception message to set.
   * @param e       Exception to wrap.
   */
  public ServiceException(String message, Exception e) {
    super(message, e);
  }
}
