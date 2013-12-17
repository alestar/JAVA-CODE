package parse;

import java.io.FileWriter;
import java.io.IOException;

public class ResultMap {

	private String mapName;
	private String path="OWLProcess/Result/";
	private int conceptCount;// cantidad de conceptos que hay en el mapa  
	private int propMapCount;// cantidad de preposiciones que hay en el mapa  
	private int cantCodificadas;// cantidad de preposiciones que han sido codificadas
	private int linkCodify;//relación inferida por f.e(es decir por la categoría de dicha frase)
	private int semanticCod;//relación inferidad por semantica(por ejemplo WN)

//	...TOTAL de relaciones obtenidas...
//	...por WN	
	private int totalRels;
	private int hypeHypoRels;
	private int meroHoloRels;
	private int meroHoloTypeRels;
//	...por OpenCyc
	private int cycHierarchyRels;
	private int cycInstanceRels;
//	...por OWLRepository		
	private double relevance;
	private int owlClassSubClassRels;
	private int owlInstanceRels;
	private int owlObjectPropertyRels;
	private int owlRestricPropertyRels;
	
//	...Relaciones obtenidas por inferencia en WN...
//	...por WN	
	private int hypeHypo;//	relID=1
	private int meroHolo;//	relID=6
	private int meroHoloType;//	relID=7
//	...por OpenCyc	
	private int cycHierarchy;//	relID=2
	private int cycInstance;//	relID=4
//	...por OWLRepository	
	private int	owlClassSubClass;//	relID=3
	private int	owlInstance;//	relID=5
	private int	owlObjectProperty;//	relID=8
	private int	owlRestricProperty;//	relID=9
	
//	....Reglas...		
	private int codRul1;
	private int codRul2;
	private int codRul3;
	private int codRul4;
	private int codRul5;
	private int codRul6;
	private int codRul7;
	private int codRul8;
	private int codRul9;
	private int codRul10;
	private int codRul11;
	
//...Constructor	
	public ResultMap(int cantConcepts, int cantProp,String mapName ) {
		this.mapName=mapName;
		this.conceptCount = cantConcepts;
		this.propMapCount = cantProp;
		this.cantCodificadas = 0;
		this.linkCodify = 0;
		this.semanticCod = 0;
		hypeHypoRels=meroHoloRels=meroHoloTypeRels=cycHierarchyRels=cycInstanceRels=owlClassSubClassRels=owlInstanceRels=owlObjectPropertyRels=owlRestricPropertyRels=0;
		hypeHypo= meroHolo= meroHoloType= cycHierarchy= cycInstance= owlClassSubClass= owlInstance= owlObjectProperty= owlRestricProperty= 0;
		codRul1 = codRul2 = codRul3 = codRul4 = codRul5 = codRul6 = codRul7 = codRul8 = codRul9 = codRul10 = codRul11 =0;
	}
//	...Sets & Gets	
	public int getCantCodificadas() {
		return cantCodificadas;
	}
	public void setCantCodificadas(int cantCodificadas) {
		this.cantCodificadas = cantCodificadas;
	}
	public int getCodRul1() {
		return codRul1;
	}
	public void setCodRul1(int codRul1) {
		this.codRul1 = codRul1;
	}
	public int getCodRul10() {
		return codRul10;
	}
	public void setCodRul10(int codRul10) {
		this.codRul10 = codRul10;
	}
	public int getCodRul11() {
		return codRul11;
	}
	public void setCodRul11(int codRul11) {
		this.codRul11 = codRul11;
	}
	public int getCodRul2() {
		return codRul2;
	}
	public void setCodRul2(int codRul2) {
		this.codRul2 = codRul2;
	}
	public int getCodRul3() {
		return codRul3;
	}
	public void setCodRul3(int codRul3) {
		this.codRul3 = codRul3;
	}
	public int getCodRul4() {
		return codRul4;
	}
	public void setCodRul4(int codRul4) {
		this.codRul4 = codRul4;
	}
	public int getCodRul5() {
		return codRul5;
	}
	public void setCodRul5(int codRul5) {
		this.codRul5 = codRul5;
	}
	public int getCodRul6() {
		return codRul6;
	}
	public void setCodRul6(int codRul6) {
		this.codRul6 = codRul6;
	}
	public int getCodRul7() {
		return codRul7;
	}
	public void setCodRul7(int codRul7) {
		this.codRul7 = codRul7;
	}
	public int getCodRul8() {
		return codRul8;
	}
	public void setCodRul8(int codRul8) {
		this.codRul8 = codRul8;
	}
	public int getCodRul9() {
		return codRul9;
	}
	public void setCodRul9(int codRul9) {
		this.codRul9 = codRul9;
	}
	public int getConceptCount() {
		return conceptCount;
	}
	public void setConceptCount(int conceptCount) {
		this.conceptCount = conceptCount;
	}
	public int getLinkCodify() {
		return linkCodify;
	}
	public void setLinkCodify(int linkCodify) {
		this.linkCodify = linkCodify;
	}
	public String getMapName() {
		return mapName;
	}
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	public int getPropMapCount() {
		return propMapCount;
	}
	public void setPropMapCount(int propMapCount) {
		this.propMapCount = propMapCount;
	}
	public int getSemanticCod() {
		return semanticCod;
	}
	public void setSemanticCod(int semanticCod) {
		this.semanticCod = semanticCod;
	}
	public int getHypeHypoRels() {
		return hypeHypoRels;
	}
	public void setHypeHypoRels(int hypeHypoRel) {
		this.hypeHypoRels = hypeHypoRel;
	}
	public int getMeroHoloRels() {
		return meroHoloRels;
	}
	public void setMeroHoloRels(int meroHoloRel) {
		this.meroHoloRels = meroHoloRel;
	}
	public int getMeroHoloTypeRels() {
		return meroHoloTypeRels;
	}
	public void setMeroHoloTypeRels(int meroHoloTypeRel) {
		this.meroHoloTypeRels = meroHoloTypeRel;
	}
	public int getCycHierarchy() {
		return cycHierarchy;
	}
	public void setCycHierarchy(int cycHierarchy) {
		this.cycHierarchy = cycHierarchy;
	}
	public int getCycInstance() {
		return cycInstance;
	}
	public void setCycInstance(int cycInstance) {
		this.cycInstance = cycInstance;
	}
	public int getOwlClassSubClass() {
		return owlClassSubClass;
	}
	public void setOwlClassSubClass(int owlClassSubClass) {
		this.owlClassSubClass = owlClassSubClass;
	}
	public int getOwlInstance() {
		return owlInstance;
	}
	public void setOwlInstance(int owlInstannce) {
		this.owlInstance = owlInstannce;
	}
	public int getOwlObjectProperty() {
		return owlObjectProperty;
	}
	public void setOwlObjectProperty(int owlObjectProperty) {
		this.owlObjectProperty = owlObjectProperty;
	}
	public int getOwlRestricProperty() {
		return owlRestricProperty;
	}
	public void setOwlRestricProperty(int owlRestricProperty) {
		this.owlRestricProperty = owlRestricProperty;
	}
	public int getHypeHypo() {
		return hypeHypo;
	}
	public void setHypeHypo(int hypeHypo) {
		this.hypeHypo = hypeHypo;
	}
	public int getMeroHolo() {
		return meroHolo;
	}
	public void setMeroHolo(int meroHolo) {
		this.meroHolo = meroHolo;
	}
	public int getMeroHoloType() {
		return meroHoloType;
	}
	public void setMeroHoloType(int meroHoloType) {
		this.meroHoloType = meroHoloType;
	}
	public int getCycHierarchyRels() {
		return cycHierarchyRels;
	}
	public void setCycHierarchyRels(int cycHierarchyRels) {
		this.cycHierarchyRels = cycHierarchyRels;
	}
	public int getCycInstanceRels() {
		return cycInstanceRels;
	}
	public void setCycInstanceRels(int cycInstanceRels) {
		this.cycInstanceRels = cycInstanceRels;
	}
	public int getOwlClassSubClassRels() {
		return owlClassSubClassRels;
	}
	public void setOwlClassSubClassRels(int owlClassSubClassRels) {
		this.owlClassSubClassRels = owlClassSubClassRels;
	}
	public int getOwlInstanceRels() {
		return owlInstanceRels;
	}
	public void setOwlInstanceRels(int owlInstannceRels) {
		this.owlInstanceRels = owlInstannceRels;
	}
	public int getOwlObjectPropertyRels() {
		return owlObjectPropertyRels;
	}
	public void setOwlObjectPropertyRels(int owlObjectPropertyRels) {
		this.owlObjectPropertyRels = owlObjectPropertyRels;
	}
	public int getOwlRestricPropertyRels() {
		return owlRestricPropertyRels;
	}
	public void setOwlRestricPropertyRels(int owlRestricPropertyRels) {
		this.owlRestricPropertyRels = owlRestricPropertyRels;
	}
	
