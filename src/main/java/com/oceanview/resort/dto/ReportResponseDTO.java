package com.oceanview.resort.dto;

import java.math.BigDecimal;

public class ReportResponseDTO {
    private int totalReservations;
    private int activeReservations;
    private int checkoutsNextSevenDays;
    private BigDecimal estimatedRevenue;
    private String mostPopularRoomType;

    public int getTotalReservations() {
        return totalReservations;
    }

    public void setTotalReservations(int totalReservations) {
        this.totalReservations = totalReservations;
    }

    public int getActiveReservations() {
        return activeReservations;
    }

    public void setActiveReservations(int activeReservations) {
        this.activeReservations = activeReservations;
    }

    public int getCheckoutsNextSevenDays() {
        return checkoutsNextSevenDays;
    }

    public void setCheckoutsNextSevenDays(int checkoutsNextSevenDays) {
        this.checkoutsNextSevenDays = checkoutsNextSevenDays;
    }

    public BigDecimal getEstimatedRevenue() {
        return estimatedRevenue;
    }

    public void setEstimatedRevenue(BigDecimal estimatedRevenue) {
        this.estimatedRevenue = estimatedRevenue;
    }

    public String getMostPopularRoomType() {
        return mostPopularRoomType;
    }

    public void setMostPopularRoomType(String mostPopularRoomType) {
        this.mostPopularRoomType = mostPopularRoomType;
    }
}
