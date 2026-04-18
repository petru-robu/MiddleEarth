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