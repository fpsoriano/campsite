package com.fabricio.campsite.service;

import static com.fabricio.campsite.helper.ReservationHelper.getDatesRange;
import static com.fabricio.campsite.helper.ReservationHelper.returnReservedDates;

import com.fabricio.campsite.exceptions.BadRequestException;
import com.fabricio.campsite.exceptions.IssueEnum;
import com.fabricio.campsite.exceptions.NotFoundException;
import com.fabricio.campsite.properties.ConfigProperties;
import com.fabricio.campsite.repository.ReservationRepository;
import com.fabricio.campsite.repository.model.reservation.ReservationModel;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

  private final ConfigProperties configProperties;
  private final ReservationRepository reservationRepository;

  @Autowired
  public ReservationService(ReservationRepository reservationRepository, ConfigProperties configProperties) {
    this.reservationRepository = reservationRepository;
    this.configProperties = configProperties;
  }

  /**
   *
   * This method will save the reservation if the business logic is correct
   *
   * @param reservationModel Reservation data
   * @return the reservation data that has been saved
   * @throws BadRequestException when the validation fail
   */
  public ReservationModel create(ReservationModel reservationModel) {
    validateReservation(reservationModel, null);
    return reservationRepository.save(reservationModel);
  }

  /**
   *
   * This method will update the reservation if the business logic is correct
   *
   * @param reservationModel Reservation data
   * @param reservationId Reservation identifier to be updated
   * @return the reservation data that has been updated
   * @throws NotFoundException if the reservation identifier does not exists in database
   * @throws BadRequestException when the validation fail
   */
  public ReservationModel update(final String reservationId, ReservationModel reservationModel) {
    Optional<ReservationModel> existingReservationModel = reservationRepository.findByIdAndCancelled(reservationId, false);
    if (!existingReservationModel.isPresent()) {
      throw NotFoundException.notFound(IssueEnum.RESERVATION_NOT_FOUND_OR_CANCELLED, reservationId);
    }
    reservationModel.setId(reservationId);
    validateReservation(reservationModel, reservationId);
    return reservationRepository.save(reservationModel);
  }

  /**
   *
   * This method will update the reservation if the business logic is correct
   *
   * @param initialDate The initial date of the range to search the available dates to reserve the campsite
   * @param finalDate The final date of the range to search the available dates to reserve the campsite
   * @return list of available dates to be reserve
   * @throws BadRequestException when the validation fail
   */
  public List<LocalDate> findAvailableReservationDatesBetweenRangeOfDates(final LocalDate initialDate, final LocalDate finalDate) {
    if (initialDate.isAfter(finalDate)) {
      throw BadRequestException.businessRule(IssueEnum.INITIAL_DATE_CANNOT_BE_AFTER_THAN_FINAL_DATE);
    }
    List<ReservationModel> reservationModels = reservationRepository.findByReservationBetweenArrivalDateAndDepartureDateAndCancelField(initialDate.minusDays(configProperties.getMaxDaysAllowedToReserve()), finalDate, false);
    List<LocalDate> reservedDates = returnReservedDates(reservationModels);
    List<LocalDate> rangeAvailableDates = getDatesRange(initialDate, finalDate);
    rangeAvailableDates.removeAll(reservedDates);
    return rangeAvailableDates;
  }

  /**
   *
   * This method will update the reservation if the business logic is correct
   *
   * @param reservationId Reservation identifier to be updated
   * @return the reservation data that has been updated
   * @throws NotFoundException if the reservation identifier does not exists in database
   */
  public ReservationModel findReservationById(String reservationId) {
    Optional<ReservationModel> reservationModel = reservationRepository.findById(reservationId);
    if (!reservationModel.isPresent()) {
      throw NotFoundException.notFound(IssueEnum.RESERVATION_NOT_FOUND, reservationId);
    }
    return reservationModel.get();
  }

  /**
   *
   * Cancel the reservation
   *
   * @param reservationId Reservation identifier to be updated
   * @throws NotFoundException if the reservation identifier does not exists in database
   */
  public ReservationModel cancelReservation(final String reservationId) {
    ReservationModel reservationModel = findReservationById(reservationId);
    reservationModel.setCancelled(true);
    reservationRepository.save(reservationModel);
    return reservationModel;
  }

  private void validateReservation(final ReservationModel reservationModel, final String reservationIdToBeUpdate) {

    if (reservationModel.getArrivalDate().isAfter(reservationModel.getDepartureDate())) {
      throw BadRequestException.businessRule(IssueEnum.ARRIVALDATE_CANNOT_BE_AFTER_THAN_DEPARTUREDATE);
    }

    List<LocalDate> rangeDatesToBeReserved = getDatesRange(reservationModel.getArrivalDate(), reservationModel.getDepartureDate());
    if (rangeDatesToBeReserved.size() > configProperties.getMaxDaysAllowedToReserve()) {
      throw BadRequestException.businessRule(IssueEnum.NOT_ALLOWED_RESERVE_MORE_THAN_3DAYS);
    }

    LocalDate requestDate = LocalDate.now();
    if (reservationModel.getArrivalDate().isBefore(requestDate)) {
      throw BadRequestException.businessRule(IssueEnum.ARRIVALDATE_SHOULD_BE_AFTER_THAN_CURRENTDATE);
    }

    List<LocalDate> rangeDatesFromRequestUntilArrivalDate = getDatesRange(requestDate, reservationModel.getArrivalDate());
    if (reservationModel.getArrivalDate().isEqual(requestDate) || rangeDatesFromRequestUntilArrivalDate.size() > configProperties.getDaysInAdvanceAllowedToReserve()) {
      throw BadRequestException.businessRule(IssueEnum.CAMPSITE_RESERVED_MINUMUM_MAX);
    }

    List<ReservationModel> reservationModels = reservationRepository.findByReservationBetweenArrivalDateAndDepartureDateAndCancelField(reservationModel.getArrivalDate().minusDays(configProperties.getMaxDaysAllowedToReserve()), reservationModel.getDepartureDate().plusDays(configProperties.getMaxDaysAllowedToReserve()), false);

    if (reservationIdToBeUpdate != null) {
      reservationModels.removeIf(reservationModel1 -> reservationModel1.getId().equals(reservationIdToBeUpdate));
    }

    List<LocalDate> reservedDates = returnReservedDates(reservationModels);
    if (rangeDatesToBeReserved.stream().anyMatch(dateToBeReserved -> reservedDates.contains(dateToBeReserved))) {
      throw BadRequestException.businessRule(IssueEnum.DATE_NOT_AVAILABLE);
    }

  }

}