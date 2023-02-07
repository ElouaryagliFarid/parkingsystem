package com.parkit.parkingsystem.model;

import java.time.LocalDateTime;

/**
 * Ticket class allow to store the variants of a Ticket table record.
 * 
 * @author Farid
 *
 */
public class Ticket {
	/**
	 * The unique identifier of a ticket.
	 */
	private int id;
	/**
	 * The number of the parking spot allocated to the vehicle.
	 */
	private ParkingSpot parkingSpot;
	/**
	 * The vehicleRegNumber of the vehicle.
	 */
	private String vehicleRegNumber;
	/**
	 * The price to pay when exit from parking.
	 */
	private double price;
	/**
	 * The LocalDateTime of parking entry.
	 */
	private LocalDateTime inTime;
	/**
	 * The LocalDateTime of parking exit.
	 */
	private LocalDateTime outTime;

	/*
	 * private Date inTime2;
	 * 
	 * private Date outTime2;
	 */

	/**
	 * Getter of Ticket.id.
	 * 
	 * @return int the id of the ticket
	 */
	public int getId() {
		return id;
	}

	/**
	 * Setter of Ticket.id.
	 * 
	 * @param id the id of the ticket
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Getter of Ticket.parkingSpot.
	 * 
	 * @return a ParkingSpot object
	 */
	public ParkingSpot getParkingSpot() {
		return parkingSpot;
	}

	/**
	 * Setter of Ticket.parkingSpot.
	 * 
	 * @param parkingSpot
	 */
	public void setParkingSpot(ParkingSpot parkingSpot) {
		this.parkingSpot = parkingSpot;
	}

	/**
	 * Getter of Ticket.parkingSpot.
	 * 
	 * @return a String - the vehicule registration number
	 */
	public String getVehicleRegNumber() {
		return vehicleRegNumber;
	}

	/**
	 * Setter of Ticket.parkingSpot.
	 * 
	 * @param vehicleRegNumber the vehicule registration number to set
	 */
	public void setVehicleRegNumber(String vehicleRegNumber) {
		this.vehicleRegNumber = vehicleRegNumber;
	}

	/**
	 * Getter of Ticket.price.
	 * 
	 * @return the price to pay when exit parking
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * Setter of Ticket.price.
	 * 
	 * @param price to pay
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * Getter of Ticket.inTime.
	 * 
	 * @return the LocalDateTime of parking entry
	 */
	public LocalDateTime getInTime() {
		return inTime;
	}

	/**
	 * Setter of Ticket.inTime.
	 * 
	 * @param inTime - the LocalDateTime of entry
	 */
	public void setInTime(LocalDateTime inTime) {
		this.inTime = inTime;
	}

	/**
	 * Getter of Ticket.outTime.
	 * 
	 * @return the LocalDateTime of parking exit
	 */
	public LocalDateTime getOutTime() {
		return outTime;
	}

	/**
	 * Setter of Ticket.ouTime.
	 * 
	 * @param outTime the LocalDateTime of exit
	 */
	public void setOutTime(LocalDateTime outTime) {
		this.outTime = outTime;
	}

	/*
	 * public void setInTimeDate(Date inTime2) { this.inTime2 = inTime2;
	 * 
	 * }
	 * 
	 * public void setOutTimeDate(Date inTime2) { this.inTime2 = inTime2; }
	 */
}
