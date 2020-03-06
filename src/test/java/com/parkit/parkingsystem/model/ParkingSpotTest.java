package com.parkit.parkingsystem.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;

class ParkingSpotTest {
	
	private static ParkingSpot parkingSpot;
	
	@BeforeEach
	private void setUpPerTest() {
		parkingSpot = new ParkingSpot(1,ParkingType.CAR,true);
	}
	
	@Test
	void test() {
		parkingSpot.setParkingType(ParkingType.BIKE);
		parkingSpot.setId(2);
		assertEquals(parkingSpot.getId(),2);
		assertEquals(parkingSpot.getParkingType(),ParkingType.BIKE);
	}

}
