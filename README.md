# Gridarena — Jeu de combat au tour par tour (MVC & Design Patterns)

[![Java Version](https://img.shields.io/badge/Java-21-blue.svg)](https://www.oracle.com/java/technologies/downloads/)
[![Build System](https://img.shields.io/badge/Build-Maven-orange.svg)](https://maven.apache.org/)
[![Design Patterns](https://img.shields.io/badge/Patterns-MVC%20%7C%20Factory%20Method%20%7C%20Proxy%20%7C%20Strategy-green.svg)](#architecture-et-design-patterns)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](./LICENSE.md)

## Description

**Gridarena** est un jeu de combat tactique au tour par tour développé en Java. Ce projet implémente une architecture **MVC (Modèle-Vue-Contrôleur)** robuste et intègre plusieurs **Design Patterns** fondamentaux. 

Les joueurs (humains, CLI ou bots intelligents) s'affrontent sur une grille bidimensionnelle parsemée d'obstacles et de bonus, en utilisant diverses compétences tactiques (tir, hache, bombes, mines, bouclier).

---

## Architecture et Design Patterns

Ce projet sert de démonstrateur pour l'application pratique des patrons de conception logicielle :
- **MVC (Modèle-Vue-Contrôleur)** : Séparation stricte de l'état du jeu (Modèle), de l'affichage (Vue GUI/CLI) et des actions utilisateur (Contrôleurs).
- **Observer (Observateur)** : Utilisé pour notifier automatiquement les vues lorsque l'état du champ de bataille change.
- **Proxy (Interface-based)** : Un proxy restreint l'accès à la grille complète pour chaque joueur, appliquant le brouillard de guerre (les mines ennemies sont invisibles).
- **Adapter (Adaptateur)** : Utilisé pour adapter le groupe de héros (`GroupHeroes`) en modèle de tableau compatible avec l'affichage Swing (`TableModel`).
- **Factory Method** : Création découplée des spécialisations de héros (`Assassin`, `Warrior`, `Mastodonte`) via des classes fabriques dédiées.
- **Strategy** :
  - **Remplissage de la grille** (`Random`, `Modulo`, `Pattern`).
  - **Intelligence des robots** utilisant des algorithmes de recherche de chemin :
    - **A\* Search** ($f(n) = g(n) + h(n)$) pour un déplacement optimal.
    - **Dijkstra** ($f(n) = g(n)$) pour une exploration exhaustive.
    - **Greedy Best-First** ($f(n) = h(n)$) pour un comportement agressif à courte vue.

---

## Captures d'Écran

### Vue du Joueur (Brouillard de guerre)
![vue-joueur](./screenshots/vue-joueur.png)

### Vue Générale (Arbitre / Spectateur)
![vue-jeu-complet](./screenshots/vue-jeu-complet.png)

### Sélection du Personnage
![choisir personnage](./screenshots/choisir-personnage.png)

---

## Règles du Jeu

À chaque tour, un joueur peut effectuer une action parmi :
- **Se déplacer** d'une case (Haut, Bas, Gauche, Droite).
- **Poser une mine** (invisible pour les adversaires) sur l'une des 8 cases adjacentes.
- **Poser une bombe** (qui explose après 3 tours dans un rayon donné) sur l'une des 8 cases adjacentes.
- **Tirer à distance** horizontalement ou verticalement (consomme des munitions, portée infinie sans obstacle).
- **Activer un bouclier** pour devenir invincible durant ce tour.
- **Donner un coup de hache** au corps à corps (Haut, Bas, Gauche, Droite).
- **Passer son tour**.

La grille contient initialement des obstacles destructibles (**barils explosifs**), des **murs**, des **kits de soins** (+PV) et des **boîtes de munitions**.

---

## Comment Lancer l'Application

Le projet utilise **Maven** pour la gestion de build. Assurez-vous d'avoir Java 21+ et Maven installés.

### 1. Compiler et Empaqueter le Projet
Générez le JAR exécutable dans le dossier `target/` :
```bash
mvn clean package
```

### 2. Exécuter l'Application
Vous pouvez lancer le jeu directement via Maven :
```bash
mvn exec:java
```

Ou exécuter le JAR généré :
```bash
cd target
java -jar gridarena-1.0-SNAPSHOT.jar
```

---

## Configuration du Jeu

Le comportement du jeu est configurable dans le fichier [config.properties](file:///home/florian/GitHub/Gridarena-Java-MVC/src/main/resources/resources/config.properties) (ou sa copie générée dans `target/config.properties`) :
- **Nombre de joueurs** : `GUI_PLAYERS`, `CLI_PLAYERS`, `BOT_PLAYERS`.
- **Paramètres de grille** : `SIZE_GRID`, `WALLS`, `MEDICAL_KITS`, `AMMO_KITS`, `BARRELS`.
- **Stratégie de l'IA (bot)** : `BOT_STRATEGY` (`astar`, `dijkstra`, `greedy`).

---

## Crédits des Images

| Image | Auteur / Licence |
| :---: | :---: |
| <img src="./src/main/resources/resources/images/mine.png" alt="mine" width="40" height="40"> | [mine - FontAwesome](https://fontawesome.com/) |
| <img src="./src/main/resources/resources/images/health.png" alt="health" width="40" height="40"> | [health - Freepik](https://fr.freepik.com/) |
| <img src="./src/main/resources/resources/images/barrel.png" alt="barrel" width="24" height="40"> | [barrel - Freepik](https://fr.freepik.com/) |
| <img src="./src/main/resources/resources/images/wall.png" alt="wall" width="40" height="40"> | [wall - Freepik](https://fr.freepik.com/) |
| <img src="./src/main/resources/resources/images/bomb.png" alt="bomb" width="40" height="40"> | [bomb - Freepik](https://fr.freepik.com/) |
| <img src="./src/main/resources/resources/images/ammo.png" alt="ammo" width="40" height="40"> | [ammo - Freepik](https://fr.freepik.com/) |
| <img src="./src/main/resources/resources/images/ground.png" alt="ground" width="40" height="40"> | [décor - Cupnooble](https://cupnooble.itch.io/sprout-lands-asset-pack) |
| <img src="./src/main/resources/resources/images/vert.png" alt="vert" width="20" height="30"> | [héros vert - sscary.itch](https://sscary.itch.io/the-adventurer-male) |
| <img src="./src/main/resources/resources/images/violet.png" alt="violet" width="20" height="30"> | [héros violet - sscary.itch](https://sscary.itch.io/the-adventurer-male) |
| <img src="./src/main/resources/resources/images/blue.png" alt="bleu" width="20" height="30"> | [héros bleu - sscary.itch](https://sscary.itch.io/the-adventurer-male) |

---

## Auteurs & Contributeurs

- **Florian Pépin**
- **Tom David** ([kitoutou999](https://github.com/kitoutou999))
- **Emilien Huron**

---

## Licence

Ce projet est disponible sous licence **MIT**. Voir le fichier [LICENSE.md](./LICENSE.md) pour plus de détails.
