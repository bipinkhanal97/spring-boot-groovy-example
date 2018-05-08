package sample

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue

import java.io.IOException
import java.util.ArrayList
import java.util.Arrays
import java.util.List
import java.util.Map
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpRequest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.http.client.InterceptingClientHttpRequestFactory
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.DefaultResponseErrorHandler
import org.springframework.web.client.RestTemplate

/**
 * Basic integration tests for service demo application.
 * 
 * @author Dave Syer
 */
public class SampleApplicationTests {

	private static ConfigurableApplicationContext context

	@BeforeClass
	public static void start() throws Exception {
		Future<ConfigurableApplicationContext> future = Executors
				.newSingleThreadExecutor().submit(
						new Callable<ConfigurableApplicationContext>() {
							@Override
							public ConfigurableApplicationContext call() throws Exception {
								return SpringApplication
										.run(SampleApplication.class)
							}
						})
		context = future.get(60, TimeUnit.SECONDS)
	}

	@AfterClass
	public static void stop() {
		if (context != null) {
			context.close()
		}
	}

	@Test
	void testHome() throws Exception {
		@SuppressWarnings("rawtypes")
		ResponseEntity<Map> entity = getRestTemplate().getForEntity(
				"http://localhost:8080", Map.class)
		assertEquals(HttpStatus.OK, entity.getStatusCode())
		@SuppressWarnings("unchecked")
		Map<String, Object> body = entity.getBody()
		assertEquals("Hello World", body.get("message"))
	}

	@Test
	void testErrorPageDirectAccess() throws Exception {
		@SuppressWarnings("rawtypes")
		ResponseEntity<Map> entity = getRestTemplate().getForEntity(
				"http://localhost:8080/error", Map.class)
		assertEquals(HttpStatus.OK, entity.getStatusCode())
		@SuppressWarnings("unchecked")
		Map<String, Object> body = entity.getBody()
		assertEquals("None", body.get("error"))
		assertEquals(999, body.get("status"))
	}

	private String getPassword() {
		return context.getBean(SecurityProperties.class).getUser().getPassword()
	}

	private RestTemplate getRestTemplate() {

		RestTemplate restTemplate = new RestTemplate()
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
			@Override
			public void handleError(ClientHttpResponse response) throws IOException {
			}
		})
		restTemplate

	}

}
