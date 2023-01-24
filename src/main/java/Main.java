import com.fasterxml.jackson.databind.ObjectMapper;
import controller.Controller;
import model.MovieRatingDatabaseClient;
import model.api.QueryString;
import model.api.TMDBClient;
import okhttp3.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws MalformedURLException, SQLException {
        Controller controller = new Controller();
        controller.start();
    }
}
