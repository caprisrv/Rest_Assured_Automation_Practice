import files.Common;
import files.Payloads;
import io.restassured.path.json.JsonPath;

public class ComplexJiraJsonParse {

	public static void main(String[] args) {
		JsonPath js = Common.rawToJson(Payloads.getIssue());
		
		int commentsCount = js.getInt("fields.comment.comments.size()");
		int commentsID = js.getInt("fields.comment.comments[0].id");
		String commentsBody = js.getString("fields.comment.comments[0].body");
		System.out.println(commentsCount);
		System.out.println(commentsID);
		System.out.println(commentsBody);
		
		
		
		  for(int i=0;i<commentsCount;i++) {
		  if(js.getInt("fields.comment.comments["+i+"].id")==commentsID) {
			  
		  
		  }
		  
		  }
		 
		 

	}

}
