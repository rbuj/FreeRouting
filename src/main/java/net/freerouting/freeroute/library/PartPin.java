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
package net.freerouting.freeroute.library;

/**
 *
 * @author Robert Buj
 */
@SuppressWarnings("serial")
public class PartPin implements Comparable<PartPin>, java.io.Serializable {

    /**
     * The number of the part pin. Must be the same number as in the componnents
     * library package.
     */
    public final int pin_no;

    /**
     * The name of the part pin. Must be the same name as in the componnents
     * library package.
     */
    public final String pin_name;

    /**
     * The name of the gate this pin belongs to.
     */
    public final String gate_name;

    /**
     * The gate swap code. Gates with the same gate swap code can be swapped.
     * Gates with swap code {@literal <}= 0 are not swappable.
     */
    public final int gate_swap_code;

    /**
     * The identifier of the pin in the gate.
     */
    public final String gate_pin_name;

    /**
     * The pin swap code of the gate. Pins with the same pin swap code can be
     * swapped inside a gate. Pins with swap code {@literal <}= 0 are not
     * swappable.
     */
    public final int gate_pin_swap_code;

    public PartPin(int p_pin_no, String p_pin_name, String p_gate_name, int p_gate_swap_code,
            String p_gate_pin_name, int p_gate_pin_swap_code) {
        pin_no = p_pin_no;
        pin_name = p_pin_name;
        gate_name = p_gate_name;
        gate_swap_code = p_gate_swap_code;
        gate_pin_name = p_gate_pin_name;
        gate_pin_swap_code = p_gate_pin_swap_code;
    }

    @Override
    public int compareTo(PartPin p_other) {
        return this.pin_no - p_other.pin_no;
    }
}
