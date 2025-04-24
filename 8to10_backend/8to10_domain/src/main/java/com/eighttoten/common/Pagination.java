package com.eighttoten.common;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Pagination<T> {
    private List<T> contents;
    private long pageNumber;
    private long pageSize;
    private Long totalElements;

    public long getTotalPages(){
        return totalElements / pageSize;
    }
}