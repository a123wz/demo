package org.demo.formula.base;

import java.math.BigDecimal;

public interface Expression {

    int operationCount();

    BigDecimal operation();
}
