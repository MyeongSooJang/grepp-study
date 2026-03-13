package com.grepp.jms.greppstudy.search.presentation.dto.response;

import java.util.Map;

public record IndexStatusResponse(boolean exists, Map<String, Object> settings, Map<String, Object> mapping) {
}
