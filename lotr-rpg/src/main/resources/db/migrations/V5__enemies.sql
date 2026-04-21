-- V5: Enemies

CREATE TABLE IF NOT EXISTS enemies (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    quest_id    INT NOT NULL,
    name        VARCHAR(100) NOT NULL,
    type        ENUM('RUFFIAN','WOLF','UNDEAD','ORC','TROLL') NOT NULL,
    health      INT NOT NULL DEFAULT 30,
    attack      INT NOT NULL DEFAULT 5,
    loot_item_id INT,
    FOREIGN KEY (quest_id)     REFERENCES quests(id) ON DELETE CASCADE,
    FOREIGN KEY (loot_item_id) REFERENCES items(id)
);

-- Shire quest 3: Ambush at the Brandywine
-- Lembas Bread=14, Leather Jerkin=10, Dwarven Shield=13
INSERT INTO enemies (quest_id, name, type, health, attack, loot_item_id) VALUES
(3, 'Ruffian Scout',  'RUFFIAN', 25,  6, 14),
(3, 'Ruffian Thug',   'RUFFIAN', 35,  8, 10),
(3, 'Ruffian Leader', 'RUFFIAN', 50, 12, 13);

-- Lothlórien quest 5: Orc Raid on Caras Galadhon
-- Elven Cloak=11, Healing Salve=16
INSERT INTO enemies (quest_id, name, type, health, attack, loot_item_id) VALUES
(5, 'Orc Scout',    'ORC', 40, 10, 16),
(5, 'Orc Warrior',  'ORC', 55, 14, NULL),
(5, 'Orc Captain',  'ORC', 75, 18, 11);

-- Lothlórien quest 6: Wargs in the Golden Wood
-- Rohirrim Spear=17
INSERT INTO enemies (quest_id, name, type, health, attack, loot_item_id) VALUES
(6, 'Forest Warg',  'WOLF', 35, 10, NULL),
(6, 'Forest Warg',  'WOLF', 35, 10, NULL),
(6, 'Warg Alpha',   'WOLF', 60, 16, 17);

-- Black Gate quest 8: Siege of the Morannon
-- Andúril=6, Mithril Coat=9
INSERT INTO enemies (quest_id, name, type, health, attack, loot_item_id) VALUES
(8, 'Mordor Soldier', 'ORC',   60, 15, NULL),
(8, 'Olog-hai Troll', 'TROLL', 100, 22, 9),
(8, 'Nazgûl Wraith',  'UNDEAD', 80, 20, 6);

-- Black Gate quest 9: Olog-hai Vanguard
-- Morgul Blade=7, Battle Axe=5
INSERT INTO enemies (quest_id, name, type, health, attack, loot_item_id) VALUES
(9, 'Olog-hai Grunt',    'TROLL', 85, 18, NULL),
(9, 'Olog-hai Crusher',  'TROLL', 110, 24, 5),
(9, 'Olog-hai Champion', 'TROLL', 130, 28, 7);
