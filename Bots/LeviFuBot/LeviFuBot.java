

public class LeviFuBot extends Bot
{

    String getAction()
    {
        String[] field = this.gameField.split(",");
        String[][] field2d = new String[this.fieldWidth][this.fieldHeight];
        for(int i = 0; i < field.length; ++i)
        {
            int row = (int)(i / this.fieldWidth);
            int column = i % this.fieldWidth;
            field2d[row][column] = field[i];
        }

        //we now perform fill on left, right, and forward

        return "up";
    }

    //int fillCount(

}
