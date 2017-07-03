import java.util.Scanner;

public class TyrviBot extends Bot {

    private Scanner scan = new Scanner(System.in);

    public String getAction() {
        return "up";
    }

    public static void main(String[] args) {
        Bot tyrvi = new TyrviBot();
        tyrvi.runBot();
    }
}
