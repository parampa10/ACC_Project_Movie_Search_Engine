// Import libraries
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Advance Computing Concept - Final Project (Group 7).
 * @author Ritul Kalpeshkumar Patel (SID: 110100418)
 */

public class SearchMovies {
	private TrieNode root;

	// Constructor that builds the trie from a given sheet
	public SearchMovies(Sheet sheet) {
		root = new TrieNode();
		buildTrie(sheet);
	}

	// Method to build the trie from the given sheet
	private void buildTrie(Sheet sheet) {
		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			// Get title, year, genre, director, cast, and rating from the current row
			String title = row.getCell(0).getStringCellValue().toLowerCase();
			String year = row.getCell(1).getStringCellValue();
			String genre = row.getCell(2).getStringCellValue();
			String director = row.getCell(3).getStringCellValue();
			String cast = row.getCell(4).getStringCellValue();
			String rating = row.getCell(5).getStringCellValue();
			// Insert title and its attributes into the trie
			insertTitle(title, year, genre, director, cast, rating);
		}
	}

	// Method to insert a title and its attributes into the trie
	private void insertTitle(String title, String year, String genre, String director, String cast, String rating) {
		TrieNode current = root;
		// Iterate through each character in the title and add it to the trie
		for (char c : title.toCharArray()) {
			if (!current.children.containsKey(c)) {
				current.children.put(c, new TrieNode());
			}
			current = current.children.get(c);
		}
		current.isEndOfWord = true;
		current.title = title;
		current.year = year;
		current.genre = genre;
		current.director = director;
		current.cast = cast;
		current.rating = rating;
	}

	// Method to print the attributes of a movie
	public boolean printMovieAttributes(String title) {
		TrieNode current = root;
		// Iterate through each character in the title and search for the movie in the trie
		for (char c : title.toCharArray()) {
			if (!current.children.containsKey(c)) {
				System.out.println("\nMovie not found with exact title");
				return false;
			}
			current = current.children.get(c);
		}

		if (!current.isEndOfWord) {
			System.out.println("\nMovie not found with exact title");
			return false;
		}

		// If the movie is found, print its attributes
		System.out.println("\nTitle: " + title);
		System.out.println("Year: " + current.year);
		System.out.println("Genre: " + current.genre);
		System.out.println("Director: " + current.director);
		System.out.println("Cast: " + current.cast);
		System.out.println("Rating: " + current.rating);
		return true;
	}

	// Helper method to print the attributes of all movies that match a pattern
	private void printMatchingMovieAttributesHelper(String pattern, TrieNode node) {
		
		if (node.isEndOfWord && node.title.contains(pattern)) {
			System.out.println("\nTitle: " + node.title);
			System.out.println("Year: " + node.year);
			System.out.println("Genre: " + node.genre);
			System.out.println("Director: " + node.director);
			System.out.println("Cast: " + node.cast);
			System.out.println("Rating: " + node.rating);
			
		}
		// Recursively search for movies

		for (char c : node.children.keySet()) {
			printMatchingMovieAttributesHelper(pattern, node.children.get(c));
		}
	}

	// Method to print the attributes of all movies that match a pattern
	public void printMatchingMovieAttributes(String pattern) {
		printMatchingMovieAttributesHelper(pattern, root);
	}

	/**
    Represents a node in the trie data structure for movie search.
	 */
	private static class TrieNode {
		Map<Character, TrieNode> children; // map of child nodes
		boolean isEndOfWord; // flag to indicate end of a word (movie title)
		String title; // movie title
		String year; // year of movie release
		String genre; // genre of the movie
		String director; // director of the movie
		String cast; // cast of the movie
		String rating; // rating of the movie

		/**
	    Constructor for creating a new TrieNode.
	    Initializes the children map and sets default values for the fields.
		 */
		TrieNode() {
			children = new HashMap<>();
			isEndOfWord = false;
			year = "";
			genre = "";
			director = "";
			cast = "";
			rating = "";
		}
	}

	public static void main(String[] args) {
		// Initialize variables
		String fileName = "src/movies_ex.xlsx"; // File name
		String sheetName = "Movies"; // Sheet name
		double startTime, endTime; // Variables to store start and end times
		boolean found; // Boolean variable to indicate if movie was found
		

		//infinte loop until user ends it
		while(true) {
			
			Scanner scanner = new Scanner(System.in); // Create a scanner object to read user input
		
			// Prompt user to input a movie title
			System.out.println("_______________________________________________________");
			System.out.print("Search a Movie or Enter \"exit\" to exit the feature\n");
			System.out.print("Enter:");
			String inputTitle = scanner.nextLine().toLowerCase();
			
			//exiting feature
			if(inputTitle.toLowerCase().equals("exit")) {
				System.out.println("_______________________________________________________\n");
				return;
			}
	
			try (FileInputStream fileInputStream = new FileInputStream(new File(fileName)); // Create a file input stream
					Workbook workbook = new XSSFWorkbook(fileInputStream)) { // Create a workbook object from the file input stream
				Sheet sheet = workbook.getSheet(sheetName); // Get the sheet from the workbook
				SearchMovies searchMovies = new SearchMovies(sheet); // Create a new SearchMovies object and pass in the sheet
	
				// Search for the movie attributes
				startTime = System.nanoTime(); // Record the start time
				found = searchMovies.printMovieAttributes(inputTitle); // Call the printMovieAttributes method and store the result
				endTime = System.nanoTime(); // Record the end time
	//			System.out.println(endTime - startTime); // Print the elapsed time
	
				// If movie is not found, search for matching movies
				if (!found) {
					startTime = System.nanoTime(); // Record the start time
					searchMovies.printMatchingMovieAttributes(inputTitle); // Call the printMatchingMovieAttributes method
					endTime = System.nanoTime(); // Record the end time
	//				System.out.println(endTime - startTime); // Print the elapsed time
				}
			} catch (IOException e) {
				e.printStackTrace(); // Print the stack trace if an exception is caught
			}
		}
	}

}

