/* $Id: DefaultUntypedInferenceParameter.java 128071 2009-06-19 21:24:49Z baxter $
 *
 * Copyright (c) 2004 - 2009 Cycorp, Inc.  All rights reserved.
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 */

package org.opencyc.inference;

import java.util.Map;

/**
 * <P>DefaultUntypedInferenceParameter is designed to...
 *
 * <P>Copyright (c) 2004 - 2009 Cycorp, Inc.  All rights reserved.
 * <BR>This software is the proprietary information of Cycorp, Inc.
 * <P>Use is subject to license terms.
 *
 * @author baxter
 * @date June 19, 2009
 * @version $Id: DefaultUntypedInferenceParameter.java 128071 2009-06-19 21:24:49Z baxter $
 */
class DefaultUntypedInferenceParameter extends AbstractInferenceParameter {

  public DefaultUntypedInferenceParameter(Map propertyMap) {
    super(propertyMap);
  }

  @Override
  public boolean isValidValue(Object potentialValue) {
    return true;
  }
}
