package com.fabricio.campsite.repository;

import com.fabricio.campsite.repository.model.reservation.ReservationModel;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Component;

@Component
public interface ReservationRepository extends MongoRepository<ReservationModel,String> {

  @Query("{arrivalDate : {$gte : ?0}, departureDate : {$lte : ?1}, cancelled : ?2}")
  List<ReservationModel> findByReservationBetweenArrivalDateAndDepartureDateAndCancelField(
      LocalDate arrivalDate, LocalDate departureDate, boolean cancelled);

  Optional<ReservationModel> findByIdAndCancelled(String reservationId, boolean cancelled);
}


