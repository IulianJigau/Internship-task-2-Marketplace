package com.java.test.junior.component;

import lombok.Data;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class PermitAllConfig {

    private List<RequestMatcher> matchers;

    public PermitAllConfig() {
        matchers = List.of(
                PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/users"),
                PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.POST, "/login"),
                PathPatternRequestMatcher.withDefaults().matcher("/v3/api-docs/**"),
                PathPatternRequestMatcher.withDefaults().matcher("/swagger-ui.html"),
                PathPatternRequestMatcher.withDefaults().matcher("/swagger-ui/**")
        );
    }
}
