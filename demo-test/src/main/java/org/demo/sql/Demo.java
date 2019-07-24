package org.demo.sql;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.expression.operators.relational.NamedExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.util.SelectUtils;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.SelectDeParser;

@Slf4j
public class Demo {

    public static void main(String[] args) throws Exception{
        replaceSelect();
    }

    public static void buidTableSelect() throws Exception{
        Select select = SelectUtils.buildSelectFromTable(new Table("mytable"));
        System.out.println(select);
        select = SelectUtils.buildSelectFromTableAndExpressions(new Table("mytable"), "a+b", "test");
        System.out.println(select);
    }

    public static void replaceSelect() throws Exception{
        String sql ="SELECT NAME, ADDRESS, COL1 FROM USER WHERE SSN IN ('11111111111111', '22222222222222') and b=1;";
        Select select = (Select) CCJSqlParserUtil.parse(sql);

        //Start of value modification
        StringBuilder buffer = new StringBuilder();
        ExpressionDeParser expressionDeParser = new ExpressionDeParser() {

            @Override
            public void visit(StringValue stringValue) {
                log.info("stringValue:{},buffer:{}",stringValue,this.getBuffer());
                this.getBuffer().append("XXXX");
            }

            @Override
            public void visit(LongValue longValue) {
                this.getBuffer().append("SSSSSS");
            }

        };
        SelectDeParser deparser = new SelectDeParser(expressionDeParser,buffer );
        expressionDeParser.setSelectVisitor(deparser);
        expressionDeParser.setBuffer(buffer);
        select.getSelectBody().accept(deparser);
//End of value modification
        System.out.println(buffer.toString());
//Result is: SELECT NAME, ADDRESS, COL1 FROM USER WHERE SSN IN (XXXX, XXXX)
    }

    public static void insert() throws Exception{
        Insert insert = (Insert) CCJSqlParserUtil.parse("insert into mytable (col1) values (1)");
        System.out.println(insert.toString());

        //adding a column
        insert.getColumns().add(new Column("col2"));

        //adding a value using a visitor
        insert.getItemsList().accept(new ItemsListVisitor() {

            @Override
            public void visit(SubSelect subSelect) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void visit(NamedExpressionList namedExpressionList) {
                log.info("namedExpressionList:{}",namedExpressionList);
            }

            @Override
            public void visit(ExpressionList expressionList) {
                expressionList.getExpressions().add(new LongValue(5));
            }

            @Override
            public void visit(MultiExpressionList multiExprList) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        System.out.println(insert.toString());

        //adding another column
        insert.getColumns().add(new Column("col3"));
        //adding another value (the easy way)
        ((ExpressionList)insert.getItemsList()).getExpressions().add(new LongValue(10));

        System.out.println(insert.toString());
    }
}
