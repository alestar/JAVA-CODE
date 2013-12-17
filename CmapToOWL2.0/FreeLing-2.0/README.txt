
  README File for FreeLing 2.0 MS-Windows port
  -----------------------------------------

   
  - - - - - - - - DISCLAIMER - - - - - - - - 

  FreeLing is a C++ Library for language analysis, developed
  under Unix/Linux systems. 

  This windows port is only provided because many people want
  to use FreeLing under MS-Windows, but IT IS NOT supported,
  and DOES NOT perform as efficiently as it would in a 
  unix-like system.

  This port has only been tested on Windows-XP. 

  If you experience trouble installing or running this software
  under windows, DO NOT address FreeLing developers. 
  Instead, you may try to get some help from the user community
  at http://www.lsi.upc.edu/~nlp/freeling

  - - - - - - - - - - - - - - - - - - - - - - 

  To run FreeLing under MS-Windows, unpack the zipfile in
  the folder where you want it installed (e.g. C:\FreeLing)

  Then, open a MS-DOS command line and cd to the Freeling 
  (or whatever you called it) folder with:

   cd C:\FreeLing

  The command to execute the analyzer is 

   bin\analyze -f share\config\es.cfg

   This will run a default Spanish analyzer, that will
   read text from the keyboard (press CTL-D to end) 
   You can specify another .cfg file in the share\config
   folder, or write one that suits your needs.

   Note that the default configuration files use a 
   variable FREELINGSHARE that has to be defined. 
   You can either edit the files and replace that 
   variable with the actual location of the files,
   or set the variable with the command:

   set FREELINGSHARE=C:\FreeLing\share

   (or the right path where you decompressed the files)
   
   
   To find out how to analyze text files or how to 
   use advanced options, refer to the User Manual at:

     doc\userman\userman.pdf
     doc\userman\html\userman.html

   Although the manuals are unix-oriented, the commands 
   and file redirections work the same under MS-DOS, 
   provided you use the backslash "\" instead of the 
   slash "/" in folder paths.

