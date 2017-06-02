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
 * PolylineShape.java
 *
 * Created on 16. November 2002, 09:34
 */
package net.freerouting.freeroute.geometry.planar;

/**
 * Abstract class with functions for shapes, whose borders consist ob straight
 * lines.
 *
 * @author Alfons Wirtz
 */
@SuppressWarnings("serial")
public abstract class PolylineShape implements Shape, java.io.Serializable {

    /**
     * returns true, if the the shape has no infinite part at this corner
     */
    public abstract boolean corner_is_bounded(int p_no);

    /**
     * Returns the number of border lines of the shape
     */
    public abstract int border_line_count();

    /**
     * Returns the p_no-th corner of this shape for p_no between 0 and
     * border_line_count() - 1. The corners are sorted starting with the
     * smallest y-coordinate in counterclock sense arount the shape. If there
     * are several corners with the smallest y-coordinate, the corner with the
     * smallest x-coordinate comes first. Consecutive corners may be equal.
     */
    public abstract Point corner(int p_no);

    /**
     * Turns this shape by p_factor times 90 degree around p_pole.
     */
    @Override
    public abstract PolylineShape turn_90_degree(int p_factor, IntPoint p_pole);

    /**
     * Rotates this shape around p_pole by p_angle. The result may be not exact.
     */
    @Override
    public abstract PolylineShape rotate_approx(double p_angle, FloatPoint p_pole);

    /**
     * Mirrors this shape at the horizontal line through p_pole.
     */
    @Override
    public abstract PolylineShape mirror_horizontal(IntPoint p_pole);

    /**
     * Mirrors this shape at the vertical line through p_pole.
     */
    @Override
    public abstract PolylineShape mirror_vertical(IntPoint p_pole);

    /**
     * Returns the affine translation of the area by p_vector
     */
    @Override
    public abstract PolylineShape translate_by(Vector p_vector);

    /**
     * Returns an approximation of the p_no-th corner of this shape for p_no
     * between 0 and border_line_count() - 1. If the shape is not bounded at
     * this corner, the coordinates of the result will be set to
     * Integer.MAX_VALUE.
     */
    public FloatPoint corner_approx(int p_no) {
        return corner(p_no).to_float();
    }

    /**
     * Returns an approximation of the all corners of this shape. If the shape
     * is not bounded at a corner, the coordinates will be set to
     * Integer.MAX_VALUE.
     */
    @Override
    public FloatPoint[] corner_approx_arr() {
        int corner_count = this.border_line_count();
        FloatPoint[] result = new FloatPoint[corner_count];
        for (int i = 0; i < corner_count; ++i) {
            result[i] = this.corner_approx(i);
        }
        return result;
    }

    /**
     * If p_point is equal to a corner of this shape, the number of that corner
     * is returned; -1 otherwise.
     */
    public int equals_corner(Point p_point) {
        for (int i = 0; i < border_line_count(); ++i) {
            if (p_point.equals(corner(i))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the cumulative border line length of the shape. If the shape is
     * unbounded, Integer.MAX_VALUE is returned.
     */
    @Override
    public double circumference() {
        if (!is_bounded()) {
            return Integer.MAX_VALUE;
        }
        int corner_count = border_line_count();
        double result = 0;
        FloatPoint prev_corner = corner_approx(corner_count - 1);
        for (int i = 0; i < corner_count; ++i) {
            FloatPoint curr_corner = corner_approx(i);
            result += curr_corner.distance(prev_corner);
            prev_corner = curr_corner;
        }
        return result;
    }

    /**
     * Returns the arithmetic middle of the corners of this shape
     */
    @Override
    public FloatPoint centre_of_gravity() {
        int corner_count = border_line_count();
        double x = 0;
        double y = 0;
        for (int i = 0; i < corner_count; ++i) {
            FloatPoint curr_point = corner_approx(i);
            x += curr_point.x;
            y += curr_point.y;
        }
        x /= corner_count;
        y /= corner_count;
        return new FloatPoint(x, y);
    }

    /**
     * checks, if this shape is completely contained in p_box.
     */
    @Override
    public boolean is_contained_in(IntBox p_box) {
        return p_box.contains(bounding_box());
    }

    /**
     * Returns the index of the corner of the shape, so that all other points of
     * the shape are to the right of the line from p_from_point to this corner
     */
    public int index_of_left_most_corner(FloatPoint p_from_point) {
        FloatPoint left_most_corner = corner_approx(0);
        int corner_count = border_line_count();
        int result = 0;
        for (int i = 1; i < corner_count; ++i) {
            FloatPoint curr_corner = corner_approx(i);
            if (curr_corner.side_of(p_from_point, left_most_corner) == Side.ON_THE_LEFT) {
                left_most_corner = curr_corner;
                result = i;
            }
        }
        return result;
    }

    /**
     * Returns the index of the corner of the shape, so that all other points of
     * the shape are to the left of the line from p_from_point to this corner
     */
    public int index_of_right_most_corner(FloatPoint p_from_point) {
        FloatPoint right_most_corner = corner_approx(0);
        int corner_count = border_line_count();
        int result = 0;
        for (int i = 1; i < corner_count; ++i) {
            FloatPoint curr_corner = corner_approx(i);
            if (curr_corner.side_of(p_from_point, right_most_corner) == Side.ON_THE_RIGHT) {
                right_most_corner = curr_corner;
                result = i;
            }
        }
        return result;
    }

    /**
     * Returns a FloatLine result, so that result.a is an approximation of the
     * left most corner of this shape when viewed from p_from_point, and
     * result.b is an approximation of the right most corner.
     */
    public FloatLine polar_line_segment(FloatPoint p_from_point) {
        if (this.is_empty()) {
            System.out.println("PolylineShape.polar_line_segment: shape is empty");
            return null;
        }
        FloatPoint left_most_corner = corner_approx(0);
        FloatPoint right_most_corner = corner_approx(0);
        int corner_count = border_line_count();
        for (int i = 1; i < corner_count; ++i) {
            FloatPoint curr_corner = corner_approx(i);
            if (curr_corner.side_of(p_from_point, right_most_corner) == Side.ON_THE_RIGHT) {
                right_most_corner = curr_corner;
            }
            if (curr_corner.side_of(p_from_point, left_most_corner) == Side.ON_THE_LEFT) {
                left_most_corner = curr_corner;
            }
        }
        return new FloatLine(left_most_corner, right_most_corner);
    }

    /**
     * Returns the p_no-th border line of this shape.
     */
    public abstract Line border_line(int p_no);

    /**
     * Returns the previos border line or corner number of this shape.
     */
    public int prev_no(int p_no) {
        int result;
        if (p_no == 0) {
            result = border_line_count() - 1;
        } else {
            result = p_no - 1;
        }
        return result;
    }

    /**
     * Returns the next border line or corner number of this shape.
     */
    public int next_no(int p_no) {
        int result;
        if (p_no == border_line_count() - 1) {
            result = 0;
        } else {
            result = p_no + 1;
        }
        return result;
    }

    @Override
    public PolylineShape get_border() {
        return this;
    }

    @Override
    public Shape[] get_holes() {
        return new Shape[0];
    }

}
