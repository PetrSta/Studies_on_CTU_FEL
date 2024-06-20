class MyVector:
    def __init__(self, vec):
        self.vec = vec

    def get_vector(self):
        return self.vec

    def __mul__(self, other):
        a = 0
        if len(self.vec) == len(other.vec):
            for i in range(len(self.vec)):
                a += self.vec[i] * other.vec[i]
        else:
            print("Zadejte prosím stejně dlouhé vektory.")
        return a


if __name__ == "__main__":
    vec1 = MyVector([1, 2, 3])
    vec2 = MyVector([3, 4, 5, 10])
    print(vec1.get_vector())
    dot_product = vec1*vec2
    print(dot_product)
