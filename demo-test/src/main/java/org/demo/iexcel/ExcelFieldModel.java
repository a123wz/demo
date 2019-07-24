package org.demo.iexcel;

import com.github.houbb.iexcel.annotation.ExcelField;
import lombok.Data;

@Data
public class ExcelFieldModel {

    @ExcelField
    private String name;

    @ExcelField(headName = "年龄")
    private String age;

    @ExcelField(mapKey = "EMAIL", writeRequire = false, readRequire = false)
    private String email;

    @ExcelField(mapKey = "ADDRESS", headName = "地址", writeRequire = true)
    private String address;
}
