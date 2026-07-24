package com.sporekart.platform.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IdGeneratorTest {

    @Test
    void testUuid() {
        String id = IdGenerator.uuid();
        assertNotNull(id);
        assertTrue(id.contains("-"));
    }

    @Test
    void testShortId() {
        String id = IdGenerator.shortId();
        assertEquals(12, id.length());
    }

    @Test
    void testPrefixedId() {
        String id = IdGenerator.prefixedId("ord");
        assertTrue(id.startsWith("ord_"));
        assertEquals(16, id.length());
    }

    @Test
    void testSequencedId() {
        String id = IdGenerator.sequencedId("inv", 42);
        assertEquals("inv_000042", id);
    }

    @Test
    void testUniqueIds() {
        assertNotEquals(IdGenerator.uuid(), IdGenerator.uuid());
    }
}
