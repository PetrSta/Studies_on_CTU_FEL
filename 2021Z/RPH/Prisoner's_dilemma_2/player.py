import random
# proměnné využité pro čitelnost programu
COOPERATE = False
DEFECT = True


class MyPlayer:
    """Hráč se snaží upravit strategii podle payoff matice a taktiky soupeře"""
    def __init__(self, payoff_matrix, number_of_iterations=0):
        # matice, tahy a počet opakování
        self.payoff_matrix = payoff_matrix
        self.number_of_iterations = number_of_iterations
        self.my_moves = []
        self.opponent_moves = []
        # jednotlivé hodnoty matice pro hru proti sobě samému (celá buňka)
        self.double_cooperate = payoff_matrix[0][0][0] + payoff_matrix[0][0][1]
        self.double_defect = payoff_matrix[1][1][0] + payoff_matrix[1][1][1]
        self.cooperate_defect = payoff_matrix[0][1][0] + payoff_matrix[0][1][1]
        # hodnoty matice pro dominantní strategii (1. tah hráč, 2. tah soupeř)
        self.cooperate_and_cooperate = payoff_matrix[0][0][0]
        self.cooperate_and_defect = payoff_matrix[0][1][0]
        self.defect_and_cooperate = payoff_matrix[0][1][1]
        self.defect_and_defect = payoff_matrix[1][1][0]
        # boolean proměnné
        self.short_game = self.number_of_iterations in range(1, 2)
        self.sparing_or_generous = False
        self.failsafe_generous = False
        self.failsafe_sparing = False
        self.yourself = False
        self.count_generous = False
        self.count_sparing = False
        self.dominate = False
        self.cont = False
        self.dominate_defect = False
        self.dominate_cooperate = False

    def record_last_moves(self, my_last_move, opponent_last_move):
        # zaznamenání posledních tahů a rozhodnutí související s poslednímy tahy
        self.my_moves.append(my_last_move)
        self.opponent_moves.append(opponent_last_move)
        counting_generous = 0
        counting_sparing = 0
        yourself = 0
        # pokus zjisti zda nejde o program který jen kooperuje (řešeno pouze pokud má smysl využít kooperující program)
        if self.defect_and_cooperate > self.cooperate_and_cooperate:
            if len(self.opponent_moves) == 11:
                for i in range(10):
                    if not self.opponent_moves[i]:
                        counting_generous += 1
                    if counting_generous >= 9:
                        self.count_generous = True
                    # pojistka proti programu který by se bránil
                    if self.opponent_moves[9] and self.opponent_moves[10] and self.count_generous:
                        self.failsafe_generous = True
        # pokus zjisti zda nejde o program který jen zrazuje (řešeno pouze pokud má smysl využít zrazující program)
        if self.cooperate_and_defect > self.defect_and_defect:
            if len(self.opponent_moves) == 11:
                for i in range(10):
                    if self.opponent_moves[i]:
                        counting_sparing += 1
                    if counting_sparing >= 9:
                        self.count_sparing = True
                    # pojistka proti programu který by se bránil
                    if self.opponent_moves[9] and not self.opponent_moves[10] and self.count_sparing:
                        self.failsafe_sparing = True
        # identifikace hráče pro hru proti sobě samému
        if len(self.opponent_moves) == 15:
            for i in range(len(self.opponent_moves)):
                if self.my_moves[-1] == self.opponent_moves[-1]:
                    yourself += 1
                if yourself >= 14:
                    self.yourself = True
        # rozhodování jak hraje soupeř v případě "nevyvážené" matice
        if self.dominate and len(self.my_moves) == 2:
            if self.opponent_moves[-1] != self.opponent_moves[-2]:
                self.cont = True
            elif self.opponent_moves[-1] == DEFECT and self.opponent_moves[-2] == DEFECT:
                self.dominate_defect = True
            elif self.opponent_moves[-1] == COOPERATE and self.opponent_moves[-2] == COOPERATE:
                self.dominate_cooperate = True
            else:
                self.cont = False

    # verze hry podle matice
    def dominant_gameplay(self):
        # zjištení nevyšší hodnoty v matici
        highest_value = max(self.cooperate_and_defect, self.cooperate_and_cooperate,
                            self.defect_and_cooperate, self.defect_and_defect)
        # zjištění zda je nevýhodnější kooperovat
        if highest_value == self.cooperate_and_cooperate:
            return COOPERATE
        # pokud je potřeba konkrétnější kombinace je třeba zjistit jak soupeř k matici přistupuje a přizpůsobit hru
        elif highest_value == self.cooperate_and_defect:
            if not self.opponent_moves:
                return DEFECT
            elif len(self.my_moves) == 1:
                return COOPERATE
            elif self.cont:
                if not self.opponent_moves[-1]:
                    return COOPERATE
                elif self.opponent_moves[-1]:
                    return DEFECT
                # pojistka pro neočekávané případy
            elif self.dominate_defect:
                if self.defect_and_defect >= self.cooperate_and_defect:
                    return DEFECT
                elif self.defect_and_defect <= self.cooperate_and_defect:
                    return COOPERATE
                # pojistka pro neočekávané případy
            elif self.dominate_cooperate:
                if self.cooperate_and_cooperate >= self.defect_and_cooperate:
                    return COOPERATE
                elif self.cooperate_and_cooperate <= self.defect_and_cooperate:
                    return DEFECT
                # pojistka pro neočekávané případy
                else:
                    return random.choice([COOPERATE, DEFECT])
            # pojistka pro neočekávané případy
            else:
                return random.choice([COOPERATE, DEFECT])
        # zjištění zda je nevýhodnější zradit
        elif highest_value == self.defect_and_defect:
            return DEFECT
        # pokud je potřeba konkrétnější kombinace je třeba zjistit jak soupeř k matici přistupuje a přizpůsobit hru
        elif highest_value == self.defect_and_cooperate:
            if not self.opponent_moves:
                return COOPERATE
            elif len(self.my_moves) == 1:
                return DEFECT
            elif self.cont:
                if not self.opponent_moves[-1]:
                    return COOPERATE
                elif self.opponent_moves[-1]:
                    return DEFECT
                # pojistka pro neočekávané případy
                else:
                    return random.choice([COOPERATE, DEFECT])
            elif self.dominate_defect:
                if self.defect_and_defect >= self.cooperate_and_defect:
                    return DEFECT
                elif self.defect_and_defect <= self.cooperate_and_defect:
                    return COOPERATE
                # pojistka pro neočekávané případy
                else:
                    return random.choice([COOPERATE, DEFECT])
            elif self.dominate_cooperate:
                if self.cooperate_and_cooperate >= self.defect_and_cooperate:
                    return COOPERATE
                elif self.cooperate_and_cooperate <= self.defect_and_cooperate:
                    return DEFECT
                # pojistka pro neočekávané případy
                else:
                    return random.choice([COOPERATE, DEFECT])
            # pojistka pro neočekávané případy
            else:
                return random.choice([COOPERATE, DEFECT])
        # pojistka pro neočekávané případy
        else:
            return random.choice([COOPERATE, DEFECT])

    # metoda hry pokud nehraje program podle matice a double_cooperate je > než double_defect (CC > DD)
    def basic_gameplay(self):
        if self.short_game:
            return DEFECT
        if not self.short_game:
            if not self.opponent_moves:
                return COOPERATE
            # využití příliš kooperujících strategií
            elif self.count_generous:
                if not self.failsafe_generous:
                    return DEFECT
                # pojistka proti bránící se strategii a pokus vrátit ji na kooperaci
                elif self.failsafe_generous:
                    for i in range(2):
                        return COOPERATE
                    if not self.opponent_moves[-1]:
                        return COOPERATE
                    elif len(self.opponent_moves) >= 2 and self.opponent_moves[-1] and self.opponent_moves[-2]:
                        return DEFECT
                    elif self.opponent_moves[-1]:
                        return COOPERATE
                    # pojistka pro neočekávané případy
                    else:
                        return random.choice([COOPERATE, DEFECT])
            # hraní posledního soupeřova tahu
            elif not self.opponent_moves[-1]:
                return COOPERATE
            elif len(self.opponent_moves) >= 2 and self.opponent_moves[-1] and self.opponent_moves[-2]:
                return DEFECT
            elif self.opponent_moves[-1]:
                return COOPERATE
            # pojistka pro neočekávané případy
            else:
                return random.choice([COOPERATE, DEFECT])

    # metoda hry pokud nehraje program podle matice a double_cooperate je < než double_defect (CC < DD)
    def basic_gameplay_defective(self):
        if self.short_game:
            return DEFECT
        if not self.short_game:
            if not self.opponent_moves:
                return DEFECT
            # využití příliš zrazujících strategií
            elif self.count_sparing:
                if not self.failsafe_sparing:
                    return COOPERATE
                # pojistka proti bránící se strategii a pokus vrátit ji na zrazování
                elif self.failsafe_sparing:
                    for i in range(2):
                        return DEFECT
                    if not self.opponent_moves[-1]:
                        return COOPERATE
                    elif len(self.opponent_moves) >= 2 and self.opponent_moves[-1] and self.opponent_moves[-2]:
                        return DEFECT
                    elif self.opponent_moves[-1]:
                        return COOPERATE
                    # pojistka pro neočekávané případy
                    else:
                        return random.choice([COOPERATE, DEFECT])
            # hraní posledního soupeřova tahu
            elif not self.opponent_moves[-1]:
                return COOPERATE
            elif len(self.opponent_moves) >= 2 and self.opponent_moves[-1] and self.opponent_moves[-2]:
                return DEFECT
            elif self.opponent_moves[-1]:
                return COOPERATE
            # pojistka pro neočekávané případy
            else:
                return random.choice([COOPERATE, DEFECT])

    # pokud hraje hráč sám se sebou hraje strategii co přinese v součtu nejvíc bodů
    def play_yourself(self):
        highest_value = max(self.double_cooperate, self.double_defect, self.cooperate_defect)
        if highest_value == self.double_cooperate:
            return COOPERATE
        elif highest_value == self.double_defect:
            return DEFECT
        elif highest_value == self.cooperate_defect:
            if self.opponent_moves[-1] == self.my_moves[-1]:
                return random.choice([COOPERATE, DEFECT])
            elif self.opponent_moves[-1] != self.my_moves[-1]:
                if self.opponent_moves[-1]:
                    return DEFECT
                elif not self.opponent_moves[-1]:
                    return COOPERATE
                else:
                    return random.choice([COOPERATE, DEFECT])
            else:
                return random.choice([COOPERATE, DEFECT])
        # pojistka pro neočekávané případy
        else:
            return random.choice([COOPERATE, DEFECT])

    # funkce identifikující zda jít do hry podle matice
    def identify(self):
        # hra bez matice se snaží primárně kooperovat pokud není výhodné - hrát podle matice
        if max(self.cooperate_and_defect, self.defect_and_cooperate) >= self.double_cooperate \
                and max(self.cooperate_and_defect, self.defect_and_cooperate, self.defect_and_defect) >= \
                self.double_defect:
            self.dominate = True
        # "zbytečná" část kódu přidaná pro čitelnost aby bylo vidět kdy má být proměnná False
        elif self.double_cooperate > self.double_defect:
            self.sparing_or_generous = False
        elif self.double_cooperate < self.double_defect:
            self.sparing_or_generous = True
        # pojistka pro neočekávané případy
        else:
            self.sparing_or_generous = False

    def move(self):
        self.identify()
        if self.yourself:
            return self.play_yourself()
        elif not self.sparing_or_generous:
            return self.basic_gameplay()
        elif self.sparing_or_generous:
            return self.basic_gameplay_defective()
        elif self.dominate:
            return self.dominant_gameplay()
        # pojistka pro neočekávané případy
        else:
            return random.choice([COOPERATE, DEFECT])
