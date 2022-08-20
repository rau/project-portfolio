"""
ID: 
LANG: PYTHON3
PROG: ride
"""
alphabet = {
    "A": 1,
    "B": 2,
    "C": 3,
    "D": 4,
    "E": 5,
    "F": 6,
    "G": 7,
    "H": 8,
    "I": 9,
    "J": 10,
    "K": 11,
    "L": 12,
    "M": 13,
    "N": 14,
    "O": 15,
    "P": 16,
    "Q": 17,
    "R": 18,
    "S": 19,
    "T": 20,
    "U": 21,
    "V": 22,
    "W": 23,
    "X": 24,
    "Y": 25,
    "Z": 26,
}

with open("ride.in") as f:
    lines = f.readlines()
    comet = lines[0].rstrip()
    ship = lines[1].rstrip()

letters = list(comet)
shipLetters = list(ship)

sum1 = 1
for letter in letters:
    sum1 *= alphabet[letter]
sum2 = 1
for shipLetter in shipLetters:
    sum2 *= alphabet[shipLetter]
with open("ride.out", "w") as out:
    if sum1 % 47 == sum2 % 47:
        out.write("GO\n")
    else:
        out.write("STAY\n")
