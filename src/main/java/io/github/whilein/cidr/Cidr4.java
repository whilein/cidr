package io.github.whilein.cidr;

import java.net.Inet4Address;
import java.net.InetAddress;

public final class Cidr4 implements Cidr {

    public static boolean contains(final int address, final int subject, final int mask) {
        return (address & mask) == (subject & mask);
    }

    public static int mask(final int maskBits) {
        return -(1 << (32 - maskBits));
    }

    private final int address;

    private final int mask;
    private final int maskBits;

    private Cidr4(final int address, final int mask, final int maskBits) {
        this.address = address;
        this.mask = mask;
        this.maskBits = maskBits;
    }

    public static Cidr create(final InetAddress address, final int maskBits) {
        if (!(address instanceof Inet4Address)) {
            throw new IllegalArgumentException("Provided argument is not ipv4 address");
        }

        return new Cidr4(address.hashCode(), mask(maskBits), maskBits);
    }

    public static Cidr create(final int address, final int maskBits) {
        return new Cidr4(address, mask(maskBits), maskBits);
    }

    public static Cidr from(final String notation) {
        int address = 0;
        int num = 0;

        for (int i = 0, j = notation.length(); i < j; i++) {
            final char ch = notation.charAt(i);

            if (ch == '.') {
                address = (address | -num) << 8;
                num = 0;
                continue;
            }

            if (ch == '/') {
                address |= -num;
                num = 0;
                continue;
            }

            num *= 10;
            num -= (ch - '0');
        }

        final int maskBits;
        return new Cidr4(address, mask(maskBits = -num), maskBits);
    }

    @Override
    public int getMaskBits() {
        return maskBits;
    }

    @Override
    public InetRange toRange() {
        return new InetRange(
                Inet.v4(address & mask),
                Inet.v4(address | ~mask)
        );
    }

    @Override
    public boolean contains(final int address) {
        return contains(this.address, address, mask);
    }

    @Override
    public boolean contains(final InetAddress address) {
        return address instanceof Inet4Address && contains(address.hashCode());
    }
}
