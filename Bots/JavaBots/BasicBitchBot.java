import java.util.Scanner;

public class BasicBitchBot extends Bot {

    private Scanner scan = new Scanner(System.in);

    public String getAction() {
        return "up";
    }

    public static void main(String[] args) {
        Bot bitch = new BasicBitchBot();
        int id = 2;
        if (args.length > 0) {
            id = Integer.parseInt(args[0]);
        }
        bitch.setBotId(id);
        bitch.runBot();
    }
}
