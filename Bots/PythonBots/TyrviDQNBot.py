from Bot import Bot
from sys import argv

class TyrviDQNBot(Bot):
   
    def get_action(self):
        return 'up'

    def on_dead(self):
        return


if __name__ == '__main__':
    if len(argv) < 2:
        raise IndexError("Must provide bot id as argument")
    tyrvi = TyrviDQNBot(argv[1])
    tyrvi.run_bot()
