package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

/**
 * ParkingSpot class allow to store the variants of a parking table record.
 * 
 * @author Farid
 *
 */
public class ParkingSpot {
	/**
	 * The unique identifier of a parking spot.
	 */
	private int number;
	/**
	 * The parking type .
	 * 
	 * @see ParkingSpot
	 */
	private ParkingType parkingType;
	/**
	 * Tell if parking spot is used or free.
	 */
	private boolean isAvailable;

	/**
	 * Class constructor.
	 *
	 * @param numb      the unique identifier of a parking spot
	 * @param pkType    the parking type (CAR or BIKE)
	 * @param available that tell if parking spot is used or free
	 */
	public ParkingSpot(int number, ParkingType parkingType,
			boolean isAvailable) {
		this.number = number;
		this.parkingType = parkingType;
		this.isAvailable = isAvailable;
	}

	/**
	 * Getter of ParkingSpot.number.
	 *
	 * @return an int the id the parking spot.
	 */
	public int getId() {
		return number;
	}

	/**
	 * Setter of ParkingSpot.number.
	 *
	 * @param id the parking spot identifier
	 */
	public void setId(int number) {
		this.number = number;
	}

	/**
	 * Getter of ParkingSpot.ParkingType.
	 *
	 * @return a ParkingType (CAR or BIKE)
	 */
	public ParkingType getParkingType() {
		return parkingType;
	}

	/**
	 * Setter of ParkingSpot.ParkingType.
	 *
	 * @param parkType the parking spot type
	 */
	public void setParkingType(ParkingType parkingType) {
		this.parkingType = parkingType;
	}

	/**
	 * Getter of ParkingSpot.isAvailable.
	 *
	 * @return boolean that tell if spot is used or free.
	 */
	public boolean isAvailable() {
		return isAvailable;
	}

	/**
	 * Setter of ParkingSpot.isAvailable.
	 *
	 * @param available
	 */
	public void setAvailable(boolean available) {
		isAvailable = available;
	}

	/**
	 * verify if this parking spot is equal to another object
	 * 
	 * @param o object to be verified
	 * @return true if the object is equal to this parking Spot, false if not.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ParkingSpot that = (ParkingSpot) o;
		return number == that.number;
	}

	@Override
	public int hashCode() {
		return number;
	}
}
