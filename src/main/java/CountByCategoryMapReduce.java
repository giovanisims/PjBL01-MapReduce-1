import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class CountByCategoryMapReduce {
    public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private final Text categoryKey = new Text();

        @Override
        public void map(LongWritable key, Text line, Context context) throws IOException, InterruptedException {
            String[] columns = line.toString().split(";");
            if (columns.length < 10 || line.toString().startsWith("country_or_area")) { return; }

            String category = columns[9].trim();
            categoryKey.set(category);
            context.write(categoryKey, one);
        }
    }
}