/*
 * QueryChangeEvent.java
 *
 * Created on August 10, 2004, 2:56 PM
 */

package org.opencyc.util.query;

import java.util.EventObject;

/**
 * @version $Id: QueryChangeEvent.java 126640 2008-12-04 13:39:36Z builder $
 * @author  mreimers
 */
public class QueryChangeEvent extends EventObject {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private QueryEventReason reason;
  
  public static final QueryEventReason SPECIFICATION_CHANGED = 
    new QueryChangeEvent.QueryEventReason("SPECIFICATION_CHANGED");

  public static final QueryEventReason DATA_AVAILABLE = 
    new QueryChangeEvent.QueryEventReason("DATA_AVAILABLE");
  
  public static final QueryEventReason STATUS_CHANGED = 
    new QueryChangeEvent.QueryEventReason("STATUS_CHANGED");
  
  public QueryChangeEvent(Query source, QueryEventReason reason) {
    super(source);
    this.reason = reason;
  }
  
  public QueryEventReason getReason() { return this.reason; }
  public Query getQuery() { return (Query)getSource(); }
  
  public static class QueryEventReason /*extends com.cyc.common.util.Enumeration*/ {
    
    //// Constructors
    
    /** Creates a new instance of GKEChangeReason.
     * @param name the name of the reason...must be unique for this enumeration.
     */
    public QueryEventReason(String name) { /*super(name);*/ }
    
  }
  //// Public Area
  
  
}
