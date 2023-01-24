package view;

import controller.Controller;
import model.jsonobjects.Movie;
import model.jsonobjects.MovieOnPage;
import controller.UserAction;

import java.util.Map;
import java.util.Scanner;

public class View {
    Controller controller;

    Scanner scanner = new Scanner(System.in);

    public View(Controller controller) {
        this.controller = controller;
    }

    public void display() {
        System.out.println("welcome to Movie Rater!!");
    }

    public String askMovieName() {
        System.out.println("what is the name of this movie?");
        //consume leftover
        scanner.nextLine();
        String output = scanner.nextLine();
        System.out.println("Fantastic Name");
        return output;
    }

    public int askMovieYear() {
        System.out.println("what year was the movie released in?");
        int output = scanner.nextInt();
        System.out.println("What a fantastic year for movies");
        return output;
    }

    public boolean askIfAdult() {
        System.out.println("can it be categorized as an adult movie? Y/N");
        //consume leftover
        scanner.nextLine();
        boolean output = scanner.nextLine().equals("Y");
        System.out.println((output) ? "Kinky" : "Nice");
        return output;
    }

    public boolean confirmMovie(MovieOnPage possibleMovieOnPage) {

        System.out.println("Is this the movie you watched? Y/N");
        System.out.println(possibleMovieOnPage.getTitle());
        System.out.println(possibleMovieOnPage.getOverview());
        return scanner.nextLine().equals("Y");
    }

    public int askUserID() {
        System.out.println("what is your user id?");
        return scanner.nextInt();
    }

    public int askMovieRating() {
        System.out.println("what rating would you give this movie");
        return scanner.nextInt();
    }

    public UserAction askUserAction() {
        System.out.println("what interests you?");
        for (int i = 0; i < UserAction.values().length; i++) {
            System.out.println(i + ". " + UserAction.values()[i].name());
        }
        return UserAction.values()[scanner.nextInt()];
    }

    public void showRatings(Map<Movie, Integer> ratingsForUser) {
        for (Map.Entry<Movie,Integer> review: ratingsForUser.entrySet()) {
            System.out.println(review.getKey().getTitle());
            System.out.println(review.getKey().getOverview());
            System.out.println("ratings: "+review.getValue());
        }
    }
}
