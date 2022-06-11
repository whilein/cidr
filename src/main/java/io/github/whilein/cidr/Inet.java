package io.github.whilein.cidr;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.net.Inet4Address;
import java.net.InetAddress;

public final class Inet {

    private static final MethodHandle INET_ADDRESS__INIT;

    static {
        MethodHandle inetAddressInit;
        try {
            final Field lookupField = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
            lookupField.setAccessible(true);

            final MethodHandles.Lookup lookup = (MethodHandles.Lookup) lookupField.get(null);

            inetAddressInit = lookup.findConstructor(Inet4Address.class,
                    MethodType.methodType(void.class, String.class, int.class));
        } catch (final Exception e) {
            inetAddressInit = null;
        }

        INET_ADDRESS__INIT = inetAddressInit;
    }

    public static InetAddress v4(final int address) {
        try {
            if (INET_ADDRESS__INIT == null) {
                final byte[] bytes = new byte[4];
                bytes[3] = (byte) (address & 0xFF);
                bytes[2] = (byte) ((address >> 8) & 0xFF);
                bytes[1] = (byte) ((address >> 16) & 0xFF);
                bytes[0] = (byte) ((address >> 24) & 0xFF);

                return InetAddress.getByAddress(bytes);
            }

            return (Inet4Address) INET_ADDRESS__INIT.invokeExact("", address);
        } catch (final Throwable e) {
            throw new RuntimeException(e);
        }
    }

}
