import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.AddPlace;
import pojo.Location;
import static io.restassured.RestAssured.*;
import java.util.ArrayList;
import java.util.List;

public class ReqResSpecBuilderTest {
	
	public static void main(String args[]) {
		
		RestAssured.baseURI="https://rahulshettyacademy.com";
		
		AddPlace ap = new AddPlace();
		
		Location l = new Location();
		l.setLat(-38.383494);
		l.setLng(33.427362);
		ap.setLocation(l);
		
		ap.setAccuracy(50);
		ap.setName("Frontline house");
		ap.setPhone_number("(+91) 983 893 3937");
		ap.setAddress("29, side layout, cohen 09");
		List<String> myList = new ArrayList<String>();
		myList.add("shoe park");
		myList.add("shop");
		ap.setTypes(myList);
		ap.setWebsite("http://google.com");
		ap.setLanguage("French-IN");
		
		RequestSpecification req=new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addQueryParam("key", "qaclick123").build();
		
		ResponseSpecification res = new ResponseSpecBuilder().expectStatusCode(200).build();
				
		
		RequestSpecification response1 = given().spec(req).log().all().header("Content-Type","application/json")
		.body(ap);
		
		
		String response = response1.when().post("/maps/api/place/add/json")
		.then().spec(res).log().all().extract().response().asString();
		
		System.out.println(response);
		
	}

}
