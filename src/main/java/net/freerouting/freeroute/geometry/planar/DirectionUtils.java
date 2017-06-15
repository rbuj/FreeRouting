package net.freerouting.freeroute.geometry.planar;

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
/**
 *
 * @author Robert Buj
 */
public final class DirectionUtils {

    static final IntDirection NULL = new IntDirection(0, 0);

    /**
     * creates a Direction from the input Vector
     */
    public static Direction get_instance(Vector p_vector) {
        return p_vector.to_normalized_direction();
    }

    /**
     * Calculates the direction from p_from to p_to. If p_from and p_to are
     * equal, null is returned.
     */
    public static Direction get_instance(Point p_from, Point p_to) {
        if (p_from.equals(p_to)) {
            return null;
        }
        return get_instance(p_to.difference_by(p_from));
    }

    /**
     * Creates a Direction whose angle with the x-axis is nearly equal to
     * p_angle
     */
    public static Direction get_instance_approx(double p_angle) {
        final double scale_factor = 10_000;
        int x = (int) Math.round(Math.cos(p_angle) * scale_factor);
        int y = (int) Math.round(Math.sin(p_angle) * scale_factor);
        return get_instance(new IntVector(x, y));
    }

    private DirectionUtils() {
        // not called
    }
}
