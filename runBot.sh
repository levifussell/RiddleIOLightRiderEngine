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
< fifo0 stdbuf -o0 ./Engine/run.out p 200 | tee -a fifo1 fifo2 output.log &
< fifo1 stdbuf -o0 python Bots/PythonBots/TyrviDQNBot.py 1 | tee -a fifo0 output.log &
< fifo2 stdbuf -o0 java -cp Bots/JavaBots LeviFuBot 0 | tee -a fifo0 output.log
