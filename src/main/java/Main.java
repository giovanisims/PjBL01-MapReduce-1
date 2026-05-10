import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.reduce.IntSumReducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Main {
    public static void main(String[] args) throws Exception {

        // IF RUNNING THROUGH IDE USE THIS VARIABLE TO CHOOSE THE TASK
        String taskId = "8";
        taskId = args.length > 0 ? args[0] : taskId;

        Configuration conf = new Configuration();
        conf.set("mapreduce.framework.name", "local");
        conf.set("fs.defaultFS", "file:///");

        Job job = Job.getInstance(conf, "Task " + taskId);
        job.setJarByClass(Main.class);

        Path inputPath = new Path("input_data/dataset.csv");
        Path outputPath = new Path("output_data/task_" + taskId);

        switch (taskId) {
            case "1":
                job.setMapperClass(CountBrazilMapReduce.Map.class);
                job.setReducerClass(IntSumReducer.class);
                job.setOutputKeyClass(Text.class);
                job.setOutputValueClass(IntWritable.class);
                break;

            case "2":
                job.setMapperClass(CountByYearMapReduce.Map.class);
                job.setReducerClass(IntSumReducer.class);
                job.setOutputKeyClass(Text.class);
                job.setOutputValueClass(IntWritable.class);
                break;

            case "3":
                job.setMapperClass(CountByCategoryMapReduce.Map.class);
                job.setReducerClass(IntSumReducer.class);
                job.setOutputKeyClass(Text.class);
                job.setOutputValueClass(IntWritable.class);
                break;

            case "4":
                job.setMapperClass(CountByFlowMapReduce.Map.class);
                job.setReducerClass(IntSumReducer.class);
                job.setOutputKeyClass(Text.class);
                job.setOutputValueClass(IntWritable.class);
                break;

            case "5":
                job.setMapperClass(AvgBrazilByYearMapReduce.Map.class);
                job.setCombinerClass(AvgBrazilByYearMapReduce.Combine.class);
                job.setReducerClass(AvgBrazilByYearMapReduce.Reduce.class);
                job.setMapOutputValueClass(writables.AvgWritable.class);
                job.setOutputKeyClass(IntWritable.class);
                job.setOutputValueClass(Text.class);
                break;

            case "6":
                job.setMapperClass(MinMaxBrazil2016MapReduce.Map.class);
                job.setCombinerClass(MinMaxBrazil2016MapReduce.Combine.class);
                job.setReducerClass(MinMaxBrazil2016MapReduce.Reduce.class);
                job.setMapOutputValueClass(writables.MinMaxWritable.class);
                job.setOutputKeyClass(Text.class);
                job.setOutputValueClass(writables.MinMaxWritable.class);
                break;

            case "7":
                job.setMapperClass(AvgExportBrazilByYearMapReduce.Map.class);
                job.setCombinerClass(AvgExportBrazilByYearMapReduce.Combine.class);
                job.setReducerClass(AvgExportBrazilByYearMapReduce.Reduce.class);
                job.setMapOutputValueClass(writables.AvgWritable.class);
                job.setOutputKeyClass(IntWritable.class);
                job.setOutputValueClass(Text.class);
                break;

            case "8":
                job.setMapperClass(MinMaxAmountByCountryYearMapReduce.Map.class);
                job.setCombinerClass(MinMaxAmountByCountryYearMapReduce.Reduce.class);
                job.setReducerClass(MinMaxAmountByCountryYearMapReduce.Reduce.class);
                job.setMapOutputValueClass(writables.MinMaxWritable.class);
                job.setMapOutputKeyClass(writables.CountryYearWritable.class);
                job.setOutputKeyClass(writables.CountryYearWritable.class);
                job.setOutputValueClass(writables.MinMaxWritable.class);
                break;

            default:
                System.out.println("Task " + taskId + " not found");
                System.exit(1);
        }

        // AUTO-DELETE EXISTING OUTPUT FOLDER
        FileSystem fs = FileSystem.get(conf);
        Path absoluteOutputPath = outputPath.makeQualified(fs.getUri(), fs.getWorkingDirectory());
        if (fs.exists(absoluteOutputPath)) {
            System.out.println("Deleting existing output directory: " + absoluteOutputPath);
            fs.delete(absoluteOutputPath, true);
        }

        FileInputFormat.addInputPath(job, inputPath);
        FileOutputFormat.setOutputPath(job, absoluteOutputPath);

        boolean success = job.waitForCompletion(true);
        if (success) {
            System.out.println("Job completed successfully!");
        } else {
            System.err.println("Job failed!");
        }

        System.exit(success ? 0 : 1);
    }
}