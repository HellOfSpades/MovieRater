package model;

import com.fasterxml.jackson.databind.ObjectMapper;
import controller.Controller;
import model.jsonobjects.Movie;
import model.jsonobjects.MovieOnPage;
import controller.MovieInformation;
import model.api.QueryString;
import model.api.TMDBClient;
import model.jsonobjects.MoviesPage;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class Model {

    Controller controller;

    public Model(Controller controller) {
        this.controller = controller;
    }

    public Iterator<MovieOnPage> getPossibleMovies(MovieInformation movieInformation){
        return new Iterator<MovieOnPage>() {

            int pageIndex = 1;
            MoviesPage page;
            int maxPage;
            int movieIndex = 0;
            ObjectMapper mapper = new ObjectMapper();
            TMDBClient tmdbClient = new TMDBClient();
            boolean hasNext;
            MovieOnPage nextMovieOnPage;

            {
                page = getPage();
                maxPage = page.getTotal_pages();
                nextMovieOnPage = getNextMovie();
                hasNext = nextMovieOnPage !=null;
            }

            private MoviesPage getPage(){
                try {
                    tmdbClient.rebuildURL(
                            "/discover/movie",
                            new QueryString("year", movieInformation.getYear() + ""),
                            new QueryString("include_adult", (movieInformation.isAdult()) ? "true" : "false"),
                            new QueryString("page", pageIndex + "")
                    );
                    ResponseBody responseBody = tmdbClient.getRequest();
                    //System.out.println("reading page "+pageIndex);
                    return mapper.readValue(responseBody.string(), MoviesPage.class);
                }catch (IOException e){
                    e.printStackTrace();
                }
                return null;
            }

            private MovieOnPage getNextMovie(){
                do {
                    for (; movieIndex < page.getResults().length; movieIndex++) {
                        MovieOnPage movieOnPage = page.getResults()[movieIndex];
                        //System.out.println("checking "+movie);
                        if (movieMatchesInformation(movieOnPage, movieInformation)) {
                            //System.out.println("correct!!");
                            hasNext = true;
                            movieIndex++;
                            return movieOnPage;
                        }
                    }
                    if(pageIndex<maxPage){
                        pageIndex++;
                        movieIndex = 0;
                        page = getPage();
                    }
                }while(pageIndex <= maxPage);

                hasNext = false;
                return null;
            }

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public MovieOnPage next() {
                MovieOnPage returnedMovieOnPage = nextMovieOnPage;
                nextMovieOnPage = getNextMovie();
                return returnedMovieOnPage;
            }
        };
    }

    private boolean movieMatchesInformation(MovieOnPage movieOnPage, MovieInformation movieInformation){
        String[] namePartsInformation = movieInformation.getName().toLowerCase().split("[^a-zA-Z]");
        String[] namePartsMovie = movieOnPage.getTitle().toLowerCase().split("[^a-zA-Z]");
        for (int i = 0; i < namePartsInformation.length; i++) {
            for (int j = 0; j < namePartsMovie.length; j++) {
                if(namePartsInformation[i].equals(namePartsMovie[j]))return true;
            }
        }
        return false;
    }

    public void saveMovieRating(MovieOnPage movieOnPage, int rating, int userID){
        MovieRatingDatabaseClient client = new MovieRatingDatabaseClient();
        client.addRating(movieOnPage, rating, userID);
    }

    public Map<Movie, Integer> getRatingsForUser(int userID){
        MovieRatingDatabaseClient client = new MovieRatingDatabaseClient();
        return client.getUsersReviews(userID);
    }
}
