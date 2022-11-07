package com.fabricio.campsite.repository.model.reservation;

import com.fabricio.campsite.dto.reservation.ReservationDto;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationModel {

  @Id
  private String id;
  private UserModel user;
  private LocalDate arrivalDate;
  private LocalDate departureDate;
  private boolean cancelled;

  public ReservationDto toReservationDto() {
    return ReservationDto.builder()
        .id(this.id)
        .arrivalDate(this.arrivalDate)
        .departureDate(this.departureDate)
        .user(this.getUser().toUserDto())
        .cancelled(this.cancelled)
        .build();
  }

}
