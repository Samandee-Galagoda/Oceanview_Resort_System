package com.oceanview.resort.util;

import com.oceanview.resort.dao.BillingDao;
import com.oceanview.resort.dao.GuestDao;
import com.oceanview.resort.dao.ReservationDao;
import com.oceanview.resort.dao.ReservationDetailDao;
import com.oceanview.resort.dao.RoomTypeDao;
import com.oceanview.resort.dao.UserDao;
import com.oceanview.resort.repository.JdbcBillingRepository;
import com.oceanview.resort.repository.JdbcGuestRepository;
import com.oceanview.resort.repository.JdbcReservationDetailRepository;
import com.oceanview.resort.repository.JdbcReservationRepository;
import com.oceanview.resort.repository.JdbcRoomTypeRepository;
import com.oceanview.resort.repository.JdbcUserRepository;

public class DaoFactory {
    private static final DaoFactory INSTANCE = new DaoFactory();

    private final ReservationDao reservationDao = new JdbcReservationRepository();
    private final UserDao userDao = new JdbcUserRepository();
    private final GuestDao guestDao = new JdbcGuestRepository();
    private final RoomTypeDao roomTypeDao = new JdbcRoomTypeRepository();
    private final ReservationDetailDao reservationDetailDao = new JdbcReservationDetailRepository();
    private final BillingDao billingDao = new JdbcBillingRepository();

    private DaoFactory() {
    }

    public static DaoFactory getInstance() {
        return INSTANCE;
    }

    public ReservationDao getReservationDao() {
        return reservationDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public GuestDao getGuestDao() {
        return guestDao;
    }

    public RoomTypeDao getRoomTypeDao() {
        return roomTypeDao;
    }

    public ReservationDetailDao getReservationDetailDao() {
        return reservationDetailDao;
    }

    public BillingDao getBillingDao() {
        return billingDao;
    }
}
