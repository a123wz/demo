package org.demo.test;

import java.io.*;

/**
 * @author  删除匹配字符串
 * @version 1.0.0
 * @Description
 * @createTime 2021年04月19日 16:26:00
 */
public class FileUtil {

    public static String regex = ".*(@TableField|@TableName).*";

    public static void main(String[] args) {
//        fileList("F:\\work\\terminal-report-common\\src\\main\\java\\com\\vivo\\it\\vwork\\entity\\common\\store");
//        fileList("F:\\work\\terminal-report-common\\src\\main\\java\\com\\vivo\\it\\vwork\\entity\\common\\product");
        fileList("F:\\work\\terminal-report-common\\src\\main\\java\\com\\vivo\\it\\vwork\\entity\\common\\salesarea");
//        fileList("");
    }

    public static void fileList(String pathStr){
        File path = new File(pathStr);
        File[] files = path.listFiles(f->{
            return f.isFile()&&f.getName().indexOf("java")>0;
        });
        for (File f: files) {
            String content = readLine(f);
            writeFile(content,f);
        }
        File[] dirs = path.listFiles(f->{
            return f.isDirectory();
        });
        if(dirs!=null){
            for (File f: dirs) {
                fileList(f.getAbsolutePath());
            }
        }
    }

    public static String readLine(File file){
        InputStream in = null;
        InputStreamReader inr = null;
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try{
            in = new FileInputStream(file);
            // 将字节输入流转化成字符输入流，并设置编码格式，InputStreamReader为 Reader 的子类
            inr = new InputStreamReader(in, "UTF-8");
            // 使用 BufferedReader 进行读取
            bufferedReader = new BufferedReader(inr);
            String line = null;
            while( null != (line = bufferedReader.readLine()) ) {
                if(!line.matches(regex)){
                    stringBuilder.append(line+"\n");
                }else{
                    System.out.println(line);
                }
            }
            return stringBuilder.toString();
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("文件读取失败");
        }finally {
            try {
                bufferedReader.close();
                inr.close();
                in.close();
            }catch (Exception e){

            }
        }
    }

    public static void writeFile(String content,File file){
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            out.write(content);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (out != null) out.close();
            }catch (Exception e){

            }
        }
    }
}
