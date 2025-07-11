package testcases;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import rest.CustomResponse;

public class TestCodeValidator {
	// Method to validate if specific keywords are used in the method's source code
	public static boolean validateTestMethodFromFile(String filePath, String methodName, List<String> keywords)
			throws IOException {
		// Read the content of the test class file
		String fileContent = new String(Files.readAllBytes(Paths.get(filePath)));
		// Extract the method body for the specified method using regex
		String methodRegex = "(public\\s+CustomResponse\\s+" + methodName + "\\s*\\(.*?\\)\\s*\\{)([\\s\\S]*?)}";
		Pattern methodPattern = Pattern.compile(methodRegex);
		Matcher methodMatcher = methodPattern.matcher(fileContent);
		if (methodMatcher.find()) {
			String methodBody = fetchBody(filePath, methodName);
			// Now we validate the method body for the required keywords
			boolean allKeywordsPresent = true;
			// Loop over the provided keywords and check if each one is present in the
			// method body
			for (String keyword : keywords) {
				Pattern keywordPattern = Pattern.compile("\\b" + keyword + "\\s*\\(");
				if (!keywordPattern.matcher(methodBody).find()) {
					System.out.println("'" + keyword + "()' is missing in the method.");
					allKeywordsPresent = false;
				}
			}
			return allKeywordsPresent;
		} else {
			System.out.println("Method " + methodName + " not found in the file.");
			return false;
		}
	}

