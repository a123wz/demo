package org.demo.formula.base;

public enum TypeEnums{
    ADD('+', 1), SUBTRACT('-', 1),
    MULTIPLY('*', 2), DIVIDE('/', 2),
    LEFT_BRACKET('(', 3), RIGHT_BRACKET(')', 3); //括号优先级最高
    char value;
    int priority;
    TypeEnums(char value, int priority) {
        this.value = value;
        this.priority = priority;
    }

    public static TypeEnums getOperator(char c) {
        for (TypeEnums o : TypeEnums.values()) {
            if (o.value == c) {
                return o;
            }
        }
        return null;
    }

    /**
     * 比较两个符号的优先级
     * @return 高则返回正数，等于返回0，小于返回负数
     */
    public int compare(TypeEnums type) {
        return this.priority - type.priority;
    }
}
