package org.opencyc.cycobject;

import java.io.*;
import org.opencyc.xml.*;

/**
 * Provides the behavior and attributes of an OpenCyc variable, typically used
 * in rule and query expressions.
 *
 * @version $0.1$
 * @author Stephen L. Reed
 *
 * <p>Copyright 2001 Cycorp, Inc., license is open source GNU LGPL.
 * <p><a href="http://www.opencyc.org/license.txt">the license</a>
 * <p><a href="http://www.opencyc.org">www.opencyc.org</a>
 * <p><a href="http://www.sourceforge.net/projects/opencyc">OpenCyc at SourceForge</a>
 * <p>
 * THIS SOFTWARE AND KNOWLEDGE BASE CONTENT ARE PROVIDED ``AS IS'' AND
 * ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE OPENCYC
 * ORGANIZATION OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE AND KNOWLEDGE
 * BASE CONTENT, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
public class CycVariable extends DefaultCycObject implements Comparable, Serializable {
  
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

/**
   * The name of the XML tag for this object.
   */
  public static final String cycVariableXMLTag = "variable";
  
  /** the HL variable id */
  public Integer hlVariableId = null;
  
  /**
   * The variable represented as a <tt>String</tt>.
   */
  public final String name;
  
  /**
   * Whether this variable is a meta variable.
   */
  public boolean isMetaVariable = false;
  
  /**
   * Constructs a new <tt>CycVariable</tt> object.
   *
   * @param name the <tt>String</tt> name of the <tt>CycVariable</tt>.
   * @param hlVariableId the HL variable id
   */
  public CycVariable(final String name, final Integer hlVariableId) {
    this(name);
    if (hlVariableId == null)
      throw new IllegalArgumentException("id must not be null");
    
    this.hlVariableId = hlVariableId;
  }
  
  /**
   * Constructs a new <tt>CycVariable</tt> object.
   *
   * @param name the <tt>String</tt> name of the <tt>CycVariable</tt>.
   */
  public CycVariable(String name) {
    if (name == null)
      throw new IllegalArgumentException("name must not be null");
    
    String myName = name;
    if (name.startsWith(":")) {
      this.isMetaVariable = true;
      myName = name.substring(1);
    } else if (name.startsWith("?")) {
      myName = name.substring(1);
    } 
    myName = myName.toUpperCase();
    myName = myName.replace(' ','-');
    if (!isValidName(myName)) {
      throw new IllegalArgumentException("Invalid name");
    }
    this.name = myName;
  }
  
  /**
   * Returns whether this is a meta variable.
   *
   * @return whether this is a meta variable
   */
  public boolean isMetaVariable() {
    return isMetaVariable;
  }
  
  /**
   * Returns whether this is an HL variable.
   *
   * @return whether this is an HL variable
   */
  public boolean isHLVariable() {
    return hlVariableId != null;
  }
  
  /**
   * Returns the string representation of the <tt>CycVariable</tt>
   *
   * @return the representation of the <tt>CycVariable</tt> as a <tt>String</tt>
   */
  public String toString() {
    return cyclify();
  }
  
  public boolean isDontCareVariable() {
    return name.startsWith("?");
  }
  
  /**
   * Returns the OpenCyc representation of the <tt>CycVariable</tt>
   *
   * @return the OpenCyc representation of the <tt>CycVariable</tt> as a
   * <tt>String</tt> prefixed by "?"
   */
  public String cyclify() {
    if (isMetaVariable) {
      return ":" + name;
    }
    else {
      return "?" + name;
    }
  }
  
  /**
   * Returns this object in a form suitable for use as an <tt>String</tt> api expression value.
   *
   * @return this object in a form suitable for use as an <tt>String</tt> api expression value
   */
  public String stringApiValue() {
    return "'" + cyclifyWithEscapeChars();
  }
  
  /**
   * Returns this object in a form suitable for use as an <tt>CycList</tt> api expression value.
   *
   * @return this object in a form suitable for use as an <tt>CycList</tt> api expression value
   */
  public Object cycListApiValue() {
    return this;
  }
  
  /**
   * Returns <tt>true</tt> some object equals this <tt>CycVariable</tt>
   *
   * @param object the <tt>Object</tt> for equality comparison
   * @return equals <tt>boolean</tt> value indicating equality or non-equality.
   */
  public boolean equals(Object object) {
    if (! (object instanceof CycVariable))
      return false;
    CycVariable var = (CycVariable)object;
    return (isMetaVariable() == var.isMetaVariable()) &&
    var.name.equals(name);
  }
  
  /**
   * Provides the hash code appropriate for this object.
   *
   * @return the hash code appropriate for this object
   */
  public int hashCode() {
    return name.hashCode();
  }
  
  /**
   * Compares this object with the specified object for order.
   * Returns a negative integer, zero, or a positive integer as this
   * object is less than, equal to, or greater than the specified object.
   *
   * @param object the reference object with which to compare.
   * @return a negative integer, zero, or a positive integer as this
   * object is less than, equal to, or greater than the specified object
   */
  public int compareTo(Object object) {
    if (! (object instanceof CycVariable))
      throw new ClassCastException("Must be a CycVariable object");
    return this.name.compareTo(((CycVariable) object).name);
  }
  
  /**
   * Returns the XML representation of this object.
   *
   * @return the XML representation of this object
   */
  public String toXMLString() throws IOException {
    XMLStringWriter xmlStringWriter = new XMLStringWriter();
    toXML(xmlStringWriter, 0, false);
    return xmlStringWriter.toString();
  }
  
  /**
   * Prints the XML representation of the CycVariable to an <code>XMLWriter</code>
   *
   * @param xmlWriter an <tt>XMLWriter</tt>
   * @param indent an int that specifies by how many spaces to indent
   * @param relative a boolean; if true indentation is relative, otherwise absolute
   */
  public void toXML(XMLWriter xmlWriter, int indent, boolean relative)
  throws IOException {
    xmlWriter.printXMLStartTag(cycVariableXMLTag, indent, relative, false);
    xmlWriter.print(TextUtil.doEntityReference(name));
    xmlWriter.printXMLEndTag(cycVariableXMLTag);
  }

  /**
   * Is <code>name</code> a valid Cyc variable name?
   */
  public boolean isValidName(String name) {
    if (name.length() < 1) {
      return false;
    } else if (name.charAt(0) == '?' || name.charAt(0) == ':') {
      return isValidName(name.substring(1));
    } else {
      for (int i = 0; i < name.length(); i++) {
        final char thisChar = name.charAt(i);
        if (i == 0 && (thisChar == '?' || thisChar == ':')) {
          continue;
        }
        if (Character.isUpperCase(thisChar)) {
          continue;
        }
        if (Character.isDigit(thisChar)) {
          continue;
        }
        if (thisChar == '-' && i > 0 && name.charAt(i-1) != '-') {
          continue;
        }
        return false;
      }
    }
    return true;
  }
  
}
