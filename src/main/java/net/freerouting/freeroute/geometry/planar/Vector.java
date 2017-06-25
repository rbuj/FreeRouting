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

/**
 * Abstract class describing functionality of Vectors. Vectors are used for
 * translating Points in the plane.
 *
 * @author Alfons Wirtz
 */
@SuppressWarnings("serial")
public abstract class Vector implements VectorInteroperability, java.io.Serializable {

   /**
     * returns true, if this vector is equal to the zero vector.
     */
    public abstract boolean is_zero();

    /**
     * returns the Vector such that this plus this.negate() is zero
     */
    public abstract Vector negate();

    /**
     * returns true, if the vector is horizontal or vertical
     */
    public abstract boolean is_orthogonal();

    /**
     * returns true, if the vector is diagonal
     */
    public abstract boolean is_diagonal();

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
     * Returns true, if the vector is orthogonal or diagonal
     */
    public boolean is_multiple_of_45_degree() {
        return is_orthogonal() || is_diagonal();
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

    public abstract Direction to_normalized_direction();

    /**
     * returns an approximation of the euclidian length of this vector
     */
    public double length_approx() {
        return to_float().size();
    }
}
