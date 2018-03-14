package org.demo.data.hadoop.mulline;

import java.io.IOException;
import java.io.InputStream;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.conf.Configuration;

@InterfaceAudience.Private
@InterfaceStability.Unstable
public class MySplitLineReader extends MyLineReader {
  public MySplitLineReader(InputStream in, byte[] recordDelimiterBytes) {
    super(in, recordDelimiterBytes);
  }

  public MySplitLineReader(InputStream in, Configuration conf,
      byte[] recordDelimiterBytes) throws IOException {
    super(in, conf, recordDelimiterBytes);
  }

  public boolean needAdditionalRecordAfterSplit() {
    return false;
  }
}