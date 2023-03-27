import java.io.*;
import java.net.SocketTimeoutException;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebCrawler {
    private static final String URL = "https://www.imdb.com/list/ls500768076/?sort=list_order,asc&st_dt=&mode=detail&page=";
    private static final int MAX_MOVIES = 100;

    public static void main(String[] args,int movies_size) {
        FileWriter writer;
        try {
            writer = new FileWriter("src/movies.csv");
            writer.append("Title,Year,Genre,Director,Cast,Rating,Description\n");

            int movieCount = 0;
            int page = 1;
            while (movieCount < movies_size) {
                String pageUrl = URL + page;
                Document doc = null;
                try {
                    doc = Jsoup.connect(pageUrl).get();
                    System.out.println("Connection established successfully... For page-"+page);
                } catch (SocketTimeoutException e) {
                    System.out.println("Connection timed out. Retrying...");
                    continue;
                }
                Elements movies = doc.select("div.lister-item-content");

                for (Element movie : movies) {
                    String title = movie.select("h3.lister-item-header > a").text();
                    String year = movie.select("h3.lister-item-header > span.lister-item-year").text().replaceAll("[^\\d]", "").substring(0,4);
                    String genre = movie.select("span.genre").text();
                    String director = movie.select("p > a:first-of-type").text();
                    String cast = movie.select("p:not(:first-of-type) > a").text();
                    String rating = "";
                    String description = "";
                    Elements ratingElement = movie.select("span.ipl-rating-star__rating");
                    if (ratingElement.size() > 0) {
                        rating = ratingElement.get(0).text().split(" ")[0];
                    }
                    Elements descriptionElement = movie.select("div.lister-item-content > p");
                    if (descriptionElement.size() > 1) {
                        description = descriptionElement.get(1).text();
                    }
                    if (cast.contains(director)) {
                        cast = cast.replace(director, "");
                    }

                    writer.append(title.replace(",", "") + "," + year + "," + genre.replace(",", "") + "," + director.replace(",", "") + "," + cast.replace(",", "") + "," + rating + "," + description.replace(",", "") + "\n");

                    movieCount++;
                    if (movieCount >= movies_size) {
                        break;
                    }
                }

                page++;
            }

            writer.close();
            System.out.println("Web scraping successful. " + movieCount + " movies fetched.");
            
         // Convert CSV to XLSX
            FileInputStream inputStream = new FileInputStream(new File("src/movies.csv"));
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Movies");

            String line;
            int rowNumber = 0;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(",");
                Row row = sheet.createRow(rowNumber++);
                for (int i = 0; i < data.length; i++) {
                    row.createCell(i).setCellValue(data[i]);
                }
            }

            FileOutputStream outputStream = new FileOutputStream("src/movies_ex.xlsx");
            workbook.write(outputStream);
            workbook.close();
            System.out.println("CSV file converted to XLSX.");
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds exception occurred. Skipping movie...");
        }
    }
}
