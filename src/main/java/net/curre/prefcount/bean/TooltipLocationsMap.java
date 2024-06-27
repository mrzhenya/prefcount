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

package net.curre.prefcount.bean;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

import net.curre.prefcount.gui.type.Place;
import net.curre.prefcount.gui.type.ScoreItem;

import org.apache.commons.lang3.StringUtils;

/**
 * This is a map to hold the score item locations (shapes)
 * for the tooltip logic.
 * <p/>
 * Created date: Jun 25, 2008
 *
 * @author Yevgeny Nyden
 */
public class TooltipLocationsMap extends HashMap<ScoreItem, Map<Place, Shape>> {

  /** {@inheritDoc} */
  @Override
  public Map<Place, Shape> get(Object item) {
    if (!(item instanceof ScoreItem)) {
      throw new IllegalArgumentException("Key is not an instance of " + ScoreItem.class.getName());
    }

    Map<Place, Shape> sMap = super.get(item);
    if (sMap == null) {
      sMap = new HashMap<>();
      super.put((ScoreItem) item, sMap);
    }

    return sMap;
  }

  /**
   * Clears a location for the given place and items.
   *
   * @param place player's place.
   * @param items score items.
   */
  public void removeLocation(Place place, ScoreItem... items) {
    for (ScoreItem item : items) {
      this.get(item).remove(place);
    }
  }

  /**
   * Adds a shape that represents a tooltip area to the map.
   *
   * @param item  score board item.
   * @param place player's place.
   * @param shape shape to add.
   */
  public void addShapeLocation(ScoreItem item, Place place, Shape shape) {
    Map<Place, Shape> shapesMap = this.get(item);
    shapesMap.put(place, shape);
  }

  /**
   * Helper method to compute and add a tooltip location
   * to the tooltip location map for the given data. Note,
   * that this method adds rectangular tooltip bounds.
   *
   * @param item  score board item.
   * @param place player's place.
   * @param g2    graphics context.
   * @param point point where the string is drawn.
   * @param str   string for which the tooltip location is recorded.
   */
  public void addRectangleLocation(ScoreItem item, Place place, Graphics2D g2,
                                   Point2D.Double point, String str) {
    Map<Place, Shape> shapesMap = this.get(item);
    if (StringUtils.isNotBlank(str)) {
      shapesMap.put(place, getRectangleBounds(g2, point, str));
    } else {
      shapesMap.remove(place);
    }
  }

  /**
   * Computes the tooltip bounding rectangle for the passed String
   * using the passed graphics context.
   *
   * @param g2    Graphics object to use.
   * @param point point at which the string is drawn.
   * @param str   string to be measured.
   * @return The rectangle that represents the tooltip bounds of the passed string.
   */
  private static Rectangle2D.Double getRectangleBounds(Graphics2D g2, Point2D.Double point, String str) {
    FontMetrics metrics = g2.getFontMetrics(g2.getFont());
    final double width = metrics.stringWidth(str);
    final double height = metrics.getHeight() - 3D;
    final double x = point.getX();
    final double y = point.getY() - height + 3D;
    return new Rectangle2D.Double(x, y, width, height);
  }
}
