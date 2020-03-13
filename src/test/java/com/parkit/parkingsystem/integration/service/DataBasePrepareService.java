package com.parkit.parkingsystem.integration.service;

import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataBasePrepareService {

	DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();

	public void clearDataBaseEntries() {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = dataBaseTestConfig.getConnection();

			// set parking entries to available
			ps = connection.prepareStatement("update parking set available = true");
			ps.execute();
			dataBaseTestConfig.closePreparedStatement(ps);

			// clear ticket entries;
			ps = connection.prepareStatement("truncate table ticket");
			ps.execute();

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			dataBaseTestConfig.closeConnection(connection);
			dataBaseTestConfig.closePreparedStatement(ps);
		}
	}

}
