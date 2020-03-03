package com.parkit.parkingsystem.integration;

import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
		// OBA : Nettoyage de la database de test est faite une seule fois en debut des
		// tests et non a chaque test !
		dataBasePrepareService.clearDataBaseEntries();
	}

	@BeforeEach
	private void setUpPerTest() throws Exception {
		// OBA modifie pour ne conserver que la selection
		when(inputReaderUtil.readSelection()).thenReturn(1);
	}

	@AfterAll
	private static void tearDown() {

	}

	@Test
	public void testParkingACar() throws Exception {
		Thread.sleep(1000);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("AAAAA");
		when(inputReaderUtil.getCurrentTime()).thenReturn(new Date(System.currentTimeMillis()));
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();
		// TODO: check that a ticket is actualy saved in DB and Parking table is updated
		// with availability
	}

	// TODO : A voir comment generer un tarif avec une duree significative
	@Test
	public void testParkingLotExit() throws Exception {
		Thread.sleep(500);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		when(inputReaderUtil.getCurrentTime()).thenReturn(new Date(System.currentTimeMillis() - (45 * 60 * 1000)));
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();
		// sleep sinon peut planter à cause de OutTime
		Thread.sleep(500);
		when(inputReaderUtil.getCurrentTime()).thenReturn(new Date(System.currentTimeMillis()));
		ParkingService parkingService2 = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService2.processExitingVehicle();
		// TODO: check that the fare generated and out time are populated correctly in
		// the database
		Thread.sleep(500);
		when(inputReaderUtil.getCurrentTime()).thenReturn(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
		parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService.processIncomingVehicle();
		// sleep sinon peut planter à cause de OutTime
		Thread.sleep(500);
		when(inputReaderUtil.getCurrentTime()).thenReturn(new Date(System.currentTimeMillis()));
		parkingService2 = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		parkingService2.processExitingVehicle();
	}

}
