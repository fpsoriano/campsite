package com.fabricio.campsite.exceptions;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Issue implements Serializable {

  private static final long serialVersionUID = 1L;

  @JsonProperty
  private final int code;

  @JsonProperty private final String message;

  @JsonProperty private List<String> details;

  public Issue(final IssueEnum issue) {

    code = issue.getCode();
    message = issue.getFormattedMessage();
  }

  public Issue(final IssueEnum issue, final Object... args) {

    code = issue.getCode();
    message = issue.getFormattedMessage(args);
  }

  public Issue(final IssueEnum issue, final List<String> details) {

    this(issue);
    this.details = details;
  }

  public int getCode() {

    return code;
  }

  public String getMessage() {

    return message;
  }

  @Override
  public String toString() {

    return String.format("Issue{code= %s, message='%s' details= '%s'}", code, message, details);
  }
}