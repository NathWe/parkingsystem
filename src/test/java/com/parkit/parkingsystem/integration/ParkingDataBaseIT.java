package com.parkit.parkingsystem.integration;


import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;



import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.Date;




@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
    static Connection con = null;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }



    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown(){
    	dataBaseTestConfig.closeConnection(con);

    }
    
    @Test
	public void testParkingACar() throws Exception{
when(inputReaderUtil.readSelection()).thenReturn(1);
    	
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();

		
		Ticket ticket = ticketDAO.getTicket("ABCDEF");	
		assertNotNull(ticket);
		
		int nextAvaibleSlot = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);
		assertNotEquals(1, nextAvaibleSlot);
    }

    @Test
	public void testParkingABike() throws Exception{
    	when(inputReaderUtil.readSelection()).thenReturn(1);
    	
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();

		
		Ticket ticket = ticketDAO.getTicket("ABCDEF");	
		assertNotNull(ticket);
		
		int nextAvaibleSlot = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);
		assertNotEquals(1, nextAvaibleSlot);
    }
    
       
	@Test
	public void testParkingLotExit() throws Exception{
		
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

		Ticket t = new Ticket();
		t.setInTime(new Date(System.currentTimeMillis() - 60 * 60 * 1000));
		t.setOutTime(null);
		t.setPrice(0);
		t.setVehicleRegNumber("ABCDEF");
		t.setId(1);
		t.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
		ticketDAO.saveTicket(t);
		parkingSpotDAO.updateParking(t.getParkingSpot());
		
		//WHEN
		parkingService.processExitingVehicle();
		
		//THEN
		Ticket ticket = ticketDAO.getTicket("ABCDEF");
		assertNotEquals(null, ticket.getOutTime());
		assertNotEquals(0, ticket.getPrice());
	}
}
