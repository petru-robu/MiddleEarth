-- V2: Character classes (references items table)

CREATE TABLE IF NOT EXISTS character_classes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL,
    base_health INT NOT NULL,
    bag_capacity DOUBLE NOT NULL,
    starter_weapon_id INT NOT NULL,
    starter_item_id INT NOT NULL,
    FOREIGN KEY (starter_weapon_id) REFERENCES items(id),
    FOREIGN KEY (starter_item_id) REFERENCES items(id)
);

-- Add class_id to players
ALTER TABLE players ADD COLUMN class_id INT AFTER name;
ALTER TABLE players ADD FOREIGN KEY (class_id) REFERENCES character_classes(id);

-- Seed the classes (IDs match V1 insert order: Sting=1, Elven Longbow=2, Iron Broadsword=3, Wizard Staff=4, Battle Axe=5)
-- Consumable IDs: Lembas Bread=14, Miruvor Draught=15, Healing Salve=16, Athelas Herb=17, Dwarven Ale=18
INSERT INTO character_classes (name, description, base_health, bag_capacity, starter_weapon_id, starter_item_id) VALUES
('Hobbit',  'Small but brave. Light on their feet with surprising resilience.', 80,  300, 1, 14),
('Elf',     'Graceful and wise. Masters of bow and ancient lore.',               90,  400, 2, 15),
('Warrior', 'Strong and sturdy. Born for the front lines of battle.',            150, 700, 3, 16),
('Wizard',  'Wielders of arcane power. Fragile body, devastating magic.',         70,  350, 4, 17),
('Dwarf',   'Tough as mountain stone. Enduring and hard-hitting.',               130, 800, 5, 18);
