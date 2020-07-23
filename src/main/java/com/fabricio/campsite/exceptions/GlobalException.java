package com.fabricio.campsite.exceptions;

public class GlobalException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final Issue issue;

  protected GlobalException(final Issue issue) {
    this.issue = issue;
  }

  public Issue getIssue() {
    return issue;
  }
}
