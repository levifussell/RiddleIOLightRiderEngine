import java.util.Scanner;

public class TyrviBot extends Bot {

    private Scanner scan = new Scanner(System.in);

    public String getAction() {
        return "up";
    }

    public static void main(String[] args) {
        Bot tyrvi = new TyrviBot();
        int id = 2;
        if (args.length > 0) {
            id = Integer.parseInt(args[0]);
        }
        tyrvi.setBotId(id);
        tyrvi.runBot();
    }

    public void onDead()
    {
        //TODO
    }
}
