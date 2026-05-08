package writables;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Writable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class MinMaxWritable implements Writable {
    private final LongWritable min;
    private final LongWritable max;

    public MinMaxWritable() {
        this.min = new LongWritable();
        this.max = new LongWritable();
    }

    public MinMaxWritable(long min, long max) {
        this.min = new LongWritable(min);
        this.max = new LongWritable(max);
    }

    public void set(long min, long max) {
        this.min.set(min);
        this.max.set(max);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        min.write(out);
        max.write(out);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        min.readFields(in);
        max.readFields(in);
    }

    public long getMin() { return min.get(); }
    public long getMax() { return max.get(); }

    @Override
    public String toString() {
        return "Min: " + min.get() + " | Max: " + max.get();
    }
}
