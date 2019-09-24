package org.demo.data.hadoop.rand;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.RecordReader;

import java.io.IOException;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

/**
 * @author luchunli
 * @description 自定义RecordReader
 *
 */
public class RandomRecordReader extends RecordReader<IntWritable, ArrayWritable> {
    private int start;
    private int end;
    private int index;

    private IntWritable key = null;
    private ArrayWritable value = null;
    private RandomInputSplit rsplit = null;

    @Override
    public void initialize(InputSplit split, TaskAttemptContext context)
            throws IOException, InterruptedException {
        this.rsplit = (RandomInputSplit)split;
        this.start = this.rsplit.getStart();
        this.end = this.rsplit.getEnd();
        this.index = this.start;
    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        if (null == key) {
            key = new IntWritable();
        }
        if (null == value) {
            value = new ArrayWritable(FloatWritable.class);
        }
        if (this.index <= this.end) {
            key.set(this.index);
            value = rsplit.getFloatArray();
            index = end + 1;
            return true;
        }
        return false;
    }

    @Override
    public IntWritable getCurrentKey() throws IOException, InterruptedException {
        return key;
    }

    @Override
    public ArrayWritable getCurrentValue() throws IOException, InterruptedException {
        return value;
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        if (this.index == this.end) {
            return 0F;
        }
        return Math.min(1.0F, (this.index - this.start) / (float)(this.end - this.start));
    }

    @Override
    public void close() throws IOException {
        // ......
    }

}
