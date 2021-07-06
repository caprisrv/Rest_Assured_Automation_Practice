import org.testng.Assert;
import org.testng.annotations.Test;

import files.Common;
import files.Payloads;
import io.restassured.path.json.JsonPath;

public class SumValidation {
	
	@Test
	public static void sumOfCourses() {
		
		int sumCoursePrices = 0;
		JsonPath js = Common.rawToJson(Payloads.coursePrice());
		
		//print total course count
		int courseCount = js.getInt("courses.size()");
		
		//Verify if Sum of all Course prices matches with Purchase Amount
		for(int i=0;i<courseCount;i++) {
			int coursePrice = js.getInt("courses["+i+"].price");
			int courseCopies = js.getInt("courses["+i+"].copies");
			sumCoursePrices = sumCoursePrices+(coursePrice*courseCopies);
			System.out.println("Total Sum of all courses prices "+sumCoursePrices);
			}
		int purchaseAmount1 = js.getInt("dashboard.purchaseAmount");
		Assert.assertEquals(sumCoursePrices, purchaseAmount1);
		
	}

}
