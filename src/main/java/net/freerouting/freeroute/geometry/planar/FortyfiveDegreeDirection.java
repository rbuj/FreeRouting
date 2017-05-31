/*
 *  Copyright (C) 2014  Alfons Wirtz  
 *   website www.freerouting.net
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License at <http://www.gnu.org/licenses/> 
 *   for more details.
 *
 * FortyfiveDegreeDirections.java
 *
 * Created on 11. Dezember 2005, 07:18
 *
 */
package net.freerouting.freeroute.geometry.planar;

@SuppressWarnings("all") // Eclipse regards get_direction() as unused

/**
 * Enum for the eight 45-degree direction starting from right in
 * counterclocksense to down45.
 *
 * @author alfons
 */
public enum FortyfiveDegreeDirection {
    RIGHT(Constants.RIGHT),
    RIGHT45(Constants.RIGHT45),
    UP(Constants.UP),
    UP45(Constants.UP45),
    LEFT(Constants.LEFT),
    LEFT45(Constants.LEFT45),
    DOWN(Constants.DOWN),
    DOWN45(Constants.DOWN45);

    IntDirection direction;

    FortyfiveDegreeDirection(IntDirection intDirection) {
        direction = intDirection;
    }

    public IntDirection get_direction() {
        return direction;
    }

    private static class Constants {

        /**
         * the direction to the east
         */
        public static final IntDirection RIGHT = new IntDirection(1, 0);
        /**
         * the direction to the northeast
         */
        public static final IntDirection RIGHT45 = new IntDirection(1, 1);
        /**
         * the direction to the north
         */
        public static final IntDirection UP = new IntDirection(0, 1);
        /**
         * the direction to the northwest
         */
        public static final IntDirection UP45 = new IntDirection(-1, 1);
        /**
         * the direction to the west
         */
        public static final IntDirection LEFT = new IntDirection(-1, 0);
        /**
         * the direction to the southwest
         */
        public static final IntDirection LEFT45 = new IntDirection(-1, -1);
        /**
         * the direction to the south
         */
        public static final IntDirection DOWN = new IntDirection(0, -1);
        /**
         * the direction to the southeast
         */
        public static final IntDirection DOWN45 = new IntDirection(1, -1);

        private Constants() {
            // not called
        }
    }
}
