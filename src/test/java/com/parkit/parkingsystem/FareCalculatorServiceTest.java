package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;



import java.util.Date;



public class FareCalculatorServiceTest {


	private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
    }

    @Test
    public void calculateFareCar(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((Fare.CAR_RATE_PER_MINUTES *60), ticket.getPrice());
}

    @Test
    public void calculateFareBike(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((Fare.BIKE_RATE_PER_MINUTES *60), ticket.getPrice());
    }

    @Test
    public void calculateFareUnkownType(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithFutureInTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() + (  60 * 60 * 1000) ); // 60 minutes parking time
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes parking time
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals((45 * Fare.BIKE_RATE_PER_MINUTES), ticket.getPrice() );
    }

    @Test
    public void calculateFareCarWithLessThanOneHourParkingTime(){
        Date inTime = new Date(System.currentTimeMillis());
        inTime.setTime( System.currentTimeMillis() - (  45 * 60 * 1000) );//45 minutes parking time
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (45 * Fare.CAR_RATE_PER_MINUTES) , ticket.getPrice());
    }
  
        
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        
    @Test
    public void calculateFareCarWithMoreThanADayParkingTime(){
        Date inTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  1440 * 60 * 1000) ); //24 hours parking time should give 1440 minutes * parking fare per hour
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket);
        assertEquals( (1440 * Fare.CAR_RATE_PER_MINUTES) , ticket.getPrice());
    }
    @Test
    public void calculateFareBikeWithLessThanThirtyMinutesParkingTime(){
    	        Date inTime = new Date();
    	        inTime.setTime( System.currentTimeMillis() - (  20 * 60 * 1000) ); // 20 minutes parking time for bike
    	        Date outTime = new Date();
    	        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

    	        ticket.setInTime(inTime);
    	        ticket.setOutTime(outTime);
    	        ticket.setParkingSpot(parkingSpot);
    	        fareCalculatorService.calculateFare(ticket);
    	        assertEquals((0 * Fare.BIKE_RATE_PER_MINUTES), ticket.getPrice());
    	    }
    
    @Test
    public void calculateFareCarWithLessThanThirtyMinutesParkingTime(){
    	        Date inTime = new Date();
    	        inTime.setTime( System.currentTimeMillis() - (  20 * 60 * 1000) );// 20 minutes parking time for car
    	        Date outTime = new Date();
    	        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

    	        ticket.setInTime(inTime);
    	        ticket.setOutTime(outTime);
    	        ticket.setParkingSpot(parkingSpot);
    	        fareCalculatorService.calculateFare(ticket);
    	        assertEquals((0), ticket.getPrice());
    	    }


	@Test
	public void calculateFareCarWithDiscount(){
				Date inTime = new Date();
				inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) ); // Reduction for a car parked one hour
				Date outTime = new Date();
				ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

				ticket.setInTime(inTime);
				ticket.setOutTime(outTime);
				ticket.setParkingSpot(parkingSpot);
				ticket.setVehicleRegNumber("ABCDEF");
				fareCalculatorService.calculateFare(ticket);
				assertEquals((Fare.CAR_RATE_PER_MINUTES - (Fare.CAR_RATE_PER_MINUTES * 0.05))*60, ticket.getPrice());
	}
	@Test
	public void calculateFareBikeWithDiscount(){
			Date inTime = new Date();
			inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );// Reduction for a car parked one hour
			Date outTime = new Date();
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

			ticket.setInTime(inTime);
			ticket.setOutTime(outTime);
			ticket.setParkingSpot(parkingSpot);
			ticket.setVehicleRegNumber("ABCDEF");
			fareCalculatorService.calculateFare(ticket);
			assertEquals((Fare.BIKE_RATE_PER_MINUTES - (Fare.BIKE_RATE_PER_MINUTES * 0.05))*60, ticket.getPrice());
}
}
		