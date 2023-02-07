package com.parkit.parkingsystem.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
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
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static ParkingSpotDAO parkingSpotDAO;
	private static TicketDAO ticketDAO;
	private static DataBasePrepareService dataBasePrepareService;

	@Mock
	private static InputReaderUtil inputReaderUtil;

	@BeforeAll
	private static void setUp() throws Exception {
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();

	}

	@BeforeEach
	private void setUpPerTest() throws Exception {
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(inputReaderUtil.readVehicleRegistrationNumber())
				.thenReturn("ABCDEF");
		dataBasePrepareService.clearDataBaseEntries();
	}

	@AfterAll
	private static void tearDown() {
		dataBasePrepareService.clearDataBaseEntries();
	}

	@Test
	@DisplayName("vérifier qu'un ticket est effectivement enregistré dans la base de données et que la table de stationnement est mise à jour avec la disponibilité")
	public void testParkingACar() {
		// Given
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();
		// TODO: check that a ticket is actualy saved in DB and Parking table is updated
		// with availability
		// obtenir la prochaine place de stationnement
		ParkingSpot parkingSpot = parkingService.getNextParkingNumberIfAvailable();
		// when
		parkingService.processIncomingVehicle();
		// Then
		Ticket ticket = new Ticket();
		// obtenir le ticket de DB pour comparer sa place de parking avec la suivante
		// obtenir une place de stationnement
		ticket = ticketDAO.getTicket("ABCDEF");
		assertThat(ticket).isNotNull();
		assertThat(ticket.getParkingSpot().getId()).isEqualTo(parkingSpot.getId());
		assertThat(ticket.getParkingSpot().getParkingType()).isEqualTo(parkingSpot.getParkingType());
		assertThat(ticket.getOutTime()).isNull(); // vérification de la mise à jour de la place de stationnement
		assertThat(parkingSpot.isAvailable()).isTrue();
		assertThat(ticket.getParkingSpot().isAvailable()).isFalse();
	}

	@Test
	@DisplayName("vérifier que le tarif généré et l'heure de sortie sont renseignés correctement dans la base de données")
	public void testParkingLotExit() throws InterruptedException {
		// When
		testParkingACar();
		TimeUnit.SECONDS.sleep(1);
		ParkingService parkingService = new ParkingService(inputReaderUtil,
				parkingSpotDAO, ticketDAO);
		// Given
		parkingService.processExitingVehicle();

		// Then
		Ticket ticket = new Ticket();
		// getting the ticket from DB
		ticket = ticketDAO.getTicket("ABCDEF");
		assertThat(ticket).isNotNull();
		assertThat(ticket.getOutTime()).isNotNull();
		assertThat(ticket.getOutTime()).isAfter(ticket.getInTime());
		assertThat(ticket.getPrice()).isZero();

	}

}
