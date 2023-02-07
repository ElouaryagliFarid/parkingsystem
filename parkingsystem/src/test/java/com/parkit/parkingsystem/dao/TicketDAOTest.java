package com.parkit.parkingsystem.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

import nl.altindag.log.LogCaptor;

@ExtendWith(MockitoExtension.class)
class TicketDAOTest {
	TicketDAO ticketDAO;
	LocalDateTime inTime;
	Ticket ticket;
	private static LogCaptor logCaptor;

	@Mock
	private static DataBaseTestConfig dataBaseTestConfig;
	@Mock
	private static PreparedStatement ps;
	@Mock
	private static ResultSet rs;
	@Mock
	private static Connection connection;

	@BeforeEach
	private void setUpTest() throws ClassNotFoundException, SQLException, IOException {
		ticketDAO = new TicketDAO();
		ticket = new Ticket();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		ticket.setId(2);
		ticket.setVehicleRegNumber("ABCD");
		ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, true));
		ticket.setPrice(1.5);
		inTime = LocalDateTime.now().minusHours(1);
		ticket.setInTime(inTime);
		ticket.setOutTime(null);
		logCaptor = LogCaptor.forName("TicketDAO");
		logCaptor.setLogLevelToInfo();
		when(dataBaseTestConfig.getConnection()).thenReturn(connection);
	}

	@Test
	@DisplayName("enregistrer le test du ticket doit renvoyer la valeur True")
	void saveTicketTest_shouldReturnTrue() throws SQLException {
		// Given
		when(connection.prepareStatement(DBConstants.SAVE_TICKET))
				.thenReturn(ps);
		doNothing().when(ps).setInt(1, ticket.getParkingSpot().getId());
		doNothing().when(ps).setString(2, ticket.getVehicleRegNumber());

		doNothing().when(ps).setDouble(3, ticket.getPrice());
		doNothing().when(ps).setTimestamp(4,
				Timestamp.valueOf(ticket.getInTime()));
		doNothing().when(ps).setTimestamp(5, null);
		when(ps.execute()).thenReturn(Boolean.TRUE);
		// When
		boolean result = ticketDAO.saveTicket(ticket);
		// Then
		assertTrue(result);
	}

	@Test
	@DisplayName("enregistrer le ticket sans temps non nul doit renvoyer vrai")
	void saveTicket_withOutTimeNotNull_shouldReturnTrue() throws SQLException {
		// Given
		ticket.setOutTime(LocalDateTime.now());
		when(connection.prepareStatement(DBConstants.SAVE_TICKET))
				.thenReturn(ps);
		doNothing().when(ps).setInt(1, ticket.getParkingSpot().getId());
		doNothing().when(ps).setString(2, ticket.getVehicleRegNumber());

		doNothing().when(ps).setDouble(3, ticket.getPrice());
		doNothing().when(ps).setTimestamp(4,
				Timestamp.valueOf(ticket.getInTime()));
		doNothing().when(ps).setTimestamp(5,
				Timestamp.valueOf(ticket.getOutTime()));
		when(ps.execute()).thenReturn(Boolean.TRUE);
		// When
		boolean result = ticketDAO.saveTicket(ticket);
		// Then
		assertTrue(result);
	}

	@Test
	@DisplayName("enregistrer un ticket avec un numéro d'enregistrement de véhicule nul doit renvoyer Faux")
	void saveTicket_withNullVehicleRegNumber_shouldReturnFalse()
			throws SQLException {
		// Given
		ticket.setVehicleRegNumber(null);
		when(connection.prepareStatement(DBConstants.SAVE_TICKET))
				.thenReturn(ps);
		doThrow(SQLException.class).when(ps).setString(2,
				ticket.getVehicleRegNumber());

		// When
		boolean result = ticketDAO.saveTicket(ticket);
		// Then
		assertThat(logCaptor.getErrorLogs())
				.contains("Error fetching next available slot");
		assertFalse(result);
	}

	@Test
	@DisplayName("obtenir un billet devrait obtenir un billet avec le numéro d'immatriculation du véhicule hors stationnement")
	void getTicket_shouldGetTicket_withvehicleRegNumberOutOffParking()
			throws SQLException {
		// Given
		LocalDateTime outTime = LocalDateTime.now();
		String vehicleRegNumber = ticket.getVehicleRegNumber();
		ticket.setOutTime(LocalDateTime.now());
		when(connection.prepareStatement(DBConstants.GET_TICKET))
				.thenReturn(ps);
		when(ps.executeQuery()).thenReturn(rs);
		when(rs.next()).thenReturn(true);
		doReturn(1).when(rs).getInt(1);
		when(rs.getString(6)).thenReturn(ParkingType.CAR.toString());
		doReturn(2).when(rs).getInt(2);
		when(rs.getDouble(3)).thenReturn(1.5);
		doReturn(Timestamp.valueOf(inTime)).when(rs).getTimestamp(4);
		doReturn(Timestamp.valueOf(outTime)).when(rs).getTimestamp(5);

		// When
		Ticket resultTicket = ticketDAO.getTicket(vehicleRegNumber);
		// Then
		assertEquals(vehicleRegNumber, resultTicket.getVehicleRegNumber());
		assertEquals(ticket.getId(), resultTicket.getId());
		assertEquals(ticket.getParkingSpot(), resultTicket.getParkingSpot());

		assertEquals(ticket.getInTime(), resultTicket.getInTime());

	}

	@Test
	@DisplayName("obtenir un billet devrait obtenir un billet avec le numéro d'enregistrement du véhicule dans le stationnement")
	void getTicket_shouldGetTicket_withvehicleRegNumberInParking()
			throws SQLException {
		// Given
		String vehicleRegNumber = ticket.getVehicleRegNumber();
		ticket.setOutTime(LocalDateTime.now());
		when(connection.prepareStatement(DBConstants.GET_TICKET))
				.thenReturn(ps);
		when(ps.executeQuery()).thenReturn(rs);
		when(rs.next()).thenReturn(true);
		doReturn(1).when(rs).getInt(1);
		when(rs.getString(6)).thenReturn(ParkingType.CAR.toString());
		doReturn(2).when(rs).getInt(2);
		when(rs.getDouble(3)).thenReturn(1.5);
		doReturn(Timestamp.valueOf(inTime)).when(rs).getTimestamp(4);
		doReturn(null).when(rs).getTimestamp(5);

		// When
		Ticket resultTicket = ticketDAO.getTicket(vehicleRegNumber);
		// Then
		assertEquals(vehicleRegNumber, resultTicket.getVehicleRegNumber());
		assertEquals(ticket.getId(), resultTicket.getId());
		assertEquals(ticket.getParkingSpot(), resultTicket.getParkingSpot());
		assertEquals(ticket.getPrice(), resultTicket.getPrice());
		assertEquals(ticket.getInTime(), resultTicket.getInTime());
		assertThat(resultTicket.getOutTime()).isNull();
	}

	@Test
	@DisplayName("obtenir un ticket avec une erreur d'accès à la base de données devrait obtenir un ticket nul")
	void getTicket_WithDatabaseAccesError_shouldGetNullTicket()
			throws SQLException {
		// Given
		String vehicleRegNumber = "ABCD";
		when(connection.prepareStatement(DBConstants.GET_TICKET))
				.thenThrow(SQLException.class);
		// When
		Ticket resultTicket = ticketDAO.getTicket(vehicleRegNumber);
		// Then
		assertThat(logCaptor.getErrorLogs())
				.contains("Error fetching next available slot");
		assertThat(resultTicket).isNull();

	}

	@Test
	@DisplayName("obtenir un ticket avec une table vide devrait obtenir un ticket nul")
	void getTicket_withEmptyTable_shouldGetNullTicket() throws SQLException {
		// Given
		String vehicleRegNumber = "ABCD";
		when(connection.prepareStatement(DBConstants.GET_TICKET))
				.thenReturn(ps);
		when(ps.executeQuery()).thenReturn(rs);
		when(rs.next()).thenReturn(false);
		// When
		Ticket resultTicket = ticketDAO.getTicket(vehicleRegNumber);
		// Then
		assertThat(resultTicket).isNull();
	}

	@Test
	@DisplayName("le ticket de mise à jour doit renvoyer la valeur True")
	void updateTicket_shouldRetrunTrue() throws SQLException {
		// Given
		when(connection.prepareStatement(DBConstants.UPDATE_TICKET))
				.thenReturn(ps);
		ticket.setOutTime(LocalDateTime.now());
		doNothing().when(ps).setDouble(1, ticket.getPrice());
		doNothing().when(ps).setTimestamp(2,
				Timestamp.valueOf(ticket.getOutTime()));
		doNothing().when(ps).setString(3, ticket.getVehicleRegNumber());
		when(ps.execute()).thenReturn(Boolean.TRUE);
		// When
		boolean result = ticketDAO.updateTicket(ticket);
		// Then
		assertTrue(result);
	}

	@Test
	@DisplayName("l'erreur d'accès à la base de données de tickets de mise à jour doit renvoyer la valeur False")
	void updateTicket_DatabaseAccesError_shouldRetrunFalse()
			throws SQLException {
		// Given
		when(connection.prepareStatement(DBConstants.UPDATE_TICKET))
				.thenThrow(SQLException.class);
		// When
		boolean result = ticketDAO.updateTicket(ticket);
		// Then
		assertFalse(result);
		assertThat(logCaptor.getErrorLogs())
				.contains("Error saving ticket info");

	}

	@Test
	@DisplayName("L'utilisateur récurrent avec un utilisateur récurrent doit renvoyer la valeur True")
	void recurrentUser_WithARecurringUser_shouldRetrunTrue()
			throws SQLException {
		// Given
		String vehicleRegNumber = "ABCD";
		when(connection.prepareStatement(DBConstants.GET_VEHICLES_IN_TICKET))
				.thenReturn(ps);
		doNothing().when(ps).setString(1, vehicleRegNumber);
		when(ps.executeQuery()).thenReturn(rs);
		when(rs.next()).thenReturn(true);
		when(rs.getString(1)).thenReturn(vehicleRegNumber);
		// When
		boolean result = ticketDAO.recurrentUser(vehicleRegNumber);
		// Then
		assertTrue(result);
	}

	@Test
	@DisplayName("L'utilisateur récurrent avec un nouvel utilisateur doit renvoyer False")
	void recurrentUser_WithANewUser_shouldRetrunFalse() throws SQLException {
		// Given
		String vehicleRegNumber = "ABCD";
		when(connection.prepareStatement(DBConstants.GET_VEHICLES_IN_TICKET))
				.thenReturn(ps);
		doNothing().when(ps).setString(1, vehicleRegNumber);
		when(ps.executeQuery()).thenReturn(rs);
		when(rs.next()).thenReturn(true);
		when(rs.getString(1)).thenReturn(null);
		// When
		boolean result = ticketDAO.recurrentUser(vehicleRegNumber);
		// Then
		assertFalse(result);
	}

	@Test
	@DisplayName("L'utilisateur récurrent avec une erreur d'accès à la base de données doit renvoyer la valeur True")
	void recurrentUser_WithDatabaseAccesError_shouldRetrunTrue()
			throws SQLException {
		// Given
		String vehicleRegNumber = "ABCD";
		when(connection.prepareStatement(DBConstants.GET_VEHICLES_IN_TICKET))
				.thenReturn(ps);
		when(ps.executeQuery()).thenThrow(SQLException.class);
		// When
		boolean result = ticketDAO.recurrentUser(vehicleRegNumber);
		// Then
		assertThat(logCaptor.getErrorLogs())
				.contains("Error fetching recurrent User ");
		assertFalse(result);
	}

}
