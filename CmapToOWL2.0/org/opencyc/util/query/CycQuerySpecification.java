/*
 * CycQuerySpecification.java
 *
 * Created on August 11, 2004, 10:49 AM
 */

package org.opencyc.util.query;

import org.opencyc.cycobject.CycList;

/**
 * @version $Id: CycQuerySpecification.java 126640 2008-12-04 13:39:36Z builder $
 * @author  mreimers
 */
public interface CycQuerySpecification extends QuerySpecification {
  
  public CycList getQueryFormula();
}
