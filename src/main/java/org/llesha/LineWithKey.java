package org.llesha;

import java.util.Objects;

import static java.lang.Integer.parseInt;

public class LineWithKey implements Comparable<LineWithKey> {
    private final long key;
    private final String content;

    public long getKey() {
        return key;
    }

    public LineWithKey(String line) {
        int delimiterIndex = line.indexOf(Config.getDelimiter());
        String number = line.substring(0, delimiterIndex);
        key = parseInt(number);
        content = line.substring(delimiterIndex + 1);
    }

    @Override
    public String toString() {
        return key + Config.getDelimiter() + content;
    }

    @Override
    public int compareTo(LineWithKey o) {
        return Long.compare(key, o.key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineWithKey that = (LineWithKey) o;
        return key == that.key && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, content);
    }
}
