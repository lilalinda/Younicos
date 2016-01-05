package com.lilalinda.younicos;

import java.util.Date;

/**
 * A pair of power and time stamp values.
 * This class is comparable by the time stamp value and consistent with equals().
 */
public final class PowerDatePair implements Comparable<PowerDatePair> {

    final Date timestamp;
    final long powerInKW;

    public PowerDatePair(Date timestamp, long powerInKW) {
        if (timestamp == null)
            throw new IllegalArgumentException("Cannot create power timestamp pair with NULL date!");
        this.timestamp = timestamp;
        this.powerInKW = powerInKW;
    }

    @Override
    public int compareTo(PowerDatePair o) {
        return timestamp.compareTo(o.timestamp);
    }

    @Override
    public String toString() {
        return String.format("<%s, %6d>", timestamp, powerInKW);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PowerDatePair that = (PowerDatePair) o;

        return powerInKW == that.powerInKW && timestamp.equals(that.timestamp);
    }

    @Override
    public int hashCode() {
        int result = timestamp.hashCode();
        result = 31 * result + (int) (powerInKW ^ (powerInKW >>> 32));
        return result;
    }
}
