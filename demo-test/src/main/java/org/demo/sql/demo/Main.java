package org.demo.sql.demo;

import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.select.Select;

import java.io.StringReader;

/**
 * 给指定表添加查询条件，用于数据权限校验
 */
public class Main {

    public static void main(String[] args) throws Exception{
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        String sql="select * from fg_apply f left join b b on f.id = b.id where 1=1";
//        sql = "select * from b b left join fg_apply f on f.id = b.id where 1=1";
        sql = "select * from b where id in (select id from fg_apply t where t.id =1)";
        Select select = (Select) parserManager.parse(new StringReader(sql));
        select.getSelectBody().accept(new SelectVisitorImpl());
        System.out.println(select.toString());
    }
}
