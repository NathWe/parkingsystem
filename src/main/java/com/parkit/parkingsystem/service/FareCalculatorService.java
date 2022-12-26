package com.parkit.parkingsystem.service;

import java.util.concurrent.TimeUnit;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.dao.TicketDAO;
/**
 * 
 * @author natha
 *
 */
public class FareCalculatorService {

	private final TicketDAO ticketDAO = new TicketDAO();
	
    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }
        // Modifying the tests to make them work. The logic was not correct, so I modified it
        // Parking time calculation
        double durationMinutes = TimeUnit.MINUTES.convert(ticket.getOutTime().getTime() - ticket.getInTime().getTime(), TimeUnit.MILLISECONDS);
        
		// Apply a tarif equal to zero for a duration of less than 30 minutes
        if (durationMinutes < Fare.FREE_PARK_PER_MINUTE) {
        	ticket.setPrice(0);
        } else {
        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(durationMinutes * Fare.CAR_RATE_PER_MINUTES); // Time calculation for a car in minutes
                break;
            }
            case BIKE: {
            	ticket.setPrice(durationMinutes * Fare.BIKE_RATE_PER_MINUTES); // Times calculation for a bike in minutes
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
        // 5% reduction for vehicles already identified

		final Ticket existingTicket = ticketDAO.getTicket(ticket.getVehicleRegNumber());
        if (existingTicket != null) {
        	ticket.setPrice(ticket.getPrice() - (ticket.getPrice() * 0.05));
        }
     }
}
