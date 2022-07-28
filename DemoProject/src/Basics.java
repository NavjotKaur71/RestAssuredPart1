import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;

import files.ReUsableMethods;
import files.payload;

public class Basics {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
// validate if Add Place API is workimg as expected 
		//Add place-> Update Place with New Address -> Get Place to validate if New address is present in response
		
		//given - all input details -  log all, query param, headers, body.
		//when - Submit the API - http method, resource(end point)
		//Then - validate the response assertthat, status code, headers, body(payload), extract response as string and store in a response string variable
		RestAssured.baseURI= "https://rahulshettyacademy.com";
		String response=given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body(payload.AddPlace()).when().post("maps/api/place/add/json")
		.then().assertThat().statusCode(200).body("scope", equalTo("APP"))
		.header("server", "Apache/2.4.18 (Ubuntu)").extract().response().asString();

		System.out.println(response);
		JsonPath js=new JsonPath(response); //for parsing Json - converts string to json so that we can check the key value pairs
		String placeId=js.getString("place_id");// get the value of a key "place_id"
		
		System.out.println(placeId);
		
		//Update Place - put method
		String newAddress = "Summer Walk, Africa";
		
		given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body("{\r\n" + 
				"\"place_id\":\""+placeId+"\",\r\n" + 
				"\"address\":\""+newAddress+"\",\r\n" + 
				"\"key\":\"qaclick123\"\r\n" + 
				"}").
		when().put("maps/api/place/update/json")
		.then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		//Get Place - get method - no body needed to send with request
		
	String getPlaceResponse=	given().log().all().queryParam("key", "qaclick123")
		.queryParam("place_id",placeId)
		.when().get("maps/api/place/get/json")
		.then().assertThat().log().all().statusCode(200).extract().response().asString();
	JsonPath js1=ReUsableMethods.rawToJson(getPlaceResponse);
	String actualAddress =js1.getString("address");
	System.out.println(actualAddress);
	Assert.assertEquals(actualAddress, "Pacific ocean");
	//Cucumber Junit, Testng
	
		
	}

}
