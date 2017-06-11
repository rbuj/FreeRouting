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
package net.freerouting.freeroute;

/**
 *
 * @author robert
 */
enum Zoom {

    IN(Constants.ZOOM_IN, Constants.C_ZOOM_FACTOR),
    OUT(Constants.ZOOM_OUT, 1 / Constants.C_ZOOM_FACTOR);

    private final String name;
    private final double factor;

    private Zoom(String name, double factor) {
        this.name = name;
        this.factor = factor;
    }

    @Override
    public String toString() {
        return name;
    }

    double get_factor() {
        return factor;
    }

    static final class Constants {

        static final double C_ZOOM_FACTOR = 2.0;
        static final String ZOOM_IN = "zoom_in";
        static final String ZOOM_OUT = "zoom_out";

        private Constants() {
            // no code
        }
    }

}
