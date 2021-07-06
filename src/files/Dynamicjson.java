package files;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import files.Common;
import files.Payloads;

public class Dynamicjson {
	
	@Test(dataProvider="BookData")
	public void addBook(String isbn,String aisle) {
		
		RestAssured.baseURI="http://216.10.245.166";
		
		String response = given().log().all().header("content-type","application/json")
				.body(Payloads.addBook(isbn,aisle))
				.when().post("Library/Addbook.php")
				.then().log().all().assertThat().statusCode(200)
				.extract().response().asString();
		
		JsonPath js = Common.rawToJson(response);
		String id = js.getString("ID");
		System.out.println(id);
	}
	
	@DataProvider(name="BookData")
	public Object[][] bookData() {
		return new Object[][] {{"srv","125"},{"srv","126"},{"srv","127"}};
	}

}
