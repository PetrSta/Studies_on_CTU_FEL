import random
import time
import numpy as np
import ox

class State:
    def __init__(self, input_board):
        size = input_board.size
        actions = input_board.get_actions()
        self.values = np.zeros(size * size)
        self.visits = np.zeros(size * size)
        self.actions = (tuple(actions))
        self.total_visits = 0
        self.lastAction = ()

    def update(self, value):
        # update values
        self.total_visits += 1
        index = self.lastAction
        self.visits[index] += 1
        increment = (value - self.values[index]) / (self.visits[index])
        self.values[index] += increment

    def ucb_value(self, action):
        # cant divide by 0, give priority to these
        if self.visits[action] == 0:
            return_value = np.Infinity
        # 5 stands for exploration value
        else:
            return_value = self.values[action] + 5 * np.sqrt(np.log(self.total_visits) / self.visits[action])

        return return_value

    def select_action(self, update_last_action):
        values = []
        # get values for each action
        for action in self.actions:
            if update_last_action:
                value = self.ucb_value(action)
                if value == np.Infinity:
                    self.lastAction = action
                    return action
            else:
                value = self.values[action]
            values.append(value)
        # get max value for all actions and get action that corresponds to it
        max_value = max(values)
        index_of_max_value = values.index(max_value)
        selected_action = self.actions[index_of_max_value]

        if update_last_action:
            self.lastAction = selected_action

        return selected_action


class MCTSBot:

    def __init__(self, play_as: int, time_limit: float):
        self.play_as = play_as
        self.time_limit = time_limit * 0.95
        self.initial_state = None
        self.states = {}

    @staticmethod
    def rollout(rollout_board):
        while not rollout_board.is_terminal():
            action = random.choice(tuple(rollout_board.get_actions()))
            rollout_board.apply_action(action)

        return rollout_board.get_rewards()

    def select_from_board(self, board_to_select_from):
        history = []
        # get best action from state and write history how we got to it
        while board_to_select_from in self.states:
            if board_to_select_from.is_terminal():
                break

            history.append(board_to_select_from.clone())
            action = self.states[board_to_select_from].select_action(True)
            board_to_select_from.apply_action(action)

        return history, board_to_select_from.clone()

    def single_search(self, initial_board):
        # single step in searching
        history, last_board = self.select_from_board(initial_board)
        # get our rewards
        if last_board.is_terminal():
            values = last_board.get_rewards()
        else:
            self.states[last_board] = State(last_board)
            values = self.rollout(last_board.clone())
        # backpropagation
        reversed_history = reversed(history)

        for state in reversed_history:
            self.states[state].update(values[state.current_player()])

    def play_action(self, initial_board):
        # function that controls the time usage for our search and is call to begin the search
        if not initial_board in self.states:
            self.states[initial_board.clone()] = State(initial_board.clone())

        self.initial_state = self.states[initial_board]

        start_time = time.time()
        while (time.time() - start_time) < self.time_limit:
            self.single_search(initial_board.clone())

        return self.initial_state.select_action(False)

#"""
if __name__ == '__main__':
    board = ox.Board(8)

    # 8x8
    bots = [MCTSBot(0, 1.0), MCTSBot(1, 1.0)]

    while not board.is_terminal():
        current_player = board.current_player()
        current_player_mark = ox.MARKS_AS_CHAR[ox.PLAYER_TO_MARK[current_player]]

        current_bot = bots[current_player]
        a = current_bot.play_action(board)
        board.apply_action(a)

        print(f"{current_player_mark}: {a} -> \n{board}\n")
#"""
