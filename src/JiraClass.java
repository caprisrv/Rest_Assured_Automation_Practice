import org.testng.Assert;
import org.testng.annotations.Test;

import files.Common;
import files.Payloads;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;

import java.io.File;

import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

public class JiraClass {
	
	@Test
	public static void jiraAPIsTest() {
		
		RestAssured.baseURI="http://localhost:8080";
		SessionFilter session = new SessionFilter();
		
		//Get Session ID
		System.out.println("***************Get Session ID API Started*********************************");
		
		given().relaxedHTTPSValidation().log().all().header("Content-Type","application/json")
		.body("{ \r\n"
				+ "    \"username\": \"saurabh.r.gupta\", \r\n"
				+ "    \"password\": \"caprisrv@123\" \r\n"
				+ "}").filter(session)
		.when().post("/rest/auth/1/session")
		.then().log().all().assertThat().statusCode(200);
		
		System.out.println("***************Get Session ID API Completed*********************************");
		
		
		//Create an Issue
		System.out.println("***************Create Issue API Started*********************************");
		
		String response = given().log().all().header("Content-Type","application/json")
		.body(Payloads.createIssue()).filter(session)
		.when().post("/rest/api/2/issue")
		.then().log().all().assertThat().statusCode(201).extract().response().asString();
		
		JsonPath js = Common.rawToJson(response);
		String key = js.getString("key");
		
		System.out.println("***************Create Issue API Completed*********************************");
		
		//Add a Comment in an issue
		System.out.println("***************Add Comment API Started*********************************");
		String comment="Adding a comment in an issue.";
		String response1=given().log().all().pathParam("defectID",key).header("Content-Type","application/json")
		.body(Payloads.addCommentMap(comment)).filter(session)
		.when().post("/rest/api/2/issue/{defectID}/comment")
		.then().log().all().assertThat().statusCode(201).extract().response().asString();
		
		JsonPath js1 = Common.rawToJson(response1);
		String commentID = js1.getString("id");
		
		System.out.println("***************Add Comment API Completed*********************************");
		
		//Add an attachment to existing issue using a Add Attachment API
		System.out.println("***************Add attachment API Started*********************************");
		
		given().log().all().header("Atlassian-Token","no-check").filter(session).pathParam("defectID",key)
		.header("Content-Type","multipart/form-data")
		.multiPart("file",new File("jira.txt"))
		.when().post("/rest/api/2/issue/{defectID}/attachments")
		.then().log().all().assertThat().statusCode(404);
		
		System.out.println("***************Add attachment API Completed*********************************");
		
		//Get Issue Details
		String response2=given().log().all().filter(session).pathParam("defectID",key)
		.when().get("/rest/api/2/issue/{defectID}")
		.then().log().all().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js2 = Common.rawToJson(response2);
		
		int commentsCount = js2.getInt("fields.comment.comments.size()");
		
		
		  for(int i=0;i<commentsCount;i++) {
		  if(js.getString("fields.comment.comments["+i+"].id")==commentID) {
			  String commentsBody = js.getString("fields.comment.comments["+i+"].body");
			  Assert.assertEquals(comment, commentsBody);
			  break;
		  }
		  
		  }
	}
}
