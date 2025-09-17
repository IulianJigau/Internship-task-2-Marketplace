package com.java.test.junior.service.actionHistory;

import com.java.test.junior.mapper.ActionHistoryMapper;
import com.java.test.junior.model.PaginationOptionsDTO;
import com.java.test.junior.model.actionHistory.ActionHistory;
import com.java.test.junior.model.actionHistory.ActionHistoryDTO;
import com.java.test.junior.model.response.PaginationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActionHistoryServiceImpl implements ActionHistoryService {

    private final ActionHistoryMapper actionHistoryMapper;

    public PaginationResponse<ActionHistory> getActionHistoryPage(PaginationOptionsDTO paginationOptions) {
        List<ActionHistory> actionHistory = actionHistoryMapper.getPage(paginationOptions.getPage(), paginationOptions.getPageSize());

        long entries = -1L;
        if (paginationOptions.getRefresh()) {
            entries = actionHistoryMapper.getTotalEntries();
        }

        return new PaginationResponse<>(entries, actionHistory);
    }

    public void createActionHistory(ActionHistoryDTO actionHistory) {
        actionHistoryMapper.insert(actionHistory);
    }
}
