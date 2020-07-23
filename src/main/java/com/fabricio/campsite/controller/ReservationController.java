package com.fabricio.campsite.controller;

import com.fabricio.campsite.service.ReservationService;
import com.fabricio.campsite.vo.reservation.ReservationVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.time.LocalDate;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Api(tags = {"Reservation"})
public class ReservationController {

  private final ReservationService reservationService;

  @Autowired
  public ReservationController(ReservationService reservationService) {this.reservationService = reservationService;}

  @ApiOperation(
      value = "Reserve campsite",
      notes = "Reserve campsite in the application")
  @PostMapping("/reservation")
  public ResponseEntity reserve(@ApiParam(value = "Reservation object", required = true) @Valid @RequestBody final ReservationVo reservationVo) {
    log.info("Request : {}", reservationVo);
    ReservationVo reservationVoResponse = reservationService.create(reservationVo.toReservationModel()).toReservationVo();
    return new ResponseEntity<>(reservationVoResponse, HttpStatus.CREATED);
  }

  @ApiOperation(
      value = "Retrieves reservation by id",
      notes = "Retrieves reservation by id to check all the information")
  @GetMapping("/reservation/{reservationId}")
  public ResponseEntity getReservationById(@ApiParam(value = "Reservation id", required = true) @PathVariable final String reservationId) {
    ReservationVo reservationVoResponse = reservationService.findReservationById(reservationId).toReservationVo();
    return new ResponseEntity<>(reservationVoResponse, HttpStatus.OK);
  }

  @ApiOperation(
      value = "Retrieves reservation by id",
      notes = "Retrieves reservation by id to check all the information")
  @DeleteMapping("/reservation/{reservationId}/cancel")
  public ResponseEntity cancelReservation(@ApiParam(value = "Reservation id", required = true) @PathVariable final String reservationId) {
    reservationService.cancelReservation(reservationId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @ApiOperation(
      value = "Update reservation by id",
      notes = "Update the reservation data by id")
  @PutMapping("/reservation/{reservationId}")
  public ResponseEntity updateReservation(
      @ApiParam(value = "Reservation object", required = true) @Valid @RequestBody final ReservationVo reservationVo,
      @ApiParam(value = "Reservation id", required = true) @PathVariable final String reservationId) {
    ReservationVo reservationVoResponse = reservationService.update(reservationId, reservationVo.toReservationModel()).toReservationVo();
    return new ResponseEntity<>(reservationVoResponse, HttpStatus.OK);
  }

  @ApiOperation(
      value = "All reservation",
      notes = "Retrieves reservations")
  @GetMapping("/reservation/available")
  public ResponseEntity findReservationWithAvailableDates(
      @ApiParam(value = "Initial Date of the range. If not set, the default range will be 1 month (considering the initialDate as the current one).")
        @RequestParam(required = false)
        @DateTimeFormat(iso = ISO.DATE)
        LocalDate initialDate,
      @ApiParam(value = "Final Date of the range. If not set, the default range will be 1 month.")
        @RequestParam(required = false)
        @DateTimeFormat(iso = ISO.DATE)
        LocalDate finalDate
  ) {
    initialDate = initialDate != null ? initialDate : LocalDate.now().plusDays(1);
    finalDate = finalDate != null ? finalDate : initialDate.plusDays(30);
    List<LocalDate> availableDatesForReservation = reservationService.findAvailableReservationDatesBetweenRangeOfDates(initialDate, finalDate);
    return new ResponseEntity<>(availableDatesForReservation, HttpStatus.OK);
  }
}
