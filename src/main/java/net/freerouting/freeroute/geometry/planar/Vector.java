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

import java.math.BigInteger;
import net.freerouting.freeroute.datastructures.Signum;

/**
 * Abstract class describing functionality of Vectors. Vectors are used for
 * translating Points in the plane.
 *
 * @author Alfons Wirtz
 */
@SuppressWarnings("serial")
public abstract class Vector implements java.io.Serializable {

    /**
     * returns true, if this vector is equal to the zero vector.
     */
    public abstract boolean is_zero();

    /**
     * returns the Vector such that this plus this.negate() is zero
     */
    public abstract Vector negate();

    /**
     * adds p_other to this vector
     */
    public abstract Vector add(Vector p_other);

    /**
     * Let L be the line from the Zero Vector to p_other. The function returns
     * Side.ON_THE_LEFT, if this Vector is on the left of L Side.ON_THE_RIGHT,
     * if this Vector is on the right of L and Side.COLLINEAR, if this Vector is
     * collinear with L.
     */
    public abstract Side side_of(Vector p_other);

    /**
     * returns true, if the vector is horizontal or vertical
     */
    public abstract boolean is_orthogonal();

    /**
     * returns true, if the vector is diagonal
     */
    public abstract boolean is_diagonal();

    /**
     * Returns true, if the vector is orthogonal or diagonal
     */
    public boolean is_multiple_of_45_degree() {
        return is_orthogonal() || is_diagonal();
    }

    /**
     * The function returns Signum.POSITIVE, if the scalar product of this
     * vector and p_other {@literal >} 0, Signum.NEGATIVE, if the scalar product
     * Vector is {@literal <} 0, and Signum.ZERO, if the scalar product is equal
     * 0.
     */
    public abstract Signum projection(Vector p_other);

    /**
     * Returns an approximation of the scalar product of this vector with
     * p_other by a double.
     */
    public abstract double scalar_product(Vector p_other);

    /**
     * approximates the coordinates of this vector by float coordinates
     */
    public abstract FloatPoint to_float();

    /**
     * Turns this vector by p_factor times 90 degree.
     */
    public abstract Vector turn_90_degree(int p_factor);

    /**
     * Mirrors this vector at the x axis.
     */
    public abstract Vector mirror_at_x_axis();

    /**
     * Mirrors this vector at the y axis.
     */
    public abstract Vector mirror_at_y_axis();

    /**
     * returns an approximation of the euclidian length of this vector
     */
    public double length_approx() {
        return this.to_float().size();
    }

    /**
     * Returns an approximation of the cosinus of the angle between this vector
     * and p_other by a double.
     */
    public double cos_angle(Vector p_other) {
        double result = this.scalar_product(p_other);
        result /= this.to_float().size() * p_other.to_float().size();
        return result;
    }

    /**
     * Returns an approximation of the signed angle between this vector and
     * p_other.
     */
    public double angle_approx(Vector p_other) {
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
    public double angle_approx() {
        Vector other = new IntVector(1, 0);
        return other.angle_approx(this);
    }

    /**
     * Returns an approximation vector of this vector with the same direction
     * and length p_length.
     */
    public abstract Vector change_length_approx(double p_lenght);

    abstract Direction to_normalized_direction();

    // auxiliary functions needed because the virtual function mechanism
    // does not work in parameter position
    abstract Vector add(IntVector p_other);

    abstract Vector add(RationalVector p_other);

    abstract Point add_to(IntPoint p_point);

    abstract Point add_to(RationalPoint p_point);

    abstract Side side_of(IntVector p_other);

    abstract Side side_of(RationalVector p_other);

    abstract Signum projection(IntVector p_other);

    abstract Signum projection(RationalVector p_other);

    abstract double scalar_product(IntVector p_other);

    abstract double scalar_product(RationalVector p_other);

}
