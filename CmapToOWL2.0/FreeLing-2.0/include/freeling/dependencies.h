//////////////////////////////////////////////////////////////////
//
//    FreeLing - Open Source Language Analyzers
//
//    Copyright (C) 2004   TALP Research Center
//                         Universitat Politecnica de Catalunya
//
//    This library is free software; you can redistribute it and/or
//    modify it under the terms of the GNU General Public
//    License as published by the Free Software Foundation; either
//    version 2.1 of the License, or (at your option) any later version.
//
//    This library is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//    General Public License for more details.
//
//    You should have received a copy of the GNU General Public
//    License along with this library; if not, write to the Free Software
//    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
//
//    contact: Lluis Padro (padro@lsi.upc.es)
//             TALP Research Center
//             despatx C6.212 - Campus Nord UPC
//             08034 Barcelona.  SPAIN
//
////////////////////////////////////////////////////////////////


#ifndef _DEPENDENCIES
#define _DEPENDENCIES

#include "fries/language.h"
#include "freeling/dep_rules.h"
#include "freeling/semdb.h"

#include <string>
#include <map>
#include <vector>


////////////////////////////////////////////////////////////////
///
///  The class completer implements a parse tree completer,
/// which given a partial parse tree (chunker output), completes
/// the full parse according to some grammar rules.
///
////////////////////////////////////////////////////////////////

class completer {
  private:
    /// set of rules, indexed by labels of nodes
    std::map<std::pair<std::string,std::string>,completerRule>  chgram;
    /// find last node with given label in a subtree
    parse_tree::iterator find_last_label(parse_tree *, const std::string &) const;
    /// retrieve rule from grammar
    completerRule find_grammar_rule(const std::string &, const std::string &);
    /// apply a completion rule
    parse_tree * applyRule(const completerRule &, parse_tree *, parse_tree *);
    /// select which is the active weight of a rule, depending on its context
    void select_active_weight(std::vector<parse_tree *> &, unsigned int, completerRule &);

  public:  
    /// Constructor. Load a tree-completion grammar 
    completer(const std::string &);
    /// find best completions for given parse tree
    parse_tree complete(parse_tree &, const std::string &);
};


////////////////////////////////////////////////////////////////////////
///
/// depLabeler is class to set labels into a dependency tree
///
///////////////////////////////////////////////////////////////////////

class depLabeler {

  private:
    // set of rules
    std::map<std::string, std::list<ruleLabeler> > rules;
    // semantic database to check for semantic conditions in rules
    semanticDB * semdb;
    // parse a condition and create checkers.
    rule_expression* build_expression(const std::string &);

  public:
    /// Constructor. create dependency parser
    depLabeler(const std::string &);
    /// Destructor
    ~depLabeler();
    /// Label nodes in a dependency tree. (Initial call)
    void label(dep_tree*);
    /// Label nodes in a dependency tree. (recursive)
    void label(dep_tree*, dep_tree::iterator);
};



///////////////////////////////////////////////////////////////////////
///
/// dependencyMaker is a class for obtaining a dependency tree from chunks.
///  this implementation uses two subclasses:
/// completer: to complete the chunk analysis in a full parse tree
/// depLabeler: to set the labels once the class has build a dependency tree
///
///////////////////////////////////////////////////////////////////////

class dependencyMaker {

 private:
   /// tree completer  
   completer comp; 
   /// dependency labeler
   depLabeler labeler;
   // Root symbol used by the chunk parser when the tree is not complete.
   std::string start;

   /// compute dependency tree
   dep_tree* dependencies(parse_tree &tr);

 public:   
   /// constructor
   dependencyMaker(const std::string &, const std::string &);
   /// Enrich all sentences in given list with a depenceny tree.
   void analyze(std::list<sentence> &);
   /// Enrich all sentences in given list, return a copy.
   std::list<sentence> analyze(const std::list<sentence> &);
};

#endif

