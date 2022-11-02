package com.a304.intagral.common.util;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Integer> {


    @Override
    public Optional<Integer> getCurrentAuditor() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if("anonymousUser".equals(userId)){
            userId = "0";
        }
        return Optional.of(Integer.valueOf(userId));
    }
}
