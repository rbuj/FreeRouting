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
package net.freerouting.freeroute.board;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Robert Buj
 */
public class LayerStructureTest {

    LayerStructure layer_structure;

    public LayerStructureTest() {
    }

    @Before
    public void setUp() {
        Layer[] arr = new Layer[]{
            new Layer("Layer#0", false),
            new Layer("Layer#1", true),
            new Layer("Layer#2", false)
        };
        layer_structure = new LayerStructure(arr);
    }

    @Test
    public void test_get_signal_layer() {
        Layer expResult, result;

        System.out.println("get_signal_layer");

        expResult = layer_structure.get_layer(1);
        result = layer_structure.get_signal_layer(0);
        assertEquals(expResult, result);
        assertSame(expResult, result);

        expResult = layer_structure.get_layer(2);
        result = layer_structure.get_signal_layer(1);
        assertEquals(expResult, result);
        assertSame(expResult, result);
    }

    @Test
    public void test_signal_layer_count() {
        int expResult = 1, result;
        System.out.println("signal_layer_count");
        result = layer_structure.signal_layer_count();
        assertEquals(expResult, result);
    }

    @Test
    public void test_get_layer_count() {
        int expResult = 3, result;
        System.out.println("get_layer_count");
        result = layer_structure.get_layer_count();
        assertEquals(expResult, result);
    }

    @Test
    public void test_get_is_signal_layer() {
        System.out.println("get_is_signal_layer");
        assertFalse(layer_structure.get_is_signal_layer(0));
        assertTrue(layer_structure.get_is_signal_layer(1));
    }

    @Test
    public void test_get_name_layer() {
        System.out.println("get_name_layer");
        assertEquals("Layer#2", layer_structure.get_name_layer(2));
    }

    @Test
    public void test_get_no() {
        int expResult = 1, result;
        System.out.println("get_no");

        result = layer_structure.get_no(layer_structure.get_layer(expResult));
        assertEquals(expResult, result);

        result = layer_structure.get_no(layer_structure.get_signal_layer(0));
        assertEquals(expResult, result);
    }

    @After
    public void tearDown() {
        layer_structure = null;
        System.gc();
    }

}
