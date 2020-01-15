package com.example.Oauth2Client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.example.Oauth2Client.entity.LoginRequest;
import com.example.Oauth2Client.entity.OauthEntity;

@RestController
@Configuration
public class ServiceController {

	@Autowired
	public RestOperations template;
	private RestTemplate restTemplate = new RestTemplate();

	@RequestMapping(value = "/test", method = RequestMethod.POST, produces = { "application/JSON" }, consumes = {
			"text/plain" })
	public String getShipmentDetails(@RequestParam(value = "name", defaultValue = "0") String name) {
		ResponseEntity<String> result = template.postForEntity("http://localhost:9080/hello", name, String.class);
		return result.getBody();
	}

	@PostMapping(path = "/login/verify")
	public @ResponseBody OauthEntity login(@RequestBody LoginRequest loginRequest) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setBasicAuth(loginRequest.getClientId(), loginRequest.getClientSecret());// ("testjwtclientid",
																							// "jwtpass");
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("grant_type", loginRequest.getGrantType());// "password");
		map.add("username", loginRequest.getUsername());// "admin");
		map.add("password", loginRequest.getPassword());// "jwtpass");
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
		ResponseEntity<OauthEntity> result = restTemplate.postForEntity("http://localhost:9090/oauth/token", request,
				OauthEntity.class);
		System.out.println("Status code: " + result.getStatusCodeValue());
		return result.getBody();
	}

}
