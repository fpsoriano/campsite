package com.fabricio.campsite.helper;

import com.fabricio.campsite.repository.model.reservation.ReservationModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationHelper {

  public static List<LocalDate> getDatesRange (
      LocalDate startDate, LocalDate endDate) {

    return startDate.datesUntil(endDate.plusDays(1))
        .collect(Collectors.toList());
  }

  public static List<LocalDate> returnReservedDates(List<ReservationModel> reservationModelList) {
    List<LocalDate> reservedDates = new ArrayList<>();
    reservationModelList.forEach(reservationModel -> {
      reservedDates.addAll(getDatesRange(reservationModel.getArrivalDate(), reservationModel.getDepartureDate()));
    });
    return reservedDates;
  }

}
