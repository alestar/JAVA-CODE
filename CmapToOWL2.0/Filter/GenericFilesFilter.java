package Filter;

import java.io.File;

import javax.swing.filechooser.FileFilter;



public class GenericFilesFilter extends FileFilter {
	
	final static String XML = "xml";
	final static String OWL = "owl";
	final static String CLX = "cxl";
	  boolean isXML=false;
	  boolean isOWL=false;
	  boolean isCXL=false;
	  boolean useXML=false;
	  boolean useOWL=false;
	  boolean useCXL=false;
    // Accept all directories and all xml files.
    public boolean accept(File f) {

        if (f.isDirectory()) {
            return true;
        }

        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
        	String extension = s.substring(i+1).toLowerCase();
        	if (XML.equals(extension)&&useXML){
        		isXML=true;
        		return isXML;
        	}
        	else if (CLX.equals(extension)&&useCXL) {
        		isCXL=true;
        		return isCXL;
        	}
        	else if (OWL.equals(extension)&&useOWL) {
    			isOWL=true;
    			return isOWL;
    		}
        }
        return false;
    }

    // The description of this filter
    public String getDescription() {//... al parecer este metodo solo permite escoger un tipo de filtro...
    	String description=null;
    	
    	description="";
    	if(isXML || isCXL)
    		description="XML, CXL Files ";
    	if (isOWL) 
    		description= "OWL Files ";
    	return description;
    }

	public boolean isCXL() {
		return isCXL;
	}

	public void setCXL(boolean isCXL) {
		this.isCXL = isCXL;
	}

	public boolean isOWL() {
		return isOWL;
	}

	public void setOWL(boolean isOWL) {
		this.isOWL = isOWL;
	}

	public boolean isXML() {
		return isXML;
	}

	public void setXML(boolean isXML) {
		this.isXML = isXML;
	}

	public boolean isUseCXL() {
		return useCXL;
	}

	public void setUseCXL(boolean useCXL) {
		this.useCXL = useCXL;
	}

	public boolean isUseOWL() {
		return useOWL;
	}

	public void setUseOWL(boolean useOWL) {
		this.useOWL = useOWL;
	}

	public boolean isUseXML() {
		return useXML;
	}

	public void setUseXML(boolean useXML) {
		this.useXML = useXML;
	}


}
