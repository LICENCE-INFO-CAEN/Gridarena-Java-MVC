# Jeu de combat au tour par tour

## Description

Ce jeu est un projet universitaire.
Le but de ce projet était de mettre en place une architecture MVC robuste et d'intégrer diverses design patterns.
Parmis les patterns utilisés on retrouve le pattern observer, le pattern proxy, le pattern adapter, le pattern factory method et le pattern strategy.

## Comment lancer l'application

+ ANT : Ouvrir le terminal à la racine du projet et lancez la commande `ant run`.

+ WINDOWS : Ouvrir le terminal dans le dossier `dist/` et lancez l'exécutable `run.bat`.

+ LINUX : Ouvrir le terminal dans le dossier `dist/` et lancez l'exécutable `run.sh`.

 
## Règles du jeu

Chaque joueur peut:
+ Se déplacer d'une case (haut, bas, gauche, droite).
+ Déposer une mine sur l'une des 8 cases adjacentes.
+ Déposer une bombe sur l'une des 8 cases adjacentes.
+ Utiliser le tir horizontal ou vertical.
+ Déclencher un bouclier, qui rend invincible pendant un tour.
+ Utiliser une hache (haut, bas, gauche, droite).
+ Passer son tour.

Lors de sa création, une grille peut contenir des murs, des kits de soins, des boîtes de
munitions et des barils explosifs. 

Par défaut une bombe explose au bout de trois tours.

Par défaut un tir horizontal ou vertical n’a pas de portée limitée.

Par défaut une mine n’est visible que par celui qui l’a posé.

## Contribuer

+ N'hésitez pas à signaler des bugs.

+ N'hésitez pas à améliorer le code existant.

## Licence

Ce projet est sous licence [MIT](./LICENSE.md).

## Captures d'écran

![vue-joueur](./screenshots/vue-joueur.png)

![vue-jeu-complet](./screenshots/vue-jeu-complet.png)

![choisir personnage](./screenshots/choisir-personnage.png)

## Crédits des images

|                                      image                                        |                                Auteur / Licence                                 |
|:---------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------:|
|  <img src="./src/resources/images/mine.png" alt="mine" width="50" height="50">    |              [mine - Freepik](https://fontawesome.com/icons/)                   |
| <img src="./src/resources/images/health.png" alt="health" width="50" height="50"> |              [health - Freepik](https://fontawesome.com/icons/)                 |
| <img src="./src/resources/images/barrel.png" alt="barrel" width="30" height="50"> |              [barrel - Freepik](https://fontawesome.com/icons/)                 |
|  <img src="./src/resources/images/wall.png" alt="wall" width="50" height="50">    |              [wall - Freepik](https://fontawesome.com/icons/)                   |
|  <img src="./src/resources/images/bomb.png" alt="bomb" width="50" height="50">    |              [bomb - Freepik](https://fontawesome.com/icons/)                   |
|  <img src="./src/resources/images/ammo.png" alt="ammo" width="50" height="50">    |              [ammo - Freepik](https://fr.freepik.com/icone/munitions_3836821)   |
| <img src="./src/resources/images/ground.png" alt="ground" width="50" height="50"> | [décor - Cupnooble](https://cupnooble.itch.io/sprout-lands-asset-pack?download) |
|  <img src="./src/resources/images/vert.png" alt="vert" width="20" height="30">    |   [héros vert - sscary.itch](https://sscary.itch.io/the-adventurer-male)        |
|  <img src="./src/resources/images/violet.png" alt="violet" width="20" height="30">|   [héros violet - sscary.itch](https://sscary.itch.io/the-adventurer-male)      |
|  <img src="./src/resources/images/blue.png" alt="bleu" width="20" height="30">    |   [héros bleu - sscary.itch](https://sscary.itch.io/the-adventurer-male)        |

## Contributeurs

+ [Tom David](https://github.com/kitoutou999)

+ Emilien Huron
