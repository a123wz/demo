package org.demo.sql;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.expression.operators.relational.NamedExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.SelectDeParser;
import net.sf.jsqlparser.util.deparser.StatementDeParser;
import org.demo.sql.demo.FromItemVisitorImpl;

import java.util.List;

@Slf4j
public class ReplaceColumnValues {

    static class ReplaceColumnAndLongValues extends ExpressionDeParser {

        @Override
        public void visit(StringValue stringValue) {
            this.getBuffer().append("?");
        }

        @Override
        public void visit(LongValue longValue) {
            this.getBuffer().append("?");
        }
    }

    public static String cleanStatement(String sql) throws JSQLParserException {
        StringBuilder buffer = new StringBuilder();
        ExpressionDeParser expr = new ReplaceColumnAndLongValues();

        SelectDeParser selectDeparser = new SelectDeParser(expr, buffer);
        expr.setSelectVisitor(selectDeparser);
        expr.setBuffer(buffer);
        StatementDeParser stmtDeparser = new StatementDeParser(expr, selectDeparser, buffer);

        Statement stmt = CCJSqlParserUtil.parse(sql);

        stmt.accept(stmtDeparser);
        return stmtDeparser.getBuffer().toString();
    }

    public static void main(String[] args) throws JSQLParserException {
//        System.out.println(cleanStatement("SELECT 'abc', 5 FROM mytable WHERE col='test'"));
//        System.out.println(cleanStatement("UPDATE table1 A SET A.columna = 'XXX' WHERE A.cod_table = 'YYY'"));
//        System.out.println(cleanStatement("INSERT INTO example (num, name, address, tel) VALUES (1, 'name', 'test ', '1234-1234')"));
//        System.out.println(cleanStatement("DELETE FROM table1 where col=5 and col2=4"));
//        System.out.println(updateStata("UPDATE A SET A1 = B1, A2 = B2, A3 = B3 FROM A, B WHERE A.ID = B.ID"));
//        System.out.println(insertStata("INSERT INTO example (num, name, address, tel) VALUES (1, 'name', 'test ', '1234-1234')"));
        System.out.println(insertStata("INSERT INTO table1 (c1, c2, c3) (SELECT v1, v2, v3 FROM table2)"));
    }
    public static String insertStata(String sql) throws JSQLParserException{
        Insert stmt = (Insert) CCJSqlParserUtil.parse(sql);
//        List<Table> tables = stmt.getTable();
        Column col = new Column();
        col.setColumnName("b");
        col.setTable(stmt.getTable());
        List<Column> columns = stmt.getColumns();
        columns.add(col);
        stmt.setColumns(columns);

        StringValue value = new StringValue("bbb");
        List<Expression> exs = stmt.getSetExpressionList();
        System.out.println(stmt.getItemsList());
        stmt.getItemsList().accept(new ItemsListVisitor(){
            @Override
            public void visit(SubSelect subSelect) {
                log.info("subSelect:{}",subSelect);
            }

            @Override
            public void visit(ExpressionList expressionList) {
                StringValue value = new StringValue("bbb");
                List<Expression> exs =  expressionList.getExpressions();
                exs.add(value);
                expressionList.setExpressions(exs);
                log.info("expressionList:{}",expressionList);
            }

            @Override
            public void visit(NamedExpressionList namedExpressionList) {
                log.info("namedExpressionList:{}",namedExpressionList);
            }

            @Override
            public void visit(MultiExpressionList multiExprList) {
                log.info("multiExprList:{}",multiExprList);
            }
        });
        exs.add(value);
        stmt.setSetExpressionList(exs);
        return stmt.toString();
    }

    public static String updateStata(String sql) throws JSQLParserException{
        String result= "";
        Update stmt = (Update) CCJSqlParserUtil.parse(sql);
        stmt.getColumns().forEach(e->{
            System.out.println(e.getName(false)+"--"+e.getColumnName());
        });
        List<Table> tables = stmt.getTables();
        Column col = new Column();
        col.setColumnName("b");
        col.setTable(tables.get(0));
        List<Column> columns = stmt.getColumns();
        columns.add(col);
        stmt.setColumns(columns);
        StringValue value = new StringValue("bbb");
        List<Expression> exs = stmt.getExpressions();
        exs.add(value);
        stmt.setExpressions(exs);
//        stmt.accept();
//        stmt.getFromItem().accept(new FromItemVisitorImpl());
        return stmt.toString();
    }

}
