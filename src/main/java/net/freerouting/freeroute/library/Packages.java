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
 * Packages.java
 *
 * Created on 3. Juni 2004, 09:29
 */
package net.freerouting.freeroute.library;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import net.freerouting.freeroute.geometry.planar.Shape;

/**
 * Describes a library of component packages.
 *
 * @author Alfons Wirtz
 */
@SuppressWarnings("serial")
public class Packages implements java.io.Serializable, Iterable<Package> {

    /**
     * The array of packages in this object
     */
    private final ArrayList<Package> package_arr = new ArrayList<>();
    final Padstacks padstack_list;

    /**
     * Creates a new instance of Packages. p_padstack_list is the list of
     * padstacks used for the pins of the packages in this data structure.
     */
    public Packages(Padstacks p_padstack_list) {
        this.padstack_list = p_padstack_list;
    }

    /**
     * Returns the package with the input name and the input side or null, if no
     * such package exists.
     */
    public Package get(String p_name, boolean p_is_front) {
        Package other_side_package = null;
        for (Package curr_package : package_arr) {
            if (curr_package != null && curr_package.name.compareToIgnoreCase(p_name) == 0) {
                if (curr_package.is_front == p_is_front) {
                    return curr_package;
                } else {
                    other_side_package = curr_package;
                }
            }
        }
        return other_side_package;
    }

    /**
     * Returns the package with index p_package_no. Packages numbers are from 1
     * to package count.
     */
    public Package get(int p_package_no) {
        Package result = package_arr.get(p_package_no - 1);
        if (result != null && result.no != p_package_no) {
            System.out.println("Padstacks.get: inconsistent padstack number");
        }
        return result;
    }

    /**
     * Returns the count of packages in this object.
     */
    public int count() {
        return package_arr.size();
    }

    /**
     * Appends a new package with the input data to this object.
     */
    public Package add(String p_name, Package.Pin[] p_pin_arr, Shape[] p_outline,
            Package.Keepout[] p_keepout_arr, Package.Keepout[] p_via_keepout_arr,
            Package.Keepout[] p_place_keepout_arr, boolean p_is_front) {
        Package new_package = new Package(p_name, package_arr.size() + 1, p_pin_arr, p_outline,
                p_keepout_arr, p_via_keepout_arr, p_place_keepout_arr, p_is_front, this);
        package_arr.add(new_package);
        return new_package;
    }

    /**
     * Appends a new package with pins p_pin_arr to this object. The package
     * name is generated internally.
     */
    public Package add(Package.Pin[] p_pin_arr) {
        String package_name = "Package#" + Integer.toString(package_arr.size() + 1);

        return add(package_name, p_pin_arr, null, new Package.Keepout[0], new Package.Keepout[0],
                new Package.Keepout[0], true);
    }

    @Override
    public Iterator<Package> iterator() {
        return package_arr.iterator();
    }

    @Override
    public void forEach(Consumer<? super Package> action) {
        Iterable.super.forEach(action); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Spliterator<Package> spliterator() {
        return Iterable.super.spliterator(); //To change body of generated methods, choose Tools | Templates.
    }

}
