package com.parkit.parkingsystem.service;

import java.time.Duration;
import java.time.LocalDateTime;

import org.apache.commons.math3.util.Precision;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		// int inHour = ticket.getInTime().getHour();
		// int outHour = ticket.getOutTime().getHour();

		// TODO: Some tests are failing here. Need to check if this logic is correct

		double duration = durationToBePaid(ticket.getInTime(), ticket.getOutTime());

		double fare = 0;

		switch (ticket.getParkingSpot().getParkingType()) {

		case CAR: {
			fare = Precision.round(
					(duration / 60) * Fare.CAR_RATE_PER_HOUR, 2); // le prix correspondra à la durée en heure mutliplié
																	// par le tarif horaire
			break;
		}
		case BIKE: {
			fare = Precision.round(
					(duration / 60) * Fare.BIKE_RATE_PER_HOUR, 2);
			break;
		}
		default:

			throw new IllegalArgumentException("Unkown Parking Type"); // creer une execption

		}
		ticket.setPrice(fare);
	}

	/**
	 * Création d'une méthode pour le calcul de la durée en minutes en fonction du
	 * moment d'ntrée et du moment de la sortie.
	 * 
	 * @param inTime  instant où on rentre dans le parking
	 * @param outTime instant où l'on sort du parking
	 * @return la durée en minute dans le parking
	 */
	public double durationToBePaid(LocalDateTime inTime, LocalDateTime outTime) {

		return ((Duration.between(inTime, outTime).toMinutes() <= 30) // on considera (pour le calcul du tarif) que une
																		// durée
				? 0 // inférieure ou égal à 30 minutes sera ramenée à 0.
				: Duration.between(inTime, outTime).toMinutes());

	}

	/**
	 * calcul de la remise de 5% pour un billet donné
	 * 
	 * @param ticket billet qui aura la réduction
	 */
	public void fivePercentDiscount(Ticket ticket) {

		double discount = 0.05;
		double fare = ticket.getPrice() - (ticket.getPrice() * discount);

		ticket.setPrice(Precision.round(fare, 2));

	}

}