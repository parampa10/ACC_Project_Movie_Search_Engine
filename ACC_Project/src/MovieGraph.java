import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
//class
class Movie_recommend {
    String name;
    ArrayList<String> genre;
    ArrayList<String> cast;
    double rating;
    //constructor
    public Movie_recommend(String name, ArrayList<String> genre, ArrayList<String> cast, double rating) {
        this.name = name;
        this.genre = genre;
        this.cast = cast;
        this.rating = rating;
    }
}
//graph class
public class MovieGraph {
    private Map<String, Set<String>> graph;
    private Map<String, Movie_recommend> movies;
    //constructor
    public MovieGraph() {
        graph = new HashMap<>();
        movies = new HashMap<>();
    }
    //method to add movie to graph as node and its attributes as neightbours
    public void addMovie(Movie_recommend movie) {
        String movieName = movie.name;
        movies.put(movieName, movie);

        // Add the movie node to the graph
        graph.put(movieName, new HashSet<>());

        // Add edges between the movie and its cast members
        for (String castMember : movie.cast) {
            if (!graph.containsKey(castMember)) {
                graph.put(castMember, new HashSet<>());
            }
            graph.get(movieName).add(castMember);
            graph.get(castMember).add(movieName);
        }

     // Add an edge between the movie and its genre
        for (String genreMember : movie.genre) {
        	
        	if (!graph.containsKey(genreMember)) {
                graph.put(genreMember, new HashSet<>());
                Movie_recommend genreMovie = new Movie_recommend(movie.name, new ArrayList<>(), new ArrayList<>(), movie.rating);
                movies.put(genreMember, genreMovie);
            }
        	graph.get(movieName).add(genreMember);
            graph.get(genreMember).add(movieName);
        }
        
       
    }
    //method to recommend movies
    public void recommendMovies() {
        Scanner sc = new Scanner(System.in);
        //infinite loop for new inputs
        while (true) {
        	System.out.println("_______________________________________________________");
        	System.out.print("Enter a Movie or Enter \"exit\" to exit the feature\n");
        	System.out.println("Enter: ");
            String movieName = sc.nextLine();
            if (movieName.toLowerCase().equals("exit")) {
            	System.out.println("_______________________________________________________");
            	return;
            }
            //creating object
            Movie_recommend movie = movies.get(movieName);
            if (movie == null) {
                System.out.println("Movie not found in database.");
                
            }
            else {
            	//finding movies and printing recommendations
            	ArrayList<String> genre = movie.genre;
                System.out.println("Recommendations for you:");
                for(String gen: genre) {
                	for (String neighbor : graph.get(gen)) {
                		if (movies.get(neighbor).rating >= movie.rating) {
                			if (!neighbor.equals(movieName)) {
                                System.out.println(neighbor + " (" + movies.get(neighbor).rating + ")");
                            }
                		}
                    }
                }
                
            }
            
        }
        
    }
    //method to print the graph
    public void printGraph() {
        for (String node : graph.keySet()) {
            System.out.print(node + " -> ");
            for (String neighbor : graph.get(node)) {
                Movie_recommend movie = movies.get(neighbor);
                if (movie != null) {
                    System.out.print(neighbor + "(" + movie.rating + ") ");
                }
            }
            System.out.println();
        }
    }

    //main method
    public static void main(String[] args) throws EncryptedDocumentException, IOException {
        MovieGraph graph = new MovieGraph();
        //reading data from excel
        FileInputStream file = new FileInputStream(new File("src/movies_ex.xlsx"));
        Workbook workbook = WorkbookFactory.create(file);

        // Get the first sheet
        Sheet sheet = workbook.getSheetAt(0);
        int count=0;
        //reading row by row data
        for (Row row : sheet) {
            if(count == 0) {
                count = 1;
                continue;
            }
   
            // Read the movie data from the row
            String name = row.getCell(0).getStringCellValue();
            String genre = row.getCell(2).getStringCellValue();
            String rating_str = row.getCell(5).getStringCellValue();
            Double rating =Double.valueOf(rating_str);
            //separating cast names for better graph
            String cast=row.getCell(4).getStringCellValue();
            String [] c=cast.split(" "); 
            List<String> cast_arr= new ArrayList();
            int flag=0;
            String add_cast="";
            for(String word : c) {
            	if (flag==0) {
            		add_cast+=word;
            		flag++;
            	}
            	else if (flag==1) {
            		add_cast+=" "+word;
            		flag++;
            		cast_arr.add(add_cast);
            	}else {
            		flag=0;
            		add_cast="";
            	}
            }
            //splitting genre and making graph more efficient
            String [] genre_=genre.split(" "); 
            List<String> genre_arr= new ArrayList();

            for(String g:genre_) {
            	genre_arr.add(g);
            }
            
            
            ArrayList<String> cast1 = new ArrayList<>(cast_arr);
            ArrayList<String> genre1 = new ArrayList<>(genre_arr);
            Movie_recommend movie_from_excel = new Movie_recommend(name, genre1, cast1, rating);
            graph.addMovie(movie_from_excel);//addingm movie to graph

        }
//
//        graph.printGraph();
        //calling for recommendations of movies
        graph.recommendMovies();

        
        
        
    }
}
