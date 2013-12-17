/*
 * Query.java
 *
 * Created on August 10, 2004, 2:04 PM
 */

package org.opencyc.util.query;

import java.util.Set;

/**
 * @version $Id: QuerySpecification.java 126640 2008-12-04 13:39:36Z builder $
 * @author  mreimers
 */
public interface QuerySpecification {
  
  public String getGloss();
  
  public Object getQuestion();
  
  public Set getConstraints();
  public Set getFilteredConstraints(Class constraintType);
  
  public Object clone();
}
