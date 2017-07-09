#include <SFML/Graphics.hpp>
#include <iostream>
#include <vector>
#include <stdexcept>
#include <stdio.h>
#include <stdlib.h>
#include <string>
#include <unistd.h>

//COMPILE: g++ -o run.out main.cpp -lsfml-audio -lsfml-network -lsfml-graphics -lsfml-window -lsfml-system


struct Grid
{
    std::vector<std::vector<int> > data;
    int rows;
    int columns;
    int gridCellDrawSize;
};

const int SCREEN_WIDTH = 500;
const int SCREEN_HEIGHT = 500;
const int PLAYER1_VAL = 2;
const int PLAYER2_VAL = 3;
const int WALL_VAL = 1;
const int FLOOR_VAL = 0;

int roundCount;
bool player1_dead;
bool player2_dead;

int* mapLR();
Grid createGrid(int rows, int columns);
Grid createGridFromMap(int rows, int columns, int* map);
void printGrid(Grid grid, sf::RenderWindow& window);
void getPlayer(int& x, int& y, const Grid& grid, int NUM);
bool movePlayer(int xDir, int yDir, Grid& grid, int NUM);
bool movePlayerDown(Grid& grid, int NUM);
bool movePlayerUp(Grid& grid, int NUM);
bool movePlayerLeft(Grid& grid, int NUM);
bool movePlayerRight(Grid& grid, int NUM);
bool readPlayerAction(Grid& grid, int NUM, char* action);
void sendUpdateCommand(char* player, char* type, int value);
void sendUpdateCommandField(char* player, char* type, const Grid& grid);
void sendActionCommand(char* type, int time);
void sendActionPlayerCommand(char* type, int time, int player);
void printFieldString(const Grid& grid);
void runOnePlayerStep(Grid& grid, int NUM);
void runOneRound(Grid& grid, sf::RenderWindow* window, int NUM);

void resetGame(Grid& grid);

bool skipFirst;
bool player1Turn;
int playerIsDeadNum;

bool printScreen;
float milisecondsToNano = 1000;
int sleepTimeNanoseconds;

int main(int argc, char* argv[])
{
    //check for print toggle
    if(argc > 1)
    {
        printScreen = (argv[1][0] == 'p');
        sleepTimeNanoseconds = atoi(argv[2]) * milisecondsToNano;
    }
    else
        printScreen = false;

    srand(time(NULL));

    Grid grid;
    resetGame(grid);
    player1Turn = true;

    if(printScreen)
    {
        sf::RenderWindow window(sf::VideoMode(SCREEN_WIDTH, SCREEN_HEIGHT), "Light Rider");

        while(window.isOpen())
        {
            sf::Event event;
            while(window.pollEvent(event))
            {
                if(event.type == sf::Event::Closed)
                    window.close();
            }

            window.clear();

            if(player1Turn)
                runOneRound(grid, &window, PLAYER1_VAL);
            else
                runOneRound(grid, &window, PLAYER2_VAL);

            player1Turn = !player1Turn;

            window.display();

            //sleep a bit before continuing
            usleep(sleepTimeNanoseconds);
        }
    }
    else
    {
        while(true)
        {
            if(player1Turn)
                runOneRound(grid, NULL, PLAYER1_VAL);
            else
                runOneRound(grid, NULL, PLAYER2_VAL);

            player1Turn = !player1Turn;
        }
    }

    return 0;
}

void resetGame(Grid& grid)
{
    skipFirst = true;
    player1Turn = false;
    playerIsDeadNum = 0;
    grid = createGridFromMap(16, 16, mapLR());
    int p_row = rand() % 16;
    int p1_col = rand() % 7;
    int p2_col = 15 - p1_col;
    grid.data.at(p_row).at(p1_col) = PLAYER1_VAL;
    grid.data.at(p_row).at(p2_col) = PLAYER2_VAL;
    roundCount = 0;
}

Grid createGrid(int rows, int columns)
{
    Grid g;
    g.gridCellDrawSize = SCREEN_HEIGHT / rows;
    std::vector<std::vector<int> > data = std::vector<std::vector<int> >(rows);
    std::vector<std::vector<float> > values = std::vector<std::vector<float> >(rows);
    for(int r = 0; r < rows; ++r)
    {
        data.at(r) = std::vector<int>(columns);
        for(int c = 0; c < columns; ++c)
        {
            data.at(r).at(c) = (int)(((float)(rand()) / (float)(RAND_MAX)) + 0.5f);
        }
    }

    g.data = data;
    g.rows = rows;
    g.columns = columns;
    return g;
}

Grid createGridFromMap(int rows, int columns, int* map)
{
    Grid g;
    g.gridCellDrawSize = SCREEN_HEIGHT / rows;
    std::vector<std::vector<int> > data = std::vector<std::vector<int> >(rows);
    for(int r = 0; r < rows; ++r)
    {
        data.at(r) = std::vector<int>(columns);
        for(int c = 0; c < columns; ++c)
        {
            data.at(r).at(c) = map[r * columns + c];
        }
    }

    g.data = data;
    g.rows = rows;
    g.columns = columns;
    return g;
}

void getPlayer(int& x, int& y, const Grid& grid, int NUM)
{
    for(int r = 0; r < grid.data.size(); ++r)
    {
        for(int c = 0; c < grid.data.at(r).size(); ++c)
        {
            if(grid.data.at(r).at(c) == NUM)
            {
                x = c;
                y = r;
            }
        }
    }
}

