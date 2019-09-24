package org.demo.data.hadoop.rand;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * @author luchunli
 * @description Mapper
 */
public class RandomMapper extends Mapper<IntWritable, ArrayWritable, IntWritable, FloatWritable> {
    private static final IntWritable one = new IntWritable(1);

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        // 为了查看当前map是在那台机器上执行的，在该机器上创建个随机文件，
        // 执行完成后到DN节点对应目录下查看即可
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
        File file = new File("/home/hadoop", "Mapper-" + format.format(new Date()));
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    @Override
    protected void map(IntWritable key, ArrayWritable value, Context context)
            throws IOException, InterruptedException {
        FloatWritable [] floatArray = (FloatWritable[])value.toArray();
        float maxValue = floatArray[0].get();
        float tmp = 0;

        for (int i = 1; i < floatArray.length; i++) {
            tmp = floatArray[i].get();
            if (tmp > maxValue) {
                maxValue = tmp;
            }
        }

        // 这里必须要保证多个map输出的key是一样的，否则reduce处理时会认为是不同的数据，
        // shuffle会分成多个组，导致每个map task算出一个最大值
        context.write(one, new FloatWritable(maxValue));
    }
}