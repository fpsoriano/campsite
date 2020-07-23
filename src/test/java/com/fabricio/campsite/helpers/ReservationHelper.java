package com.fabricio.campsite.helpers;

import com.fabricio.campsite.repository.model.reservation.ReservationModel;
import com.fabricio.campsite.repository.model.reservation.UserModel;
import com.fabricio.campsite.vo.reservation.ReservationVo;
import com.fabricio.campsite.vo.reservation.UserVo;
import java.time.LocalDate;

public class ReservationHelper {

  public static String RESERVATION_ID = "reservationId";
  public static String NAME = "Fabricio Soriano";
  public static String EMAIL = "fabricio@gmail.com";

  public static ReservationVo mockReservationVo() {
    ReservationVo reservationVo = new ReservationVo();
    reservationVo.setId(RESERVATION_ID);
    reservationVo.setCancelled(false);
    reservationVo.setArrivalDate(LocalDate.now().plusDays(1));
    reservationVo.setDepartureDate(LocalDate.now().plusDays(3));
    UserVo userVo = new UserVo(NAME, EMAIL);
    reservationVo.setUser(userVo);
    return reservationVo;
  }

  public static ReservationModel mockReservationModel() {
    ReservationModel reservationModel = new ReservationModel();
    reservationModel.setId(RESERVATION_ID);
    reservationModel.setCancelled(false);
    reservationModel.setArrivalDate(LocalDate.now().plusDays(1));
    reservationModel.setDepartureDate(LocalDate.now().plusDays(3));
    UserModel userModel = new UserModel(NAME, EMAIL);
    reservationModel.setUser(userModel);
    return reservationModel;
  }

}
