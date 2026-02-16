package com.oceanview.resort.pricing;

import java.math.BigDecimal;

public class SuiteBillingStrategy implements BillingStrategy {
    @Override
    public String getRoomType() {
        return "Suite";
    }

    @Override
    public BigDecimal calculateTotal(long nights, BigDecimal baseRate) {
        return baseRate.multiply(BigDecimal.valueOf(1.25)).multiply(BigDecimal.valueOf(nights));
    }
}

