import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import utils.CSVParser;
import writables.CountryYearWritable;
import writables.MinMaxWritable;

import java.io.IOException;

public class MinMaxAmountByCountryYearMapReduce {

    public static class Map extends Mapper<LongWritable, Text, CountryYearWritable, MinMaxWritable> {
        private final CountryYearWritable compositeKey = new CountryYearWritable();
        private final MinMaxWritable minMax = new MinMaxWritable();

        @Override
        public void map(LongWritable key, Text line, Context context) throws IOException, InterruptedException {
            String[] columns = CSVParser.parseAndValidate(line);
            if (columns == null)
                return;

            String country = columns[0].trim();
            String year = columns[1].trim();

            try {
                double price = Double.parseDouble(columns[5].trim());
                compositeKey.set(country, Integer.parseInt(year));
                minMax.set(price, price);
                context.write(compositeKey, minMax);
            } catch (NumberFormatException e) {
            }
        }
    }

    public static class Reduce
            extends Reducer<CountryYearWritable, MinMaxWritable, CountryYearWritable, MinMaxWritable> {
        private final MinMaxWritable result = new MinMaxWritable();

        @Override
        public void reduce(CountryYearWritable key, Iterable<MinMaxWritable> values, Context context)
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
