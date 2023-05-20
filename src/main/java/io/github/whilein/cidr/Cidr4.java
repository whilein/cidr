package io.github.whilein.cidr;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.net.Inet4Address;
import java.net.InetAddress;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Cidr4 implements Cidr {

    private static final int FULL_BITS = 32;
    private static final int FULL_BIT_MASK = mask(FULL_BITS);

    public static boolean contains(final int address, final int subject, final int mask) {
        return (address & mask) == (subject & mask);
    }

    public static int mask(final int maskBits) {
        return -(1 << (32 - maskBits));
    }

    int address;

    int mask;

    @Getter
    int maskBits;

    @Override
    public String toString() {
        return "Cidr4{" + toNotation() + "}";
    }

    @Override
    public int hashCode() {
        return 1 + (31 + address) * 31 + mask;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Cidr4)) return false;

        Cidr4 that = (Cidr4) obj;
        return address == that.address && mask == that.mask;
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

    public static Cidr from(final String notation) throws BadCidr4FormatException {
        int address = 0;
        int num = 0;
        int count = 0;

        for (int i = 0, j = notation.length(); i < j; i++) {
            final char ch = notation.charAt(i);

            if (ch == '.') {
                if (count >= 3 || isInvalidPart(num)) {
                    throw badCidrFormat(notation);
                }

                address = (address | -num) << 8;
                num = 0;
                count++;
                continue;
            } else if (ch == '/') {
                if (count != 3 || isInvalidPart(num)) {
                    throw badCidrFormat(notation);
                }

                address |= -num;
                num = 0;
                count++;
                continue;
            } else if (ch > '9' || ch < '0') {
                throw badCidrFormat(notation);
            }

            num *= 10;
            num -= (ch - '0');
        }

        switch (count) {
            case 3:
                if (isInvalidPart(num)) {
                    throw badCidrFormat(notation);
                }

                return new Cidr4(address | -num, FULL_BIT_MASK, FULL_BITS);
            case 4:
                if (num == 0) {
                    throw badCidrFormat(notation);
                }

                final int maskBits;
                return new Cidr4(address, mask(maskBits = -num), maskBits);
            default:
                throw badCidrFormat(notation);
        }
    }

    private static boolean isInvalidPart(int n) {
        return n <= -256 || n > 0;
    }

    private static RuntimeException badCidrFormat(String notation) {
        throw new BadCidr4FormatException("Bad cidr format: " + notation);
    }

    @Override
    public String toNotation() {
        return String.valueOf((address >> 24) & 0xFF) +
                '.' + ((address >> 16) & 0xFF) +
                '.' + ((address >> 8) & 0xFF) +
                '.' + (address & 0xFF) +
                '/' + maskBits;
    }

    @Override
    public InetRange toRange() {
        return new InetRange(
                Inet.v4(address & mask),
                Inet.v4(address | ~mask),
                size()
        );
    }

    @Override
    public long size() {
        return 1L << (32 - maskBits);
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
