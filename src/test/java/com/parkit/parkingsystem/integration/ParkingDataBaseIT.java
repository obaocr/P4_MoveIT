package com.parkit.parkingsystem.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
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
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		dataBasePrepareService.clearDataBaseEntries();
	}

	@AfterAll
	private static void tearDown() {

	}

	@Test
	public void testParkingACar() throws Exception {
		when(inputReaderUtil.getCurrentDate()).thenReturn(new Date(System.currentTimeMillis()));
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();
		assertNotNull(ticketDAO.getTicket(inputReaderUtil.readVehicleRegistrationNumber()));
		assertTrue(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR) > 1);
		// TODO: check that a ticket is actualy saved in DB and Parking table is updated
		// with availability
	}

	@Test
	public void testParkingLotExit() throws Exception {
		when(inputReaderUtil.getCurrentDate()).thenReturn(new Date(System.currentTimeMillis() - (45 * 60 * 1000)));
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();
		Thread.sleep(100);
		when(inputReaderUtil.getCurrentDate()).thenReturn(new Date(System.currentTimeMillis()));
		parkingService.processExitingVehicle();
		assertTrue(ticketDAO.getTicket(inputReaderUtil.readVehicleRegistrationNumber()).getPrice() > 0);
		assertNotNull(ticketDAO.getTicket(inputReaderUtil.readVehicleRegistrationNumber()).getOutTime());
		// TODO: check that the fare generated and out time are populated correctly in
		// the database
	}
	
	// OBA ajout d'un test pour tester la requête de comptage pour les clients recurrents
	@Test
	public void testParkingLotRecurrentUser() throws Exception {
		Thread.sleep(100);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		when(inputReaderUtil.getCurrentDate()).thenReturn(new Date(System.currentTimeMillis() - (120 * 60 * 1000)));
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();
		Thread.sleep(100);
		when(inputReaderUtil.getCurrentDate()).thenReturn(new Date(System.currentTimeMillis()));
		parkingService.processExitingVehicle();
		Thread.sleep(100);
		parkingService.processIncomingVehicle();
		// On peut tester la requête de comptage
		assertTrue(ticketDAO.getCountTicketByVehRegNum(inputReaderUtil.readVehicleRegistrationNumber()) > 0);
	}

}
