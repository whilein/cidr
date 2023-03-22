package io.github.whilein.cidr;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.net.InetAddress;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public final class InetRange {

    InetAddress start, end;

    @Accessors(fluent = true)
    long size;

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

}
