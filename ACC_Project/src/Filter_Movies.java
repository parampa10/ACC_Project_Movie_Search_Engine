import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class Filter_Movies {

    private Map<String, Map<String, String>> movies = new HashMap<>();

    public void loadMoviesFromExcel(String filePath) throws IOException {
        FileInputStream inputStream = new FileInputStream(new File(filePath));
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            Map<String, String> movieDetails = new HashMap<>();
            Cell titleCell = row.getCell(0);
            Cell yearCell = row.getCell(1);
            Cell directorCell = row.getCell(3);
            Cell genreCell = row.getCell(2);
            String title = titleCell.getStringCellValue();
            movieDetails.put("year", yearCell.getStringCellValue());
            movieDetails.put("director", directorCell.getStringCellValue());
            movieDetails.put("genre", genreCell.getStringCellValue());
            movies.put(title, movieDetails);
        }

        workbook.close();
        inputStream.close();
    }

    public Map<String, Map<String, String>> filterMovies(String filterType, String filterValue) {
        Map<String, Map<String, String>> filteredMovies = new HashMap<>();
        for (Map.Entry<String, Map<String, String>> entry : movies.entrySet()) {
            Map<String, String> movie = entry.getValue();
            if (filterType.equals("genre")) {
                String[] genres = movie.get("genre").split(" ");
                for (String genre : genres) {
                    if (genre.equalsIgnoreCase(filterValue)) {
                        filteredMovies.put(entry.getKey(), movie);
                        break;
                    }
                }
            } else if (filterType.equals("year")) {
                String yearStr = movie.get("year");
                if (yearStr.matches("\\d+")) { // check if year string is a number
                	
                    int movieYear = Integer.parseInt(yearStr);
                    if (movieYear == Integer.parseInt(filterValue)) {
                        filteredMovies.put(entry.getKey(), movie);
                    }
                }
            } else if (filterType.equals("director")) {
                if (movie.get("director").equals(filterValue)) {
                    filteredMovies.put(entry.getKey(), movie);
                }
            }
        }
        return filteredMovies;
    }

    public static void main(String[] args) throws IOException {
        Filter_Movies engine = new Filter_Movies();
        engine.loadMoviesFromExcel("src/movies_ex.xlsx");
        
        
        while(true) {
        	
        	
        	
        	
        	System.out.println("_______________________________________________________");
        	Scanner scanner = new Scanner(System.in);
            System.out.println("Choice of Filters:");
            System.out.println("1. Year, 2. Director, 3. Genre, 4. Exit-Feature");
            System.out.print("Enter:");
            String filterType = scanner.nextLine();
            if(filterType.equals("4")) {
            	System.out.println("_______________________________________________________\n");
				return;
            	
            }
            System.out.print("Enter filter value:");
            String filterValue = scanner.nextLine();

            Map<String, Map<String, String>> filteredMovies;

            switch (filterType) {
                case "1":
                    filteredMovies = engine.filterMovies("year", filterValue);
                    break;
                case "2":
                    filteredMovies = engine.filterMovies("director", filterValue);
                    break;
                case "3":
                    filteredMovies = engine.filterMovies("genre", filterValue);
                    break;
                case "4":
                	System.out.println("_______________________________________________________\n");
    				return;
                default:
                    System.out.println("Invalid filter type.");
                    return;
            }

            if (filteredMovies.isEmpty()) {
                System.out.println("No movies found.");
            } else {
                System.out.println("Filtered movies:");
                for (Map.Entry<String, Map<String, String>> entry : filteredMovies.entrySet()) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
            }
        }
        
        
    }

}

