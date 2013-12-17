package OpenCycAccess;


import java.io.File;
import java.io.IOException;
import java.util.Locale;

import org.opencyc.api.CycAccess;
import org.opencyc.cycobject.CycConstant;
import org.opencyc.util.Log;

import sintax.CheckSintax;
import file.FileConcepsManage;

public class OpenCycAccess {
	public String CycPath = "opencyc-2.0/scripts/";
	private CycAccess  cycAccess;
	private FileConcepsManage notConstanFile;
	private FileConcepsManage ConstanFile;
	private CheckSintax Sintax;
	
	
	public OpenCycAccess() {
		Sintax= new CheckSintax();
		notConstanFile=new FileConcepsManage("StaticsResult/OpenCyc/NotOpenCycConstant");
		ConstanFile= new FileConcepsManage("StaticsResult/OpenCyc/OpenCycConstantMatchMap");
		ConstanFile.loadConcep();
	}
	
	public OpenCycAccess(String cycPath) {
		Sintax= new CheckSintax();
		notConstanFile=new FileConcepsManage("StaticsResult/OpenCyc/NotOpenCycConstant");
		notConstanFile.loadConcep();
		ConstanFile= new FileConcepsManage("StaticsResult/OpenCyc/OpenCycConstantMatchMap");
		ConstanFile.loadConcep();
		
		this.CycPath = new File( cycPath).getAbsolutePath();
		Log.makeLog();
		Log.current.println("Initializing Cyc server connection, and caching frequently used terms.");
	try {
		/*		String path1="C:/opencyc-2.0/server/cyc/run";
			String path="C:/opencyc-2.0/scripts";
			ProcessBuilder pb = new ProcessBuilder();
			pb.directory(new File(path));
			String[] cmd1 = {path1+File.separatorChar+"bin/run-cyc-32bit.bat"};
			String[] cmd = {path+File.separatorChar+"run-cyc.bat"};
			pb.command(cmd);
			Process proc = pb.start();
			
		    pb.redirectErrorStream(true);
		    proc.waitFor();*/
	                      
			Locale.setDefault(Locale.ENGLISH); 
			cycAccess = new CycAccess();
		}
		catch (Exception e) {
			Log.current.errorPrintln(e.getMessage());
			Log.current.printStackTrace(e);
		}
		cycAccess.traceOn();
		Log.current.println("Now tracing Cyc server messages");
	}
	
	
	public CycConstant checkSintaxForConstant(String concep){
		CycConstant constant=null;
		String concepTrim=concep.trim();
		try {
			if(!notConstanFile.findConcep(concepTrim)){

					constant=cycAccess.getConstantByName(concepTrim);
					if(constant==null)
						constant=cycAccess.getConstantByName(concepTrim.replaceAll(" ", "-"));
					if(constant==null)
						constant=cycAccess.getConstantByName(concepTrim.replaceAll(" ", ""));	
					if(constant==null)
						constant=cycAccess.getConstantByName(concepTrim.replaceAll(" ", "-The"));
					if(constant==null)
						constant=cycAccess.getConstantByName(concepTrim.replaceAll(" ", "_"));
					if(constant==null){
						String concepCapital=Sintax.firstLettersToCapital(concepTrim);
						constant=cycAccess.getConstantByName(concepCapital.replaceAll(" ", "-"));
						if(constant==null)
							constant=cycAccess.getConstantByName(concepCapital.replaceAll(" ", "-The"));
						if(constant==null)
							constant=cycAccess.getConstantByName(concepCapital.replaceAll(" ", ""));
						if(constant==null)
							constant=cycAccess.getConstantByName(concepCapital.replaceAll(" ", "_"));
					}
					if(constant==null){
						notConstanFile.addConcep(concepTrim);
						notConstanFile.loadConcep();//Se recarga el fichero para k se consideren los nuevos conceptos añadidos en las futuras validaciones
					}
					else if(!ConstanFile.findConcep(concepTrim)){
						ConstanFile.addConcep(concepTrim);
						ConstanFile.loadConcep();
					}
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return constant;
	}
	
	public boolean getHierarchyRelation(String srcConcep,String dstConcep){
		boolean match=false;
		try { 
			CycConstant srcConst=checkSintaxForConstant(srcConcep);//...Se chekea la sintaxia correcta para la inferencia en OpenCyc...
			CycConstant destConst=	checkSintaxForConstant(dstConcep);//...Se chekea la sintaxia correcta para la inferencia en OpenCyc..
			
			if(srcConst!=null && destConst!=null ){	
//				...Buscar la Relacion Jerarquica Directa..o Inversa(o indirecta)...entre conceptos			
				if(cycAccess.areHierarchical(destConst, srcConst)||cycAccess.areHierarchical(srcConst,destConst))
					match=true;
			}
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return match;
	}   
	
	public boolean getInstanceRelation(String srcConcep,String dstConcep) {
		boolean match=false;
		try {
			CycConstant srcConst=checkSintaxForConstant(srcConcep);//...Se chekea la sintaxia correcta para la inferencia en OpenCyc...
			CycConstant destConst=	checkSintaxForConstant(dstConcep);//...Se chekea la sintaxia correcta para la inferencia en OpenCyc...

			if(srcConst!=null && destConst!=null ){
//				...Buscar la Relacion de Instancia Directa..o Inversa(o indirecta)...entre conceptos	
				if(cycAccess.isa(srcConst,destConst)||cycAccess.isa(destConst,srcConst))
					match=true;
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return match;
	}  
	
}
 

