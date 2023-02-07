package com.parkit.parkingsystem.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

import nl.altindag.log.LogCaptor;

@ExtendWith(MockitoExtension.class)
class ParkingSpotDAOTest {

	private ParkingSpotDAO parkingSpotDAO;
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
	public void setUp()
			throws ClassNotFoundException, SQLException, IOException {
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		logCaptor = LogCaptor.forName("ParkingSpotDAO");
		logCaptor.setLogLevelToInfo();
		when(dataBaseTestConfig.getConnection()).thenReturn(connection);
	}

	@Test
	@DisplayName("obtenir le prochain emplacement disponible pour la voiture devrait renvoyer le numéro de stationnement")
	void getNextAvailableSlot_ForCar_ShouldRetrunParkingNumber()
			throws Exception {
		// Given
		when(connection.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT))
				.thenReturn(ps);
		when(ps.executeQuery()).thenReturn(rs);
		when(rs.next()).thenReturn(true);
		when(rs.getInt(1)).thenReturn(2);
		// When
		int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
		// Then
		assertThat(result).isEqualTo(2);
	}

	@Test
	@DisplayName("obtenir le prochain emplacement disponible pour le vélo devrait renvoyer le numéro de stationnement")
	void getNextAvailableSlot_ForBike_ShouldRetrunParkingNumber()
			throws Exception {
		// Given
		when(connection.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT))
				.thenReturn(ps);
		when(ps.executeQuery()).thenReturn(rs);
		when(rs.next()).thenReturn(true);
		when(rs.getInt(1)).thenReturn(4);
		// When
		int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE);
		// Then
		assertThat(result).isEqualTo(4);
	}

	@Test
	@DisplayName("obtenir le prochain emplacement disponible avec une erreur d'accès à la base de données devrait enregistrer une erreur")
	void getNextAvailableSlot_WithDatabaseAccesError__ShouldLogError()
			throws SQLException {
		// Given
		when(connection.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT))
				.thenReturn(ps);
		when(ps.executeQuery()).thenThrow(SQLException.class);
		// When
		int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
		// Then
		assertThat(logCaptor.getErrorLogs())
				.contains("Error fetching next available slot");
		assertThat(result).isEqualTo(-1);

	}

	@Test
	@DisplayName("obtenir le prochain emplacement disponible avec un parking complet devrait revenir moins un")
	void getNextAvailableSlot_WithFullParking_ShouldReturnMinusOne()
			throws SQLException {
		// Given
		when(connection.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT))
				.thenReturn(ps);
		when(ps.executeQuery()).thenReturn(rs);
		when(rs.next()).thenReturn(false);
		// When
		int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);
		// Then
		assertThat(result).isEqualTo(-1);

	}

	@Test
	@DisplayName("mettre à jour le stationnement avec un numéro de stationnement invalide devrait retourner faux")
	void updateParking_withInvalidParkingNumber_ShouldReturnFlase()
			throws SQLException {
		// Given
		ParkingSpot parkingSpot = new ParkingSpot(0, ParkingType.CAR, true);
		when(connection.prepareStatement(DBConstants.UPDATE_PARKING_SPOT))
				.thenReturn(ps);
		when(ps.executeUpdate()).thenReturn(0);
		// When Then
		assertFalse(parkingSpotDAO.updateParking(parkingSpot));

	}

	@Test
	@DisplayName("mettre à jour le stationnement avec une place de stationnement non valide doit renvoyer la valeur True")
	void updateParking_withAvalidParkingSpot_ShouldReturnTrue()
			throws SQLException {
		// Given
		ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, true);
		when(connection.prepareStatement(DBConstants.UPDATE_PARKING_SPOT))
				.thenReturn(ps);
		when(ps.executeUpdate()).thenReturn(1);
		// When Then
		assertTrue(parkingSpotDAO.updateParking(parkingSpot));
	}

	@Test
	@DisplayName("mettre à jour le stationnement avec une erreur d'accès à la base de données doit renvoyer False")
	void updateParking_withDatabaseAccesError_shouldReturnFalseAndLogError()
			throws SQLException {
		// Given
		ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
		when(connection.prepareStatement(DBConstants.UPDATE_PARKING_SPOT))
				.thenThrow(SQLException.class);
		// When Then
		assertFalse(parkingSpotDAO.updateParking(parkingSpot));
		assertThat(logCaptor.getErrorLogs())
				.contains("Error updating parking info");

	}

	@Test
	@DisplayName("le véhicule est en stationnement avec le véhicule est en stationnement devrait renvoyer Faux")
	void vehicleIsInParking_withvehicleIsInParking_shouldReturnFalse()
			throws SQLException {
		// Given
		String vehicleRegNumber = "AAA";
		when(connection.prepareStatement(DBConstants.GET_VEHICLES_IN_PARKING))
				.thenReturn(ps);
		when(ps.executeQuery()).thenReturn(rs);
		when(rs.next()).thenReturn(true);
		when(rs.getString(1)).thenReturn("AAA");
		// When Then
		assertTrue(parkingSpotDAO.vehicleIsInParking(vehicleRegNumber));
	}

	@Test
	@DisplayName("le véhicule est en stationnement avec le véhicule non en stationnement devrait renvoyer Vrai")
	void vehicleIsInParking_withvehicleNOTInParking_shouldReturnTrue()
			throws SQLException {
		// Given
		String vehicleRegNumber = "ABCDE";
		when(connection.prepareStatement(DBConstants.GET_VEHICLES_IN_PARKING))
				.thenReturn(ps);
		when(ps.executeQuery()).thenReturn(rs);
		when(rs.next()).thenReturn(true);
		when(rs.getString(1)).thenReturn(null);
		// When Then
		assertFalse(parkingSpotDAO.vehicleIsInParking(vehicleRegNumber));
	}

	@Test
	@DisplayName("le véhicule en stationnement avec un numéro d'enregistrement de véhicule nul doit renvoyer une erreur")
	void vehicleIsInParking_withNullvehicleRegNumber_shouldReturnFalseAndLogError()
			throws Exception, SQLException {
		// Given
		String vehicleRegNumber = null;
		when(connection.prepareStatement(DBConstants.GET_VEHICLES_IN_PARKING))
				.thenReturn(ps);
		doThrow(SQLException.class).when(ps).setString(1, vehicleRegNumber);
		// When Then
		assertFalse(parkingSpotDAO.vehicleIsInParking(vehicleRegNumber));
		assertThat(logCaptor.getErrorLogs())
				.contains("Error vehicule exit controlling");
	}

	@Test
	@DisplayName("véhicule en stationnement avec des données vides doit renvoyer faux")
	void vehicleIsInParking_withEmptyData_shouldReturnFalse()
			throws Exception, SQLException {
		// Given
		String vehicleRegNumber = "ABCDE";
		when(connection.prepareStatement(DBConstants.GET_VEHICLES_IN_PARKING))
				.thenReturn(ps);
		when(ps.executeQuery()).thenReturn(rs);
		when(rs.next()).thenReturn(false);
		// When Then
		assertFalse(parkingSpotDAO.vehicleIsInParking(vehicleRegNumber));

	}

}
