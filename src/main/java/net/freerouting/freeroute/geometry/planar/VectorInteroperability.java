/*
 * Copyright (C) 2017 Robert Antoni Buj Gelonch {@literal <}rbuj{@literal @}fedoraproject.org{@literal >}
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributedx in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.freerouting.freeroute.geometry.planar;

import net.freerouting.freeroute.datastructures.Signum;

/**
 *
 * @author Robert Buj
 */
public interface VectorInteroperability {
     /**
     * Returns an approximation of the scalar product of this vector with
     * p_other by a double.
     */
    default double scalar_product(Vector p_other) {
        double result;
        if (p_other instanceof IntVector) {
            result = scalar_product((IntVector) p_other);
        } else if (p_other instanceof RationalVector) {
            result = scalar_product((RationalVector) p_other);
        } else {
            throw new AssertionError(p_other.getClass());
        }
        return result;
    }

    double scalar_product(IntVector p_other);

    double scalar_product(RationalVector p_other);

    /**
     * Let L be the line from the Zero Vector to p_other. The function returns
     * Side.ON_THE_LEFT, if this Vector is on the left of L Side.ON_THE_RIGHT,
     * if this Vector is on the right of L and Side.COLLINEAR, if this Vector is
     * collinear with L.
     */
    default Side side_of(Vector p_other) {
        Side result;
        if (p_other instanceof IntVector) {
            result = side_of((IntVector) p_other);
        } else if (p_other instanceof RationalVector) {
            result = side_of((RationalVector) p_other);
        } else {
            throw new AssertionError(p_other.getClass());
        }
        return result;
    }

    Side side_of(IntVector p_other);

    Side side_of(RationalVector p_other);

    /**
     * adds p_other to this vector
     */
    default Vector add(Vector p_other) {
        Vector result;
        if (p_other instanceof IntVector) {
            result = add((IntVector) p_other);
        } else if (p_other instanceof RationalVector) {
            result = add((RationalVector) p_other);
        } else {
            throw new AssertionError(p_other.getClass());
        }
        return result;
    }

    Vector add(IntVector p_other);

    Vector add(RationalVector p_other);

    /**
     * The function returns Signum.POSITIVE, if the scalar product of this
     * vector and p_other {@literal >} 0, Signum.NEGATIVE, if the scalar product
     * Vector is {@literal <} 0, and Signum.ZERO, if the scalar product is equal
     * 0.
     */
    default Signum projection(Vector p_other) {
        Signum result;
        if (p_other instanceof IntVector) {
            result = projection((IntVector) p_other);
        } else if (p_other instanceof RationalVector) {
            result = projection((RationalVector) p_other);
        } else {
            throw new AssertionError(p_other.getClass());
        }
        return result;
    }

    Signum projection(IntVector p_other);

    Signum projection(RationalVector p_other);

    default Point add_to(Point p_point) {
        Point result;
        if (p_point instanceof IntPoint) {
            result = add_to((IntPoint) p_point);
        } else if (p_point instanceof RationalPoint) {
            result = add_to((RationalPoint) p_point);
        } else {
            throw new AssertionError(p_point.getClass());
        }
        return result;
    }

    Point add_to(IntPoint p_point);

    Point add_to(RationalPoint p_point);

    /**
     * Returns an approximation of the cosinus of the angle between this vector
     * and p_other by a double.
     */
    default public double cos_angle(Vector p_other) {
        double result;
        if (p_other instanceof IntVector) {
            IntVector int_vector = (IntVector) p_other;
            result = scalar_product(int_vector);
            result /= to_float().size() * int_vector.to_float().size();
        } else if (p_other instanceof RationalVector) {
            RationalVector rational_vector = (RationalVector) p_other;
            result = scalar_product(rational_vector);
            result /= to_float().size() * rational_vector.to_float().size();
        } else {
            throw new AssertionError(p_other.getClass());
        }
        return result;
    }

    /**
     * Returns an approximation of the signed angle between this vector and
     * p_other.
     */
    default public double angle_approx(Vector p_other) {
        double result;
        if (p_other instanceof IntVector) {
            IntVector int_vector = (IntVector) p_other;
            result = Math.acos(cos_angle(int_vector));
            if (side_of(int_vector) == Side.ON_THE_LEFT) {
                result = -result;
            }
        } else if (p_other instanceof RationalVector) {
            RationalVector rational_vector = (RationalVector) p_other;
            result = Math.acos(cos_angle(rational_vector));
            if (side_of(rational_vector) == Side.ON_THE_LEFT) {
                result = -result;
            }
        } else {
            throw new AssertionError(p_other.getClass());
        }
        return result;
    }

    /**
     * approximates the coordinates of this vector by float coordinates
     */
    FloatPoint to_float();

}
