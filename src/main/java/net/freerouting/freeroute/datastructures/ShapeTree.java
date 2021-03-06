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
 * ShapeTree.java
 *
 * Created on 1. September 2004, 10:50
 */
package net.freerouting.freeroute.datastructures;

import net.freerouting.freeroute.geometry.planar.RegularTileShape;
import net.freerouting.freeroute.geometry.planar.Shape;
import net.freerouting.freeroute.geometry.planar.ShapeBoundingDirections;
import net.freerouting.freeroute.geometry.planar.TileShape;

/**
 * Abstract binary search tree for shapes in the plane. The shapes are stored in
 * the leafs of the tree. Objects to be stored in the tree must implement the
 * interface ShapeTree.Storable.
 *
 * @author Alfons Wirtz
 */
public abstract class ShapeTree {

    /**
     * the fixed directions for calculating bounding RegularTileShapes of shapes
     * to store in this tree.
     */
    final protected ShapeBoundingDirections bounding_directions;
    /**
     * Root node - initially null
     */
    protected TreeNode root;
    /**
     * The number of entries stored in the tree
     */
    protected int leaf_count;

    /**
     * Creates a new instance of ShapeTree
     */
    ShapeTree(ShapeBoundingDirections p_directions) {
        bounding_directions = p_directions;
        root = null;
        leaf_count = 0;
    }

    /**
     * Inserts all shapes of p_obj into the tree
     */
    public void insert(ShapeTree.Storable p_obj) {
        int shape_count = p_obj.tree_shape_count(this);
        if (shape_count <= 0) {
            return;
        }
        Leaf[] leaf_arr = new Leaf[shape_count];
        for (int i = 0; i < shape_count; ++i) {
            leaf_arr[i] = insert(p_obj, i);
        }
        p_obj.set_search_tree_entries(leaf_arr, this);
    }

    /**
     * Insert a shape - creates a new node with a bounding shape
     */
    protected Leaf insert(ShapeTree.Storable p_object, int p_index) {
        Shape object_shape = p_object.get_tree_shape(this, p_index);
        if (object_shape == null) {
            return null;
        }

        RegularTileShape bounding_shape = object_shape.bounding_shape(bounding_directions);
        if (bounding_shape == null) {
            System.out.println("ShapeTree.insert: bounding shape of TreeObject is null");
            return null;
        }
        // Construct a new KdLeaf and set it up
        Leaf new_leaf = new Leaf(p_object, p_index, null, bounding_shape);
        this.insert(new_leaf);
        return new_leaf;
    }

    abstract void insert(Leaf p_leaf);

    abstract void remove_leaf(Leaf p_leaf);

    /**
     * removes all entries of p_obj in the tree.
     */
    public void remove(Leaf[] p_entries) {
        if (p_entries == null) {
            return;
        }
        for (Leaf leaf : p_entries) {
            remove_leaf(leaf);
        }
    }

    public static class TreeEntry {

        public final ShapeTree.Storable object;
        public final int shape_index_in_object;

        public TreeEntry(ShapeTree.Storable p_object, int p_shape_index_in_object) {
            object = p_object;
            shape_index_in_object = p_shape_index_in_object;
        }
    }

    //////////////////////////////////////////////////////////
    /**
     * Common functionality of inner nodes and leaf nodes.
     */
    protected static class TreeNode {

        public RegularTileShape bounding_shape;
        InnerNode parent;
    }

    public static class InnerNode extends TreeNode {

        public TreeNode first_child;
        public TreeNode second_child;

        InnerNode(RegularTileShape p_bounding_shape, InnerNode p_parent) {
            bounding_shape = p_bounding_shape;
            parent = p_parent;
            first_child = null;
            second_child = null;
        }
    }

    public static class Leaf extends TreeNode implements Comparable<Leaf> {

        /**
         * Actual object stored
         */
        public ShapeTree.Storable object;
        /**
         * index of the shape in the object
         */
        public int shape_index_in_object;

        private Leaf(ShapeTree.Storable p_object, int p_index, InnerNode p_parent, RegularTileShape p_bounding_shape) {
            bounding_shape = p_bounding_shape;
            parent = p_parent;
            object = p_object;
            shape_index_in_object = p_index;
        }

        @Override
        public int compareTo(Leaf p_other) {
            int result = this.object.compareTo(p_other.object);
            if (result == 0) {
                result = shape_index_in_object - p_other.shape_index_in_object;
            }
            return result;
        }
    }

    /**
     * Interface, which must be implemented by objects to be stored in a
     * ShapeTree.
     */
    public interface Storable extends Comparable<Object> {

        /**
         * Number of shapes of an object to store in p_shape_tree
         */
        int tree_shape_count(ShapeTree p_shape_tree);

        /**
         * Get the Shape of this object with index p_index stored in the
         * ShapeTree with index identification number p_tree_id_no
         */
        TileShape get_tree_shape(ShapeTree p_tree, int p_index);

        /**
         * Stores the entries in the ShapeTrees of this object for better
         * performance while for example deleting tree entries. Called only by
         * insert methods of class ShapeTree.
         */
        void set_search_tree_entries(Leaf[] p_entries, ShapeTree p_tree);
    }
}
