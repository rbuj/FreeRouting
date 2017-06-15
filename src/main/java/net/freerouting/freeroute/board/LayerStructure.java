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

/**
 * Describes the layer structure of the board.
 *
 * @author alfons
 */
@SuppressWarnings("serial")
public class LayerStructure implements java.io.Serializable {

    private final Layer[] arr;

    /**
     * Creates a new instance of LayerStructure
     */
    public LayerStructure(Layer[] p_layer_arr) {
        arr = (p_layer_arr == null) ? null : p_layer_arr.clone();
    }

    /**
     * Returns the index of the layer with the name p_name in the array arr, -1,
     * if arr contains no layer with name p_name.
     */
    public int get_no(String p_name) {
        for (int i = 0; i < arr.length; ++i) {
            if (p_name.equals(arr[i].get_name())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the index of p_layer in the array arr, or -1, if arr does not
     * contain p_layer.
     */
    public int get_no(Layer p_layer) {
        for (int i = 0; i < arr.length; ++i) {
            if (p_layer == arr[i]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns the count of signal layers of this layer_structure.
     */
    public int signal_layer_count() {
        int found_signal_layers = 0;
        for (int i = 0; i < arr.length; ++i) {
            if (arr[i].is_signal()) {
                ++found_signal_layers;
            }
        }
        return found_signal_layers;
    }

    /**
     * Gets the p_no-th signal layer of this layer structure.
     */
    public Layer get_signal_layer(int p_no) {
        int found_signal_layers = 0;
        for (int i = 0; i < arr.length; ++i) {
            if (arr[i].is_signal()) {
                if (p_no == found_signal_layers) {
                    return arr[i];
                }
                ++found_signal_layers;
            }
        }
        return arr[arr.length - 1];
    }

    /**
     * Returns the count of signal layers with a smaller number than p_layer
     */
    public int get_signal_layer_no(Layer p_layer) {
        int found_signal_layers = 0;
        for (int i = 0; i < arr.length; ++i) {
            if (arr[i] == p_layer) {
                return found_signal_layers;
            }
            if (arr[i].is_signal()) {
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
        if (arr == null) {
            return null;
        }
        boolean[] active_routing_layer_arr = new boolean[arr.length];
        for (int i = 0; i < arr.length; ++i) {
            active_routing_layer_arr[i] = arr[i].is_signal();
        }
        return active_routing_layer_arr;
    }

    public int get_layer_count(){
        return (arr == null) ? 0 : arr.length;
    }

    public boolean get_is_signal_layer(int index) {
        return (arr == null) ? null : (arr[index] == null) ? null : arr[index].is_signal();
    }

    public String get_name_layer(int index) {
        return (arr == null) ? null : (arr[index] == null) ? null : arr[index].get_name();
    }

    public Layer get_layer(int index) {
        return (arr == null) ? null : (arr[index] == null) ? null : arr[index];
    }
}
