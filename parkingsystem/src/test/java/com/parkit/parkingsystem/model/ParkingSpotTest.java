package com.parkit.parkingsystem.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;

class ParkingSpotTest {
	ParkingSpot parkingSpot;
	@BeforeEach
	private void setUpTest() {
		parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
	}

	@Test
	void equalsTest_withAnEqualObject_shouldReturnTrue() {
		Object o = new ParkingSpot(1, ParkingType.CAR, true);
		boolean result = parkingSpot.equals(o);
		assertThat(result).isTrue();
	}
	@Test
	void equalsTest_sammeParkingSpotObjcet_shouldReturnTrue() {

		boolean result = parkingSpot.equals(parkingSpot);
		assertThat(result).isTrue();
	}

	@Test
	void equalsTest_withAnUnEqualObject_shouldReturnFalse() {

		Object o = new ParkingSpot(4, ParkingType.BIKE, true);
		boolean result = parkingSpot.equals(o);
		assertThat(result).isFalse();
	}
	@Test
	void equalsTest_withNewObject_shouldReturnFalse() {
		Object o = new Object();
		boolean result = parkingSpot.equals(o);
		assertThat(result).isFalse();
	}
	@Test
	void equalsTest_withNullObject_shouldReturnFalse() {
		boolean result = parkingSpot.equals(null);
		assertThat(result).isFalse();
	}
	@Test
	void HashCodeTest() {
		int result = parkingSpot.hashCode();
		assertThat(result).isEqualTo(1);
	}
}
