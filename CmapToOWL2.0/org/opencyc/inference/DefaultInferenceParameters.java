/* $Id: DefaultInferenceParameters.java 128067 2009-06-19 17:55:19Z baxter $
 *
 * Copyright (c) 2004 - 2006 Cycorp, Inc.  All rights reserved.
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 */
package org.opencyc.inference;

//// Internal Imports
import org.opencyc.api.*;
import org.opencyc.cycobject.*;

//// External Imports
import java.util.*;

/**
 * <P>DefaultInferenceParameters is designed to...
 *
 * <P>Copyright (c) 2004 - 2006 Cycorp, Inc.  All rights reserved.
 * <BR>This software is the proprietary information of Cycorp, Inc.
 * <P>Use is subject to license terms.
 *
 * @author zelal
 * @date August 14, 2005, 2:46 PM
 * @version $Id: DefaultInferenceParameters.java 128067 2009-06-19 17:55:19Z baxter $
 */
@SuppressWarnings("serial")
public class DefaultInferenceParameters extends HashMap<CycSymbol, Object>
        implements InferenceParameters {

  //// Constructors
  /** Creates a new instance of DefaultInferenceParameters. */
  public DefaultInferenceParameters(CycAccess cycAccess) {
    this.cycAccess = cycAccess;
  }

  /** Creates a new instance of DefaultInferenceParameters. */
  public DefaultInferenceParameters(CycAccess cycAccess, boolean shouldReturnAnswersInHL) {
    this.cycAccess = cycAccess;
    if (shouldReturnAnswersInHL) {
      getAnswersInHL();
    } else {
      getAnswersInEL();
    }
  }

  //// Public Area
  public CycAccess getCycAccess() {
    return cycAccess;
  }

  public void getAnswersInHL() {
    put(ANSWER_LANGUAGE, HL);
  }

  public void getAnswersInEL() {
    put(ANSWER_LANGUAGE, EL);
  }

  public Integer getMaxNumber() {
    final Object rawValue = get(MAX_NUMBER);
    if (rawValue instanceof Integer) {
      return (Integer) rawValue;
    } else {
      return null;
    }
  }

  public void setMaxNumber(final Integer max) {
    put(MAX_NUMBER, max);
  }

  public Integer getMaxTime() {
    final Object rawValue = get(MAX_TIME);
    if (rawValue instanceof Integer) {
      return (Integer) rawValue;
    } else {
      return null;
    }
  }

  public void setBrowsable(boolean b) {
    put(CycObjectFactory.makeCycSymbol(":browsable?"), CycObjectFactory.t);
  }

  @Override
  public Object put(CycSymbol parameterName, Object value) {
    // @Hack the following  if statements are a hack and should be removed as soon as support
    // for the following inference parameters are supported... --Tony
    // @Hack 2 if the value is :UNKNOWN the parameter will not be set and it is assumed that 
    // Cyc uses its own default for that particular parameter
    if (value instanceof CycSymbol && ((CycSymbol) value).toString().equals(":UNKNOWN")) {
      return null;
    }
    if (":ALLOWED-RULES".equals(parameterName.toString())) {
      return null;
    }
    if (":ALLOWED-MODULES".equals(parameterName.toString())) {
      return null;
    }
    if (":FORBIDDEN-RULES".equals(parameterName.toString())) {
      return null;
    }
    if (":NON-EXPLANATORY-SENTENCE".equals(parameterName.toString())) {
      return null;
    }
    if (":PROBLEM-STORE".equals(parameterName.toString())) {
      if (value instanceof CycList) {
        return super.put(parameterName, value);
      } else if (CycObjectFactory.nil.equals(value)) {
        return super.put(parameterName, value);
      } else {
        throw new RuntimeException("Got invalid value " + value + " for parameter " + parameterName);
      }
    }
    InferenceParameterDescriptions descriptions =
            (InferenceParameterDescriptions) DefaultInferenceParameterDescriptions.getDefaultInferenceParameterDescriptions(cycAccess);
    if (descriptions == null) {
      throw new RuntimeException("Cannot find inference parameter descriptions");
    }
    InferenceParameter param = (InferenceParameter) descriptions.get(parameterName);
    if (param == null) {
      throw new RuntimeException("No parameter found by name " + parameterName);
    }
    value = param.canonicalizeValue(value);
    if (!param.isValidValue(value)) {
      throw new RuntimeException("Got invalid value " + value + " for parameter " + parameterName);
    }
    return super.put(parameterName, value);
  }

  public String stringApiValue() {
    if (size() <= 0) {
      return CycObjectFactory.nil.stringApiValue();
    }
    StringBuffer buf = new StringBuffer("(LIST ");
    for (Iterator iter = keySet().iterator(); iter.hasNext();) {
      Object key = iter.next();
      buf.append(DefaultCycObject.stringApiValue(key));
      buf.append(" ");
      final Object val = get(key);
      buf.append(parameterValueStringApiValue(key, val));
      if (iter.hasNext()) {
        buf.append(" ");
      }
    }
    buf.append(")");
    return buf.toString();
  }

  /* @return the string API value for val qua value for key. */
  public static String parameterValueStringApiValue(final Object key, final Object val) {
    final Object cycListApiValue = parameterValueCycListApiValue(key, val);
    if (isProblemStoreSpecification(key, cycListApiValue)) {
      return problemStoreStringApiValue((List) cycListApiValue);
    }
    if (cycListApiValue instanceof CycObject) {
      return ((CycObject) cycListApiValue).stringApiValue();
    } else {
      return (DefaultCycObject.stringApiValue(cycListApiValue));
    }
  }

  /* @return the CycList API value for val qua value for key. */
  public static Object parameterValueCycListApiValue(final Object key, final Object val) {
    if (val instanceof Boolean) {
      if (((Boolean) val).booleanValue()) {
        return (CycObjectFactory.t);
      } else {
        return (CycObjectFactory.nil);
      }
    } else if (isProblemStoreSpecification(key, val)) {
      return val;
    } else if (isInfiniteValue(val)) {
      return (CycObjectFactory.nil);
    } else {
      return (val);
    }
  }

  private static boolean isProblemStoreSpecification(final Object key, final Object val) {
    return (":PROBLEM-STORE".equals(key.toString())) && (val instanceof List);
  }

  //@hack -- Only way to pass a problem store is to pass a form that evaluates to one:
  private static String problemStoreStringApiValue(final List val) {
    final StringBuffer buf = new StringBuffer("(");
    for (Iterator i = ((List) val).iterator(); i.hasNext();) {
      final Object item = i.next();
      if (item instanceof String) {
        buf.append(DefaultCycObject.stringApiValue(item));
      } else {
        buf.append(item);
      }
      buf.append(" ");
    }
    buf.append(")");
    return buf.toString();
  }

  public Object clone() {
    DefaultInferenceParameters copy = new DefaultInferenceParameters(cycAccess);
    Iterator<CycSymbol> iterator = this.keySet().iterator();
    while (iterator.hasNext()) {
      CycSymbol key = iterator.next();
      Object value = this.get(key); // note: this might should be cloned
      copy.put(key, value);
    }
    return copy;
  }

  public static Object getInfiniteValue() {
    return null;
  }

  public static boolean isInfiniteValue(final Object value) {
    return null == value;
  }
  //// Protected Area
  //// Private Area
  //// Internal Rep
  private CycAccess cycAccess;
  final static CycSymbol ANSWER_LANGUAGE = new CycSymbol(":ANSWER-LANGUAGE");
  final static CycSymbol HL = new CycSymbol(":HL");
  final static CycSymbol EL = new CycSymbol(":EL");

  //// Main
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    try {
      System.out.println("Starting...");
      CycAccess cycAccess = new CycAccess("localhost", 3600);
      InferenceParameters parameters = new DefaultInferenceParameters(cycAccess);
      parameters.put(new CycSymbol(":MAX-NUMBER"), new Integer(10));
      parameters.put(new CycSymbol(":PROBABLY-APPROXIMATELY-DONE"), new Double(0.5));
      parameters.put(new CycSymbol(":ABDUCTION-ALLOWED?"), Boolean.TRUE);
      parameters.put(new CycSymbol(":EQUALITY-REASONING-METHOD"), new CycSymbol(":CZER-EQUAL"));
      try {
        parameters.put(new CycSymbol(":MAX-NUMBER"), new CycSymbol(":BINDINGS"));
        System.out.println("Failed to catch exception.");
      } catch (Exception e) {
      } // ignore
      try {
        parameters.put(new CycSymbol(":PROBABLY-APPROXIMATELY-DONE"), new CycSymbol(":BINDINGS"));
        System.out.println("Failed to catch exception.");
      } catch (Exception e) {
      } // ignore
      try {
        parameters.put(new CycSymbol(":ABDUCTION-ALLOWED?"), new CycSymbol(":BINDINGS"));
        System.out.println("Failed to catch exception.");
      } catch (Exception e) {
      } // ignore
      try {
        parameters.put(new CycSymbol(":EQUALITY-REASONING-METHOD"), new Double(0.5));
        System.out.println("Failed to catch exception.");
      } catch (Exception e) {
      } // ignore
      System.out.println("PARAMETERS: " + parameters.stringApiValue());
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      System.out.println("Exiting...");
      System.exit(0);
    }
  }
}