	public static String fetchBody(String filePath, String methodName) {
		StringBuilder methodBody = new StringBuilder();
		boolean methodFound = false;
		boolean inMethodBody = false;
		int openBracesCount = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				// Check if the method is found by matching method signature
				if (line.contains("public CustomResponse " + methodName + "(")
						|| line.contains("public String " + methodName + "(")) {
					methodFound = true;
				}
				// Once the method is found, start capturing lines
				if (methodFound) {
					if (line.contains("{")) {
						inMethodBody = true;
						openBracesCount++;
					}
					// Capture the method body
					if (inMethodBody) {
						methodBody.append(line).append("\n");
					}
					// Check for closing braces to identify the end of the method
					if (line.contains("}")) {
						openBracesCount--;
						if (openBracesCount == 0) {
							break; // End of method body
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return methodBody.toString();
	}

	public static boolean validateResponseFields(String methodName, CustomResponse customResponse) {
	    boolean isValid = true;

	    switch (methodName) {
	        case "getSubunitsTree":
	            List<Map<String, Object>> SubunitList = customResponse.getResponse().jsonPath().getList("data");

	            if (SubunitList == null || SubunitList.isEmpty()) {
	                System.out.println("'data' section is missing or empty in the response.");
	                return false;
	            }

	            List<String> allFieldsToCheck = List.of("response", "statusCode", "status", "ids", "unitIds", "names", "levels", "children");
	            List<String> requiredFields = List.of("id", "name");

	            for (int i = 1; i < SubunitList.size(); i++) {
	                Map<String, Object> holiday = SubunitList.get(i);

	                // Step 1: Check for required fields (id, name)
	                for (String field : requiredFields) {
	                    if (!holiday.containsKey(field)) {
	                        System.out.println("❌ Missing required field '" + field + "' in holiday at index " + i);
	                        isValid = false;
	                    } else {
	                        Object value = holiday.get(field);
	                        if (value == null || value.toString().trim().isEmpty()) {
	                            System.out.println("⚠️ Required field '" + field + "' is null or empty in holiday at index " + i);
	                            isValid = false;
	                        }
	                    }
	                }

	                // Step 2: Check presence of all fields
	                for (String field : allFieldsToCheck) {
	                    if (!holiday.containsKey(field)) {
	                        System.out.println("🔍 Field '" + field + "' is missing in holiday at index " + i);
	                        isValid = false;
	                    }
	                }
	            }

	            break;
	            
	        case "getAdminSkills":
	            List<Map<String, Object>> SkillList = customResponse.getResponse().jsonPath().getList("data");

	            if (SkillList == null || SkillList.isEmpty()) {
	                System.out.println("'data' section is missing or empty in the response.");
	                return false;
	            }

	            // ✅ Only these fields should be present per API
	            List<String> SkillFieldsToCheck = List.of("id", "name", "description");

	            // ✅ These fields must not be null or empty
	            List<String> requiredSkillFields = List.of("id", "name");

	            for (int i = 0; i < SkillList.size(); i++) {
	                Map<String, Object> skill = SkillList.get(i);

	                // Step 1: Ensure no unexpected fields
	                for (String key : skill.keySet()) {
	                    if (!SkillFieldsToCheck.contains(key)) {
	                        System.out.println("❌ Unexpected field '" + key + "' found in skill at index " + i);
	                        isValid = false;
	                    }
	                }

	                // Step 2: Ensure all expected fields are present
	                for (String expectedField : SkillFieldsToCheck) {
	                    if (!skill.containsKey(expectedField)) {
	                        System.out.println("❌ Expected field '" + expectedField + "' is missing in skill at index " + i);
	                        isValid = false;
	                    }
	                }

	                // Step 3: Validate required fields are not null or empty
	                for (String field : requiredSkillFields) {
	                    Object value = skill.get(field);
	                    if (value == null || value.toString().trim().isEmpty()) {
	                        System.out.println("⚠️ Required field '" + field + "' is null or empty in skill at index " + i);
	                        isValid = false;
	                    }
	                }
	            }

	            break;

	            
	            
	        case "getAdminEdu":
	            List<Map<String, Object>> AdminEduList = customResponse.getResponse().jsonPath().getList("data");

	            if (AdminEduList == null || AdminEduList.isEmpty()) {
	                System.out.println("'data' section is missing or empty in the response.");
	                return false;
	            }

	            // ✅ Only these fields should be present per API
	            List<String> AdminEduFieldsToCheck = List.of("id", "name");

	            // ✅ These fields must not be null or empty
	            List<String> requiredAdminEduFields = List.of("id", "name");

	            for (int i = 0; i < AdminEduList.size(); i++) {
	                Map<String, Object> edu = AdminEduList.get(i);

	                // Step 1: Ensure no unexpected fields
	                for (String key : edu.keySet()) {
	                    if (!AdminEduFieldsToCheck.contains(key)) {
	                        System.out.println("❌ Unexpected field '" + key + "' found in education at index " + i);
	                        isValid = false;
	                    }
	                }

	                // Step 2: Ensure all expected fields are present
	                for (String expectedField : AdminEduFieldsToCheck) {
	                    if (!edu.containsKey(expectedField)) {
	                        System.out.println("❌ Expected field '" + expectedField + "' is missing in education at index " + i);
	                        isValid = false;
	                    }
	                }

	                // Step 3: Validate required fields are not null or empty
	                for (String field : requiredAdminEduFields) {
	                    Object value = edu.get(field);
	                    if (value == null || value.toString().trim().isEmpty()) {
	                        System.out.println("⚠️ Required field '" + field + "' is null or empty in education at index " + i);
	                        isValid = false;
	                    }
	                }
	            }

	            break;
	            
	        case "getAdminLicense":
	            List<Map<String, Object>> licenseList = customResponse.getResponse().jsonPath().getList("data");

	            if (licenseList == null || licenseList.isEmpty()) {
	                System.out.println("'data' section is missing or empty in the response.");
	                return false;
	            }

	            // ✅ Only allowed fields as per API
	            List<String> licenseFieldsToCheck = List.of("id", "name");

	            // ✅ These fields must NOT be null/empty
	            List<String> requiredLicenseFields = List.of("id", "name");

	            for (int i = 0; i < licenseList.size(); i++) {
	                Map<String, Object> license = licenseList.get(i);

	                // Step 1: Check no extra fields exist
	                for (String key : license.keySet()) {
	                    if (!licenseFieldsToCheck.contains(key)) {
	                        System.out.println("❌ Unexpected field '" + key + "' found in license at index " + i);
	                        isValid = false;
	                    }
	                }

	                // Step 2: Ensure all expected fields are present
	                for (String expectedField : licenseFieldsToCheck) {
	                    if (!license.containsKey(expectedField)) {
	                        System.out.println("❌ Expected field '" + expectedField + "' is missing in license at index " + i);
	                        isValid = false;
	                    }
	                }

	                // Step 3: Validate required fields (not null or empty)
	                for (String field : requiredLicenseFields) {
	                    Object value = license.get(field);
	                    if (value == null || value.toString().trim().isEmpty()) {
	                        System.out.println("⚠️ Required field '" + field + "' is null or empty in license at index " + i);
	                        isValid = false;
	                    }
	                }
	            }

	            break;
	            
	        case "getAdminLanguages":
	            List<Map<String, Object>> LanguagesList = customResponse.getResponse().jsonPath().getList("data");

	            if (LanguagesList == null || LanguagesList.isEmpty()) {
	                System.out.println("'data' section is missing or empty in the response.");
	                return false;
	            }

	            // ✅ Only allowed fields as per API
	            List<String> LanguagesFieldsToCheck = List.of("id", "name");

	            // ✅ These fields must NOT be null/empty
	            List<String> requiredLanguagesFields = List.of("id", "name");

	            for (int i = 0; i < LanguagesList.size(); i++) {
	                Map<String, Object> license = LanguagesList.get(i);

	                // Step 1: Check no extra fields exist
	                for (String key : license.keySet()) {
	                    if (!LanguagesFieldsToCheck.contains(key)) {
	                        System.out.println("❌ Unexpected field '" + key + "' found in license at index " + i);
	                        isValid = false;
	                    }
	                }

	                // Step 2: Ensure all expected fields are present
	                for (String expectedField : LanguagesFieldsToCheck) {
	                    if (!license.containsKey(expectedField)) {
	                        System.out.println("❌ Expected field '" + expectedField + "' is missing in license at index " + i);
	                        isValid = false;
	                    }
	                }

	                // Step 3: Validate required fields (not null or empty)
	                for (String field : requiredLanguagesFields) {
	                    Object value = license.get(field);
	                    if (value == null || value.toString().trim().isEmpty()) {
	                        System.out.println("⚠️ Required field '" + field + "' is null or empty in license at index " + i);
	                        isValid = false;
	                    }
	                }
	            }

	            break;
	        case "deleteValidUsers":
	            List<Map<String, Object>> deleteListUsers = customResponse.getResponse().jsonPath().getList("data");

	            if (deleteListUsers == null || deleteListUsers.isEmpty()) {
	                System.out.println("'data' section is missing or empty in the response.");
	                return false;
	            }

	            // ✅ Only allowed fields as per API
	            List<String> deleteitems = List.of("id");

	            // ✅ These fields must NOT be null/empty
	            List<String> deleteItemsNotNull = List.of("id");

	            for (int i = 0; i < deleteListUsers.size(); i++) {
	                Map<String, Object> license = deleteListUsers.get(i);

	                // Step 1: Check no extra fields exist
	                for (String key : license.keySet()) {
	                    if (!deleteitems.contains(key)) {
	                        System.out.println("❌ Unexpected field '" + key + "' found in license at index " + i);
	                        isValid = false;
	                    }
	                }

	                // Step 2: Ensure all expected fields are present
	                for (String expectedField : deleteitems) {
	                    if (!license.containsKey(expectedField)) {
	                        System.out.println("❌ Expected field '" + expectedField + "' is missing in license at index " + i);
	                        isValid = false;
	                    }
	                }

	                // Step 3: Validate required fields (not null or empty)
	                for (String field : deleteItemsNotNull) {
	                    Object value = license.get(field);
	                    if (value == null || value.toString().trim().isEmpty()) {
	                        System.out.println("⚠️ Required field '" + field + "' is null or empty in license at index " + i);
	                        isValid = false;
	                    }
	                }
	            }

	            break;
	            
	        case "putJobTitleByID":
	            Map<String, Object> data = customResponse.getResponse().jsonPath().getMap("data");

	            if (data == null || data.isEmpty()) {
	                System.out.println("❌ 'data' field is missing or empty in the response.");
	                return false;
	            }

	            List<String> requiredTopLevelFields = List.of("id", "title", "description", "note", "jobSpecification");
	            List<String> requiredJobSpecFields = List.of("id", "filename", "fileType", "fileSize");

	            // Step 1: Check presence of top-level fields
	            for (String field : requiredTopLevelFields) {
	                if (!data.containsKey(field)) {
	                    System.out.println("❌ Missing field '" + field + "' in data object.");
	                    isValid = false;
	                }
	            }

	            // Step 2: Check required fields for null or empty (only 'id' and 'title' are required to be non-null)
	            Object id = data.get("id");
	            Object title = data.get("title");

	            if (id == null || id.toString().trim().isEmpty()) {
	                System.out.println("⚠️ 'id' field is null or empty.");
	                isValid = false;
	            }

	            if (title == null || title.toString().trim().isEmpty()) {
	                System.out.println("⚠️ 'title' field is null or empty.");
	                isValid = false;
	            }

	            // Step 3: Validate jobSpecification object
	            Object jobSpecObj = data.get("jobSpecification");
	            if (jobSpecObj instanceof Map) {
	                @SuppressWarnings("unchecked")
					Map<String, Object> jobSpec = (Map<String, Object>) jobSpecObj;

	                for (String field : requiredJobSpecFields) {
	                    if (!jobSpec.containsKey(field)) {
	                        System.out.println("❌ Missing field '" + field + "' in jobSpecification object.");
	                        isValid = false;
	                    }
	                }

	            } else {
	                System.out.println("⚠️ 'jobSpecification' is missing or not an object.");
	                isValid = false;
	            }

	            break;
	            
	            
	            
	        case "postUniquePaygrade":
	            Map<String, Object> data11 = customResponse.getResponse().jsonPath().getMap("data");

	            if (data11 == null || data11.isEmpty()) {
	                System.out.println("❌ 'data' field is missing or empty in the response.");
	                return false;
	            }

	            List<String> requiredFields11 = List.of("id", "name");

	            // Step 1: Check presence of required fields and ensure they are not null or empty
	            for (String field : requiredFields11) {
	                if (!data11.containsKey(field)) {
	                    System.out.println("❌ Missing required field '" + field + "' in data object.");
	                    isValid = false;
	                } else {
	                    Object value = data11.get(field);
	                    if (value == null || value.toString().trim().isEmpty()) {
	                        System.out.println("⚠️ Field '" + field + "' is null or empty.");
	                        isValid = false;
	                    }
	                }
	            }

	            break;
	            
	        case "postNewJobTitles":
	            Map<String, Object> Titledata = customResponse.getResponse().jsonPath().getMap("data");

	            if (Titledata == null || Titledata.isEmpty()) {
	                System.out.println("❌ 'data' field is missing or empty in the response.");
	                return false;
	            }

	            List<String> requiredTitleFields = List.of("title");

	            // Step 1: Check presence of required fields and ensure they are not null or empty
	            for (String field : requiredTitleFields) {
	                if (!Titledata.containsKey(field)) {
	                    System.out.println("❌ Missing required field '" + field + "' in data object.");
	                    isValid = false;
	                } else {
	                    Object value = Titledata.get(field);
	                    if (value == null || value.toString().trim().isEmpty()) {
	                        System.out.println("⚠️ Field '" + field + "' is null or empty.");
	                        isValid = false;
	                    }
	                }
	            }

	            break;
	            



	            
	//deleteValidUsers            
	        

// ✅ Add this break to prevent fallthrough

	        default:
	            System.out.println("Method " + methodName + " is not recognized for validation.");
	            isValid = false;
	            break;
	    }

	    return isValid;
	}

}
