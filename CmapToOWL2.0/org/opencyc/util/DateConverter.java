/* $Id: DateConverter.java 126640 2008-12-04 13:39:36Z builder $
 *
 * Copyright (c) 2007 Cycorp, Inc.  (Copyright is assigned to the United States Government under DFARS 252.227-7020).
 */

package org.opencyc.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.opencyc.cycobject.CycConstant;
import org.opencyc.cycobject.CycList;
import org.opencyc.cycobject.Guid;

//// External Imports

/**
 * <P>DateConverter is designed to convert java-style dates to their corresponding
 * CycL representations. It may be extended to also convert the other direction,
 * from CycL dates to java dates.
 *
 * <P>Copyright (c) 2007 Cycorp, Inc.  (Copyright is assigned to the United States Government under DFARS 252.227-7020).
 *
 *
 * @author baxter
 * @date January 15, 2008, 5:33 PM
 * @version $Id: DateConverter.java 126640 2008-12-04 13:39:36Z builder $
 */
public class DateConverter {
  
  
  //// Constructors
  
  /** Creates a new instance of DateConverter. */
  private DateConverter() {
    SHARED_INSTANCE = this;;
  }
  
  //// Public Area
  /** Returns an instance of <code>DateConverter</code>.
   *
   * If an instance has already been created, the existing one will be returned.
   * Otherwise, a new one will be created.
   */
  public static DateConverter getInstance() {
    DateConverter dateConverter = SHARED_INSTANCE;
    if (dateConverter == null) {
      dateConverter = new DateConverter();
    }
    return dateConverter;
  }
  
  /** Try to parse <code>cycList</code> into a java <code>Date</code>
   *
   * If the parse fails, prints a stack trace iff <code>shouldReportFailure</code>
   * is non-null, and returns null.
   */
  static public Date parseCycDate(final CycList cycList, final boolean shouldReportFailure) {
    final Calendar calendar = Calendar.getInstance();
    calendar.clear();
    try {
      getInstance().updateCalendar(cycList, calendar);
    } catch (DateConverter.ParseException ex) {
      if (shouldReportFailure) {
        ex.printStackTrace();
      }
      return null;
    }
    return calendar.getTime();
  }
  
  /** Try to parse <code>cycList</code> into a java <code>Date</code>
   *
   * Prints stack trace and returns null if the parse fails.
   */
  @SuppressWarnings("static-access")
static public Date parseCycDate(final CycList cycList) {
    return getInstance().parseCycDate(cycList, true);
  }
  
  /** @return the precision of <tt>cycDate</tt> as a Calendar constant int. */
  public int getCycDatePrecision(CycList cycDate) {
    final Object fn = cycDate.first();
    if (YEAR_FN.equals(fn)) return Calendar.YEAR;
    if (MONTH_FN.equals(fn)) return Calendar.MONTH;
    if (DAY_FN.equals(fn)) return Calendar.DAY_OF_MONTH;
    if (HOUR_FN.equals(fn)) return Calendar.HOUR_OF_DAY;
    if (MINUTE_FN.equals(fn)) return Calendar.MINUTE;
    if (SECOND_FN.equals(fn)) return Calendar.SECOND;
    if (MILLISECOND_FN.equals(fn)) return Calendar.MILLISECOND;
    return -1;
  }
  
  /** Convert the date in <code>date</code> to a CycL date term.
   *
   * @param granularity Indicates the desired granularity of the CycL term. Should
   *        be an <code>int</code> constant from the <code>Calendar</code> class,
   *        e.g. <code>Calendar.YEAR</code> or <code>Calendar.DAY_OF_MONTH</code>.
   **/
  public CycList toCycDate(final Date date, final int granularity) {
    final Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return toCycDate(calendar, granularity);
  }
  
