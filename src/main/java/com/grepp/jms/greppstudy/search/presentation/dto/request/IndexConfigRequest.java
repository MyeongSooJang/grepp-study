package com.grepp.jms.greppstudy.search.presentation.dto.request;

public record IndexConfigRequest(Integer numberOfShards, Integer numberOfReplicas) {
}
