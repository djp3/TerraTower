![Tower with boy looking at it](http://upload.wikimedia.org/wikipedia/commons/c/cb/Broadway_tower_edit.jpg)
TerraTower
==========

An augmented reality physical tower defense inspired game with a splash of capture the flag.  The game engine is in Java and the clients are made in Unity for Android or iOS.
A [description of the game is here](http://www.ics.uci.edu/~djp3/classes/2014_03_ICS163/tasks/terraTowerClient.html).

6/11/14
* We ran a playtest at UCI and got the following feedback:
 * It was fun
 * The pacing of the game was good
 * It was hard to see the screen in the sun

* TODO:
 * Place a person where you are on the map in addition to the camera
 * Fix the colors so you can see them
 * Make the network connections faster
  * Log the timing of the network calls
 * Make the bombs look bigger
 * Check the bomb and tower timings.  They seemed to be too fast
 * Consider combining upload buttons into one
 * Have someway of doing a network check before uploading
 * Add sound feedback
 * Have a news feed of in game events so you can see what happened
 * Have a designated start and stop time for the game session
 * Lower the altitude of the camera
 * Have a points system that is more than just territory
 * Allow people to assign themselves a color

* Brainstorm ideas
 * Have a mini-game to battle between territories
 * See other people on the map
 * Have a land mines
 * Audio help
 * Audio chat
 * Drop random bombs from the sky
 * Announce change in leaderboard

6/3/14
* Implemented the Tower Territory growing

6/2/14
* Finished implementing the Place Tower event

5/30/14:
* Went and fixed a bunch of stuff in luci-utility to support log4j v2
* Set up the framework for the gamemap

5/28/14:
* Laid out the initial project in Eclipse.
* Added "Maven Integration for Eclipse" to Eclipse to manage the project
* Converted the project to a Maven project
* Added a dependency on commons-configuration in Maven
* Added a dependency on luci-utility: https://github.com/djp3/luci-utility

	

