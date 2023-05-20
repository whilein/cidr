package io.github.whilein.cidr;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;

class Cidr4Test {

    @Test
    void testParseNotation() {
        val cidr = Cidr4.from("192.168.0.0/16");
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
        val cidr = Cidr4.from("192.168.0.0/16");
        assertEquals("192.168.0.0/16", cidr.toNotation());
    }

    @Test
    void testCount1() {
        val cidr = Cidr4.from("127.0.0.1/32");
        assertEquals(1, cidr.size());
    }

    @Test
    void testCount256() {
        val cidr = Cidr4.from("127.0.0.1/24");
        assertEquals(256, cidr.size());
    }

    @Test
    void testBadFormat() {
        assertThrows(BadCidr4FormatException.class, () -> Cidr4.from("127.0.0"));
        assertThrows(BadCidr4FormatException.class, () -> Cidr4.from("127.0.0/1"));
        assertThrows(BadCidr4FormatException.class, () -> Cidr4.from("333.0.0.0"));
        assertThrows(BadCidr4FormatException.class, () -> Cidr4.from("0.333.0.0"));
        assertThrows(BadCidr4FormatException.class, () -> Cidr4.from("0.0.333.0"));
        assertThrows(BadCidr4FormatException.class, () -> Cidr4.from("0.0.0.333"));
        assertThrows(BadCidr4FormatException.class, () -> Cidr4.from("333.0.0.0/1"));
        assertThrows(BadCidr4FormatException.class, () -> Cidr4.from("0.333.0.0/1"));
        assertThrows(BadCidr4FormatException.class, () -> Cidr4.from("0.0.333.0/1"));
        assertThrows(BadCidr4FormatException.class, () -> Cidr4.from("0.0.0.333/1"));
        assertThrows(BadCidr4FormatException.class, () -> Cidr4.from("bla.bla.bla.bla"));
        assertThrows(BadCidr4FormatException.class, () -> Cidr4.from("bla.bla.bla.bla/1"));
        assertThrows(BadCidr4FormatException.class, () -> Cidr4.from("0.0.0.o"));
        assertThrows(BadCidr4FormatException.class, () -> Cidr4.from("0.0.0.o/1"));
        assertThrows(BadCidr4FormatException.class, () -> Cidr4.from("0.0.0.0/"));
        assertThrows(BadCidr4FormatException.class, () -> Cidr4.from("0.0.0/"));
        assertThrows(BadCidr4FormatException.class, () -> Cidr4.from("0.0.0.0.0"));
        assertThrows(BadCidr4FormatException.class, () -> Cidr4.from("0.0.0.0/1/1"));
        assertThrows(BadCidr4FormatException.class, () -> Cidr4.from("0.0.0.0/a"));
    }

    @Test
    void testCidrWithoutBits() {
        val cidr = Cidr4.from("127.0.0.1");
        assertEquals(1, cidr.size());
        assertEquals(32, cidr.getMaskBits());

        val range = cidr.toRange();
        assertEquals(range.getStart(), range.getEnd());
        assertEquals("127.0.0.1", range.getStart().getHostAddress());
    }

    @Test
    void testRange() {
        val range = Cidr4.from("192.168.0.0/16").toRange();
        assertEquals(inet("192.168.0.0"), range.getStart());
        assertEquals(inet("192.168.255.255"), range.getEnd());
    }

    @Test
    void testContains() {
        val cidr = Cidr4.create(inet("192.168.0.0"), 16);

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