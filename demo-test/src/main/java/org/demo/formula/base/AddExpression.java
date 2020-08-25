package org.demo.formula.base;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class AddExpression extends TwoExpression{

    @Override
    public BigDecimal operation() {
//        TypeEnums.ADD.compareTo(s)
        return one.add(two);
    }
}
