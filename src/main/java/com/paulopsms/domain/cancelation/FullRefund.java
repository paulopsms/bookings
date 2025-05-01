package com.paulopsms.domain.cancelation;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FullRefund implements RefundRule {

    @Override
    public BigDecimal calculateRefund(BigDecimal totalPrice) {
        return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_DOWN);
    }
}
