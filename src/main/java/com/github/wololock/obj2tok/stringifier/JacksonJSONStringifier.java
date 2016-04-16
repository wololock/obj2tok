package com.github.wololock.obj2tok.stringifier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JacksonJSONStringifier implements Stringifier {

	private final ObjectMapper objectMapper;

	public JacksonJSONStringifier() {
		this.objectMapper = new ObjectMapper();
	}

	public JacksonJSONStringifier(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public String convertToString(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public <T> T createFromString(String str, Class<T> type) {
		try {
			return objectMapper.readValue(str, type);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
