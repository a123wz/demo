package org.demo.sql.demo;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 产品id拦截
 */
@Slf4j
public class FromItemVisitorImpl implements FromItemVisitor {

    /**
     * /声明增强条件
     */
    private Expression enhancedCondition;

    /**
     定义需要数据隔离的表
      **/
    private static HashSet<String> interceptTableSet = new HashSet<>();

    static {
        interceptTableSet.add("fg_apply");
        interceptTableSet.add("ct_apply_order");

    }

    @Override
    public void visit(ParenthesisFromItem aThis) {
        System.out.println(aThis);
    }


    @Override
    public void visit(Table tableName) {
        //判断该表是否是需要操作的表
        if(interceptTableSet.contains(tableName.getFullyQualifiedName().toLowerCase())){
            // 声明表达式数组
            Expression[] expressions;
            // 其他情况,也就是最常用的情况，比如where 1 = 1
            Column column = new Column(new Table(tableName.getAlias() != null ? tableName.getAlias().getName() : tableName.getFullyQualifiedName()), "product_id");
            expressions = new Expression[]{column, new LongValue(1)};
//            if (getShiroUser() == null) {
//                return;
//            } else {
//                expressions = new Expression[]{column, new LongValue(getShiroUser().getCompanyId())};
//            }
            log.info("数据拦截");
            // 根据运算符对原始数据进行拼接

//            Expression operator = this.getOperator("=", expressions);
//            AuthUserInfo user = UserAuthUtils.getLoginUser();
//            if(user==null||user.getProductAuthList()==null||user.getProductAuthList().isEmpty()){
//                return ;
//            }
            InExpression operator = new InExpression();
            operator.setNot(false);
            operator.setLeftExpression(column);
            List<Expression> list = new ArrayList<>();
//            user.getProductAuthList().forEach(e->{
//                list.add(new LongValue(e.getProductId()));
//            });
            list.add(new LongValue(1));
            list.add(new LongValue(2));
            operator.setRightItemsList(new ExpressionList(list));
            if (this.enhancedCondition != null) {
                enhancedCondition = new AndExpression(enhancedCondition, operator);
            } else {
                enhancedCondition = operator;
            }
        }
    }

    // FROM 子查询
    @Override
    public void visit(SubSelect subSelect) {
        // 如果是子查询的话返回到select接口实现类
        subSelect.getSelectBody().accept(new SelectVisitorImpl());
    }

    // FROM subjoin
    @Override
    public void visit(SubJoin subjoin) {
        subjoin.getLeft().accept(new FromItemVisitorImpl());
        if(subjoin.getJoinList() != null) {
            subjoin.getJoinList().forEach(e -> e.getRightItem().accept(new FromItemVisitorImpl()));
        }
//        subjoin.getJoin().getRightItem().accept(new FromItemVisitorImpl());
    }

    // FROM 横向子查询
    @Override
    public void visit(LateralSubSelect lateralSubSelect) {
        lateralSubSelect.getSubSelect().getSelectBody()
                .accept(new SelectVisitorImpl());
    }

    // FROM value列表
    @Override
    public void visit(ValuesList valuesList) {
        System.out.println(valuesList);
    }

    // FROM tableFunction
    @Override
    public void visit(TableFunction tableFunction) {
        System.out.println(tableFunction);
    }

    // 将字符串类型的运算符转换成数据库运算语句
    private Expression getOperator(String op, Expression[] exp) {
        if ("=".equals(op)) {
            EqualsTo eq = new EqualsTo();
            eq.setLeftExpression(exp[0]);
            eq.setRightExpression(exp[1]);
            return eq;
        } else if (">".equals(op)) {
            GreaterThan gt = new GreaterThan();
            gt.setLeftExpression(exp[0]);
            gt.setRightExpression(exp[1]);
            return gt;
        } else if (">=".equals(op)) {
            GreaterThanEquals geq = new GreaterThanEquals();
            geq.setLeftExpression(exp[0]);
            geq.setRightExpression(exp[1]);
            return geq;
        } else if ("<".equals(op)) {
            MinorThan mt = new MinorThan();
            mt.setLeftExpression(exp[0]);
            mt.setRightExpression(exp[1]);
            return mt;
        } else if ("<=".equals(op)) {
            MinorThanEquals leq = new MinorThanEquals();
            leq.setLeftExpression(exp[0]);
            leq.setRightExpression(exp[1]);
            return leq;
        } else if ("<>".equals(op)) {
            NotEqualsTo neq = new NotEqualsTo();
            neq.setLeftExpression(exp[0]);
            neq.setRightExpression(exp[1]);
            return neq;
        } else if ("is null".equalsIgnoreCase(op)) {
            IsNullExpression isNull = new IsNullExpression();
            isNull.setNot(false);
            isNull.setLeftExpression(exp[0]);
            return isNull;
        } else if ("is not null".equalsIgnoreCase(op)) {
            IsNullExpression isNull = new IsNullExpression();
            isNull.setNot(true);
            isNull.setLeftExpression(exp[0]);
            return isNull;
        } else if ("like".equalsIgnoreCase(op)) {
            LikeExpression like = new LikeExpression();
            like.setNot();
            like.setLeftExpression(exp[0]);
            like.setRightExpression(exp[1]);
            return like;
        } else if ("not like".equalsIgnoreCase(op)) {
            LikeExpression nlike = new LikeExpression();
            nlike.setNot();
            nlike.setLeftExpression(exp[0]);
            nlike.setRightExpression(exp[1]);
            return nlike;
        } else if ("between".equalsIgnoreCase(op)) {
            Between bt = new Between();
            bt.setNot(false);
            bt.setLeftExpression(exp[0]);
            bt.setBetweenExpressionStart(exp[1]);
            bt.setBetweenExpressionEnd(exp[2]);
            return bt;
        } else if ("not between".equalsIgnoreCase(op)) {
            Between bt = new Between();
            bt.setNot(true);
            bt.setLeftExpression(exp[0]);
            bt.setBetweenExpressionStart(exp[1]);
            bt.setBetweenExpressionEnd(exp[2]);
            return bt;
        } else if("in".equalsIgnoreCase(op)){

            // 如果没有该运算符对应的语句
            return null;
        }else{
            return null;
        }

    }

    public Expression getEnhancedCondition() {
        return enhancedCondition;
    }

    // 判断传入的table是否是要进行操作的table
    public boolean isActionTable(String tableName) {
        // 默认为操作
        boolean flag = true;
        // 无需操作的表的表名
        List<String> tableNames = new ArrayList<String>();
        // 由于sql可能格式不规范可能表名会存在小写，故全部转换成大写,最上面的方法一样
        if (interceptTableSet.contains(tableName.toLowerCase())) {
            // 如果表名在过滤条件中则将flag改为flase
            flag = false;
        }
        return flag;
    }

}
