/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as 
 * published by the Free Software Foundation;
 */

package net.curre.prefcount.gui.type;

/**
 * This enum represents player's place.
 * <p/>
 * Created date: May 28, 2008
 *
 * @author Yevgeny Nyden
 */
public enum Place {

  /** East side player. */
  EAST(0, "pref.scoreboard.eastLetter", "pref.dialog.east"),

  /** South side player. */
  SOUTH(1, "pref.scoreboard.southLetter", "pref.dialog.south"),

  /** West side player. */
  WEST(2, "pref.scoreboard.westLetter", "pref.dialog.west"),

  /** North side player. */
  NORTH(3, "pref.scoreboard.northLetter", "pref.dialog.north");

  /** Place's short string resource key. */
  public final String shortKey;

  /** Place's long string resource key. */
  public final String longKey;

  /** Index of this place relative to other places. */
  public final int index;

  /** Three player game places. */
  public static final Place[] THREE_PLAYERS = {EAST, SOUTH, WEST};

  /** Four player game places. */
  public static final Place[] FOUR_PLAYERS = {NORTH, EAST, SOUTH, WEST};

  /**
   * Constructor.
   *
   * @param index    index of this place relative to other places.
   * @param shortKey place's short string resource key.
   * @param longKey  place's long string resource key.
   */
  private Place(int index, String shortKey, String longKey) {
    this.shortKey = shortKey;
    this.index = index;
    this.longKey = longKey;
  }

  /**
   * Given number of player, returns array of places
   * for this game.
   *
   * @param playerNum number of player in the game.
   * @return array of places.
   */
  public static Place[] getPlaces(int playerNum) {
    if (playerNum == 3) {
      return THREE_PLAYERS;
    } else if (playerNum == 4) {
      return FOUR_PLAYERS;
    } else {
      throw new IllegalArgumentException("Only 3 and 4 players supported!");
    }
  }

  /**
   * Fetches a place for the given index.
   *
   * @param index place's index.
   * @return place for the given index.
   */
  public static Place getPlaceForIndex(int index) {
    switch (index) {
      case 0:
        return EAST;

      case 1:
        return SOUTH;

      case 2:
        return WEST;

      case 3:
        return NORTH;

      default:
        throw new IllegalArgumentException("Unable to determine place for index " + index + "!");
    }
  }

  /**
   * Gets all whist places that the given player.
   *
   * @param place      player's place.
   * @param numPlayers total number of player in the game.
   * @return whist whist places for all opponents of the given player.
   */
  public static Place[] getOtherPlayersWhistPlaces(Place place, int numPlayers) {
    Place[] places = new Place[numPlayers - 1];
    switch (place) {
      case EAST:
        places[0] = SOUTH;
        places[1] = WEST;
        break;

      case SOUTH:
        places[0] = EAST;
        places[1] = WEST;
        break;

      case WEST:
        places[0] = EAST;
        places[1] = SOUTH;
        break;

      case NORTH:
        places[0] = EAST;
        places[1] = SOUTH;
        places[2] = WEST;
        break;

      default:
        throw new IllegalArgumentException("Illegal place " + place + "!");
    }
    if (numPlayers == 4 && place != NORTH) {
      places[2] = NORTH;
    }

    return places;
  }

}
