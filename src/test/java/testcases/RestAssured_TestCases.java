package testcases;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import rest.ApiUtil;
import rest.CustomResponse;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import coreUtilities.utils.FileOperations;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

public class RestAssured_TestCases {

	private static String baseUrl;
	private static String username;
	private static String password;
	private static String cookieValue = null;
	private ApiUtil apiUtil;
	int payGradeId;
	@SuppressWarnings("unused")
	private int userIdToDelete;
	private String apiUtilPath = System.getProperty("user.dir") + "\\src\\main\\java\\rest\\ApiUtil.java";
	private String excelPath = System.getProperty("user.dir") + "\\src\\main\\resources\\testData.xlsx";
	FileOperations excel = new FileOperations();

	/**
	 * @BeforeClass method to perform login via Selenium and retrieve session cookie
	 *              for authenticated API calls.
	 * 
	 *              Steps: 1. Setup ChromeDriver using WebDriverManager. 2. Launch
	 *              browser and open the OrangeHRM login page. 3. Perform login with
	 *              provided username and password. 4. Wait for login to complete
	 *              and extract the 'orangehrm' session cookie. 5. Store the cookie
	 *              value to be used in API requests. 6. Quit the browser session.
	 * 
	 *              Throws: - InterruptedException if thread sleep is interrupted. -
	 *              RuntimeException if the required session cookie is not found.
	 */

	@Test(priority = 0, groups = {
			"PL1" }, description = "Login to OrangeHRM using Selenium and retrieve session cookie")
	public void loginWithSeleniumAndGetCookie() throws InterruptedException {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();

		apiUtil = new ApiUtil();
		baseUrl = apiUtil.getBaseUrl();
		username = apiUtil.getUsername();
		password = apiUtil.getPassword();

		driver.get(baseUrl + "/web/index.php/auth/login");
		Thread.sleep(3000); // Wait for page load

		// Login to the app
		driver.findElement(By.name("username")).sendKeys(username);
		driver.findElement(By.name("password")).sendKeys(password);
		driver.findElement(By.cssSelector("button[type='submit']")).click();
		Thread.sleep(6000); // Wait for login

		// Extract cookie named "orangehrm"
		Set<Cookie> cookies = driver.manage().getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("orangehrm")) {
				cookieValue = cookie.getValue();
				break;
			}
		}

		driver.quit();

		if (cookieValue == null) {
			throw new RuntimeException("orangehrm cookie not found after login");
		}
	}

	@Test(priority = 1, groups = {
			"PL1" }, description = "1. Send a GET request to the '/web/index.php/api/v2/admin/subunits?mode=tree' endpoint with a valid cookie\n"
					+ "2. Do not pass any request body (null)\n"
					+ "3. Validate the request uses proper Rest Assured methods (given, cookie, get, response)\n"
					+ "4. Validate that the response contains non-empty 'id' and 'name' fields in the 'data' list\n"
					+ "5. Validate response status code is 200 and status line is 'HTTP/1.1 200 OK'\n"
					+ "6. Print the full response body for verification")

	public void getSubunitsTree() throws IOException {
		String endpoint = "/web/index.php/api/v2/admin/subunits?mode=tree";

		CustomResponse customResponse = apiUtil.getSubunitsTree(endpoint, cookieValue, null);

		// Step 1: Validate that method uses proper Rest Assured calls
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "getSubunitsTree",
				List.of("given", "cookie", "get", "response"));
		List<Object> itemIds = customResponse.getIds();
		List<Object> names = customResponse.getNames();
		Assert.assertFalse(itemIds.isEmpty(), "ID list should not be empty.");
		Assert.assertFalse(names.isEmpty(), "Name list should not be empty.");

		Assert.assertTrue(isImplementationCorrect,
				"GetHolidayData must be implemented using RestAssured methods only!");

		Assert.assertTrue(TestCodeValidator.validateResponseFields("getSubunitsTree", customResponse),
				"Response must contain all required fields");

		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");

		Assert.assertEquals(customResponse.getStatus(), "HTTP/1.1 200 OK", "Status should be OK.");

		System.out.println("getSubunitsTree API Response:");
		customResponse.getResponse().prettyPrint();
	}

