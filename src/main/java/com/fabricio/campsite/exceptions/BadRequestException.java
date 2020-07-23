package com.fabricio.campsite.exceptions;

public class BadRequestException extends GlobalException {

  private static final long serialVersionUID = 1L;

  private BadRequestException(final Issue issue) {
    super(issue);
  }

  public static BadRequestException businessRule(final IssueEnum issueEnum, final Object... args) {
    return new BadRequestException(new Issue(issueEnum, args));
  }
}
