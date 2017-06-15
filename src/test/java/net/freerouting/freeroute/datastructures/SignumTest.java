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
package net.freerouting.freeroute.datastructures;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Robert Buj
 */
public class SignumTest {
    
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    public SignumTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of of method, of class Signum.
     */
    @Test
    public void testOf() {
        System.out.println("of");
        double p_value;
        Signum expResult, result;

        p_value = 0.0;
        expResult = Signum.ZERO;
        result = Signum.of(p_value);
        assertEquals(expResult, result);

        p_value = 10.0;
        expResult = Signum.POSITIVE;
        result = Signum.of(p_value);
        assertEquals(expResult, result);

        p_value = -10.0;
        expResult = Signum.NEGATIVE;
        result = Signum.of(p_value);
        assertEquals(expResult, result);
    }

    /**
     * Test of as_int method, of class Signum.
     */
    @Test
    public void testAs_int() {
        System.out.println("as_int");
        double p_value;
        int expResult;
        int result;

        p_value = 0.0;
        expResult = 0;
        result = Signum.as_int(p_value);
        assertEquals(expResult, result);

        p_value = -10.0;
        expResult = -1;
        result = Signum.as_int(p_value);
        assertEquals(expResult, result);

        p_value = 10.0;
        expResult = 1;
        result = Signum.as_int(p_value);
        assertEquals(expResult, result);
    }

    /**
     * Test of negate method, of class Signum.
     */
    @Test
    public void testNegate() {
        System.out.println("negate");
        Signum instance, expResult, result;

        instance = Signum.NEGATIVE;
        expResult = Signum.POSITIVE;
        result = instance.negate();
        assertEquals(expResult, result);

        instance = Signum.POSITIVE;
        expResult = Signum.NEGATIVE;
        result = instance.negate();
        assertEquals(expResult, result);

        instance = Signum.ZERO;
        expResult = Signum.ZERO;
        result = instance.negate();
        assertEquals(expResult, result);
    }
}