  /** Convert the date in <code>calendar</code> to a CycL date term.
   *
   * @param granularity Indicates the desired granularity of the CycL term. Should
   *        be an <code>int</code> constant from the <code>Calendar</code> class,
   *        e.g. <code>Calendar.YEAR</code> or <code>Calendar.DAY_OF_MONTH</code>.
   **/
  @SuppressWarnings("unchecked")
public CycList toCycDate(final Calendar calendar, final int granularity) {
    CycList cycLForDate = new CycList(YEAR_FN);
    cycLForDate.appendElement(calendar.get(Calendar.YEAR));
    if (granularity > Calendar.YEAR) {
      cycLForDate = CycList.makeCycList(
        MONTH_FN, lookupMonth(calendar.get(Calendar.MONTH)), cycLForDate);
      if (granularity > Calendar.MONTH) {
        cycLForDate = CycList.makeCycList(
          DAY_FN,calendar.get(Calendar.DAY_OF_MONTH), cycLForDate);
        if (granularity > Calendar.DAY_OF_MONTH) {
          cycLForDate = CycList.makeCycList(
            HOUR_FN, calendar.get(Calendar.HOUR_OF_DAY), cycLForDate);
          if (granularity > Calendar.HOUR) {
            cycLForDate = CycList.makeCycList(
              MINUTE_FN, calendar.get(Calendar.MINUTE), cycLForDate);
            if (granularity > Calendar.MINUTE) {
              cycLForDate = CycList.makeCycList(
                SECOND_FN, calendar.get(Calendar.SECOND), cycLForDate);
              if (granularity > Calendar.SECOND) {
                cycLForDate = CycList.makeCycList(
                  MILLISECOND_FN, calendar.get(Calendar.MILLISECOND), cycLForDate);
              }
            }
          }
        }
      }
    }
    return cycLForDate;
  }
  
  //// Protected Area
  
  //// Private Area
  
  //Set the time on <code>calendar</code> based on the CycL date <code>cycList</code>
  private void updateCalendar(final CycList cycList, final Calendar calendar) throws ParseException {
    if (cycList.size() == 2 && YEAR_FN.equals(cycList.get(0))) {
      final Object yearNum = new Integer(cycList.get(1).toString());
      if (!(yearNum instanceof Integer) ) {
        throw new ParseException(yearNum + " is not a valid year number.");
      }
      calendar.set(Calendar.YEAR, ((Integer)yearNum));
    } else if (cycList.size() == 3 && MONTH_FN.equals(cycList.get(0))) {
      final Object cycMonth = cycList.get(1);
      if (!(cycMonth instanceof CycConstant)) {
        throw new ParseException(cycMonth + " is not a valid CycL month.");
      }
      final int monthNum = lookupMonthNum((CycConstant)cycList.get(1));
      if (monthNum < 0 || monthNum > 11) {
        throw new ParseException(cycMonth + " is not a valid CycL month.");
      }
      final Object yearExpr = cycList.get(2);
      if (!(yearExpr instanceof CycList)) {
        throw new ParseException(yearExpr + " is not a valid CycL year.");
      }
      updateCalendar((CycList)yearExpr, calendar);
      calendar.set(Calendar.MONTH, monthNum);
    } else if (cycList.size() == 3 && DAY_FN.equals(cycList.get(0))) {
      final Object dayNum = new Integer(cycList.get(1).toString());
      if (!(dayNum instanceof Integer && (Integer)dayNum > 0 && (Integer)dayNum < 32)) {
        throw new ParseException(dayNum + " is not a valid day number.");
      }
      final Object monthExpr = cycList.get(2);
      if (!(monthExpr instanceof CycList)) {
        throw new ParseException(monthExpr + " is not a valid CycL month.");
      }
      updateCalendar((CycList)monthExpr, calendar);
      calendar.set(Calendar.DAY_OF_MONTH, (Integer)dayNum);
    } else if (cycList.size() == 3 && HOUR_FN.equals(cycList.get(0))) {
      final Object hourNum = new Integer(cycList.get(1).toString());
      if (!(hourNum instanceof Integer && (Integer)hourNum >= 0 && (Integer)hourNum < 24)) {
        throw new ParseException(hourNum + " is not a valid hour number.");
      }
      final Object dayExpr = cycList.get(2);
      if (!(dayExpr instanceof CycList)) {
        throw new ParseException(dayExpr + " is not a valid CycL day.");
      }
      updateCalendar((CycList)dayExpr, calendar);
      calendar.set(Calendar.HOUR_OF_DAY, (Integer)hourNum);
    } else if (cycList.size() == 3 && MINUTE_FN.equals(cycList.get(0))) {
      final Object minuteNum = new Integer(cycList.get(1).toString());
      if (!(minuteNum instanceof Integer && (Integer)minuteNum >= 0 && (Integer)minuteNum < 60)) {
        throw new ParseException(minuteNum + " is not a valid minute number.");
      }
      final Object hourExpr = cycList.get(2);
      if (!(hourExpr instanceof CycList)) {
        throw new ParseException(hourExpr + " is not a valid CycL hour.");
      }
      updateCalendar((CycList)hourExpr, calendar);
      calendar.set(Calendar.MINUTE, (Integer)minuteNum);
    } else if (cycList.size() == 3 && SECOND_FN.equals(cycList.get(0))) {
      final Object secondNum = new Integer(cycList.get(1).toString());
      if (!(secondNum instanceof Integer && (Integer)secondNum >= 0 && (Integer)secondNum < 60)) {
        throw new ParseException(secondNum + " is not a valid second number.");
      }
      final Object minuteExpr = cycList.get(2);
      if (!(minuteExpr instanceof CycList)) {
        throw new ParseException(minuteExpr + " is not a valid CycL minute.");
      }
      updateCalendar((CycList)minuteExpr, calendar);
      calendar.set(Calendar.SECOND, (Integer)secondNum);
    } else if (cycList.size() == 3 && MILLISECOND_FN.equals(cycList.get(0))) {
      final Object millisecondNum = new Integer(cycList.get(1).toString());
      if (!(millisecondNum instanceof Integer && (Integer)millisecondNum >= 0 && (Integer)millisecondNum < 1000)) {
        throw new ParseException(millisecondNum + " is not a valid millisecond number.");
      }
      final Object secondExpr = cycList.get(2);
      if (!(secondExpr instanceof CycList)) {
        throw new ParseException(secondExpr + " is not a valid CycL second.");
      }
      updateCalendar((CycList)secondExpr, calendar);
      calendar.set(Calendar.MILLISECOND, (Integer)millisecondNum);
    } else {
      throw new ParseException("Can't parse " + cycList);
    }
  }
  
