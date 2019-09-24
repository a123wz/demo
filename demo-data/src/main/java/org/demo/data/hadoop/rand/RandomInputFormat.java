package org.demo.data.hadoop.rand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

/**
 * @author luchunli
 * @description 自定义InputFormat
 */
public class RandomInputFormat extends InputFormat<IntWritable, ArrayWritable> {

    public static float [] floatValues = null;

    /** 自定义分片规则 **/
    @Override
    public List<InputSplit> getSplits(JobContext context) throws IOException,
            InterruptedException {
        // 初始化数组的长度
        int NumOfValues = context.getConfiguration().getInt("lucl.random.nums", 100);
        floatValues = new float[NumOfValues];

        Random random = new Random ();
        for (int i = 0; i < NumOfValues; i++) {
            floatValues[i] = random.nextFloat();
        }
        System.out.println("生成的随机数的值如下：");
        for (float f : floatValues) {
            System.out.println(f);
        }
        System.out.println("====================");

        // 如下代码表示指定两个map task来处理这100个小数，每个map task处理50个小数
        // 初始化split分片数目，split分片的数量等于map任务的数量，但是也可以通过配置参数mapred.map.tasks来指定
        // 如果该参数和splits的切片数不一致时，map task的数目如何确定，后续再通过代码分析
        int NumSplits = context.getConfiguration().getInt("mapreduce.job.maps", 2);
        int begin = 0;
        // Math.floor是为了下取整，这里是100刚好整除，如果是99的话Math.floor的值是49.0
        // 50
        int length = (int)Math.floor(NumOfValues / NumSplits);
        // end = 49，第一个split的范围就是0~49
        int end = length - 1;

        // 默认的FileInputFormat类的getSplits方法中是通过文件数目和blocksize进行分的，
        // 文件超过一个块会分成多个split，否则一个文件一个split分片
        List<InputSplit> splits = new ArrayList<InputSplit>();
        for (int i = 0; i < NumSplits - 1; i++) {    // 2个splits分片，分别为0和1
            RandomInputSplit split = new RandomInputSplit(begin, end);
            splits.add(split);

            // begin是上一个split切片的下一个值
            begin = end + 1;        // 50
            // 切片的长度不变，结束位置为起始位置+分片的长度，而数组下标是从0开始的，
            // 因此结束位置应该是begin加长度-1
            end = begin + (length - 1);    // 50 + (50 -1) = 99
        }
        RandomInputSplit split = new RandomInputSplit(begin, end);
        splits.add(split);

        /**
         * <pre>
         *     通过默认的TextInputFormat来实现的时候，如果有两个小文件，则splits=2，参见：
         *     http://luchunli.blog.51cto.com/2368057/1676185
         * </pre>
         */

        return splits;
    }

    @Override
    public RecordReader<IntWritable, ArrayWritable> createRecordReader(InputSplit split,
                                                                       TaskAttemptContext context) throws IOException, InterruptedException {
        return new RandomRecordReader();
    }
}
