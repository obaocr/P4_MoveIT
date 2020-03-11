package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	// method to round the fare
	public static double roundFare(double fare) {
		return Math.round(fare * 100) / 100.0;
	}

	// calculate the duration in hour
	// TODO : A voir si on peut faire plus court ou laisser en l'etat
	private static double calculateDuration(Ticket ticket) {
		long diff = ticket.getOutTime().getTime() - ticket.getInTime().getTime();
		long diffDays = diff / (24 * 60 * 60 * 1000);
		long diffHours = diff / (60 * 60 * 1000) % 24;
		long diffMinutes = diff / (60 * 1000) % 60;
		return (diffDays * 24 + diffHours + diffMinutes / 60.0);
	}

	// OBA : Nouvelle methode pour le tarif avec un argument en plus ce qui facilite
	// les tests unitaires
	public void calculateFare(Ticket ticket, int NbOccVeh) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}
		if (NbOccVeh < 0 ) {
			throw new IllegalArgumentException("Invalide occurence for recurring users :" + NbOccVeh);
		}

		Double pctDiscount = 0.0;
		double calculateFare = 0.0;

		// OBA : Fix issue to calculate duration, round for the fare
		double duration = calculateDuration(ticket);

		// OBA Calculate PctDiscount
		if (NbOccVeh > 0) {
			pctDiscount = Fare.PCT_DISCOUNT_REC_USERS;
		}

		switch (ticket.getParkingSpot().getParkingType()) {
		case CAR: {
			if (duration < Fare.FREE_DURATION_IN_HOUR) {
				ticket.setPrice(Fare.FARE_LESS_30_MIN);
			} else {
				calculateFare = duration * Fare.CAR_RATE_PER_HOUR;
				calculateFare = calculateFare - ((calculateFare * pctDiscount) / 100.0);
				ticket.setPrice(roundFare(calculateFare));
			}
			break;
		}
		case BIKE: {
			if (duration < Fare.FREE_DURATION_IN_HOUR) {
				ticket.setPrice(Fare.FARE_LESS_30_MIN);
			} else {
				calculateFare = duration * Fare.BIKE_RATE_PER_HOUR;
				calculateFare = calculateFare - ((calculateFare * pctDiscount) / 100.0);
				ticket.setPrice(roundFare(calculateFare));
			}
			break;
		}
		default:
			throw new IllegalArgumentException("Unkown Parking Type");
		}
	}

}