  @SuppressWarnings("serial")
private class ParseException extends Exception {
    private ParseException(final String message) {
      super(message);
    }
  }
  
  private CycConstant lookupMonth(final int month) {
    ensureMonthMapInitialized();
    return MONTH_NUMBER_TO_CYC_MONTH_TERM_MAP.get(month);
  }
  
  private int lookupMonthNum(CycConstant cycMonth) {
    ensureMonthMapInitialized();
    for (Iterator<java.util.Map.Entry<Integer,CycConstant>> i =
      MONTH_NUMBER_TO_CYC_MONTH_TERM_MAP.entrySet().iterator(); i.hasNext();) {
      java.util.Map.Entry<Integer,CycConstant> entry = i.next();
      if (cycMonth.equals(entry.getValue())) {
        return entry.getKey();
      }
    }
    return -1;
  }
  
  private static void ensureMonthMapInitialized() {
    if (MONTH_NUMBER_TO_CYC_MONTH_TERM_MAP == null) {
      initializeMonthCycTermHash();
    }
  }
  
  private static void initializeMonthCycTermHash() {
    MONTH_NUMBER_TO_CYC_MONTH_TERM_MAP = new HashMap<Integer,CycConstant>();
    MONTH_NUMBER_TO_CYC_MONTH_TERM_MAP.put(Calendar.JANUARY, JANUARY);
    MONTH_NUMBER_TO_CYC_MONTH_TERM_MAP.put(Calendar.FEBRUARY, FEBRUARY);
    MONTH_NUMBER_TO_CYC_MONTH_TERM_MAP.put(Calendar.MARCH, MARCH);
    MONTH_NUMBER_TO_CYC_MONTH_TERM_MAP.put(Calendar.APRIL, APRIL);
    MONTH_NUMBER_TO_CYC_MONTH_TERM_MAP.put(Calendar.MAY, MAY);
    MONTH_NUMBER_TO_CYC_MONTH_TERM_MAP.put(Calendar.JUNE, JUNE);
    MONTH_NUMBER_TO_CYC_MONTH_TERM_MAP.put(Calendar.JULY, JULY);
    MONTH_NUMBER_TO_CYC_MONTH_TERM_MAP.put(Calendar.AUGUST, AUGUST);
    MONTH_NUMBER_TO_CYC_MONTH_TERM_MAP.put(Calendar.SEPTEMBER, SEPTEMBER);
    MONTH_NUMBER_TO_CYC_MONTH_TERM_MAP.put(Calendar.OCTOBER, OCTOBER);
    MONTH_NUMBER_TO_CYC_MONTH_TERM_MAP.put(Calendar.NOVEMBER, NOVEMBER);
    MONTH_NUMBER_TO_CYC_MONTH_TERM_MAP.put(Calendar.DECEMBER, DECEMBER);
  }
  
