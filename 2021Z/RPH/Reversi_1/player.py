import random


class MyPlayer:
    """Hráč hraje náhodně"""
    # konstruktor
    def __init__(self, my_color, opponent_color):
        self.name = "stankpe4"
        self.my_color = my_color
        self.opponent_color = opponent_color
        self.empty = -1
        self.valid_moves = []
        self.directions = [(-1, 0), (-1, -1), (-1, 1), (0, -1), (0, 1), (1, 1), (1, -1), (1, 0)]

    # funkce prochází jednotlivé směry a stanovuje podmínky kdy už by posun šel ze šachovnice
    def check(self, x, y, direction):
        x_moved = x + direction[0]
        y_moved = y + direction[1]
        return (0 <= x_moved <= 7) and (0 <= y_moved <= 7)

    def find_valid_moves(self, board):
        # resetování možných tahů pro každý tah
        self.valid_moves = []
        # cykly pro procházení matice
        for i in range(len(board)):
            for j in range(len(board[i])):
                # pokud najdu svůj kámen začnu zjišťovat zda s ním jde hrát
                if board[i][j] == self.my_color:
                    # cyklus procházející jednotlivé směry
                    for direction in self.directions:
                        # vytvoření "potencionálního tahu"
                        x = j
                        y = i
                        # kontrola jestli pohyby ve směru už přešli alespoň jeden soupeřův kámen
                        at_least_one = 0
                        # volání kontroly jestli nehrozí opuštění šachovnice
                        control = self.check(x, y, direction)
                        # probíhá pouze pokud nehrozí opuštění šachovnice
                        while control:
                            # posunutí v daném směru
                            x += direction[0]
                            y += direction[1]
                            # pokud pohyb "jde" přes soupeřův kámen
                            if board[y][x] == self.opponent_color:
                                # potvrzení že pohyb přešel soupeřův kámen
                                at_least_one += 1
                                # volání kontroly jestli nehrozí opuštění šachovnice
                                control = self.check(x, y, direction,)
                            # pokud pohyb "jde" přes prázdné pole a už šel alespoň před jeden soupeřův kámen
                            elif board[y][x] == self.empty and at_least_one > 0:
                                # přidání tahu do možných tahů
                                self.valid_moves.append((y, x))
                                break
                            # pokud pohyb "jde" přes prázdné pole
                            elif board[y][x] == self.empty:
                                break
                            # pokud pohyb "jde" přes vlastní kámen
                            elif board[y][x] == self.my_color:
                                break
                            # jinak
                            else:
                                break

    # funkce volající fuknce zodpovědné za provedení tahu sama navrací zahraný tah
    def move(self, board):
        self.find_valid_moves(board)
        # pokud předchozí funkce našla platné tahy vyber náhodně tah
        if not self.valid_moves == []:
            return random.choice(self.valid_moves)
        # pokud funkce nenašla platný tah vynechej
        elif not self.valid_moves:
            return None


if __name__ == "__main__":
    pass
