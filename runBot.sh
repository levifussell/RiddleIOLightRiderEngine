#!/bin/bash
if [ -p fifo0 ]; then
    rm fifo0
    rm fifo1
    rm fifo2
fi

mkfifo fifo0 fifo1 fifo2
./Engine/run.out p | tee fifo1 fifo2 < fifo1 &
java -cp Bots/ TyrviBot < fifo1 > fifo0 &
java -cp Bots/ TyrviBot < fifo2 > fifo0

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


