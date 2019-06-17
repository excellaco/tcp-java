package com.excella.reactor.shared;

import com.excella.reactor.domain.DomainModel;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class SampleEntity extends DomainModel {}
