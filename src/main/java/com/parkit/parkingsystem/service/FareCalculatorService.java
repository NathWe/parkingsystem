package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;


public class FareCalculatorService {
	

    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }
        
        // Convert hours in ms
        double inHour = (int) ticket.getInTime().getTime();
        double outHour = (int) ticket.getOutTime().getTime();

        // Calcul duration and get it in hours
       double duration =(outHour - inHour) / (1000 * 60 * 60);
       
       //Setting duration at 0 to have 30 minutes free
       if (duration <= 0.5) {
    	  duration = 0;
        }
    	   
        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default:
                throw new IllegalArgumentException("Unkown Parking Type");
        }
 
	// Reduce of 5% when the user is already came    
      
       if (ticket.isAlreadyCame()) {
    	   
    	   double discountTicket = ticket.getPrice() * Fare.DISCOUNT_FOR_REGULAR_USER;
    	   ticket.setPrice(discountTicket);
       }
    }
}
