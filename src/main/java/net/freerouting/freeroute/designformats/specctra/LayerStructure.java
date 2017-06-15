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
 * LayerStructure.java
 *
 * Created on 16. Mai 2004, 08:08
 */
package net.freerouting.freeroute.designformats.specctra;

import java.util.Collection;

/**
 * Describes a layer structure read from a dsn file.
 *
 * @author alfons
 */
public class LayerStructure {

    public final Layer[] arr;

    /**
     * Creates a new instance of LayerStructure from a list of layers
     */
    LayerStructure(Collection<Layer> p_layer_list) {
        arr = p_layer_list.stream().toArray(Layer[]::new);
    }

    /**
     * Creates a dsn-LayerStructure from a board LayerStructure.
     */
    LayerStructure(net.freerouting.freeroute.board.LayerStructure p_board_layer_structure) {
        arr = new Layer[p_board_layer_structure.get_layer_count()];
        for (int i = 0; i < arr.length; ++i) {
            net.freerouting.freeroute.board.Layer board_layer = p_board_layer_structure.get_layer(i);
            arr[i] = new Layer(board_layer.get_name(), i, board_layer.is_signal());
        }
    }

    /**
     * returns the number of the layer with the name p_name, -1, if no layer
     * with name p_name exists.
     */
    public int get_no(String p_name) {
        for (int i = 0; i < arr.length; ++i) {
            if (p_name.equals(arr[i].name)) {
                return i;
            }
        }
        // check for special layers of the Electra autorouter used for the outline
        if (p_name.contains("Top")) {
            return 0;
        }
        if (p_name.contains("Bottom")) {
            return arr.length - 1;
        }
        return -1;
    }

    /**
     * Returns, if the net with name p_net_name contains a powwer plane.
     */
    public boolean contains_plane(String p_net_name) {
        for (Layer curr_layer : arr) {
            if (!curr_layer.is_signal && curr_layer.net_names.contains(p_net_name)) {
                return true;
            }
        }
        return false;
    }

}
