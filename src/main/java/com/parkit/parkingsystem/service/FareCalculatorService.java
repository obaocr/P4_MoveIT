package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {
	
	// method to round the fare
	private static double roundFare(double fare) {
		return Math.round(fare * 100)/100.0;
	}
	
	// calculate the duration in hour
	// TODO : A voir si on peut faire plkus court ou laisser en l'etat
	private static double calculateDuration(Ticket ticket) {
		long diff = ticket.getOutTime().getTime() - ticket.getInTime().getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000);
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffMinutes = diff / (60 * 1000) % 60;
        return (diffDays*24 + diffHours +  diffMinutes/60.0);
	}
	
	
    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }
        
        //OBA : Fix issue to calculate duration, round for the fare
        double duration = calculateDuration(ticket);
        
        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
            	ticket.setPrice(roundFare(duration * Fare.CAR_RATE_PER_HOUR));
                break;
            }
            case BIKE: {
                ticket.setPrice(roundFare(duration * Fare.BIKE_RATE_PER_HOUR));
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}