package com.a304.intagral.common.util;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Long> {


    @Override
    public Optional<Long> getCurrentAuditor() {
        return Optional.of(Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName()));
    }
}
