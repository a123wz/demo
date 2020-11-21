package org.demo.drools;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class SourceData {

    private String type;

    private String rule;

    private Integer size;

    private Integer count;
}
