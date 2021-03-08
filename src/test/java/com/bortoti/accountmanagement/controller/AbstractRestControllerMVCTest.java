package com.bortoti.accountmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.MultiValueMap;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class AbstractRestControllerMVCTest {

	private ObjectMapper mapper;

	@Autowired
	private MockMvc mockMvc;

	public void setup() throws Exception {
		this.mapper = new ObjectMapper();
		this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	protected ResultActions performPost(
			String path, Object request, MultiValueMap<String, String> requestParams, Object... pathParams) throws Exception {

		return mockMvc //
				.perform(post(path, pathParams) //
						.contentType(APPLICATION_JSON) //
						.content(json(request)) //
						.params(requestParams));
	}

	protected ResultActions performGet(
			String path, MultiValueMap<String, String> requestParams, Object... pathParams) throws Exception {

		return mockMvc //
				.perform(get(path, pathParams) //
						.contentType(APPLICATION_JSON)
						.params(requestParams));
	}

	protected String json(Object o) throws IOException {
		return this.mapper.writeValueAsString(o);
	}
}