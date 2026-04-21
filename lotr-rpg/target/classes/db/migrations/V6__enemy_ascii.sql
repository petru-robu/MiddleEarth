-- V6: ASCII art column for enemies

ALTER TABLE enemies ADD COLUMN ascii_art TEXT;

-- RUFFIAN art: hooded figure with a blade
--   O
--  /|\
--  / \
UPDATE enemies SET ascii_art = '  O  \n /|\\ \n / \\ ' WHERE type = 'RUFFIAN';

-- ORC art: hunched orc warrior
--  >O<
--  /|\
--  / \
UPDATE enemies SET ascii_art = ' >O< \n /|\\ \n / \\ ' WHERE type = 'ORC';

-- WOLF art: snarling warg
--  /\_/\
-- ( o.o )
--  > ^ <
UPDATE enemies SET ascii_art = ' /\\_/\\\n( o.o )\n > ^ < ' WHERE type = 'WOLF';

-- TROLL art: massive hulking form
--   O
--  \|/
--  / \
UPDATE enemies SET ascii_art = '  O  \n \\|/ \n / \\ ' WHERE type = 'TROLL';

-- UNDEAD art: wraith-like spectre
--  ,-.
-- (X_X)
--  |||
UPDATE enemies SET ascii_art = ' ,-. \n(X_X)\n ||| ' WHERE type = 'UNDEAD';
