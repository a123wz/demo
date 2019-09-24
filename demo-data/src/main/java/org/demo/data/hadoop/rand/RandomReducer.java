package org.demo.data.hadoop.rand;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * @author luchunli
 * @description Rducer
 */
public class RandomReducer extends Reducer<IntWritable, FloatWritable, Text, FloatWritable> {
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
        // 为了查看当前reduce是在那台机器上执行的，在该机器上创建个随机文件
        File file = new File("/home/hadoop", "Reducer-" + format.format(new Date()));
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    @Override
    protected void reduce(IntWritable key, Iterable<FloatWritable> value, Context context)
            throws IOException, InterruptedException {
        Iterator<FloatWritable> it = value.iterator();
        float maxValue = 0;
        float tmp = 0;
        if (it.hasNext()) {
            maxValue = it.next().get();
        } else {
            context.write(new Text("The max value is : "), new FloatWritable(maxValue));
            return;
        }

        while (it.hasNext()) {
            tmp = it.next().get();
            if (tmp > maxValue) {
                maxValue = tmp;
            }
        }
        context.write(new Text("The max value is : "), new FloatWritable(maxValue));
    }
}