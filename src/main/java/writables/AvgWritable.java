package writables;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Writable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class AvgWritable implements Writable {
    private final DoubleWritable sum;
    private final IntWritable count;

    public AvgWritable() {
        this.sum = new DoubleWritable(0);
        this.count = new IntWritable(0);
    }

    public AvgWritable(double sum, int count) {
        this.sum = new DoubleWritable(sum);
        this.count = new IntWritable(count);
    }

    public void set(double sum, int count) {
        this.sum.set(sum);
        this.count.set(count);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        sum.write(out);
        count.write(out);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        sum.readFields(in);
        count.readFields(in);
    }

    public double getSum() { return sum.get(); }
    public int getCount() { return count.get(); }
}