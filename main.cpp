#include <SFML/Graphics.hpp>
#include <iostream>
#include <vector>
#include <stdexcept>

#include "helpers.h"

//COMPILE: g++ main.cpp -lsfml-audio -lsfml-network -lsfml-graphics -lsfml-window -lsfml-system

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

int main()
{
    srand(time(NULL));

    //Grid grid = createGrid(5, 5);
    Grid grid = createGridFromMap(16, 16, mapLR());
    grid.data.at(0).at(0) = PLAYER1_VAL;
    grid.data.at(0).at(5) = PLAYER2_VAL;

    movePlayerDown(grid, PLAYER1_VAL);
    movePlayerDown(grid, PLAYER1_VAL);
    movePlayerRight(grid, PLAYER1_VAL);
    movePlayerRight(grid, PLAYER1_VAL);
    movePlayerDown(grid, PLAYER1_VAL);
    movePlayerDown(grid, PLAYER1_VAL);

    movePlayerDown(grid, PLAYER2_VAL);
    movePlayerDown(grid, PLAYER2_VAL);
    movePlayerRight(grid, PLAYER2_VAL);
    movePlayerRight(grid, PLAYER2_VAL);
    movePlayerDown(grid, PLAYER2_VAL);
    movePlayerDown(grid, PLAYER2_VAL);

    char* h = "hello";
    char* why = "hello";

    bool same = Helpers::stringCompare(h, why);
    std::cout << same << "\n";

    //delete h;
    //delete why;

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

        printGrid(grid, window);

        window.display();
    }

    return 0;
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
bool readPlayerAction(Grid& grid, int NUM, char* action)
{
    if(Helpers::stringCompare(action, "up"))
        movePlayerUp(grid, NUM);
    else if(Helpers::stringCompare(action, "down"))
        movePlayerDown(grid, NUM);
    else if(Helpers::stringCompare(action, "left"))
        movePlayerLeft(grid, NUM);
    else if(Helpers::stringCompare(action, "right"))
        movePlayerRight(grid, NUM);
    else
        throw std::invalid_argument("invalid control input");
}

void sendUpdateCommand(char* player, char* type, char* value)
{
    std::cout << "update " << player << " " << type << " " << value << "\n";
}
void sendActionCommand(char* type, char* time)
{
    std::cout << "action " << type << " " << time << "\n";
}
void sendActionPlayerCommand(char* type, char* time, char* player)
{
    std::cout << "action " << type << " " << time <<  " " << player << "\n";
}
char* getFieldString()
{

}
void runOneRound()
{
    sendUpdateCommand("game", "round", itoa(roundCount));
    sendUpdateCommand("game", "field", getFieldString());

    sendActionPlayerCommand("move", "200", itoa(PLAYER1_VAL));
    sendActionPlayerCommand("move", "200", itoa(PLAYER2_VAL));
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
