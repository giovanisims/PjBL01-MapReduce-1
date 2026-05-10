import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import utils.CSVParser;
import writables.MinMaxWritable;

import java.io.IOException;

public class MinMaxBrazil2016MapReduce {

    public static class Map extends Mapper<LongWritable, Text, Text, MinMaxWritable> {
        private final Text resultKey = new Text("Brazil 2016");
        private final MinMaxWritable minMax = new MinMaxWritable();

        @Override
        public void map(LongWritable key, Text line, Context context) throws IOException, InterruptedException {
            String[] columns = CSVParser.parseAndValidate(line);
            if (columns == null)
                return;

            String country = columns[0].trim();
            String year = columns[1].trim();

            if (country.equalsIgnoreCase("Brazil") && year.equals("2016")) {
                try {
                    double price = Double.parseDouble(columns[5].trim());
                    minMax.set(price, price);
                    context.write(resultKey, minMax);
                } catch (NumberFormatException e) {
                }
            }
        }
    }

    // The combiner here will be identical to the reducer but it's set as a
    // requirement in the question
    public static class Combine extends Reducer<Text, MinMaxWritable, Text, MinMaxWritable> {
        private final MinMaxWritable result = new MinMaxWritable();

        @Override
        public void reduce(Text key, Iterable<MinMaxWritable> values, Context context)
                throws IOException, InterruptedException {
            double min = Double.MAX_VALUE;
            double max = Double.MIN_VALUE;

            for (MinMaxWritable val : values) {
                if (val.getMin() < min)
                    min = val.getMin();
                if (val.getMax() > max)
                    max = val.getMax();
            }
            result.set(min, max);
            context.write(key, result);
        }
    }

    public static class Reduce extends Reducer<Text, MinMaxWritable, Text, MinMaxWritable> {
        private final MinMaxWritable result = new MinMaxWritable();

        @Override
        public void reduce(Text key, Iterable<MinMaxWritable> values, Context context)
                throws IOException, InterruptedException {
            double min = Double.MAX_VALUE;
            double max = Double.MIN_VALUE;

            for (MinMaxWritable val : values) {
                if (val.getMin() < min)
                    min = val.getMin();
                if (val.getMax() > max)
                    max = val.getMax();
            }
            result.set(min, max);
            context.write(key, result);
        }
    }
}
