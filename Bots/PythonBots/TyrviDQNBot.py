from Bot import Bot
from sys import argv

import numpy as np
from keras.models import Sequential
from keras.layers import Dense, Activation, Flatten
from keras.optimizers import Adam

from rl.agents.dqn import DQNAgent
from rl.policy import BoltzmannQPolicy
from rl.memory import SequentialMemory

class TyrviDQNBot(Bot):
    
    def __init__(self):
        model = Sequential()
        model.add(Flatten(input_shape=(256,)))
        modle.add(Dense(130))
        model.add(Activation('relu'))
        model.add(Dense(3))
        model.add(Activation('linear'))
        memory = SequentialMemory(limit=50000, window_length=1)
        policy = BoltzmannQPolicy()
        dqn = DQNAgent(model=model, nb_actions=3, memory=memory, nb_steps_warmup=10,
                       target_model_update=1e-2, policy=policy)

    def convert_field_to_matrix(self, field):
        matrix = field.split(',')
        for i in range(len(matrix)):
            if matrix[i] == '.':
                matrix[i] = 0
            elif matrix[i] == 'x':
                matrix[i] = 1
            elif matrix[i] == '0':
                matrix[i] = 2
            elif matrix[i] == '1':
                matrix[i] = 3
                
        return matrix
                        
    def get_action(self):
        return 'up'

    def on_dead(self):
        return


if __name__ == '__main__':
    if len(argv) < 2:
        raise IndexError("Must provide bot id as argument")
    tyrvi = TyrviDQNBot(argv[1])
    tyrvi.run_bot()
