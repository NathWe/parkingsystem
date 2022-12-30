package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class TicketDAO {

    private Logger logger = LogManager.getLogger("TicketDAO");

    public DataBaseConfig dataBaseConfig = new DataBaseConfig();
    
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;


	public boolean saveTicket(Ticket ticket){
        Connection con = null;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.SAVE_TICKET);
            //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
            //ps.setInt(1,ticket.getId());
            ps.setInt(1,ticket.getParkingSpot().getId());
            ps.setString(2, ticket.getVehicleRegNumber());
            ps.setDouble(3, ticket.getPrice());
            ps.setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
            ps.setTimestamp(5, (ticket.getOutTime() == null)?null: (new Timestamp(ticket.getOutTime().getTime())) );
            return ps.execute();
        }catch (final Exception ex){
            logger.error("Error fetching next available slot",ex);
        }finally {
            try {
            	if (ps != null)
            		ps.close();
            }catch (Exception e) {
            	logger.error("Error close resource PreparedStatement",e);
            }
            
            try {
            	if (con != null)
            		con.close();
            }catch (Exception e) {
            	logger.error("Error close resource connection", e);
            }
        }
            return false;
        }
    


	public Ticket getTicket(String vehicleRegNumber) {
        
        Ticket ticket = null;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.GET_TICKET);
            //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
            ps.setString(1,vehicleRegNumber);
            rs = ps.executeQuery();
            if(rs.next()){
                ticket = new Ticket();
                ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(6)),false);
                ticket.setParkingSpot(parkingSpot);
                ticket.setId(rs.getInt(2));
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(rs.getDouble(3));
                ticket.setInTime(rs.getTimestamp(4));
                ticket.setOutTime(rs.getTimestamp(5));
                System.out.println("Ticket get");
            }

        }catch (Exception ex){
            logger.error("Error fetching next available slot",ex);
        }finally {
        	 try {
             	if (rs != null)
             		rs.close();
             }catch (Exception e) {
             	logger.error("Error close resource resultuser",e);
             }
             
        	 try {
              	if (ps != null)
              		ps.close();
              }catch (Exception e) {
              	logger.error("Error close resource PreparedStatement",e);
              }
             try {
             	if (con != null)
             		con.close();
             }catch (Exception e) {
             	logger.error("Error close resource connection",e);
             }
         }
             return ticket;
         }
        

    public boolean updateTicket(Ticket ticket) {
      
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
            ps.setDouble(1, ticket.getPrice());
            ps.setTimestamp(2, new Timestamp(ticket.getOutTime().getTime()));
            ps.setInt(3,ticket.getId());
            ps.execute();
            System.out.println("Ticket update");
            
        }catch (final Exception ex){
            logger.error("Error saving ticket info",ex);
        }finally {
       	 try {
          	if (ps != null)
          		ps.close();
          }catch (Exception e) {
          	logger.error("Error close resource PreparedStatement", e);
          }
    	 try {
          	if (con != null)
          		con.close();
          }catch (Exception e) {
          	logger.error("Error close resource connexion", e);
          }
        }
        
        return false;
    }
    public void setLogger(Logger testlogger) {
    	this.logger = testlogger;
    }
}
