package de.ms.ptenabler.locationtools;

import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.DistanceMeasure;

class MyDistanceMeasure implements DistanceMeasure{



	/**
	 * 
	 */
	private static final long serialVersionUID = -1994288737634809277L;

	@Override
	public boolean compare(double arg0, double arg1) {
		if(arg0<=arg1){
			return true;
		}
		return false;
	}

	@Override
	public double getMaxValue() {
		// TODO Auto-generated method stub
		return 180;
	}

	@Override
	public double getMinValue() {
		// TODO Auto-generated method stub
		return -90;
	}

	@Override
	public double measure(Instance arg0, Instance arg1) {
		// TODO Auto-generated method stub
		//double dis =
		//return dis;
		return computeDistance(arg0.get(0), arg0.get(1), arg1.get(0), arg1.get(1));
	}
	
	public float computeDistance(double lat1, double lon1, double lat2, double lon2)
	{
		// Based on http://www.ngs.noaa.gov/PUBS_LIB/inverse.pdf
		// using the "Inverse Formula" (section 4)

		final int MAXITERS = 20;
		// Convert lat/long to radians
		lat1 *= Math.PI / 180.0;
		lat2 *= Math.PI / 180.0;
		lon1 *= Math.PI / 180.0;
		lon2 *= Math.PI / 180.0;

		final double a = 6378137.0; // WGS84 major axis
		final double b = 6356752.3142; // WGS84 semi-major axis
		final double f = (a - b) / a;
		final double aSqMinusBSqOverBSq = (a * a - b * b) / (b * b);

		final double L = lon2 - lon1;
		double A = 0.0;
		final double U1 = Math.atan((1.0 - f) * Math.tan(lat1));
		final double U2 = Math.atan((1.0 - f) * Math.tan(lat2));

		final double cosU1 = Math.cos(U1);
		final double cosU2 = Math.cos(U2);
		final double sinU1 = Math.sin(U1);
		final double sinU2 = Math.sin(U2);
		final double cosU1cosU2 = cosU1 * cosU2;
		final double sinU1sinU2 = sinU1 * sinU2;

		double sigma = 0.0;
		double deltaSigma = 0.0;
		double cosSqAlpha = 0.0;
		double cos2SM = 0.0;
		double cosSigma = 0.0;
		double sinSigma = 0.0;
		double cosLambda = 0.0;
		double sinLambda = 0.0;

		double lambda = L; // initial guess
		for (int iter = 0; iter < MAXITERS; iter++)
		{
			final double lambdaOrig = lambda;
			cosLambda = Math.cos(lambda);
			sinLambda = Math.sin(lambda);
			final double t1 = cosU2 * sinLambda;
			final double t2 = cosU1 * sinU2 - sinU1 * cosU2 * cosLambda;
			final double sinSqSigma = t1 * t1 + t2 * t2; // (14)
			sinSigma = Math.sqrt(sinSqSigma);
			cosSigma = sinU1sinU2 + cosU1cosU2 * cosLambda; // (15)
			sigma = Math.atan2(sinSigma, cosSigma); // (16)
			final double sinAlpha = (sinSigma == 0) ? 0.0 : cosU1cosU2 * sinLambda / sinSigma; // (17)
			cosSqAlpha = 1.0 - sinAlpha * sinAlpha;
			cos2SM = (cosSqAlpha == 0) ? 0.0 : cosSigma - 2.0 * sinU1sinU2 / cosSqAlpha; // (18)

			final double uSquared = cosSqAlpha * aSqMinusBSqOverBSq; // defn
			A = 1 + (uSquared / 16384.0) * // (3)
					(4096.0 + uSquared * (-768 + uSquared * (320.0 - 175.0 * uSquared)));
			final double B = (uSquared / 1024.0) * // (4)
					(256.0 + uSquared * (-128.0 + uSquared * (74.0 - 47.0 * uSquared)));
			final double C = (f / 16.0) * cosSqAlpha * (4.0 + f * (4.0 - 3.0 * cosSqAlpha)); // (10)
			final double cos2SMSq = cos2SM * cos2SM;
			deltaSigma = B
					* sinSigma
					* // (6)
					(cos2SM + (B / 4.0)
							* (cosSigma * (-1.0 + 2.0 * cos2SMSq) - (B / 6.0) * cos2SM * (-3.0 + 4.0 * sinSigma * sinSigma) * (-3.0 + 4.0 * cos2SMSq)));

			lambda = L + (1.0 - C) * f * sinAlpha * (sigma + C * sinSigma * (cos2SM + C * cosSigma * (-1.0 + 2.0 * cos2SM * cos2SM))); // (11)

			final double delta = (lambda - lambdaOrig) / lambda;

			if (Math.abs(delta) < 1.0e-12)
				break;
		}

		return (float) (b * A * (sigma - deltaSigma));
	}
}