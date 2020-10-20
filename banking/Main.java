package banking;

public class Main {
    public static void main(String[] args) {
        CardService cardService = new CardService(args[1]);
        cardService.menu();
    }
}
