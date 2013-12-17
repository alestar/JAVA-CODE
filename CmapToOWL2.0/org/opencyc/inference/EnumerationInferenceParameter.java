/* $Id: EnumerationInferenceParameter.java 126640 2008-12-04 13:39:36Z builder $
 *
 * Copyright (c) 2004 - 2006 Cycorp, Inc.  All rights reserved.
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 */

package org.opencyc.inference;

//// Internal Imports
import java.util.List;

/**
 * <P>InferenceParameter is designed to...
 *
 * <P>Copyright (c) 2004 - 2006 Cycorp, Inc.  All rights reserved.
 * <BR>This software is the proprietary information of Cycorp, Inc.
 * <P>Use is subject to license terms.
 *
 * @author tbrussea
 * @date August 2, 2005, 10:25 AM
 * @version $Id: EnumerationInferenceParameter.java 126640 2008-12-04 13:39:36Z builder $
 */
public interface EnumerationInferenceParameter extends InferenceParameter {
  public List getPotentialValues();
}