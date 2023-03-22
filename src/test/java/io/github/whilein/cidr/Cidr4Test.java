package io.github.whilein.cidr;

import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Cidr4Test {

    @Test
    void testParseNotation() {
        Cidr cidr = Cidr4.from("192.168.0.0/16");
        assertEquals(16, cidr.getMaskBits());

        for (int i = 0; i < 255; i++) {
            for (int j = 0; j < 255; j++) {
                assertTrue(cidr.contains(inet("192.168." + i + "." + j)));
            }
        }

        assertFalse(cidr.contains(inet("127.0.0.1")));
        assertFalse(cidr.contains(inet("191.168.0.0")));
        assertFalse(cidr.contains(inet("193.168.0.0")));
        assertFalse(cidr.contains(inet("192.167.0.0")));
        assertFalse(cidr.contains(inet("192.169.0.0")));
    }

    @Test
    void toNotation() {
        Cidr cidr = Cidr4.from("192.168.0.0/16");
        assertEquals("192.168.0.0/16", cidr.toNotation());
    }

    @Test
    void testCount1() {
        Cidr cidr = Cidr4.from("127.0.0.1/32");
        assertEquals(1, cidr.size());
    }

    @Test
    void testCount256() {
        Cidr cidr = Cidr4.from("127.0.0.1/24");
        assertEquals(256, cidr.size());
    }

    @Test
    void testRange() {
        InetRange range = Cidr4.from("192.168.0.0/16").toRange();
        assertEquals(inet("192.168.0.0"), range.getStart());
        assertEquals(inet("192.168.255.255"), range.getEnd());
    }

    @Test
    void testContains() {
        Cidr cidr = Cidr4.create(inet("192.168.0.0"), 16);

        for (int i = 0; i < 255; i++) {
            for (int j = 0; j < 255; j++) {
                assertTrue(cidr.contains(inet("192.168." + i + "." + j)));
            }
        }

        assertFalse(cidr.contains(inet("127.0.0.1")));
        assertFalse(cidr.contains(inet("191.168.0.0")));
        assertFalse(cidr.contains(inet("193.168.0.0")));
        assertFalse(cidr.contains(inet("192.167.0.0")));
        assertFalse(cidr.contains(inet("192.169.0.0")));
    }

    private static InetAddress inet(final String host) {
        try {
            return InetAddress.getByName(host);
        } catch (final UnknownHostException e) {
            throw new AssertionError(e);
        }
    }

}