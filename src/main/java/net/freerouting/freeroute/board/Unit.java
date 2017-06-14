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
 * Unit.java
 *
 * Created on 13. Dezember 2004, 08:01
 */
package net.freerouting.freeroute.board;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum for the userunits inch, mil or millimeter.
 *
 * @author Alfons Wirtz
 */
public enum Unit {
    MIL("mil"),
    INCH("inch"),
    MM("mm"),
    UM("um");

    private final String name;
    public static final int size = 4;

    Unit(String s) {
        name = s;
    }

    @Override
    public String toString() {
        return name;
    }

    // Reverse-lookup map for getting a unit from a string
    private static final Map<String, Unit> lookup = new HashMap<String, Unit>(size);

    static {
        for (Unit u : Unit.values()) {
            lookup.put(u.toString(), u);
        }
    }

    /**
     * Scales p_value from p_from_unit to p_to_unit
     */
    public static double scale(double p_value, Unit p_from_unit, Unit p_to_unit) {
        double result = 0;
        if (p_from_unit == p_to_unit) {
            result = p_value;
        } else if (p_from_unit == INCH) {
            if (null != p_to_unit) {
                switch (p_to_unit) {
                    case MIL:
                        result = p_value * 1000.0;
                        break;
                    case MM:
                        result = p_value * INCH_TO_MM;
                        break;
                    // um
                    default:
                        result = p_value * INCH_TO_MM * 1000.0;
                        break;
                }
            }
        } else if (p_from_unit == MIL) {
            if (null != p_to_unit) {
                switch (p_to_unit) {
                    case INCH:
                        result = p_value / 1000.0;
                        break;
                    case MM:
                        result = p_value * INCH_TO_MM;
                        break;
                    // um
                    default:
                        result = (p_value * INCH_TO_MM) * 1000.0;
                        break;
                }
            }
        } else if (p_from_unit == MM) {
            if (null != p_to_unit) {
                switch (p_to_unit) {
                    case INCH:
                        result = p_value / INCH_TO_MM;
                        break;
                    case UM:
                        result = p_value * 1_000;
                        break;
                    // mil
                    default:
                        result = (p_value * 1000.0) / INCH_TO_MM;
                        break;
                }
            }
        } else //UM
        {
            if (null != p_to_unit) {
                switch (p_to_unit) {
                    case INCH:
                        result = p_value / (INCH_TO_MM * 1000.0);
                        break;
                    case MM:
                        result = p_value / 1000.0;
                        break;
                    // mil
                    default:
                        result = p_value / INCH_TO_MM;
                        break;
                }
            }
        }
        return result;
    }

    /**
     * Return the unit corresponding to the input string, or null, if the input
     * string is different from mil, inch and mm.
     */
    public static Unit from_string(String p_string) {
        return lookup.get(p_string);
    }

    public static final double INCH_TO_MM = 25.4;
}
