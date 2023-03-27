import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class MovieSorter {
    int sortCriteria;
    MovieInfo[] movies;

    public int getSortCriteria() {
        return sortCriteria;
    }

    public void setSortCriteria(int sortCriteria) {
        this.sortCriteria = sortCriteria;
    }

    class MovieInfo {
        private String title;
        private int releaseYear;
        private String director;
        private Double rating;

        public MovieInfo(String title, int releaseYear, String director,Double rating) {
            this.title = title;
            this.releaseYear = releaseYear;
            this.director = director;
            this.rating= rating;
        }

        public String getTitle() {
            return title;
        }

        public int getReleaseYear() {
            return releaseYear;
        }

        public String getDirector() {
            return director;
        }
        
        public Double getRating() {
            return rating;
        }
        
        
    }

    
    
    public static void main(String[] args) {
        MovieSorter ms = new MovieSorter();

        try {
            // Open the Excel file
            FileInputStream file = new FileInputStream(new File("src/movies_ex.xlsx"));
            Workbook workbook = WorkbookFactory.create(file);

            // Get the first sheet
            Sheet sheet = workbook.getSheetAt(0);

            // Read the data from the Excel file and add it to the movie array
            int count = 0;
            ms.movies = new MovieInfo[sheet.getLastRowNum()];
            for (Row row : sheet) {
                if (count == 0) {
                    count = 1;
                    continue;
                }

                // Read the movie data from the row
                String title = row.getCell(0).getStringCellValue();
                String releaseYearString = row.getCell(1).getStringCellValue();
                
                String rating_str = row.getCell(5).getStringCellValue();
                Double rating =Double.valueOf(rating_str);
                int releaseYear;
                try {
                    releaseYear = Integer.parseInt(releaseYearString);
                } catch (NumberFormatException e) {
                    releaseYear = 0;
                }
                String director = row.getCell(3).getStringCellValue();

                // Create a new movie object and add it to the movie array
                MovieSorter movieSorter = new MovieSorter();
                MovieSorter.MovieInfo movie = movieSorter.new MovieInfo(title, releaseYear, director, rating);

                
//                MovieInfo movie = new MovieInfo(title, releaseYear, director,rating);
                ms.movies[count - 1] = movie;
                count++;
            }

            // Close the workbook and file input stream
            workbook.close();
            file.close();

            Scanner scanner = new Scanner(System.in);
            int x = -1;

            while (x != 0) {
                // Get the user-selected sorting criteria
                System.out.println("Select sorting criteria (1=title, 2=release year, 3=director, 4=Rating, 0=Exit): ");
                x = scanner.nextInt();
                ms.setSortCriteria(x);

                // Sort the movie array based on the selected criteria
                switch (ms.getSortCriteria()) {
                    case 1:
                        mergeSort(ms.movies, 0, ms.movies.length - 1, "title");
                        System.out.println("Sorted by title:");
                        for (MovieInfo movie : ms.movies) {
                            System.out.println(movie.getTitle());
                        }
                        break;
                    case 2:
                        mergeSort(ms.movies, 0, ms.movies.length - 1, "year");
                        System.out.println("Sorted by release year:");
                        for (MovieInfo movie : ms.movies) {
                            System.out.println(movie.getTitle() + "   " + movie.getReleaseYear());
                        }
                        break;
                    case 3:
                        mergeSort(ms.movies, 0, ms.movies.length - 1, "director");
                        System.out.println("Sorted by director:");
                        for (MovieInfo movie : ms.movies) {
                            System.out.println(movie.getTitle() + "   " + movie.getDirector());
                        }
                        break;
                    case 4:
                        mergeSort(ms.movies, 0, ms.movies.length - 1, "rating");
                        System.out.println("Sorted by Rating:");
                        for (MovieInfo movie : ms.movies) {
                            System.out.println(movie.getTitle() + "   " + movie.getRating());
                        }
                        break;
                    case 0:
                    	System.out.println("_______________________________________________________\n");
                        return;
                    default:
                        System.out.println("Invalid sorting criteria");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Merge sort
       
        private static void mergeSort(MovieInfo[] arr, int l, int r, String sortBy) {
            if (l < r) {
                int m = (l + r) / 2;
                mergeSort(arr, l, m, sortBy);
                mergeSort(arr, m + 1, r, sortBy);
                merge(arr, l, m, r, sortBy);
            }
        }
        
        private static void merge(MovieInfo[] arr, int l, int m, int r, String sortBy) {
            int n1 = m - l + 1;
            int n2 = r - m;
            MovieInfo[] L = new MovieInfo[n1];
            MovieInfo[] R = new MovieInfo[n2];
            for (int i = 0; i < n1; i++) {
                L[i] = arr[l + i];
            }
            for (int j = 0; j < n2; j++) {
                R[j] = arr[m + 1 + j];
            }
            int i = 0, j = 0, k = l;
            while (i < n1 && j < n2) {
                if (sortBy.equals("title")) {
                    if (L[i].getTitle().compareTo(R[j].getTitle()) <= 0) {
                        arr[k] = L[i];
                        i++;
                    } else {
                        arr[k] = R[j];
                        j++;
                    }
                } else if (sortBy.equals("director")) {
                    if (L[i].getDirector().compareTo(R[j].getDirector()) <= 0) {
                        arr[k] = L[i];
                        i++;
                    } else {
                        arr[k] = R[j];
                        j++;
                    }
                } else if (sortBy.equals("year")) {
                    if (L[i].getReleaseYear() <= R[j].getReleaseYear()) {
                        arr[k] = L[i];
                        i++;
                    } else {
                        arr[k] = R[j];
                        j++;
                    }
                } else if (sortBy.equals("rating")) {
                    if (L[i].getRating() >= R[j].getRating()) {
                        arr[k] = L[i];
                        i++;
                    } else {
                        arr[k] = R[j];
                        j++;
                    }
                }
                k++;
            }
            while (i < n1) {
                arr[k] = L[i];
                i++;
                k++;
            }
            while (j < n2) {
                arr[k] = R[j];
                j++;
                k++;
            }
        }
        
        
    }     // Sort the movies by year using merge sort
     


