package com.fabricio.campsite.controller;

import com.fabricio.campsite.service.ReservationService;
import com.fabricio.campsite.dto.reservation.ReservationDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import java.time.LocalDate;
import java.util.List;
import javax.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Manage reservations",
     description = "APIs to manage parking reservations.")
public class ReservationController {

  private final ReservationService reservationService;

  @Autowired
  public ReservationController(ReservationService reservationService) {this.reservationService = reservationService;}

  @Operation(summary = "Reserve campsite", description = "Reserve campsite in the application")
  @PostMapping("/reservation")
  public ResponseEntity reserve(@ApiParam(value = "Reservation object", required = true) @Valid @RequestBody final ReservationDto reservationDto) {
    log.info("Request : {}", reservationDto);
    ReservationDto reservationDtoResponse = reservationService.create(reservationDto.toReservationModel()).toReservationDto();
    return new ResponseEntity<>(reservationDtoResponse, HttpStatus.CREATED);
  }

  @Operation(summary = "Retrieves reservation by id", description = "Retrieves reservation by id to check all the information")
  @GetMapping("/reservation/{reservationId}")
  public ResponseEntity getReservationById(@ApiParam(value = "Reservation id", required = true) @PathVariable final String reservationId) {
    ReservationDto reservationDtoResponse = reservationService.findReservationById(reservationId).toReservationDto();
    return new ResponseEntity<>(reservationDtoResponse, HttpStatus.OK);
  }

  @Operation(summary = "Delete reservation by id")
  @DeleteMapping("/reservation/{reservationId}/cancel")
  public ResponseEntity cancelReservation(@ApiParam(value = "Reservation id", required = true) @PathVariable final String reservationId) {
    reservationService.cancelReservation(reservationId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Operation(summary = "Update reservation by id", description = "Update the reservation data by id")
  @PutMapping("/reservation/{reservationId}")
  public ResponseEntity updateReservation(
      @Parameter(description = "Reservation object", required = true) @Valid @RequestBody final ReservationDto reservationDto,
      @Parameter(description = "Reservation id", required = true) @PathVariable final String reservationId) {
    ReservationDto reservationDtoResponse = reservationService.update(reservationId, reservationDto.toReservationModel()).toReservationDto();
    return new ResponseEntity<>(reservationDtoResponse, HttpStatus.OK);
  }

  @Operation(summary = "Retrieves available reservations")
  @GetMapping("/reservation/available")
  public ResponseEntity findReservationWithAvailableDates(
      @Parameter(description = "Initial Date of the range. If not set, the default range will be 1 month (considering the initialDate as the current one).")
        @RequestParam(required = false)
        @DateTimeFormat(iso = ISO.DATE)
        LocalDate initialDate,
      @Parameter(description = "Final Date of the range. If not set, the default range will be 1 month.")
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
