package com.gkancheva.tripmanager.repositories;

import com.gkancheva.tripmanager.model.trip.Trip;

import java.util.Date;
import java.util.List;

public class TripManager {

    public boolean saveNewTrip(Trip trip) {
        trip.save();
        return true;
    }

    public boolean editTrip(long tripId, Trip currentTrip) {
        Trip trip = Trip.findById(Trip.class, tripId);
        trip = currentTrip;
        trip.save();
        return true;
    }

    public boolean deleteTrip(long tripId) {
        Trip trip = Trip.findById(Trip.class, tripId);
        trip.delete();
        return true;
    }

    public Trip findTripById(long tripId) {
        return Trip.findById(Trip.class, tripId);
    }

    public List<Trip> getAllTrips() {
        return Trip.listAll(Trip.class);
    }

    public List<Trip> findOrdered() {
        List<Trip> trips = Trip.findWithQuery(Trip.class,
                "Select * FROM Trip ORDER BY m_start_date ASC");
        return trips;
    }

    public List<Trip> findOrderedUpcoming() {
        List<Trip> trips = Trip.findWithQuery(Trip.class,
                "Select * FROM Trip WHERE m_end_date > ? ORDER BY m_start_date ASC", String.valueOf(new Date().getTime()));
        return trips;
    }

    public Trip findFirstUpcomingTrip() {
        if(findOrdered().size() > 0) {
            Trip trip = findOrderedUpcoming().get(0);
            return trip;
        }
        return null;
    }
}
