from blockworld import BlockWorldEnv
import random
import numpy

class QLearning():
	# don't modify the methods' signatures!
	def __init__(self, env: BlockWorldEnv):
		self.env = env
		self.q_learning_dict = {}
		self.alpha = 0.1
		self.epsilon = 0.9
		self.gamma = 0.9

	def train(self):
		# get state and goal to train on
		current_state, current_goal = self.env.reset()
		q_learning_dict = self.q_learning_dict
		# until we are stopped train
		while True:
			# get action to explore
			action = self.my_act(current_state, current_goal)
			state_and_goal, reward, finished = self.env.step(action)
			future_state = state_and_goal[0]
			future_goal = state_and_goal[1]
			current_best_q_value = -numpy.inf
			# get list of future actions
			future_actions= future_state.get_actions()

			# set baseline value for dict if the entry does not exist yet
			if (action, current_state, current_goal) not in q_learning_dict:
				q_learning_dict[(action, current_state, current_goal)] = 0

			# check rewards for future state
			for future_action in future_actions:
				# baseline value
				future_action_q_value = 0

				# if already in dictionary get that value
				if (future_action, future_state, future_goal) in q_learning_dict:
					future_action_q_value = q_learning_dict[(future_action, future_state, future_goal)]

				# if we improved the value set it as new best
				if future_action_q_value > current_best_q_value:
					current_best_q_value = future_action_q_value

			# increment the value of dict based on the math figure given in seminar 7
			q_learning_dict[(action, current_state, current_goal)] += self.alpha * \
				  	(reward + self.gamma * current_best_q_value - q_learning_dict[action, current_state, current_goal])

			# if we solved current state get new one
			if finished:
				current_state, current_goal = self.env.reset()
			# otherwise change states to correspond
			else:
				current_state = future_state
				current_goal = future_goal

	def my_act(self, current_state, current_goal):
		# get our trained dictionary
		q_learning_dict = self.q_learning_dict
		# get possible actions
		available_actions = current_state.get_actions()

		# at first, we have random choice in case we cannot find any action or if we want to explore
		current_best_q_value = -numpy.inf
		best_action = random.choice(available_actions)

		# in 9 out of 10 times we try to find our best found action otherwise we explore with random action
		if numpy.random.uniform() < self.epsilon:
			# search if we can find better action
			for action in available_actions:

				if (action, current_state, current_goal) in q_learning_dict:
					dict_q_value = q_learning_dict[(action, current_state, current_goal)]

					if dict_q_value > current_best_q_value:
						current_best_q_value = dict_q_value
						best_action = action

		return best_action

	def act(self, s):
		# get our trained dictionary
		q_learning_dict = self.q_learning_dict
		# s is tuple containing state and goal
		current_state = s[0]
		current_goal = s[1]
		# get possible actions
		available_actions = current_state.get_actions()

		# set default values -> default get random action
		current_best_q_value = -numpy.inf
		best_action = random.choice(available_actions)

		# search if we can find better action
		for action in available_actions:

			if (action, current_state, current_goal) in q_learning_dict:
				dict_q_value = q_learning_dict[(action, current_state, current_goal)]

				if dict_q_value > current_best_q_value:
					current_best_q_value = dict_q_value
					best_action = action

		return best_action

if __name__ == '__main__':
	# Here you can test your algorithm. Stick with N <= 4
	N = 4

	env = BlockWorldEnv(N)
	qlearning = QLearning(env)

	# Train
	qlearning.train()

	# Evaluate
	test_env = BlockWorldEnv(N)

	test_problems = 10
	solved = 0
	avg_steps = []

	for test_id in range(test_problems):
		s = test_env.reset()
		done = False

		print(f"\nProblem {test_id}:")
		print(f"{s[0]} -> {s[1]}")

		for step in range(50): 	# max 50 steps per problem
			a = qlearning.act(s)
			s_, r, done = test_env.step(a)

			print(f"{a}: {s[0]}")

			s = s_

			if done:
				solved += 1
				avg_steps.append(step + 1)
				break

	avg_steps = sum(avg_steps) / len(avg_steps)
	print(f"Solved {solved}/{test_problems} problems, with average number of steps {avg_steps}.")