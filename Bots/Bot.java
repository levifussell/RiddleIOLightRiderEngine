import java.util.Scanner;


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

    abstract String getAction();

    public void updateSettings(String type, String value) {
        switch (type) {
        case "timebank":
            this.timeBank = Integer.parseInt(value);
            break;
        case "time_per_move":
            this.timePerMove = Integer.parseInt(value);
            break;
        case "player_names":
            this.playerNames = value;
            break;
        case "your_bot":
            this.yourBot = value;
            break;
        case "your_botid":
            this.yourBotId = Integer.parseInt(value);
            break;
        case "field_width":
            this.fieldWidth = Integer.parseInt(value);
            break;
        case "field_height":
            this.fieldHeight = Integer.parseInt(value);
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
        System.out.println(type + " " + value);
    }

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
                System.out.println(this.getAction());
                break;

            }
        }
    }
}
