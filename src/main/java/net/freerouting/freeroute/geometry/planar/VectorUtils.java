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
public final class VectorUtils {
    /**
     * Standard implementation of the zero vector .
     */
    public static final IntVector ZERO = new IntVector(0, 0);

    /**
     * Creates a Vector (p_x, p_y) in the plane.
     */
    public static Vector get_instance(int p_x, int p_y) {
        IntVector result = new IntVector(p_x, p_y);
        if (Math.abs(p_x) > Limits.CRIT_INT
                || Math.abs(p_x) > Limits.CRIT_INT) {
            return new RationalVector(result);
        }
        return result;
    }

    /**
     * Creates a 2-dimensinal Vector from the 3 input values. If p_z != 0 it
     * correspondents to the Vector in the plane with rational number
     * coordinates (p_x / p_z, p_y / p_z).
     */
    public static Vector get_instance(BigInteger p_x, BigInteger p_y,
            BigInteger p_z) {
        if (p_z.signum() < 0) {
            // the dominator z of a RationalVector is expected to be positive
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
            return new IntVector(p_x.intValue(), p_y.intValue());
        }
        return new RationalVector(p_x, p_y, p_z);
    }
}
