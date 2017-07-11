import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class LeviFuBotDensityFill extends Bot
{
//    public enum FillDirection
//    {
//        LEFT, RIGHT
//    };

    private final boolean LOCAL_TEST_MODE = true;

    private int rowPrevious;
    private int colPrevious;
    private int dirRow;
    private int dirCol;

    private static BufferedWriter writer;

    private int opponentBotId;

    private int deathsYourBot;
    //private int deathsOpponentBot;

    public LeviFuBotDensityFill()
    {
        super();

        this.Initialise();

    }

    public static void main(String[] args) {

        Bot levifu = new LeviFuBotDensityFill();
        int id = 3;
        if (args.length > 0) {
            id = Integer.parseInt(args[0]);
        }
        levifu.setBotId(id);

        writer = null;
        try
        {
            writer = new BufferedWriter(new FileWriter("DEBUG" + levifu.GetBotId() + ".log"));
            //writer.write("START DEBUG\n\n");

            levifu.runBot();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                writer.close();
            }
            catch(Exception e)
            {

            }
        }
    }

    private void Initialise()
    {
        this.rowPrevious = -1;
        this.colPrevious = -1;
        this.dirRow = 0;
        this.dirCol = 0;

        if(LOCAL_TEST_MODE)
        {
            this.fieldWidth = 16;
            this.fieldHeight = 16;
        }
    }

    private static void WriteDebug(String output)
    {

        try
        {
            writer.write(output);
        }
        catch(IOException e)
        {
        }
    }

    protected void onDead(int botId)
    {
        if(botId == this.yourBotId)
            this.deathsYourBot++;
        //else if(botId == this.opponentBotId)
            //this.deathsOpponentBot++;

        //WriteDebug("DEAD!\n");
        WriteDebug("DEATH COUNT: \n");
        WriteDebug("\t" + this.yourBotId +": " + this.deathsYourBot + "\n");
        //WriteDebug("\t" + this.opponentBotId +": " + this.deathsOpponentBot + "\n");
        this.Initialise();
    }

    String getAction()
    {
        if(this.yourBotId == 0)
            this.opponentBotId = 1;
        else
            this.opponentBotId = 0;

        if(this.gameField == null)
            throw new IllegalArgumentException("gamefield is null");

        String[] field = this.gameField.split(",");
        String[][] field2d = new String[this.fieldHeight][this.fieldWidth];

        for(int i = 0; i < field.length; ++i)
        {
            int row = i / this.fieldWidth;
            int column = i % this.fieldWidth;
            field2d[row][column] = field[i];

            if(field[i].equals((this.yourBotId) + ""))
            {
                if(this.rowPrevious != -1)
                {
                    this.dirRow = row - this.rowPrevious;
                    this.dirCol = column - this.colPrevious;
                }
                this.rowPrevious = row;
                this.colPrevious = column;
            }
        }

        //WriteDebug("DIRECTION: " + "r: " + this.dirRow + ", c: " + this.dirCol + "\n");

        //check if in the next move we might hit a wall
        int futureRow = this.rowPrevious + this.dirRow;
        int futureCol = this.colPrevious + this.dirCol;

        //WriteDebug("\tFUTURE POS: " + "r: " + futureRow + ", c: " + futureCol + "\n");
        //if(futureRow >= 0 && futureRow <= this.fieldHeight - 1 && futureCol >= 0 && futureCol <= this.fieldWidth - 1)
            //WriteDebug("\tFUTURE CELL: " + field2d[futureRow][futureCol] + "\n");

        //if(futureRow < 0 || futureRow > this.fieldHeight - 1 || futureCol < 0 || futureCol > this.fieldWidth - 1
                //|| this.IsBadCell(field2d[futureRow][futureCol]))
        //{
            //fill left
//            int countLeft = fillCountRun(this.rowPrevious, this.colPrevious, field2d, FillDirection.LEFT, this.dirRow, this.dirCol);
            float countLeft = fillCountRun(this.rowPrevious, this.colPrevious, field2d, "left", this.dirRow, this.dirCol);

            //fill right
//            int countRight = fillCountRun(this.rowPrevious, this.colPrevious, field2d, FillDirection.RIGHT, this.dirRow, this.dirCol);
            float countRight = fillCountRun(this.rowPrevious, this.colPrevious, field2d, "right", this.dirRow, this.dirCol);

            float countForward = fillCountRun(this.rowPrevious, this.colPrevious, field2d, "forward", this.dirRow, this.dirCol);

            //WriteDebug("\tFILL LEFT: " + countLeft + ", FILL RIGHT: " + countRight + ", FILL FORWARD: " + countForward + "\n");
            //get the vector that points to the left of the player
            //int orRow_p = this.dirCol;
            //int orCol_p = -this.dirRow;
            int orRow_p = -this.dirCol;
            int orCol_p = this.dirRow;
            //return "left"; //TEMP
            //translate the turning vector to the new direction
            //go left if more fill
            float bestChoice = Math.max(Math.max(countLeft, countRight), countForward); //for now the best choice does not decide draws
            if(bestChoice == countLeft)
            {
                //return "left";
                orRow_p = -this.dirCol;
                orCol_p = this.dirRow;
            }
            //go right if more fill
            else if(bestChoice == countRight)
            {
                //return "right";
                orRow_p = this.dirCol;
                orCol_p = -this.dirRow;
            }
            //otherwise go forward (because more fill)
            else
            {
                orRow_p = this.dirRow;
                orCol_p = this.dirCol;
            }

            if(orRow_p == -1)
                return "up";
            else if(orRow_p == 1)
                return "down";
            else if(orCol_p == -1)
                return "left";
            else if(orCol_p == 1)
                return "right";
            else
                return "left";
        //}

        //otherwise we continue in the same direction
        //if(this.dirRow == -1)
            //return "up";
        //else if(this.dirRow == 1)
            //return "down";
        //else if(this.dirCol == -1)
            //return "left";
        //else if(this.dirCol == 1)
            //return "right";
        //else
            //return "left";

        //return "up";
    }

    //we now perform fill on left, right, and forward
    //int fillCountRun(int startRow, int startCol, String[][] field2d, FillDirection dir, int orRow, int orCol)
    float fillCountRun(int startRow, int startCol, String[][] field2d, String direction, int orRow, int orCol) {
        int orRow_p = 0;
        int orCol_p = 0;
        //create fill direction based on player
        if (direction == "left")
        {
            //left turn
            orRow_p = -orCol;
            orCol_p = orRow;
        }
        else if(direction == "right")
        {
            //right turn
            orRow_p = orCol;
            orCol_p = -orRow;
        }
        else if(direction == "forward")
        {
            orRow_p = orRow;
            orCol_p = orCol;
        }

        int[][] usedCells = new int[this.fieldHeight][this.fieldWidth];
        for(int r = 0; r < this.fieldHeight; ++r)
        {
            for(int c = 0; c < this.fieldWidth; ++c)
            {
                usedCells[r][c] = 0;
            }
        }

        float[] area_perim = fillCount(startRow + orRow_p, startCol + orCol_p, field2d, orRow_p, orCol_p, usedCells, startRow + orRow_p, startCol + orCol_p);
        float area = area_perim[0];
        float dist = area_perim[1];

        if(area == 0 || dist == 0)
        {
            return 0;
        }
        else
        {
            //float perimOfCircleWithArea = 2 * (float)Math.sqrt(area * Math.PI);//2 * (float)Math.PI * (float)Math.sqrt(area / Math.PI);
            //return (float)(area * area) * (perimOfCircleWithArea / (float)perim);
            //float avgDist = (float)dist / (float)area;
            //return (float)(area) - avgDist;
            return area;
        }
    }

    float[] fillCount(int startRow, int startCol, String[][] field2d, int orRow, int orCol, int[][] usedCells, int originRow, int originCol)
    {
        float returnBonus = 1.0f;
        if(startRow < 0 || startRow > this.fieldHeight - 1 || startCol < 0 || startCol > this.fieldWidth - 1
                || usedCells[startRow][startCol] == 1 ||
                //check for collision with ourselves or X's. We want a bonus to turn
                //  towards the player instead.
                this.IsBadCell(field2d[startRow][startCol]))
                //!field2d[startRow][startCol].equals(this.opponentBotId + "")))
        {
            float[] perimeter_block = {0.0f, 0.0f};
            return perimeter_block;
        }
        //else if(field2d[startRow][startCol].equals(this.opponentBotId + ""))
        //{
            //returnBonus = 1;//-10;
            //WriteDebug("PLAYER TURN: " + this.opponentBotId + "\n");
        //}

        usedCells[startRow][startCol] = 1;

        //left turn
        int leftRow = -orCol + startRow;
        int leftCol = orRow + startCol;
        float[] countLeft = fillCount(leftRow, leftCol, field2d, orRow, orCol, usedCells, originRow, originCol);

        //right turn
        int rightRow = orCol + startRow;
        int rightCol = -orRow + startCol;
        float[] countRight = fillCount(rightRow, rightCol, field2d, orRow, orCol, usedCells, originRow, originCol);

        //forward
        int forwardRow = orRow + startRow;
        int forwardCol = orCol + startCol;
        float[] countForward = fillCount(forwardRow, forwardCol, field2d, orRow, orCol, usedCells, originRow, originCol);

        int distX = Math.abs(originRow - startRow);
        int distY = Math.abs(originCol - startCol);
        int dist = distX + distY;
        float[] block = {returnBonus, dist};
        float[] culm = {0.0f, 0.0f};
        float gamma = 10.0f;//1.5f;
        // IF gamma > 1.0f, bot cares more about distant squares.
        // IF gamma < 1.0f, bot cares more about close squares.
        culm[0] = block[0] + gamma * (countLeft[0] + countRight[0] + countForward[0]); //area
        culm[1] = block[1] + gamma * (countLeft[1] + countRight[1] + countForward[1]); //distance

        //TODO: narrowness: measure of how many left and right blocks get counted. This could be
        // done either by counting left/right blocks or by starting with a long wall-strip on each
        // side of the bot that only counts forward (avoid left/right except at the start).

        return culm;
    }

    boolean IsBadCell(String cell)
    {
        return cell.equals("x") || cell.equals("0") || cell.equals("1");
    }

    boolean IsWallCell(String cell)
    {
        return cell.equals("x");
    }
}
