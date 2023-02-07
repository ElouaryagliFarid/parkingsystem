package com.parkit.parkingsystem.integration.config;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.config.DataBaseConfig;

public class DataBaseTestConfig extends DataBaseConfig {

	private static final Logger logger = LogManager.getLogger("DataBaseTestConfig");

	/*
	 * public Connection getConnection() throws ClassNotFoundException, SQLException
	 * { logger.info("Create DB connection");
	 * Class.forName("com.mysql.cj.jdbc.Driver"); return
	 * DriverManager.getConnection(
	 * "jdbc:mysql://localhost:3306/test","root","rootroot"); }
	 */
	@Override
	public Connection getConnection()
			throws ClassNotFoundException, SQLException, IOException {
		logger.info("Create DB connection");
		String url_timezone = "?serverTimezone=" + TimeZone.getDefault().getID();

		String url = "jdbc:mysql://localhost:3306/test";

		System.out.println(url_timezone);
		System.out.println(url);

		return DriverManager.getConnection(url + url_timezone, "root", "rootroot");

	}

	@Override
	public void closeConnection(Connection con) {
		if (con != null) {
			try {
				con.close();
				logger.info("Closing DB connection");
			} catch (SQLException e) {
				logger.error("Error while closing connection", e);
			}
		}
	}

	@Override
	public void closePreparedStatement(PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close();
				logger.info("Closing Prepared Statement");
			} catch (SQLException e) {
				logger.error("Error while closing prepared statement", e);
			}
		}
	}

	@Override
	public void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
				logger.info("Closing Result Set");
			} catch (SQLException e) {
				logger.error("Error while closing result set", e);
			}
		}
	}
}
