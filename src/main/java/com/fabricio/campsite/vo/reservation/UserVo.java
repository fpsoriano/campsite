package com.fabricio.campsite.vo.reservation;

import static com.fabricio.campsite.helper.ValidationConstants.EMAIL_IS_REQUIRED;
import static com.fabricio.campsite.helper.ValidationConstants.NAME_IS_REQUIRED;

import com.fabricio.campsite.repository.model.reservation.UserModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {

  @ApiModelProperty(example = "Fabricio Soriano Palhavam", notes = "Full name of the responsible to reserve the campsite")
  @NotNull(message = NAME_IS_REQUIRED)
  private String fullName;


  @ApiModelProperty(example = "fabricio@gmail.com", notes = "Email of the responsible to reserve the campsite")
  @NotNull(message = EMAIL_IS_REQUIRED)
  private String email;

  public UserModel toUserModel() {
    return UserModel.builder()
        .fullName(this.fullName)
        .email(this.email)
        .build();
  }

}
