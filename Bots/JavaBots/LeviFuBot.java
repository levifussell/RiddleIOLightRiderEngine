import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class LeviFuBot extends Bot
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

    public LeviFuBot()
    {
        super();

        this.Initialise();
    }

    public static void main(String[] args) {

        Bot levifu = new LeviFuBot();
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

    protected void onDead()
    {
        WriteDebug("DEAD!\n");
        this.Initialise();
    }

    String getAction()
    {
        if(this.gameField == null)
            throw new IllegalArgumentException("gamefield is null");

        String[] field = this.gameField.split(",");
        String[][] field2d = new String[this.fieldHeight][this.fieldWidth];

        for(int i = 0; i < field.length; ++i)
        {
            int row = (int)(i / this.fieldWidth);
            int column = i % this.fieldWidth;
            field2d[row][column] = field[i];

            if(field[i].equals((this.yourBotId - 2) + ""))
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

        WriteDebug("DIRECTION: " + "r: " + this.dirRow + ", c: " + this.dirCol + "\n");

        //check if in the next move we might hit a wall
        int futureRow = this.rowPrevious + this.dirRow;
        int futureCol = this.colPrevious + this.dirCol;

        WriteDebug("\tFUTURE POS: " + "r: " + futureRow + ", c: " + futureCol + "\n");
        if(futureRow >= 0 && futureRow <= this.fieldHeight - 1 && futureCol >= 0 && futureCol <= this.fieldWidth - 1)
            WriteDebug("\tFUTURE CELL: " + field2d[futureRow][futureCol] + "\n");

        //if(futureRow < 0 || futureRow > this.fieldHeight - 1 || futureCol < 0 || futureCol > this.fieldWidth - 1
                //|| this.IsBadCell(field2d[futureRow][futureCol]))
        //{
            //fill left
//            int countLeft = fillCountRun(this.rowPrevious, this.colPrevious, field2d, FillDirection.LEFT, this.dirRow, this.dirCol);
            int countLeft = fillCountRun(this.rowPrevious, this.colPrevious, field2d, "left", this.dirRow, this.dirCol);

            //fill right
//            int countRight = fillCountRun(this.rowPrevious, this.colPrevious, field2d, FillDirection.RIGHT, this.dirRow, this.dirCol);
            int countRight = fillCountRun(this.rowPrevious, this.colPrevious, field2d, "right", this.dirRow, this.dirCol);

            int countForward = fillCountRun(this.rowPrevious, this.colPrevious, field2d, "forward", this.dirRow, this.dirCol);

            WriteDebug("\tFILL LEFT: " + countLeft + ", FILL RIGHT: " + countRight + ", FILL FORWARD: " + countForward + "\n");
            //get the vector that points to the left of the player
            //int orRow_p = this.dirCol;
            //int orCol_p = -this.dirRow;
            int orRow_p = -this.dirCol;
            int orCol_p = this.dirRow;
            //return "left"; //TEMP
            //translate the turning vector to the new direction
            //go left if more fill
            int bestChoice = Math.max(Math.max(countLeft, countRight), countForward); //for now the best choice does not decide draws
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
    int fillCountRun(int startRow, int startCol, String[][] field2d, String direction, int orRow, int orCol) {
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

        return fillCount(startRow + orRow_p, startCol + orCol_p, field2d, orRow_p, orCol_p, usedCells);
    }

    int fillCount(int startRow, int startCol, String[][] field2d, int orRow, int orCol, int[][] usedCells)
    {
        if(startRow < 0 || startRow > this.fieldHeight - 1 || startCol < 0 || startCol > this.fieldWidth - 1
                || usedCells[startRow][startCol] == 1 || this.IsBadCell(field2d[startRow][startCol]))
            return 0;

        usedCells[startRow][startCol] = 1;

        //left turn
        int leftRow = -orCol + startRow;
        int leftCol = orRow + startCol;
        int countLeft = fillCount(leftRow, leftCol, field2d, orRow, orCol, usedCells);

        //right turn
        int rightRow = orCol + startRow;
        int rightCol = -orRow + startCol;
        int countRight = fillCount(rightRow, rightCol, field2d, orRow, orCol, usedCells);

        //forward
        int forwardRow = orRow + startRow;
        int forwardCol = orCol + startCol;
        int countForward = fillCount(forwardRow, forwardCol, field2d, orRow, orCol, usedCells);

        return 1 + countLeft + countRight + countForward;
    }

    boolean IsBadCell(String cell)
    {
        return cell.equals("x") || cell.equals("0") || cell.equals("1");
    }

}
