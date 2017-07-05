package com.cinema;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws ParseException {
        Scanner reader = new Scanner(System.in);
        List<Movie> movies = getMoviesList();
        System.out.println("Plese select a movie from the list (enter number): ");
        movies.forEach(movie -> System.out.println(movie.getId() + ". " + movie.getName()));
        String movieId = reader.next();
        while(!isMovieValidOption(movieId, movies)){
            System.out.println("Please insert a valid option !");
            movieId = reader.next();
        }
        long finalMovieId = Long.valueOf(movieId);
        //noinspection OptionalGetWithoutIsPresent
        Movie movie = movies.stream().filter(m -> m.getId() == finalMovieId).findFirst().get();
        System.out.println("You selected movie: " + movie.getName());
        System.out.println("Please enter the date (format dd/mm/yyyy) :");
        String dateInput = reader.next();
        while (!isDateValid(dateInput)){
            System.out.println("Please insert a valid option !");
            dateInput = reader.next();
        }
        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dateInput);
        System.out.println("Please enter numbers of reservation: ");
        String numberOfReservations = reader.next();
        while (!isNumeric(numberOfReservations)){
            System.out.println("Please insert a valid option !");
            numberOfReservations = reader.next();
        }
        List<Ticket> tickets = new ArrayList<>();
        for(int i = 0;i < Integer.valueOf(numberOfReservations); i++ ){
            System.out.println("Please enter type of ticket (number):");
            System.out.println("1. Standard - £8\n" +
                    "2. OAP - £6\n" +
                    "3. Student - £6\n" +
                    "4. Child - £4");
            String ticketType = reader.next();
            while (!isValidTicketType(ticketType)){
                System.out.println("Please insert a valid option !");
                ticketType = reader.next();
            }
            switch (Integer.valueOf(ticketType)){
                case 1 : tickets.add(new StandardTicket());
                    break;
                case 2 : tickets.add(new OAPTicket());
                    break;
                case 3 : tickets.add(new StudentTicket());
                    break;
                case 4 : tickets.add(new ChildTicket());
                    break;
            }
        }
        long totalPrice = calculateTotalPrice(tickets, date);
        System.out.println("Your reservation is for movie : " + movie.getName() + " for " + numberOfReservations + " persons. ");
        if(isDiscountAvailable(date)){
            System.out.println("The Total cost of tickets for this movie is £" + totalPrice + " (Wednesday discount)");
        }else {
            System.out.println("The Total cost of tickets for this movie is  £" + totalPrice);
        }
    }

    private static boolean isDiscountAvailable(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY;
    }

    private static long calculateTotalPrice(List<Ticket> tickets, Date date){
        long price = tickets.stream().mapToLong(Ticket::getPrice).sum();
        if(isDiscountAvailable(date)){
            price = price - tickets.size() * 2;
        }
        return price;
    }

    private static List<Movie> getMoviesList(){
        List<Movie> movies = new ArrayList<>();
        movies.add(new MovieImpl(1L,"Guardians Of The Galaxy Vol. 2", "James Gunn"));
        movies.add(new MovieImpl(2L,"Snatched", "Jonathan Levine"));
        movies.add(new MovieImpl(3L,"King Arthur: Legend Of The Sword", "Guy Ritchie"));
        movies.add(new MovieImpl(4L,"The Wall", "Doug Liman"));
        movies.add(new MovieImpl(5L,"Gifted", "Marc Webb"));
        return movies;
    }

    private static boolean isDateValid(String date){
        String dateFormat = "dd/MM/yyyy";
        try {
            new SimpleDateFormat(dateFormat).parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private static boolean isMovieValidOption(String s, List<Movie> movies) {
        boolean isNumerical = isNumeric(s);
        return isNumerical && Integer.valueOf(s) > 0 && movies.size() >= Integer.valueOf(s);
    }

    private static boolean isNumeric(String s){
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    private static boolean isValidTicketType(String s){
        return isNumeric(s) && Integer.valueOf(s) > 0 && Integer.valueOf(s) <= 4;
    }
}

