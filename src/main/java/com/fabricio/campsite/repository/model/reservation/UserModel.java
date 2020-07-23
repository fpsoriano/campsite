package com.fabricio.campsite.repository.model.reservation;

import com.fabricio.campsite.vo.reservation.UserVo;
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

  public UserVo toUserVo() {
    return UserVo.builder()
        .fullName(this.fullName)
        .email(this.email)
        .build();
  }

}
