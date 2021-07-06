import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;
import files.Common;
import files.Payloads;

public class Basics {
	
	public static String place_id;
	public static String newAddress;
	
	public static void main(String[] args) {
		
		RestAssured.baseURI="https://rahulshettyacademy.com";
		
		//Add Place
		//content of the file to string -> content of the file can convert in to byte -> convert byte in to string
		String response = null;
		try {
			response = given()
					.log().all().queryParam("key", "qaclick123").header("content-type","application/json")
					.body(new String (Files.readAllBytes(Paths.get("src\\staticPayloads\\addPlace.json"))))
					.when().post("maps/api/place/add/json")
					.then().log().all()
					.assertThat().statusCode(200)
					.body("scope", equalTo("APP"))
					.body("status", equalTo("OK"))
					.header("Server", "Apache/2.4.18 (Ubuntu)")
					.extract().response().asString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JsonPath js = Common.rawToJson(response);
		place_id=js.getString("place_id");
		
		
		
		System.out.println(place_id);
		
		//Update Place
		newAddress="70 winter walk, USA";
		given().log().all()
		.queryParam("key", "qaclick123")
		.header("content-type","application/json")
		.body(Payloads.updatePlace(place_id, newAddress))
		.when().put("maps/api/place/update/json")
		.then().log().all()
		.assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		//Get Place
		String getResponse=given().log().all()
		.queryParam("key", "qaclick123")
		.queryParam("place_id", place_id)
		.when().get("maps/api/place/get/json")
		.then().log().all()
		.assertThat().statusCode(200)
		.extract().response().asString();
		
		JsonPath js1 = Common.rawToJson(getResponse);
		String actualAddress=js1.getString("address");
		System.out.println("Actual Address is "+actualAddress);
		System.out.println("Expected Address is "+newAddress);
		
		Assert.assertEquals(actualAddress, newAddress);
		
		
	}

}
