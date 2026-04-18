-- V4: Regions and Quests

CREATE TABLE IF NOT EXISTS regions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(300) NOT NULL,
    xp INT
);

-- Seed: Regions
INSERT INTO regions (name, description, xp) VALUES
("The Shire", "Rolling green hills and quiet hobbit holes. A peaceful place to start.", 0),
("Bree", "A bustling crossroads town. Watch your pockets at the Prancing Pony.", 25),
("Rivendell", "The Last Homely House east of the Sea. Home to Elrond and ancient wisdom.", 100),
("Lothlórien", "The golden forest of the Galadhrim. Enchanted, beautiful, and dangerous.", 250),
("The Black Gate", "The jagged iron entrance to Mordor. Only the bravest (or most foolish) approach.", 500);


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

-- Seed: The Shire quests (region_id = 1)
INSERT INTO quests (region_id, title, description, quest_type, difficulty, xp_reward, item_reward_id) VALUES
(1, 'Riddles in the Dark',        'A strange creature blocks the tunnel beneath the hill. Answer his three riddles to pass.', 'PUZZLE', 1, 10, NULL),
(1, 'The Crop Raider''s Folly',   'Farmer Maggot''s crops are being stolen nightly. Deduce which neighbor is the culprit from the clues left behind.', 'PUZZLE', 1, 15, NULL),
(1, 'Ambush at the Brandywine',   'A band of ruffians has set up camp near Bucklebury Ferry. Drive them off before they threaten the Shire.', 'BATTLE', 2, 20, 14),
(1, 'The Missing Mathom',         'A prized mathom has vanished from the Michel Delving museum. Follow the trail of clues to recover it.', 'PUZZLE', 2, 15, NULL),
(1, 'Wolves of the Old Forest',   'Hungry wolves have crept out of the Old Forest. Defend the hobbits of Buckland.', 'BATTLE', 2, 25, 16);

-- Seed: Bree quests (region_id = 2)
INSERT INTO quests (region_id, title, description, quest_type, difficulty, xp_reward, item_reward_id) VALUES
(2, 'The Prancing Pony Mystery',  'A hooded stranger left a cryptic message at the inn. Decode it before the Nazgûl arrive.', 'PUZZLE', 3, 30, NULL),
(2, 'Brigands on the Greenway',   'Bill Ferny''s thugs are ambushing travelers on the road south. Put an end to their racket.', 'BATTLE', 3, 35, 10),
(2, 'The Stolen Pony',            'Bill Ferny sold a stolen pony. Track down the witnesses and piece together the con.', 'PUZZLE', 2, 25, NULL),
(2, 'Barrow-wight''s Curse',      'A chilling fog rolls from the Barrow-downs. Face the undead wight guarding an ancient tomb.', 'BATTLE', 4, 45, 12),
(2, 'Midgewater Marshes',         'A merchant lost his cargo in the marshes. Navigate the treacherous bogs using his torn map.', 'PUZZLE', 3, 30, NULL);

-- Seed: Rivendell quests (region_id = 3)
INSERT INTO quests (region_id, title, description, quest_type, difficulty, xp_reward, item_reward_id) VALUES
(3, 'The Lore of the Rings',      'Elrond tests your knowledge of the Rings of Power. Answer his questions about their history and bearers.', 'PUZZLE', 4, 50, NULL),
(3, 'Trollshaw Skirmish',         'Stone-trolls may be dormant, but their cave is now home to a nest of goblins. Clear them out.', 'BATTLE', 4, 55, 3),
(3, 'Bilbo''s Last Riddle',       'Old Bilbo has one final riddle before he departs for the Grey Havens. Can you solve it?', 'PUZZLE', 3, 40, NULL),
(3, 'Ford of Bruinen Defense',    'Nazgûl scouts probe the ford. Hold the crossing until Elrond''s flood-spell is ready.', 'BATTLE', 5, 65, 15),
(3, 'The Shards of Narsil',       'A piece of the legendary blade is missing from the display. Investigate who took it and why.', 'PUZZLE', 5, 60, NULL);

-- Seed: Lothlórien quests (region_id = 4)
INSERT INTO quests (region_id, title, description, quest_type, difficulty, xp_reward, item_reward_id) VALUES
(4, 'The Mirror of Galadriel',    'Gaze into the Mirror and interpret the visions it shows. Your choices shape what you see.', 'PUZZLE', 5, 70, NULL),
(4, 'Orc Raid on Caras Galadhon', 'An orc scouting party breaches the forest border. Repel them before they reach the city of trees.', 'BATTLE', 6, 80, 11),
(4, 'The Song of Nimrodel',       'A minstrel sings an incomplete ballad. Piece together the missing verses from clues scattered in the forest.', 'PUZZLE', 4, 55, NULL),
(4, 'Wargs in the Golden Wood',   'A warg pack prowls the eaves of Lórien. Hunt them down under the golden mallorn canopy.', 'BATTLE', 6, 75, 17),
(4, 'The Riddle of Celebrant',    'An ancient stone marker at the Field of Celebrant bears a coded inscription. Decipher it to reveal a hidden cache.', 'PUZZLE', 5, 65, NULL);

-- Seed: The Black Gate quests (region_id = 5)
INSERT INTO quests (region_id, title, description, quest_type, difficulty, xp_reward, item_reward_id) VALUES
(5, 'The Mouth of Sauron''s Gambit', 'The Mouth of Sauron challenges you with a deadly game of wits. Outsmart him or doom your allies.', 'PUZZLE', 7, 100, NULL),
(5, 'Siege of the Morannon',       'The armies of Mordor pour through the Black Gate. Hold the line in a brutal last stand.', 'BATTLE', 8, 120, 6),
(5, 'The Eye''s Riddle',           'A flaming inscription appears on the gate. Decipher Sauron''s trap before it springs.', 'PUZZLE', 7, 90, NULL),
(5, 'Olog-hai Vanguard',           'Massive troll-warriors lead the charge. Defeat them before they break through your ranks.', 'BATTLE', 9, 130, 9),
(5, 'The Last Debate',             'Aragorn calls a war council. Solve the strategic puzzle of where to commit your dwindling forces.', 'PUZZLE', 8, 110, NULL);


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