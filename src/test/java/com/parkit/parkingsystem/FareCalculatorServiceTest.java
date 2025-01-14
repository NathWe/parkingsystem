package com.parkit.parkingsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;

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
	public void calculateFareCar() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - 60 * 60 * 1000);
	Date outTime = new Date();
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket);
	assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
	}

	@Test
	public void calculateFareBike() {
	final Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
	final Date outTime = new Date();
	final ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket);
	assertEquals(Fare.BIKE_RATE_PER_HOUR, ticket.getPrice());
	}

	@Test
	public void calculateFareUnkownType() {
	final Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
	final Date outTime = new Date();
	final ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
	}

	@Test
	public void calculateFareCarWithFutureInTime() {
	final Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
	final Date outTime = new Date();
	final ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
	}

	@Test
	public void calculateFareBikeWithFutureInTime() {
	final Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
	final Date outTime = new Date();
	final ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
	}

	@Test
	public void calculateFareBikeWithLessThanOneHourParkingTime() {
	final Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));// 45 minutes parking time should give 3/4th parking
	// fare
	final Date outTime = new Date();
	final ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket);
	assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareCarWithLessThanOneHourParkingTime() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));// 45 minutes parking time should give 3/4th parking
	// fare
	Date outTime = new Date();
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket);
	assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareCarWithMoreThanADayParkingTime() {
	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));// 24 hours parking time should give 24 *
	// parking fare per hour
	Date outTime = new Date();
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket);
	assertEquals((24 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareBikeWithMoreThanADayParkingTime() {
	final Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));// 24 hours parking time should give 24 *
	// parking fare per hour
	final Date outTime = new Date();
	final ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket);
	assertEquals((24 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareBikeWithLessThanThirtyMinutesParkingTime() {
	final Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (20 * 60 * 1000));// 30 minutes parking time should give free parking

	final Date outTime = new Date();
	final ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket);
	assertEquals((0 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareCarWithLessThanThirtyMinutesParkingTime() {
	final Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (20 * 60 * 1000));// 30 minutes parking time should give free parking

	final Date outTime = new Date();
	final ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	fareCalculatorService.calculateFare(ticket);
	assertEquals((0 * Fare.CAR_RATE_PER_HOUR), ticket.getPrice());
	}

	@Test
	public void calculateFareCarWithDiscount() {

	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
	Date outTime = new Date();
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
	
	final TicketDAO ticketDAO = new TicketDAO();
	ticketDAO.dataBaseConfig = new DataBaseTestConfig();
	
	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	ticket.setVehicleRegNumber("ABCDEF");
	ticket.setAlreadyCame(true);
	

	ticketDAO.saveTicket(ticket);
	ticketDAO.updateTicket(ticket);

	fareCalculatorService.calculateFare(ticket);
	assertEquals(ticket.getPrice(), (Fare.CAR_RATE_PER_HOUR * Fare.DISCOUNT_FOR_REGULAR_USER));
	}

	@Test
	public void calculateFareBikeWithDiscount() {

	Date inTime = new Date();
	inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
	Date outTime = new Date();
	ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);
	
	final TicketDAO ticketDAO = new TicketDAO();
	ticketDAO.dataBaseConfig = new DataBaseTestConfig();

	ticket.setInTime(inTime);
	ticket.setOutTime(outTime);
	ticket.setParkingSpot(parkingSpot);
	ticket.setVehicleRegNumber("ABCDEF");
	ticket.setAlreadyCame(true);

	
	ticketDAO.saveTicket(ticket);
	ticketDAO.updateTicket(ticket);

	fareCalculatorService.calculateFare(ticket);
	assertEquals(ticket.getPrice(), (Fare.BIKE_RATE_PER_HOUR * Fare.DISCOUNT_FOR_REGULAR_USER));
	}
}