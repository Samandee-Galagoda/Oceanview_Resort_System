package com.oceanview.resort.pricing;

import java.math.BigDecimal;

public class FamilyBillingStrategy implements BillingStrategy {
    @Override
    public String getRoomType() {
        return "Family";
    }

    @Override
    public BigDecimal calculateTotal(long nights, BigDecimal baseRate) {
        return baseRate.multiply(BigDecimal.valueOf(1.05)).multiply(BigDecimal.valueOf(nights));
    }
}

