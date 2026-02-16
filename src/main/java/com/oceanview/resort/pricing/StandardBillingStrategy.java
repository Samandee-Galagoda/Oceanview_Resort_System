package com.oceanview.resort.pricing;

import java.math.BigDecimal;

public class StandardBillingStrategy implements BillingStrategy {
    @Override
    public String getRoomType() {
        return "Standard";
    }

    @Override
    public BigDecimal calculateTotal(long nights, BigDecimal baseRate) {
        return baseRate.multiply(BigDecimal.valueOf(nights));
    }
}

