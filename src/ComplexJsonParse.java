import org.testng.Assert;

import files.Common;
import files.Payloads;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {
	
	public static void main(String [] args) {
		int sumCoursePrices = 0;
		JsonPath js = Common.rawToJson(Payloads.coursePrice());
		
		//print total course count
		int courseCount = js.getInt("courses.size()");
		System.out.println("course count is " +courseCount);
		
		//print purchase amount
		int purchaseAmount = js.getInt("dashboard.purchaseAmount");
		System.out.println("purchase amount is " +purchaseAmount);
		
		//print first title of courses
		String firstTitle = js.getString("courses[0].title");
		System.out.println("course first title is " +firstTitle);
		
		//print all courses title and prices
		for (int i=0;i<courseCount;i++) {
			System.out.println(js.getString("courses["+i+"].title"));
			System.out.println(js.getString("courses["+i+"].price"));
		}
		
		//Print no of copies sold by RPA Course
		for (int i=0;i<courseCount;i++) {
			if(js.getString("courses["+i+"].title").equalsIgnoreCase("RPA")){
				System.out.println(js.getString("courses["+i+"].copies"));
				break;
			}
		}
		
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
