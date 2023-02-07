package com.parkit.parkingsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

/**
 * TicketDAO class define the methods used to process the Ticket table of the
 * prod database.
 * 
 * @author Farid
 * @see Ticket
 * @see DBConstants
 * @see DataBaseConfig
 */
public class TicketDAO {
	/**
	 * Initialise a Logger used to send messages to the console.
	 */
	private static final Logger LOGGER = LogManager.getLogger("TicketDAO");
	/**
	 * Create a DataBasObject used to make a connection with the Prod DataBase.
	 */
	public DataBaseConfig dataBaseConfig = new DataBaseConfig();

	/**
	 * Used to save the given ticket in the ticket table of prod DB.
	 *
	 * @param ticket the Ticket to save
	 * @return true if the ticket is saved, false if not .
	 */
	public boolean saveTicket(Ticket ticket) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.SAVE_TICKET);
			// ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
			// ps.setInt(1,ticket.getId());
			ps.setInt(1, ticket.getParkingSpot().getId());
			ps.setString(2, ticket.getVehicleRegNumber());
			ps.setDouble(3, ticket.getPrice());
			ps.setTimestamp(4, Timestamp.valueOf(ticket.getInTime()));
			ps.setTimestamp(5,
					(ticket.getOutTime() == null)
							? null
							: Timestamp.valueOf(ticket.getOutTime()));

			return ps.execute();
		} catch (Exception ex) {
			LOGGER.error("Error fetching next available slot");
			return false;
		} finally {
			dataBaseConfig.closePreparedStatement(ps);
			dataBaseConfig.closeConnection(con);
		}
	}

	/**
	 * Used to get the latest Ticket of the given vehicleRegNumber.
	 *
	 * @param vehicleRegNumber Vehicle registration number of the ticket
	 * @return the latest Ticket of the given vehicleRegNumber
	 */
	public Ticket getTicket(String vehicleRegNumber) {
		Connection con = null;
		Ticket ticket = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.GET_TICKET);
			// ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
			ps.setString(1, vehicleRegNumber);
			rs = ps.executeQuery();
			if (rs.next()) {
				ticket = new Ticket();
				ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1),
						ParkingType.valueOf(rs.getString(6)), false);
				ticket.setParkingSpot(parkingSpot);
				ticket.setId(rs.getInt(2));
				ticket.setVehicleRegNumber(vehicleRegNumber);
				ticket.setPrice(rs.getDouble(3));
				ticket.setInTime(rs.getTimestamp(4).toLocalDateTime());
				ticket.setOutTime((rs.getTimestamp(5) == null)
						? null
						: rs.getTimestamp(5).toLocalDateTime());
			}

		} catch (Exception ex) {
			LOGGER.error("Error fetching next available slot");
		} finally {

			dataBaseConfig.closeResultSet(rs);
			dataBaseConfig.closePreparedStatement(ps);
			dataBaseConfig.closeConnection(con);

		}
		return ticket;
	}

	/**
	 * used to update given ticket with price and outTime
	 * 
	 * @param ticket the Ticket to be updated
	 * @return true if the ticket was updated successfully false if the updating
	 *         process failed
	 */
	public boolean updateTicket(Ticket ticket) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
			ps.setDouble(1, ticket.getPrice());
			ps.setTimestamp(2, Timestamp.valueOf(ticket.getOutTime()));
			// ps.setInt(3,ticket.getId());
			ps.setString(3, ticket.getVehicleRegNumber());
			ps.execute();
			return true;
		} catch (Exception ex) {
			LOGGER.error("Error saving ticket info");

		} finally {
			dataBaseConfig.closePreparedStatement(ps);
			dataBaseConfig.closeConnection(con);
		}
		return false;
	}

	/**
	 * utilisé pour vérifier si le véhicule a déjà été en stationnement
	 * 
	 * @param Numéro d'immmatriculation du véhicule à vérifier
	 * @return True si le véhicule a déjà été en stationnement, False sinon .
	 */
	public boolean recurrentUser(String vehicleRegNumber) {
		Connection con = null;
		boolean result = false;
		PreparedStatement ps = null;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.GET_VEHICLES_IN_TICKET);
			ps.setString(1, vehicleRegNumber);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				result = (rs.getString(1) != null);
			}
			dataBaseConfig.closeResultSet(rs);
		} catch (Exception ex) {
			LOGGER.error("Error fetching recurrent User ");
		} finally {

			dataBaseConfig.closePreparedStatement(ps);
			dataBaseConfig.closeConnection(con);
		}

		return result;

	}
}
