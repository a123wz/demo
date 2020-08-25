package org.demo.formula;

import lombok.Data;
import lombok.experimental.Accessors;
import org.demo.formula.base.*;
import org.demo.formula.field.FieldHander;
import org.demo.formula.field.NumberHander;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;

@Data
@Accessors(chain = true)
public class FormulaParsing {

    private String content;

    private Map<TypeEnums,Expression> expressionMap = new HashMap<>();

    private Stack<Object> nodes = new Stack<>();

    private List<FieldHander> handers = new ArrayList<>();

    public static void main(String[] args) {
        FormulaParsing parsing = new FormulaParsing().setContent("5+(2+3)*8+b");
        parsing.analysis();
        parsing.initExpression();
        parsing.initHander();
        parsing.calculate();
    }

    public void initHander(){
        handers.add(new NumberHander());
        handers.add(e->{
            if(e.equals("b")) return new BigDecimal(1);
            return null;
        });
    }

    public void initExpression(){
        expressionMap.put(TypeEnums.ADD,new AddExpression());
        expressionMap.put(TypeEnums.MULTIPLY,new MulExpression());
    }

    public void calculate(){
        int size = this.nodes.size();
        Stack<BigDecimal> result = new Stack<>();
        for(int i =0 ;i < size; i++ ){
            Object o = nodes.get(i);
            if(o instanceof TypeEnums){
                Expression e = this.expressionMap.get(o);
                if(e instanceof TwoExpression){
                    ((TwoExpression) e).setOne(result.pop());
                    ((TwoExpression) e).setTwo(result.pop());
                    result.push(e.operation());
                }
            }else{
                result.push(convert(o));
            }
        }
        System.out.println(result);
    }

    public BigDecimal convert(Object o){
        BigDecimal result = null;
        for (FieldHander hander:this.handers){
            result = hander.hander((String)o);
            if(result!=null){
                return result;
            }
        }
        if(result==null){
            throw new RuntimeException("字段:"+o+"解析异常");
        }
        return result;
    }

    public void analysis(){
        Stack<TypeEnums> operators = new Stack<>(); //运算符
        char[] arr = content.toCharArray();
        int lenth = arr.length;
        int pre = 0;
        int bracket = 0; // 左括号的数量
        boolean digital;
        for(int i = 0; i<lenth;){
            pre = i;
            digital = Boolean.FALSE;
            //截取数字
            while (i < lenth && TypeEnums.getOperator(arr[i])==null) {
                i++;
                digital = Boolean.TRUE;
            }

            if (digital) {
                nodes.push(content.substring(pre, i));
            } else {
                TypeEnums type = TypeEnums.getOperator(arr[i++]);
                if (type == TypeEnums.LEFT_BRACKET) {
                    bracket++;
                }
                if (bracket > 0) {
                    if (type == TypeEnums.RIGHT_BRACKET) {
                        while (!operators.empty()) {
                            TypeEnums top = operators.pop();
                            if (top == TypeEnums.LEFT_BRACKET) {
                                break;
                            }
                            nodes.push(top);
                        }
                        bracket--;
                    } else {
                        //如果栈顶为 ( ，则直接添加，不顾其优先级
                        //如果之前有 ( ，但是 ( 不在栈顶，则需判断其优先级，如果优先级比栈顶的低，则依次出栈
                        while (!operators.empty() && operators.peek() != TypeEnums.LEFT_BRACKET && type.compare(operators.peek())<= 0) {
                            nodes.push(operators.pop());
                        }
                        operators.push(type);
                    }
                } else {
                    while (!operators.empty() && type.compare(operators.peek())<= 0) {
                        nodes.push(operators.pop());
                    }
                    operators.push(type);
                }
            }
        }
        //遍历结束，将运算符栈全部压入output
        while (!operators.empty()) {
            nodes.push(operators.pop());
        }
    }
}
