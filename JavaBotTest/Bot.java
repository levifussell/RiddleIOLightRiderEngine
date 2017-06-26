import java.util.Scanner;


public abstract class Bot {
    private Scanner scan = new Scanner(System.in);
    // settings variables
    private int timeBank;
    private int timePerMove;
    private String playerNames;
    private String yourBot;
    private int yourBotId;
    private int fieldWidth;
    private int fieldHeight;

    // game variables
    private int gameRound;
    private String gameField;


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
                this.updateGame(command[1], command[2]);
                break;

            case "action":
                this.getAction();
                break;
                
            }
        }
    }

    public static void main(String[] args) {
        
    }
}
