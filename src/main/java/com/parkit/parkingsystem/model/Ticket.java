package com.parkit.parkingsystem.model;

import java.util.Date;

import org.junit.Ignore;

public class Ticket {
    private int id;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber;
    private double price;
    private Date inTime;
    private Date outTime;
    
    private boolean isAlreadyCame = false;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Ignore
    public Date getInTime() {
        return inTime != null ? (Date) inTime.clone() : null;
    }

    @Ignore
    public void setInTime(Date inTime) {
        this.inTime = inTime != null ? (Date) inTime.clone() : null;
    }

@Ignore
    public Date getOutTime() {
        return outTime != null ? (Date) outTime.clone() : null;
    }

@Ignore
    public void setOutTime(Date outTime) {
        this.outTime = outTime != null ? (Date) outTime.clone() : null;
    }

    public boolean isAlreadyCame() {
        return isAlreadyCame;
    }

    public void setAlreadyCame(boolean alreadyCame) {
        isAlreadyCame = alreadyCame;
    }
}