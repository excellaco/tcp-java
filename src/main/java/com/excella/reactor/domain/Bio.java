package com.excella.reactor.domain;

import com.excella.reactor.validation.constraints.AdultBirthdate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
@Embeddable
public class Bio implements Serializable {
  @NotEmpty private String firstName;
  private String middleName;
  @NotEmpty private String lastName;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @JsonDeserialize(using = LocalDateDeserializer.class)
  @JsonSerialize(using = LocalDateSerializer.class)
  @NotNull
  @AdultBirthdate
  private LocalDate birthDate;

  @NotNull
  @Enumerated(value = EnumType.STRING)
  private Gender gender;

  @NotNull
  @Enumerated(value = EnumType.STRING)
  private Ethnicity ethnicity;

  @NotNull private Boolean usCitizen;
}
