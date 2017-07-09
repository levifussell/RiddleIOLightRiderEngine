

public class LeviFuBot extends Bot
{

    String getAction()
    {
        return "down";
    }

    public static void main(String[] args) {
        Bot levifu = new LeviFuBot();
        int id = 2;
        if (args.length > 0) {
            id = Integer.parseInt(args[0]);
        }
        levifu.setBotId(id);
        levifu.runBot();
    }
}
