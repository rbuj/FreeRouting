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
 */
package net.freerouting.freeroute.geometry.planar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Collectors;

/**
 *
 * A Polygon is a list of points in the plane, where no 2 consecutive points may
 * be equal and no 3 consecutive points collinear.
 *
 * @author Alfons Wirtz#
 */
@SuppressWarnings("serial")
public class Polygon implements java.io.Serializable {

    private final Collection<Point> corners;

    /**
     * Creates a polygon from p_point_arr. Multiple points and points, which are
     * collinear with its previous and next point will be removed.
     */
    Polygon(Point[] p_point_arr) {
        if (p_point_arr.length == 0) {
            corners = new ArrayList<>();
            return;
        }
        corners = Arrays.stream(p_point_arr).collect(Collectors.toCollection(ArrayList::new));
        check();
    }

    /**
     * Creates a polygon from p_point_list. Multiple points and points, which
     * are collinear with its previous and next point will be removed.
     */
    private Polygon(Collection<Point> p_point_list) {
        if (p_point_list.isEmpty()) {
            corners = new ArrayList<>();
            return;
        }
        corners = new ArrayList<>(p_point_list);

        check();
    }

    private void check() {
        boolean corner_removed = true;
        while (corner_removed) {
            corner_removed = false;
            // remove multiple points

            if (corners.isEmpty()) {
                return;
            }
            Iterator<Point> i = corners.iterator();
            Point curr_ob = i.next();
            while (i.hasNext()) {
                Point next_ob = i.next();
                if (next_ob.equals(curr_ob)) {
                    i.remove();
                    corner_removed = true;
                } else {
                    curr_ob = next_ob;
                }
            }

            // remove points which are collinear with  the previous
            // and next point.
            i = corners.iterator();
            Point prev = i.next();
            Iterator<Point> prev_i = corners.iterator();
            if (!i.hasNext()) {
                continue;
            }
            Point curr = i.next();
            prev_i.next();
            while (i.hasNext()) {
                Point next = i.next();
                prev_i.next();

                if (curr.side_of(prev, next) == Side.COLLINEAR) {
                    prev_i.remove();
                    corner_removed = true;
                    break;
                }
                prev = curr;
                curr = next;
            }
        }
    }

    /**
     * returns the array of corners of this polygon
     */
    public Point[] corner_array() {
        return corners.stream().toArray(Point[]::new);
    }

    /**
     * Reverts the order of the corners of this polygon.
     */
    public Polygon revert_corners() {
        ArrayList<Point> reverse_corners;
        if (!corners.isEmpty()) {
            reverse_corners = new ArrayList<>(corners);
            Collections.reverse(reverse_corners);
        } else {
            reverse_corners = new ArrayList<>();
        }
        return new Polygon(reverse_corners);
    }

    /**
     * Returns the winding number of this polygon, treated as closed. It will be
     * {@literal >} 0, if the corners are in countercock sense, and {@literal <}
     * 0, if the corners are in clockwise sense.
     */
    public int winding_number_after_closing() {
        Point[] corner_arr = corner_array();
        if (corner_arr.length < 2) {
            return 0;
        }
        Vector first_side_vector = corner_arr[1].difference_by(corner_arr[0]);
        Vector prev_side_vector = first_side_vector;
        int corner_count = corner_arr.length;
        // Skip the last corner, if it is equal to the first corner.
        if (corner_arr[0].equals(corner_arr[corner_count - 1])) {
            --corner_count;
        }
        double angle_sum = 0;
        for (int i = 1; i <= corner_count; ++i) {
            Vector next_side_vector;
            if (i == corner_count - 1) {
                next_side_vector = corner_arr[0].difference_by(corner_arr[i]);
            } else if (i == corner_count) {
                next_side_vector = first_side_vector;
            } else {
                next_side_vector = corner_arr[i + 1].difference_by(corner_arr[i]);
            }
            angle_sum += prev_side_vector.angle_approx(next_side_vector);
            prev_side_vector = next_side_vector;
        }
        angle_sum /= 2.0 * Math.PI;
        if (Math.abs(angle_sum) < 0.5) {
            System.out.println("Polygon.winding_number_after_closing: winding number != 0 expected");
        }
        return (int) Math.round(angle_sum);
    }

}
