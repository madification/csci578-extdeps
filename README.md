# csci578-extdeps
External dependencies extraction from existing recovery method results.

HOW TO RUN:
Navigate to location of csci578-extdeps.jar
Open Bash window
Enter following
    java -cp csci578-extdeps.jar Main path/to/input/file

Source code located at:
https://github.com/madification/csci578-extdeps

Generates two text files in the same location as the input file with the following labels:
_externalDeps
_externalDeps_Scored

Opens GUI:
The GUI presents information on the potential impact a change to each file could have on the system.
The GUI displays the each file as a circle with a radius equal to the number of files in the system
which use the file of interest. The files are listed below the graph and the user may select individual
files to view their placement on the graph relative to the rest of the files. Users may click the
"Show Details" button to view the data collected on the file. This data includes various scores which
have been calculated as well as the list of files which directly reference (import or include) the
selected file. The scores are explained in detail in this view.