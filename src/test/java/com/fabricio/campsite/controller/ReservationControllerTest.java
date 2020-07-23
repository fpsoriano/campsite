package com.fabricio.campsite.controller;

import static com.fabricio.campsite.helpers.ReservationHelper.RESERVATION_ID;
import static com.fabricio.campsite.helpers.ReservationHelper.mockReservationModel;
import static com.fabricio.campsite.helpers.ReservationHelper.mockReservationVo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fabricio.campsite.repository.model.reservation.ReservationModel;
import com.fabricio.campsite.service.ReservationService;
import com.fabricio.campsite.vo.reservation.ReservationVo;
import java.time.LocalDate;
import java.util.List;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ReservationControllerTest {

  @InjectMocks
  private ReservationController reservationController;

  @Mock
  private ReservationService reservationService;

  @Test
  public void reserveCampsite() {
    ReservationVo reservationVo = mockReservationVo();
    when(reservationService.create(any(ReservationModel.class))).thenReturn(mockReservationModel());
    ResponseEntity<ReservationVo> reservationVoResponseEntity = reservationController.reserve(reservationVo);
    verify(reservationService, times(1)).create(any(ReservationModel.class));
    Assert.assertEquals(HttpStatus.CREATED, reservationVoResponseEntity.getStatusCode());
    assertReservationFields(reservationVo, reservationVoResponseEntity.getBody());
  }

  @Test
  public void updateReservation() {
    ReservationVo reservationVo = mockReservationVo();
    when(reservationService.update(any(String.class), any(ReservationModel.class))).thenReturn(mockReservationModel());
    ResponseEntity<ReservationVo> reservationVoResponseEntity = reservationController.updateReservation(reservationVo, reservationVo.getId());
    verify(reservationService, times(1)).update(any(String.class), any(ReservationModel.class));
    Assert.assertEquals(HttpStatus.OK, reservationVoResponseEntity.getStatusCode());
    assertReservationFields(reservationVo, reservationVoResponseEntity.getBody());
  }

  @Test
  public void gerReservationById() {
    ReservationVo reservationVo = mockReservationVo();
    when(reservationService.findReservationById(any(String.class))).thenReturn(mockReservationModel());
    ResponseEntity<ReservationVo> reservationVoResponseEntity = reservationController.getReservationById(reservationVo.getId());
    verify(reservationService, times(1)).findReservationById(reservationVo.getId());
    Assert.assertEquals(HttpStatus.OK, reservationVoResponseEntity.getStatusCode());
    assertReservationFields(reservationVo, reservationVoResponseEntity.getBody());
  }

  @Test
  public void cancelReservation() {
    ResponseEntity responseEntity = reservationController.cancelReservation(RESERVATION_ID);
    verify(reservationService, times(1)).cancelReservation(RESERVATION_ID);
    Assert.assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
  }

  @Test
  public void findReservationWithAvailableDates() {
    LocalDate startDate = LocalDate.now();
    LocalDate endDate = LocalDate.now().plusDays(3);
    List<LocalDate> availableDatesForReservation = List.of(startDate, endDate);
    when(reservationService.findAvailableReservationDatesBetweenRangeOfDates(any(LocalDate.class), any(LocalDate.class))).thenReturn(availableDatesForReservation);
    ResponseEntity responseEntity = reservationController.findReservationWithAvailableDates(startDate, endDate);

    verify(reservationService, times(1)).findAvailableReservationDatesBetweenRangeOfDates(any(LocalDate.class), any(LocalDate.class));
    Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    List<LocalDate> response = (List<LocalDate>) responseEntity.getBody();
    Assert.assertTrue(response.containsAll(availableDatesForReservation));
  }

  public void assertReservationFields(ReservationVo reservationVoExpected, ReservationVo reservationVoResponse){
    Assert.assertEquals(reservationVoExpected.getId(), reservationVoResponse.getId());
    Assert.assertEquals(reservationVoExpected.getArrivalDate(), reservationVoResponse.getArrivalDate());
    Assert.assertEquals(reservationVoExpected.getDepartureDate(), reservationVoResponse.getDepartureDate());
    Assert.assertEquals(reservationVoExpected.isCancelled(), reservationVoResponse.isCancelled());
    Assert.assertEquals(reservationVoExpected.getUser().getFullName(), reservationVoResponse.getUser().getFullName());
    Assert.assertEquals(reservationVoExpected.getUser().getEmail(), reservationVoResponse.getUser().getEmail());
  }


}