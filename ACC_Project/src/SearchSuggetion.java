import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class SearchSuggetion {

    private List<Map<String, String>> movies = new ArrayList<>();

    public void loadMoviesFromExcel(String filePath) throws IOException {
        FileInputStream inputStream = new FileInputStream(new File(filePath));
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            Map<String, String> movie = new HashMap<>();
            Cell titleCell = row.getCell(0);
            Cell yearCell = row.getCell(1);
            Cell directorCell = row.getCell(3);
            Cell genreCell = row.getCell(2);
            movie.put("title", titleCell.getStringCellValue());
            movie.put("year", yearCell.getStringCellValue());
            movie.put("director", directorCell.getStringCellValue());
            movie.put("genre", genreCell.getStringCellValue());
            movies.add(movie);
        }

        workbook.close();
        inputStream.close();
    }

    public List<String> searchMoviesByTitle(String keyword) {
        List<String> matchingTitles = new ArrayList<>();
        for (Map<String, String> movie : movies) {
            String title = movie.get("title");
            if (title.toLowerCase().contains(keyword.toLowerCase())) {
                matchingTitles.add(title);
            }
        }
        return matchingTitles;
    }

    public static void main(String[] args) throws IOException {
        SearchSuggetion engine = new SearchSuggetion();
        engine.loadMoviesFromExcel("src/movies_ex.xlsx");
        
        while(true) {
        	
	        Scanner scanner = new Scanner(System.in);
	        
	        System.out.println("_______________________________________________________");
        	System.out.print("Enter Movie keyword or Enter \"exit\" to exit the feature\n");
        	System.out.println("Enter: ");
	        String keyword = scanner.nextLine();
	        
	        if (keyword.toLowerCase().equals("exit")) {
            	System.out.println("_______________________________________________________");
            	return;
            }
	        
	        List<String> matchingTitles = engine.searchMoviesByTitle(keyword);
	
	        if (matchingTitles.isEmpty()) {
	            System.out.println("No matching movies found.");
	        } else {
	            System.out.println("Matching titles:");
	            for (String title : matchingTitles) {
	                System.out.println(title);
	            }
	        }
        }
    }
}
