package io.github.whilein.cidr;

import java.net.InetAddress;

public interface Cidr {

    int getMaskBits();

    String toNotation();

    InetRange toRange();

    long size();

    boolean contains(int address);

    boolean contains(InetAddress address);


}
