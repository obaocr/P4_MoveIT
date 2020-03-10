package com.parkit.parkingsystem.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ResourceBundle;

public class DataBaseConfig {

	private static final Logger logger = LogManager.getLogger("DataBaseConfig");

	// OBA TimeZone Issue
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		logger.info("Create DB connection");
		Class.forName("com.mysql.cj.jdbc.Driver");
		ResourceBundle mybundle = ResourceBundle.getBundle("config");
		return DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/prod?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=Europe/Paris",
				mybundle.getString("loginProd"), mybundle.getString("passwordProd"));
	}

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
