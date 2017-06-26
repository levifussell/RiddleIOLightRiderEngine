if [ -p fifo0 ]; then
    rm fifo0
    rm fifo1
fi

mkfifo fifo0 fifo1
./../run.out > fifo0 < fifo1 &
java JavaBot < fifo0 > fifo1

