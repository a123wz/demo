package org.demo.data.hadoop.mulline;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class Test {
    /**
     * 
     * @author 汤高

     */
    //Map过程
    static int count=0;
    public static class MyTestMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
        /***
         * 
         */
        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, LongWritable>.Context context)
                throws IOException, InterruptedException {
            //默认的map的value是每一行,我这里自定义的是以空格分割
            count++;
            //String[] vs = value.toString().split(",");
            //for (String v : vs) {
                //写出去
                context.write(new Text(value), key);
            //}
            System.out.println("========>"+count);
        }
    }

    public static void main(String[] args) {

        Configuration conf=new Configuration();
        try {
            //args从控制台获取路径 解析得到域名
        	String inputPath = "hdfs://127.0.0.1:9000/output/part-r-00000";
    		String outputPath = "hdfs://127.0.0.1:9000/output";
    		args = new String[2];
    		args[0] = inputPath;
    		args[1] = outputPath;
            String[] paths=new GenericOptionsParser(conf,args).getRemainingArgs();
            if(paths.length<2){
                throw new RuntimeException("必須輸出 輸入 和输出路径");
            }

            //得到一个Job 并设置名字
            Job job=Job.getInstance(conf,"myTest");
            //设置Jar 使本程序在Hadoop中运行
            job.setJarByClass(Test.class);
            //设置Map处理类
            job.setMapperClass(MyTestMapper.class);
            job.setInputFormatClass(MyInputFormat.class);
            //设置map的输出类型,因为不一致,所以要设置
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(LongWritable.class);
            //设置输入和输出目录
            FileInputFormat.addInputPath(job, new Path(paths[0]));
            FileOutputFormat.setOutputPath(job, new Path(paths[1] + System.currentTimeMillis()));// 整合好结果后输出的位置
            //启动运行
            System.exit(job.waitForCompletion(true) ? 0:1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}