package com.fabricio.campsite.exceptions;

import java.util.IllegalFormatException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum IssueEnum {
  ARRIVALDATE_CANNOT_BE_AFTER_THAN_DEPARTUREDATE(1, "Arrival date cannot be after than departure date"),
  RESERVATION_NOT_FOUND(2, "Reservation %s not found."),
  RESERVATION_NOT_FOUND_OR_CANCELLED(2, "Reservation %s not found or cancelled cannot be updated."),
  NOT_ALLOWED_RESERVE_MORE_THAN_3DAYS(3, "It is not allowed to reserve more than 3 days"),
  CAMPSITE_RESERVED_MINUMUM_MAX(4, "The campsite can be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance."),
  ARRIVALDATE_SHOULD_BE_AFTER_THAN_CURRENTDATE(5, "The arrival date should be after of the current date."),
  DATE_NOT_AVAILABLE(6, "There is a reservation between the arrivalDate and departureDate. Please choose other day(s)."),
  INITIAL_DATE_CANNOT_BE_AFTER_THAN_FINAL_DATE(7, "Initial date cannot be after than Final date"),

  MALFORMED_REQUEST(1, "Malformed Request"),
  INTERNAL_SERVER_ERROR(1, "Internal Server Error");


  private final int code;
  private final String message;

  IssueEnum(final int code, final String message) {

    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public String getFormattedMessage(final Object... args) {

    if (message == null) {
      return "";
    }

    try {
      return String.format(message, args);
    } catch (final IllegalFormatException e) {
      log.error(e.getMessage(), e);
      return message.replace("%s", "");
    }
  }

}
