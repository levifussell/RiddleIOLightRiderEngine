import argparse
from sys import exit
from os import path, mkfifo, unlink
from subprocess import Popen

parser = argparse.ArgumentParser()
parser.add_argument('-g', '--graphics', action='store_const', const=True)
parser.add_argument('-j', '--java', nargs='*', default=[])
parser.add_argument('-p', '--python', nargs='*', default=[])
parser.add_argument('-c', nargs='*', default=[])

args = parser.parse_args()

print args

files = args.c+args.python+args.java

# make sure only 2 bots provided
if len(files) != 2:
    exit("ERROR: please provide 2 bots")

# make sure bot paths exist
if not path.isfile(files[0]):
    exit("ERROR: " + files[0] + " is not a file")
elif not path.isfile(files[1]):
    exit("ERROR: " + files[1] + " is not a file")

print "bot1="+files[0]+" bot2="+files[1]

pipe0 = "fifo0"
pipe1 = "fifo1"

try:
    mkfifo(pipe0)
except OSError:
    unlink(pipe0)
    mkfifo(pipe0)

try:
    mkfifo(pipe1)
except OSError:
    unlink(pipe1)
    mkfifo(pipe1)

# assign bot1 to type
if files[0].split(".")[-1] == "class":
    bot1 = ["java"] + [files[0].split(".")[0]]
elif files[0].split(".")[-1] == "py":
    bot1 = ["python"] + [files[0]]
else:
    bot1 = "./" + files[0]

# assign bot2 to type
if files[1].split(".")[-1] == "class":
    bot2 = ["java"] + [files[1].split(".")[0]]
elif files[1].split(".")[-1] == "py":
    bot2 = ["python"] + [files[1]]
else:
    bot2 = "./" + files[1]

#print "bot1: "+bot1+" bot2: "+bot2

if args.graphics:
    engine = ["./Engine/run.out", "p"]
else:
    engine = ["./Engine/run.out"]

command = engine + ['>', 'fifo0', '<', 'fifo1', '&'] + bot1 + ['<', 'fifo0', '>', 'fifo1', '&'] + bot2 + ['<', 'fifo0', '>', 'fifo1']

print command

runEngine = Popen(engine, stdin=pipe1, stdout=pipe0)
runBot1 = Popen(bot1, stdin=pipe0, stdout=pipe1)
runBot2 = Popen(bot2, stdin=pipe0, stdout=pipe1)
