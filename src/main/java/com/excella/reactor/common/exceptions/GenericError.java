package com.excella.reactor.common.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class GenericError implements Serializable {
  @NonNull private Integer code;
  @NonNull private String message;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private List<String> details;
}
