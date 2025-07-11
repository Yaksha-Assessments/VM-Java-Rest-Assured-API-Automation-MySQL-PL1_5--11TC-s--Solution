package rest;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ApiUtil {
	private static final Set<Integer> usedNumbers = new HashSet<>();
	private static final Random random = new Random();
	private static String BASE_URL;
	Properties prop;

	/**
	 * Retrieves the base URL from the configuration properties file.
	 *
	 * <p>
	 * This method loads the properties from the file located at
	 * <code>{user.dir}/src/main/resources/config.properties</code> and extracts the
	 * value associated with the key <code>base.url</code>. The value is stored in
	 * the static variable <code>BASE_URL</code> and returned.
	 *
	 * @return the base URL string if successfully read from the properties file;
	 *         {@code null} if an I/O error occurs while reading the file.
	 */
	public String getBaseUrl() {
		prop = new Properties();
		try (FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\resources\\config.properties")) {
			prop.load(fis);
			BASE_URL = prop.getProperty("base.url");
			return BASE_URL;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Retrieves the username from the configuration properties file.
	 *
	 * <p>
	 * This method reads the properties from the file located at
	 * <code>{user.dir}/src/main/resources/config.properties</code> and returns the
	 * value associated with the key <code>username</code>.
	 *
	 * @return the username as a {@code String} if found in the properties file;
	 *         {@code null} if an I/O error occurs while reading the file.
	 */
	public String getUsername() {
		prop = new Properties();
		try (FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\resources\\config.properties")) {
			prop.load(fis);
			return prop.getProperty("username");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getPassword() {
		prop = new Properties();
		try (FileInputStream fis = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\resources\\config.properties")) {
			prop.load(fis);
			return prop.getProperty("password");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Retrieves the password from the configuration properties file.
	 *
	 * <p>
	 * This method loads the properties from the file located at
	 * <code>{user.dir}/src/main/resources/config.properties</code> and returns the
	 * value associated with the key <code>password</code>.
	 *
	 * @return the password as a {@code String} if found in the properties file;
	 *         {@code null} if an I/O error occurs while reading the file.
	 */
	public static String generateUniqueName(String base) {
		int uniqueNumber;
		do {
			uniqueNumber = 1000 + random.nextInt(9000);
		} while (usedNumbers.contains(uniqueNumber));

		usedNumbers.add(uniqueNumber);
		return base + uniqueNumber;
	}

	public static int generateUniqueID(int base) {
		int uniqueNumber;
		do {
			uniqueNumber = 1000 + random.nextInt(9000);
		} while (usedNumbers.contains(uniqueNumber));

		usedNumbers.add(uniqueNumber);
		return base + uniqueNumber;
	}

	/**
	 * Sends a GET request to the specified endpoint with a cookie and optional
	 * request body.
	 *
	 * <p>
	 * This method uses RestAssured to construct and send a GET request to the given
	 * endpoint. It attaches a cookie named <code>orangehrm</code> with the provided
	 * value and sets the <code>Content-Type</code> header to
	 * <code>application/json</code>. If a request body is provided, it is included
	 * in the request.
	 *
	 * @param endpoint    the API endpoint to send the request to (relative to the
	 *                    base URL)
	 * @param cookieValue the value of the <code>orangehrm</code> cookie to include
	 *                    in the request
	 * @param body        a map containing key-value pairs to be sent as the JSON
	 *                    request body (can be null)
	 * @return the response received from the GET request
	 */

	public CustomResponse getSubunitsTree(String endpoint, String cookieValue, Map<String, String> body) {

		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (body != null) {
			request.body(body);
		}

		Response response = request.get(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		List<Object> ids = new ArrayList<>();
		List<Object> unitIds = new ArrayList<>();
		List<Object> names = new ArrayList<>();
		List<Object> levels = new ArrayList<>();
		List<Object> children = new ArrayList<>();

		JsonPath jsonPath = response.jsonPath();
		List<Map<String, Object>> data = jsonPath.getList("data");

		if (data != null) {
			for (Map<String, Object> unit : data) {
				ids.add(unit.get("id"));
				unitIds.add(unit.get("unitId"));
				names.add(unit.get("name"));
				levels.add(unit.get("level"));
				children.add(unit.get("children")); // This can be nested list
			}
		} else {
			System.out.println("⚠️ 'data' field is null in response. Status code: " + statusCode);
		}

		return new CustomResponse(response, statusCode, status, ids, unitIds, names, levels, children);
	}

	/**
	 * Sends a GET request to the specified endpoint with a cookie and optional
	 * request body.
	 *
	 * <p>
	 * This method constructs a GET request using RestAssured, attaching a cookie
	 * named <code>orangehrm</code> with the specified value and setting the
	 * <code>Content-Type</code> header to <code>application/json</code>. If a body
	 * is provided, it is included in the request.
	 *
	 * @param endpoint    the relative URL endpoint to send the GET request to
	 * @param cookieValue the value for the <code>orangehrm</code> cookie
	 * @param body        a map of key-value pairs representing the request body
	 *                    (optional, may be null)
	 * @return the response received from the GET request
	 */
	public CustomResponse getAdminSkills(String endpoint, String cookieValue, Map<String, String> body) {

		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (body != null) {
			request.body(body);
		}

		Response response = request.get(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		List<Object> ids = new ArrayList<>();
		List<Object> names = new ArrayList<>();
		List<Object> descriptions = new ArrayList<>();

		JsonPath jsonPath = response.jsonPath();
		List<Map<String, Object>> data = jsonPath.getList("data");

		if (data != null) {
			for (Map<String, Object> skill : data) {
				ids.add(skill.get("id"));
				names.add(skill.get("name"));
				descriptions.add(skill.get("description"));
			}
		} else {
			System.out.println("⚠️ 'data' field is null in response. Status code: " + statusCode);
		}

		return new CustomResponse(response, statusCode, status, ids, names, descriptions);
	}

	/**
	 * Sends a GET request to the specified endpoint with a cookie and optional
	 * request body.
	 *
	 * <p>
	 * This method constructs a GET request using RestAssured, attaching a cookie
	 * named <code>orangehrm</code> with the specified value and setting the
	 * <code>Content-Type</code> header to <code>application/json</code>. If a body
	 * is provided, it is included in the request.
	 *
	 * @param endpoint    the relative URL endpoint to send the GET request to
	 * @param cookieValue the value for the <code>orangehrm</code> cookie
	 * @param body        a map of key-value pairs representing the request body
	 *                    (optional, may be null)
	 * @return the response received from the GET request
	 */
	public CustomResponse getAdminEdu(String endpoint, String cookieValue, Map<String, String> body) {

		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (body != null) {
			request.body(body);
		}

		Response response = request.get(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		List<Object> ids = new ArrayList<>();
		List<Object> names = new ArrayList<>();

		JsonPath jsonPath = response.jsonPath();
		List<Map<String, Object>> data = jsonPath.getList("data");

		if (data != null) {
			for (Map<String, Object> item : data) {
				ids.add(item.get("id"));
				names.add(item.get("name"));
			}
		} else {
			System.out.println("⚠️ 'data' field is null in response. Status code: " + statusCode);
		}

		return new CustomResponse(response, statusCode, status, ids, names);
	}

	/**
	 * Sends a GET request to the specified endpoint with a cookie and optional
	 * request body.
	 *
	 * <p>
	 * This method constructs a GET request using RestAssured, attaching a cookie
	 * named <code>orangehrm</code> with the specified value and setting the
	 * <code>Content-Type</code> header to <code>application/json</code>. If a body
	 * is provided, it is included in the request.
	 *
	 * @param endpoint    the relative URL endpoint to send the GET request to
	 * @param cookieValue the value for the <code>orangehrm</code> cookie
	 * @param body        a map of key-value pairs representing the request body
	 *                    (optional, may be null)
	 * @return the response received from the GET request
	 */
	public CustomResponse getAdminLicenses(String endpoint, String cookieValue, Map<String, String> body) {

		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (body != null) {
			request.body(body);
		}

		Response response = request.get(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		List<Object> ids = new ArrayList<>();
		List<Object> names = new ArrayList<>();

		JsonPath jsonPath = response.jsonPath();
		List<Map<String, Object>> data = jsonPath.getList("data");

		if (data != null) {
			for (Map<String, Object> item : data) {
				ids.add(item.get("id"));
				names.add(item.get("name"));
			}
		} else {
			System.out.println("⚠️ 'data' field is null in response. Status code: " + statusCode);
		}

		return new CustomResponse(response, statusCode, status, ids, names);
	}

	/**
	 * Sends a GET request to the specified endpoint with a cookie and optional
	 * request body.
	 *
	 * <p>
	 * This method constructs a GET request using RestAssured, attaching a cookie
	 * named <code>orangehrm</code> with the specified value and setting the
	 * <code>Content-Type</code> header to <code>application/json</code>. If a body
	 * is provided, it is included in the request.
	 *
	 * @param endpoint    the relative URL endpoint to send the GET request to
	 * @param cookieValue the value for the <code>orangehrm</code> cookie
	 * @param body        a map of key-value pairs representing the request body
	 *                    (optional, may be null)
	 * @return the response received from the GET request
	 */
	public CustomResponse getAdminLanguages(String endpoint, String cookieValue, Map<String, String> body) {

		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (body != null) {
			request.body(body);
		}

		Response response = request.get(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		List<Object> ids = new ArrayList<>();
		List<Object> names = new ArrayList<>();

		JsonPath jsonPath = response.jsonPath();
		List<Map<String, Object>> data = jsonPath.getList("data");

		if (data != null) {
			for (Map<String, Object> item : data) {
				ids.add(item.get("id"));
				names.add(item.get("name"));
			}
		} else {
			System.out.println("⚠️ 'data' field is null in response. Status code: " + statusCode);
		}

		return new CustomResponse(response, statusCode, status, ids, names);
	}

	/**
	 * Sends a GET request to the specified API endpoint with an authentication
	 * cookie and optional body.
	 *
	 * <p>
	 * This method utilizes RestAssured to perform a GET request to the provided
	 * endpoint. It includes a cookie named <code>orangehrm</code> with the
	 * specified value and sets the <code>Content-Type</code> to
	 * <code>application/json</code>. If a request body is provided, it is attached
	 * to the request.
	 *
	 * @param endpoint    the relative endpoint path to be appended to the base URL
	 * @param cookieValue the value of the <code>orangehrm</code> cookie for session
	 *                    handling or authentication
	 * @param body        a map containing request body parameters (optional; may be
	 *                    null)
	 * @return the {@code Response} object containing the results of the GET request
	 */
	public CustomResponse deleteValidUsers(String endpoint, String cookieValue, String requestBody) {

		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (requestBody != null) {
			request.body(requestBody);
		}

		Response response = request.delete(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		// Log full response
		System.out.println("Delete API Response:");
		response.prettyPrint();

		return new CustomResponse(response, statusCode, status, null);
	}

	/**
	 * Sends a POST request to the specified endpoint with a cookie and JSON string
	 * payload.
	 *
	 * <p>
	 * This method uses RestAssured to perform a POST request to the given endpoint.
	 * It sets the <code>Content-Type</code> header to
	 * <code>application/json</code>, includes a cookie named
	 * <code>orangehrm</code>, and attaches the provided JSON string as the request
	 * body.
	 *
	 * @param endpoint    the relative URL endpoint for the POST request, appended
	 *                    to the base URL
	 * @param cookieValue the value of the <code>orangehrm</code> cookie used for
	 *                    authentication or session handling
	 * @param body        a JSON-formatted string representing the request payload
	 * @return the {@link io.restassured.response.Response} object containing the
	 *         server's response
	 */
	public CustomResponse postNewJobTitles(String endpoint, String cookieValue, String requestBody) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (requestBody != null) {
			request.body(requestBody);
		}

		Response response = request.post(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		JsonPath jsonPath = response.jsonPath();
		Map<String, Object> data = jsonPath.getMap("data");

		Object title2 = null;

		if (data != null) {

			title2 = data.get("title");

			System.out.println("✅ Title: " + title2);
		} else {
			System.out.println("❌ 'data' object is missing or empty in the response. Status code: " + statusCode);
		}

		return new CustomResponse(response, statusCode, status, title2);
	}

	/**
	 * Sends a PUT request to the specified endpoint with a cookie and optional
	 * request body.
	 *
	 * <p>
	 * This method constructs a PUT request using RestAssured, sets the
	 * <code>Content-Type</code> header to <code>application/json</code>, and
	 * includes a cookie named <code>orangehrm</code>. If a request body is
	 * provided, it is attached to the request.
	 *
	 * @param endpoint    the relative endpoint to which the PUT request is sent,
	 *                    appended to the base URL
	 * @param cookieValue the value of the <code>orangehrm</code> cookie for session
	 *                    or authorization purposes
	 * @param requestBody an object representing the request body (can be null)
	 * @return the {@link io.restassured.response.Response} returned from the PUT
	 *         request
	 */
	@SuppressWarnings("unchecked")
	public CustomResponse putJobTitleByID(String endpoint, String cookieValue, String requestBody) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (requestBody != null) {
			request.body(requestBody);
		}

		Response response = request.put(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		JsonPath jsonPath = response.jsonPath();
		Map<String, Object> data = jsonPath.getMap("data");

		List<Object> ids = new ArrayList<>();
		List<Object> titles = new ArrayList<>();
		Object description = null;
		Object note = null;
		Map<String, Object> jobSpecification = null;

		if (data != null) {
			ids.add(data.get("id"));
			titles.add(data.get("title"));
			description = data.get("description");
			note = data.get("note");
			jobSpecification = (Map<String, Object>) data.get("jobSpecification");
		} else {
			System.out.println("❌ 'data' object is missing or empty in the response. Status code: " + statusCode);
		}

		return new CustomResponse(response, statusCode, status, ids, titles, description, note, jobSpecification);
	}

	/**
	 * Sends a PUT request to the specified endpoint with a cookie and optional
	 * request body.
	 *
	 * <p>
	 * This method constructs a PUT request using RestAssured, sets the
	 * <code>Content-Type</code> header to <code>application/json</code>, and
	 * includes a cookie named <code>orangehrm</code>. If a request body is
	 * provided, it is attached to the request.
	 *
	 * @param endpoint    the relative endpoint to which the PUT request is sent,
	 *                    appended to the base URL
	 * @param cookieValue the value of the <code>orangehrm</code> cookie for session
	 *                    or authorization purposes
	 * @param body        an object representing the request body (can be null)
	 * @return the {@link io.restassured.response.Response} returned from the PUT
	 *         request
	 */

	public CustomResponse postUniquePaygrade(String endpoint, String cookieValue, String requestBody) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (requestBody != null) {
			request.body(requestBody);
		}

		Response response = request.post(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();

		JsonPath jsonPath = response.jsonPath();
		Map<String, Object> data = jsonPath.getMap("data");

		Object id2 = null;
		Object name = null;

		if (data != null) {
			id2 = data.get("id");
			name = data.get("name");

			System.out.println("✅ ID: " + id2);
			System.out.println("✅ Name: " + name);
		} else {
			System.out.println("❌ 'data' object is missing or empty in the response. Status code: " + statusCode);
		}

		return new CustomResponse(response, statusCode, status, id2, name);
	}

	/*
	 * Executes a DELETE request to the specified endpoint with a cookie and JSON
	 * string body.
	 *
	 * <p> This method constructs a DELETE request using RestAssured, setting the
	 * <code>Content-Type</code> header to <code>application/json</code> and
	 * attaching a cookie named <code>orangehrm</code>. The provided JSON string is
	 * used as the request payload.
	 *
	 * @param endpoint the relative API endpoint to which the DELETE request is sent
	 * 
	 * @param cookieValue the value of the <code>orangehrm</code> cookie used for
	 * session or authentication
	 * 
	 * @param body the request payload in JSON string format
	 * 
	 * @return the {@link io.restassured.response.Response} object representing the
	 * server's response
	 */

	public CustomResponse deletePaygradeById(String endpoint, String cookieValue, String requestBody) {
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		if (requestBody != null) {
			request.body(requestBody);
		}

		Response response = request.delete(BASE_URL + endpoint).then().extract().response();

		int statusCode = response.getStatusCode();
		String status = response.getStatusLine();
		;

		JsonPath jsonPath = response.jsonPath();
		List<Object> dataList = jsonPath.getList("data");

		System.out.println(dataList);

		if (dataList != null && !dataList.isEmpty()) {
		} else {
			System.out.println("❌ 'data' object is missing or empty in the response. Status code: " + statusCode);
		}

		return new CustomResponse(response, statusCode, status);
	}

}
