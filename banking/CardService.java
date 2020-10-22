package banking;

import java.util.Scanner;

public class CardService {
    private CardRepository cardRepository;
    public CardService(String dbName) {
        cardRepository = new CardRepository(dbName);
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
                    Card card = new Card();
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
        if (cardRepository.getCardByNumber(cardToCheck.getNumber()) == null){
            return false;
        }
       return true;
    }

    private void loggedMenu(Scanner scanner, Card loggedCard) {
        System.out.println("You have successfully logged in!\n");
        int loggedMenu;
        while (true) {
            System.out.print("1. Balance\n"
                    + "2. Add income\n" +
                    "3. Do transfer\n" +
                    "4. Close account\n" +
                    "5. Log out\n"
                    + "0. Exit\n" + ">");
            loggedMenu = scanner.nextInt();
            switch (loggedMenu) {
                case 1:
                    System.out.println("Balance: " +
                            cardRepository.getBalance(loggedCard) + "\n");
                    break;
                case 2:
                    System.out.print("Enter income:\n" +
                            ">");
                    int input = scanner.nextInt();
                    cardRepository.addIncome(input, loggedCard.getNumber());
                    System.out.println("Income was added!");
                    break;
                case 3:
                    System.out.println("Transfer\n" +
                            "Enter card number:");
                    String stringCardTo = String.valueOf(scanner.next()).trim();
                    transfer(stringCardTo, loggedCard, scanner);
                    break;
                case 4:
                    cardRepository.deleteCardByNumber(loggedCard.getNumber());
                    System.out.println("The account has been closed!\n");
                    break;
                case 5:
                    return;
                case 0:
                    System.out.println("\nBye!");
                    System.exit(0);
            }
        }
    }

    private void transfer(String stringCardTo, Card loggedCard, Scanner scanner) {
        if (stringCardTo.equals(loggedCard.getNumber())){
            System.out.println("You can't transfer money to the same account!");
            return;
        } else if (!stringCardTo.equals(Card.applyLuhn(stringCardTo))){
            System.out.println("Probably you made mistake in the card number. Please try again!");
            return;
        } else if (cardRepository.getCardByNumber(stringCardTo) == null){
            System.out.println("Such a card does not exist.");
            return;
        }
        System.out.print("Enter how much money you want to transfer:\n" + ">");
        int transfer = scanner.nextInt();
        System.out.println(cardRepository.doTransfer(stringCardTo, loggedCard, transfer));
    }
}
