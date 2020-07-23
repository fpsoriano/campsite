package com.fabricio.campsite.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "config")
public class ConfigProperties {

  private int maxDaysAllowedToReserve;
  private int daysInAdvanceAllowedToReserve;

  public int getMaxDaysAllowedToReserve() {
    return maxDaysAllowedToReserve;
  }

  public void setMaxDaysAllowedToReserve(int maxDaysAllowedToReserve) {
    this.maxDaysAllowedToReserve = maxDaysAllowedToReserve;
  }

  public int getDaysInAdvanceAllowedToReserve() {
    return daysInAdvanceAllowedToReserve;
  }

  public void setDaysInAdvanceAllowedToReserve(int daysInAdvanceAllowedToReserve) {
    this.daysInAdvanceAllowedToReserve = daysInAdvanceAllowedToReserve;
  }
}
