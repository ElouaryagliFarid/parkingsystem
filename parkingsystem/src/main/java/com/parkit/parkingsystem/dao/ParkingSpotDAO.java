package com.parkit.parkingsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
/**
 * ParkingSpotDAO class define the methods used to process the parking table of
 * the prod database.
 */
public class ParkingSpotDAO {
	/**
	 * Initialise a Logger used to send messages to the console.
	 */
	private static final Logger LOGGER = LogManager.getLogger("ParkingSpotDAO");
	/**
	 * Create a DataBasObject used to make a connection with the Prod DataBase.
	 */
	public DataBaseConfig dataBaseConfig = new DataBaseConfig();
	/**
	 * allows to obtain an available Parking Spot for a type of vehicle.
	 *
	 * @param parkingType
	 *            the type of vehicle
	 * @see ParkingType
	 * @return an int, the ParkingSpot id
	 * @return -1 if parking is full
	 */
	public int getNextAvailableSlot(ParkingType parkingType) {
		Connection con = null;
		int result = -1;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
			ps.setString(1, parkingType.toString());
			rs = ps.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}

		} catch (Exception ex) {
			LOGGER.error("Error fetching next available slot");
		} finally {
			dataBaseConfig.closeResultSet(rs);
			dataBaseConfig.closePreparedStatement(ps);
			dataBaseConfig.closeConnection(con);
		}
		return result;
	}
	/**
	 * Used to update availability of the given ParkingSpot.
	 * 
	 * @param parkingSpot
	 *            the ParkingSpot to update
	 * @see ParkingSpot
	 * @return true if the parking space has been updated , False if not .
	 */
	public boolean updateParking(ParkingSpot parkingSpot) {
		// update the availability for that parking slot
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT);
			ps.setBoolean(1, parkingSpot.isAvailable());
			ps.setInt(2, parkingSpot.getId());
			int updateRowCount = ps.executeUpdate();

			return (updateRowCount == 1);
		} catch (Exception ex) {
			LOGGER.error("Error updating parking info");
			return false;
		} finally {
			if (ps != null) {
				dataBaseConfig.closePreparedStatement(ps);
			}
			dataBaseConfig.closeConnection(con);

		}

	}
	/**
	 * check if a Vehicle is already in the parking lot
	 * 
	 * @param vehicleRegNumber
	 *            Vehicle registration number to be verified
	 * @return true if the Vehicle is in the parking lot, false if it is not.
	 */
	public boolean vehicleIsInParking(String vehicleRegNumber) {
		Connection con = null;
		boolean result = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.GET_VEHICLES_IN_PARKING);
			ps.setString(1, vehicleRegNumber);
			rs = ps.executeQuery();

			if (rs.next()) {
				result = (rs.getString(1) != null);

			}

		} catch (Exception ex) {
			LOGGER.error("Error vehicule exit controlling");
		} finally {
			dataBaseConfig.closeResultSet(rs);
			dataBaseConfig.closePreparedStatement(ps);
			dataBaseConfig.closeConnection(con);
		}

		return result;

	}

}
