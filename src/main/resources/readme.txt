This describes the architecture of the program.


Lexer

Takes a string of characters and turns it into a list of tokens.


Reader

Takes a flat list of tokens and turns it into a tree where the leaves are
tokens (identifiers, literals, etc.). Internal nodes do not contain data and
are just trees themselves.


