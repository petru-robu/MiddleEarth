-- V3: Inventory join table and save games

CREATE TABLE IF NOT EXISTS inventory_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    player_id INT NOT NULL,
    item_id INT NOT NULL,
    FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES items(id)
);