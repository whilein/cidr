package io.github.whilein.cidr;

import java.net.InetAddress;

public final class InetRange {

    private final InetAddress start, end;

    public InetRange(final InetAddress start, final InetAddress end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "InetRange{" + start.getHostAddress() + " -> " + end.getHostAddress() + "}";
    }

    public InetAddress getStart() {
        return start;
    }

    public InetAddress getEnd() {
        return end;
    }
}
