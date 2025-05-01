package com.paulopsms.domain.cancelation;

import java.math.BigDecimal;

public interface RefundRule {
    BigDecimal calculateRefund(BigDecimal totalPrice);
}
