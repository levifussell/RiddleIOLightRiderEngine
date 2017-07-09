#!/bin/bash
if [ -p fifo0 ]; then
    rm fifo0
    rm fifo1
    rm fifo2
fi

if [ -f output.log ]; then
    rm output.log
fi

mkfifo fifo0 fifo1 fifo2
< fifo0 stdbuf -o0 ./Engine/run.out p | tee -a fifo1 fifo2 output.log 1> /dev/null &
< fifo1 stdbuf -o0 java -cp Bots/JavaBots TyrviBot 2 | tee -a fifo0 output.log 1> /dev/null &
< fifo2 stdbuf -o0 java -cp Bots/JavaBots LeviFuBot 3 | tee -a fifo0 output.log 1> /dev/null
