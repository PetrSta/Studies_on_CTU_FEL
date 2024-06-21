from blockworld import BlockWorld
from queue import PriorityQueue


# noinspection PyShadowingNames
class BlockWorldHeuristic(BlockWorld):
	def __init__(self, num_blocks=5, state=None):
		BlockWorld.__init__(self, num_blocks, state)

	def heuristic(self, goal):
		self_state = self.get_state()
		goal_state = goal.get_state()

		# find amount of elements
		amount_of_elements = 0

		for i in goal_state:
			amount_of_elements += len(i)

		# initialize variables
		x = 1
		goal_tuple = ()
		state_tuple = ()
		heuristic_value = 0

		while x <= amount_of_elements:
			# find in which tuples is x stored -> we need to check if x is placed correctly
			for i in goal_state:
				if x in i:
					goal_tuple = i
			for j in self_state:
				if x in j:
					state_tuple = j

			# if both tuples contain only one element no need to check anything
			if len(state_tuple) == 1 and len(goal_tuple) == 1:
				x += 1
				continue

			# loop over tuples from the end -> if we find any non-matching element before we find the element
			# we are looking for it means the element we are looking for is not place correctly
			index = -1
			limit = -min(len(goal_tuple), len(state_tuple))
			continue_looping = True
			while continue_looping:
				# if we find non-matching element increment heuristic -> lower priority of state
				if goal_tuple[index] != state_tuple[index]:
					heuristic_value += 1
					continue_looping = False
				# if we find the element we are looking for end search
				elif goal_tuple[index] == state_tuple[index] and goal_tuple[index] == x:
					continue_looping = False
				# if to make sure we can never get out of bounds -> shouldn´t happen even without it
				if index == limit:
					continue_looping = False
				else:
					index -= 1
			x += 1

		return heuristic_value


# noinspection PyShadowingNames
class AStar:
	@staticmethod
	def backtrack(closed, start, goal):
		# get values we need from dictionary
		action = closed[goal][0]
		prev_state = closed[goal][1]
		# start creating path
		result_path = [action]
		# until we reach continue backtracking
		while prev_state != start:
			# get values from dictionary and append path
			action = closed[prev_state][0]
			prev_state = closed[prev_state][1]
			result_path.append(action)

		# since we want path from start and not from goal to start we need to reverse the path
		result_path.reverse()
		return result_path

	def search(self, start, goal):
		state = start.clone()
		# priority queue to manage state priority easily
		found_states = PriorityQueue()
		# add first state to queue -> start, 0 is for heuristic value -> value to sort priority queue
		found_states.put((0, state))
		# dictionary to store information about explored states -> easy access using states as keys
		# previous action, previous state, cost, heuristic value -> start has no previous action, state, costs 0
		# and no heuristic needs to be calculated
		heuristic_value = 0
		searched_states = {state: [None, None, 0, heuristic_value]}

		# while True could be used, but issue would arise if we couldn't locate goal
		while not found_states.empty():
			# get state from queue, no need for heuristic value
			current_state = found_states.get()[1]

			# if we found goal return path to it
			if current_state == goal:
				return self.backtrack(searched_states, start.clone(), goal)
			else:
				# if goal was not found get all state reachable from current_state
				for action, neighbor in current_state.get_neighbors():
					# since we just used another action increment cost of state
					new_cost = searched_states[current_state][2] + 1

					if neighbor not in searched_states or new_cost < searched_states[neighbor][2]:
						# if we already have heuristic_value stored for neighbor
						# there is no need to calculate it again
						if neighbor in searched_states:
							heuristic_value = searched_states[neighbor][3]
						# otherwise calculate heuristic_value for neighbor
						else:
							heuristic_value = neighbor.heuristic(goal)
						# we need to check how high heuristic value to give to the new state
						# heuristic is equal to amount of boxes placed in wrong spot
						# priority takes heuristic value and add cost to punish long sequences of actions
						priority = heuristic_value + new_cost
						# add new state to priority queue
						found_states.put((priority, neighbor))
						# update / add information about explored state
						searched_states[neighbor] = (action, current_state, new_cost, heuristic_value)
		# only reachable if we cant locate goal -> safety measure
		return None

if __name__ == '__main__':
	# Here you can test your algorithm. You can try different N values, e.g. 6, 7.
	N = 10

	start = BlockWorldHeuristic(N)
	goal = BlockWorldHeuristic(N)

	print("Searching for a path:")
	print(f"{start} -> {goal}")
	print()

	astar = AStar()
	path = astar.search(start, goal)

	if path is not None:
		print("Found a path:")
		print(path)

		print("\nHere's how it goes:")

		s = start.clone()
		print(s)

		for a in path:
			s.apply(a)
			print(s)

	else:
		print("No path exists.")

	print("Total expanded nodes:", BlockWorld.expanded)