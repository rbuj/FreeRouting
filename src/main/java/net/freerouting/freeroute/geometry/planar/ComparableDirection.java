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

/**
 *
 * @author Robert Buj
 */
interface ComparableDirection extends Comparable<Direction> {

    /**
     * Implements the Comparable interface. Returns 1, if this direction has a
     * strict bigger angle with the positive x-axis than p_other_direction, 0,
     * if this direction is equal to p_other_direction, and -1 otherwise. Throws
     * an exception, if p_other_direction is not a Direction.
     */
    @Override
    default int compareTo(Direction p_other_direction) {
        int result;
        if (p_other_direction instanceof IntDirection) {
            result = compareTo((IntDirection) p_other_direction);
        } else if (p_other_direction instanceof BigIntDirection) {
            result = compareTo((BigIntDirection) p_other_direction);
        } else {
            throw new ClassCastException("can't compare to " + p_other_direction.getClass().getName());
        }
        return result;
    }

    int compareTo(IntDirection p_other);

    int compareTo(BigIntDirection p_other);
}
