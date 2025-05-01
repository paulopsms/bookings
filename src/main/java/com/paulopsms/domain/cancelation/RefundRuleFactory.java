package com.paulopsms.domain.cancelation;

import java.time.LocalDate;

public class RefundRuleFactory {
    public static RefundRule getRefundRule(LocalDate currentDate, LocalDate startDate) {
        if (startDate.isAfter(currentDate.plusDays((7)))) {
            return new FullRefund();
        } else if (currentDate.isBefore(startDate) && startDate.isBefore(currentDate.plusDays((7)))) {
            return new PartialRefund();
        } else return new NoRefund();
    }
}
