import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import utils.CSVParser;


public class CountBrazilMapReduce {

    public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private final Text countryKey = new Text();

        // LongWritable is just the byte offset that hadoop uses to track the blocks it's sends to processing
        // we can ignore it, but it needs to be there
        @Override
        public void map(LongWritable key, Text line, Context context) throws IOException, InterruptedException {

            String[] columns = CSVParser.parseAndValidate(line);
            if (columns == null) {
                return;
            }

            String country = columns[0].trim();
            if (country.equalsIgnoreCase("Brazil")) {
                countryKey.set(country);
                context.write(countryKey, one);
            }
        }
    }
}