package controller;

import model.Model;
import model.jsonobjects.MovieOnPage;
import view.View;

import java.util.Iterator;

public class Controller {
    View view;
    Model model;
    int userID;
    public Controller(){
        view = new View(this);
        model = new Model(this);
    }

    public void start(){
        view.display();
        userID = view.askUserID();
        while(true){
            switch (view.askUserAction()){
                case NEW_MOVIE_RATING -> newMovieRating();
                case VIEW_PREVIOUS_RATING -> viewPreviousMovieRatings();
            }
        }
    }

    private void newMovieRating(){
        String movieName = view.askMovieName();
        int movieYear = view.askMovieYear();
        boolean isAdult = view.askIfAdult();
        MovieInformation movieInformation = new MovieInformation(
                movieName,
                movieYear,
                isAdult
        );
        MovieOnPage confirmedMovieOnPage = null;
        for (Iterator<MovieOnPage> it = model.getPossibleMovies(movieInformation); it.hasNext(); ) {
            MovieOnPage movieOnPage = it.next();
            if(view.confirmMovie(movieOnPage)){
                confirmedMovieOnPage = movieOnPage;
                break;
            }
        }
        if(confirmedMovieOnPage ==null)return;
        int movieRating = view.askMovieRating();
        model.saveMovieRating(confirmedMovieOnPage, movieRating, userID);
    }
    private void viewPreviousMovieRatings(){
        view.showRatings(model.getRatingsForUser(userID));
    }
}