# Database

## Overview

The game uses **MariaDB/MySQL** via JDBC for persistence. The database stores player data, inventory, and save games. If the database is unavailable, the game runs without persistence.

## Connection

Managed by `DatabaseConfiguration` in `com.middleearth.db`.

- **URL:** `jdbc:mysql://localhost:3306/lotr_db`
- **User:** `lotr_user`
- **Password:** `pass`

The connection is a lazy singleton — created on first use and reused throughout the session. On app exit, `DatabaseConfiguration.close()` is called.

To override defaults programmatically before startup:
```java
DatabaseConfiguration.configure("jdbc:mysql://host:3306/mydb", "user", "password");
```

## Migration System

Migrations live in `src/main/resources/db/migrations/` as SQL files. On startup, `MigrationManager.migrate()` runs automatically from `App.main()`.

### How it works

1. Creates a `schema_migrations` tracking table if it doesn't exist
2. Scans `/db/migrations/` in the classpath for `.sql` files
3. Compares file versions against already-applied versions in `schema_migrations`
4. Executes pending migrations in order and records them

### Naming convention

```
V{version}__{description}.sql
```

- `V` prefix (required)
- Version number (integer, sequential)
- Double underscore `__` separator
- Description with underscores as spaces
- `.sql` extension

**Examples:**
```
V1__create_schema.sql
V2__add_quest_tables.sql
V3__add_battle_log.sql
```

### Current migrations

| Version | File | Description |
|---------|------|-------------|
| 1 | `V1__create_schema.sql` | Creates `players`, `inventory_items`, and `save_games` tables |

### Adding a new migration

1. Create a new SQL file in `src/main/resources/db/migrations/`
2. Follow the naming convention: `V{next_number}__{description}.sql`
3. Write your SQL statements (multiple statements separated by `;` are supported)
4. Rebuild and run — migrations apply automatically on startup

## Schema

### `players`
| Column | Type | Notes |
|--------|------|-------|
| id | INT | Primary key, auto-increment |
| name | VARCHAR(100) | Not null |
| health | INT | Default 100 |
| xp | INT | Default 0 |
| created_at | TIMESTAMP | Auto-set |

### `inventory_items`
| Column | Type | Notes |
|--------|------|-------|
| id | INT | Primary key, auto-increment |
| player_id | INT | FK → players(id), cascade delete |
| item_name | VARCHAR(100) | Not null |
| item_type | VARCHAR(50) | Not null |
| description | TEXT | |
| weight | DOUBLE | Default 0.0 |
| bonus | INT | Default 0 |
| slot | VARCHAR(50) | |

### `save_games`
| Column | Type | Notes |
|--------|------|-------|
| id | INT | Primary key, auto-increment |
| player_id | INT | FK → players(id), cascade delete |
| current_state | VARCHAR(100) | Not null |
| saved_at | TIMESTAMP | Auto-set |

## Useful Commands

### Setup

```bash
# Create database and user (requires sudo for MariaDB)
sudo mysql -e "CREATE DATABASE IF NOT EXISTS lotr_db;"
sudo mysql -e "CREATE USER IF NOT EXISTS 'lotr_user'@'localhost' IDENTIFIED BY 'pass';"
sudo mysql -e "GRANT ALL PRIVILEGES ON lotr_db.* TO 'lotr_user'@'localhost'; FLUSH PRIVILEGES;"
```

### Verify connection

```bash
mysql -u lotr_user -ppass lotr_db -e "SELECT 1;"
```

### Inspect database

```bash
# List all tables
mysql -u lotr_user -ppass lotr_db -e "SHOW TABLES;"

# Check migration history
mysql -u lotr_user -ppass lotr_db -e "SELECT * FROM schema_migrations;"

# View table structure
mysql -u lotr_user -ppass lotr_db -e "DESCRIBE players;"
mysql -u lotr_user -ppass lotr_db -e "DESCRIBE inventory_items;"
mysql -u lotr_user -ppass lotr_db -e "DESCRIBE save_games;"
```

### Reset database

```bash
# Drop and recreate (all data lost)
sudo mysql -e "DROP DATABASE lotr_db; CREATE DATABASE lotr_db;"

# Or just clear migration history to re-run migrations
mysql -u lotr_user -ppass lotr_db -e "DROP TABLE schema_migrations;"
```

### Query data

```bash
# List all players
mysql -u lotr_user -ppass lotr_db -e "SELECT * FROM players;"

# List inventory for a player
mysql -u lotr_user -ppass lotr_db -e "SELECT * FROM inventory_items WHERE player_id = 1;"

# List save games
mysql -u lotr_user -ppass lotr_db -e "SELECT * FROM save_games;"
```

## Project structure

```
com.middleearth.db/
├── DatabaseConfiguration.java   # Connection singleton
├── GenericRepository.java       # CRUD interface for repositories
└── migration/
    ├── Migration.java           # Migration data model
    └── MigrationManager.java    # Discovers and runs SQL migrations

src/main/resources/db/migrations/
└── V1__create_schema.sql        # Initial schema
```
