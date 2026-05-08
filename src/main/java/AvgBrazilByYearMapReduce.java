import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import writables.AvgWritable;
import utils.CSVParser;

import java.io.IOException;

public class AvgBrazilByYearMapReduce {

    public static class Map extends Mapper<LongWritable, Text, IntWritable, AvgWritable> {
        private final IntWritable yearKey = new IntWritable();
        private final AvgWritable avgValue = new AvgWritable();

        @Override
        public void map(LongWritable key, Text line, Context context) throws IOException, InterruptedException {
            String[] columns = CSVParser.parseAndValidate(line);
            if (columns == null) {
                return;
            }

            String country = columns[0].trim();
            if (country.equalsIgnoreCase("Brazil")) {

                String year = columns[1].trim();

                try {
                    long price = Long.parseLong(columns[5].trim());

                    yearKey.set(Integer.parseInt(year));
                    avgValue.set(price, 1);
                    context.write(yearKey, avgValue);

                } catch (NumberFormatException e) {
                    System.err.println("Skipping invalid price " + columns[5]);
                }
            }
        }
    }

    public static class Combine extends Reducer<IntWritable, AvgWritable, IntWritable, AvgWritable> {
        private final AvgWritable result = new AvgWritable();

        @Override
        public void reduce(IntWritable key, Iterable<AvgWritable> values, Context context)
                throws IOException, InterruptedException {

            long sum = 0L;
            int count = 0;

            for (AvgWritable val : values) {
                sum += val.getSum();
                count += val.getCount();
            }

            result.set(sum, count);
            context.write(key, result);
        }
    }

    public static class Reduce extends Reducer<IntWritable, AvgWritable, IntWritable, DoubleWritable> {

        private final DoubleWritable result = new DoubleWritable();

        @Override
        public void reduce(IntWritable key, Iterable<AvgWritable> values, Context context)
                throws IOException, InterruptedException {

            long sum = 0L;
            int count = 0;

            for (AvgWritable val : values) {
                sum += val.getSum();
                count += val.getCount();
            }

            if (count > 0) {
                double avg = (double) sum / count;
                result.set(avg);
                context.write(key, result);
            }
        }
    }
}