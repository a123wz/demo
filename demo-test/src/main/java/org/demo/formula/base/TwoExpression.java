package org.demo.formula.base;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public abstract class TwoExpression implements Expression{

    protected BigDecimal one;

    protected BigDecimal two;

    @Override
    public int operationCount() {
        return 2;
    }
}
