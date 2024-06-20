import random
COOPERATE = False
DEFECT = True


class MyPlayer:
    """Hráč se snaží upravit strategii podle payoff matice a taktiky soupeře"""
    _ps_identification = 0

    def __init__(self, payoff_matrix, number_of_iterations=0):
        # identifikace hráče pro hru proti sobě samému
        MyPlayer._ps_identification += 1
        # matice, tahy a počet opakování
        self.payoff_matrix = payoff_matrix
        self.number_of_iterations = number_of_iterations
        self.my_moves = []
        self.opponent_moves = []
        # jednotlivé hodnoty matice pro hru proti sobě samému
        self.double_cooperate = payoff_matrix[0][0][0] + payoff_matrix[0][0][1]
        self.double_defect = payoff_matrix[1][1][0] + payoff_matrix[1][1][1]
        self.cooperate_defect = payoff_matrix[0][1][0] + payoff_matrix[0][1][1]
        # hodnoty matice pro dominantní strategii
        self.cooperate__cooperate = payoff_matrix[0][0][0]
        self.cooperate__defect = payoff_matrix[0][1][0]
        self.defect__cooperate = payoff_matrix[0][1][1]
        self.defect__defect = payoff_matrix[1][1][0]
        # boolean proměnné
        self.short_game = self.number_of_iterations in range(1, 2)
        self.failsafe = False
        self.yourself = False
        self.count_generous = False

    def record_last_moves(self, my_last_move, opponent_last_move):
        self.my_moves.append(my_last_move)
        self.opponent_moves.append(opponent_last_move)
        # pokus zjisti zda nejde o program který jen kooperuje - předělat na boolean
        counting = 0
        if not self.opponent_moves:
            counting += 1
            if counting >= 10:
                self.count_generous = True
        if self.opponent_moves[-1] and self.count_generous:
            self.count_generous = False

    def dominant_gameplay(self):
        if self.cooperate__cooperate > self.defect__cooperate or self.cooperate__defect > self.defect__defect:
            return COOPERATE
        elif self.cooperate__cooperate < self.defect__cooperate or self.cooperate_defect < self.defect__defect:
            return DEFECT

    def basic_gameplay(self):
        if self.short_game:
            return DEFECT
        if not self.short_game:
            if not self.opponent_moves:
                return COOPERATE
            # využití příliš kooperujících strategií
            elif self.count_generous:
                if not self.failsafe:
                    return DEFECT
                elif self.failsafe:
                    for i in range(2):
                        return COOPERATE
                    if not self.opponent_moves[-1]:
                        return COOPERATE
                    elif self.opponent_moves[-1] and self.opponent_moves[-2]:
                        return DEFECT
            elif not self.opponent_moves[-1]:
                return COOPERATE
            elif self.opponent_moves[-1] and self.opponent_moves[-2]:
                return DEFECT

    # pokud hraje hráč sám se sebou hraje strategii co přinese v součtu nejvíc bodů
    def play_yourself(self):
        if self.double_cooperate > self.double_defect and self.double_cooperate > self.cooperate_defect:
            return COOPERATE
        elif self.double_defect > self.double_cooperate and self.double_defect > self.cooperate_defect:
            return DEFECT
        elif self.cooperate_defect > self.double_defect and self.cooperate_defect > self.double_cooperate:
            # tuto část vylepšit
            if self.my_moves[-1] == self.opponent_moves[-1]:
                a = random.randrange(1, 50)
                if a in range(1, 25):
                    return DEFECT
                elif a in range(26, 50):
                    return COOPERATE
            elif self.my_moves[-1] != self.opponent_moves:
                if not self.opponent_moves[-1]:
                    return COOPERATE
                elif self.opponent_moves[-1]:
                    return DEFECT

    def identify(self):
        if MyPlayer._ps_identification == 2:
            self.yourself = True
            MyPlayer._ps_identification = 0
        else:
            MyPlayer._ps_identification = 0

    def move(self):
        self.identify()
        strategies = ["basic", "dominant"]
        pick = random.choices(strategies, weights=[3, 2])
        if self.yourself:
            return self.play_yourself()
        elif pick == ["basic"]:
            return self.basic_gameplay()
        elif pick == ["dominant"]:
            return self.dominant_gameplay()
