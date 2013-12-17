//////////////////////////////////////////////////////////////////
//
//    Fries - Feature Retriever for Intensional Encoding of Sentences
//
//    Copyright (C) 2006   TALP Research Center
//                         Universitat Politecnica de Catalunya
//
//    This file is part of the Fries library
//
//    The Fries library is free software; you can redistribute it 
//    and/or modify it under the terms of the GNU General Public
//    License as published by the Free Software Foundation; either
//    version 2 of the License, or (at your option) any later version.
//
//    This library is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//    General Public License for more details.
//
//    You should have received a copy of the GNU General Public
//    License along with this library; if not, write to the Free Software
//    Foundation, Inc., 51 Franklin St, 5th Floor, Boston, MA 02110-1301 USA
//
//    contact: Lluis Padro (padro@lsi.upc.es)
//             TALP Research Center
//             despatx Omega.S112 - Campus Nord UPC
//             08034 Barcelona.  SPAIN
//
////////////////////////////////////////////////////////////////

#ifndef _LANGUAGE
#define _LANGUAGE

#include <string>
#include <list>
#include <vector>

#include "fries/tree.h"
#include "regexp.h"

class word; // predeclaration

////////////////////////////////////////////////////////////////
///   Class analysis stores a possible reading (lemma, PoS, probability)
///   for a word.
////////////////////////////////////////////////////////////////

class analysis {

   private:
      /// lemma
      std::string lemma;
      /// PoS tag
      std::string parole;
      /// probability of that lemma-tag given the word
      double prob;
      ///  of possible senses for that analysis
      std::list<std::string> senses;
      /// information to retokenize the word after tagging if this analysis is selected
      std::list<word> retok;

   public:
      /// user-managed data, we just store it.
      std::vector<std::string> user;

      /// constructor
      analysis();
      /// constructor
      analysis(const std::string &, const std::string &);
      /// assignment
      analysis& operator=(const analysis&);

      void set_lemma(const std::string &);
      void set_parole(const std::string &);
      void set_prob(double);
      void set_retokenizable(const std::list<word> &);

      bool has_prob(void) const;  
      std::string get_lemma(void) const;
      std::string get_parole(void) const;
      std::string get_short_parole(const std::string &) const;
      double get_prob(void) const;
      bool is_retokenizable(void) const;
      std::list<word> get_retokenizable(void) const;

      std::list<std::string> get_senses(void) const;
      void set_senses(const std::list<std::string> &);

      /// Comparison to sort analysis by *decreasing* probability
      int operator<(const analysis &) const;
};


////////////////////////////////////////////////////////////////
///   Class word stores all info related to a word: 
///  form, list of analysis, list of tokens (if multiword).
////////////////////////////////////////////////////////////////

class word : public std::list<analysis> {
   private:
     /// lexical form
     std::string form;                 
     /// first selected analysis (if any)
     word::iterator selected;
     /// empty list if not a multiword
     std::list<word> multiword;
     /// token span
     unsigned long start, finish;
     /// word form found in dictionary
     bool in_dict;
     /// word morphological shouldn't be further modified
     bool locked;

   public:
      /// user-managed data, we just store it.
      std::vector<std::string> user;

      /// constructor
      word();
      /// constructor
      word(const std::string &);
      /// constructor
      word(const std::string &, const std::list<word> &);
      /// constructor
      word(const std::string &, const std::list<analysis> &, const std::list<word> &);
      /// Copy constructor
      word(const word &);
      /// assignment
      word& operator=(const word&);

      /// Get the number of selected analysis
      int get_n_selected(void) const;
      /// get the number of unselected analysis
      int get_n_unselected(void) const;
      /// true iff the word is a multiword compound
      bool is_multiword(void) const;
      /// get number of words in compound
      int get_n_words_mw(void) const;
      /// get word objects that compound the multiword
      std::list<word> get_words_mw(void) const;
      /// get word form
      std::string get_form(void) const;
      /// Get an iterator to the first selected analysis
      word::iterator selected_begin(void);
      /// Get an iterator to the first selected analysis
      word::const_iterator selected_begin(void) const;
      /// Get an iterator to the end of selected analysis list
      word::iterator selected_end(void);
      /// Get an iterator to the end of selected analysis list
      word::const_iterator selected_end(void) const;
      /// Get an iterator to the first unselected analysis
      word::iterator unselected_begin(void);
      /// Get an iterator to the first unselected analysis
      word::const_iterator unselected_begin(void) const;
      /// Get an iterator to the end of unselected analysis list
      word::iterator unselected_end(void);
      /// Get an iterator to the end of unselected analysis list
      word::const_iterator unselected_end(void) const;
      /// get lemma for the selected analysis in list
      std::string get_lemma(void) const;
      /// get parole for the selected analysis  
      std::string get_parole(void) const;
      /// get parole (short version) for the selected analysis  
      std::string get_short_parole(const std::string &) const;

      /// get sense list for the selected analysis  
      std::list<std::string> get_senses(void) const;
      /// set sense list for the selected analysis  
      void set_senses(const std::list<std::string> &);
   
      /// get token span.
      unsigned long get_span_start(void) const;
      unsigned long get_span_finish(void) const;

