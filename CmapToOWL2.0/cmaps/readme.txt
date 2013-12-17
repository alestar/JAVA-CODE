Falta
Analyzer
Salida mapa indentado
Guardar Macosoft
Cargar y guardar cxl
Reporte
Dominios

/* Pointer type and search type counts */

#define MAXPTR		19
#define MAXSEARCH       29

/* Pointers */

#define ANTPTR           1	/* ! */
#define HYPERPTR         2	/* @ */
#define HYPOPTR          3	/* ~ */
#define ENTAILPTR        4	/* * */
#define SIMPTR           5	/* & */

#define ISMEMBERPTR      6	/* #m */
#define ISSTUFFPTR       7	/* #s */
#define ISPARTPTR        8	/* #p */

#define HASMEMBERPTR     9	/* %m */
#define HASSTUFFPTR     10	/* %s */
#define HASPARTPTR      11	/* %p */

#define MERONYM         12	/* % (not valid in lexicographer file) */
#define HOLONYM         13	/* # (not valid in lexicographer file) */
#define CAUSETO         14	/* > */
#define PPLPTR	        15	/* < */
#define SEEALSOPTR	16	/* ^ */
#define PERTPTR		17	/* \ */
#define ATTRIBUTE	18	/* = */
#define VERBGROUP	19	/* $ */

#define SYNS            (MAXPTR + 1)
#define FREQ            (MAXPTR + 2)
#define FRAMES          (MAXPTR + 3)
#define COORDS          (MAXPTR + 4)
#define RELATIVES	(MAXPTR + 5)
#define HMERONYM        (MAXPTR + 6)
#define HHOLONYM	(MAXPTR + 7)
#define WNESCORT	(MAXPTR + 8)
#define WNGREP		(MAXPTR + 9)
#define OVERVIEW	(MAXPTR + 10)



Pointers are used to represent the relations between the words in one synset and another. Semantic pointers represent relations between word meanings, and therefore pertain to all of the words in the source and target synsets. Lexical pointers represent relations between word forms, and pertain only to specific words in the source and target synsets. The following pointer types are usually used to indicate lexical relations: Antonym, Pertainym, Participle, Also See. The remaining pointer types are generally used to represent semantic relations. 
A relation from a source to a target synset is formed by specifying a word from the target synset in the source synset, followed by the pointer_symbol indicating the pointer type. The location of a pointer within a synset defines it as either lexical or semantic. The Lexicographer File Format section describes the syntax for entering a semantic pointer, and Word Syntax describes the syntax for entering a lexical pointer. 

Although there are many pointer types, only certain types of relations are permitted between synsets of each syntactic category. 

The pointer_symbol s for nouns are: 

!    Antonym 
@    Hypernym 
~    Hyponym 
#m    Member meronym 
#s    Substance meronym 
#p    Part meronym 
%m    Member holonym 
%s    Substance holonym 
%p    Part holonym 
=    Attribute 

The pointer_symbol s for verbs are: 

!    Antonym 
@    Hypernym 
~    Hyponym 
*    Entailment 
>    Cause 
^    Also see 
$    Verb Group 

The pointer_symbol s for adjectives are: 

!    Antonym 
&    Similar to 
<    Participle of verb 
\    Pertainym (pertains to noun) 
=    Attribute 
^    Also see 

The pointer_symbol s for adverbs are: 

!    Antonym 
\    Derived from adjective 

Many pointer types are reflexive, meaning that if a synset contains a pointer to another synset, the other synset should contain a corresponding reflexive pointer. grind(1WN) automatically inserts missing reflexive pointers for the following pointer types: 


Pointer Reflect  

Antonym  Antonym  
Hyponym  Hypernym  
Hypernym  Hyponym  
Holonym  Meronym  
Meronym  Holonym  
Similar to  Similar to  
Attribute  Attribute  
Verb Group  Verb Group  