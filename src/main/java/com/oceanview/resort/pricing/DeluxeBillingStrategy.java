package com.oceanview.resort.pricing;

import java.math.BigDecimal;

public class DeluxeBillingStrategy implements BillingStrategy {
    @Override
    public String getRoomType() {
        return "Deluxe";
    }

    @Override
    public BigDecimal calculateTotal(long nights, BigDecimal baseRate) {
        return baseRate.multiply(BigDecimal.valueOf(1.15)).multiply(BigDecimal.valueOf(nights));
    }
}

