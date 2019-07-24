package org.demo.sql.demo;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.values.ValuesStatement;

@Slf4j
public class SelectVisitorImpl implements SelectVisitor {
    // 主要工作就是实现各种底层visitor，然后在解析的时候添加条件

    @Override
    public void visit(ValuesStatement aThis) {

    }

    // 正常的select，也就是包含全部属性的select
    @Override
    public void visit(PlainSelect plainSelect) {

        // 访问 select
        if (plainSelect.getSelectItems() != null) {
            for (SelectItem item : plainSelect.getSelectItems()) {
                item.accept(new SelectItemVisitorImpl());
            }
        }

        // 访问from
        FromItem fromItem = plainSelect.getFromItem();
        FromItemVisitorImpl fromItemVisitorImpl = new FromItemVisitorImpl();
        fromItem.accept(fromItemVisitorImpl);

        // 访问where
        if (plainSelect.getWhere() != null) {
            plainSelect.getWhere().accept(new ExpressionVisitorImpl());
        }

        //过滤增强的条件
        log.info("fromItemVisitorImpl.getEnhancedCondition:{}",fromItemVisitorImpl.getEnhancedCondition());
//        kzSql(fromItemVisitorImpl,plainSelect);
        if (fromItemVisitorImpl.getEnhancedCondition() != null) {
            if (plainSelect.getWhere() != null) {
                Expression expr = new Parenthesis(plainSelect.getWhere());
                Expression enhancedCondition =  new Parenthesis(fromItemVisitorImpl.getEnhancedCondition());
                AndExpression and = new AndExpression(enhancedCondition, expr);
                plainSelect.setWhere(and);
            } else {
                plainSelect.setWhere(fromItemVisitorImpl.getEnhancedCondition());
            }
        }

        // 访问join
        if (plainSelect.getJoins() != null) {
            for (Join join : plainSelect.getJoins()) {
                FromItemVisitorImpl joinFromItem = new FromItemVisitorImpl();
                join.getRightItem().accept(joinFromItem);
                if (joinFromItem.getEnhancedCondition() != null) {
                    Expression enhancedCondition =  new Parenthesis(joinFromItem.getEnhancedCondition());
                    AndExpression and = new AndExpression(enhancedCondition, join.getOnExpression());
                    join.setOnExpression(and);
                }
            }
        }

        // 访问 order by
        if (plainSelect.getOrderByElements() != null) {
            for (OrderByElement orderByElement : plainSelect
                    .getOrderByElements()) {
                orderByElement.getExpression().accept(new ExpressionVisitorImpl());
            }
        }

        // 访问group by having
        if (plainSelect.getHaving() != null) {
            plainSelect.getHaving().accept(new ExpressionVisitorImpl());
        }

    }

    /*private void kzSql(FromItemVisitorImpl joinFromItem,PlainSelect plainSelect){
        if(joinFromItem.getEnhancedCondition()!=null) {
            if (plainSelect.getWhere() != null) {
                Expression expr = new Parenthesis(plainSelect.getWhere());
                Expression enhancedCondition = new Parenthesis(joinFromItem.getEnhancedCondition());
                AndExpression and = new AndExpression(enhancedCondition, expr);
                plainSelect.setWhere(and);
            } else {
                plainSelect.setWhere(joinFromItem.getEnhancedCondition());
            }
        }
    }*/

    // set操作列表
    @Override
    public void visit(SetOperationList setOpList) {
        for (SelectBody plainSelect : setOpList.getSelects()) {
            plainSelect.accept(new SelectVisitorImpl());
        }
    }

    // with项
    @Override
    public void visit(WithItem withItem) {
        withItem.getSelectBody().accept(new SelectVisitorImpl());
    }

}