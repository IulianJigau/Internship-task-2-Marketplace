package com.java.test.junior.model.RequestResponses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PaginationResponse<T> {
    private Long totalElements;
    private List<T> data;
}
