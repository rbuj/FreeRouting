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

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 *
 * @author robert
 */
@SuppressWarnings("serial")
class ArrayComboBoxModel<E> extends AbstractListModel<E> implements ComboBoxModel<E> {

    private final E[] arr;
    private Object selectedItem;

    ArrayComboBoxModel(E[] p_arr) {
        this.arr = (p_arr == null) ? null : p_arr.clone();
    }

    @Override
    public int getSize() {
        return (arr == null) ? 0 : arr.length;
    }

    @Override
    public E getElementAt(int index) {
        if (index >= 0 && index < arr.length) {
            return arr[index];
        } else {
            return null;
        }
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selectedItem = anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selectedItem;
    }
}
