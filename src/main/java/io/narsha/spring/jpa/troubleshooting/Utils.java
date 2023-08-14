package io.narsha.spring.jpa.troubleshooting;

import org.apache.commons.csv.CSVFormat;
import org.postgresql.Driver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;

public class Utils {

    public static void main(String[] args) throws Exception {

        var start = Instant.now();
        parseCountries();
        var finish = Instant.now();
        System.out.println("Countries injection duration : " + Duration.between(start, finish).toMillis() + " ms");

        start = Instant.now();
        parseRating();
        finish = Instant.now();
        System.out.println("Rating injection duration : " + Duration.between(start, finish).toMinutes() + "minutes");
    }

    private static void parseCountries() throws IOException, ClassNotFoundException, SQLException {
        var driver = Class.forName(Driver.class.getName());
        var db = DriverManager.getConnection("jdbc:postgresql://localhost:5432/hibernate-issue", "postgres", "admin");
        db.setAutoCommit(false);
        String continentSQL = "insert into continent(name) values(?) on conflict do nothing";
        String regionSQL = "insert into region(name, continent_id) values(?, (select id from continent where name = ?)) on conflict do nothing";
        String countrySQL = "insert into country(name, region_id) values(?, (select id from region where name = ?)) on conflict do nothing";


        var in = new InputStreamReader(Utils.class.getResourceAsStream("/dataset/countries.csv"));
        var records = CSVFormat.EXCEL.parse(in);
        var first = true;

        try (var continentStmt = db.prepareStatement(continentSQL);
             var regionStmt = db.prepareStatement(regionSQL);
             var countryStmt = db.prepareStatement(countrySQL)) {

            int i = 0;
            for (var record : records) {
                i++;
                if (first) {
                    first = false;
                    System.out.println(record);
                    continue;
                }

                var countryName = record.get(1);
                var regionName = record.get(4);
                var continentName = record.get(6);

                continentStmt.setString(1, continentName);
                continentStmt.addBatch();

                regionStmt.setString(1, regionName);
                regionStmt.setString(2, continentName);
                regionStmt.addBatch();

                countryStmt.setString(1, countryName);
                countryStmt.setString(2, regionName);
                countryStmt.addBatch();

                if (i % 100 == 0) {
                    continentStmt.executeBatch();
                    regionStmt.executeBatch();
                    countryStmt.executeBatch();
                    db.commit();
                }
            }
            db.commit();
        }
    }

    private static void parseRating() throws IOException, ClassNotFoundException, SQLException {

        var driver = Class.forName(Driver.class.getName());
        var db = DriverManager.getConnection("jdbc:postgresql://localhost:5432/hibernate-issue", "postgres", "admin");
        db.setAutoCommit(false);
        String bookSQL = "insert into book(id, title) values(?, ?) on conflict do nothing";
        String userSQL = "insert into users(id, username, country_id) values(?, ?, (select id from country order by random() limit 1)) on conflict do nothing";
        String reviewSQL = "insert into review(book_id, user_id, score, summary, description) values(?, ?, ?, ?, ?) on conflict do nothing";

        var in = new InputStreamReader(Utils.class.getResourceAsStream("/dataset/books_rating.csv"));
        var records = CSVFormat.EXCEL.parse(in);
        var first = true;

        try (var userStmt = db.prepareStatement(userSQL);
             var bookStmt = db.prepareStatement(bookSQL);
             var reviewStmt = db.prepareStatement(reviewSQL)) {

            int i = 0;
            for (var record : records) {
                i++;
                if (first) {
                    first = false;
                    System.out.println(record);
                    continue;
                }
                String id = record.get(0);
                String title = record.get(1);

                bookStmt.setString(1, id);
                bookStmt.setString(2, title);
                bookStmt.addBatch();

                String userId = record.get(3);
                String userName = record.get(4);
                userStmt.setString(1, userId);
                userStmt.setString(2, userName);
                userStmt.addBatch();

                Double score = Double.valueOf(record.get(6));
                String summary = record.get(8);
                String review = record.get(9);
                reviewStmt.setString(1, id);
                reviewStmt.setString(2, userId);
                reviewStmt.setDouble(3, score);
                reviewStmt.setString(4, summary);
                reviewStmt.setString(5, usingSubstringMethod(review, 500));
                reviewStmt.addBatch();

                if (i % 100 == 0) {
                    bookStmt.executeBatch();
                    userStmt.executeBatch();
                    reviewStmt.executeBatch();
                    db.commit();
                }
            }
            db.commit();
        }
    }

    static String usingSubstringMethod(String text, int length) {
        if (text.length() <= length) {
            return text;
        } else {
            return text.substring(0, length);
        }
    }
}
