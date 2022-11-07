package com.fabricio.campsite.dto.reservation;

import static com.fabricio.campsite.helper.ValidationConstants.ARRIVAL_DATE_IS_REQUIRED;
import static com.fabricio.campsite.helper.ValidationConstants.DEPARTURE_DATE_IS_REQUIRED;

import com.fabricio.campsite.repository.model.reservation.ReservationModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {

  @Id
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @Schema(description = "Reservation ID generate by the application. It will be possible edit/cancel the reservation using this ID.")
  private String id;

  @Valid
  private UserDto user;

  @Schema(example = "2020-07-16", description = "Intended arrival date")
  @NotNull(message = ARRIVAL_DATE_IS_REQUIRED)
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate arrivalDate;

  @Schema(example = "2020-07-18", description = "Departure date")
  @NotNull(message = DEPARTURE_DATE_IS_REQUIRED)
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate departureDate;

  @Schema(description = "Indicate if the reservation was cancelled or not. There is a specific endpoint to cancel the reservation.")
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private boolean cancelled;

  public ReservationModel toReservationModel() {
    return ReservationModel.builder()
        .arrivalDate(this.arrivalDate)
        .departureDate(this.departureDate)
        .user(this.getUser().toUserModel())
        .build();
  }

}
