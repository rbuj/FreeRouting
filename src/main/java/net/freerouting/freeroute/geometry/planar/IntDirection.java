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
 * IntDirection.java
 *
 * Created on 3. Februar 2003, 08:17
 */
package net.freerouting.freeroute.geometry.planar;

import net.freerouting.freeroute.datastructures.Signum;

/**
 *
 * Implements an abstract class Direction as an equivalence class of
 * IntVector's.
 *
 *
 * @author Alfons Wirtz
 */
@SuppressWarnings("serial")
public class IntDirection implements Direction {

    public final int x;
    public final int y;

    IntDirection(int p_x, int p_y) {
        x = p_x;
        y = p_y;
    }

    @Override
    public boolean is_orthogonal() {
        return (x == 0 || y == 0);
    }

    @Override
    public boolean is_diagonal() {
        return (Math.abs(x) == Math.abs(y));
    }

    @Override
    public Vector get_vector() {
        return new IntVector(x, y);
    }

    @Override
    public int compareTo(IntDirection p_other) {
        if (y > 0) {
            if (p_other.y < 0) {
                return -1;
            }
            if (p_other.y == 0) {
                if (p_other.x > 0) {
                    return 1;
                }
                return -1;
            }
        } else if (y < 0) {
            if (p_other.y >= 0) {
                return 1;
            }
        } else // y == 0
        {
            if (x > 0) {
                if (p_other.y != 0 || p_other.x < 0) {
                    return -1;
                }
                return 0;
            }
            // x < 0
            if (p_other.y > 0 || p_other.y == 0 && p_other.x > 0) {
                return 1;
            }
            if (p_other.y < 0) {
                return -1;
            }
            return 0;
        }

        // now this direction and p_other are located in the same
        // open horizontal half plane
        double determinant = (double) p_other.x * y - (double) p_other.y * x;
        return Signum.as_int(determinant);
    }

    @Override
    public Direction opposite() {
        return new IntDirection(-x, -y);
    }

    @Override
    public Direction turn_45_degree(int p_factor) {
        int n = p_factor % 8;
        int new_x;
        int new_y;
        switch (n) {
            case 0: // 0 degree
                new_x = x;
                new_y = y;
                break;
            case 1: // 45 degree
                new_x = x - y;
                new_y = x + y;
                break;
            case 2: // 90 degree
                new_x = -y;
                new_y = x;
                break;
            case 3: // 135 degree
                new_x = -x - y;
                new_y = x - y;
                break;
            case 4: // 180 degree
                new_x = -x;
                new_y = -y;
                break;
            case 5: // 225 degree
                new_x = y - x;
                new_y = -x - y;
                break;
            case 6: // 270 degree
                new_x = y;
                new_y = -x;
                break;
            case 7: // 315 degree
                new_x = x + y;
                new_y = y - x;
                break;
            default:
                new_x = 0;
                new_y = 0;
        }
        return new IntDirection(new_x, new_y);
    }

    @Override
    public int compareTo(BigIntDirection p_other) {
        return -(p_other.compareTo(this));
    }

    double determinant(IntDirection p_other) {
        return (double) x * p_other.y - (double) y * p_other.x;
    }

}
