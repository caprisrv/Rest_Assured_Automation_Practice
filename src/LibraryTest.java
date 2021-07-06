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

public class LibraryTest {
	
	public static String place_id;
	public static String newAddress;
	
	public static void main(String[] args) throws IOException {
		
		RestAssured.baseURI="https://rahulshettyacademy.com";
		
		String response = given().log().all().header("Content-Type","application/json")
		.body(Payloads.addBookHashMap())
		.when().post("/Library/Addbook.php").then().log().all()
		.assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js = Common.rawToJson(response);
		String msg = js.getString("Msg");
		String ID = js.getString("ID");
		System.out.println(msg+" and "+ID);
		
		
	}

}
