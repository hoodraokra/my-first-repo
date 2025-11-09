package com.example.math;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BooleanLogicTest {

    @Test
    void testAnd() {
        assertTrue(BooleanLogic.And(true, true));
        assertFalse(BooleanLogic.And(true, false));
    }

    @Test
    void testOr() {
        assertTrue(BooleanLogic.Or(true, false));
        assertFalse(BooleanLogic.Or(false, false));
    }

    @Test
    void testNot() {
        assertTrue(BooleanLogic.Not(false));
        assertFalse(BooleanLogic.Not(true));
    }

    @Test
    void testXor() {
        assertTrue(BooleanLogic.Xor(true, false));
        assertFalse(BooleanLogic.Xor(true, true));
    }
}