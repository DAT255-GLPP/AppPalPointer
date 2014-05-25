package com.example.palpointer;

public class Calculations {

	public static int calculateDistance (double Lat1, double Long1, double Lat2, double Long2){

		double radius = 6371000;
		double dLat = Math.toRadians(Lat1 - Lat2);
		double dLong = Math.toRadians(Long2 - Long1);
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) + 
				Math.cos(Math.toRadians(Lat1)) * Math.cos(Math.toRadians(Lat2)) *
				Math.sin(dLong/2) * Math.sin(dLong/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		return (int) Math.round(radius * c);
	}
	
	public static double calculateBearing (double Lat2, double Long2, double Lat1, double Long1){
		Lat1 = Math.toRadians(Lat1);
		Long1 = Math.toRadians(Long1);
		Lat2 = Math.toRadians(Lat2);
		Long2 = Math.toRadians(Long2);
		double dLong = (Long2-Long1);
		double y = Math.sin(dLong) * Math.cos(Lat2);
		double x = Math.cos(Lat1)*Math.sin(Lat2) - Math.sin(Lat1)*Math.cos(Lat2)*Math.cos(dLong);
		double bearing = Math.toDegrees((Math.atan2(y, x)));
		return (360 - ((bearing + 360) % 360));
	}
}
