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
    protected final boolean DEBUG = false;
    private final int DELAY_TIME = 100;

    abstract String getAction();

    public void setBotId(int id) {
        this.yourBotId = id;
    }

    public void updateSettings(String type, String value) {
        switch (type) {
        case "timebank":
            this.timeBank = Integer.parseInt(value);
            //System.out.println("GOT timebank: " + this.timeBank);
            break;
        case "time_per_move":
            this.timePerMove = Integer.parseInt(value);
            //System.out.println("GOT time_per_move: " + this.timePerMove);
            break;
        case "player_names":
            this.playerNames = value;
            //System.out.println("GOT player_names: " + this.playerNames.toString());
            break;
        case "your_bot":
            this.yourBot = value;
            //System.out.println("GOT your_bot: " + this.yourBot);
            break;
        case "your_botid":
            this.yourBotId = Integer.parseInt(value);
            //System.out.println("GOT your_botid: " + this.yourBotId);
            break;
        case "field_width":
            this.fieldWidth = Integer.parseInt(value);
            //System.out.println("GOT field_width: " + this.fieldWidth);
            break;
        case "field_height":
            this.fieldHeight = Integer.parseInt(value);
            //System.out.println("GOT field_height: " + this.fieldHeight);
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

    protected abstract void onDead(int botId);

    public void runBot() {
        while (scan.hasNextLine()) {
            String line = scan.nextLine();

            if (line.length() == 0) continue;

            String[] command = line.split(" ");
            switch (command[0]) {
            case "settings":
                this.updateSettings(command[1], command[2]);
                break;

            case "update":
                this.updateGame(command[2], command[3]);
                break;

            case "action":
                if (command.length < 4 || Integer.parseInt(command[3]) == this.yourBotId)
                    System.out.println(this.getAction());

                //if(DEBUG)
                //{
                    ////TimeUnit.MILLISECONDS.sleep(500);
                    //try
                    //{
                        //Thread.sleep(DELAY_TIME);
                    //}
                    //catch(InterruptedException ex)
                    //{
                        //Thread.currentThread().interrupt();
                    //}
                //}
                break;
            case "dead":
                if (Integer.parseInt(command[2]) != this.yourBotId) {
                    System.out.println("reset");
                }
                this.onDead(Integer.parseInt(command[2]));
                //if(DEBUG)
                //{
                    ////TimeUnit.MILLISECONDS.sleep(500);
                    //try
                    //{
                        //Thread.sleep(DELAY_TIME);
                    //}
                    //catch(InterruptedException ex)
                    //{
                        //Thread.currentThread().interrupt();
                    //}
                //}

                break;
            }
        }
    }

    public int GetBotId() { return this.yourBotId; }
}
