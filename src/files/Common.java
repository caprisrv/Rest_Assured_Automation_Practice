package files;

import io.restassured.path.json.JsonPath;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Common {
	
	public static JsonPath rawToJson(String response) {
		JsonPath js = new JsonPath(response);
		return js;
	}
	
	public static ArrayList<String> getDataFromExcel(String filePath,String sheetName,String columnName,String rowName) throws IOException {
		
		ArrayList<String> a=new ArrayList<String>();
		FileInputStream fis = new FileInputStream(filePath);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		
		int sheetCount = workbook.getNumberOfSheets();
		
		for(int i=0;i<sheetCount;i++) {
			if(workbook.getSheetName(i).equalsIgnoreCase(sheetName)) {
				XSSFSheet sheet = workbook.getSheetAt(i);
				System.out.println(sheet);
				//Identify Testcases coloum by scanning the entire 1st row
				Iterator<Row> rows = sheet.iterator();// sheet is collection of rows
				Row firstrow=rows.next();
				
				Iterator<Cell> cell = firstrow.cellIterator();//row is collection of cells
				int k=0;
				int column=0;
				while(cell.hasNext()) {
					Cell value=cell.next();
					if(value.getStringCellValue().equalsIgnoreCase(columnName)) {
						column=k;
					}
					k++;			
				}
				
				System.out.println(column);
				
			////once coloumn is identified then scan entire testcase coloum to identify purchase testcase row
				
				while(rows.hasNext()) {
					Row r = rows.next();
					if(r.getCell(column).getStringCellValue().equalsIgnoreCase(rowName)) {
					////after you grab purchase testcase row = pull all the data of that row and feed into test
						Iterator<Cell> cv=r.cellIterator();
						
						while(cv.hasNext()) {
							Cell c = cv.next();
							if(c.getCellType()==CellType.STRING) {
							a.add(c.getStringCellValue());
							}
							else {
								
								a.add(NumberToTextConverter.toText(c.getNumericCellValue()));
							}
						}
						
					}
				}
				
			}
		}
		
		return a;

		}


}
