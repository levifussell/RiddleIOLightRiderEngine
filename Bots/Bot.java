import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public abstract class Bot {
    private Scanner scan = new Scanner(System.in);
    // settings variables
    protected int timeBank;
    protected int timePerMove;
    protected String playerNames;
    protected String yourBot;
    protected int yourBotId;
    protected int fieldWidth;
    protected int fieldHeight;

    // game variables
    protected int gameRound;
    protected String gameField;

    //debug mode
    protected final boolean DEBUG = true;
    private final int DELAY_TIME = 100;
    protected int playerIdNumber; //used for local identification of bots

    public Bot()
    {
        this.playerIdNumber = -1;
    }
    public Bot(int playerIdNumber)
    {
        this.playerIdNumber = playerIdNumber;
    }

    abstract String getAction();

    public void updateSettings(String type, String value) {
        switch (type) {
        case "timebank":
            this.timeBank = Integer.parseInt(value);
            System.out.println("GOT timebank: " + this.timeBank);
            break;
        case "time_per_move":
            this.timePerMove = Integer.parseInt(value);
            System.out.println("GOT time_per_move: " + this.timePerMove);
            break;
        case "player_names":
            this.playerNames = value;
            System.out.println("GOT player_names: " + this.playerNames.toString());
            break;
        case "your_bot":
            this.yourBot = value;
            System.out.println("GOT your_bot: " + this.yourBot);
            break;
        case "your_botid":
            this.yourBotId = Integer.parseInt(value);
            System.out.println("GOT your_botid: " + this.yourBotId);
            break;
        case "field_width":
            this.fieldWidth = Integer.parseInt(value);
            System.out.println("GOT field_width: " + this.fieldWidth);
            break;
        case "field_height":
            this.fieldHeight = Integer.parseInt(value);
            System.out.println("GOT field_height: " + this.fieldHeight);
            break;
        }
    }

    public void updateGame(String type, String value) {
        switch (type) {
        case "round":
            this.gameRound = Integer.parseInt(value);
            break;
        case "field":
            this.gameField = value;
        }
    }

    public void runBot() {
        while (scan.hasNextLine()) {
            String line = scan.nextLine();

            if (line.length() == 0) continue;

            String[] command = line.split(" ");
            switch (command[0]) {
            case "settings":
                this.updateSettings(command[2], command[3]);
                break;

            case "update":
                this.updateGame(command[2], command[3]);
                break;

            case "action":
                //System.out.println("up");
                if(command.length < 4 || Integer.parseInt(command[3]) == this.playerIdNumber)
                {
                    if(this.playerIdNumber == 2)
                        System.out.println("up");
                    else
                        System.out.println("down");
//                    else
//                    {
//                        System.out.println(this.getAction());
//
//                    }
                }

                if(DEBUG)
                {
                    //TimeUnit.MILLISECONDS.sleep(500);
                    try
                    {
                        Thread.sleep(DELAY_TIME);
                    }
                    catch(InterruptedException ex)
                    {
                        Thread.currentThread().interrupt();
                    }
                }
                break;
            case "dead":
                System.out.println("reset");
                System.out.flush();
                if(DEBUG)
                {
                    //TimeUnit.MILLISECONDS.sleep(500);
                    try
                    {
                        Thread.sleep(DELAY_TIME);
                    }
                    catch(InterruptedException ex)
                    {
                        Thread.currentThread().interrupt();
                    }
                }
                break;

            }
        }
    }

    public static void main(String[] args) {

    }
}
