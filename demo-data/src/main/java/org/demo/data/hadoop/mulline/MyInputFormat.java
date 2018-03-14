package org.demo.data.hadoop.mulline;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.io.compress.SplittableCompressionCodec;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import com.google.common.base.Charsets;

public class MyInputFormat extends FileInputFormat<LongWritable, Text> {
	// 用来压缩的
	@Override
	protected boolean isSplitable(JobContext context, Path file) {
		final CompressionCodec codec = new CompressionCodecFactory(context.getConfiguration()).getCodec(file);
		if (null == codec) {
			return true;
		}
		return codec instanceof SplittableCompressionCodec;
	}

	@Override
	public RecordReader<LongWritable, Text> createRecordReader(InputSplit genericSplit, TaskAttemptContext context)
			throws IOException, InterruptedException {
//		 String delimiter = context.getConfiguration().get(
//		 "textinputformat.record.delimiter");
//		 byte[] recordDelimiterBytes = null;
//		 if (null != delimiter)
//		 recordDelimiterBytes = delimiter.getBytes(Charsets.UTF_8);
//		 return new MyRecordReader(recordDelimiterBytes);
		context.setStatus(genericSplit.toString());
		return new MyRecordReader(context.getConfiguration());
	}

}
