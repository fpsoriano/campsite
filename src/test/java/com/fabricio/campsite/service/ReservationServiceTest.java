package com.fabricio.campsite.service;

import static com.fabricio.campsite.helpers.ReservationHelper.mockReservationModel;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fabricio.campsite.exceptions.BadRequestException;
import com.fabricio.campsite.exceptions.Issue;
import com.fabricio.campsite.exceptions.IssueEnum;
import com.fabricio.campsite.exceptions.NotFoundException;
import com.fabricio.campsite.properties.ConfigProperties;
import com.fabricio.campsite.repository.ReservationRepository;
import com.fabricio.campsite.repository.model.reservation.ReservationModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

  public static final String RESERVATION_ID = "reservationId";

  @InjectMocks
  private ReservationService reservationService;

  @Mock
  private ReservationRepository reservationRepository;

  @Mock
  private ConfigProperties configProperties;

  @Test
  public void createReservation_Success() {
    ReservationModel reservationModel = mockReservationModel();
    reservationModel.setArrivalDate(reservationModel.getArrivalDate().plusDays(3));
    reservationModel.setDepartureDate(reservationModel.getDepartureDate().plusDays(2));
    when(reservationRepository.findByReservationBetweenArrivalDateAndDepartureDateAndCancelField(any(LocalDate.class), any(LocalDate.class), any(boolean.class))).thenReturn(
        new ArrayList<>(Arrays.asList(reservationModel)));
    when(configProperties.getMaxDaysAllowedToReserve()).thenReturn(3);
    when(configProperties.getDaysInAdvanceAllowedToReserve()).thenReturn(30);
    ReservationModel reservationModelToBeCreated= mockReservationModel();
    when(reservationRepository.save(any(ReservationModel.class))).thenReturn(reservationModelToBeCreated);

    ReservationModel reservationModelResponse = reservationService.create(reservationModelToBeCreated);
    verify(reservationRepository, times(1)).save(any());
    assertReservationFields(reservationModelToBeCreated, reservationModelResponse);
  }

  @Test
  public void createReservation_ArrivalDateAfterDepartureDate_BadRequestException() {
    ReservationModel reservationModel = mockReservationModel();
    reservationModel.setArrivalDate(reservationModel.getArrivalDate().plusDays(6));
    BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> {
      reservationService.create(reservationModel);
    });
    Issue issueEnum = new Issue(IssueEnum.ARRIVALDATE_CANNOT_BE_AFTER_THAN_DEPARTUREDATE);
    assertEquals(issueEnum.getMessage(),
        badRequestException.getIssue().getMessage());
  }

  @Test
  public void reserveMoreThan3Days_BadRequestException() {
    ReservationModel reservationModel = mockReservationModel();
    reservationModel.setDepartureDate(reservationModel.getDepartureDate().plusDays(2));
    when(configProperties.getMaxDaysAllowedToReserve()).thenReturn(3);
    BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> {
      reservationService.create(reservationModel);
    });
    Issue issueEnum = new Issue(IssueEnum.NOT_ALLOWED_RESERVE_MORE_THAN_3DAYS);
    assertEquals(issueEnum.getMessage(),
        badRequestException.getIssue().getMessage());
  }

  @Test
  public void createReservation_withArrivalDateBeforeThanTheCurrentDate_BadRequestException() {
    ReservationModel reservationModel = mockReservationModel();
    reservationModel.setArrivalDate(reservationModel.getArrivalDate().minusDays(6));
    reservationModel.setDepartureDate(reservationModel.getDepartureDate().minusDays(6));
    when(configProperties.getMaxDaysAllowedToReserve()).thenReturn(3);
    BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> {
      reservationService.create(reservationModel);
    });
    Issue issueEnum = new Issue(IssueEnum.ARRIVALDATE_SHOULD_BE_AFTER_THAN_CURRENTDATE);
    assertEquals(issueEnum.getMessage(),
        badRequestException.getIssue().getMessage());
  }

  @Test
  public void tryToMakeReservationInTheArrivalDate_BadRequestException() {
    ReservationModel reservationModel = mockReservationModel();
    reservationModel.setArrivalDate(reservationModel.getArrivalDate().minusDays(1));
    reservationModel.setDepartureDate(reservationModel.getDepartureDate().minusDays(1));
    when(configProperties.getMaxDaysAllowedToReserve()).thenReturn(3);
    BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> {
      reservationService.create(reservationModel);
    });
    Issue issueEnum = new Issue(IssueEnum.CAMPSITE_RESERVED_MINUMUM_MAX);
    assertEquals(issueEnum.getMessage(),
        badRequestException.getIssue().getMessage());
  }

  @Test
  public void tryToMakeReservationWithMoreThanOn1MonthInAdvance_BadRequestException() {
    ReservationModel reservationModel = mockReservationModel();
    reservationModel.setArrivalDate(reservationModel.getArrivalDate().plusDays(35));
    reservationModel.setDepartureDate(reservationModel.getDepartureDate().plusDays(35));
    when(configProperties.getMaxDaysAllowedToReserve()).thenReturn(3);
    when(configProperties.getDaysInAdvanceAllowedToReserve()).thenReturn(30);
    BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> {
      reservationService.create(reservationModel);
    });
    Issue issueEnum = new Issue(IssueEnum.CAMPSITE_RESERVED_MINUMUM_MAX);
    assertEquals(issueEnum.getMessage(),
        badRequestException.getIssue().getMessage());
  }

  @Test
  public void tryToMakeReservationInDateAlreadyReserved_BadRequestException() {
    ReservationModel reservationModel = mockReservationModel();
    reservationModel.setArrivalDate(reservationModel.getArrivalDate().plusDays(2));
    reservationModel.setDepartureDate(reservationModel.getDepartureDate().plusDays(2));
    when(reservationRepository.findByReservationBetweenArrivalDateAndDepartureDateAndCancelField(any(LocalDate.class), any(LocalDate.class), any(boolean.class))).thenReturn(
        new ArrayList<>(Arrays.asList(reservationModel)));
    when(configProperties.getMaxDaysAllowedToReserve()).thenReturn(3);
    when(configProperties.getDaysInAdvanceAllowedToReserve()).thenReturn(30);

    ReservationModel reservationModelToBeCreated = mockReservationModel();
    BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> {
      reservationService.create(reservationModelToBeCreated);
    });
    Issue issueEnum = new Issue(IssueEnum.DATE_NOT_AVAILABLE);
    assertEquals(issueEnum.getMessage(),
        badRequestException.getIssue().getMessage());
  }

  @Test
  public void updateReservation_Success() {
    ReservationModel reservationModel = mockReservationModel();
    reservationModel.setArrivalDate(reservationModel.getArrivalDate().plusDays(3));
    reservationModel.setDepartureDate(reservationModel.getDepartureDate().plusDays(2));
    when(reservationRepository.findByReservationBetweenArrivalDateAndDepartureDateAndCancelField(any(LocalDate.class), any(LocalDate.class), any(boolean.class))).thenReturn(
        new ArrayList<>(Arrays.asList(reservationModel)));
    when(reservationRepository.findByIdAndCancelled(any(String.class), any(boolean.class))).thenReturn(Optional.ofNullable(mockReservationModel()));
    when(configProperties.getMaxDaysAllowedToReserve()).thenReturn(3);
    when(configProperties.getDaysInAdvanceAllowedToReserve()).thenReturn(30);
    ReservationModel reservationModelToBeUpdated = mockReservationModel();
    reservationModelToBeUpdated.setArrivalDate(reservationModelToBeUpdated.getArrivalDate().plusDays(1));
    when(reservationRepository.save(any(ReservationModel.class))).thenReturn(reservationModelToBeUpdated);

    ReservationModel reservationModelResponse = reservationService.update(RESERVATION_ID, reservationModelToBeUpdated);
    verify(reservationRepository, times(1)).save(any());
    assertReservationFields(reservationModelToBeUpdated, reservationModelResponse);
  }

  @Test
  public void updateNonexistentReservation_NotFoundException() {
    when(reservationRepository.findByIdAndCancelled(any(String.class), any(boolean.class))).thenReturn(Optional.ofNullable(null));
    NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> {
      reservationService.update(RESERVATION_ID, mockReservationModel());
    });
    Issue issueEnum = new Issue(IssueEnum.RESERVATION_NOT_FOUND_OR_CANCELLED, RESERVATION_ID);
    assertEquals(issueEnum.getMessage(),
        notFoundException.getIssue().getMessage());
  }

  @Test
  public void findAvailableReservationDatesBetweenRangeOfDates() {
    ReservationModel reservationModel = mockReservationModel();
    reservationModel.setArrivalDate(LocalDate.of(2020,7,21));
    reservationModel.setDepartureDate(LocalDate.of(2020,7,23));
    when(configProperties.getMaxDaysAllowedToReserve()).thenReturn(3);
    when(reservationRepository.findByReservationBetweenArrivalDateAndDepartureDateAndCancelField(any(LocalDate.class), any(LocalDate.class), any(boolean.class))).thenReturn(
        Arrays.asList(reservationModel));
    List<LocalDate> availableReservationDates = reservationService.findAvailableReservationDatesBetweenRangeOfDates(LocalDate.of(2020,7,20), LocalDate.of(2020,7,25));
    List<LocalDate> expectedAvailableDates = Arrays.asList(LocalDate.of(2020,7,20), LocalDate.of(2020,7,24), LocalDate.of(2020,7,25));
    Assert.assertTrue(expectedAvailableDates.containsAll(availableReservationDates));
  }

  @Test
  public void findAvailableReservationDatesBetweenRangeOfDates_InitialDateAfterFinalDate_BadRequestException() {
    BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> {
      reservationService.findAvailableReservationDatesBetweenRangeOfDates(LocalDate.of(2020,7,25), LocalDate.of(2020,7,24));
    });
    Issue issueEnum = new Issue(IssueEnum.INITIAL_DATE_CANNOT_BE_AFTER_THAN_FINAL_DATE);
    assertEquals(issueEnum.getMessage(),
        badRequestException.getIssue().getMessage());
  }

  @Test
  public void findReservationById() {
    ReservationModel reservationModelExpected = mockReservationModel();
    when(reservationRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(
        mockReservationModel()));
    ReservationModel reservationModel = reservationService.findReservationById(RESERVATION_ID);
    assertReservationFields(reservationModelExpected, reservationModel);
  }

  @Test
  public void findReservationById_NotFound() {
    when(reservationRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(null));
    NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> {
      reservationService.findReservationById(RESERVATION_ID);
    });
    Issue issueEnum = new Issue(IssueEnum.RESERVATION_NOT_FOUND, RESERVATION_ID);
    assertEquals(issueEnum.getMessage(),
        notFoundException.getIssue().getMessage());
  }

  @Test
  public void cancelReservationById() {
    ReservationModel reservationModel = mockReservationModel();
    when(reservationRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(
        reservationModel));
    ReservationModel reservationModelCancelled = reservationService.cancelReservation(RESERVATION_ID);
    verify(reservationRepository, times(1)).save(reservationModel);
    Assert.assertTrue(reservationModelCancelled.isCancelled());
  }

  @Test
  public void cancelNonexistentReservationById_NotFound() {
    when(reservationRepository.findById(any(String.class))).thenReturn(Optional.ofNullable(null));
    NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> {
      reservationService.cancelReservation(RESERVATION_ID);
    });
    Issue issueEnum = new Issue(IssueEnum.RESERVATION_NOT_FOUND, RESERVATION_ID);
    assertEquals(issueEnum.getMessage(),
        notFoundException.getIssue().getMessage());
  }

  public void assertReservationFields(ReservationModel reservationModelExpected, ReservationModel reservationModelResponse){
    Assert.assertEquals(reservationModelExpected.getId(), reservationModelResponse.getId());
    Assert.assertEquals(reservationModelExpected.getArrivalDate(), reservationModelResponse.getArrivalDate());
    Assert.assertEquals(reservationModelExpected.getDepartureDate(), reservationModelResponse.getDepartureDate());
    Assert.assertEquals(reservationModelExpected.isCancelled(), reservationModelResponse.isCancelled());
    Assert.assertEquals(reservationModelExpected.getUser().getFullName(), reservationModelResponse.getUser().getFullName());
    Assert.assertEquals(reservationModelExpected.getUser().getEmail(), reservationModelResponse.getUser().getEmail());
  }

}