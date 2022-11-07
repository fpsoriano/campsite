package com.fabricio.campsite.helpers;

import com.fabricio.campsite.repository.model.reservation.ReservationModel;
import com.fabricio.campsite.repository.model.reservation.UserModel;
import com.fabricio.campsite.dto.reservation.ReservationDto;
import com.fabricio.campsite.dto.reservation.UserDto;
import java.time.LocalDate;

public class ReservationHelper {

  public static String RESERVATION_ID = "reservationId";
  public static String NAME = "Fabricio Soriano";
  public static String EMAIL = "fabricio@gmail.com";

  public static ReservationDto mockReservationVo() {
    ReservationDto reservationDto = new ReservationDto();
    reservationDto.setId(RESERVATION_ID);
    reservationDto.setCancelled(false);
    reservationDto.setArrivalDate(LocalDate.now().plusDays(1));
    reservationDto.setDepartureDate(LocalDate.now().plusDays(3));
    UserDto userDto = new UserDto(NAME, EMAIL);
    reservationDto.setUser(userDto);
    return reservationDto;
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
