import java.util.Scanner;

public class javaBot {

    private Scanner scan = new Scanner(System.in);

    public void run() {
        while (scan.hasNextLine()) {
            String line = scan.nextLine();

            if (line.length() == 0) continue;

            String[] parts = line.split(" ");
            switch (parts[0]) {
                case "settings":
                    // store game settings
                    break;
                case "update":
                    // store game updates
                    break;
                case "action":
                    if(parts[parts.length - 1].equals("2"))
                    {
                        System.out.println("up");
                        //System.out.flush();
                    }
                    else
                    {
                        System.out.println("down");
                    }
                    //System.error(parts);
                    //System.out.println(parts);
                    break;
                case "dead":
                    System.out.println("reset");
                    //System.out.flush();
                    return;
                default:
                    // error
            }
        }
    }

    public static void main(String[] args) {
        (new javaBot()).run();
    }
}

