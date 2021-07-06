import files.Common;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import pojo.API;
import pojo.Getcourses;
import pojo.WebAutomation;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;


public class OAuthTest {

	public static void main(String[] args) throws InterruptedException {
		
		String[] apiCourseTitle = {"Rest Assured Automation using Java","SoapUI Webservices testing"};
		
		//Get Code
		/*
		 * System.setProperty
		 * ("webdriver.chrome.driver","D:\\chromedriver_win32\\chromedriver.exe");
		 * WebDriver driver = new ChromeDriver(); driver.get(
		 * "https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php"
		 * ); driver.manage().window().maximize();
		 * driver.findElement(By.cssSelector("input[type='email']")).sendKeys(
		 * "eng.saurabh.bit");
		 * driver.findElement(By.cssSelector("input[type='email']")).sendKeys(Keys.ENTER
		 * ); Thread.sleep(4000);
		 * driver.findElement(By.cssSelector("input[type='password']")).sendKeys(
		 * "capri_srv@123");
		 * driver.findElement(By.cssSelector("input[type='password']")).sendKeys(Keys.
		 * ENTER); Thread.sleep(4000); String url = driver.getCurrentUrl();
		 */
		String url = "https://rahulshettyacademy.com/getCourse.php?code=4%2F0AX4XfWg2vRe-HiGvzRY2bBnCZ9FqwMuxoQ5vJjUmNFqYILMyf14y7KRHllToe2WbCU6DHg&scope=email+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&authuser=0&prompt=none";
		String partialCode = url.split("code=")[1];
		String code = partialCode.split("&scope")[0];
		System.out.println("Code is "+code);
		
		//Get Access Token
		String accessTokenResponse = given().urlEncodingEnabled(false).log().all()
		.queryParams("code",code)
		.queryParams("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		.queryParams("client_secret","erZOWM9g3UtwNRj340YYaK_W")
		.queryParams("redirect_uri","https://rahulshettyacademy.com/getCourse.php")
		.queryParams("grant_type","authorization_code")
		.when().post("https://www.googleapis.com/oauth2/v4/token")
		.then().log().all().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js = Common.rawToJson(accessTokenResponse);
		String accessToken = js.getString("access_token");
		
		//Hit Actual Request
		Getcourses gc = given().log().all().queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON)
		.when().get("https://rahulshettyacademy.com/getCourse.php")
		.then().log().all().assertThat().statusCode(200).extract().response().as(Getcourses.class);
		
		System.out.println(gc.getLinkedIn());
		System.out.println(gc.getInstructor());
		
		//Get price of Protractor course title of WebAutomation
		List<WebAutomation> wa = gc.getCourses().getWebAutomation();
		
		for(int i=0;i<wa.size();i++) {
			if(wa.get(i).getCourseTitle().equalsIgnoreCase("Protractor")) {
				Assert.assertEquals(wa.get(i).getPrice(), "40");
				break;
			}
		}
		
		//Get All Course Title and compare with expected course title mentioned above
		List<API> api=gc.getCourses().getApi();
		
		ArrayList<String> al = new ArrayList<String>();
		for(int j=0;j<api.size();j++) {
			al.add(api.get(j).getCourseTitle());
		}
		
		//Convert Array in to Array List
		List<String> ai = Arrays.asList(apiCourseTitle);
		
		Assert.assertTrue(ai.equals(al));
		
	}

}
