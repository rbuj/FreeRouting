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
package net.freerouting.freeroute.designformats.specctra;

/**
 *
 * @author robert
 */
public class PinInfo {

    /**
     * Phe name of the pastack of this pin.
     */
    public final String padstack_name;
    /**
     * Phe name of this pin.
     */
    public final String pin_name;
    /**
     * The x- and y-coordinates relative to the package location.
     */
    public final double[] rel_coor;
    /**
     * The rotation of the pin relative to the package.
     */
    public final double rotation;

    private PinInfo(String p_padstack_name, String p_pin_name, double[] p_rel_coor, double p_rotation) {
        padstack_name = p_padstack_name;
        pin_name = p_pin_name;
        rel_coor = p_rel_coor;
        rotation = p_rotation;
    }

    /**
     * Reads the information of a single pin in a package.
     */
    public static PinInfo read_pin_info(Scanner p_scanner) throws ReadScopeException {
        try {
            // Read the padstack name.
            p_scanner.yybegin(SpecctraFileScanner.NAME);
            String padstack_name;
            Object next_token = p_scanner.next_token();
            if (next_token instanceof String) {
                padstack_name = (String) next_token;
            } else if (next_token instanceof Integer) {
                padstack_name = next_token.toString();
            } else {
                throw new ReadScopeException("Package.read_pin_info: String or Integer expected");
            }
            double rotation = 0;

            p_scanner.yybegin(SpecctraFileScanner.NAME); // to be able to handle pin names starting with a digit.
            next_token = p_scanner.next_token();
            if (next_token == Keyword.OPEN_BRACKET) {
                // read the padstack rotation
                next_token = p_scanner.next_token();
                if (next_token == Keyword.ROTATE) {
                    rotation = read_rotation(p_scanner);
                } else {
                    ScopeKeyword.skip_scope(p_scanner);
                }
                p_scanner.yybegin(SpecctraFileScanner.NAME);
                next_token = p_scanner.next_token();
            }
            // Read the pin name.
            String pin_name;
            if (next_token instanceof String) {
                pin_name = (String) next_token;
            } else if (next_token instanceof Integer) {
                pin_name = next_token.toString();
            } else {
                throw new ReadScopeException("Package.read_pin_info: String or Integer expected");
            }

            double[] pin_coor = new double[2];
            for (int i = 0; i < 2; ++i) {
                next_token = p_scanner.next_token();
                if (next_token instanceof Double) {
                    pin_coor[i] = (double) next_token;
                } else if (next_token instanceof Integer) {
                    pin_coor[i] = ((Number) next_token).doubleValue();
                } else {
                    throw new ReadScopeException("Package.read_pin_info: number expected");
                }
            }
            // Handle scopes at the end of the pin scope.
            for (;;) {
                Object prev_token = next_token;
                next_token = p_scanner.next_token();

                if (next_token == null) {
                    throw new ReadScopeException("Package.read_pin_info: unexpected end of file");
                }
                if (next_token == Keyword.CLOSED_BRACKET) {
                    // end of scope
                    break;
                }
                if (prev_token == Keyword.OPEN_BRACKET) {
                    if (next_token == Keyword.ROTATE) {
                        rotation = read_rotation(p_scanner);
                    } else {
                        ScopeKeyword.skip_scope(p_scanner);
                    }
                }
            }
            return new PinInfo(padstack_name, pin_name, pin_coor, rotation);
        } catch (java.io.IOException e) {
            throw new ReadScopeException("Package.read_pin_info: IO error while scanning file", e);
        }
    }

    private static double read_rotation(Scanner p_scanner) throws ReadScopeException {
        double result = 0;
        try {
            Object next_token = p_scanner.next_token();
            if (next_token instanceof Integer) {
                result = ((Number) next_token).doubleValue();
            } else if (next_token instanceof Double) {
                result = (double) next_token;
            } else {
                throw new ReadScopeException("Package.read_rotation: number expected");
            }
            // Overread The closing bracket.
            next_token = p_scanner.next_token();
            if (next_token != Keyword.CLOSED_BRACKET) {
                throw new ReadScopeException("Package.read_rotation: closing bracket expected");
            }
        } catch (java.io.IOException e) {
            throw new ReadScopeException("Package.read_rotation: IO error while scanning file", e);
        }
        return result;
    }

    public static void write_scope(WriteScopeParameter p_par, net.freerouting.freeroute.library.Package.Pin curr_pin) throws java.io.IOException {
        p_par.file.new_line();
        p_par.file.write("(pin ");
        net.freerouting.freeroute.library.Padstack curr_padstack = p_par.board.library.padstacks.get(curr_pin.padstack_no);
        p_par.identifier_type.write(curr_padstack.name, p_par.file);
        p_par.file.write(" ");
        p_par.identifier_type.write(curr_pin.name, p_par.file);
        double[] rel_coor = p_par.coordinate_transform.board_to_dsn(curr_pin.relative_location);
        for (int j = 0; j < rel_coor.length; ++j) {
            p_par.file.write(" ");
            p_par.file.write(Double.toString(rel_coor[j]));
        }
        int rotation = (int) Math.round(curr_pin.rotation_in_degree);
        if (rotation != 0) {
            p_par.file.write("(rotate ");
            p_par.file.write(Integer.toString(rotation));
            p_par.file.write(")");
        }
        p_par.file.write(")");
    }
}
