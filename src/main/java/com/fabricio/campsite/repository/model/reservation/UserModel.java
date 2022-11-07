package com.fabricio.campsite.repository.model.reservation;

import com.fabricio.campsite.dto.reservation.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {

  private String fullName;
  private String email;

  public UserDto toUserDto() {
    return UserDto.builder()
        .fullName(this.fullName)
        .email(this.email)
        .build();
  }

}
