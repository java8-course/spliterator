package spliterators.part2.exercise;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class IndexedDoublePair {
    private final long index;
    private final double value;

    public IndexedDoublePair(long index, double value) {
        this.index = index;
        this.value = value;
    }

    public long getIndex() {
        return index;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("index", index)
                .append("value", value)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        IndexedDoublePair that = (IndexedDoublePair) o;

        return new EqualsBuilder()
                .append(index, that.index)
                .append(value, that.value)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(index)
                .append(value)
                .toHashCode();
    }
}
