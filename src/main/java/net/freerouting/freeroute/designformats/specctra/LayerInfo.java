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
 * LayerInfo.java
 *
 * Created on 15. Mai 2004, 08:29
 */
package net.freerouting.freeroute.designformats.specctra;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Describes a layer in a Specctra dsn file.
 *
 * @author alfons
 */
public abstract class LayerInfo {

    /**
     * all layers of the board
     */
    static final LayerInfo PCB = new LayerNotSignalInfo("pcb", -1);
    /**
     * the signal layers
     */
    static final LayerInfo SIGNAL = new LayerSignalInfo("signal", -1);

    /**
     * Writes a layer scope in the stucture scope.
     */
    public static void write_scope(WriteScopeParameter p_par, int p_layer_no,
            boolean p_write_rule) throws java.io.IOException {
        p_par.file.start_scope("layer ");
        net.freerouting.freeroute.board.Layer board_layer = p_par.board.layer_structure.get_layer(p_layer_no);
        p_par.identifier_type.write(board_layer.get_name(), p_par.file);
        p_par.file.new_line();
        p_par.file.write("(type " + board_layer.get_type() + ")");
        if (p_write_rule) {
            Rule.write_default_rule(p_par, p_layer_no);
        }
        p_par.file.end_scope();
    }

    final String name;
    final int layer_no;
    final java.util.Collection<String> net_names;

    /**
     * Creates a new instance of Layer. p_layer_no is the physical layer number
     * starting with 0 at the component side and ending at the solder side. If
     * p_is_signal, the layer is a signal layer, otherwise it is a powerground
     * layer. For Layer objects describing more than 1 layer the number is -1.
     * p_net_names is a list of nets for this layer, if the layer is a power
     * plane.
     */
    LayerInfo(String p_name, int p_layer_no, Collection<String> p_net_names) {
        name = p_name;
        layer_no = p_layer_no;
        net_names = p_net_names;
    }

    /**
     * Creates a new instance of Layer. p_layer_no is the physical layer number
     * starting with 0 at the component side and ending at the solder side. If
     * p_is_signal, the layer is a signal layer, otherwise it is a powerground
     * layer. For Layer objects describing more than 1 layer the number is -1.
     */
    LayerInfo(String p_name, int p_layer_no) {
        name = p_name;
        layer_no = p_layer_no;
        net_names = new LinkedList<>();
    }
}
