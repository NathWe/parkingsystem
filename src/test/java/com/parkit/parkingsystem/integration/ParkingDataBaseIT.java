package com.parkit.parkingsystem.integration;

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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.TimeUnit;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

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

	private Connection con;

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown(){

    }
    
    @Test
	public void testParkingACar1() {
		final ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();

		final Ticket ticket = ticketDAO.getTicket("ABCDEF");
		assertNotNull(ticket.getId());

		final ParkingSpot parkingSpot = ticket.getParkingSpot();
		assertFalse(parkingSpot.isAvailable());

		// TODO: check that a ticket is actually saved in DB and Parking table is
		// updated with availability
	}

	@Test
	public void testParkingLotExit1() {
		testParkingACar1();
		final ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processExitingVehicle();

		final Ticket ticket = ticketDAO.getTicket("ABCDEF");
		assertNotNull(ticket.getOutTime());
		assertNotNull(ticket.getPrice());

		// TODO: check that the fare generated and out time are populated correctly in
		// the database
	}


    @Test
    public void testParkingACar2() throws Exception{
        final ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
        
        con = dataBaseTestConfig.getConnection();

		String sql = "SELECT VEHICLE_REG_NUMBER FROM ticket WHERE ID=?";
		PreparedStatement statement = con.prepareStatement(sql);
		statement.setInt(1, 1);
		ResultSet rs = statement.executeQuery();
		rs.next();

		String sql1 = "SELECT AVAILABLE FROM parking WHERE PARKING_NUMBER=?";
		PreparedStatement statement1 = con.prepareStatement(sql1);
		statement1.setInt(1, 1);
		ResultSet rs1 = statement1.executeQuery();
		rs1.next();

		
		// assertThat(rs.getString(1)).isEqualTo("ABCDEF");

		// assertThat(rs1.getInt(1)).isEqualTo(0);
    }

    @Test
    public void testParkingLotExit2() throws Exception{
        testParkingACar1();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processExitingVehicle();
        //TODO: check that the fare generated and out time are populated correctly in the database
        
        TimeUnit.MILLISECONDS.sleep(500);
		parkingService.processExitingVehicle();
		
		con = dataBaseTestConfig.getConnection();

		String sql2 = "SELECT PRICE FROM ticket WHERE ID=?";
		PreparedStatement statement2 = con.prepareStatement(sql2);
		statement2.setInt(1, 1);
		ResultSet rs2 = statement2.executeQuery();
		rs2.next();

		String sql3 = "SELECT OUT_TIME FROM ticket WHERE ID=?";
		PreparedStatement statement3 = con.prepareStatement(sql3);
		statement3.setInt(1, 1);
		ResultSet rs3 = statement3.executeQuery();
		rs3.next();

		
		// assertThat(rs2.getDouble(1)).isEqualTo(ticketDAO.getTicket("ABCDEF").getPrice());

		// assertThat(rs3.getTimestamp(1)).isEqualTo(ticketDAO.getTicket("ABCDEF").getOutTime());
		
		
    }}