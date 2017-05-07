/*
 * Copyright (C) 2017 Robert Antoni Buj Gelonch {@literal <}rbuj{@literal @}fedoraproject.org{@literal >}
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.freerouting.freeroute.geometry.planar;

import java.math.BigInteger;

/**
 *
 * @author robert
 */
public final class PointUtils {
    /**
     * Standard implementation of the zero point .
     */
    public static final IntPoint ZERO = new IntPoint(0, 0);

    /**
     * creates an IntPoint from p_x and p_y. If p_x or p_y is to big for an
     * IntPoint, a RationalPoint is created.
     */
    public static Point get_instance(int p_x, int p_y) {
        IntPoint result = new IntPoint(p_x, p_y);
        if (Math.abs(p_x) > Limits.CRIT_INT
                || Math.abs(p_x) > Limits.CRIT_INT) {
            return new RationalPoint(result);
        }
        return result;
    }

    /**
     * factory method for creating a Point from 3 BigIntegers
     */
    public static Point get_instance(BigInteger p_x, BigInteger p_y,
            BigInteger p_z) {
        if (p_z.signum() < 0) {
            // the dominator z of a RationalPoint is expected to be positive
            p_x = p_x.negate();
            p_y = p_y.negate();
            p_z = p_z.negate();

        }
        if ((p_x.mod(p_z)).signum() == 0 && (p_x.mod(p_z)).signum() == 0) {
            // p_x and p_y can be divided by p_z
            p_x = p_x.divide(p_z);
            p_y = p_y.divide(p_z);
            p_z = BigInteger.ONE;
        }
        if (p_z.equals(BigInteger.ONE) && ((p_x.abs()).compareTo(Limits.CRIT_INT_BIG) <= 0
                && (p_y.abs()).compareTo(Limits.CRIT_INT_BIG) <= 0)) {
            // the Point fits into an IntPoint
            return new IntPoint(p_x.intValue(), p_y.intValue());
        }
        return new RationalPoint(p_x, p_y, p_z);
    }

}
