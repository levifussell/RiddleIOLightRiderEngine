import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class JavaBot {

    private static boolean DEBUG = true;
    private Scanner scan = new Scanner(System.in);
    private Bot tyrvi = new TyrviBot();

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
                        // green
                        //System.out.println("up");
                        //System.out.flush();
                        System.out.println(tyrvi.getAction());
                    }
                    else
                    {
                        // red
                        System.out.println("down");
                    }
                    System.out.flush();
                    //System.error(parts);
                    //System.out.println(parts);

                    if(DEBUG)
                    {
                        //TimeUnit.MILLISECONDS.sleep(500);
                        try
                        {
                            Thread.sleep(500);
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
                    break;
                    //return;
                default:
                    // error
            }
        }
    }

    public static void main(String[] args) {
        (new JavaBot()).run();
    }
}