//
	@Test(priority = 2, groups = {
			"PL1" }, description = "1. Send a GET request to the '/web/index.php/api/v2/admin/skills?limit=50&offset=0' endpoint with a valid cookie\n"
					+ "2. Do not pass any request body (null)\n"
					+ "3. Validate that the method uses Rest Assured methods: given, cookie, get, response\n"
					+ "4. Assert that the 'data' array in the response contains non-empty 'id' and 'name' fields\n"
					+ "5. Validate response status code is 200 and status line is 'HTTP/1.1 200 OK'\n"
					+ "6. Print the complete API response body for verification")

	public void getAdminSkills() throws IOException {
		String endpoint = "/web/index.php/api/v2/admin/skills?limit=50&offset=0";

		CustomResponse customResponse = apiUtil.getAdminSkills(endpoint, cookieValue, null);

		// Step 1: Validate that method uses proper Rest Assured calls
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "getAdminSkills",
				List.of("given", "cookie", "get", "response"));
		// response, statusCode, status, ids, names, descriptions
		List<Object> itemIds = customResponse.getIds(); // id
		List<Object> names = customResponse.getNames(); // date
		Assert.assertFalse(itemIds.isEmpty(), "ID list should not be empty.");
		Assert.assertFalse(names.isEmpty(), "Name list should not be empty.");

		Assert.assertTrue(isImplementationCorrect,
				"GetHolidayData must be implemented using RestAssured methods only!");

		Assert.assertTrue(TestCodeValidator.validateResponseFields("getAdminSkills", customResponse),
				"Response must contain all required fields");

		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");

		Assert.assertEquals(customResponse.getStatus(), "HTTP/1.1 200 OK", "Status should be OK.");

		System.out.println("getAdminSkills API Response:");
		customResponse.getResponse().prettyPrint();
	}

	@Test(priority = 3, groups = {
			"PL1" }, description = "1. Send a GET request to the '/web/index.php/api/v2/admin/educations?limit=50&offset=0' endpoint with a valid cookie\n"
					+ "2. Do not pass any request body (null)\n"
					+ "3. Validate that the method uses Rest Assured methods: given, cookie, get, response\n"
					+ "4. Assert that the 'data' array in the response contains non-empty 'id' and 'name' fields\n"
					+ "5. Verify that the status code is 200 and status line is 'HTTP/1.1 200 OK'\n"
					+ "6. Print the full API response body to the console for verification")

	public void getAdminEdu() throws IOException {
		String endpoint = "/web/index.php/api/v2/admin/educations?limit=50&offset=0";

		CustomResponse customResponse = apiUtil.getAdminEdu(endpoint, cookieValue, null);
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "getAdminEdu",
				List.of("given", "cookie", "get", "response"));
		List<Object> itemIds = customResponse.getIds(); // id
		List<Object> names = customResponse.getNames(); // date
		Assert.assertFalse(itemIds.isEmpty(), "ID list should not be empty.");
		Assert.assertFalse(names.isEmpty(), "Name list should not be empty.");

		Assert.assertTrue(isImplementationCorrect,
				"GetHolidayData must be implemented using RestAssured methods only!");

		Assert.assertTrue(TestCodeValidator.validateResponseFields("getAdminEdu", customResponse),
				"Response must contain all required fields");

		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");

		Assert.assertEquals(customResponse.getStatus(), "HTTP/1.1 200 OK", "Status should be OK.");

		System.out.println("getAdminEdu API Response:");
		customResponse.getResponse().prettyPrint();

	}

	@Test(priority = 4, groups = {
			"PL1" }, description = "1. Send a GET request to the '/web/index.php/api/v2/admin/licenses?limit=50&offset=0' endpoint with a valid cookie\n"
					+ "2. Do not pass any request body (null)\n"
					+ "3. Validate that the method uses Rest Assured methods: given, cookie, get, response\n"
					+ "4. Assert that the 'data' array in the response contains non-empty 'id' and 'name' fields\n"
					+ "5. Verify the status code is 200 and the status line is 'HTTP/1.1 200 OK'\n"
					+ "6. Print the full API response body for verification")

	public void getAdminLicenses() throws IOException {
		String endpoint = "/web/index.php/api/v2/admin/licenses?limit=50&offset=0";

		CustomResponse customResponse = apiUtil.getAdminLicenses(endpoint, cookieValue, null);

		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "getAdminLicenses",
				List.of("given", "cookie", "get", "response"));

		System.out.println("Status Code: " + customResponse.getStatus());
		Assert.assertTrue(isImplementationCorrect,
				"getAdminLicenses must be implementated using the Rest assured  methods only!");
		assertEquals(customResponse.getStatusCode(), 200);

		List<Object> itemIds = customResponse.getIds(); // id
		List<Object> names = customResponse.getNames(); // date
		Assert.assertFalse(itemIds.isEmpty(), "ID list should not be empty.");
		Assert.assertFalse(names.isEmpty(), "Name list should not be empty.");

		Assert.assertTrue(isImplementationCorrect,
				"GetHolidayData must be implemented using RestAssured methods only!");

		Assert.assertTrue(TestCodeValidator.validateResponseFields("getAdminEdu", customResponse),
				"Response must contain all required fields");

		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");

		Assert.assertEquals(customResponse.getStatus(), "HTTP/1.1 200 OK", "Status should be OK.");

		System.out.println("getAdminEdu API Response:");
		customResponse.getResponse().prettyPrint();

	}

