package com.parkit.parkingsystem.constants;
/**
 * Affect all SQL queries in public static final Strings.
 */

public class DBConstants {
	/**
	 * SQL query use to find an available parking spot.
	 */
	public static final String GET_NEXT_PARKING_SPOT = "select min(PARKING_NUMBER) from parking where AVAILABLE = true and TYPE = ?";
	/**
	 * SQL query used to update availability of a parking spot in Database.
	 */
	public static final String UPDATE_PARKING_SPOT = "update parking set available = ? where PARKING_NUMBER = ?";
	/**
	 * SQL query used to save a ticket in Database.
	 */
	public static final String SAVE_TICKET = "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values(?,?,?,?,?)";
	/**
	 * SQL query used to update a ticket in Database.
	 */
	public static final String UPDATE_TICKET = "update ticket set PRICE=?, OUT_TIME=? where VEHICLE_REG_NUMBER = ? order by ID DESC LIMIT 1";
	/**
	 * SQL query used to get a ticket from Database.
	 */
	public static final String GET_TICKET = "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, t.OUT_TIME, p.TYPE from ticket t,parking p where p.parking_number = t.parking_number and t.VEHICLE_REG_NUMBER=? t.OUT_TIME is NULL  order  by t.IN_TIME DESC limit 1";
	/**
	 * SQL query used to get a vehicles in parking .
	 */
	public static final String GET_VEHICLES_IN_PARKING = "select min(VEHICLE_REG_NUMBER) from ticket  where VEHICLE_REG_NUMBER = ? and OUT_TIME is NULL ";
	/**
	 * SQL query used to get a recurrent user from Database.
	 */
	public static final String GET_VEHICLES_IN_TICKET = "select min(VEHICLE_REG_NUMBER) from ticket  where VEHICLE_REG_NUMBER = ? and OUT_TIME is NOT NULL ";
}
