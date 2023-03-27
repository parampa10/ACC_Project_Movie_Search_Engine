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

    // A list of movies to store data read from an Excel file
    private List<Map<String, String>> movies = new ArrayList<>();

    // A method to load movies from an Excel file
    public void loadMoviesFromExcel(String filePath) throws IOException {
        FileInputStream inputStream = new FileInputStream(new File(filePath));
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        // Read data from each row in the sheet and add it to the movies list
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

    // A method to search for movies based on a keyword
    public List<String> searchMoviesByTitle(String keyword) {
        List<String> matchingTitles = new ArrayList<>();
        // Check if each movie's title contains the keyword and add it to the matchingTitles list if it does
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
        // Load movies from an Excel file
        engine.loadMoviesFromExcel("src/movies_ex.xlsx");

        while (true) {
            Scanner scanner = new Scanner(System.in);

            System.out.println("_______________________________________________________");
            System.out.print("Enter Movie keyword or Enter \"exit\" to exit the feature\n");
            System.out.println("Enter: ");
            String keyword = scanner.nextLine();

            // Exit the program if the user enters "exit"
            if (keyword.toLowerCase().equals("exit")) {
                System.out.println("_______________________________________________________");
                return;
            }

            // Search for movies based on the user's input
            List<String> matchingTitles = engine.searchMoviesByTitle(keyword);

            if (matchingTitles.isEmpty()) {
                System.out.println("No matching movies found.");
            } else {
                System.out.println("Matching titles:");
                // Print the titles of the matching movies
                for (String title : matchingTitles) {
                    System.out.println(title);
                }
            }
        }
    }
}
