package com.java.test.junior.service.actionHistory;

import com.java.test.junior.model.PaginationOptionsDTO;
import com.java.test.junior.model.actionHistory.ActionHistory;
import com.java.test.junior.model.actionHistory.ActionHistoryDTO;
import com.java.test.junior.model.response.PaginationResponse;

public interface ActionHistoryService {
    PaginationResponse<ActionHistory> getActionHistoryPage(PaginationOptionsDTO paginationOptions);

    void createActionHistory(ActionHistoryDTO actionHistory);
}
