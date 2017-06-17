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
 * LayerComboBox.java
 *
 * Created on 20. Februar 2005, 08:14
 */
package net.freerouting.freeroute;

import net.freerouting.freeroute.board.LayerStructure;
import static net.freerouting.freeroute.SignalLayerWithIndexBuilder.build_signal_layer_array;

/**
 * A Combo Box with items for individuell board layers plus an additional item
 * for all layers.
 *
 * @author Alfons Wirtz
 */
@SuppressWarnings("serial")
final class ComboBoxLayer extends javax.swing.JComboBox<SignalLayerWithIndex> {

    /**
     * Creates a new instance of LayerComboBox
     */
    ComboBoxLayer(LayerStructure p_layer_structure) {
        this.setModel(new ArrayComboBoxModel<>(build_signal_layer_array(p_layer_structure)));
        this.setSelectedIndex(0);
    }

    SignalLayerWithIndex get_selected_layer() {
        return (SignalLayerWithIndex) this.getSelectedItem();
    }
}
