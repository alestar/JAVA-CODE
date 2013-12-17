/* $Id: InferenceParameters.java 127920 2009-06-02 21:17:00Z baxter $
 *
 * Copyright (c) 2004 - 2006 Cycorp, Inc.  All rights reserved.
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 */

package org.opencyc.inference;

//// Internal Imports
import org.opencyc.api.CycAccess;
import org.opencyc.cycobject.CycSymbol;

//// External Imports
import java.util.Map;
import org.opencyc.api.CycObjectFactory;

/**
 * <P>InferenceParameters is designed to...
 *
 * <P>Copyright (c) 2004 - 2006 Cycorp, Inc.  All rights reserved.
 * <BR>This software is the proprietary information of Cycorp, Inc.
 * <P>Use is subject to license terms.
 *
 * @author zelal
 * @date August 14, 2005, 2:41 PM
 * @version $Id: InferenceParameters.java 127920 2009-06-02 21:17:00Z baxter $
 */
public interface InferenceParameters extends Map<CycSymbol,Object>, Cloneable {

  public final static CycSymbol MAX_NUMBER = CycObjectFactory.makeCycSymbol(":MAX-NUMBER");
  public Integer getMaxNumber();

  public final static CycSymbol MAX_TIME = CycObjectFactory.makeCycSymbol(":MAX-TIME");
  public Integer getMaxTime();
  public void setMaxNumber(Integer max);
  String stringApiValue();
  CycAccess getCycAccess();
  public Object clone();
}
