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

    @Override
    public int hashCode() {
        return 1 + (31 + start.hashCode()) * 31 + end.hashCode();
    }
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof InetRange)) return false;

        InetRange that = (InetRange) obj;
        return start.equals(that.start) && end.equals(that.end);
    }

    public InetAddress getStart() {
        return start;
    }

    public InetAddress getEnd() {
        return end;
    }
}
