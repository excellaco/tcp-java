package com.excella.reactor.common.exceptions;

import java.io.Serializable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor(staticName = "of")
public class GenericError implements Serializable {
  @NonNull private Integer code;
  @NonNull private String message;
}
