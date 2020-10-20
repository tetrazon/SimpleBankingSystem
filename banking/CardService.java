package banking;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CardService {
    private List<Card> cardList;
    private CardRepository cardRepository;
    public CardService(String dbName) {
        cardRepository = new CardRepository(dbName);
        cardList = new ArrayList<>();
    }

    public Card addCard() {
        Card card = new Card();
        cardList.add(card);
        return card;
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);
        int input;
        while (true) {
            System.out.print("1. Create an account\n"
            + "2. Log into account\n"
            + "0. Exit\n>");
            input = scanner.nextInt();
            switch (input){
                case 1:
                    Card card = addCard();
                    cardRepository.saveCard(card);
                    System.out.println("\nYour card has been created\n" + "Your card number:");
                    System.out.println(card.getNumber());
                    System.out.println("Your card PIN:");
                    System.out.println(card.getPin() + "\n");
                    break;
                case 2:
                    System.out.print("Enter your card number:\n" + ">");
                    String cardNumber = scanner.next();
                    System.out.print("Enter your PIN:\n" + ">");
                    String pin = String.valueOf(scanner.next()).trim();
                    Card cardToCheck = new Card(cardNumber, pin);
                    if (!isContainsCard(cardToCheck)) {
                        System.out.println("Wrong card number or PIN!\n");
                    } else {
                        loggedMenu(scanner, cardToCheck);
                    }
                    break;
                case 0:
                    System.out.println("\nBye!");
                    System.exit(0);

            }
        }
    }

    private boolean isContainsCard(Card cardToCheck) {
        for (Card card : cardList) {
            if (!(card.getNumber().equals(cardToCheck.getNumber()) && card.getPin().equals(cardToCheck.getPin()))){
                return false;
            }
        }
       return true;
    }

    private void loggedMenu(Scanner scanner, Card cardToCheck) {
        System.out.println("You have successfully logged in!\n");
        int loggedMenu;
        while (true) {
            System.out.print("1. Balance\n"
                    + "2. Log out\n"
                    + "0. Exit\n" + ">");
            loggedMenu = scanner.nextInt();
            switch (loggedMenu) {
                case 1:
                    System.out.println("Balance: " + cardToCheck.getBalance() + "\n");
                    break;
                case 2:
                    return;
                case 0:
                    System.out.println("\nBye!");
                    System.exit(0);
            }
        }
    }
}
