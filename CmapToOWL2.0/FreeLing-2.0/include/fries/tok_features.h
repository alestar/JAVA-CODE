/* A Bison parser, made by GNU Bison 2.0.  */

/* Skeleton parser for Yacc-like parsing with Bison,
   Copyright (C) 1984, 1989, 1990, 2000, 2001, 2002, 2003, 2004 Free Software Foundation, Inc.

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2, or (at your option)
   any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software
   Foundation, Inc., 59 Temple Place - Suite 330,
   Boston, MA 02111-1307, USA.  */

/* As a special exception, when this file is copied by Bison into a
   Bison output file, you may use that output file without restriction.
   This special exception was added by the Free Software Foundation
   in version 1.24 of Bison.  */

/* Tokens.  */
#ifndef YYTOKENTYPE
# define YYTOKENTYPE
   /* Put the tokens into the symbol table, so that GDB and other debuggers
      know about them.  */
   enum yytokentype {
     SENSOR = 258,
     TARG = 259,
     COMPRGF = 260,
     INT = 261,
     CONJUNCT = 262,
     WORD = 263,
     FLAG = 264,
     COLON = 265,
     LBRACK = 266,
     RBRACK = 267,
     LPAREN = 268,
     RPAREN = 269,
     NEWLINE = 270,
     COMMA = 271,
     AMP = 272,
     BAR = 273,
     SMALLX = 274,
     BIGX = 275,
     EQUALS = 276,
     SEMICOLON = 277,
     FLOC = 278,
     FINC = 279,
     FMARK = 280,
     ERROR = 281,
     QUOTE = 282
   };
#endif
#define SENSOR 258
#define TARG 259
#define COMPRGF 260
#define INT 261
#define CONJUNCT 262
#define WORD 263
#define FLAG 264
#define COLON 265
#define LBRACK 266
#define RBRACK 267
#define LPAREN 268
#define RPAREN 269
#define NEWLINE 270
#define COMMA 271
#define AMP 272
#define BAR 273
#define SMALLX 274
#define BIGX 275
#define EQUALS 276
#define SEMICOLON 277
#define FLOC 278
#define FINC 279
#define FMARK 280
#define ERROR 281
#define QUOTE 282




#if ! defined (YYSTYPE) && ! defined (YYSTYPE_IS_DECLARED)
#line 90 "features.y"
typedef union YYSTYPE {
  int i;      /* For returning numbers  */
  SubRGF *s;  /* For returning SubRGF*  */
  RGF *r;     /* For returning RGF*     */
  char *c;    /* For returning char*    */
} YYSTYPE;
/* Line 1318 of yacc.c.  */
#line 98 "tok_features.h"
# define yystype YYSTYPE /* obsolescent; will be withdrawn */
# define YYSTYPE_IS_DECLARED 1
# define YYSTYPE_IS_TRIVIAL 1
#endif

extern YYSTYPE yylval;



