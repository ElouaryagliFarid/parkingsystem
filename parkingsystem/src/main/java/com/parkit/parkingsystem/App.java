package com.parkit.parkingsystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.service.InteractiveShell;

/*
 * class main
 * 
 * @author Farid
 *
 */
public class App {
	private static final Logger LOGGER = LogManager.getLogger("App");

	/**
	 * method main
	 */

	public static void main(String[] args) {
		LOGGER.info("Initializing Parking System");
		InteractiveShell.loadInterface();
	}
}
