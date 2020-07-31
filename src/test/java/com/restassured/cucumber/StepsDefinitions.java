package com.restassured.cucumber;

import java.util.List;
import java.util.Map;

import org.junit.Assert;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class StepsDefinitions {
	
	private static final String BASE_URL = "http://localhost:8088";
	private static Response response;
	private static String jsonString;



	@Given("^A list of books are available$")
	public void listOfBooksAreAvailable() {
		RestAssured.baseURI = BASE_URL;
		RequestSpecification request = RestAssured.given();
		response = request.get("/emp/employees");

		System.out.println("responseof get="+response);
		jsonString = response.asString();
		System.out.println("get methosd= "+jsonString);
		List<Map<String, String>> employee = JsonPath.from(jsonString).get("employee");
		Assert.assertTrue(employee.size() > 0);
		//employeeId = employee.get(0).get("id");	   
	}

	@When("^I add a book to my reading list$")
	public void addBookInList() {
		RestAssured.baseURI = BASE_URL;
		RequestSpecification request = RestAssured.given();
		//request.header("Authorization", "Bearer " + token)
		request.header("Content-Type", "application/json");

		response = request.body("{\n" +
	            "  \"name\": \"shadab\",\n" +
	            "  \"deptname\": \"testing\"\n" +
	            "}")
				.post("/emp/create");
		Assert.assertEquals(201, response.getStatusCode());
	}

	@Then("^The book is added$")
	public void bookIsAdded() {
		Assert.assertEquals(201, response.getStatusCode());
	}
}
