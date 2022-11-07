package com.fabricio.campsite.exceptions;

public class ConfigurationException extends GlobalException {

  private static final long serialVersionUID = 1L;

  private ConfigurationException(final Issue issue) {
    super(issue);
  }

  public static ConfigurationException error(final IssueEnum issueEnum, final Object... args) {
    return new ConfigurationException(new Issue(issueEnum, args));
  }
}
