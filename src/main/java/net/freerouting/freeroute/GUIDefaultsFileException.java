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
 * @author Robert Buj
 */
@SuppressWarnings("serial")
public class GUIDefaultsFileException extends Exception {

    /**
     * Creates a new instance of <code>GUIDefaultsFileException</code> without
     * detail message.
     */
    public GUIDefaultsFileException() {
    }

    /**
     * Constructs an instance of <code>GUIDefaultsFileException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public GUIDefaultsFileException(String msg) {
        super(msg);
    }

    public GUIDefaultsFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