      /// get in_dict
      bool found_in_dict(void) const;
      /// set in_dict
      void set_found_in_dict(bool);
      /// check if there is any retokenizable analysis
      bool has_retokenizable(void) const;
      /// mark word as having definitive analysis
      void lock_analysis();
      /// check if word is marked as having definitive analysis
      bool is_locked(void) const;

      /// add one analysis to current analysis list  (no duplicate check!)
      void add_analysis(const analysis &);
      /// set analysis list to one single analysis, overwriting current values
      void set_analysis(const analysis &);
      /// set analysis list, overwriting current values
      void set_analysis(const std::list<analysis> &);
      /// set word form
      void set_form(const std::string &);
      /// set token span
      void set_span(unsigned long, unsigned long);
      
      /// look for an analysis with a parole matching given regexp
      bool find_tag_match(RegEx &);

      /// get number of analysis in current list
      int get_n_analysis(void) const; 
      /// copy analysis list
      void copy_analysis(const word &);
      /// empty the list of selected analysis
      void unselect_all_analysis();
      /// mark all analysisi as selected
      void select_all_analysis();
      /// add the given analysis to selected list.
      void select_analysis(word::iterator);
      /// remove the given analysis from selected list.
      void unselect_analysis(word::iterator);
      /// get list of analysis (useful for perl API)
      std::list<analysis> get_analysis(void) const; 
      /// get begin iterator to analysis list (useful for perl/java API)
      word::iterator analysis_begin(void);
      word::const_iterator analysis_begin(void) const;
      /// get end iterator to analysis list (useful for perl/java API)
      word::iterator analysis_end(void);
      word::const_iterator analysis_end(void) const;
};



////////////////////////////////////////////////////////////////
///   Class node stores nodes of a parse_tree
///  Each node in the tree is either a label (intermediate node)
///  or a word (leaf node)
////////////////////////////////////////////////////////////////

class node {
  protected:
    /// is the node the head of the rule?
    bool head;
    /// is the node the root of a chunk? which?
    int chunk;
    /// node label
    std::string label;
    /// sentence word related to the node (if leaf)
    word * w;


  public:
    /// constructors
    node();
    node(const std::string &);
    /// get node label
    std::string get_label(void) const;
    /// get node word
    word get_word(void) const;
    /// set node label
    void set_label(const std::string &);
    /// set node word
    void set_word(word &);
    /// find out whether node is a head
    bool is_head(void) const;
    /// set whether node is a head
    void set_head(const bool);
    /// find out whether node is a chunk
    bool is_chunk(void) const;
    /// set position of the chunk in the sentence
    void set_chunk(const int);
    /// get position of the chunk in the sentence
    int  get_chunk_ord(void) const;

  
};

////////////////////////////////////////////////////////////////
///   Class parse tree is used to store the results of parsing
////////////////////////////////////////////////////////////////

class parse_tree : public tree<node> {
    public:
      parse_tree();
      parse_tree(parse_tree::iterator p);
      parse_tree(const node &);
};


////////////////////////////////////////////////////////////////
/// class denode stores nodes of a dependency tree and
///  parse tree <-> deptree relations
////////////////////////////////////////////////////////////////

class depnode : public node {

  private:
    /// correspondinf node of the parse tree in the same sentence.
    parse_tree::iterator itree;
    /// information about categories which support the building of dependencies
    std::string dep_source;
    std::string dep_target;
    std::string dep_result;

  public:
    depnode();
    depnode(const std::string &);
    depnode(const node &);
    void set_link(const parse_tree::iterator);
    parse_tree::iterator get_link(void);
    /// get link ref (useful for Java API)
    tree<node>& get_link_ref(void);
    
    void set_dep_source(const std::string &);
    void set_dep_target(const std::string &); 
    void set_dep_result(const std::string &); 
    std::string get_dep_source(void) const; 
    std::string get_dep_target(void) const;
    std::string get_dep_result(void) const;
    
};



////////////////////////////////////////////////////////////////
/// class dep_tree stores a dependency tree
////////////////////////////////////////////////////////////////

class dep_tree :  public tree<depnode> {
  public:
    dep_tree();
    dep_tree(const depnode &);
};


////////////////////////////////////////////////////////////////
///   Class sentence is just a list of words that someone
/// (the splitter) has validated it as a complete sentence.
/// It may include a parse tree.
////////////////////////////////////////////////////////////////

class sentence : public std::vector<word> {
 private:
  parse_tree pt;
  dep_tree dt;

 public:
  sentence();
  
  void set_parse_tree(const parse_tree &);
  parse_tree & get_parse_tree(void);
  bool is_parsed() const;
  
  dep_tree & get_dep_tree();
  void set_dep_tree(const dep_tree &);
  bool is_dep_parsed() const;

  /// get word list (useful for perl API)
  std::vector<word> get_words() const;
  /// get iterators to word list (useful for perl/java API)
  sentence::iterator words_begin(void);
  sentence::const_iterator words_begin(void) const;
  sentence::iterator words_end(void);
  sentence::const_iterator words_end(void) const;
};

////////////////////////////////////////////////////////////////
///   Class paragraph is just a list of sentences that someone
///  has validated it as a paragraph.
////////////////////////////////////////////////////////////////

class paragraph : public std::list<sentence> {};

////////////////////////////////////////////////////////////////
///   Class document is a list of paragraphs. It may have additional 
///  information (such as title)
////////////////////////////////////////////////////////////////

class document : public std::list<paragraph> {
  paragraph title;
};


#endif

