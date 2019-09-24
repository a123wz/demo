package org.demo.data.hadoop.rand;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.sun.jersey.core.impl.provider.entity.XMLJAXBElementProvider.Text;

/**
 * @author luchunli
 * @description MapReduce自带的输入类都是基于HDFS的，如下示例代码不用从HDFS上面读取内容，
 * 而是在内存里面随机生成100个（0-1）float类型的小数，然后求这100个小数的最大值。
 */

/**
 * 执行方法 hadoop jar RandomMR.jar /201512020027
 * 查看结果
 * [hadoop@nnode code]$ hdfs dfs -ls /201512020027
 * Found 2 items
 * -rw-r--r--   2 hadoop hadoop          0 2015-12-02 00:29 /201512020027/_SUCCESS
 * -rw-r--r--   2 hadoop hadoop         31 2015-12-02 00:29 /201512020027/part-r-00000
 * [hadoop@nnode code]$ hdfs dfs -text /201512020027/part-r-00000
 * The max value is :      0.98296344
 */
public class RandomDriver extends Configured implements Tool {

    public static void main(String[] args) {
        try {
            ToolRunner.run(new RandomDriver(), args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int run(String[] args) throws Exception {
        Configuration conf = this.getConf();
        conf.set("lucl.random.nums", "100");
        conf.set("mapreduce.job.maps", "2");

        Job job = Job.getInstance(getConf(), this.getClass().getSimpleName());

        job.setJarByClass(RandomDriver.class);

        job.setInputFormatClass(RandomInputFormat.class);

        job.setMapperClass(RandomMapper.class);
        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(FloatWritable.class);

        job.setReducerClass(RandomReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FloatWritable.class);

        String outputPath = "hdfs://127.0.0.1:9000/output1";
        FileOutputFormat.setOutputPath(job, new Path(outputPath));

        FileInputFormat.addInputPath(job, new Path(outputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));

        return job.waitForCompletion(true) ? 0 : 1;
    }

}