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
< fifo0 stdbuf -o0 ./Engine/run.out p 0 | tee -a fifo1 fifo2 output.log 1> /dev/null &
< fifo1 stdbuf -o0 java -cp Bots/JavaBots LeviFuBot 0 | tee -a fifo0 output.log 1> /dev/null & #GREEEN
< fifo2 stdbuf -o0 java -cp Bots/JavaBots LeviFuBotDensityFill 1 | tee -a fifo0 output.log 1> /dev/null #RED

# die() {
#    printf '%s\n' "$1" >&2
#    exit 1
# }

# exists() {
#     if [ ! -f $1 ]; then
#         printf 'File does not exist %s\n' "$1" >&2
#         exit 1
#     fi
# }

# show_usage() {
#     echo "HELP"
# }

# MINARGS=2
# graphicsMode=false
# bot1=false
# bot2=false

# # case $1 in
# #     ""|"-h"|"--help") show_usage ;;
# # esac

# # ARGC=$#

# # if [[ $ARGC -lt $MINARGS ]]; then
# #     echo "Too few arguments given. Must provide exactly (Minimum:$MINARGS) bots"
# #     echo
# #     show_usage
# # fi

# # for WORD in 

# while :; do
#     case $1 in
#         -g)
#             echo "graphics mode enabled"
#             graphicsMode=true
#             >&2
#             ;;
#         -j)
#             exists "$2"
#             if [ "$2" ]; then
#                 if [ bot1=false ]; then
#                     bot1="java ${2%%.*}"
#                 else
#                     bot2="java ${2%%.*}"
#                 fi
#                 shift 2
#             else
#                 die 'ERROR: "-j" requires a compiled java file'
#             fi
#             ;;
#         -p)
#             exists "$2"
#             if [ "$2" ]; then
#                 if [ bot1=false ]; then
#                     bot1="python $2"
#                 else
#                     bot2="python $2"
#                 fi
#                 shift 2
#             else
#                 die 'ERROR: "-p" requires a python file'
#             fi
#             ;;
#         -c)
#             exists "$2"
#             if [ "$2" ]; then
#                 if [ bot1=false ]; then
#                     bot1="./$2"
#                 else
#                     bot2="./$2"
#                 fi
#                 shift 2
#             else
#                 die 'ERROR: "-c" requires a compiled C/C++ file'
#             fi
#             ;;
#         --)
#             shift
#             break
#             ;;        
#         -?*)
#             printf 'WARNING: Unkown option (ignored): %s\n' "$1" >&2
#             ;;
#         *)
#             break   
#     esac
#     shift
# done

# echo "Bot1: ${bot1}"
# echo "Bot2: ${bot2}"

# if [ bot1=false ] || [ bot2=false ]; then
#     die 'ERROR: please make sure you provided 2 bots'
# fi
