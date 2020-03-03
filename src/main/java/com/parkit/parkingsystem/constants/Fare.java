package com.parkit.parkingsystem.constants;

public class Fare {
    public static final double BIKE_RATE_PER_HOUR = 1.0;
    public static final double CAR_RATE_PER_HOUR = 1.5;
    // Ajout pour gestion tarif des 30 premières minutes
    public static final double FARE_LESS_30_MIN = 0.0;
	public static final double FREE_DURATION_IN_HOUR = 0.5;
	// % discount pour les clients récurrents
	public static final double PCT_DISCOUNT_REC_USERS = 5.0;
}
