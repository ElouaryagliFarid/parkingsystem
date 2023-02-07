package com.parkit.parkingsystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
// import java.util.Date; est remplacer par la librairie LocalDateTime

import org.apache.commons.math3.util.Precision;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;

public class FareCalculatorServiceTest {

	private static FareCalculatorService fareCalculatorService;
	private Ticket ticket;

	@BeforeAll
	private static void setUp() {
		fareCalculatorService = new FareCalculatorService();
	}

	@BeforeEach
	private void setUpPerTest() {
		ticket = new Ticket();
	}

	@Test
	@DisplayName("Pour une voiture stationnée pendant une heure, le prix doit être égal au tarif horaire de la voiture.")
	public void calculateFareCar() {
		// Given
		LocalDateTime inTime = LocalDateTime.now().minusHours(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// When
		fareCalculatorService.calculateFare(ticket);
		// Then
		assertThat(ticket.getPrice()).isEqualTo(Precision.round(Fare.CAR_RATE_PER_HOUR, 2));
	}

	@Test
	@DisplayName("Pour un vélo stationné une heure, le prix doit être égal au tarif horaire du vélo.")
	public void calculateFareBike() {
		// Given
		LocalDateTime inTime = LocalDateTime.now().minusHours(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// When
		fareCalculatorService.calculateFare(ticket);
		// Then
		assertThat(ticket.getPrice()).isEqualTo(Precision.round(Fare.BIKE_RATE_PER_HOUR, 2));
	}

	@Test
	@DisplayName("Pour un type inconnu, on doit lever NullPointerException.")
	public void calculateFareUnkownType() {
		// Given
		LocalDateTime inTime = LocalDateTime.now().minusHours(2);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, null, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// Then
		assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
	}

	@Test
	@DisplayName("Pour un vélo, qui stationnerait dans le future, on doit lancer IllegalArgumentException")
	public void calculateFareBikeWithFutureInTime() {
		// Given
		LocalDateTime inTime = LocalDateTime.now();
		LocalDateTime outTime = LocalDateTime.now().minusHours(1);
		ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// Then
		assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
	}

	@Test
	@DisplayName("Pour un vélo, avec 45 minutes de temps de stationnement devrait donner 75% du tarif horaire de stationnement")
	public void calculateFareBikeWithLessThanOneHourParkingTime() {
		// Given
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(45);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// When
		fareCalculatorService.calculateFare(ticket);
		// Then
		// assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
		assertThat(ticket.getPrice()).isEqualTo(Precision.round(0.75 * Fare.BIKE_RATE_PER_HOUR, 2));

	}

	@Test
	@DisplayName("Pour une voiture, avec 45 minutes de temps de stationnement devrait donner 75% du tarif horaire de stationnement")
	void calculateFareCarWithLessThanOneHourParkingTime() {
		// Given
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(45);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// When
		fareCalculatorService.calculateFare(ticket);
		// Then
		// assertEquals(Precision.round(0.75 * Fare.CAR_RATE_PER_HOUR, 2),
		// ticket.getPrice());
		assertThat(ticket.getPrice()).isEqualTo(Precision.round(0.75 * Fare.CAR_RATE_PER_HOUR, 2));
	}

	@Test
	@DisplayName("Pour une voiture, definition du tarif horaire de stationnement après une durée de stationnement de 24 heures.")
	public void calculateFareCarWithMoreThanADayParkingTime() {
		// Given
		LocalDateTime inTime = LocalDateTime.now().minusHours(24);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// When
		fareCalculatorService.calculateFare(ticket);
		// Then
		// assertEquals(Precision.round(24 * Fare.CAR_RATE_PER_HOUR, 2),
		// ticket.getPrice());
		assertThat(ticket.getPrice()).isEqualTo(Precision.round(24 * Fare.CAR_RATE_PER_HOUR, 2));
	}

//*********************************

	@Test
	@DisplayName("Pour une voiture, avec 20 minutes de temps de stationnement pour la voiture devrait être gratuit")
	void calculateFareCarWithLessThan30Minutes() {
		// Given
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(20);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// When
		fareCalculatorService.calculateFare(ticket);
		// Then
		assertEquals((0), ticket.getPrice());
	}

	@Test
	@DisplayName("Pour un vélo, avec 20 minutes de temps de stationnement pour vélo devrait être gratuit")
	void calculateFareBikeWithLessThan30Minutes() {
		// Given
		LocalDateTime inTime = LocalDateTime.now().minusMinutes(20);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// When
		fareCalculatorService.calculateFare(ticket);
		// Then
		assertEquals((0), ticket.getPrice());
	}

	@Test
	@DisplayName("Réduction pour une voiture : Par heure de stationnement devrait obtenir un tarif avec une réduction de 5%")
	void fivePercentDiscount_ForOneHour_OfCarParking() {
		// Given
		LocalDateTime inTime = LocalDateTime.now().minusHours(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("ABCD");
		double fare = ticket.getPrice() - (ticket.getPrice() * 0.05);
		// When
		fareCalculatorService.fivePercentDiscount(ticket);
		// Then
		assertEquals(Precision.round(fare, 2), ticket.getPrice());
	}

	@Test
	@DisplayName("Durée à payer pour une heure, on devrait donner 60 minutes")
	void getTheDurationToBePaidTest_forMoreThen30Minutes() {
		// Given
		LocalDateTime inTime = LocalDateTime.now();
		LocalDateTime outTime = LocalDateTime.now().plusHours(1);
		// When
		double duration = fareCalculatorService.durationToBePaid(inTime,
				outTime);
		// Then
		assertThat(duration).isEqualTo(60);
	}

	@Test
	@DisplayName("Durée à payer pour 15 minutes, doit donner 0")
	void getTheDurationToBePaidTest_forLessThen30Minutes() {
		// Given
		LocalDateTime inTime = LocalDateTime.now();
		LocalDateTime outTime = LocalDateTime.now().plusMinutes(15);
		// When
		double duration = fareCalculatorService.durationToBePaid(inTime,
				outTime);
		// Then
		assertThat(duration).isZero();
	}

	@Test
	@DisplayName("La réduction pour un vélo, avec une heure de stationnement pour vélo devrait donner une réduction de 5 %")
	void fivePercentDiscountTest_forMoreThen30Minutes() {
		ticket.setInTime(LocalDateTime.now());
		ticket.setOutTime(LocalDateTime.now().plusHours(1));
		ticket.setParkingSpot(new ParkingSpot(4, ParkingType.BIKE, true));
		ticket.setVehicleRegNumber("ABCD");
		ticket.setPrice(Precision.round(Fare.BIKE_RATE_PER_HOUR, 2));

		double reducedPrice = ticket.getPrice() - (ticket.getPrice() * 0.05);
		fareCalculatorService.fivePercentDiscount(ticket);

		assertThat(ticket.getPrice()).isEqualTo(reducedPrice);

	}

	@Test
	@DisplayName("Pour un vélo, avec 20 minutes de temps de stationnement pour vélo, la remise devrait donner 0")
	void fivePercentDiscount_ForLessThen30minutes_OfBikeParking() {
		// Given
		LocalDateTime inTime = LocalDateTime.now().minusHours(1);
		LocalDateTime outTime = LocalDateTime.now();
		ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.BIKE, false);
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("ABCD");
		// When
		fareCalculatorService.fivePercentDiscount(ticket);
		// Then
		assertThat(ticket.getPrice()).isEqualTo(0);
	}

}
