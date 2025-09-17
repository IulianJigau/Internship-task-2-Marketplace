package com.java.test.junior.mapper;

import com.java.test.junior.model.actionHistory.ActionHistory;
import com.java.test.junior.model.actionHistory.ActionHistoryDTO;

import java.util.List;

public interface ActionHistoryMapper {
    List<ActionHistory> getPage(Integer page, Integer size);

    long getTotalEntries();

    void insert(ActionHistoryDTO actionHistory);
}
