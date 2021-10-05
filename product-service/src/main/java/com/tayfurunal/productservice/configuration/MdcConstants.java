package com.tayfurunal.productservice.configuration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MdcConstants {

    public static final String X_CORRELATION_ID = "x-correlationId";
    public static final String X_AGENT_NAME = "x-agent-name";
    public static final String X_AGENT_USER = "x-agent-user";
    public static final String UNKNOWN_APPLICATION = "UNKNOWN_APPLICATION";
}

