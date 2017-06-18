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

import java.util.Locale;
import net.freerouting.freeroute.board.LayerStructure;

/**
 *
 * @author Robert Buj
 */
public final class SignalLayerWithIndexBuilder {
    /**
     * The layer index, when all layers are selected.
     */
    public static final int ALL_LAYER_INDEX = -1;
    /**
     * The layer index, when all inner layers ar selected.
     */
    public static final int INNER_LAYER_INDEX = -2;

    private SignalLayerWithIndexBuilder() {
        // no code
    }

    static SignalLayerWithIndex[] build_signal_layer_array(LayerStructure p_layer_structure) {
        java.util.ResourceBundle resources
                = java.util.ResourceBundle.getBundle("net.freerouting.freeroute.resources.Default", Locale.getDefault());
        int signal_layer_count = p_layer_structure.signal_layer_count();
        int item_count = signal_layer_count + 1;
        boolean add_inner_layer_item = signal_layer_count > 2;
        if (add_inner_layer_item) {
            ++item_count;
        }
        SignalLayerWithIndex[] layer_arr = new SignalLayerWithIndex[item_count];
        layer_arr[0] = new SignalLayerWithIndex(resources.getString("all"), ALL_LAYER_INDEX);
        int curr_layer_no = 0;
        if (add_inner_layer_item) {
            layer_arr[1] = new SignalLayerWithIndex(resources.getString("inner"), INNER_LAYER_INDEX);
            ++curr_layer_no;
        }
        for (int i = 0; i < signal_layer_count; ++i) {
            ++curr_layer_no;
            net.freerouting.freeroute.board.Layer curr_signal_layer = p_layer_structure.get_signal_layer(i);
            layer_arr[curr_layer_no] = new SignalLayerWithIndex(curr_signal_layer.get_name(), p_layer_structure.get_no(curr_signal_layer));
        }
        return layer_arr;
    }
}
