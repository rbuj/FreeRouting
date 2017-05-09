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
 * SnapAngle.java
 *
 * Created on 14. Juli 2003, 07:40
 */
package net.freerouting.freeroute.board;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum for angle restrictions none, fortyfive degree and ninety degree.
 *
 * @author Alfons Wirtz
 */
public enum AngleRestriction {

    NONE(Constants.NONE, 0),
    FORTYFIVE_DEGREE(Constants.FORTYFIVE_DEGREE, 1),
    NINETY_DEGREE(Constants.NINETY_DEGREE, 2);

    private final String name;
    private final int no;

    /**
     * Creates a new instance of SnapAngle
     */
    private AngleRestriction(String p_name, int p_no) {
        name = p_name;
        no = p_no;
    }

    // Reverse-lookup map for getting a unit from an int
    public static final Map<Integer, AngleRestriction> lookup = new HashMap<Integer, AngleRestriction>();
    static {
        for (AngleRestriction angleRestriction : AngleRestriction.values()) {
            lookup.put(angleRestriction.get_no(), angleRestriction);
        }
    }

    /**
     * Returns the string of this instance
     */
    public String to_string() {
        return name;
    }

    /**
     * Returns the number of this instance
     */
    public int get_no() {
        return no;
    }

    private class Constants {
        public static final String NONE = "none";
        public static final String FORTYFIVE_DEGREE = "45 degree";
        public static final String NINETY_DEGREE = "90 degree";
    }
}
