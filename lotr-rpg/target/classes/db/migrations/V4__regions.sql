-- V4: Regions and Quests

CREATE TABLE IF NOT EXISTS regions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(300) NOT NULL,
    xp INT
);

-- Seed: 3 Regions
INSERT INTO regions (name, description, xp) VALUES
("The Shire",      "Rolling green hills and quiet hobbit holes. A peaceful place to start.", 0),
("Lothlórien",     "The golden forest of the Galadhrim. Enchanted, beautiful, and dangerous.", 100),
("The Black Gate", "The jagged iron entrance to Mordor. Only the bravest (or most foolish) approach.", 350);


-- Quests table
CREATE TABLE IF NOT EXISTS quests (
    id INT AUTO_INCREMENT PRIMARY KEY,
    region_id INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    description TEXT NOT NULL,
    quest_type ENUM('PUZZLE', 'BATTLE') NOT NULL,
    difficulty INT NOT NULL DEFAULT 1,
    xp_reward INT NOT NULL DEFAULT 10,
    item_reward_id INT,
    FOREIGN KEY (region_id) REFERENCES regions(id) ON DELETE CASCADE,
    FOREIGN KEY (item_reward_id) REFERENCES items(id)
);

-- Seed: The Shire quests (region_id = 1) — quest IDs 1, 2, 3
INSERT INTO quests (region_id, title, description, quest_type, difficulty, xp_reward, item_reward_id) VALUES
(1, 'Riddles in the Dark',      'A strange creature blocks the tunnel beneath the hill. Answer his riddles to pass.',           'PUZZLE', 1, 30, NULL),
(1, 'The Crop Raider''s Folly', 'Farmer Maggot''s crops are being stolen. Deduce the culprit from the clues left behind.',     'PUZZLE', 1, 70, NULL),
(1, 'Ambush at the Brandywine', 'A band of ruffians camps near Bucklebury Ferry. Drive them off before they threaten the Shire.', 'BATTLE', 2, 115, 14);

-- Seed: Lothlórien quests (region_id = 2) — quest IDs 4, 5, 6
INSERT INTO quests (region_id, title, description, quest_type, difficulty, xp_reward, item_reward_id) VALUES
(2, 'The Mirror of Galadriel',    'Gaze into the Mirror and interpret the visions it shows. Your choices reveal the truth.',           'PUZZLE', 4, 55, NULL),
(2, 'Orc Raid on Caras Galadhon', 'An orc scouting party breaches the forest border. Repel them before they reach the city of trees.',  'BATTLE', 5, 85, 11),
(2, 'Wargs in the Golden Wood',   'A warg pack prowls the eaves of Lórien. Hunt them down under the golden mallorn canopy.',          'BATTLE', 5, 115, 17);

-- Seed: The Black Gate quests (region_id = 3) — quest IDs 7, 8, 9
INSERT INTO quests (region_id, title, description, quest_type, difficulty, xp_reward, item_reward_id) VALUES
(3, 'The Mouth of Sauron''s Gambit', 'The Mouth of Sauron challenges you to a deadly game of wits. Outsmart him or doom your allies.', 'PUZZLE', 7, 120,  NULL),
(3, 'Siege of the Morannon',          'The armies of Mordor pour through the Black Gate. Hold the line in a brutal last stand.',       'BATTLE', 8, 200, 6),
(3, 'Olog-hai Vanguard',              'Massive troll-warriors lead the Mordor charge. Defeat them before they break through your ranks.', 'BATTLE', 8, 350, 9);


-- Completed quests tracking
CREATE TABLE IF NOT EXISTS completed_quests (
    id INT AUTO_INCREMENT PRIMARY KEY,
    player_id INT NOT NULL,
    quest_id INT NOT NULL,
    completed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE,
    FOREIGN KEY (quest_id) REFERENCES quests(id),
    UNIQUE(player_id, quest_id)
);