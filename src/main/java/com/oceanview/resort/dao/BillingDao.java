package com.oceanview.resort.dao;

import com.oceanview.resort.model.Billing;

/**
 * Billing DAO for working with invoices.
 */
public interface BillingDao {

    void create(Billing billing);

    Billing findByInvoiceNumber(String invoiceNumber);

    Billing findByReservationId(long reservationId);
}