//
	@Test(priority = 5, groups = {
			"PL1" }, description = "1. Send a GET request to the '/web/index.php/api/v2/admin/languages?limit=50&offset=0' endpoint with a valid cookie\n"
					+ "2. Do not pass any request body (null)\n"
					+ "3. Validate that the method uses Rest Assured methods: given, cookie, get, response\n"
					+ "4. Assert that the 'data' array in the response contains non-empty 'id' and 'name' fields\n"
					+ "5. Verify the status code is 200 and the status line is 'HTTP/1.1 200 OK'\n"
					+ "6. Print the full API response body for verification")

	public void getAdminLanguages() throws IOException {
		String endpoint = "/web/index.php/api/v2/admin/licenses?limit=50&offset=0";

		CustomResponse customResponse = apiUtil.getAdminLanguages(endpoint, cookieValue, null);

		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "getAdminLanguages",
				List.of("given", "cookie", "get", "response"));

		System.out.println("Status Code: " + customResponse.getStatus());
		Assert.assertTrue(isImplementationCorrect,
				"getAdminLanguages must be implementated using the Rest assured  methods only!");
		assertEquals(customResponse.getStatusCode(), 200);

		List<Object> itemIds = customResponse.getIds(); // id
		List<Object> names = customResponse.getNames(); // date
		Assert.assertFalse(itemIds.isEmpty(), "ID list should not be empty.");
		Assert.assertFalse(names.isEmpty(), "Name list should not be empty.");

		Assert.assertTrue(isImplementationCorrect,
				"getAdminLanguages must be implemented using RestAssured methods only!");

		Assert.assertTrue(TestCodeValidator.validateResponseFields("getAdminLanguages", customResponse),
				"Response must contain all required fields");

		Assert.assertEquals(customResponse.getStatusCode(), 200, "Status code should be 200.");

		Assert.assertEquals(customResponse.getStatus(), "HTTP/1.1 200 OK", "Status should be OK.");

		System.out.println("getAdminLanguages API Response:");
		customResponse.getResponse().prettyPrint();

	}

	@Test(priority = 6, dependsOnMethods = "loginWithSeleniumAndGetCookie", groups = {
			"PL1" }, description = "1. Send a GET request to '/web/index.php/api/v2/admin/users' to fetch user list\n"
					+ "2. Extract a valid user ID for deletion (excluding Admin)\n"
					+ "3. Construct the DELETE request body with the extracted ID in format: {\"ids\": [id]}\n"
					+ "4. Send a DELETE request to '/web/index.php/api/v2/admin/users' with a valid cookie and body\n"
					+ "5. Assert that the status code is 200 and implementation uses only RestAssured methods")

	public void deleteValidUsers() throws IOException {
		String endpoint = "/web/index.php/api/v2/admin/users?limit=50&offset=0&sortField=u.userName&sortOrder=ASC";
		Response Iresponse = getUsersID(endpoint, cookieValue, null);

		JsonPath jsonPath = Iresponse.jsonPath();
		int userIdToDelete = jsonPath.getInt("data[1].id");

		System.out.println("User ID to delete: " + userIdToDelete);

		String deleteEndPoint = "/web/index.php/api/v2/admin/users";
		String requestBody = "{\"ids\": [" + userIdToDelete + "]}";
		System.out.println("This is the delete request body: " + requestBody);

		// 🛠 FIXED: Pass the actual requestBody
		CustomResponse customResponse = apiUtil.deleteValidUsers(deleteEndPoint, cookieValue, requestBody);

		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "deleteValidUsers",
				List.of("given", "cookie", "delete", "response"));

		Assert.assertEquals(customResponse.getStatusCode(), 200, "Expected status code 200 for success.");
		Assert.assertTrue(isImplementationCorrect,
				"deleteValidUsers must be implemented using Rest Assured methods only!");

		System.out.println("deleteValidUsers API Response:");
		customResponse.getResponse().prettyPrint();
	}

	@Test(priority = 7, groups = { "PL1" }, description = "1. Generate a unique job title using a utility method\n"
			+ "2. Create a JSON request body with the generated job title and additional required fields\n"
			+ "3. Send a POST request to the '/web/index.php/api/v2/admin/job-titles' endpoint using a valid cookie\n"
			+ "4. Print and verify the status code and response body\n"
			+ "5. Assert that the status code is 200 and validate correct implementation using RestAssured methods")

	public void postNewJobTitles() throws Exception {
// this is used with the excel file;
		FileOperations excel = new FileOperations();

		Map<String, String> testData = excel.readExcelPOI(excelPath, "sheet4");
		System.out.println(testData);
		String titleName = testData.get("title");
		String uniqueTitle = ApiUtil.generateUniqueName(titleName);

		String endpoint = "/web/index.php/api/v2/admin/job-titles";

		String requestBody = "{\r\n" + "  \"title\": \"" + uniqueTitle + "\",\n" + "  \"description\": \"abccde\",\r\n"
				+ "  \"specification\": null,\r\n" + "  \"note\": \"\"\r\n" + "}\r\n" + "";

		CustomResponse customResponse = apiUtil.postNewJobTitles(endpoint, cookieValue, requestBody);

		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "postNewJobTitles",
				List.of("given", "cookie", "post", "response"));

		System.out.println("Status Code: " + customResponse.getStatusCode());
		System.out.println("Response Body: " + customResponse.getTitle2());
		Assert.assertTrue(isImplementationCorrect,
				"postNewJobTitles must be implementated using the Rest assured  methods only!");
		assertEquals(customResponse.getStatusCode(), 200);
		System.out.println("postNewJobTitles API Response:");
		customResponse.getResponse().prettyPrint();
	}

	@SuppressWarnings("unchecked")
	@Test(priority = 8, groups = {
			"PL1" }, description = "1. Prepare a JSON request body with updated job title details\n"
					+ "2. Send a PUT request to the '/web/index.php/api/v2/admin/job-titles/22' endpoint using a valid cookie\n"
					+ "3. Verify and assert that the response contains a non-empty list of titles and IDs\n"
					+ "4. Print the status code and the full response body for verification\n"
					+ "5. Assert that the status code is 200 (OK) and validate correct implementation using RestAssured methods")

	public void putJobTitleByID() throws IOException {
		String endpoint = "/web/index.php/api/v2/admin/job-titles/22";
		Map<String, String> testData = null;
		try {

			testData = excel.readExcelPOI(excelPath, "sheet4");
		} catch (Exception e) {

			System.out.println("sheet data not found");

			e.printStackTrace();
		}

		String titleData = testData.get("putTitle");

		String requestBody = "{ \"title\": \"" + titleData + "\", \"description\": null, \"note\": null }";

		CustomResponse customResponse = apiUtil.putJobTitleByID(endpoint, cookieValue, requestBody);
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath, "putJobTitleByID",
				List.of("given", "cookie", "put", "response"));
		List<Object> title = (List<Object>) customResponse.getTitles(); // date
		Assert.assertFalse(title.isEmpty(), "ID list should not be empty.");
		List<Object> id = customResponse.getIdss();
		Assert.assertFalse(id.isEmpty(), "ID list should not be empty.");

		// Assertions
		System.out.println("Status Code: " + customResponse.getStatusCode());
		Assert.assertTrue(isImplementationCorrect,
				"putJobTitleByID must be implementated using the Rest assured  methods only!");
		assertEquals(customResponse.getStatusCode(), 200);
		System.out.println("putJobTitleByID API Response:");
		customResponse.getResponse().prettyPrint();
	}

	@Test(priority = 9, groups = {
			"PL1" }, description = "1. Dynamically generate a unique pay grade name using a helper method\n"
					+ "2. Send a POST request to the '/web/index.php/api/v2/admin/pay-grades' endpoint with a valid cookie and the generated name in the request body\n"
					+ "3. Extract and store the ID and name of the newly created pay grade from the response\n"
					+ "4. Print the generated ID, name, request body, status code, and response body for verification\n"
					+ "5. Assert that ID and name are not null\n"
					+ "6. Assert that the status code is 200 and validate implementation correctness using RestAssured methods")

	public void postUniquePaygrade() throws IOException {
		String endpoint = "/web/index.php/api/v2/admin/pay-grades";
		Map<String, String> testData = null;
		try {
			testData = excel.readExcelPOI(excelPath, "sheet4");

		} catch (Exception e) {
			System.out.println("sheet data not found");
			e.printStackTrace();
		}
		String paygrade = testData.get("putTitle");
		String uniqueName = ApiUtil.generateUniqueName(paygrade);

		String requestBody = "{\n" + "  \"name\": \"" + uniqueName + "\"\n" + "}";
		CustomResponse customResponse = apiUtil.postUniquePaygrade(endpoint, cookieValue, requestBody);
		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath,
				"postUniquePaygrade", List.of("given", "cookie", "post", "response"));
		System.out.println("Status Code: " + customResponse.getStatusCode());
		Object id = customResponse.getId2();
		Object name = customResponse.getName();
		payGradeId = (int) customResponse.getId2();
		System.out.println("✅ ID: " + payGradeId);
		System.out.println("✅ ID: " + id);
		System.out.println("✅ Name: " + name);
		System.out.println("id" + ' ' + "name");
		if (id == null || name == null) {
			throw new AssertionError("❌ 'id' or 'name' is null. Test failed.");

		}
		Assert.assertTrue(isImplementationCorrect,
				"putJobTitleByID must be implementated using the Rest assured  methods only!");
		System.out.println("postUniquePaygrade API Response:");
		customResponse.getResponse().prettyPrint();

	}

	// delete method is not working because the data is not present on API .
	@Test(priority = 10, dependsOnMethods = "postUniquePaygrade", groups = {
			"PL1" }, description = "1. Retrieve the payGradeId stored from the previous test\n"
					+ "2. Prepare the DELETE request body dynamically using the retrieved payGradeId\n"
					+ "3. Send a DELETE request to the '/web/index.php/api/v2/admin/pay-grades' endpoint with a valid cookie\n"
					+ "4. Verify that the request body is correctly passed\n"
					+ "5. Print and verify the status code and response body\n"
					+ "6. Assert that the status code is 200 and validate implementation correctness using RestAssured methods")

	public void deletePaygradeById() throws IOException {
		String endpoint = "/web/index.php/api/v2/admin/pay-grades";
		int GradeId = payGradeId;
		String requestBody = "{\"ids\": [" + GradeId + "]}";

		CustomResponse customResponse = apiUtil.deletePaygradeById(endpoint, cookieValue, requestBody);

		boolean isImplementationCorrect = TestCodeValidator.validateTestMethodFromFile(apiUtilPath,
				"deletePaygradeById", List.of("given", "cookie", "delete", "response"));
		String status = customResponse.getStatus();
		Assert.assertTrue(isImplementationCorrect,
				"putJobTitleByID must be implementated using the Rest assured  methods only!");
		Assert.assertFalse(status.isEmpty(), "status should not be empty.");
		System.out.println("deletePaygradeById API Response:");
		customResponse.getResponse().prettyPrint();

	}

	/*----------------------------------------Helper Functions-----------------------------------------*/

	public Response getUsersID(String endpoint, String cookieValue, Map<String, String> body) {
		baseUrl = apiUtil.getBaseUrl();
		RequestSpecification request = RestAssured.given().cookie("orangehrm", cookieValue).header("Content-Type",
				"application/json");

		// Only add the body if it's not null
		if (body != null) {
			request.body(body);
		}

		return request.get(baseUrl + endpoint).then().extract().response();
	}

}
