-- V1: Players and Items

CREATE TABLE IF NOT EXISTS players (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    health INT DEFAULT 100,
    xp INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- Create items table
CREATE TABLE IF NOT EXISTS items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    item_type VARCHAR(50) NOT NULL,
    description TEXT,
    weight DOUBLE DEFAULT 0.0,
    bonus INT DEFAULT 0,
    slot VARCHAR(50),
    restore_amount INT DEFAULT 0
);

-- Seed: Weapons
INSERT INTO items (name, item_type, description, weight, bonus, slot, restore_amount) VALUES
('Sting',             'WEAPON', 'A small Elvish blade that glows blue when Orcs are near.', 60.0, 6,  'MAIN_HAND', 0),
('Elven Longbow',     'WEAPON', 'A graceful bow of Lothlórien make. Deadly accurate.', 3.0, 10, 'MAIN_HAND', 0),
('Iron Broadsword',   'WEAPON', 'A heavy, battle-tested blade forged by Men.', 5.0, 14, 'MAIN_HAND', 0),
('Wizard Staff',      'WEAPON', 'A gnarled staff crackling with arcane energy.', 4.0, 16, 'MAIN_HAND', 0),
('Battle Axe',        'WEAPON', 'A dwarven-forged axe. Heavy and devastating.', 6.0, 12, 'MAIN_HAND', 0),
('Andúril',           'WEAPON', 'The Flame of the West. Reforged from the shards of Narsil.', 4.5, 25, 'MAIN_HAND', 0),
('Morgul Blade',      'WEAPON', 'A cursed dagger from the Witch-king. Chills the soul.', 1.5, 18, 'MAIN_HAND', 0),
('Rohirrim Spear',    'WEAPON', 'A long spear carried by the riders of Rohan.', 5.5, 11, 'MAIN_HAND', 0);

-- Seed: Armor
INSERT INTO items (name, item_type, description, weight, bonus, slot, restore_amount) VALUES
('Mithril Coat',      'ARMOR', 'A shirt of mithril rings. Light as a feather, hard as dragon scales.', 3.0, 20, 'CHEST', 0),
('Leather Jerkin',    'ARMOR', 'Simple but sturdy leather armor.', 4.0, 5, 'CHEST', 0),
('Elven Cloak',       'ARMOR', 'A shimmering cloak from Lothlórien that hides the wearer.', 1.0, 8, 'CHEST', 0),
('Iron Helm',         'ARMOR', 'A sturdy iron helmet. Not pretty, but it works.', 3.5, 6, 'HEAD', 0),
('Dwarven Shield',    'ARMOR', 'A thick round shield etched with dwarven runes.', 7.0, 10, 'OFF_HAND', 0);

-- Seed: Consumables
INSERT INTO items (name, item_type, description, weight, bonus, slot, restore_amount) VALUES
('Lembas Bread',      'POTION', 'One bite is enough to fill the stomach of a grown man.', 0.5, 0, NULL, 30),
('Miruvor Draught',   'POTION', 'A warm and fragrant Elven cordial that revives the spirit.', 0.5, 0, NULL, 40),
('Healing Salve',     'POTION', 'A thick herbal paste that mends wounds quickly.', 0.5, 0, NULL, 25),
('Athelas Herb',      'POTION', 'Kingsfoil — a healing plant known to the Dúnedain.', 0.3, 0, NULL, 35),
('Dwarven Ale',       'POTION', 'Strong dwarven brew. Heals the body, numbs the mind.', 1.0, 0, NULL, 20),
('Ent-draught',       'POTION', 'A mysterious drink from Fangorn Forest. You feel yourself growing.', 1.5, 0, NULL, 60),
('Old Toby Pipeweed', 'POTION', 'The finest weed in the Southfarthing. Soothes the nerves.', 0.2, 0, NULL, 10);