  public static boolean isCycDate(Object object) {
    return (object instanceof CycList) && parseCycDate((CycList)object, false) != null;
  }
  
  //// Internal Rep
  
  public static final CycConstant YEAR_FN = new CycConstant("YearFn",
    new Guid("bd58f29a-9c29-11b1-9dad-c379636f7270"));
  public static final CycConstant MONTH_FN = new CycConstant("MonthFn",
    new Guid("be00fd8d-9c29-11b1-9dad-c379636f7270"));
  public static final CycConstant DAY_FN = new CycConstant("DayFn",
    new Guid("be00ff5b-9c29-11b1-9dad-c379636f7270"));
  public static final CycConstant HOUR_FN = new CycConstant("HourFn",
    new Guid("be010082-9c29-11b1-9dad-c379636f7270"));
  public static final CycConstant MINUTE_FN = new CycConstant("MinuteFn",
    new Guid("be0100d7-9c29-11b1-9dad-c379636f7270"));
  public static final CycConstant SECOND_FN = new CycConstant("SecondFn",
    new Guid("be01010a-9c29-11b1-9dad-c379636f7270"));
  public static final CycConstant MILLISECOND_FN = new CycConstant("MillisecondFn",
    new Guid("8c3206d3-1616-11d8-99b1-0002b361bcfc"));
  
  public static final CycConstant JANUARY = new CycConstant("January",
    new Guid("bd58b833-9c29-11b1-9dad-c379636f7270"));
  public static final CycConstant FEBRUARY = new CycConstant("February",
    new Guid("bd58c2f7-9c29-11b1-9dad-c379636f7270"));
  public static final CycConstant MARCH = new CycConstant("March",
    new Guid("bd58c2bd-9c29-11b1-9dad-c379636f7270"));
  public static final CycConstant APRIL = new CycConstant("April",
    new Guid("bd58c279-9c29-11b1-9dad-c379636f7270"));
  public static final CycConstant MAY = new CycConstant("May",
    new Guid("bd58c232-9c29-11b1-9dad-c379636f7270"));
  public static final CycConstant JUNE = new CycConstant("June",
    new Guid("bd58c1f0-9c29-11b1-9dad-c379636f7270"));
  public static final CycConstant JULY = new CycConstant("July",
    new Guid("bd58c1ad-9c29-11b1-9dad-c379636f7270"));
  public static final CycConstant AUGUST = new CycConstant("August",
    new Guid("bd58c170-9c29-11b1-9dad-c379636f7270"));
  public static final CycConstant SEPTEMBER = new CycConstant("September",
    new Guid("bd58c131-9c29-11b1-9dad-c379636f7270"));
  public static final CycConstant OCTOBER = new CycConstant("October",
    new Guid("bd58c0ef-9c29-11b1-9dad-c379636f7270"));
  public static final CycConstant NOVEMBER = new CycConstant("November",
    new Guid("bd58c0a5-9c29-11b1-9dad-c379636f7270"));
  public static final CycConstant DECEMBER = new CycConstant("December",
    new Guid("bd58b8ba-9c29-11b1-9dad-c379636f7270"));
  
  private static Map<Integer,CycConstant> MONTH_NUMBER_TO_CYC_MONTH_TERM_MAP = null;
  
  private static DateConverter SHARED_INSTANCE = null;
  
  //// Main
  
  
  
  
}
