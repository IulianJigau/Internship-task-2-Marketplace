package com.java.test.junior.controller;

import com.java.test.junior.model.PaginationOptionsDTO;
import com.java.test.junior.model.actionHistory.ActionHistory;
import com.java.test.junior.model.response.PaginationResponse;
import com.java.test.junior.service.actionHistory.ActionHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Action history controller", description = "Lists action history")
@RestController
@Validated
@RequestMapping("/history")
@RequiredArgsConstructor
public class ActionHistoryController {

    private final ActionHistoryService actionHistoryService;

    @Operation(summary = "Get the user action history")
    @GetMapping("/list")
    @PreAuthorize("@roleChecker.hasAdminRole(principal)")
    public PaginationResponse<ActionHistory> getActionHistory(@Valid @ModelAttribute PaginationOptionsDTO paginationOptions) {
        return actionHistoryService.getActionHistoryPage(paginationOptions);
    }

}
