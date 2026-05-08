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

        // IF RUNNING THROUGH IDE CHANGE USE THIS VARIABLE TO CHOOSE THE TASK
        String taskId = "2";
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

            default:
                System.out.println("Task " + taskId + " not found");
                System.exit(1);
        }

        // AUTO-DELETE EXISTING OUTPUT FOLDER
        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(outputPath)) {
            fs.delete(outputPath, true);
        }

        FileInputFormat.addInputPath(job, inputPath);
        FileOutputFormat.setOutputPath(job, outputPath);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}