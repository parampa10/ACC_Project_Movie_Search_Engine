import java.io.IOException;
import java.util.Scanner;

import org.apache.poi.EncryptedDocumentException;

public class MovieSearchEngine {
	
	public static void main(String[] args) throws EncryptedDocumentException, IOException {
		
		
		
		//pre-processing web crawling which creates .csv & .xlsx files for data of (N) movies from url.
		
		//
		Scanner s = new Scanner(System.in);
		System.out.print("Enter number of movies you want in database:");
		int movie_numbers=s.nextInt();
		
		WebCrawler.main(args, movie_numbers);
		
		//user choice of feature
		
		
		System.out.printf("%s","*************************************************************************\n");
		System.out.printf("%45s","Movie Search Engine\n");
		
		
		while(true) {
			System.out.println("*************************************************************************");
			System.out.printf("%45s","--List of Features--\n");
			System.out.println("*************************************************************************");
			System.out.printf("%s","1) Searching Movies \n");
			System.out.printf("%s","2) Sorting Movies \n");
			System.out.printf("%s","3) Filtering Movies \n");
			System.out.printf("%s","4) Recommending Movies \n");
			System.out.printf("%s","5) Suggetions For Searching Movies (Extra Feature)\n");
			System.out.printf("%s","0) Exit \n\n");
			
			
			//taking user choice for feature use
//			Scanner s = new Scanner(System.in);
			System.out.println("Make a choice from 0-5:");
			System.out.print("Enter Your Choice :");
			int featureChoice = s.nextInt();
			
			switch (featureChoice) {
			
				case 0:
					System.out.println("\n\n*************************************************************************");
					System.out.println("Good Bye!!\nSo sorry to see you leaving :( ");
					return;
				
				//searching feature
				case 1:
					System.out.println("_______________________________________________________");
					System.out.println("Searching Movies");
					SearchMovies.main(args);
					
					break;
					
				case 2:
					System.out.println("_______________________________________________________");
					System.out.println("Sorting Movies");
					MovieSorter.main(args);
					break;
				case 3:
					System.out.println("_______________________________________________________");
					System.out.println("Filtering Movies");
					Filter_Movies.main(args);
					break;
				case 4:
					System.out.println("_______________________________________________________");
					System.out.println("Recommending Movies");
					MovieGraph.main(args);
					break;
				case 5:
					System.out.println("_______________________________________________________");
					System.out.println("Search Suggetions for Movie");
					SearchSuggetion.main(args);
					break;
			}
		}
		
		
		
		
	}
}
