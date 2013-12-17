/* $Id: DefaultBooleanInferenceParameter.java 128086 2009-06-22 16:59:36Z builder $
 *
 * Copyright (c) 2004 - 2006 Cycorp, Inc.  All rights reserved.
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 */
package org.opencyc.inference;


//// External Imports
import java.util.Map;

//// Internal Imports
import org.opencyc.api.CycObjectFactory;

/**
 * <P>DefaultBooleanInferenceParameter is designed to...
 *
 * <P>Copyright (c) 2004 - 2006 Cycorp, Inc.  All rights reserved.
 * <BR>This software is the proprietary information of Cycorp, Inc.
 * <P>Use is subject to license terms.
 *
 * @author zelal
 * @date August 9, 2005, 9:09 PM
 * @version $Id: DefaultBooleanInferenceParameter.java 128086 2009-06-22 16:59:36Z builder $
 */
public class DefaultBooleanInferenceParameter extends AbstractInferenceParameter implements BooleanInferenceParameter {

  //// Constructors
  /** Creates a new instance of DefaultBooleanInferenceParameter. */
  public DefaultBooleanInferenceParameter(Map propertyMap) {
    super(propertyMap);
  }

  //// Public Area
  public boolean isValidValue(Object potentialValue) {
    if (isAlternateValue(potentialValue)) {
      return true;
    } else if (potentialValue instanceof Boolean) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public Object canonicalizeValue(final Object value) {
    if (!(value instanceof Boolean)) {
      if (value == null) {
        throw new RuntimeException("Got invalid boolean value " + value);
      } else if (value.toString().equals(CycObjectFactory.nil.toString())) {
        return Boolean.FALSE;
      } else if (value.toString().equals(CycObjectFactory.t.toString())) {
        return Boolean.TRUE;
      } else {
        throw new RuntimeException("Got invalid boolean value " + value);
      }
    } else {
      return super.canonicalizeValue(value);
    }
  }

  //// Protected Area
  //// Private Area
  //// Internal Rep
  //// Main
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
  }
}
