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
 * Vector.java
 *
 * Created on 1. Februar 2003, 14:28
 */
package net.freerouting.freeroute.geometry.planar;

import net.freerouting.freeroute.datastructures.Signum;

/**
 * Abstract class describing functionality of Vectors. Vectors are used for
 * translating Points in the plane.
 *
 * @author Alfons Wirtz
 */
@SuppressWarnings("serial")
public interface Vector extends java.io.Serializable {

    /**
     * returns true, if this vector is equal to the zero vector.
     */
    public boolean is_zero();

    /**
     * returns the Vector such that this plus this.negate() is zero
     */
    public Vector negate();

    /**
     * adds p_other to this vector
     */
    public Vector add(Vector p_other);

    /**
     * Let L be the line from the Zero Vector to p_other. The function returns
     * Side.ON_THE_LEFT, if this Vector is on the left of L Side.ON_THE_RIGHT,
     * if this Vector is on the right of L and Side.COLLINEAR, if this Vector is
     * collinear with L.
     */
    public Side side_of(Vector p_other);

    /**
     * returns true, if the vector is horizontal or vertical
     */
    public boolean is_orthogonal();

    /**
     * returns true, if the vector is diagonal
     */
    public boolean is_diagonal();

    /**
     * Returns true, if the vector is orthogonal or diagonal
     */
    default public boolean is_multiple_of_45_degree() {
        return is_orthogonal() || is_diagonal();
    }

    /**
     * The function returns Signum.POSITIVE, if the scalar product of this
     * vector and p_other {@literal >} 0, Signum.NEGATIVE, if the scalar product
     * Vector is {@literal <} 0, and Signum.ZERO, if the scalar product is equal
     * 0.
     */
    public Signum projection(Vector p_other);

    /**
     * Returns an approximation of the scalar product of this vector with
     * p_other by a double.
     */
    public double scalar_product(Vector p_other);

    /**
     * approximates the coordinates of this vector by float coordinates
     */
    public FloatPoint to_float();

    /**
     * Turns this vector by p_factor times 90 degree.
     */
    public Vector turn_90_degree(int p_factor);

    /**
     * Mirrors this vector at the x axis.
     */
    public Vector mirror_at_x_axis();

    /**
     * Mirrors this vector at the y axis.
     */
    public Vector mirror_at_y_axis();

    /**
     * returns an approximation of the euclidian length of this vector
     */
    default public double length_approx() {
        return this.to_float().size();
    }

    /**
     * Returns an approximation of the cosinus of the angle between this vector
     * and p_other by a double.
     */
    default public double cos_angle(Vector p_other) {
        double result = this.scalar_product(p_other);
        result /= this.to_float().size() * p_other.to_float().size();
        return result;
    }

    /**
     * Returns an approximation of the signed angle between this vector and
     * p_other.
     */
    default public double angle_approx(Vector p_other) {
        double result = Math.acos(cos_angle(p_other));
        if (this.side_of(p_other) == Side.ON_THE_LEFT) {
            result = -result;
        }
        return result;
    }

    /**
     * Returns an approximation of the signed angle between this vector and the
     * x axis.
     */
    default public double angle_approx() {
        Vector other = new IntVector(1, 0);
        return other.angle_approx(this);
    }

    /**
     * Returns an approximation vector of this vector with the same direction
     * and length p_length.
     */
    public Vector change_length_approx(double p_lenght);

    Direction to_normalized_direction();

    Vector add(IntVector p_other);

    Vector add(RationalVector p_other);

    Point add_to(IntPoint p_point);

    Point add_to(RationalPoint p_point);

    Side side_of(IntVector p_other);

    Side side_of(RationalVector p_other);

    Signum projection(IntVector p_other);

    Signum projection(RationalVector p_other);

    double scalar_product(IntVector p_other);

    double scalar_product(RationalVector p_other);

}