bool movePlayer(int xDir, int yDir, Grid& grid, int NUM)
{
    int xPos = -1;
    int yPos = -1;
    getPlayer(xPos, yPos, grid, NUM);

    if(xPos == -1 || yPos == -1)
        throw std::invalid_argument("cannot find player position.");

    int xPos_new = xPos + xDir >= 0 && xPos + xDir < grid.data.at(0).size() ? xPos + xDir : xPos;
    int yPos_new = yPos + yDir >= 0 && yPos + yDir < grid.data.size() ? yPos + yDir : yPos;

    //player is dead if collides with a wall, another player or itself (collides with itself when it hits a wall)
    bool player_dead = (grid.data.at(yPos_new).at(xPos_new) == WALL_VAL || grid.data.at(yPos_new).at(xPos_new) == PLAYER1_VAL
            || grid.data.at(yPos_new).at(xPos_new) == PLAYER2_VAL);

    //player can only move on white spaces, otherwise dies, therefore
    // we know we set it to white
    grid.data.at(yPos).at(xPos) = WALL_VAL;
    grid.data.at(yPos_new).at(xPos_new) = NUM;

    return player_dead;
}

bool movePlayerDown(Grid& grid, int NUM) { movePlayer(0, 1, grid, NUM); }
bool movePlayerUp(Grid& grid, int NUM) { movePlayer(0, -1, grid, NUM); }
bool movePlayerLeft(Grid& grid, int NUM) { movePlayer(-1, 0, grid, NUM); }
bool movePlayerRight(Grid& grid, int NUM) { movePlayer(1, 0, grid, NUM); }
bool readPlayerAction(Grid& grid, int NUM, std::string action)
{
    if(playerIsDeadNum == 0)
    {
        if(action.compare("up") == 0)
            return movePlayerUp(grid, NUM);
        else if(action.compare("down") == 0)
            return movePlayerDown(grid, NUM);
        else if(action.compare("left") == 0)
            return movePlayerLeft(grid, NUM);
        else if(action.compare("right") == 0)
            return movePlayerRight(grid, NUM);
    }
    else if(action.compare("reset") == 0)
    {
        resetGame(grid);
        return false;
    }
    else
        throw std::invalid_argument("invalid control input");
}

void sendUpdateCommand(char* player, char* type, int value)
{
    std::cout << "update " << player << " " << type << " " << value << "\n";
}
void sendUpdateCommandField(char* player, char* type, const Grid& grid)
{
    std::cout << "update " << player << " " << type << " ";
    printFieldString(grid);
    std::cout << "\n";
}
void sendActionCommand(char* type, int time)
{
    std::cout << "action " << type << " " << time << "\n";
}
void sendActionPlayerCommand(char* type, int time, int player)
{
    std::cout << "action " << type << " " << time <<  " " << player << "\n";
}
void printFieldString(const Grid& grid)
{
    //std::cout << "[";
    for(int r = 0; r < grid.rows; ++r)
    {
        for(int c = 0; c < grid.columns; ++c)
        {
            switch(grid.data.at(r).at(c))
            {
                case PLAYER1_VAL:
                    std::cout << "0";
                    break;
                case PLAYER2_VAL:
                    std::cout << "1";
                    break;
                case WALL_VAL:
                    std::cout << "x";
                    break;
                case FLOOR_VAL:
                    std::cout << ".";
                    break;
            }
            std::cout << ",";
        }
    }
    //std::cout << "]";
}
void runOnePlayerStep(Grid& grid, int NUM)
{

    if(playerIsDeadNum == 0)
    {
        //input for player
        sendActionPlayerCommand("move", 200, NUM);
    }

    std::string player_move;
    std::getline(std::cin, player_move);
    bool isDead = readPlayerAction(grid, NUM, player_move);
    if(isDead && playerIsDeadNum == 0)
    {
        playerIsDeadNum = NUM;
        std::cout << "dead player " << NUM << "\n";
    }

    //if(playerIsDead)
        //std::cout << "dead player " << NUM << "\n";
}
void runOneRound(Grid& grid, sf::RenderWindow* window, int NUM)
{
    if(playerIsDeadNum == 0)
    {
        sendUpdateCommand("game", "round", roundCount);
        sendUpdateCommandField("game", "field" , grid);
    }

    //input for player1
    runOnePlayerStep(grid, NUM);

    if(window != NULL)
        printGrid(grid, *window);

    //increment to next round
    roundCount++;
}

void printGrid(Grid grid, sf::RenderWindow& window)
{
    for(int r = 0; r < grid.data.size(); ++r)
    {
        for(int c = 0; c < grid.data.at(r).size(); ++c)
        {
            sf::RectangleShape rec;
            rec.setSize(sf::Vector2f(grid.gridCellDrawSize, grid.gridCellDrawSize));
            rec.setPosition(c * grid.gridCellDrawSize, r * grid.gridCellDrawSize);
            sf::Color col = sf::Color::Yellow;
            switch(grid.data.at(r).at(c))
            {
                case 0:
                default:
                    col = sf::Color::White;
                    break;
                case 1:
                    col = sf::Color::Black;
                    break;
                case 2:
                    col = sf::Color::Green;
                    break;
                case 3:
                    col = sf::Color::Red;
                    break;
            }
            rec.setFillColor(col);
            window.draw(rec);
        }
    }
}

int* mapLR()
{
    const int size = 16*16;
    static int map[size];
    for(int i = 0; i < size; ++i)
        map[i] = 0;

    return map;
}
