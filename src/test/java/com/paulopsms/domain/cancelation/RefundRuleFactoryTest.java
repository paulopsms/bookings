package com.paulopsms.domain.cancelation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class RefundRuleFactoryTest {

    @Test
    public void givenStartDateAfterSevenDaysFromCurrentDate_whenGetRefundRule_thenReturnFullRefund() {
        LocalDate currentDate = LocalDate.of(2023, 10, 1);
        LocalDate startDate = LocalDate.of(2023, 10, 10);

        RefundRule refundRule = RefundRuleFactory.getRefundRule(currentDate, startDate);

        assertTrue(refundRule instanceof FullRefund);
    }

    @Test
    public void givenStartDateBetweenOneAndSevenDaysFromCurrentDate_whenGetRefundRule_thenReturnPartialRefund() {
        LocalDate currentDate = LocalDate.of(2023, 10, 1);
        LocalDate startDate = LocalDate.of(2023, 10, 5);

        RefundRule refundRule = RefundRuleFactory.getRefundRule(currentDate, startDate);

        assertTrue(refundRule instanceof PartialRefund);
    }

    @Test
    public void givenStartDateOneDayFromCurrentDate_whenGetRefundRule_thenReturnNoRefund() {
        LocalDate currentDate = LocalDate.of(2023, 10, 1);
        LocalDate startDate = LocalDate.of(2023, 9, 30);

        RefundRule refundRule = RefundRuleFactory.getRefundRule(currentDate, startDate);

        assertTrue(refundRule instanceof NoRefund);
    }

    @Test
    public void givenStartDateLessThanOneDayFromCurrentDate_whenGetRefundRule_thenReturnNoRefund() {
        LocalDate currentDate = LocalDate.of(2023, 10, 1);
        LocalDate startDate = LocalDate.of(2023, 10, 1);

        RefundRule refundRule = RefundRuleFactory.getRefundRule(currentDate, startDate);

        assertTrue(refundRule instanceof NoRefund);
    }
}
