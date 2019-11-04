package org.demo.htmlparser;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.output.StringBuilderWriter;
import org.htmlparser.Parser;
import org.htmlparser.filters.*;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.io.*;

@Slf4j
public class AreaParser {

    public static NodeList check(String url,String nodeSelect){
        return check(url,nodeSelect,null);
    }

    public static NodeList check(String url,String nodeSelect,String noSelect){
        try {
            Thread.sleep(200);
        }catch (Exception e1){

        }
        Parser parser=new Parser();
        CssSelectorNodeFilter cla= new CssSelectorNodeFilter(nodeSelect);
        try {
            parser.setURL(url);
            NodeList list = parser.extractAllNodesThatMatch(cla);
            if(noSelect!=null){
                NotFilter filter = new NotFilter(new HasChildFilter(new TagNameFilter(noSelect)));
                list = list.extractAllNodesThatMatch(filter);
            }

//            log.info(list.size());
            return list;
        } catch (ParserException e) {
            e.printStackTrace();
            log.error("获取url:{}错误nodeSelect:{}",url,nodeSelect);
            try {
                Thread.sleep(200);
                return check(url, nodeSelect, noSelect);
            }catch (Exception e1){

            }
        }
        return new NodeList();
    }

    public static void checkProvince(String url){
        NodeList list = check(url,".provincetable a");
        System.out.println(url+""+list.size());
        for (int i = 0; i < list.size(); i++) {
            LinkTag node=(LinkTag) list.elementAt(i);
//            LinkTag textNode=(LinkTag) list.elementAt(i+1);
            String link = node.getLink();
            String code = link.substring(link.lastIndexOf("/")+1,link.lastIndexOf("."))+"0000000000";
//            log.info("省份:"+node.getLinkText()+" code:"+code);
            dataInit(code,node.getLinkText(),"0","1");
//            checkCity(node.getLink(),code);
            Thread t = new Thread(()->{
                checkCity(node.getLink(),code);
            });
            t.start();
        }
    }

    public static FileWriter writer;

    public static Integer count = 0;

    public static int split = 1000;

    static{
        try {
            writer = new FileWriter("E:/token.txt");
        }catch (Exception e) {
        }
    }

    public static void dataInit(String code,String name,String parent,String level){
        log.info("code:{} \t name:{} \t parent:{} \t level:{}",code,name,parent,level);
        try{
            String str = "";
            synchronized (count) {
                if (count % split == 0) {
                    str = String.format("insert into area(`id`,`name`,`code`,`parent_id`,`level`) value(%s,'%s','%s',%s,%s)\n", code, name, code, parent, level);
                } else if (count % split == split - 1) {
                    str = String.format(",(%s,'%s','%s',%s,%s);\n", code, name, code, parent, level);
                } else {
                    str = String.format(",(%s,'%s','%s',%s,%s)\n", code, name, code, parent, level);
                }
                count++;
                writer.write(str);
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void checkCity(String url,String code){
        NodeList list = check(url,".citytable a");
//        lineAnalysis(list,"城市");
        for (int i = 0; i < list.size(); i+=2) {
            LinkTag node=(LinkTag) list.elementAt(i);
            LinkTag textNode=(LinkTag) list.elementAt(i+1);
//            log.info("城市编码:"+node.getLinkText()+" \t 名字:"+textNode.getLinkText());
            dataInit(node.getLinkText(),textNode.getLinkText(),code,"2");
            checkCounty(node.getLink(),node.getLinkText());
        }

    }

    public static void checkCounty(String url,String code){
        NodeList list = check(url,".countytable a");
//        lineAnalysis(list);
        for (int i = 0; i < list.size(); i+=2) {
            LinkTag node=(LinkTag) list.elementAt(i);
            LinkTag textNode=(LinkTag) list.elementAt(i+1);
//            log.info("市区编码:"+node.getLinkText()+" \t 名字:"+textNode.getLinkText());
            dataInit(node.getLinkText(),textNode.getLinkText(),code,"3");
            checkTown(node.getLink(),node.getLinkText());
        }
        list = check(url,".countytable .countytr td","a");
        for (int i = 0; i < list.size(); i+=2) {
            TableColumn node=(TableColumn) list.elementAt(i);
            TableColumn textNode=(TableColumn) list.elementAt(i+1);
//            log.info("link:"+node.getStringText()+"--"+textNode.getStringText());
            dataInit(node.getStringText(),textNode.getStringText(),code,"3");
//            log.info("\t --linktext"+node.getLinkText()+textNode.getLinkText());
        }
    }

    public static void checkTown(String url,String code){
        NodeList list = check(url,".towntable a");
//        lineAnalysis(list);
        for (int i = 0; i < list.size(); i+=2) {
            LinkTag node=(LinkTag) list.elementAt(i);
            LinkTag textNode=(LinkTag) list.elementAt(i+1);
//            log.info("城镇编码:"+node.getLinkText()+" \t 名字:"+textNode.getLinkText());
            dataInit(node.getLinkText(),textNode.getLinkText(),code,"4");
            checkVillage(node.getLink(),node.getLinkText());
        }
        list = check(url,".towntable .towntr td","a");
        for (int i = 0; i < list.size(); i+=2) {
            TableColumn node=(TableColumn) list.elementAt(i);
            TableColumn textNode=(TableColumn) list.elementAt(i+1);
//            log.info("link:"+node.getStringText()+"--"+textNode.getStringText());
            dataInit(node.getStringText(),textNode.getStringText(),code,"4");
//            log.info("\t --linktext"+node.getLinkText()+textNode.getLinkText());
        }
    }

    public static void checkVillage(String url,String code){
        NodeList list = check(url,".villagetable a");
        lineAnalysis(list);
        list = check(url,".villagetable .villagetr td","a");
        for (int i = 0; i < list.size(); i+=3) {
            TableColumn node=(TableColumn) list.elementAt(i);
            TableColumn textNode=(TableColumn) list.elementAt(i+2);
            dataInit(node.getStringText(),textNode.getStringText(),code,"5");
//            log.info("村庄编码:"+node.getStringText()+" \t 名字:"+textNode.getStringText());
//            log.info("link:"+node.getStringText()+"--"+textNode.getStringText());
//            log.info("\t --linktext"+node.getLinkText()+textNode.getLinkText());
        }
    }


    public static void lineAnalysis(NodeList list){
        for (int i = 0; i < list.size(); i+=2) {
            LinkTag node=(LinkTag) list.elementAt(i);
            LinkTag textNode=(LinkTag) list.elementAt(i+1);
            log.error("------编码:"+node.getLinkText()+" \t 名字:"+textNode.getLinkText());
        }
    }

    public static void main(String[] args) {
        String url = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2018/index.html";
        checkProvince(url);
//        try {
//            writer.write(";");
//            writer.close();
//        }catch (Exception e){
//            e.printStackTrace();
//        }


//        url = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2018/11.html";
//        checkCity(url);
//        url = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2018/65/6501.html";
//        checkCounty(url);
//        url = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2018/65/01/03/650103001.html";
//        checkVillage(url);
    }
}
