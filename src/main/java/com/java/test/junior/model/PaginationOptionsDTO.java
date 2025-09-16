package com.java.test.junior.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginationOptionsDTO {
    @Positive
    Integer page = 1;
    @Max(1000)
    @Positive
    Integer pageSize = 10;
    Boolean refresh = true;
}
