/* $Id: InferenceWorkerSuspendReason.java 127814 2009-05-13 19:38:51Z baxter $
 *
 * Copyright (c) 2004 - 2006 Cycorp, Inc.  All rights reserved.
 * This software is the proprietary information of Cycorp, Inc.
 * Use is subject to license terms.
 */

package org.opencyc.inference;

//// Internal Imports
import org.opencyc.api.CycObjectFactory;
import org.opencyc.cycobject.CycSymbol;

//// External Imports

/**
 * <P>InferenceWorkerSuspendReason is designed to...
 *
 * <P>Copyright (c) 2004 - 2006 Cycorp, Inc.  All rights reserved.
 * <BR>This software is the proprietary information of Cycorp, Inc.
 * <P>Use is subject to license terms.
 *
 * @author bklimt
 * @date October 31, 2005, 10:29 AM
 * @version $Id: InferenceWorkerSuspendReason.java 127814 2009-05-13 19:38:51Z baxter $
 */
public final class InferenceWorkerSuspendReason {
  
  //// Constructors
  
  /** Creates a new instance of InferenceWorkerSuspendReason. */
  private InferenceWorkerSuspendReason(String description) {
    this.description = description;
  }
  
  //// Public Area
  
  public static final InferenceWorkerSuspendReason EXHAUST =
    new InferenceWorkerSuspendReason("Exhausted");
  
  public static final InferenceWorkerSuspendReason MAX_DEPTH =
    new InferenceWorkerSuspendReason("Max depth reached");

  public static final InferenceWorkerSuspendReason MAX_TIME =
    new InferenceWorkerSuspendReason("Max time reached");

  public static final InferenceWorkerSuspendReason MAX_PROBLEM_COUNT =
    new InferenceWorkerSuspendReason("Max problem count reached");

  public static final InferenceWorkerSuspendReason MAX_PROOF_COUNT =
    new InferenceWorkerSuspendReason("Max proof count reached");
  
  public static final InferenceWorkerSuspendReason MAX_NUMBER =
    new InferenceWorkerSuspendReason("Max results reached");
  
  public static final InferenceWorkerSuspendReason MAX_STEP =
    new InferenceWorkerSuspendReason("Max steps performed");

  public static final InferenceWorkerSuspendReason PROBABLY_APPROXIMATELY_DONE =
    new InferenceWorkerSuspendReason("Probably approximately done");
  
  public static final InferenceWorkerSuspendReason ABORTED =
    new InferenceWorkerSuspendReason("Aborted");
  
  public static final InferenceWorkerSuspendReason INTERRUPT =
    new InferenceWorkerSuspendReason("Interrupted");
  
  public static final InferenceWorkerSuspendReason UNKNOWN =
    new InferenceWorkerSuspendReason("Unknown");
  
  public static final CycSymbol ERROR_SYMBOL = CycObjectFactory.makeCycSymbol(":ERROR");
  private static final CycSymbol EXHAUST_SYMBOL = CycObjectFactory.makeCycSymbol(":EXHAUST");
  private static final CycSymbol EXHAUST_TOTAL_SYMBOL = CycObjectFactory.makeCycSymbol(":EXHAUST-TOTAL");
  private static final CycSymbol MAX_DEPTH_SYMBOL = CycObjectFactory.makeCycSymbol(":LOOK-NO-DEEPER-FOR-ADDITIONAL-ANSWERS");
  private static final CycSymbol MAX_TIME_SYMBOL = CycObjectFactory.makeCycSymbol(":MAX-TIME");
  private static final CycSymbol MAX_PROOF_COUNT_SYMBOL = CycObjectFactory.makeCycSymbol(":MAX-PROOF-COUNT");
  private static final CycSymbol MAX_PROBLEM_COUNT_SYMBOL = CycObjectFactory.makeCycSymbol(":MAX-PROBLEM-COUNT");
  private static final CycSymbol MAX_STEP_SYMBOL = CycObjectFactory.makeCycSymbol(":MAX-STEP");
  private static final CycSymbol MAX_NUMBER_SYMBOL = CycObjectFactory.makeCycSymbol(":MAX-NUMBER");
  private static final CycSymbol INTERRUPT_SYMBOL = CycObjectFactory.makeCycSymbol(":INTERRUPT");
  private static final CycSymbol PROBABLY_APPROXIMATELY_DONE_SYMBOL = CycObjectFactory.makeCycSymbol(":PROBABLY-APPROXIMATELY-DONE");
  private static final CycSymbol ABORT_SYMBOL = CycObjectFactory.makeCycSymbol(":ABORT");
  private static final CycSymbol NIL_SYMBOL = CycObjectFactory.nil;

  /** Create a new error reason. */
  public static InferenceWorkerSuspendReason createFromErrorString(final String errorString) {
   final InferenceWorkerSuspendReason reason = new InferenceWorkerSuspendReason("Error: " + errorString);
   reason.setErrorFlag(true);
   return reason;
  }

  /** Was inference suspended because of an error? */
  public boolean isError() {
    return isError;
  }

  public static InferenceWorkerSuspendReason createFromCycSymbol(CycSymbol symbol) {
    if (symbol.equals(EXHAUST_SYMBOL) || symbol.equals(EXHAUST_TOTAL_SYMBOL)) {
      return EXHAUST;
    } else if (symbol.equals(MAX_DEPTH_SYMBOL)) {
      return MAX_DEPTH;
    } else if (symbol.equals(MAX_TIME_SYMBOL)) {
      return MAX_TIME;
    } else if (symbol.equals(MAX_PROOF_COUNT_SYMBOL)) {
      return MAX_PROOF_COUNT;
    } else if (symbol.equals(MAX_PROBLEM_COUNT_SYMBOL)) {
      return MAX_PROBLEM_COUNT;
    } else if (symbol.equals(MAX_STEP_SYMBOL)) {
      return MAX_STEP;
    } else if (symbol.equals(MAX_NUMBER_SYMBOL)) {
      return MAX_NUMBER;
    } else if (symbol.equals(INTERRUPT_SYMBOL)) {
      return INTERRUPT;
    } else if (symbol.equals(PROBABLY_APPROXIMATELY_DONE_SYMBOL)) {
      return PROBABLY_APPROXIMATELY_DONE;
    } else if (symbol.equals(ABORT_SYMBOL)) {
      return ABORTED;
    } else if (symbol.equals(NIL_SYMBOL) || symbol == null) {
      return UNKNOWN;
    } else {
      throw new IllegalArgumentException("Unable to create InferenceWorkerSuspendReason from "+symbol);
    }
  }
  
  public String toString() {
    return description;
  }
  
  //// Protected Area
  
  //// Private Area
  private String description;

  private void setErrorFlag(boolean b) {
    isError = b;
  }
  
  //// Internal Rep
  private boolean isError = false; // true iff inference suspended because of an error
  
  //// Main
  
}
