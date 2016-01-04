package com.lilalinda.younicos;

import java.util.Date;

/**
 * A pair of power and time stamp values.
 */
public final class PowerDatePair {

    final Date timestamp;
    final long powerInKW;

    public PowerDatePair(Date timestamp, long powerInKW) {
        if (timestamp == null)
            throw new IllegalArgumentException("Cannot create power timestamp pair with NULL date!");
        this.timestamp = timestamp;
        this.powerInKW = powerInKW;
    }
}
