package utils;

import org.apache.hadoop.io.Text;

public class CSVParser {
    public static String[] parseAndValidate(Text line) {
        String[] columns = line.toString().split(";");
        if (columns.length < 10 || columns[0].trim().equals("country_or_area")) {
            return null;
        }
        return columns;
    }

}
