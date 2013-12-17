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


#ifndef _DEPRULES
#define _DEPRULES

#include <sstream>
#include <iostream>
#include <set>
#include <list>

#include "freeling/semdb.h"

////////////////////////////////////////////////////////////////
///
///  The class completerRule stores rules used by the
/// completer of parse trees
///
////////////////////////////////////////////////////////////////

class completerRule {
 
 public:
   std::string chunk;
   std::string leftChunk;
   std::string newNode;
   std::string operation;
   std::string dependenceLabel;
   std::list<std::pair<std::string,int> > weight_spec;
   int weight;
   
   /// constructors
   completerRule();
   completerRule(const std::string &, const std::string&);
   completerRule( const completerRule &);
   /// assignment
   completerRule & operator=( const completerRule &);
   /// auxiliar for rule parsing
   void parse_weight_spec(std::istringstream &s);
   
   /// Comparison. The more weight the higher priority 
   int operator<(const completerRule & a ) const;
};


////////////////////////////////////////////////////////////////
///
/// The class rule_expression is an abstract class (interface)
/// for building dynamic restriction on a ruleLabeler 
///  which are used by class depLabeler
///
////////////////////////////////////////////////////////////////
class rule_expression {

  private:
    dep_tree::iterator parse_node_ref(std::string, dep_tree::iterator) const;

  protected:
    // node the expression has to be checked against (p/d)
    std::string node;
    // set of values (if any) to check against.
    std::set<std::string> valueList;
    // obtain the iterator to the node to be checked
    dep_tree::iterator node_to_check(dep_tree::iterator, dep_tree::iterator) const;

  public:
    // constructors and destructors
    rule_expression();
    rule_expression(const std::string &,const std::string &);
    virtual ~rule_expression() {}
    // search a value in expression list
    bool find(const std::string &) const;
    bool find_match(const std::string &) const;
    bool find_any(const std::list<std::string> &) const;
    bool find_any_match(const std::list<std::string> &) const;
    // virtual, evaluate expression
    virtual bool eval(dep_tree::iterator, dep_tree::iterator) const =0;
};


/////////////////////////////////////////////////////////////////
///
/// The following classes are implementations of different constraints
///
/// check_and:  and of basic constraints
/// check_not:  negation
/// check_side: side (left/rigth) of descendant wrt ancestor 
/// check_lemma: check if lemma of given node is in list of lemmas (separator character is |)
/// check_category: check PoS category of given node
/// check_wordclass: check if lemma of given node belongs to a word class
/// check_tonto: Check if given node has a top ontology property
///
/////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////
/// And of basic contraints
////////////////////////////////////////////////////////////////

class check_and : public rule_expression {
  private:
    std::list<rule_expression *> check_list;
  public:
    void add(rule_expression *);
    bool eval(dep_tree::iterator  ancestor, dep_tree::iterator  descendant) const;
};


////////////////////////////////////////////////////////////////
/// negation
////////////////////////////////////////////////////////////////

class check_not : public rule_expression {
  private:
    rule_expression * check_op;
  public:
    check_not(rule_expression *);
    bool eval(dep_tree::iterator, dep_tree::iterator) const;
};


////////////////////////////////////////////////////////////////
///  side of descendant respect to ancestor (or viceversa)
////////////////////////////////////////////////////////////////

class check_side : public rule_expression {
  public:
    check_side(const std::string &,const std::string &);
    bool eval (dep_tree::iterator, dep_tree::iterator) const;
};


////////////////////////////////////////////////////////////////
/// find lemma in list of lemmas (separator character is |)
////////////////////////////////////////////////////////////////

class check_lemma : public rule_expression {
  public:
    check_lemma(const std::string &,const std::string &);
    bool eval(dep_tree::iterator, dep_tree::iterator) const;
};


////////////////////////////////////////////////////////////////
/// check category category
////////////////////////////////////////////////////////////////

class check_category :public rule_expression {
  public:
    check_category(const std::string &,const std::string &);
    bool eval(dep_tree::iterator, dep_tree::iterator) const;
};


////////////////////////////////////////////////////////////////
/// lemma belongs to a class
////////////////////////////////////////////////////////////////

class check_wordclass : public rule_expression { 
  public:    
    static std::set<std::string> wordclasses; // items are class#verbLemma
    check_wordclass(const std::string &, const std::string &);
    bool eval (dep_tree::iterator, dep_tree::iterator) const;
};

////////////////////////////////////////////////////////////////
/// lemma has given top ontology class
////////////////////////////////////////////////////////////////

class check_tonto : public rule_expression { 
  private: 
    semanticDB &semdb;
  public:    
    check_tonto(semanticDB &, const std::string &, const std::string &);
    bool eval (dep_tree::iterator, dep_tree::iterator) const;
};

////////////////////////////////////////////////////////////////
/// lemma has given WN semantic file
////////////////////////////////////////////////////////////////

class check_semfile : public rule_expression { 
  private: 
    semanticDB &semdb;
  public:    
    check_semfile(semanticDB &, const std::string &, const std::string &);
    bool eval (dep_tree::iterator, dep_tree::iterator) const;
};

////////////////////////////////////////////////////////////////
/// lemma has given synonym
////////////////////////////////////////////////////////////////

class check_synon : public rule_expression { 
  private: 
    semanticDB &semdb;
  public:    
    check_synon(semanticDB &, const std::string &, const std::string &);
    bool eval (dep_tree::iterator, dep_tree::iterator) const;
};

////////////////////////////////////////////////////////////////
/// lemma or any ancestor has given synonym
////////////////////////////////////////////////////////////////

class check_asynon : public rule_expression { 
  private: 
    semanticDB &semdb;
  public:    
    check_asynon(semanticDB &, const std::string &, const std::string &);
    bool eval (dep_tree::iterator, dep_tree::iterator) const;
};


////////////////////////////////////////////////////////////////
/// ruleLabeler is an auxiliary class for the depLabeler
////////////////////////////////////////////////////////////////

class ruleLabeler {

  public:
   std::string label;
   rule_expression * re;
   std::string ancestorLabel;

   ruleLabeler(void);
   ruleLabeler(const std::string &, rule_expression *);
   bool eval(dep_tree::iterator, dep_tree::iterator) const;
};


////////////////////////////////////////////////////////////////
/// auxiliary class to store information about dependency building
////////////////////////////////////////////////////////////////

class dep_info {

  private:
    std::string dep_source;
    std::string dep_target;
    std::string dep_result;

  public:
    dep_info(void);
    dep_info(const std::string &, const std::string &, const std::string &);
    
    std::string get_dep_source(void);
    std::string get_dep_target(void);
    std::string get_dep_result(void);
    void set_dep_source(const std::string &);
    void set_dep_target(const std::string &);
    void set_dep_result(const std::string &);
};


#endif
