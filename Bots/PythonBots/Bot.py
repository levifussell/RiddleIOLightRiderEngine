import sys

class Bot():

    def __init__(self, your_botid):
        self.time_bank = 0
        self.time_per_move = 0
        self.playerNames = ""
        self.your_bot = ""
        self.your_botid = your_botid
        self.field_width = 16
        self.field_height = 16
        self.game_round = 0
        self.game_field = ""
    
    def get_action(self):
        raise NotImplementedError("Must implement get action")

    def on_dead(self):
        raise NotImplementedError("Must implement on dead method")

    def set_botid(self, your_bot_id):
        self.your_bot_id = your_bot_id

    def update_settings(self, setting, value):
        if setting == 'timebank':
            self.time_bank = value            
        elif setting == 'time_per_move':
            self.time_per_move = value
        elif setting == 'player_names':
            self.player_names = value
        elif setting == 'your_bot':
            self.your_bot = value
        elif setting == 'your_botid':
            self.your_botid = value
        elif setting == 'field_width':
            self.field_width = value
        elif setting == 'field_height':
            self.field_height = value            

    def update_game(self, setting, value):
        if setting == 'round':
            self.game_round = value
        elif setting == 'field':
            self.game_field = value
            

    def run_command(self, command):
        command_type = command[0]

        if command_type == 'settings':
            self.update_settings(command[1], command[2])
        elif command_type == 'update':
            self.update_game(command[2], command[3])
        elif command_type == 'action':
            if command[3] == self.your_botid:                
                action = self.get_action()            
                print('%s' % action)
        elif command_type == 'dead':
            if command[2] != self.your_botid:
                print('reset')        
    
    def run_bot(self):
        not_finished = True
        while(not_finished):
            try:
                current_line = sys.stdin.readline().split()                
                self.run_command(current_line)                    
            except EOFError:
                break
            except KeyboardInterrupt:
                raise
            #except:
                # don't raise error or return so that bot attempts to stay alive
                #traceback.print_exc(file=sys.stderr)
                #sys.stderr.flush()

