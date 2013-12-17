/* $Id: InferenceParameterDescriptions.java 126640 2008-12-04 13:39:36Z builder $
 *
 * Copyright (c) 2004 - 2006 Cycorp, Inc.  All rights reserved.
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 */

package org.opencyc.inference;

//// Internal Imports
import org.opencyc.api.*;

//// External Imports
import java.util.*;

/**
 * <P>InferenceParameterDescriptions is designed to...
 *
 * <P>Copyright (c) 2004 - 2006 Cycorp, Inc.  All rights reserved.
 * <BR>This software is the proprietary information of Cycorp, Inc.
 * <P>Use is subject to license terms.
 *
 * @author tbrussea
 * @date August 2, 2005, 10:21 AM
 * @version $Id: InferenceParameterDescriptions.java 126640 2008-12-04 13:39:36Z builder $
 */
public interface InferenceParameterDescriptions extends Map {
  
  public String stringApiValue();
  public CycAccess getCycAccess();
  public InferenceParameters getDefaultInferenceParameters();

}
