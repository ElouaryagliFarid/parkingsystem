package com.parkit.parkingsystem.integration;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.config.DataBaseConfig;

class DataBaseConfigSIT {

	DataBaseConfig dataBaseConfig = new DataBaseConfig();

	private Connection connection;

	private ResultSet resultSet;

	private PreparedStatement preparedstatement;

	@BeforeEach
	public void setUp() throws ClassNotFoundException, SQLException {
		dataBaseConfig = new DataBaseConfig();
		connection = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/test", "root", "rootroot");
		preparedstatement = connection.prepareStatement("select * from ticket");
	}

	/*
	 * @Test void getConnection_shouldNotReturnNull() throws ClassNotFoundException,
	 * SQLException, IOException {
	 * 
	 * assertThat(dataBaseConfig.getConnection()).isNotNull(); }
	 */

	@Test
	void closeConnection_shouldCloseConnection() throws SQLException {

		// When
		dataBaseConfig.closeConnection(connection);
		// then
		assertTrue(connection.isClosed());

	}

	@Test
	void closePreparedStatement_shouldclosePreparedStatement()
			throws SQLException {
		// When
		dataBaseConfig.closePreparedStatement(preparedstatement);
		// Then
		assertTrue(preparedstatement.isClosed());
	}

	@Test
	void closeResult_shouldcloseResultSet() throws SQLException {
		// Given
		resultSet = preparedstatement.executeQuery();
		// When
		dataBaseConfig.closeResultSet(resultSet);
		// Then
		assertTrue(resultSet.isClosed());
	}

}
