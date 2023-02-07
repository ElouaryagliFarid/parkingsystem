package com.parkit.parkingsystem.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;

import java.io.FileNotFoundException;
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

import nl.altindag.log.LogCaptor;

@ExtendWith(MockitoExtension.class)
class DataBaseConfigTest {
	DataBaseConfig dataBaseConfig;
	private static LogCaptor logCaptor;

	@Mock
	Connection connection;
	@Mock
	PreparedStatement preparedStatement;
	@Mock
	ResultSet resultSet;

	@BeforeEach
	private void setUpTest()
			throws ClassNotFoundException, SQLException, FileNotFoundException {
		dataBaseConfig = new DataBaseConfig();

		logCaptor = LogCaptor.forName("DataBaseConfig");
		logCaptor.setLogLevelToInfo();

	}

	@Test
	@DisplayName("fermer la connexion avec l'exception doit enregistrer l'erreur")
	void closeConnection_withExpetion_shouldlogError() throws SQLException {
		// Given
		doThrow(SQLException.class).when(connection).close();
		// When
		dataBaseConfig.closeConnection(connection);
		// Then
		assertThat(logCaptor.getErrorLogs())
				.contains("Error while closing connection");
		assertThat(connection.isClosed()).isFalse();

	}

	@Test
	@DisplayName("fermer l'instruction préparée avec exception doit enregistrer une erreur")
	void closePreparedStatement_withExpetion_shouldlogError()
			throws SQLException {
		// Given
		doThrow(SQLException.class).when(preparedStatement).close();
		// When
		dataBaseConfig.closePreparedStatement(preparedStatement);
		// Then
		assertThat(logCaptor.getErrorLogs())
				.contains("Error while closing prepared statement");
		assertThat(connection.isClosed()).isFalse();
	}

	@Test
	@DisplayName("fermer le jeu de résultats avec exception doit enregistrer une erreur")
	void closeResultSet_withExpetion_shouldlogError() throws SQLException {
		// Given
		doThrow(SQLException.class).when(resultSet).close();
		// When
		dataBaseConfig.closeResultSet(resultSet);
		// Then
		assertThat(logCaptor.getErrorLogs())
				.contains("Error while closing result set");
		assertThat(connection.isClosed()).isFalse();
	}

}
