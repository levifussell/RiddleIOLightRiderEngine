import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import LeviFuBot.*;

public class JavaBot {

    private static boolean DEBUG = true;
    private Scanner scan = new Scanner(System.in);
    private final int DELAY_TIME = 100;

    private Bot levi1 = new LeviFuBot(2);
    private Bot levi2 = new LeviFuBot(3);

    public void run() {

        levi1.runBot();
        levi2.runBot();

//        while (scan.hasNextLine()) {
//            String line = scan.nextLine();
//
//            if (line.length() == 0) continue;
//
//            String[] parts = line.split(" ");
//            switch (parts[0]) {
//                case "settings":
//                    // store game settings
//                    break;
//                case "update":
//                    // store game updates
//                    break;
//                case "action":
//                    if(parts[parts.length - 1].equals("2"))
//                    {
//                        // green
//                        //System.out.println("up");
//                        //System.out.flush();
//                        //System.out.println(tyrvi.getAction());
//                        System.out.println(levi1.getAction());
//                    }
//                    else
//                    {
//                        // red
//                        System.out.println("down");
//                    }
//                    System.out.flush();
//                    //System.error(parts);
//                    //System.out.println(parts);
//
//                    if(DEBUG)
//                    {
//                        //TimeUnit.MILLISECONDS.sleep(500);
//                        try
//                        {
//                            Thread.sleep(DELAY_TIME);
//                        }
//                        catch(InterruptedException ex)
//                        {
//                            Thread.currentThread().interrupt();
//                        }
//                    }
//                    break;
//                case "dead":
//                    System.out.println("reset");
//                    System.out.flush();
//                    if(DEBUG)
//                    {
//                        //TimeUnit.MILLISECONDS.sleep(500);
//                        try
//                        {
//                            Thread.sleep(DELAY_TIME);
//                        }
//                        catch(InterruptedException ex)
//                        {
//                            Thread.currentThread().interrupt();
//                        }
//                    }
//                    break;
//                    //return;
//                default:
//                    // error
//            }
//        }
    }

    public static void main(String[] args) {
        (new JavaBot()).run();
    }
}

