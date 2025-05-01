package com.paulopsms.domain.cancelation;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NoRefund implements RefundRule {

    @Override
    public BigDecimal calculateRefund(BigDecimal totalPrice) {
        return totalPrice.setScale(2, RoundingMode.HALF_DOWN);
    }
}
