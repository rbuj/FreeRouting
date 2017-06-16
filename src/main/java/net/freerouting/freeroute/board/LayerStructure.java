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
 * Created on 26. Mai 2004, 06:37
 */
package net.freerouting.freeroute.board;

import com.google.common.collect.ImmutableList;
import java.util.stream.Collectors;

/**
 * Describes the layer structure of the board.
 *
 * @author alfons
 */
@SuppressWarnings("serial")
public class LayerStructure implements java.io.Serializable {

    private final ImmutableList<Layer> layer_list;
    private final ImmutableList<Layer> signal_layer_list;

    /**
     * Creates a new instance of LayerStructure
     */
    public LayerStructure(Layer[] p_layer_arr) {
        layer_list = (p_layer_arr == null) ? null : ImmutableList.copyOf(p_layer_arr);
        signal_layer_list = (p_layer_arr == null) ? null : ImmutableList.copyOf(
                layer_list.stream()
                        .filter(layer -> layer.is_signal())
                        .collect(Collectors.toList()));
    }

    /**
     * Returns the index of the layer with the name p_name in the array arr, -1,
     * if arr contains no layer with name p_name.
     */
    public int get_no(String p_name) {
        int layer_no = 0;
        for (Layer layer : layer_list) {
            if (p_name.equals(layer.get_name())) {
                return layer_no;
            }
            ++layer_no;
        }
        return -1;
    }

    /**
     * Returns the index of p_layer in the array arr, or -1, if arr does not
     * contain p_layer.
     */
    public int get_no(Layer p_layer) {
        return layer_list.indexOf(p_layer);
    }

    /**
     * Returns the count of signal layers of this layer_structure.
     */
    public int signal_layer_count() {
        return signal_layer_list.size();
    }

    /**
     * Gets the p_no-th signal layer of this layer structure.
     */
    public Layer get_signal_layer(int p_no) {
        return (p_no < signal_layer_list.size()) ? signal_layer_list.get(p_no) : layer_list.get(layer_list.size() - 1);
    }

    /**
     * Returns the count of signal layers with a smaller number than p_layer
     */
    public int get_signal_layer_no(Layer p_layer) {
        int found_signal_layers = 0;
        for (Layer layer : layer_list) {
            if (layer == p_layer) {
                return found_signal_layers;
            }
            if (layer.is_signal()) {
                ++found_signal_layers;
            }
        }
        return -1;
    }

    /**
     * Gets the layer number of the p_signal_layer_no-th signal layer in this
     * layer structure
     */
    public int get_layer_no(int p_signal_layer_no) {
        Layer curr_signal_layer = get_signal_layer(p_signal_layer_no);
        return get_no(curr_signal_layer);
    }

    public boolean[] active_routing_layer_arr() {
        boolean[] active_routing_layer_arr = new boolean[layer_list.size()];
        for (int i = 0; i < layer_list.size(); ++i) {
            active_routing_layer_arr[i] = layer_list.get(i).is_signal();
        }
        return active_routing_layer_arr;
    }

    public int get_layer_count() {
        return (layer_list == null) ? 0 : layer_list.size();
    }

    public boolean get_is_signal_layer(int index) {
        return (layer_list == null) ? null : layer_list.get(index).is_signal();
    }

    public String get_name_layer(int index) {
        return (layer_list == null) ? null : layer_list.get(index).get_name();
    }

    public Layer get_layer(int index) {
        return (layer_list == null) ? null : layer_list.get(index);
    }
}
