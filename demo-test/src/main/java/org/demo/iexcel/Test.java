package org.demo.iexcel;

import com.github.houbb.iexcel.core.writer.IExcelWriter;
import com.github.houbb.iexcel.exception.ExcelRuntimeException;
import com.github.houbb.iexcel.util.excel.ExcelUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        excelWriter03Test();
    }

    public static void excelWriter03Test() {
        // 待生成的 excel 文件路径
        final String filePath = "D:/excelWriter03.xls";

        // 对象列表
        List<ExcelFieldModel> models = buildModelList();

        try(IExcelWriter excelWriter = ExcelUtil.get03ExcelWriter();
            OutputStream outputStream = new FileOutputStream(filePath)) {
            // 可根据实际需要，多次写入列表
            excelWriter.write(models);

            // 将列表内容真正的输出到 excel 文件
            excelWriter.flush(outputStream);
        } catch (IOException e) {
            throw new ExcelRuntimeException(e);
        }
    }

    /**
     * 构建测试的对象列表
     * @return 对象列表
     */
    private static List<ExcelFieldModel> buildModelList() {
        List<ExcelFieldModel> models = new ArrayList<>();
        ExcelFieldModel model = new ExcelFieldModel();
        model.setName("测试1号");
        model.setAge("25");
        model.setEmail("123@gmail.com");
        model.setAddress("贝克街23号");

        ExcelFieldModel modelTwo = new ExcelFieldModel();
        modelTwo.setName("测试2号");
        modelTwo.setAge("30");
        modelTwo.setEmail("125@gmail.com");
        modelTwo.setAddress("贝克街26号");

        models.add(model);
        models.add(modelTwo);
        return models;
    }
}
