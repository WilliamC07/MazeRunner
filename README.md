# MazeRunner
This is an escape-the-maze game, where walls block your line of sight.  
You start in the top left corner and your objective is to reach the bottom right.  
Monsters roam the maze and will chase you if you enter their line of sight.  
To stop the monsters from chasing you, cut off their line of sight by rounding a corner.  

Controls:  
WASD or Arrow Keys to move  
H for a hint  
G to give up, shows way out  


# William  
5/17/19:  
Created bash script to compile and run the project and added the jar.  
Wrote boilerplate code for Wall, Character, and Main  
Can render Wall and Character  
5/18/19:  
Can determine point of intersection between two line segments  
More boilerplate code (distance formula, accessors)
Merged ray casting to master  
5/19/19:
Draw additional rays to give a smoother field of vision
Fixed calculating intersection between two line segments and intersection between a ray and line segment  
5/20/19:
Half completed ray casting drawing triangles for field of vision. A small bug of not drawing some triangles
Merged and connect Eric's work to mine  
5/21/19
Floating point rounding error bug found
Class time to figure out a better way to represent the maze in memory to increase efficiency
5/22/19
Fixed ray casting -- actually works now (tested on custom walls)
Cleaned up Ray.java to simplified creation (only one constructor; down from 3)
Merged ray_casting_fix to master  
5/23/19  
Found out ray casting is still broken  
Continued work on ray casting
5/24/19
Continued work on ray casting  
5/25/19
Finished ray casting and removed testing code
Merged ray casting fix to master  
Working on converting a boolean[][] of where walls are to a 2D grid of walls
5/28/19  
Continued working on wall Generation  
Merged working branch to deal with merge conflict early  
5/29/19  
Fixed generating horizontal walls (fixed infinite loop)  
Starting working on vertical walls  
5/30/19  
Continued work on converting maze to walls  
6/1/19  
Finish converting maze to walls
Fixed ray casting to work when two walls intersect but don't share endpoint  
Removed unused code    
Map is moved around the screen with the player to allow for large maps without parts cutting off  
Created god mode to view the game without ray casting  
Merged all branches and fixed up any merging logical error (deleted too many methods)  
6/2/19  
Working on title screen -- added images and created title screen (missing play and what is this section)  
6/3/19  
Finished title screen mode  
Fixed too many verticies forming and causing weird rays to form  
Added check to see if two Character can see each other (no walls blocking)  
Started and finish end screen (win and lose) and starting a new game

# Eric  
5/17/19  
Finished Renderable Interface  
Finished Skeleton of Point Class  
5/18/19  
Finished Skeleton of Ray Class (Constructors,Accessors)  
Basic Character Movement - WASD Controls  
5/19/19  
Initial Attempts at Basic Maze Generation  
Constructs Maze w/ All Walls Filled  
5/20/19  
Completed Maze Generation - Recursively Remove Walls  
Bug Fixes - Incorrect Visit Checks  
5/21/19   
Completed Flattening of Maze  
Changed Character to Take Flattened Maze  
5/22/19  
Initial Attempt at Converting Maze to Boolean Array   
Used for A* Algorithm  
Used for Ray Casting  
5/27/19  
Fixed Boolean Conversion - Missing Walls At Intersections  
5/28/19  
Started and Finished A* Maze Solving  
5/29/19  
Upgraded Movement - Arrow Keys  
Movement Smoothed Out  
Implemented Diagonal Movement  
5/30/19  
Started and Finished Hints  
Fixed Many Bugs Associated w/ Hints = See Commits  
Added Give Up System - Displays Path Out  
5/31/19  
Constrained Player to Prevent Clipping Through Wall Border  
6/2/19  
Updated Hints to Work With Maze Movement  
Changed Hint to Display a Line Instead of Dots  
Added Minimap Functionality  
Added Godmode Minimap Functionality  
Started Monsters - Can Spawn Monsters and Roam  
6/3/19  
Finished Monsters - Chase Players if in Line of Sight  
Stopped Players from Clipping Through Walls  
Added Victory and Defeat Conditions  
Fixed Non-Square Mazes of Greater Length Than Width  

