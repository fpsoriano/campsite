package com.fabricio.campsite.vo.reservation;

import static com.fabricio.campsite.helper.ValidationConstants.ARRIVAL_DATE_IS_REQUIRED;
import static com.fabricio.campsite.helper.ValidationConstants.DEPARTURE_DATE_IS_REQUIRED;

import com.fabricio.campsite.repository.model.reservation.ReservationModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationVo {

  @Id
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @ApiModelProperty(notes = "Reservation ID generate by the application. It will be possible edit/cancel the reservation using this ID.")
  private String id;

  @Valid
  private UserVo user;

  @ApiModelProperty(example = "2020-07-16", notes = "Intended arrival date")
  @NotNull(message = ARRIVAL_DATE_IS_REQUIRED)
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate arrivalDate;

  @ApiModelProperty(example = "2020-07-18", notes = "Departure date")
  @NotNull(message = DEPARTURE_DATE_IS_REQUIRED)
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate departureDate;

  @ApiModelProperty(notes = "Indicate if the reservation was cancelled or not. There is a specific endpoint to cancel the reservation.")
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
