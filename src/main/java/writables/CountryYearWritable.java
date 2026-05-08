package writables;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

public class CountryYearWritable implements WritableComparable<CountryYearWritable> {
    private final Text country;
    private final IntWritable year;

    public CountryYearWritable() {
        this.country = new Text();
        this.year = new IntWritable();
    }

    public CountryYearWritable(String country, int year) {
        this.country = new Text(country);
        this.year = new IntWritable(year);
    }

    public void set(String country, int year) {
        this.country.set(country);
        this.year.set(year);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        country.write(out);
        year.write(out);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        country.readFields(in);
        year.readFields(in);
    }

    @Override
    public int compareTo(CountryYearWritable other) {
        int cmp = this.country.compareTo(other.country);
        if (cmp != 0) {
            return cmp;
        }
        return this.year.compareTo(other.year);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountryYearWritable that = (CountryYearWritable) o;
        return Objects.equals(country, that.country) && Objects.equals(year, that.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, year);
    }

    @Override
    public String toString() {
        return country + " (" + year + ")";
    }

    public String getCountry() { return country.toString(); }
    public int getYear() { return year.get(); }
}
