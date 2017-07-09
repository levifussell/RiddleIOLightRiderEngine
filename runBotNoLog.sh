#!/bin/bash
if [ -p fifo0 ]; then
    rm fifo0
    rm fifo1
    rm fifo2
fi

mkfifo fifo0 fifo1 fifo2
< fifo0 stdbuf -o0 ./Engine/run.out | tee -a fifo1 fifo2 1> /dev/null &
< fifo1 stdbuf -o0 java -cp Bots/JavaBots TyrviBot 2 | tee -a fifo0 1> /dev/null &
< fifo2 stdbuf -o0 java -cp Bots/JavaBots TyrviBot 3 | tee -a fifo0 1> /dev/null
