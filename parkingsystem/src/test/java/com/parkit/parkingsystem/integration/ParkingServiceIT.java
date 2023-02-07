package com.parkit.parkingsystem.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

import nl.altindag.log.LogCaptor;

@ExtendWith(MockitoExtension.class)
class ParkingServiceIT {

	private static ParkingService parkingService;

	private static LogCaptor logCaptor;
	private static DataBasePrepareService dataBasePrepareService;
	private static ParkingSpotDAO parkingSpotDAO;
	private static TicketDAO ticketDAO;
	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

	@Mock
	private static InputReaderUtil inputReaderUtil;

	private final static ByteArrayOutputStream outContent = new ByteArrayOutputStream();

	@BeforeAll
	private static void setUp() throws Exception {
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();

	}

	@BeforeEach
	void setUpPerTest() throws Exception {
		parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO,
				ticketDAO);
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(inputReaderUtil.readVehicleRegistrationNumber())
				.thenReturn("ABCDEF");
		dataBasePrepareService.clearDataBaseEntries();
		logCaptor = LogCaptor.forName("ParkingService");
		logCaptor.setLogLevelToInfo();
		dataBasePrepareService.clearDataBaseEntries();

	}

	@AfterEach
	void tearDownAfterClass() throws IOException {
		dataBasePrepareService.clearDataBaseEntries();

	}

	@Test
	@DisplayName("vérifier que le même véhicule n'entre pas deux fois dans le parking")
	void processIncomingVehicle_forAVehicleAlreadyInTheParking() {
		// Given
		parkingService.processIncomingVehicle();
		// when
		System.setOut(new PrintStream(outContent));
		parkingService.processIncomingVehicle();
		// Then
		assertThat(logCaptor.getErrorLogs())
				.contains("Unable to process incoming vehicle");

	}

	@Test

	@DisplayName("vérifier que le message de remise de 5 % s'affiche pour l'utilisateur récurrent")
	void processIncomingVehicle_forARecurringUser() throws InterruptedException, IOException { // Given

		parkingService.processIncomingVehicle();
		TimeUnit.SECONDS.sleep(1);
		parkingService.processExitingVehicle();
		TimeUnit.SECONDS.sleep(1);
		System.setOut(new PrintStream(outContent)); // when
		parkingService.processIncomingVehicle(); // Then
		assertThat(outContent.toString()).contains(
				"Welcome back! As a recurring user of our parking lot, you'll benefit from a 5% discount");

	}

}
