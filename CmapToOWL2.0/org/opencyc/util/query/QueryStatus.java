/*
 * QueryStatus.java
 *
 * Created on August 10, 2004, 2:14 PM
 */

package org.opencyc.util.query;

/**
 * @version $Id: QueryStatus.java 126640 2008-12-04 13:39:36Z builder $
 * @author  mreimers
 */
public class QueryStatus { 
  private String text;

  protected QueryStatus(String s) { text = s; }

  public String toString() { 
    return text;
  }
  public final static QueryStatus READY = new QueryStatus("Ready");
  public final static QueryStatus WORKING = new QueryStatus("Working");
  public final static QueryStatus PAUSED = new QueryStatus("Paused");
  public final static QueryStatus FINISHED = new QueryStatus("Finished");
}
