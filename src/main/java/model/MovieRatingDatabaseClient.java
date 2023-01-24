package model;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.api.TMDBClient;
import model.jsonobjects.Movie;
import model.jsonobjects.MovieOnPage;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MovieRatingDatabaseClient {

    Connection connection;

    public MovieRatingDatabaseClient(){

        String url;
        String username;
        String password;

        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("src/main/resources/config.properties"));
            url = properties.getProperty("AWSDATABASEURL");
            username = properties.getProperty("AWSDATABASEUSERNAME");
            password = properties.getProperty("AWSDATABASEPASSWORD");
        }catch (IOException e){
            url = null;
            username = null;
            password = null;
        }

        try {
            connection = DriverManager.getConnection(url, username, password);
            useDatabase();
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }

    private void useDatabase(){
        executeQuery("USE movieratings");
    }

    public void createRatingTable() {
        executeUpdate("CREATE TABLE ratings (" +
                "review_id int(11) unsigned NOT NULL AUTO_INCREMENT, " +
                "user_id int(11) unsigned NOT NULL, " +
                "movie_id int(11) unsigned NOT NULL, " +
                "movie_rating int(2) unsigned NOT NULL, " +
                "PRIMARY KEY (review_id))");
    }

    public Map<Movie, Integer> getUsersReviews(int userID){
        try (Statement stmt = connection.createStatement()) {
            ResultSet results = stmt.executeQuery("SELECT * FROM ratings WHERE user_id='"+userID+"'");

            //get movies from the movie api
            TMDBClient client = new TMDBClient();
            ObjectMapper mapper = new ObjectMapper();
            Map<Movie, Integer> ratings = new HashMap<>();
            while(results.next()){
                client.rebuildURL("/movie/"+results.getInt(3));
                String response = client.getRequest().string();
                Movie movie = mapper.readValue(response, Movie.class);
                ratings.put(movie, results.getInt(4));
            }
            return ratings;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet executeQuery(String query){
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            stmt.close();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void executeUpdate(String query){
        try (Statement stmt = connection.createStatement()) {
            int rs = stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addRating(MovieOnPage movieOnPage, int rating, int userID) {
        executeUpdate("INSERT INTO ratings (user_id, movie_id, movie_rating) VALUES(" +
                "'"+userID+"'"+", " +
                "'"+ movieOnPage.getId()+"'"+", " +
                "'"+rating+"'"+
                ")"
        );
    }
}