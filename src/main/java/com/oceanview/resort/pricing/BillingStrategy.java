package com.oceanview.resort.pricing;

import java.math.BigDecimal;

public interface BillingStrategy {
    String getRoomType();

    BigDecimal calculateTotal(long nights, BigDecimal baseRate);
}

