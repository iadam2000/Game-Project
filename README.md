# Game

Welcome to my group university game development project!

**Accessing Debug Mode in the game**

When in a level, you can access debug mode by pressing the 'g' key. In debug mode, the game is effectively paused (although no pause screen should be visible unless 'esc' is pressed). You can use the arrow keys/WASD to look around the level. If you press 'space' when in debug mode, you will enable player debugging.

If player debugging is enabled, the various collision boxes associated with the player will be displayed. The green box is for collisions with the sides and top, and the red box is for detecting collisions on the bottom. In player debugging, the data about the player's jump is also printed to the command line whenever the user jumps. The player is also made invincible. Player debugging is enabled until the user disables it as shown below. This means the user can exit debug mode and play through the game with these collision boxes visible. 

To disable debugging mode, press 'g' again. To disable player debugging, go into debug mode and press 'space'.

-----


**Running the game from the Command Line**

~~Quick disclaimer: I have not figured out how to run the program directly from the command line (i.e. by running 'java Launcher'). My method below will direct you to compile the code to a runnable .jar file and then run that file from the command line.~~

Praise be to Paul! With his advice, I realised I was being an idiot and so have figured out how to run the stuff from the command line. The jar file method is still there, just below the new command line method (last disclaimer still applies).

Before you start, you will need the **December 2013** jsfml.jar. It can be downloaded from the address below (will download a zip file for you to extract).

Also, please use the Command Prompt where possible. The VS Code terminal and PowerShell have both resulted in problems.

https://jsfml.sfmlprojects.org/?page=download

-----

**To run the game from the command line, you should:**

Disclaimer: Only tested on my windows laptop so far.

1 - Move the jsfml.jar file into the same folder as the .java files.

2 - Compile the .java files, making sure to include jsfml.jar in the class path. This can be done by running the following command from withing the folder containing the .java files and the jsfml.jar file:

    javac -cp jsfml.jar *.java

If you want, you can tidy things up by moving the resulting .class files into a new folder.

3 - Create a new folder called 'src' and place the Assets folder inside of it.

4 - Make sure the following files are all in the same folder:
*   All .class files from the previous compile operation.
*   The jsfml.jar file.  
*   The 'src' folder containing the Assets folder with images, music, etc...

5 - From inside the folder with the above files, run the following command:

    java -cp jsfml.jar; Launcher

The game should now be running.

If using Linux, you might need to replace the '**;**' with '**:**'.

**Note:** the arguments 'jsfml.jar' and 'Launcher' can both be paths that lead to the files wherever they are on the system. If you do this, you can skip step 3 - although the Assets folder should be in the same folder as the .class files.

-----


**To compile the code into a runnable .jar file, you should:**

Disclaimer: This works on Windows, but seems to have problems on the University's Linux VM.

1 - Move the jsfml.jar file into the same folder as the .java files.

2 - Compile the .java files, making sure to include jsfml.jar in the class path. This can be done by running the following command from withing the folder containing the .java files and the jsfml.jar file:

    javac -cp jsfml.jar *.java

If you want, you can tidy things up by moving the resulting .class files into a new folder.

3 - Make sure the following files are all in the same folder:
*   All .class files from the previous compile operation.
*   The jsfml.jar file.  
*   The Assets folder containing the images, music, etc..     

4 - Create a new text file (mine will be called 'manifest.txt'). Inside this text file write out the text shown below (Each line of text should be on a new line, but there shouldn't be any blank lines between the three lines).

 Manifest-Version: 1.0
 
 Main-Class: Launcher

 Class-Path: jsfml.jar
 

5 - Run the following command from within the folder containing the .class files, jsfml.jar, the Assets folder and the text file:

	jar cvfm groupGame.jar manifest.txt *.class jsfml.jar Assets

Note - groupGame.jar is the name of the jar file we're creating, this can be whatever you want.

6 - You have now created the jar file. It can be run using:
	
	java -jar groupGame.jar
# Second-Year-Game-Project
