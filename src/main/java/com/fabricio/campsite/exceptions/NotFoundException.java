package com.fabricio.campsite.exceptions;

public class NotFoundException extends GlobalException {

  private static final long serialVersionUID = 1L;

  private NotFoundException(final Issue issue) {

    super(issue);
  }

  public static NotFoundException notFound(final IssueEnum message, final Object... args) {
    return new NotFoundException(new Issue(message, args));
  }
}
