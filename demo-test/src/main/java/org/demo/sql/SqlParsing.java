package org.demo.sql;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.AddAliasesVisitor;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.util.List;

public class SqlParsing {
    public static void main(String[] args) throws Exception{
        aliases();
    }

    /**
     * Extract table names from SQL 获取表明
      * @throws Exception
     */
    public static void tables() throws Exception{
        Statement statement = CCJSqlParserUtil.parse("SELECT * FROM MY_TABLE1 t join T1 t1 on t1.id=t.id");
        Select selectStatement = (Select) statement;
        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
        List<String> tableList = tablesNamesFinder.getTableList(selectStatement);
        tableList.forEach(e-> System.out.println(e));
    }
    /**
     * Apply aliases to all expressions 添加别名
     */
    public static void aliases() throws Exception{
        Select select = (Select) CCJSqlParserUtil.parse("select a,b,c from test");
        final AddAliasesVisitor instance = new AddAliasesVisitor();
        select.getSelectBody().accept(instance);
        System.out.println(select);
    }
}
