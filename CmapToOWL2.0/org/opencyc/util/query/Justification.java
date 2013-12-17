/*
 * Justification.java
 *
 * Created on August 10, 2004, 2:06 PM
 */

package org.opencyc.util.query;

/**
 * @version $Id: Justification.java 126640 2008-12-04 13:39:36Z builder $
 * @author  mreimers
 */
public interface Justification {
  public QueryResultSet getQueryResultSet();
  public int getQueryResultSetIndex();
  public String toPrettyString();
}
