package org.demo.formula.base;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class MulExpression extends TwoExpression {

    @Override
    public BigDecimal operation() {
//        TypeEnums.ADD.compareTo(s)
        return one.multiply(two);
    }
}