	   public void report(String mapMame){
		   
		   try {
			   this.totalRels=hypeHypoRels+meroHoloRels+meroHoloTypeRels+cycHierarchyRels+cycInstanceRels+cycInstanceRels+owlClassSubClassRels+owlInstanceRels+owlObjectPropertyRels+owlRestricPropertyRels;
			   
			   FileWriter fileResults = new FileWriter(this.path+mapMame+"Result.txt");
			   fileResults.write("Nombre Mapa: " + mapMame+'\n');
			   fileResults.write('\n');
			   
			   fileResults.write("Relevancia Semantica del Repositorio de Ontologías para este mapa: "); 
			   fileResults.write(String.valueOf(this.relevance));
			   fileResults.write('\n');

			   fileResults.write("Conceptos: ");
			   fileResults.write(String.valueOf(this.conceptCount));
			   fileResults.write('\n');
			   fileResults.write('\n');

			   fileResults.write("Proposiciones: ");
			   fileResults.write(String.valueOf(this.propMapCount));
			   fileResults.write('\n');
			   fileResults.write('\n');

			   fileResults.write("Proposiciones codificadas: ");
			   fileResults.write(String.valueOf(this.cantCodificadas));
			   fileResults.write('\n');
			   fileResults.write('\n');
			   
			   fileResults.write("Proposiciones encontradas por semantica: ");
			   fileResults.write(String.valueOf(this.totalRels));
			   fileResults.write('\n');
			   fileResults.write('\n');
			   
			   fileResults.write("Proposiciones codificadas por semantica: ");
			   fileResults.write(String.valueOf(this.semanticCod));
			   fileResults.write('\n');
			   fileResults.write('\n');

			   fileResults.write("Proposiciones codificadas por f.e: ");
			   fileResults.write(String.valueOf(this.linkCodify));
			   fileResults.write('\n');
			   fileResults.write('\n');
//			   ...TOTAL de relaciones obtenidas...
			   fileResults.write("Relaciones encontradas en las Bases de Conocimiento :");
			   fileResults.write('\n');
			   fileResults.write('\n');

			   fileResults.write("<WordNet>");	
			   fileResults.write('\n');
			   fileResults.write('\n');

			   fileResults.write("Hype_Hypo_Relations: ");
			   fileResults.write(String.valueOf(this.hypeHypoRels));
			   fileResults.write('\n');

			   fileResults.write("Mero_Holo_Relations: ");
			   fileResults.write(String.valueOf(this.meroHoloRels));
			   fileResults.write('\n');

			   fileResults.write("Mero_Holo_Type_Relations: ");
			   fileResults.write(String.valueOf(this.meroHoloTypeRels));
			   fileResults.write('\n');
			   fileResults.write('\n');
			   
			   fileResults.write("<OpenCyc>");	
			   fileResults.write('\n');
			   fileResults.write('\n');

			   fileResults.write("OpenCyc_Hierarchy_Relations: ");
			   fileResults.write(String.valueOf(this.cycHierarchyRels));
			   fileResults.write('\n');

			   fileResults.write("OpenCyc_Instance_Relations ");
			   fileResults.write(String.valueOf(this.cycInstanceRels));
			   fileResults.write('\n');
			   fileResults.write('\n');

			   fileResults.write("<OWLRepository>");
			   fileResults.write('\n');
			   fileResults.write('\n');

			   fileResults.write("OWL_ClassSubClass_Relations : ");
			   fileResults.write(String.valueOf(this.owlClassSubClassRels));
			   fileResults.write('\n');		

			   fileResults.write("OWL_Instance_Relations : ");
			   fileResults.write(String.valueOf(this.owlInstanceRels));
			   fileResults.write('\n');

			   fileResults.write("OWL_ObjectProperty_Relations : ");
			   fileResults.write(String.valueOf(this.owlObjectPropertyRels));
			   fileResults.write('\n');

			   fileResults.write("OWL_RestricProperty_Relations : ");
			   fileResults.write(String.valueOf(this.owlRestricPropertyRels));
			   fileResults.write('\n');
			   fileResults.write('\n');
//			   ...Relaciones obtenidas por inferencia en WN...			
			   fileResults.write("Inferencias obtenidas por las Bases de Conocimiento:");
			   fileResults.write('\n');
			   fileResults.write('\n');

			   fileResults.write("<WordNet>");	
			   fileResults.write('\n');
			   fileResults.write('\n');

			   fileResults.write("Hype_Hypo_Infered: ");
			   fileResults.write(String.valueOf(this.hypeHypo));
			   fileResults.write('\n');

			   fileResults.write("Mero_Holo_Ifered: ");
			   fileResults.write(String.valueOf(this.meroHolo));
			   fileResults.write('\n');

			   fileResults.write("Mero_Holo_Type_Infered: ");
			   fileResults.write(String.valueOf(this.meroHoloType));
			   fileResults.write('\n');
			   fileResults.write('\n');

			   fileResults.write("<OpenCyc>");	
			   fileResults.write('\n');
			   fileResults.write('\n');

			   fileResults.write("OpenCyc_Hierarchy_Infered : ");
			   fileResults.write(String.valueOf(this.cycHierarchy));
			   fileResults.write('\n');

			   fileResults.write("OpenCyc_Instance_Infered : ");
			   fileResults.write(String.valueOf(this.cycInstance));
			   fileResults.write('\n');
			   fileResults.write('\n');

			   fileResults.write("<OWLRepository>");
			   fileResults.write('\n');
			   fileResults.write('\n');

			   fileResults.write("OWL_ClassSubClass_Infered : ");
			   fileResults.write(String.valueOf(this.owlClassSubClass));
			   fileResults.write('\n');		

			   fileResults.write("OWL_Instance_Infered : ");
			   fileResults.write(String.valueOf(this.owlInstance));
			   fileResults.write('\n');

			   fileResults.write("OWL_ObjectProperty_Infered : ");
			   fileResults.write(String.valueOf(this.owlObjectProperty));
			   fileResults.write('\n');

			   fileResults.write("OWL_RestricProperty_Infered : ");
			   fileResults.write(String.valueOf(this.owlRestricProperty));
			   fileResults.write('\n');
			   fileResults.write('\n');

//			   .........................Reglas.........................................................	
			   fileResults.write("Cantidad de ejecuciones de cada regla: ");
			   fileResults.write('\n');
			   fileResults.write('\n');

			   fileResults.write("R1: ");
			   fileResults.write(String.valueOf(this.codRul1));
			   fileResults.write('\n');

			   fileResults.write("R2: ");
			   fileResults.write(String.valueOf(this.codRul2));
			   fileResults.write('\n');

			   fileResults.write("R3: ");
			   fileResults.write(String.valueOf(this.codRul3));
			   fileResults.write('\n');

			   fileResults.write("R4: ");
			   fileResults.write(String.valueOf(this.codRul4));
			   fileResults.write('\n');

			   fileResults.write("R5: ");
			   fileResults.write(String.valueOf(this.codRul5));
			   fileResults.write('\n');

			   fileResults.write("R6: ");
			   fileResults.write(String.valueOf(this.codRul6));
			   fileResults.write('\n');

			   fileResults.write("R7: ");
			   fileResults.write(String.valueOf(this.codRul7));
			   fileResults.write('\n');

			   fileResults.write("R8: ");
			   fileResults.write(String.valueOf(this.codRul8));
			   fileResults.write('\n');

			   fileResults.write("R9: ");
			   fileResults.write(String.valueOf(this.codRul9));
			   fileResults.write('\n');

			   fileResults.write("R10: ");
			   fileResults.write(String.valueOf(this.codRul10));
			   fileResults.write('\n');

			   fileResults.write("R11: ");
			   fileResults.write(String.valueOf(this.codRul11));
			   fileResults.write('\n');

			   fileResults.close();

		   } catch (IOException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
		   }
	   }
	public double getRelevance() {
		return relevance;
	}
	public void setRelevance(double relevance) {
		this.relevance = relevance;
	}


}
