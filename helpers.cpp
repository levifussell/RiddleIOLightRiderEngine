#include "helpers.h"

bool Helpers::stringCompare(char* a, char* b)
{
    bool allCharSame = true;

    int index = 0;
    while(a[index] != '\0' || b[index] != '\0')
    {
        if(a[index] != b[index])
        {
            allCharSame = false;
            break;
        }

        index++;
    }

    return allCharSame;
}
