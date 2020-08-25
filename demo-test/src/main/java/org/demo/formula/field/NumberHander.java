package org.demo.formula.field;

import java.math.BigDecimal;

public class NumberHander implements FieldHander{

    @Override
    public BigDecimal hander(String field) {
        if(field.matches("[0-9]+")){
            return new BigDecimal(field);
        }
        return null;
    }
}
