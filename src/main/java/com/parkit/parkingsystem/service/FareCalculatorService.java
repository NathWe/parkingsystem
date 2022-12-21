package com.parkit.parkingsystem.service;

import java.util.concurrent.TimeUnit;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.dao.TicketDAO;

public class FareCalculatorService {

	private final TicketDAO ticketDAO = new TicketDAO();
	
    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        //TODO: Some tests are failing here. Need to check if this logic is correct
        
        double durationMinutes = TimeUnit.MINUTES.convert(ticket.getOutTime().getTime() - ticket.getInTime().getTime(), TimeUnit.MILLISECONDS);
		
        if (durationMinutes < Fare.FREE_PARK_PER_MINUTE) {
        	ticket.setPrice(0);
        } else {
        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(durationMinutes * Fare.CAR_RATE_PER_MINUTES);
                break;
            }
            case BIKE: {
            	ticket.setPrice(durationMinutes * Fare.BIKE_RATE_PER_MINUTES);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
		final Ticket existingTicket = ticketDAO.getTicket(ticket.getVehicleRegNumber());
        if (existingTicket != null) {
        	ticket.setPrice(ticket.getPrice() - (ticket.getPrice() * 0.05));
        }
     }
}
