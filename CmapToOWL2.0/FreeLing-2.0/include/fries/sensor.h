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

//===========================================================
//=                                                         =
//=   This module is based on FEX project developed         =
//=   at U.Illinois, and distributed under GPL              =
//=   with the permission of its authors                    =
//=                                                         =
//=   For info about original version, visit                =
//=      http://l2r.cs.uiuc.edu/~cogcomp/                   =
//=   or contact                                            =
//=      University of Illinois at Urbana/Champaign         =
//=      Department of Computer Science                     =
//=      Dr. Dan Roth - Cognitive Computation Group         =
//=                                                         =
//===========================================================

#ifndef _SENSOR
#define _SENSOR

#include "fries/language.h"
#include "fries/util.h"
#include "regexp.h"
#include <string>
#include <set>
#include <map>
#include <iostream>
#include <fstream>
#include <sstream>

// Added by Scott Yih, 09/27/01
typedef enum { ST_WORD, ST_PHRASE } SensorType;

// Add SensorType --Scott Yih, 09/27/01
class Sensor
{
 public:

  // default setting
  Sensor(std::string targ) { includeLocation = false; sensorType = ST_WORD; target=targ;}
  virtual ~Sensor() {}

  bool IncludeLocation()  { return includeLocation; }
  void IncludeLocation(bool val) { includeLocation = val; }
  SensorType getSensorType() { return sensorType; }
  std::string solve_target(const sentence &sent, int rec) {
    std::string ins;
    if (target=="w") ins = sent[rec].get_form();
    else if (target=="l") ins = sent[rec].get_lemma();
    else if (target=="t") ins = sent[rec].get_parole();
    else if (target.substr(0,2)=="u.") {
      unsigned int i = util::string2int(target.substr(2,target.length()-2));
      if (i >= sent[rec].user.size()) {			 
       std::cerr << "RGF file refers to unexisting user field number "<<i<< std::endl;
       exit(-1);
      }
      ins = sent[rec].user[i];
    }
    return(ins);
  }

  // If the sensor is PHRASE TYPE, then the last two parameters are
  //   treated as the start and end locations of the phrase.
  virtual void Extract( const sentence &sent,
			std::set<std::string> &outSet,
			int rec_OR_start,
			int targLoc_OR_end ) = 0;

 protected:
  void Output( std::set<std::string> &outSet, std::string feat, int loc);
  SensorType sensorType;
  std::string target;

 private:
  std::string& PostProcess( std::string &feat, const char* checkFeat );
  bool includeLocation;
};


class SensorData : public Sensor
{
 public:
  SensorData(std::string targ) : Sensor(targ) {};
  
  void Extract( const sentence &sent,std::set<std::string> &outSet,int rec,int targLoc );
};


class SensorMap : public Sensor
{
 public:
 
   SensorMap(char* targ, char* fname, char* dpath) : Sensor(targ) {

     char filename[512];

     if (fname[0] == '/')  // absolute path, leave untouched
       strcpy(filename,fname);
     else { // relative, use datapath as base
       strcpy(filename,dpath);
       strcat(filename,fname);
     }
       
     std::ifstream mapFile(filename);
     if (!mapFile) {			 
       std::cerr << filename << " not found" << std::endl;
       exit(-1);
     }
      
     std::string key, data;
     while(!mapFile.eof()) {
       mapFile>>key>>data;
       content.insert(std::pair<std::string, std::string>(key, data));
     }
     mapFile.close();
   };
   
  void Extract( const sentence &sent,std::set<std::string> &outSet,int rec,int targLoc ); 
 private:
    std::map<std::string,std::string> content;
};


class SensorSet : public Sensor
{
 public:
   SensorSet(char* targ, char* fname, char* dpath) : Sensor(targ) {

     char filename[512];

     if (fname[0] == '/')  // absolute path, leave untouched
       strcpy(filename,fname);
     else { // relative, use datapath as base
       strcpy(filename,dpath);
       strcat(filename,fname);
     }
       
     std::ifstream setFile(filename);
     if (!setFile) {			 
       std::cerr << filename << " not found" << std::endl;
       exit(-1);
     }

     std::string key;
     while(!setFile.eof()) {
       setFile>>key;
       content.insert(key);
     }
     setFile.close();
    };
  
  void Extract( const sentence &sent,std::set<std::string> &outSet,int rec,int targLoc );

 private:
    std::set<std::string> content;
};

class SensorSetPart : public Sensor {
 public:
  SensorSetPart(char* targ, char* fname, char* dpath) : Sensor(targ) {
    
    char filename[512];
    
    if (fname[0] == '/')  // absolute path, leave untouched
      strcpy(filename,fname);
    else { // relative, use datapath as base
      strcpy(filename,dpath);
      strcat(filename,fname);
    }
    
    std::ifstream setFile(filename);
    if (!setFile) {			 
      std::cerr << filename << " not found" << std::endl;
      exit(-1);
    }
    
    std::string key;
    while(!setFile.eof()) {
      setFile>>key;
      content.insert(key);
    }
    setFile.close();
  };
  
  void Extract( const sentence &sent,std::set<std::string> &outSet,int rec,int targLoc );

 private:
  std::set<std::string> content;
};


class SensorMatchRE : public Sensor
{
 public:
  SensorMatchRE(char* targ, char* expr) : Sensor(targ), expression(expr) {};
  
  void Extract( const sentence &sent,std::set<std::string> &outSet,int rec,int targLoc );

 private:
    RegEx expression;
};



class SensorCheckMwRE : public Sensor
{
 public:
  SensorCheckMwRE(char* targ, char* expr) : Sensor(targ) 
  {
    mw_patrons = util::string2list(std::string(expr), ";");
  };

  void Extract( const sentence &sent, std::set<std::string> &outSet, int rec, int targLoc );

  private:
    std::list<std::string> mw_patrons;
};



class SensorCheckRE : public Sensor
{ 
 public:
  
  SensorCheckRE(char* targ, char* expr) : Sensor(targ), expression(expr) {};
  
  void Extract( const sentence &sent,std::set<std::string> &outSet,int rec,int targLoc );

 private:
    RegEx expression;
};

#endif

