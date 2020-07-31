package com.restassured.cucumber;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.json.simple.JSONObject;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class StepDefinitions {

	public RequestSpecification requestSpecification;
	public Response response;
	public Properties p;
	
	@Given("^I will connect to \"([^\"]*)\" environment$")
	public void i_will_connect_to_environment(String env) {
		System.out.println("I am connected to " + env + " environment");
		init(env);
		System.out.println(p.getProperty("base_url")+" "+p.getProperty("path_param"));
		RestAssured.baseURI=p.getProperty("base_url");
		RestAssured.basePath=p.getProperty("path_param");
		System.out.println("Url for post ="+(RestAssured.baseURI+RestAssured.basePath));
		requestSpecification = RestAssured.given();
	}

	@Given("^I will use the below data$")
	public void i_will_use_the_below_data(DataTable dataTable) {
		Map<String, String> dataMap = dataTable.asMap(String.class, String.class);
		System.out.println("data ma11p=" + dataMap.toString());
		JSONObject json = new JSONObject();
		json.putAll(dataMap);
		requestSpecification.headers("content-Type", "application/json");
		//requestSpecification.post("/payments/payment");
		requestSpecification.body(json.toJSONString());
		System.out.println("=============="+requestSpecification.toString());
		System.out.println("=============="+json.toJSONString());
	}

	@When("^I will make \"([^\"]*)\" call to \"([^\"]*)\"$")
	public void i_will_make_call_to(String callType, String url) {
		System.out.println("inside post call method");
		if(callType.equalsIgnoreCase("post"))
		{
			System.out.println("inside post condition");
			System.out.println("This is post request");
			response = requestSpecification.request(Method.POST,url);
			System.out.println("path123="+response.jsonPath().get("path"));
			System.out.println("reqyeeeeeeeeeeee="+requestSpecification.toString());
		}
	}

	@Then("^will get below output$")
	public void will_get_below_output(io.cucumber.datatable.DataTable dataTable) {
		Map<String,String> exp_response = dataTable.asMap(String.class, String.class);
		System.out.println("Expected Response="+exp_response);
		System.out.println("Response ="+response.getBody().asString());
		System.out.println("path="+response.jsonPath().get("path"));
		assertEquals(response.jsonPath().get("status"),exp_response.get("statusCode"));
	}

	private void init(String env) {
		try {
			FileInputStream fis = new FileInputStream(
					System.getProperty("user.dir") + "\\src\\main\\resources\\" + env + ".properties");
			if (p == null) {
				p = new Properties();
				p.load(fis);
				System.out.println("File loaded successfully");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
