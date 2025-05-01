package com.paulopsms.domain.cancelation;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PartialRefund implements RefundRule {
    @Override
    public BigDecimal calculateRefund(BigDecimal totalPrice) {
        return new BigDecimal("0.5").multiply(totalPrice).setScale(2, RoundingMode.HALF_DOWN);
    }
}
