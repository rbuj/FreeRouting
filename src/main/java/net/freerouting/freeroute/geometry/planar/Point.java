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
 * Point.java
 *
 * Created on 1. Februar 2003, 11:38
 */
package net.freerouting.freeroute.geometry.planar;

/**
 * Abstract class describing functionality for Points in the plane.
 *
 * @author Alfons Wirtz
 */
@SuppressWarnings("serial")
public abstract class Point implements java.io.Serializable {

    /**
     * returns the translation of this point by p_vector
     */
    public abstract Point translate_by(Vector p_vector);

    /**
     * returns the difference vector of this point and p_other
     */
    public abstract Vector difference_by(Point p_other);

    /**
     * approximates the coordinates of this point by float coordinates
     */
    public abstract FloatPoint to_float();

    /**
     * returns true, if this Point is a RationalPoint with denominator z = 0.
     */
    public abstract boolean is_infinite();

    /**
     * creates the smallest Box with integer coordinates containing this point.
     */
    public abstract IntBox surrounding_box();

    /**
     * creates the smallest Octagon with integer coordinates containing this
     * point.
     */
    public abstract IntOctagon surrounding_octagon();

    /**
     * Returns true, if this point lies in the interiour or on the border of
     * p_box.
     */
    public abstract boolean is_contained_in(IntBox p_box);

    public abstract Side side_of(Line p_line);

    /**
     * returns the nearest point to this point on p_line
     */
    public abstract Point perpendicular_projection(Line p_line);

    /**
     * The function returns Side.ON_THE_LEFT, if this Point is on the left of
     * the line from p_1 to p_2; Side.ON_THE_RIGHT, if this Point is on the
     * right of the line from p_1 to p_2; and Side.COLLINEAR, if this Point is
     * collinear with p_1 and p_2.
     */
    public Side side_of(Point p_1, Point p_2) {
        Vector v1 = difference_by(p_1);
        Vector v2 = p_2.difference_by(p_1);
        return v1.side_of(v2);
    }

    /**
     * Calculates the perpendicular direction froma this point to p_line.
     * Returns Direction.NULL, if this point lies on p_line.
     */
    public Direction perpendicular_direction(Line p_line) {
        Side side = this.side_of(p_line);
        if (side == Side.COLLINEAR) {
            return DirectionUtils.NULL;
        }
        Direction result;
        if (side == Side.ON_THE_RIGHT) {
            result = p_line.direction().turn_45_degree(2);
        } else {
            result = p_line.direction().turn_45_degree(6);
        }
        return result;
    }

    /**
     * Returns 1, if this Point has a strict bigger x coordinate than p_other,
     * 0, if the x cooordinates are equal, and -1 otherwise.
     */
    public abstract int compare_x(Point p_other);

    /**
     * Returns 1, if this Point has a strict bigger y coordinate than p_other,
     * 0, if the y cooordinates are equal, and -1 otherwise.
     */
    public abstract int compare_y(Point p_other);

    /**
     * The function returns compare_x (p_other), if the result is not 0.
     * Otherwise it returns compare_y (p_other).
     */
    public int compare_x_y(Point p_other) {
        int result = compare_x(p_other);
        if (result == 0) {
            result = compare_y(p_other);
        }
        return result;
    }

    /**
     * Turns this point by p_factor times 90 degree around p_pole.
     */
    public Point turn_90_degree(int p_factor, Point p_pole) {
        Vector v = this.difference_by(p_pole);
        v = v.turn_90_degree(p_factor);
        return p_pole.translate_by(v);
    }

    /**
     * Mirrors this point at the vertical line through p_pole.
     */
    public Point mirror_vertical(Point p_pole) {
        Vector v = this.difference_by(p_pole);
        v = v.mirror_at_y_axis();
        return p_pole.translate_by(v);
    }

    /**
     * Mirrors this point at the horizontal line through p_pole.
     */
    public Point mirror_horizontal(Point p_pole) {
        Vector v = this.difference_by(p_pole);
        v = v.mirror_at_x_axis();
        return p_pole.translate_by(v);
    }

    // auxiliary functions needed because the virtual function mechanism
    // does not work in parameter position
    abstract Point translate_by(IntVector p_vector);

    abstract Point translate_by(RationalVector p_vector);

    abstract Vector difference_by(IntPoint p_other);

    abstract Vector difference_by(RationalPoint p_other);

    abstract int compare_x(IntPoint p_other);

    abstract int compare_x(RationalPoint p_other);

    abstract int compare_y(IntPoint p_other);

    abstract int compare_y(RationalPoint p_other);
}
