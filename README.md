# Middle-Earth LOTR RPG

Joc RPG cu interfata in terminal, inspirat din universul Stapanul Inelelor.

Tech Stack: Java + MySQL

---

## Actiuni

| # | Actiune | Descriere |
|---|---------|-----------|
| 1 | `ATTACK` | Ataca inamicul in lupta |
| 2 | `USE_HEALING_ITEM` | Foloseste un obiect de vindecare in lupta |
| 3 | `FLEE_BATTLE` | Fuge din lupta |
| 4 | `EQUIP_WEAPON` | Echipeaza o arma din inventar |
| 5 | `EQUIP_ARMOR` | Echipeaza o armura din inventar |
| 6 | `UNEQUIP_WEAPON` | Dezechipeaza arma curenta |
| 7 | `UNEQUIP_ARMOR` | Dezechipeaza armura curenta |
| 8 | `CONSUME_ITEM` | Consuma un obiect consumabil din inventar |
| 9 | `START_QUEST` | Porneste o misiune noua |
| 10 | `COMPLETE_QUEST` | Finalizeaza o misiune cu succes |
| 11 | `TRAVEL_TO_REGION` | Calatoreste intr-o noua regiune |
| 12 | `LOOT_ITEM` | Ridica un obiect de pe inamicul invins |
| 13 | `VIEW_INVENTORY` | Deschide inventarul |
| 14 | `NEW_GAME` | Creeaza un personaj nou |
| 15 | `QUIT_GAME` | Paraseste jocul |

---

## Tipuri de obiecte

| # | Tip | Descriere |
|---|-----|-----------|
| 1 | `Player` | Personaj: nume, HP, XP, clasa, inventar, echipament |
| 2 | `Enemy` | Inamic: HP, atac, tip, obiect de loot, ASCII art |
| 3 | `Quest` | Misiune: titlu, tip (BATTLE/PUZZLE), dificultate, recompensa XP si obiect |
| 4 | `Region` | Regiune: nume, descriere, cerinta minima XP |
| 5 | `CharacterClass` | Clasa personajului: HP de baza, capacitate bagaj, arma si item de start |
| 6 | `Item` | Interfata de baza pentru toate obiectele: ID, nume, descriere, greutate |
| 7 | `Equipable` | Obiecte echipabile: slot, bonus ATK/DEF |
| 8 | `Consumable` | Obiecte consumabile: cantitate restaurata, mesaj utilizare |
| 9 | `Weapon` | Arma concreta (MAIN_HAND) |
| 10 | `Armor` | Armura concreta (HEAD/CHEST/OFF_HAND/FEET) |
| 11 | `Potion` | Potiune concreta |
| 12 | `GameSession` | Sesiunea activa de joc |

---

## Audit

La fiecare actiune se scrie automat un rand in `audit.csv`:

```
action_name,timestamp
ATTACK,2026-04-21T14:32:00
COMPLETE_QUEST,2026-04-21T14:35:12
```

Implementat in `service.AuditService`.

---

## Baza de date

- **Engine:** MySQL (MariaDB)
- **URL:** `jdbc:mysql://localhost:3306/lotr_db`
- **User:** `lotr_user` / **Parola:** `pass`

Migrarile ruleaza automat din: `src/main/resources/db/migrations/`.
Conventie de denumire: `V{nr}__{descriere}.sql`
---

## Rulare

### Cerinte

- JDK 11+
- Maven
- MariaDB / MySQL

### 1. Pregatire baza de date

```bash
sudo mysql -e "CREATE DATABASE IF NOT EXISTS lotr_db;"
sudo mysql -e "CREATE USER IF NOT EXISTS 'lotr_user'@'localhost' IDENTIFIED BY 'pass';"
sudo mysql -e "GRANT ALL PRIVILEGES ON lotr_db.* TO 'lotr_user'@'localhost'; FLUSH PRIVILEGES;"
```

### 2. Compilare si ambalare

```bash
cd lotr-rpg
mvn clean package
```

### 3. Pornire joc

```bash
./play.sh
```

### Reset baza de date

```bash
sudo mysql -e "DROP DATABASE lotr_db; CREATE DATABASE lotr_db;"
# Porneste jocul - migrarile ruleaza automat
```

---

## Comenzi globale in joc

| Comanda | Efect |
|---------|-------|
| `:i` | Informatii despre jucator si inventar |
| `:h` | Afiseaza comenzile disponibile |
| `:r` | Redeseneaza ecranul |
| `:q` | Iese din joc |
