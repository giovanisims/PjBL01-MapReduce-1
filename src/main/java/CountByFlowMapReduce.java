import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;
import utils.CSVParser;


public class CountByFlowMapReduce {
    public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private final Text flowKey = new Text();

        @Override
        public void map(LongWritable key, Text line, Context context) throws IOException, InterruptedException {
            String[] columns = CSVParser.parseAndValidate(line);
            if (columns == null) {
                return;
            }

            String flow = columns[4].trim();
            flowKey.set(flow);
            context.write(flowKey, one);
        }
    }
}