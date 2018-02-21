package com.gkancheva.tripmanager.repositories;

import com.gkancheva.tripmanager.model.expense.Accommodation;
import com.gkancheva.tripmanager.model.expense.Event;
import com.gkancheva.tripmanager.model.expense.Expense;
import com.gkancheva.tripmanager.model.expense.RentACar;
import com.gkancheva.tripmanager.model.expense.Transportation;
import com.gkancheva.tripmanager.model.trip.Trip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class EventExpenseManager {
    private TripManager mTripManager;

    public boolean saveNewExpense(Expense expense) {
        expense.save();
        return true;
    }

    public boolean editExpense(long expenseId, Expense currentExpense) {
        switch (currentExpense.getExpenseCategory()) {
            case AIRFARE:
            case BOAT:
            case BUS:
            case TRAIN:
            case OTHER_TRANSPORT:
                Transportation tr = findTrById(expenseId);
                tr = (Transportation)currentExpense;
                tr.save();
                return true;
            case ACCOMMODATION:
                Accommodation acc = findAccommodationById(expenseId);
                Accommodation acc2;
                if(acc.getType().equals("check-in")){
                    acc = (Accommodation)currentExpense;
                    acc.save();
                } else {
                    acc2 = findAccommodationById(acc.getId() - 1);
                    acc2 = (Accommodation)currentExpense;
                    acc2.save();
                }
                return true;
            case RENT_A_CAR:
                RentACar r = findRentACarById(expenseId);
                RentACar r1;
                if(r.getType().equals("pick-up")){
                    r = (RentACar) currentExpense;
                    r.save();
                } else {
                    r1 = findRentACarById(r.getId() - 1);
                    r1 = (RentACar) currentExpense;
                    r1.save();
                }
                return true;
            case FUEL:
            case GIFTS:
            case MEAL:
            case OTHER_SIMPLE_EXP:
            case PUBLIC_TRANSPORTATION:
            case SHOPPING:
            case TRAVEL_INSURANCE:
                Expense exp = findExpenseById(expenseId);
                exp = currentExpense;
                exp.save();
                return true;
            default:
                break;
        }
        return false;
    }

    public boolean deleteAllExpensesByTrip(long tripId) {
        mTripManager = new TripManager();
        List<Expense> expenses = getAllExpensesByTrip(mTripManager.findTripById(tripId));
        for(int i = 0; i < expenses.size(); i++) {
            expenses.get(i).delete();
        }
        return true;
    }

    public boolean deleteExpense(long expenseId) {
        Expense expense = Expense.findById(Expense.class, expenseId);
        expense.delete();
        return true;
    }

    public boolean deleteEvent(Event event) {
        switch (event.getExpenseCategory()) {
            case AIRFARE:
            case BOAT:
            case BUS:
            case OTHER_TRANSPORT:
            case TRAIN:
                Transportation tr = Transportation.findById(Transportation.class, event.getId());
                tr.delete();
                return true;
            case ACCOMMODATION:
                Accommodation acc1 = Accommodation.findById(Accommodation.class, event.getId());
                Accommodation acc2;
                if(acc1.getType().equals("check-in")){
                    acc2 = findAccommodationById(acc1.getId() + 1);
                } else {
                    acc2 = findAccommodationById(acc1.getId() - 1);
                }
                acc1.delete();
                acc2.delete();
                return true;
            case RENT_A_CAR:
                RentACar r = RentACar.findById(RentACar.class, event.getId());
                RentACar r1;
                if(r.getType().equals("check-in")){
                    r1 = findRentACarById(r.getId() + 1);
                } else {
                    r1 = findRentACarById(r.getId() - 1);
                }
                r.delete();
                r1.delete();
                return true;
            default:
                break;
        }
        return false;
    }

    public boolean deleteAllEventsByTrip(long tripId) {
        mTripManager = new TripManager();
        List<Event> events = getAllEventsByTrip(mTripManager.findTripById(tripId));
        for(int i = 0; i < events.size(); i++) {
            events.get(i).delete();
        }
        return true;
    }

    public Expense findExpenseById(long expenseId) {
        return Expense.findById(Expense.class, expenseId);
    }

    public Transportation findTrById(long id) {
        return Transportation.findById(Transportation.class, id);
    }

    public RentACar findRentACarById(long id) {
        return RentACar.findById(RentACar.class, id);
    }

    public Accommodation findAccommodationById(long id) {
        return Accommodation.findById(Accommodation.class, id);
    }

    public List<Expense> getAllExpenses() {
        return Expense.listAll(Expense.class);
    }

    public List<Event> getAllUpcomingEventsByTrip(Trip trip) {
        List<Event> events = new ArrayList<>();
        events = getAllEventsByTrip(trip);
        for(int i = 0; i < events.size(); i++) {
            if(events.get(i).getStartDate().before(new Date())) {
                events.remove(i);
            }
        }
        return events;
    }

    public List<Expense> getAllExpensesByTrip(Trip trip) {
        if(trip != null) {
            List<Expense> expenses = new ArrayList<>();
            List<Expense> exps = Expense.findWithQuery(Expense.class,
                    "SELECT * FROM Expense WHERE m_trip_identifier = ? ", String.valueOf(trip.getId()));
            List<Accommodation> accs = Accommodation.findWithQuery(Accommodation.class,
                    "SELECT * FROM Accommodation WHERE m_trip_identifier = ? AND m_type = ?", String.valueOf(trip.getId()), "check-in");
            List<Transportation> transports = Transportation.findWithQuery(Transportation.class,
                    "SELECT * FROM Transportation WHERE m_trip_identifier = ? ", String.valueOf(trip.getId()));
            List<RentACar> rents = RentACar.findWithQuery(RentACar.class,
                    "SELECT * FROM Rent_A_Car WHERE m_trip_identifier = ? AND m_type = ?", String.valueOf(trip.getId()), "pick-up");
            expenses.addAll(accs);
            expenses.addAll(transports);
            expenses.addAll(rents);
            expenses.addAll(exps);
            Collections.sort(expenses, new Comparator<Expense>() {
                @Override
                public int compare(Expense exp1, Expense exp2) {
                    return exp1.getDate().compareTo(exp2.getDate());
                }
            });
            return expenses;
        } else {
            return null;
        }
    }

    public List<Event> getAllEventsByTrip(Trip trip) {
        List<Event> events = new ArrayList<>();
        List<Accommodation> accs = Accommodation.findWithQuery(Accommodation.class,
                "SELECT * FROM Accommodation WHERE m_trip_identifier = ? ", String.valueOf(trip.getId()));
        List<Transportation> transports = Transportation.findWithQuery(Transportation.class,
                "SELECT * FROM Transportation WHERE m_trip_identifier = ? ", String.valueOf(trip.getId()));
        List<RentACar> rents = RentACar.findWithQuery(RentACar.class,
                "SELECT * FROM Rent_A_Car WHERE m_trip_identifier = ? ", String.valueOf(trip.getId()));
        events.addAll(accs);
        events.addAll(transports);
        events.addAll(rents);
        Collections.sort(events, new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                return e1.getStartDate().compareTo(e2.getStartDate());
            }
        });
        return events;
    }

    public double getTotalSumExpByTrip(Trip trip) {
        double total = 0;
        if(trip != null) {
            List<Expense> exp = getAllExpensesByTrip(trip);
            for(int i = 0; i < exp.size(); i++) {
                total += exp.get(i).getPrice();
            }
        }
        return total;
    }
}
