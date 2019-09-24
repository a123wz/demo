package org.demo.data.hadoop.rand;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputSplit;

/**
 * @author luchunli
 * @description
 *     自定义InputSplit,参照了{@link org.apache.hadoop.mapreduce.lib.input.Filesplit}
 *  <br/>
 *     FileSplit是针对HDFS上文件的实现，因此其属性包括文件绝对路径(Path)、分片起始位置(start)、
 *     分片长度(length)、副本信息(保存Block复本数据到的主机数组)。
 *     <br/>
 *  自定义的InputSplit是针对内存中的数组数据进行的处理，因此无需记录文件路径及副本信息，只需要记录对数组分片的起始位置、分片长度即可。
 * </pre>
 */
public class RandomInputSplit extends InputSplit implements Writable {
    private int start;
    private int end;
    private ArrayWritable floatArray = new ArrayWritable(FloatWritable.class);

    public RandomInputSplit () {}

    /**
     * Constructs a split
     *
     * @param start
     * @param end
     *
     */
    public RandomInputSplit (int start, int end) {
        this.start = start;
        this.end = end;

        int len = this.end - this.start + 1;
        int index = start;
        FloatWritable [] result = new FloatWritable[len];
        for (int i = 0; i < len; i++) {
            float f = RandomInputFormat.floatValues[index];
            FloatWritable fw = new FloatWritable(f);

            result[i] = fw;

            index++;
        }
        floatArray.set(result);

//        System.out.println("查看分片数据：");
//        for (FloatWritable fw : (FloatWritable[])floatArray.toArray()) {
//            System.out.println(fw.get());
//        }
//        System.out.println("=====================");
    }

    @Override
    public long getLength() throws IOException, InterruptedException {
        return this.end - this.start;
    }

    @Override
    public String[] getLocations() throws IOException, InterruptedException {
        return new String[]{"dnode1", "dnode2"};
    }

//    @Override
    public void readFields(DataInput in) throws IOException {
        this.start = in.readInt();
        this.end = in.readInt();
        this.floatArray.readFields(in);
    }

//    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(this.getStart());
        out.writeInt(this.getEnd());
        this.floatArray.write(out);
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public ArrayWritable getFloatArray() {
        return floatArray;
    }

    @Override
    public String toString() {
        return this.getStart() + "-" + this.getEnd();
    }
}