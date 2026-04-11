# MiddleEarth
Turn-based RPG game inspired from LotR built in Java.


## App Structure

### Actions / Queries

- findHero: query the database to find a hero by the name
- getItems: filter items by certain criteria
- sortInventory: sort the inventory of the player by weight / value
- battleOutcome: calculate battle outcome (taking in account hero's attack and enemy defense)
- levelUp: level up a hero
- checkMissionState: check if a mission is acomplished by having all subtasks completed
- getTeamLevel: get the average level of a team
- tradeSmith: make a trade at the blacksmith
- discoveredLocations: view areas explored
- getBattleLog: view the log of a particular battle

### Classes
- Item :- usable items (Weapon, Armor, Potion, Artifact, Ability Card)
- Character :- playable characters (Hobbit, Wizzard, Orc, Human, Elf, Dwarf)
- Trade :- trade conducted with the blacksmith
- Region / Location :- area where the player is currently at
- Team :- team formed of more characters
- Quest :- big part of the game
- Mission :- missions part of quests/chapters
- Battle :- battles part of mission

### Game flow (The journey)

- Start with your hero. Hero has XP.
- Available actions:
    - Move to a new region (if XP high enough)
    - Complete a quest on the current region
    - Trade (buy/sell items) with the blacksmith
    - Check your inventory (equip / unequip items)
    - Engage in battles in the current region (open-world style)

> Note: Not all battles are combat, you can encounter logical puzzles.

## Running instructions

Prerequesites:
- JDK
- Maven

```bash
cd lotr-rpg
```

To compile and execute the project do:
```bash
mvn clean compile exec:java -Dexec.mainClass="com.middlearth.App"
```

To package:

```bash
mvn clean package
```

To run the package:

```bash
./play.sh
```