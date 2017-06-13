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
 * ClearanceComboBox.java
 *
 * Created on 1. Maerz 2005, 09:27
 */
package net.freerouting.freeroute;

import net.freerouting.freeroute.rules.ClearanceMatrix;

/**
 * A Combo Box with an item for each clearance class of the board..
 *
 * @author alfons
 */
@SuppressWarnings("serial")
public final class ComboBoxClearance extends javax.swing.JComboBox<ClearanceClass> {

    /**
     * Creates a new instance of ClearanceComboBox
     */
    ComboBoxClearance(ClearanceMatrix p_clearance_matrix) {
        ClearanceClass[] class_arr = new ClearanceClass[p_clearance_matrix.get_class_count()];
        for (int i = 0; i < class_arr.length; ++i) {
            class_arr[i] = new ClearanceClass(p_clearance_matrix.get_name(i), i);
        }
        this.setModel(new ArrayComboBoxModel<>(class_arr));
        this.setSelectedIndex(1);
    }

    /**
     * Adjusts this combo box to p_new_clearance_matrix.
     */
    public void adjust(ClearanceMatrix p_new_clearance_matrix) {
        int old_index = this.get_selected_class_index();
        ClearanceClass[] class_arr = new ClearanceClass[p_new_clearance_matrix.get_class_count()];
        for (int i = 0; i < class_arr.length; ++i) {
            class_arr[i] = new ClearanceClass(p_new_clearance_matrix.get_name(i), i);
        }
        this.setModel(new ArrayComboBoxModel<>(class_arr));
        this.setSelectedIndex(Math.min(old_index, class_arr.length - 1));
    }

    /**
     * Returns the index of the selected clearance class in the clearance
     * matrix.
     */
    public int get_selected_class_index() {
        return ((ClearanceClass) this.getSelectedItem()).index;
    }

    /**
     * Returns the number of clearance classes in this combo box.
     */
    public int get_class_count() {
        return this.getItemCount();
    }
}
