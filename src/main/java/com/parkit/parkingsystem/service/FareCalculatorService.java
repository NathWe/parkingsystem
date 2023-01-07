package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.dao.TicketDAO;



public class FareCalculatorService {
	
	private final TicketDAO ticketDAO = new TicketDAO();

    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }

        int inHour = (int) ticket.getInTime().getTime();
        int outHour = (int) ticket.getOutTime().getTime();

       int duration =(outHour - inHour) / 1000 / 60;
       
       if (duration <= 30) {
    	   ticket.setPrice(0);
        }

       else {
    	   
        switch (ticket.getParkingSpot().getParkingType()) {
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR / 60);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR / 60);
                break;
            }
            default:
                throw new IllegalArgumentException("Unkown Parking Type");
        }


	// Reduce of 5% when the user is already came
       final Ticket existingTicket = ticketDAO.getTicket(ticket.getVehicleRegNumber());
       if (existingTicket != null) {
    	   ticket.setPrice(ticket.getPrice() - ticket.getPrice() * 0.05);
       }

    }

}
}