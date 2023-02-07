package com.parkit.parkingsystem.util;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * InputReaderUtil Class used to read int keyboard inputs, and String
 * registering number inputs.
 * 
 * @author Farid
 *
 */
public class InputReaderUtil {

	private Scanner scan = new Scanner(System.in, "UTF-8");
	/**
	 * Initialise a Logger used to send messages to the console.
	 */
	private static final Logger LOGGER = LogManager.getLogger("" + "");
	/**
	 * . Read the selections
	 * 
	 * @return input by the selections
	 */
	public int readSelection() {
		try {
			int input = Integer.parseInt(scan.nextLine());
			return input;
		} catch (Exception e) {
			LOGGER.error("Error while reading user input from Shell", e);
			System.out.println(
					"Error reading input. Please enter valid number for proceeding further");
			return -1;
		}
	}
	/**
	 * . Read the number registration vehicle
	 * 
	 * @return the number registration vehicle
	 * @throws Exception
	 */
	public String readVehicleRegistrationNumber() throws Exception {
		try {
			String vehicleRegNumber = scan.nextLine();
			if (vehicleRegNumber == null
					|| vehicleRegNumber.trim().length() == 0) {
				throw new IllegalArgumentException("Invalid input provided");
			}
			return vehicleRegNumber;
		} catch (Exception e) {
			LOGGER.error("Error while reading user input from Shell", e);
			System.out.println(
					"Error reading input. Please enter a valid string for vehicle registration number");
			throw e;
		}
	}

}
