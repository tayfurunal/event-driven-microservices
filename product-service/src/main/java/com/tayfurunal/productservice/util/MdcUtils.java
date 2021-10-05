package com.tayfurunal.productservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.MDC;

import java.util.Objects;

import static com.tayfurunal.productservice.configuration.MdcConstants.X_AGENT_USER;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MdcUtils {

    public static Integer getAgentUserId() {
        final String xAgentUser = MDC.get(X_AGENT_USER);
        if (Objects.nonNull(xAgentUser)) {
            return Integer.valueOf(xAgentUser);
        }
        return null;
    }
}
