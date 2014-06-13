![Tower with boy looking at it](http://upload.wikimedia.org/wikipedia/commons/c/cb/Broadway_tower_edit.jpg)
TerraTower
==========

An augmented reality physical tower defense inspired game with a splash of capture the flag.  The game engine is in Java and the clients are made in Unity for Android or iOS.
A [description of the game is here](http://www.ics.uci.edu/~djp3/classes/2014_03_ICS163/tasks/terraTowerClient.html).

* Brainstorm ideas
 * Have a mini-game to battle between territories
 * See other people on the map
 * Have a land mines
 * Audio help
 * Audio chat
 * Drop random bombs from the sky

* TODO:
 * Performance
   * Server needs to be more robust
   * The game state update must be sped up drastically
     * Profile a multi-player simulated game to find where the hotspots are
     * Make the network connections faster
       * Log the timing of the network calls
   * Put some kind of interlock in the client so people stop mashing buttons for refreshes
 * Client U/I
   * Android client needs a decent vertex color shader
   * Place a person where you are on the map in addition to the camera
   * Consider combining upload buttons into one
   * Have someway of doing a network check before uploading
   * Add sound feedback
   * Lower the altitude of the camera
 * Game Play
   * Check the bomb and tower timings.  They seemed to be too fast
   * Bombs should blow up an area corresponding to physical radius, not number of squares
   * Territory should grow in a circle better
   * Have a news feed of in game events so you can see what happened
   * Have a designated start and stop time for the game session
   * Have a points system that is more than just territory
   * Allow people to assign themselves a color
   * Announce change in leaderboard

6/13/14
 * Made the bombs look bigger
 * Fixing the territory caching on the server/engine side
 * Fixed the client colors so they are chosen from a vibrant palette 

6/12/14
* We played a big game with 26 teams for the ICS 163 final exam
 * Fun

6/11/14
* We ran a playtest at UCI and got the following feedback:
 * It was fun
 * The pacing of the game was good
 * It was hard to see the screen in the sun


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

	

