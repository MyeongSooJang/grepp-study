package com.grepp.jms.greppstudy.search.presentation.dto.response;

public record IndexUpdateResponse(boolean created, boolean settingsUpdated, boolean mappingUpdated) {
}
