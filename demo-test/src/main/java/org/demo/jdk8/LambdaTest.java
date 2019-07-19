package org.demo.jdk8;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Lambda 表达式
 */
public class LambdaTest {

    /*
    Function<T, R>：将 T 作为输入，返回 R 作为输出，他还包含了和其他函数组合的默认方法。
    Predicate<T> ：将 T 作为输入，返回一个布尔值作为输出，该接口包含多种默认方法来将 Predicate 组合成其他复杂的逻辑（与、或、非）。
    Consumer<T> ：将 T 作为输入，不返回任何内容，表示在单个参数上的操作。
     */

    public static void main(String[] args) {
        System.out.println(copy("1",str->{
            return new Integer(str);
        }));
        System.out.println(T.name());

        //2. 使用 Lambda 表达式
        Function<Integer, String> f2 = (t)->String.valueOf(t);
        //3. 使用方法引用的方式
        Function<Integer, String> f1 = String::valueOf;
        System.out.println(f1.apply(1));
    }

    public static Integer copy(String str, Function<String,Integer> apply){
        return apply.apply(str);
    }
    //接口增强
    interface T{
        static  String name(){
            return "str";
        }
        default Integer count(){
            return 1;
        }
    }
}
