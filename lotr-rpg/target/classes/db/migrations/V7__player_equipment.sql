-- V7: Player equipment slots

ALTER TABLE players
    ADD COLUMN equipped_weapon_id INT NULL,
    ADD COLUMN equipped_armor_id  INT NULL,
    ADD FOREIGN KEY (equipped_weapon_id) REFERENCES items(id),
    ADD FOREIGN KEY (equipped_armor_id)  REFERENCES items(id);
