import random
# udělat rozhodování na základě "hodnoty" polí dohromady s počtem otočených kamenů


class MyPlayer:
    """Player plays randomly"""
    def __init__(self, my_color, opponent_color):
        self.name = "stankpe4"
        self.my_color = my_color
        self.opponent_color = opponent_color
        self.empty = -1
        self.valid_moves = []
        self.better_moves = []
        # best squares
        self.corners = [(0, 0), (7, 7), (0, 7), (7, 0)]
        # little bit better squares
        self.middle = [(2, 2), (2, 3), (2, 4), (2, 5), (3, 2), (3, 3), (3, 4), (3, 5),
                       (4, 2), (4, 3), (4, 4), (4, 5), (5, 2), (5, 3), (5, 4), (5, 5)]
        # worse squares
        self.worse_squares = [(3, 0), (4, 0), (0, 3), (0, 4), (7, 3), (7, 4), (3, 7), (4, 7)]
        # worst squares
        self.worst_squares = [(1, 1), (1, 6), (6, 1), (6, 6)]
        # 2nd worst squares
        self.second_worst_squares = [(0, 1), (1, 0), (6, 0), (7, 1), (0, 6), (1, 7), (7, 6), (6, 7)]
        # 2nd best squares
        self.second_best_squares = [(0, 2), (0, 5), (7, 2), (7, 5), (2, 0), (5, 0), (2, 7), (5, 7)]
        # normal squares
        self.normal_squares = [(2, 1), (3, 1), (4, 1), (5, 1), (2, 6), (3, 6), (4, 6), (5, 6),
                               (1, 2), (1, 3), (1, 4), (1, 5), (6, 2), (6, 3), (6, 4), (6, 5)]
        self.directions = [(-1, 0), (-1, -1), (-1, 1), (0, -1), (0, 1), (1, 1), (1, -1), (1, 0)]

    # function checks possible directions and checks  when move would go outsisde of chessboard
    def check(self, x, y, direction):
        x_moved = x + direction[0]
        y_moved = y + direction[1]
        return (0 <= x_moved <= 7) and (0 <= y_moved <= 7)

    # function uses previous function check to find all possible moves
    def find_valid_moves(self, board):
        # restart of possible move for every move
        self.valid_moves = []
        for row in range(len(board)):
            for column in range(len(board[row])):
                if board[row][column] == self.my_color:
                    for direction in self.directions:
                        # creation of "potential move"
                        x = column
                        y = row
                        opponent_pieces_count = 0
                        control = self.check(x, y, direction)
                        while control:
                            x += direction[0]
                            y += direction[1]
                            if board[y][x] == self.opponent_color:
                                opponent_pieces_count += 1
                                control = self.check(x, y, direction,)
                            elif board[y][x] == self.empty and opponent_pieces_count > 0:
                                self.valid_moves.append((y, x))
                                break
                            elif board[y][x] == self.empty:
                                break
                            elif board[y][x] == self.my_color:
                                break

    # functions goes through all valid moves and assigns value to them to evaluate which are the best
    def find_better_moves(self):
        biggest_move_value = -4
        for move in self.valid_moves:
            if move in self.corners:
                move_value = 3
            elif move in self.middle:
                move_value = 1
            elif move in self.worse_squares:
                move_value = -1
            elif move in self.worst_squares:
                move_value = -3
            elif move in self.second_worst_squares:
                move_value = -2
            elif move in self.second_best_squares:
                move_value = 2
            else:
                move_value = 0
            if move_value > biggest_move_value:
                self.better_moves = []
                self.better_moves.append(move)
            if move_value == biggest_move_value:
                self.better_moves.append(move)

    # function calls other functions that are responsible for picking moves and returns chosen move
    def move(self, board):
        self.find_valid_moves(board)
        self.find_better_moves()
        if not self.valid_moves == []:
            return random.choice(self.better_moves)
        elif not self.valid_moves:
            return None
