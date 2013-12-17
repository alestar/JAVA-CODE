/**
 * 
 */
package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import parse.ParseOWL;
import parse.ResultMap;
import sintax.CheckSintax;
import wordnetAccess.WNAccess;
import wordnetAccess.WNFactory;
import wordnetDataFile.Relation;
import wordnetDataFile.Synset;
import OpenCycAccess.OpenCycAccess;
import classes.IntersectionProperty;
import classes.Union;
import cmaps.CMap;
import cmaps.CmapConcept;
import cmaps.CmapLink;
import cmaps.CmapProposition;
import connection.Connection_MC;
import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLHasValue;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLIntersectionClass;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLObjectProperty;
import edu.stanford.smi.protegex.owl.model.OWLSomeValuesFrom;
import edu.stanford.smi.protegex.owl.model.OWLUnionClass;
import file.FileConcepsManage;
import freelingAccess.FreelingAccess;
/**
 * @author Amhed y Armando
 *
 */
//::::::::::::::::::::::::::::::::::::::Clase Atributos::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
/**
 * @author FRANCOIS
 *
 */
/**
 * @author FRANCOIS
 *
 */
/**
 * @author FRANCOIS
 *
 */
public class ObjectOwl implements Runnable {

	private CMap map;
	//Conjuntos Obtenidos de la Fase de Preprosesamiento,por el Interprete Sintactico(Parseador)
	private ArrayList<CmapConcept> desConcepts;// Conceptos del MC(C-MC)
	private ArrayList<CmapProposition>mapOriginal; //proposiciones  del MC (PP-MC)
	private ArrayList<CmapProposition>mapPrepToRel; //proposiciones  del MC para sacar relaciones de las Bases de Conocimiento
	private ArrayList<CmapProposition>mapPrepToRep; //proposiciones  del MC para sacar relaciones en el RepositorioOWL especificamente(para acelerar el procesamiento)
	private ArrayList<CmapProposition>mapPrepToInfer; //proposiciones  del MC para inferir en la reglas
	//Conexion con BD de Servimap
	private String server;
	private String repositoryDB;
	private String user;
	private String pass;
	
	private CheckSintax Sintax;
	
	//Directorios de Freeling 2.0
	private String frlanguage;
	private String frPath;
	FreelingAccess FAcess;
	//Directorios de WN 2.1
	FileConcepsManage wnConcepsFile;
	FileConcepsManage repOWLMatchConcepsFile;
	FileConcepsManage repOWLIncompletePropMatch;
	private String pathWNDir;
	private WNFactory wnFactory;
	private WNAccess  wnAccessfiles21;
	private WNAccess  wnAccessfiles16;
//	Conjuntos obtenidos por WordNet
	private ArrayList<CmapProposition> hypeHypoRels;
	private ArrayList<CmapProposition> meroHoloRels;
	private ArrayList<CmapProposition> meroHoloTypeRels;
	private ArrayList<CmapProposition> prepositionRepository;
//	Conjuntos obtenidos por el Repositorio de Ontologías
	private JenaOWLModel owlModel;
	private String owlRepositoryPath="";
	private ParseOWL owlParse;
	private ArrayList<CmapProposition> owlClassSubClass;
	private ArrayList<CmapProposition> owlInstance;
	private ArrayList<CmapProposition> owlObjectProperty;
	private ArrayList<CmapProposition> owlRestricProperty;
	private boolean owlRepositoryLoaded=false;

//  Conjuntos obtenidos por OpenCyc	
	private OpenCycAccess OpenCyc;
	private String OpenCycpath;
	private ArrayList<CmapProposition> CycHerarchys;
	private ArrayList<CmapProposition> CycInstances;
//  Conjuntos  Globales de la Propuesta obtenidos por todas fuentes
	private ArrayList<CmapProposition> CRSClassSubClass;
	private ArrayList<CmapProposition> CRSInstannce;
	private ArrayList<CmapProposition> CRSmeroHolo;
	private ArrayList<CmapProposition> CRSmeroHoloType;
	private ArrayList<CmapProposition> CRSObjectProperty;
	private ArrayList<CmapProposition> CRSRestricProperty;
	
//	Conjuntos obtenidos por f.e(CFE) y por la semantica/Para luego codificarlas...
	private ArrayList<CmapProposition>C_CS;//Clasificacion
	private ArrayList<CmapProposition>C_CI;//Instancia
	private ArrayList<CmapProposition>C_CP;//Propiedad
	private ArrayList<CmapProposition>C_CPV;//Propiedad-Valor/regla unica k utiliza a frelling
	private ArrayList<CmapProposition>C_CPVHasValue;
	//LIstas para codificar
	private ArrayList<CmapProposition>symetricProperty;
	private ArrayList<CmapProposition>functionalProperty;
	private ArrayList<Union>intersectionClass;
	private ArrayList<IntersectionProperty>intersectionClassProperty;
	private ArrayList<Union>unionClass;
			
	//Clase para calcular cobertura y estadisticas
	private ResultMap result;
	//Lista de Mapas extraidos del Repositorio de Ontologías

//	::::::::::::::::::::::::::::::::::::::::.Constructor:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::   
	@SuppressWarnings("static-access")
	public ObjectOwl(CMap mapparsed) {
		this.mapOriginal = new ArrayList<CmapProposition>();
		this.mapPrepToRel = new ArrayList<CmapProposition>();
		this.mapPrepToRep= new ArrayList<CmapProposition>();
		this.mapPrepToInfer = new ArrayList<CmapProposition>();
		
		this.desConcepts = mapparsed.getConcepts();
		this.mapOriginal.addAll(mapparsed.getPropositions());
		this.mapPrepToRel.addAll(mapparsed.getPropositions());
		this.mapPrepToInfer.addAll(mapparsed.getPropositions());
		map=mapparsed;
		
		server="";
		repositoryDB="";
		user="";
		pass="";
		
		Sintax= new CheckSintax();
		
		//OpenCycpath="C:/opencyc-2.0/scripts";
		OpenCycpath="opencyc-2.0/scripts";
	
		owlRepositoryPath="OWLRepository/";
		repOWLIncompletePropMatch=new FileConcepsManage("StaticsResult/RepositoryOWL/RepositoryOWLIncompletePropMatch");
		
		repOWLMatchConcepsFile= new FileConcepsManage("StaticsResult/RepositoryOWL/RepositoryOWLMatchMap");
		repOWLMatchConcepsFile.loadConcep();
		wnConcepsFile=new FileConcepsManage("StaticsResult/Wordnet/WordnetMatchMap");
		wnConcepsFile.loadConcep();
		
		pathWNDir="WordNets/";
		wnAccessfiles21=wnFactory.getInstance("WordNet 2.1", pathWNDir);
		wnAccessfiles16=wnFactory.getInstance("WordNet 1.6", pathWNDir);

		frlanguage="en";
		frPath="FreeLing-2.0/";
		this.FAcess= new FreelingAccess(this.frlanguage,this.frPath); 
		
		this.C_CS = new ArrayList<CmapProposition>();
		this.C_CI = new ArrayList<CmapProposition>();
		this.C_CP = new ArrayList<CmapProposition>();
		this.C_CPV = new ArrayList<CmapProposition>();
		this.C_CPVHasValue = new ArrayList<CmapProposition>();
		this.symetricProperty = new ArrayList<CmapProposition>();
		this.functionalProperty = new ArrayList<CmapProposition>();
		this.intersectionClass  = new ArrayList<Union>();
		this.intersectionClassProperty = new ArrayList<IntersectionProperty>();
		this.unionClass = new ArrayList<Union>();
		this.owlModel = ProtegeOWL.createJenaOWLModel();
		this.OpenCyc = new OpenCycAccess();
		this.owlParse =  new ParseOWL(owlRepositoryPath);
		this.prepositionRepository = new ArrayList<CmapProposition>();
		this.result = new ResultMap(this.desConcepts.size(),this.mapOriginal.size(),this.map.getTitle());
		this.hypeHypoRels = new ArrayList<CmapProposition>();
		this.meroHoloRels = new ArrayList<CmapProposition>();
		this.meroHoloTypeRels = new ArrayList<CmapProposition>();
		this.owlClassSubClass= new ArrayList<CmapProposition>() ;
		this.owlInstance= new ArrayList<CmapProposition>() ;
		this.owlObjectProperty= new ArrayList<CmapProposition>() ;
		this.owlRestricProperty= new ArrayList<CmapProposition>() ;
		this.CycHerarchys = new ArrayList<CmapProposition>() ;
		this.CycInstances = new ArrayList<CmapProposition>() ;
		this.CRSClassSubClass= new ArrayList<CmapProposition>() ;
		this.CRSInstannce= new ArrayList<CmapProposition>() ;
		this.CRSmeroHolo= new ArrayList<CmapProposition>() ;
		this.CRSmeroHoloType= new ArrayList<CmapProposition>() ;
		this.CRSObjectProperty= new ArrayList<CmapProposition>() ;
		this.CRSRestricProperty= new ArrayList<CmapProposition>() ;
	}


	@SuppressWarnings("static-access")
	public ObjectOwl() {
		this.desConcepts = new ArrayList<CmapConcept>();
		this.mapOriginal = new ArrayList<CmapProposition>();
		this.mapPrepToRel = new ArrayList<CmapProposition>();
		this.mapPrepToRep= new ArrayList<CmapProposition>();
		this.mapPrepToInfer = new ArrayList<CmapProposition>();
		this.map = new CMap();
	
		server="";
		repositoryDB="";
		user="";
		pass="";
		
		Sintax= new CheckSintax();
		
		//OpenCycpath="C:/opencyc-2.0/scripts";
		OpenCycpath="opencyc-2.0/scripts";
		owlRepositoryPath="OWLRepository/";
		
		repOWLIncompletePropMatch=new FileConcepsManage("StaticsResult/RepositoryOWL/RepositoryOWLIncompletePropMatch");
		
		repOWLMatchConcepsFile= new FileConcepsManage("StaticsResult/RepositoryOWL/RepositoryOWLMatchMap");
		repOWLMatchConcepsFile.loadConcep();
		wnConcepsFile=new FileConcepsManage("StaticsResult/Wordnet/WordnetMatchMap");
		wnConcepsFile.loadConcep();
		
		pathWNDir="WordNets/";
		wnAccessfiles21=wnFactory.getInstance("WordNet 2.1", pathWNDir);
		wnAccessfiles16=wnFactory.getInstance("WordNet 1.6", pathWNDir);
		
		frlanguage="en";
		frPath="FreeLing-2.0/";
		this.FAcess= new FreelingAccess(this.frlanguage,this.frPath); 

		this.C_CS = new ArrayList<CmapProposition>();
		this.C_CI = new ArrayList<CmapProposition>();
		this.C_CP = new ArrayList<CmapProposition>();
		this.C_CPV = new ArrayList<CmapProposition>();
		this.C_CPVHasValue = new ArrayList<CmapProposition>();
		this.symetricProperty = new ArrayList<CmapProposition>();
		this.functionalProperty = new ArrayList<CmapProposition>();
		this.intersectionClass  = new ArrayList<Union>();
		this.intersectionClassProperty = new ArrayList<IntersectionProperty>();
		this.unionClass = new ArrayList<Union>();
		this.owlModel = ProtegeOWL.createJenaOWLModel();
		this.owlParse =  new ParseOWL(owlRepositoryPath);
		this.OpenCyc = new OpenCycAccess();//Carga Acceso al Modelo de OpenCyc
		this.prepositionRepository = new ArrayList<CmapProposition>();
		this.result = new ResultMap(this.desConcepts.size(),this.mapPrepToRel.size(),this.map.getTitle());
		this.hypeHypoRels = new ArrayList<CmapProposition>();
		this.meroHoloRels = new ArrayList<CmapProposition>();
		this.meroHoloTypeRels = new ArrayList<CmapProposition>();
		this.owlClassSubClass= new ArrayList<CmapProposition>() ;
		this.owlInstance= new ArrayList<CmapProposition>() ;
		this.owlObjectProperty= new ArrayList<CmapProposition>() ;
		this.owlRestricProperty= new ArrayList<CmapProposition>() ;
		this.CycHerarchys = new ArrayList<CmapProposition>() ;
		this.CycInstances = new ArrayList<CmapProposition>() ;
		this.CRSClassSubClass= new ArrayList<CmapProposition>() ;
		this.CRSInstannce= new ArrayList<CmapProposition>() ;
		this.CRSmeroHolo= new ArrayList<CmapProposition>() ;
		this.CRSmeroHoloType= new ArrayList<CmapProposition>() ;
		this.CRSObjectProperty= new ArrayList<CmapProposition>() ;
		this.CRSRestricProperty= new ArrayList<CmapProposition>() ;
	}
	
	public void initialize() {
		this.desConcepts = map.getConcepts();
		this.mapOriginal.addAll(map.getPropositions());
		this.mapPrepToRel.addAll(map.getPropositions());
		this.mapPrepToInfer.addAll(map.getPropositions());
		this.owlModel = ProtegeOWL.createJenaOWLModel();
		this.result = new ResultMap(this.desConcepts.size(),this.mapOriginal.size(),this.map.getTitle());
	
	}
	
	public void finalize(){
		this.desConcepts.clear();
		this.mapOriginal.clear();
		this.mapPrepToRel.clear();
		this.mapPrepToInfer.clear();
		this.mapPrepToRep.clear();
		this.C_CI.clear();
		this.C_CP.clear();this.C_CP.clear();
		this.C_CPV.clear();
		this.C_CPVHasValue.clear();
		this.C_CS.clear();
		this.CRSClassSubClass.clear();
		this.CRSInstannce.clear();
		this.CRSmeroHolo.clear();
		this.CRSmeroHoloType.clear();
		this.CRSObjectProperty.clear();
		this.CRSRestricProperty.clear();
		this.CycHerarchys.clear();
		this.CycInstances.clear();
		this.hypeHypoRels.clear();
		this.meroHoloRels.clear();
		this.meroHoloTypeRels.clear();
		this.owlClassSubClass.clear();
		this.owlInstance.clear();
		this.owlObjectProperty.clear();
		this.owlRestricProperty.clear();
		this.unionClass.clear();
		this.intersectionClass.clear();
		this.intersectionClassProperty.clear();
		this.owlModel.close();
		this.owlModel=null;
	}
	
//	::::::::::::::::::::::::::::::::::::::Metodos Set y Get::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	public String getFrlanguage() {
		return frlanguage;
	}

	public void setFrlanguage(String frlanguage) {
		this.frlanguage = frlanguage;
	}

	public String getFrPath() {
		return frPath;
	}

	public void setFrPath(String frPath) {
		this.frPath = frPath;
	}

	public FreelingAccess getFAcess() {
		return FAcess;
	}


	public void setFAcess(FreelingAccess acess) {
		FAcess = acess;
	}

	public String getOwlRepositoryPath() {
		return owlRepositoryPath;
	}

	public void setOwlRepositoryPath(String lrepositoryPath) {
		owlRepositoryPath = lrepositoryPath;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getPathWNDir() {
		return pathWNDir;
	}

	public void setPathWNDir(String pathWNDir) {
		this.pathWNDir = pathWNDir;
	}

	public WNAccess getWnAccessfiles21() {
		return wnAccessfiles21;
	}

	public void setWnAccessfiles21(WNAccess wnAccessfiles21) {
		this.wnAccessfiles21 = wnAccessfiles21;
	}

	public OpenCycAccess getOpenCyc() {
		return OpenCyc;
	}

	public void setOpenCyc(OpenCycAccess openCyc) {
		OpenCyc = openCyc;
	}

	public CMap getMap() {
		return map;
	}

	public void setMap(CMap map) {
		this.map = map;
	}

	public String getRepositoryDB() {
		return repositoryDB;
	}

	public void setRepositoryDB(String repositoryDB) {
		this.repositoryDB = repositoryDB;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	public ArrayList<CmapConcept> getDesConcepts() {
		return desConcepts;
	}

	public void setDesConcepts(ArrayList<CmapConcept> desConcepts) {
		this.desConcepts = desConcepts;
	}

	public ArrayList<CmapProposition> getMapPrepToRel() {
		return mapPrepToRel;
	}

	public void setMapPrepToRel(ArrayList<CmapProposition> mapProp) {
		this.mapPrepToRel = mapProp;
	}

	public ArrayList<CmapProposition> getFunctionalProperty() {
		return functionalProperty;
	}

	public void setFunctionalProperty(ArrayList<CmapProposition> functionalProperty) {
		this.functionalProperty = functionalProperty;
	}

	public ArrayList<Union> getIntersectionClass() {
		return intersectionClass;
	}

	public void setIntersectionClass(ArrayList<Union> intersectionClass) {
		this.intersectionClass = intersectionClass;
	}

	public ArrayList<IntersectionProperty> getIntersectionClassProperty() {
		return intersectionClassProperty;
	}

	public void setIntersectionClassProperty(
			ArrayList<IntersectionProperty> intersectionClassProperty) {
		this.intersectionClassProperty = intersectionClassProperty;
	}

	public ArrayList<CmapProposition> getC_CI() {
		return C_CI;
	}

	public void setC_CI(ArrayList<CmapProposition> s_ci) {
		C_CI = s_ci;
	}

	public ArrayList<CmapProposition> getC_CP() {
		return C_CP;
	}

	public void setC_CP(ArrayList<CmapProposition> s_cp) {
		C_CP = s_cp;
	}

	public ArrayList<CmapProposition> getC_CPV() {
		return C_CPV;
	}

	public void setC_CPV(ArrayList<CmapProposition> s_cpv) {
		C_CPV = s_cpv;
	}

	public ArrayList<CmapProposition> getC_CPVHasValue() {
		return C_CPVHasValue;
	}

	public void setC_CPVHasValue(ArrayList<CmapProposition> hasValue) {
		C_CPVHasValue = hasValue;
	}

	public ArrayList<CmapProposition> getC_CS() {
		return C_CS;
	}

	public void setC_CS(ArrayList<CmapProposition> s_cs) {
		C_CS = s_cs;
	}

	public ArrayList<CmapProposition> getSymetricProperty() {
		return symetricProperty;
	}

	public void setSymetricProperty(ArrayList<CmapProposition> symetricProperty) {
		this.symetricProperty = symetricProperty;
	}

	public ArrayList<Union> getUnionClass() {
		return unionClass;
	}

	public void setUnionClass(ArrayList<Union> unionClass) {
		this.unionClass = unionClass;
	}

	public JenaOWLModel getOwlModel() {
		return owlModel;
	}

	public void setOwlModel(JenaOWLModel owlModel) {
		this.owlModel = owlModel;
	}
	public ArrayList<CmapProposition> getHypeHypoRels() {
		return hypeHypoRels;
	}

	public void setHypeHypoRels(ArrayList<CmapProposition> hypeHypoRel) {
		this.hypeHypoRels = hypeHypoRel;
	}

	public ArrayList<CmapProposition> getMeroHoloRels() {
		return meroHoloRels;
	}

	public void setMeroHoloRels(ArrayList<CmapProposition> meroHoloRel) {
		this.meroHoloRels = meroHoloRel;
	}

	public ArrayList<CmapProposition> getMeroHoloTypeRels() {
		return meroHoloTypeRels;
	}

	public void setMeroHoloTypeRels(ArrayList<CmapProposition> meroHoloType) {
		this.meroHoloTypeRels = meroHoloType;
	}

	public ResultMap getResult() {
		return result;
	}

	public void setResult(ResultMap result) {
		this.result = result;
	}

	public ArrayList<CmapProposition> getConceptRepository() {
		return prepositionRepository;
	}

	public ParseOWL getOwlParse() {
		return owlParse;
	}

	public void setOwlParse(ParseOWL owlParse) {
		this.owlParse = owlParse;
	}
	
	public ArrayList<CmapProposition> getOwlClassSubClass() {
		return owlClassSubClass;
	}

	public void setOwlClassSubClass(ArrayList<CmapProposition> owlClassSubClass) {
		this.owlClassSubClass = owlClassSubClass;
	}

	public ArrayList<CmapProposition> getOwlInstance() {
		return owlInstance;
	}

	public void setOwlInstance(ArrayList<CmapProposition> owlInstannce) {
		this.owlInstance = owlInstannce;
	}

	public ArrayList<CmapProposition> getOwlObjectProperty() {
		return owlObjectProperty;
	}

	public void setOwlObjectProperty(ArrayList<CmapProposition> owlObjectProperty) {
		this.owlObjectProperty = owlObjectProperty;
	}

	public ArrayList<CmapProposition> getOwlProperty() {
		return owlRestricProperty;
	}

	public void setOwlProperty(ArrayList<CmapProposition> owlProperty) {
		this.owlRestricProperty = owlProperty;
	}
	
	public void setOpenCycpath(String openCycpath) {
		OpenCycpath = openCycpath;
	}

	public ArrayList<CmapProposition> getCRSObjectProperty() {
		return CRSObjectProperty;
	}
	
	public ArrayList<CmapProposition> getCycHerarchys() {
		return CycHerarchys;
	}


	public void setCycHerarchys(ArrayList<CmapProposition> cycHerarchys) {
		CycHerarchys = cycHerarchys;
	}


	public ArrayList<CmapProposition> getCycInstances() {
		return CycInstances;
	}

	public void setCycInstances(ArrayList<CmapProposition> cycInstances) {
		CycInstances = cycInstances;
	}
	
	public ArrayList<CmapProposition> getCRSClassSubClass() {
		return CRSClassSubClass;
	}

	public void setCRSClassSubClass(ArrayList<CmapProposition> classSubClass) {
		CRSClassSubClass = classSubClass;
	}

	public ArrayList<CmapProposition> getCRSInstannce() {
		return CRSInstannce;
	}

	public void setCRSInstannce(ArrayList<CmapProposition> instannce) {
		CRSInstannce = instannce;
	}

	public ArrayList<CmapProposition> getCRSmeroHolo() {
		return CRSmeroHolo;
	}

	public void setCRSmeroHolo(ArrayList<CmapProposition> smeroHolo) {
		CRSmeroHolo = smeroHolo;
	}

	public ArrayList<CmapProposition> getCRSmeroHoloType() {
		return CRSmeroHoloType;
	}

	public void setCRSmeroHoloType(ArrayList<CmapProposition> smeroHoloType) {
		CRSmeroHoloType = smeroHoloType;
	}
	
	public String getOpenCycpath() {
		return OpenCycpath;
	}
	
	public void setCRSObjectProperty(ArrayList<CmapProposition> propertyObject) {
		CRSObjectProperty = propertyObject;
	}

	public ArrayList<CmapProposition> getCRSRestricProperty() {
		return CRSRestricProperty;
	}

	public void setCRSRestricProperty(ArrayList<CmapProposition> restricProperty) {
		CRSRestricProperty = restricProperty;
	}
	
//	::::::::::::::::::::::::::::::::::::::Trabajo con la fuente de conocimientos Servimap:::::::::::::::::::::::::::::::::::::::::::::::::

	/**Método para buscar (PP-MC)propociciones k contienen al menos u cocepto del mapa
	 * @param origConcep;en este caso concepto origen como referencia,
	 * se obtienen de consultar a servimap
	 */	
	private ResultSet prepositionbyOrigConcep(String origConcep) {	

		ResultSet rsSenses = null;
		Connection_MC conn = new Connection_MC(this.repositoryDB,this.user,this.pass);		

		if(conn.setConnection()) {
			try {
				Connection con = conn.getConnection();
				String sqlQuery = " select TBArco.PalabraEnlace, TBConcepto.Nombre from TBConcepto inner join TBMapaConceptoArco" + " "+
				"on TBConcepto.IdConcepto = TBMapaConceptoArco.IdConceptoDestino inner join TBArco" + " "+
				"on TBArco.IdArco = TBMapaConceptoArco.IdArco" + " "+
				"where TBMapaConceptoArco.IdConceptoOrigen = (select TBConcepto.IdConcepto from TBConcepto where TBConcepto.Nombre = ?)";

				PreparedStatement pstm = con.prepareStatement(sqlQuery);
				pstm.setString(1, origConcep);
				rsSenses = pstm.executeQuery();	
			} 
			catch (SQLException e) {
				// TODO: handle exception
				//e.printStackTrace();
				System.out.println("");
			}
		}
		return  rsSenses;		
	}

	private ResultSet prepositionbyDestinyConcep(String destinyConcep) {	

		ResultSet rsSenses = null;
		Connection_MC conn = new Connection_MC(this.repositoryDB,this.user,this.pass);		

		if(conn.setConnection()) {
			try {
				Connection con = conn.getConnection();
				String sqlQuery = " select TBConcepto.Nombre, TBArco.PalabraEnlace  from TBConcepto inner join TBMapaConceptoArco" + " "+
				"on TBConcepto.IdConcepto = TBMapaConceptoArco.IdConceptoOrigen inner join TBArco" + " "+
				"on TBArco.IdArco = TBMapaConceptoArco.IdArco" + " "+
				"where TBMapaConceptoArco.IdConceptoDestino = (select TBConcepto.IdConcepto from TBConcepto where TBConcepto.Nombre = ?)";

				PreparedStatement pstm = con.prepareStatement(sqlQuery);
				pstm.setString(1, destinyConcep);
				rsSenses = pstm.executeQuery();	
			} 
			catch (SQLException e) {
				// TODO: handle exception
				//e.printStackTrace();
				System.out.println("");
			}
		}
		return  rsSenses;		
	}
//	::::::::::::::::::::::::::::::::TRABAJO CON EL FICHERO DE ENLACES(lINK)::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	/**Método para buscar la categoría correspondiente una palabra-enlace específica
	 * Acceso a datos con ficheros link.txt apra palabras en ingles
	 * @param link la palabra-enlace de la cual se desea obtener la categoría 
	 */	
	private String linkCategory(String link) {	
		String category= new String();
		category= wnAccessfiles21.getLinkCategory(link);
		/*if(category==null)
			category=wnAccessfiles16.getLinkCategory(link);*/
		return category;
	}

	/**Metodo que añada nuevas palabras de enlace con tratamiento de ficheros
	 * para añadir las p.e al fichero Frase Enlace
	 * @param linkName la palabra-enlace de la cual se desea obtener la categoría 
	 * @param category categoría de las palabars de enlace(Propiedad ,Subclasificación ..etc)
	 **/
	private boolean insertLinkWord(String linkName, String category) {
		return wnAccessfiles21.addLink(linkName, category);
	}

//	::::::::::::::::::::::::::::::::TRABAJO CON PREPOSICIONES:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::	
	public CmapProposition findProp(CmapProposition p, ArrayList<CmapProposition> propList){
		CmapProposition foundPrep= null;

		boolean found = false;
		Iterator<CmapProposition> iter=propList.iterator();
		while (iter.hasNext()&&(!found)) {
			CmapProposition prep=iter.next();

			String orig =prep.getOrigConcept().getConceptName();
			String dest = prep.getDestConcept().getConceptName();
			//	CmapLink linkName = prep.getLink();

			if(orig.equalsIgnoreCase( p.getOrigConcept().getConceptName() ) ){
				if(dest.equalsIgnoreCase(p.getDestConcept().getConceptName())){
					//		if(linkName.equalsIgnoreCase(p.getLink().getLinkName()))
					foundPrep= prep;
				}
			}
		}
		return foundPrep;
	}


/*	private Triple findTriple(CmapPreposition p, ArrayList<Triple> tripList) {

		Triple result = null;
		boolean found = false;
		int index = 0;

		while ((index <tripList.size())&&(!found)) {

			String orig = tripList.get(index).getOrigConcept().getConceptName();
			String dest = tripList.get(index).getDestConcept().getConceptName();

			if((orig.equalsIgnoreCase(p.getOrigConcept().getConceptName())) && 
					(dest.equalsIgnoreCase(p.getDestConcept().getConceptName()))) 	
				found = true;
			else
				index ++;
		}
		if(found)
			result = tripList.get(index);

		return result;
	}*/

	private boolean findIntersection(String intersectionName, ArrayList<Union> intersectionList) {

		boolean found = false;
		int index = 0;
		while ((index <intersectionList.size())&&(!found)) {					
			if((intersectionList.get(index).getConceptUnion().getConceptName().equalsIgnoreCase(intersectionName))) 
				found = true;
			else
				index ++;
		}

		return found;
	}

	private CmapConcept findOrigConcep(CmapConcept c,ArrayList<CmapProposition> propList) 
	{	
		boolean found = false;
		CmapConcept resulconcep= null;
		Iterator propitem=propList.iterator();
		while (propitem.hasNext() && !found ){
			CmapProposition prep = (CmapProposition) propitem.next();
			if(prep.getOrigConcept().getConceptName().equalsIgnoreCase(c.getConceptName()))
			{
				found = true;
				resulconcep=prep.getOrigConcept();
			}
		}
		return resulconcep;
	}
	
	private CmapConcept findDestConcep(CmapConcept c,ArrayList<CmapProposition> propList) 
	{	
		boolean found = false;
		CmapConcept resulconcep= null;
		Iterator propitem=propList.iterator();
		while (propitem.hasNext() && !found ){
			CmapProposition prep = (CmapProposition) propitem.next();
			if(prep.getDestConcept().getConceptName().equalsIgnoreCase(c.getConceptName()))
			{
				found = true;
				resulconcep=prep.getDestConcept();
			}

		}
		return resulconcep;
	}

	private ArrayList<CmapProposition> propByOrigenConcep(CmapProposition p) {
		ArrayList<CmapProposition>resultList = new ArrayList<CmapProposition>();

		for (CmapProposition propElement : prepositionRepository) {

			if(propElement.getOrigConcept().getConceptName().equalsIgnoreCase(p.getOrigConcept().getConceptName()))
				resultList.add(propElement);
		}
		return resultList;
	}

	private boolean isFunctionalProperty(CmapProposition p) {

		ArrayList<CmapProposition> origProp = propByOrigenConcep(p);
		int count = 0;
		boolean linkFound = false;

		for (CmapProposition propElement : origProp) {

			//verificar si las palabras de enlace son iguales o sinónimos

			if(propElement.getLink().getLinkName().equalsIgnoreCase(p.getLink().getLinkName()))
				linkFound = true;
			else{
				ArrayList<String> synPE = synonymByConcept(propElement.getLink().getLinkName());
				if(synPE.contains(p.getLink().getLinkName()))
					linkFound = true;
			}
			// si encontré la condicion del enlace voy a verificar los conceptos son sinónimos o  (I = C')
			if(linkFound ){
				if(propElement.getDestConcept().getConceptName().equalsIgnoreCase(p.getDestConcept().getConceptName()))
					count++;
				else{
					ArrayList<String> synI = synonymByConcept(propElement.getDestConcept().getConceptName());
					if(synI.contains(p.getDestConcept().getConceptName()))
						count++;
				}
			}
		}
		if((origProp.size()==count)&& (origProp.size()>0))
			return true;
		else
			return false;
	}


	private boolean isSymetricProperty(CmapProposition p){

		ArrayList<CmapProposition> inverses = propByDestConceptRepository(p);
		int count = 0;
		boolean linkFound = false;
		for (CmapProposition propElement : inverses) {

			//verificar si las palabras de enlace son iguales o sinónimos
			if(propElement.getLink().getLinkName().equalsIgnoreCase(p.getLink().getLinkName()))
				linkFound = true;
			else{
				ArrayList<String> syn = synonymByConcept(propElement.getLink().getLinkName());

				if(syn.contains(p.getLink().getLinkName()))
					linkFound = true;	  
			}
			//si encontré la condicion del enlace voy a verificar los conceptos son sinónimos o  (C' = C)
			if(linkFound == true){
				if(propElement.getDestConcept().getConceptName().equalsIgnoreCase(p.getOrigConcept().getConceptName()))
					count++;
				else{
					ArrayList<String> synC = synonymByConcept(propElement.getDestConcept().getConceptName());
					if(synC.contains(p.getDestConcept().getConceptName()))
						count++;  
				}
			}
		}
		if((inverses.size()>0) && (inverses.size()==count))
			return true;
		else
			return false;
	}

	private ArrayList<CmapProposition> propByDestConceptRepository(CmapProposition prp) {

		ArrayList<CmapProposition>resultList = new ArrayList<CmapProposition>();

		for (CmapProposition propItem : prepositionRepository) {

			if(propItem.getOrigConcept().getConceptName().equalsIgnoreCase(prp.getDestConcept().getConceptName()))
				resultList.add(propItem);

		}	

		return resultList;
	}

//	::::::::::::::::::::Metodos (busqueda de relaciones/Tratamiento) entre synset en fiheros de WN:::::::::::::::::::
	/**@author Alejandro
	 * @return Las relaciones del tipo Clase-SubClase; Clase-SubClase; propiedades de holonimia y meronimia..etc
	 * que son encontradas a traves de los synset de WN
	 */
	
	public void loadWordNetRelations(){

		hypeHypoRels = hypeHypo_PS_WN();
		meroHoloRels = meroHolo_PS_WN();
		meroHoloTypeRels = meroHoloType_PS_EWN();
	}

	public void loadCRSConjunction(){
//		Cargando todas las relaciones de Clase- SubClase que se puedan encontrar en las Base de Conocimiento Correspondiente		
		CRSClassSubClass.addAll(hypeHypoRels);
		CRSClassSubClass.addAll(CycHerarchys);
		CRSClassSubClass.addAll(owlClassSubClass);
//		.. Clase- Instancia...
		CRSInstannce.addAll(CycInstances);
		CRSInstannce.addAll(owlInstance);	
//		... meroholo ...	
		CRSmeroHolo.addAll(meroHoloRels);
//		...meroHoloType ...	
		CRSmeroHoloType.addAll(meroHoloTypeRels);
//		... Clase- ObjetoPropiedad ...	
		CRSObjectProperty.addAll(owlObjectProperty);
		
//		...Clase- Propiedad ...	
		CRSObjectProperty.addAll(owlRestricProperty);
		
		
	}
	/**@author Alejandro R.C
	 * @return Este método es el método fundamental de la aplicación
	 */
	public void loadAllRelations(){//... Estan ordenado por velocidad de procesamiento...
		finalize();
		initialize();
		createConceptRepository();//..hay que concectarse a la base de datos primero
		loadWordNetRelations();//Cargar las relaciones de WordNet 1)
		loadOWLRelations(); //Cargar las relaciones del repositorio de ontologias  2)
		loadOpenCycRelations();//Cargar las relaciones del Repositorio de Ontologías  3)
		loadCRSConjunction();//...Agrupa las relaciones cargadas anteriormente...
	}

//	::::::::::::::::::::::::::::::::::::::Fase Inicial.Cojuntos de Partida::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	/**
	 * Las proposiciones (CO, CD) son hyponimia(has_hyponym)
	 * Las proposiciones (CD, C0) son hyperonimia(has_hyperonym)
	 * @return Metodo para alamcenar las proposiciones del MC donde los sentidos de 
	 * los conceptos tengan relacion directa de hiperonimia o hiponimia 
	 * */
	public ArrayList<CmapProposition> hypeHypo_PS_WN(){
		ArrayList<CmapProposition> hypeHypoProp = new ArrayList<CmapProposition>();
		ArrayList<CmapProposition>prepLoad = new ArrayList<CmapProposition>();

		for (CmapProposition prepItem : mapPrepToRel){

			//Verificar si el "concepOrig" tiene sentido, en caso NUll ó >1 no tiene caso analizar el otro concepto de la preposición...
			if(prepItem.getOrigConcept().getSenses().size()==1){
				String	senOrig = prepItem.getOrigConcept().getSenses().get(0).getSense();

				//Verificar si el "concepDest" tiene sentido, en caso NUll ó >1 no tiene caso analizar su relacion con el "senOrig"...
				if(prepItem.getDestConcept().getSenses().size()==1){
					String senDest = prepItem.getDestConcept().getSenses().get(0).getSense();

					if((!senDest.equalsIgnoreCase("")) && (!senOrig.equalsIgnoreCase(""))){//Existen ambos sentidos desambiguados

						String type  = findDirctRelation(senOrig, senDest);
						if(type!=null){	/*..Verificar si hay relación directa...*/

//										has_hyponym						has_xpos_hyponym
							if (type.equalsIgnoreCase("Hyponym") || type .equalsIgnoreCase( "Instance Hyponym") ){
								prepItem.setType(type);
								hypeHypoProp.add(prepItem);
								prepLoad.add(prepItem);
							}
							/*..Cambien la orientacion de la preposicion a la forma "SuperClase-link-Clase"*/
//										has_hyperonym							has_xpos_hyperonym
							else if (type.equalsIgnoreCase( "Hypernym") || type.equalsIgnoreCase( "Instance Hypernym")) {
								CmapProposition prepinv = new CmapProposition(prepItem.getDestConcept(),prepItem.getLink(),prepItem.getOrigConcept(),type);
								hypeHypoProp.add(prepinv);
								prepLoad.add(prepItem);
							} 
						}
						else{/*..No existe una relación directa; verificar si hay relación  indirecta...*/

							type=findIndirctRelation(senOrig, senDest,"Hyponym");
							if(type!=null){
								prepItem.setType(type);
								hypeHypoProp.add(prepItem);
								prepLoad.add(prepItem);
							}
							else {
								type=findIndirctRelation(senOrig, senDest,"Instance Hyponym");
								if(type!=null){
									prepItem.setType(type);
									hypeHypoProp.add(prepItem);
									prepLoad.add(prepItem);
								}
								else{
									type=findIndirctRelation(senDest,senOrig,"Hypernym");
									if(type!=null){
										prepItem.setType(type);
										hypeHypoProp.add(prepItem);
										prepLoad.add(prepItem);
									}
									else{/*..Cambiar la orientacion de la preposicion a la forma "SuperClase-link-Clase"*/

										CmapProposition prepinv = new CmapProposition(prepItem.getDestConcept(),prepItem.getLink(),prepItem.getOrigConcept());
										type=findIndirctRelation(senOrig, senDest,"Hypernym");
										if(type!=null){
											prepinv.setType(type);
											hypeHypoProp.add(prepinv);
											prepLoad.add(prepItem);
										}
										else{
											type=findIndirctRelation(senOrig, senDest,"Instance Hypernym");
											if(type!=null){
												prepinv.setType(type);
												hypeHypoProp.add(prepinv);
												prepLoad.add(prepItem);
											}
											else{
												type=findIndirctRelation(senDest,senOrig,"Hyponym");
												if(type!=null){
													prepinv.setType(type);
													hypeHypoProp.add(prepinv);
													prepLoad.add(prepItem);
												}
											}
										}
									}
								}
							}			
						}
					}
				}
			}
		}
//		p/cada preposicion k ha sido cargada por este conjunto, se codifica y se elimina para no inferirla(o procesarla) otra ves en los demas conjuntos		
		for (CmapProposition preposition : prepLoad){
			String origConcep=preposition.getOrigConcept().getConceptName();
			String origDest=preposition.getDestConcept().getConceptName();
			
			if(!wnConcepsFile.findConcep(origConcep)){
				wnConcepsFile.addConcep(origConcep);
				wnConcepsFile.loadConcep();
			}
			if(!wnConcepsFile.findConcep(origDest)){
				wnConcepsFile.addConcep(origDest);
				wnConcepsFile.loadConcep();
			}
			mapPrepToRel.remove(preposition);
		}
			
		
		return hypeHypoProp;	
	}
		
	/**
	 * Meotod para alamcenar las proposiciones del MC donde los sentidos de 
	 * los conceptos tengan relacion directa de holonimia o meronimia 
	 * */
	public ArrayList<CmapProposition> meroHolo_PS_WN(){
		ArrayList<CmapProposition> meroHoloProp = new ArrayList<CmapProposition>();/*
		ArrayList<CmapPreposition>prepLoad = new ArrayList<CmapPreposition>();
		
		String senOrig = "";//string del offset de un sentido(ej:05109766)
		String senDest = "";

		for (Preposition propItem : mapPrepToRel) {

			if(propItem.getOrigConcept().getSenses().size()>0){
				senOrig = propItem.getOrigConcept().getSenses().get(0).getSense();

				if(propItem.getDestConcept().getSenses().size()>0) {
					senDest = propItem.getDestConcept().getSenses().get(0).getSense();

					if((!senDest.equalsIgnoreCase("")) && (!senOrig.equalsIgnoreCase(""))){

						String category = findDirctRelation(senOrig, senDest);

						if(category!=null){
							///Verificar equivalencia de los holonym y meronym en WN ingles
							if (category.equalsIgnoreCase("Member holonym") ||category.equalsIgnoreCase( "Substance holonym") ||category.equalsIgnoreCase( "Part holonym") 
								||category.equalsIgnoreCase( "Member meronym")||category.equalsIgnoreCase( "Substance meronym")||category.equalsIgnoreCase("Part meronym") )
										meroHoloProp.add(propItem);					
						}
					}
				}
			}
		}
		 for (CmapPreposition preposition : prepLoad){ 
		 	String origConcep=preposition.getOrigConcept().getConceptName();
			String origDest=preposition.getDestConcept().getConceptName();
			
			if(!wnConcepsFile.findConcep(origConcep)){
				wnConcepsFile.addConcep(origConcep);
				wnConcepsFile.loadConcep();
			}
			if(!wnConcepsFile.findConcep(origDest)){
				wnConcepsFile.addConcep(origDest);
				wnConcepsFile.loadConcep();
			}
				mapPrepToRel.remove(preposition); 
			}
			*/
		
		return meroHoloProp;
	}
	
	/**
	 * Meotod para alamcenar las proposiciones del MC donde los sentidos de 
	 * los conceptos tengan relacion directa de algun tipo de holonimia o meronimia 
	 * */
	public ArrayList<CmapProposition> meroHoloType_PS_EWN(){
		ArrayList<CmapProposition> meroHoloTypeProp = new ArrayList<CmapProposition>();
		ArrayList<CmapProposition>prepLoad = new ArrayList<CmapProposition>();
		int i=0;
		for (CmapProposition prepItem : mapPrepToRel){
			i++;
			if(prepItem.getOrigConcept().getSenses().size()>0){
				String senOrig = prepItem.getOrigConcept().getSenses().get(0).getSense();

				if(prepItem.getDestConcept().getSenses().size()>0){
					String senDest = prepItem.getDestConcept().getSenses().get(0).getSense();

					if((!senDest.equalsIgnoreCase("")) && (!senOrig.equalsIgnoreCase(""))){

						String type = findDirctRelation(senOrig, senDest);
						if(type!=null){
							
							/*..........OJO BUSCAR EQUIVALENCIAS PARA ESTAS CATEGORÍAS DE ENLACE.........................
						  has_holo_location/has_holo_madeof/has_holo_member/has_holo_part/has_holo_portion 
						  o has_mero_location/has_mero_madeof/has_mero_member/has_mero_part*/
							
							if (type.equalsIgnoreCase("Member holonym") ||type.equalsIgnoreCase( "Substance holonym") ||type.equalsIgnoreCase( "Part holonym" )
									||type.equalsIgnoreCase( "Member meronym")||type.equalsIgnoreCase("Substance meronym")||type.equalsIgnoreCase( "Part meronym")){

								CmapProposition prep = new CmapProposition(prepItem.getOrigConcept(),prepItem.getLink(),prepItem.getDestConcept(),type);
								prep.setType(type);
								meroHoloTypeProp.add(prep);   
								prepLoad.add(prepItem);
							}
						}
					} 
				}
			}
		}
//		p/cada preposicion k ha sido cargada por este conjunto, se codifica y se elimina para no inferirla(o procesarla) otra ves en los demas conjuntos				
		for (CmapProposition preposition : prepLoad){ 
			String origConcep=preposition.getOrigConcept().getConceptName();
			String origDest=preposition.getDestConcept().getConceptName();
			
			if(!wnConcepsFile.findConcep(origConcep)){
				wnConcepsFile.addConcep(origConcep);
				wnConcepsFile.loadConcep();
			}
			if(!wnConcepsFile.findConcep(origDest)){
				wnConcepsFile.addConcep(origDest);
				wnConcepsFile.loadConcep();
			}
			
			
			mapPrepToRel.remove(preposition);
		}
		
		return meroHoloTypeProp;
	}
	
	private String linkRelMeroHolo(String type){
		String result = null;
		if(type!=null){
		//					has_holo_member						has_mero_member
		if(type.equalsIgnoreCase("Member holonym")||type.equalsIgnoreCase("Member meronym"))
			result = "has member";
		//					has_holo_part						has_mero_part
		else if(type.equalsIgnoreCase("Part meronym")||type.equalsIgnoreCase("Part holonym"))	
			result = "has part";
		//																has_holo_portion		
		else if(type.equalsIgnoreCase("Substance meronym")||type.equalsIgnoreCase("Substance holonym"))	
			result = "has portion";
		}
		return result;
	}
	private ArrayList<Synset>getSynsetsOfword(String lemma){
		return wnAccessfiles21.getAllSynsets(lemma);
	}

	private ArrayList<String>getSynsetsOffset(String lemma){
		ArrayList<String> offset=new ArrayList<String>();
		for (Synset synset : this.getSynsetsOfword(lemma))
			offset.add(synset.getOffset());
		return offset;
	}

	/**Obtiene la lista de synset(tupla del fichero data.pos) de todos los ficherosWN(adv,noun,adv,verb) donde exista un match para el offset "sense" 
	 * @param sense offset de sense para buscar en los ficheros WN(adv,noun,adv,verb)
	 * Acceso a  ficheros de WN par realizar la busqueda
	 * */
	private ArrayList<Synset>getSynsetLineFileWN(String sense){
//		..una Busqueda por los 4 tipos de ficheros donde puede apracer el sentido
		ArrayList<Synset> synsetsOrig=wnAccessfiles16.getAllFullSynsets(sense);
		if(synsetsOrig==null)
			synsetsOrig=wnAccessfiles21.getAllFullSynsets(sense);
		return synsetsOrig;
	}
	/**Obtiene la lista de Relation( parte de un synset, donde estan los pointers ) de todos los ficherosWN, donde exista un match para el offset "sense" 
	 * @param sense offset de sense para buscar en los ficheros WN(adv,noun,adv,verb)
	 * Acceso a  ficheros de WN par realizar la busqueda
	 * */
//	..buscada en la lista de sentidos
	private ArrayList<Relation>getRelationSynsetFileWN(String sense){

		ArrayList<Synset> synsets=getSynsetLineFileWN(sense);
		ArrayList<Relation> Relationsynsets= new ArrayList<Relation>();
		for(Synset synset:synsets) {
			Relationsynsets.addAll(synset.getPointers());
		}
		return Relationsynsets;
	}

	private String pointerToTypeNounetAll(String pointer){
		String category= null;
		if(pointer.equalsIgnoreCase("!"))
			category="Antonym";
		else if(pointer.equalsIgnoreCase("@"))
			category="Hypernym";
		else if(pointer.equalsIgnoreCase("@i"))
			category="Instance Hypernym";
		else if(pointer.equalsIgnoreCase("~"))
			category="Hyponym";
		else if(pointer.equalsIgnoreCase("~i"))
			category="Instance Hyponym";
		else if(pointer.equalsIgnoreCase("#m"))
			category="Member holonym";
		else if(pointer.equalsIgnoreCase("#s"))
			category="Substance holonym";
		else if(pointer.equalsIgnoreCase("#p"))
			category="Part holonym";
		else if(pointer.equalsIgnoreCase("%m"))
			category="Member meronym";
		else if(pointer.equalsIgnoreCase("%s"))
			category="Substance meronym";
		else if(pointer.equalsIgnoreCase("%p"))
			category="Part meronym";
		else if(pointer.equalsIgnoreCase("="))
			category="Attribute";
		else if(pointer.equalsIgnoreCase("+"))
			category="Derivationally related form";
		else if(pointer.equalsIgnoreCase(";c"))
			category="Domain of synset - TOPIC";
		else if(pointer.equalsIgnoreCase("-c"))
			category="Member of this domain - TOPIC";
		else if(pointer.equalsIgnoreCase(";r"))
			category="Domain of synset - REGION";
		else if(pointer.equalsIgnoreCase("-r"))
			category="Member of this domain - REGION";
		else if(pointer.equalsIgnoreCase(";u"))
			category="Domain of synset - USAGE";
		else if(pointer.equalsIgnoreCase("-u "))
			category="Member of this domain – USAGE";

		return category;
	}
	
	private String	pointerToTypeAdj(String pointer)
	{	String category= null;
	if(pointer.equalsIgnoreCase("&"))
		category="Similar to";
	else if(pointer.equalsIgnoreCase("<"))
		category="Participle of verb";
	else if(pointer.charAt(0)==92) //pointer=="\"
		category="Pertainym (pertains to noun";
	else if(pointer.equalsIgnoreCase("ˆ"))
		category="Also see";

	return category;
	}
	
	private String	pointerToTypeAdv(String pointer)
	{	String category= null;
	if(pointer.charAt(0)==92) //pointer=="\"
		category="Pertainym (pertains to noun";
	return category;
	}
	
	private String	pointerToTypeVerb(String pointer)
	{	String category= null;
	if(pointer.equalsIgnoreCase("*"))
		category="Entailment";
	else if(pointer.equalsIgnoreCase(">"))
		category="Cause";
	else if(pointer.equalsIgnoreCase("ˆ"))
		category="Also see";//observar que tambien existe en los adv
	else if(pointer.equalsIgnoreCase("$"))
		category="Verb Group";
	else if(pointer.equalsIgnoreCase("+"))
		category="Derivationally related form";				
	return category;
	}


	private String	findRelationType(char posOrig,char posDest,String pointer)
	{	String Type= null;
	//..despues de encontrar una relacion(pointer) entre origen/destino..	
	//..para obtener la categoría del tipo de relacion entre..
	//sustantivos o insteccion con los cojuntos(adj,adv,verb)
	if((posDest=='n'&&posOrig=='n')||(posDest=='a'&&posOrig=='a')||(posDest=='r'&& posOrig=='r')||(posDest=='v'&&posOrig=='v'))
	{
		Type= pointerToTypeNounetAll(pointer);
	}
	//para adjetivos solamente
	else if((posDest=='a'&&posOrig=='a')&& Type==null)
	{
		Type= pointerToTypeAdj(pointer);
	}		 	
	//para advervios solamente
	else if((posDest=='r'&&posOrig=='r')&& Type==null)
	{
		Type= pointerToTypeAdv(pointer);
	}	
	//para verbos solamente
	else if((posDest=='v'&&posOrig=='v')&& Type==null)
	{	
		Type= pointerToTypeVerb(pointer);
	}	
	return Type;
	}	

	private String	TypeToPointer(String type)
	{	
		String pointer=null;

		if(type.equalsIgnoreCase("Hypernym"))
			pointer="@";
		else if(type.equalsIgnoreCase("Instance Hypernym"))
			pointer="@i";
		else if(type.equalsIgnoreCase("Hyponym"))
			pointer="~";
		else if(type.equalsIgnoreCase("Instance Hyponym"))
			pointer="~i";
		else if(type.equalsIgnoreCase("Member holonym"))
			pointer="#m";
		else if(type.equalsIgnoreCase("Substance holonym"))
			pointer="#s";
		else if(type.equalsIgnoreCase("Part holonym"))
			pointer="#p";
		else if(type.equalsIgnoreCase("Member meronym"))
			pointer="%m";
		else if(type.equalsIgnoreCase("Part meronym"))
			pointer="%s";
		else if(type.equalsIgnoreCase("Hypernym"))
			pointer="%p";
		else if(type.equalsIgnoreCase("Attribute"))
			pointer="=";
		else if(type.equalsIgnoreCase("Derivationally related form"))
			pointer="+";
		else if(type.equalsIgnoreCase("Domain of synset - TOPIC"))
			pointer=";c";
		else if(type.equalsIgnoreCase("Member of this domain - TOPIC"))
			pointer="-c";
		else if(type.equalsIgnoreCase("Domain of synset - REGION"))
			pointer=";r";
		else if(type.equalsIgnoreCase("Member of this domain - REGION"))
			pointer="-r";
		else if(type.equalsIgnoreCase("Domain of synset - USAGE"))
			pointer=";u";
		else if(type.equalsIgnoreCase("Member of this domain – USAGE"))
			pointer="-u";
		//para adjetivos solamente
		if(type.equalsIgnoreCase("Similar to"))
			pointer="&";
		else if(type.equalsIgnoreCase("Participle of verb"))
			pointer="<";
		/*	else if(category=="\") //ver esto que el compilador no lo permite,sugerencia de poner el ASQII
				pointer="Pertainym (pertains to noun)";*/
		else if(type.equalsIgnoreCase("Also see"))
			pointer="ˆ";
		//para advervios solamente
		/*if(category=="Derived from adjective") //ver esto que el compilador no lo permite,sugerencia de poner el ASQII
				pointer="\";*/
		//para verbos solamente
		if(type.equalsIgnoreCase("Entailment"))
			pointer="*";
		else if(type.equalsIgnoreCase("Cause"))
			pointer=">";
		else if(type.equalsIgnoreCase("Also see"))
			pointer="ˆ";//observar que tambien existe en los adv
		else if(type.equalsIgnoreCase("Verb Group"))
			pointer="$";
		else if(type.equalsIgnoreCase("Derivationally related form"))
			pointer="+";			

		return pointer;
	}	

	private String InversePointer(String pointer)
	{	
		String invpointer="";
		if(pointer.equalsIgnoreCase("@"))
			invpointer="~";
		else if(pointer.equalsIgnoreCase("@i"))//las categorías k hay que buscarle la equivalencia en WN files
			invpointer="~i";
		else if(pointer.equalsIgnoreCase("~"))
			invpointer="@";
		else if(pointer.equalsIgnoreCase("~i"))//las categorías k hay que buscarle la equivalencia en WN files
			invpointer="@i";

		return invpointer;
	}
	/**Infiere una relacion_directa en tre dos sentidos apartir de su "relación_inversa"
	 * @param senseOrig sentido origen
	 * @param senseDest sentido destino
	 * @param  pointer tipo de relacion(por simbolo)entre el senseOrig y el sentido destino
	 * Acceso a  ficheros de WN par realizar la busqueda
	 * */	
	private boolean InferRelationInverse(String senseOrig,String senseDest,String pointer){
		boolean infer=false;
		//Obtengo todas las relaciones inversas(del destino al origen)que coincidieron con el offset "senseDest"
		ArrayList<Relation> RelationInv=getRelationSynsetFileWN(senseDest);
		int i=0;
		for (Relation relationInv : RelationInv){	
			i++;
			String synsetInv=relationInv.getTarget_offset();
			/* 
			 pointer="@" o “Hypernym”, significa destino es superclase del origen, de la forma(senseOrig:Clase-> @ ->senseDest:SuperClase),
			 ...y en la forma indirecta Invpointer="~" o“Hyponym”,donde destino es superclase del origen,(senseOrig:Clase<- ~ <-senseDest:SuperClase)
			
			 pointer="~" o “Hyponym”, significa origen es superclase del destino, de la forma(senseOrig:SuperClase-> ~ ->senseDest:Clase),
			...y en la forma indirecta Invpointer="@" o“Hypernym”,donde origen es superclase del destino(senseOrig:SuperClase<- @ <-senseDest:Clase)
			 */
			String synsetInvpointer=relationInv.getPointer_symbol();
			
			//Verifico si el sentidoInv en relación inversa coincide con "senseOrig" y ademas que la relación sea inversa a la relacion directa "pointer"
			if(	(synsetInv.equalsIgnoreCase(senseOrig))	&&	(synsetInvpointer.equalsIgnoreCase(InversePointer(pointer) )	) )
				infer=true;
		}
		return infer;
	}

	/**Obtiene la lista de sentidos destinos relacionados por "pointer" con senseOrig
	 * @param senseOrig sentido origen
	 * @param tipo de relacion(por simbolo)entre el senseOrig y la "lista de sensesDest"
	 * Acceso a  ficheros de WN par realizar la busqueda
	 * */
	private ArrayList<String> relationSensesDest(String senseOrig,String findpointer){
		ArrayList<String> relationSensesDest= new ArrayList<String>();

		//Obtengo todas las relaciones de todos los synset en los ficheros WN que coincidieron con el offset "senseOrig"
		ArrayList<Relation> RelationOrig=getRelationSynsetFileWN(senseOrig);
		int i=0;
		//..ahora continuo chequeando las relaciones para buscar los sentidos destinos relacionados por "pointer" con "senseOrig".
		for (Relation relation : RelationOrig){
			i++;
			String synsetDest=relation.getTarget_offset();
			String pointer=relation.getPointer_symbol();
			//si se encontro relación,de forma directa,se adiciona
			if(pointer.equalsIgnoreCase(findpointer))
				relationSensesDest.add(synsetDest);
			//si no se encontro, inferir "la relacion directa" por la relación inversa a partir de "synsetDest"
			else if(InferRelationInverse(senseOrig,synsetDest,findpointer)){
				relationSensesDest.add(synsetDest);
			}
		}
		return relationSensesDest;
	}

	/**Verifica si entre dos sentidos existe alguna relación indirecta(category)hasta una profundidad de nivel 3 
	 * @param senseDest sentido destino
	 * @param senseOrig sentido origen
	 * Acceso a  ficheros de WN par realizar la busqueda
	 * */
	private String findIndirctRelation(String senseOrig, String senseDestiny,String type)
	{		
		String TYPE= null;
		
	String pointer=TypeToPointer(type);
	//Obtengo las relaciones directa(1er Nivel) entre el origen y el destino
	ArrayList<String> firstLevel = relationSensesDest(senseOrig,pointer);	//1er Nivel

	//Busqueda en porfundida de una Relacción_Iindirecta partir de  las relaciones directa(1er Nivel)
	int first = 0,sec=0;
	for (String senseItem : firstLevel){
		first++;
		if(firstLevel.contains(senseDestiny)&&first<1)
			TYPE=type;
		else{
			ArrayList<String> secondLevel = relationSensesDest(senseItem,pointer);//2nd Nivel
			if(secondLevel.contains(senseDestiny))
				TYPE=type;
			else{//Busqueda en porfundida de una Relacción_Iindirecta a partir de  las relaciones (2 Nivel)
				for (String secondItem : secondLevel){
					sec++;
					ArrayList<String> thirdLevel = relationSensesDest(secondItem,pointer);//3er Nivel
					if(thirdLevel.contains(senseDestiny))
						TYPE=type;
				}	
			}
		}
	}
	return TYPE;
	}
	/**Verifica si entre dos sentidos existe alguna relación(pointer)
	 * @param senseDest sentido destino
	 * @param senseOrig sentido origen
	 * Acceso a  ficheros de WN par realizar la busqueda
	 * */
	private String findDirctRelation(String senseOrig, String senseDest)
	{	boolean equal=false;
	String type= null;
	String pointer=null;
	char posOrig=' ';
	char posDest=' ';
	Synset synsetItem;
	Relation relationItem;

	//busco en los registros de todos los ficheros, de WN2.1 y WN 1.6, que puedan contener al "sentidoOrig" ...
	ArrayList<Synset> synsetsOrig= getSynsetLineFileWN(senseOrig);

	//..buscada en la lista de sentidos
	Iterator itersynset=synsetsOrig.iterator();
	while (itersynset.hasNext()&& !equal) {
		synsetItem=(Synset) itersynset.next();
		posOrig=synsetItem.getPos();

		//..ahora continuo chequeando todas las posibles relaciones para buscar un enlace con "senseDest"	...
		ArrayList<Relation> RelationOrig=synsetItem.getPointers();

		//..busqueda en las relaciones del "sentidoOrig"
		Iterator iter = RelationOrig.iterator();
		int i=0;
		while(iter.hasNext()&&!equal) {	
			i++;

			//..si el offset de un sentidodestino aparace en las relaciones que tiene "sentidoOrig" ...
			relationItem=(Relation) iter.next();
			if(relationItem.getTarget_offset().equalsIgnoreCase(senseDest)){
				//...entonces se pregunta que tipo de relacion es..
				equal=true;
				pointer=relationItem.getPointer_symbol();
				posDest=relationItem.getTarget_pos();							
			}
		}
	}	
	if(equal)
		type=  findRelationType(posOrig,posDest,pointer);
	return  type;
	}


	private ArrayList<String> synonymBySense(String Sense){

		ArrayList<String> synonyms = new ArrayList<String>();
		for (Relation relation : getRelationSynsetFileWN(Sense)){

			char posdest=relation.getTarget_pos();
			String pointdest=relation.getPointer_symbol();

			if(posdest=='a'&& pointerToTypeAdj(pointdest).equalsIgnoreCase("Similar to"))
				synonyms.add(relation.getTarget_offset());
		}
		return synonyms;
	}

	private ArrayList<String> synonymByConcept(String conceptName){

		ArrayList<String> synonyms = new ArrayList<String>();

		ArrayList<String> senses= getSynsetsOffset(conceptName);

		for (String senseItem : senses) {

			ArrayList<String> synonymSenses = synonymBySense(senseItem);

			for (String synItem : synonymSenses) {

				ArrayList<String> synTerm = getSynsetsOffset(synItem);
				synonyms.addAll(synTerm);
			}
		}		
		return synonyms;
	}

//::::::::::::::::::::::::::::::::::::::Trabajo con Repositorio de Ontología:::::::::::::::::::::::::::::::::::::::::::::::::::::::
	
	/**
	 * @author Alejandro
	 * Carga todos los ficheros .owl del repositorio de ontologias 
	 */
	public void loadOWLRepository()//Proceso en batch para cada ontología del repositorio
	{
		if(this.owlRepositoryLoaded==false ){//El repositorio de ontologías se carga solo una vez
			this.setOwlRepositoryLoaded(true);
			File[] owls = (new File(this.owlRepositoryPath)).listFiles();//Carga las ontologías para convertirlas en mapas
			if(owls != null)
				for (File file : owls) {
					String filepath = file.getAbsolutePath();//aki se obtine el nombre del fichero
					//check if .owl
					if(filepath.length()>4 && filepath.substring(filepath.length()-4, filepath.length()).equalsIgnoreCase(".owl")){
						this.owlParse.setOwlRepositoryPath(filepath);//	Cargar OWL
						this.owlParse.loadOWLtoMap();//Finalmente este es el metodo que inicaliza todo el proceso para formalizar el mapa
					}
				}
		}
		owlParse.loadOWLMapsSimilars(this.map);
		if(this.map.getRepositoryRelv()>0)
			result.setRelevance(map.getRepositoryRelv());
		owlParse.loadOWLConjunctions();
	}
	
	/**@author Alejandro R.C
	 * @return Carga los elementos y preposiciones de los ficheros .owl
	 */
	public void loadOWLRelations()
	{
		if(mapPrepToRel.size()>0){
			loadOWLRepository();

			this.mapPrepToRep.addAll(mapPrepToRel);

			owlClassSubClass=owlRel("ClaseSubClase");
			owlInstance=owlRel("Instance");
			owlObjectProperty=owlRel("ObjectProperty");
			owlRestricProperty=owlRel("Property");
		}
		
	}
	public ArrayList<CmapProposition> owlRel(String type){
		ArrayList<CmapProposition> owlprep = new ArrayList<CmapProposition>();
		ArrayList<CmapProposition>prepLoad = new ArrayList<CmapProposition>();
		ArrayList<CmapProposition>badpreps = new ArrayList<CmapProposition>();
		boolean prepgood;
		boolean badadd;
		ArrayList<CmapProposition> OWLprepositions = getOwlRel(type);
		if(OWLprepositions!=null){

			for (CmapProposition prepItem : mapPrepToRep){
				boolean existOWLRel=false;
				prepgood=false;
				badadd=false;

				if( prepItem.getOrigConcept()!=null && (!prepItem.getOrigConcept().getConceptName().equalsIgnoreCase("")) ){//Verificar que el concepto origen tienen nombre
					String	concpOrig = prepItem.getOrigConcept().getConceptName();

					if(prepItem.getDestConcept()!=null && (!prepItem.getDestConcept().getConceptName().equalsIgnoreCase("")) ){//Verificar que el concepto dest tienen nombre
						String	concpDest = prepItem.getDestConcept().getConceptName();

						ArrayList<String> concpOrigroots=new ArrayList<String>();
						this.FAcess.getRoots(concpOrig, concpOrigroots);//Obtener todas las raices o palabras primitivas del origen(por el problema de los plurales y conjugaciones)
						if(concpOrigroots.size()>0){ 

							Iterator<String> iterorig=concpOrigroots.iterator();
							while (iterorig.hasNext()&&!existOWLRel&&!prepgood){
								String rootorig=iterorig.next();

								String normalConcepOrig=Sintax.normalizeTerms(rootorig);// Se normaliza(se quitan los espaciones,guiones, etc y se dejaen "texto plano") el termino raiz... 
								if(owlParse.getOwlNormalizedRepConcepsFile().findConcep(normalConcepOrig)){//...si este se encuentra en el repositorio normalizado, es posible q exista una relación, sino...(ver else)

									ArrayList<String> concpDestroots=new ArrayList<String>();
									this.FAcess.getRoots(concpDest, concpDestroots);//Obtener todas las raices o palabras primitivas del destino(por el problema de los plurales y conjugaciones)
									if(concpDestroots.size()>0){

										Iterator<String> iterdest=concpDestroots.iterator();
										while (iterdest.hasNext()&&!existOWLRel&&!prepgood){
											String rootdest = iterdest.next();

											String normalConcepDest=Sintax.normalizeTerms(rootdest);// Se normaliza(se quitan los espaciones,guiones, etc y se dejaen "texto plano") el termino raiz...  
											if(owlParse.getOwlNormalizedRepConcepsFile().findConcep(normalConcepDest)){//...si este se encuentra en el repositorio normalizado, es posible q exista una relación, sino...(ver else)
												prepgood=true;
									
												if(findOwlMatch(rootorig, rootdest,type,OWLprepositions)){
													existOWLRel=true;
													CmapConcept orig= new CmapConcept(concpOrig);
													CmapConcept dest= new CmapConcept(concpDest);
													CmapLink link= new CmapLink(prepItem.getLink().getLinkName());//ver que f.e se le debe poner, la de owlrepository o la del mapa
													if(type.equalsIgnoreCase("ClaseSubClase"))
														link.setCategory("Subclasificacion");
													else if(type.equalsIgnoreCase("Instance"))
														link.setCategory("Instancia");
													else if(type.equalsIgnoreCase("ObjectProperty"))
														link.setCategory("Propiedad");
													else if(type.equalsIgnoreCase("Property"))
														link.setCategory("Valor de Propiedad");
													CmapProposition prep= new CmapProposition();
													prep.setType("");
													prep.setOrigConcept(orig);
													prep.setLink(link);
													prep.setDestConcept(dest);
													owlprep.add(prep);
													prepLoad.add(prepItem);
												}
											}
										}
										if(!prepgood){
											badpreps.add(prepItem);//...sino se encuentra el termino normalizado en el Reposit. OWL no tiene sentido volverha analizar la proposición
											badadd=true;
										}
									}
								}
							}
							if(!prepgood && !badadd)
								badpreps.add(prepItem);//...sino se encuentra el termino normalizado en el Reposit. OWL no tiene sentido volverha analizar la proposición
						}
					}
				}
			}
			for (CmapProposition preposition : badpreps){ 
				mapPrepToRep.remove(preposition);
			}
			for (CmapProposition preposition : prepLoad){
				mapPrepToRel.remove(preposition);
				mapPrepToRep.remove(preposition);
			}
		}
		return owlprep;

	}
	public 	ArrayList<CmapProposition> getOwlRel(String typePrep){
		ArrayList<CmapProposition> OWLprepositions = new ArrayList<CmapProposition>();

		
		if(typePrep.equalsIgnoreCase("ClaseSubClase"))
			OWLprepositions=owlParse.getOwlSubclass();
		else if(typePrep.equalsIgnoreCase("Instance"))
			OWLprepositions=owlParse.getOwlInstannce();
		else if(typePrep.equalsIgnoreCase ("ObjectProperty"))
			OWLprepositions=owlParse.getOwlObjectproperty();
		else if(typePrep.equalsIgnoreCase("Property"))
			OWLprepositions=owlParse.getOwlProperty();

		if(OWLprepositions.size()==0)	
				 OWLprepositions=null;
		
		return OWLprepositions;
	}
	

	/**@author Alejandro R.C
	 * @return Busca en preposiciones específicas(ya sea ClaseSubClase,Instancia...etc) del repositorio de los ficheros .owl
	 * una correspondencia con los conceptos del mapa si este ocurre se puede inferir cierta semantica.
	 */			
	public boolean findOwlMatch(String concpOrig,String concpDest,String typePrep,	ArrayList<CmapProposition>  OWLprepositions){
		boolean found=false;

		if(typePrep.equalsIgnoreCase("ClaseSubClase")){
			String temp="";
			temp=concpOrig;//..se invierten los valores para invertir los sentidos con los conceptos en su forma original...
			concpOrig=concpDest;//...En el caso "ClaseSubClase",recordar que el sentido en la ontología va de la forma Subclase-f.e-Clase(ej:clase subclaseof...)
			concpDest=temp;//...(ej.Algae is a Plant)por eso se invierten los origenes y sentidos...
			
		}
		else if(typePrep.equalsIgnoreCase("Instance")){
				String temp="";
				temp=concpOrig;//...En el caso de "Instancia",  recordar que el sentido en la ontología va de la forma Instancia-f.e-Clase
				concpOrig=concpDest;//...por eso se invierten los origenes y sentidos...esto no se cumple en los demas tipos de relaciones
				concpDest=temp;
		}
		
		int level=1;//...Empieza a buscar recursivamente en el 1er nivel.. 
		if(findRecursiveOwlMatch("",concpDest,concpOrig,typePrep,OWLprepositions,level))
			found=true;

		return found;
	}
	
	/**@author Alejandro R.C
	 * @return Busca en la lista de proposiciones de un tipo las correspondencias entre los conceptos de las
	 * proposiciones del mapa y los conceptos de las proposiciones del Rep OWL(si existe alguna correspondencia);
	 * en  el caso de relaciones jerarquicas busca recursivamente hasta el nivel 3
	 * @param interConcep ...Concepto-Origen o Concepto-Dest intermedio en la relación indirecta entre los conceptos target
	 * @param targetconcpOrig...es el origen inicial mediente el cual se comienza a buscar una relación directa o indirecta con el destino inicial 
	 * @param targetconcpDest...es el destino  inicial el cual se utiliza para buscar una relación directa o indirecta con el origen inicial
	 * @param typePrep... el tipo de relación entre terminos solo hay recursividad para las ClaseSubClase
	 * @param OWLprepositions... la lista de proposiciones de un tipo
	 * @param level... el nivel jerarquico en la relación entre terminos, solo llega hasta el nivel 3
	 */
	public boolean findRecursiveOwlMatch(String interConcep,String targetconcpOrig,String targetconcpDest,String typePrep,ArrayList<CmapProposition>  OWLprepositions, int level){
		
		boolean found=false;
	//	String mapowner="";
		
		CmapProposition owlprep=new CmapProposition();
		
		if(level==1)
			interConcep=targetconcpOrig;
		
		Iterator iter= OWLprepositions.iterator();
		while (iter.hasNext()&&!found) {
			String catgTagOrig="";
			String catgTagDest="";
			owlprep = (CmapProposition) iter.next();
			
			if( findConcepOWLRelMatch(interConcep,owlprep.getOrigConcept().getConceptName(),targetconcpOrig,targetconcpDest) )
				catgTagOrig=owlprep.getOrigConcept().getCatgTag();
			if( findConcepOWLRelMatch(targetconcpDest,owlprep.getDestConcept().getConceptName(),targetconcpOrig,targetconcpDest) )
				catgTagDest=owlprep.getDestConcept().getCatgTag();
				
			 if(!catgTagOrig.equalsIgnoreCase("") && catgTagDest.equalsIgnoreCase("") && level==1)	
				repOWLIncompletePropMatch.addConcepTagCatg(targetconcpOrig, catgTagOrig);
			 
			else if(catgTagOrig.equalsIgnoreCase("") && !catgTagDest.equalsIgnoreCase(""))
				repOWLIncompletePropMatch.addConcepTagCatg(targetconcpDest, catgTagDest);
						
			else if(!catgTagOrig.equalsIgnoreCase("") && !catgTagDest.equalsIgnoreCase(""))
					found=true;
				
			else if( level<=2 && typePrep.equalsIgnoreCase("ClaseSubClase") && !found && !catgTagOrig.equalsIgnoreCase("")){//Solo llevar hasta level 3 para no hacer una iteración adicional fuera del proposito
					interConcep=owlprep.getDestConcept().getConceptName();
					level++;//...cuando (level=2)++, ya se esta en level 3 porlo que no se se itera más
					if(findRecursiveOwlMatch(interConcep, targetconcpOrig,targetconcpDest, typePrep,OWLprepositions,level))
						found=true;
				}
		}
		return found;
	}
		
	
	public boolean findConcepOWLRelMatch(String concp,String concpPrep,String targetconcpOrig,String targetconcpDest){
		boolean found=false;
		
		String normConcep= Sintax.normalizeTerms(concp);
		String normConcpPrep= Sintax.normalizeTerms(concpPrep);
		if(normConcep.equalsIgnoreCase(normConcpPrep)){
			found= true;
//			...Solamente se guarda en el fichero los conceptos target que son los conceptos originales	del MC y no un concepto intermedio de la relación indirecta		
			if((concp.equalsIgnoreCase(targetconcpOrig)||concp.equalsIgnoreCase(targetconcpDest))&&!repOWLMatchConcepsFile.findConcep(concp)){
				repOWLMatchConcepsFile.addConcep(concp);
				repOWLMatchConcepsFile.loadConcep();
			}
		}

		return found;		
	}
	
//	::::::::::::::::::::::::::::::::::::::Trabajo con Opencyc::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	public void loadOpenCycRelations(){
		if(mapPrepToRel.size()>0){
			OpenCyc= new OpenCycAccess(OpenCycpath);
			this.CycHerarchys= cycHerarchys();
			this.CycInstances=cycInstances();
		}
	}
	
	/**@author Alejandro R.C
	 * @return Por cada preposición del mapa a procesar verifica si entre sus conceptos(origen-dest)existe alguna relación de jerarquia en OpenCyc..
	 */
	public ArrayList<CmapProposition> cycHerarchys(){
		ArrayList<CmapProposition> cycherarchys = new ArrayList<CmapProposition>();
		ArrayList<CmapProposition>prepLoad = new ArrayList<CmapProposition>();
		int i=0;
		for (CmapProposition prepItem : mapPrepToRel){
			boolean existCycRel=false;
			i++;
			if(prepItem.getOrigConcept()!=null){
				String	concpOrig = prepItem.getOrigConcept().getConceptName();

				if(prepItem.getDestConcept()!=null){
					String	concpDest = prepItem.getDestConcept().getConceptName();
					
					if((!concpOrig.equalsIgnoreCase("")) && (!concpDest.equalsIgnoreCase(""))){
						
						ArrayList<String> concpOrigroots=new ArrayList<String>();
						this.FAcess.getRoots(concpOrig, concpOrigroots);//Obtener todas las raices o palabras primitivas del origen
						if(concpOrigroots.size()>0){ 
							
							ArrayList<String> concpDestroots=new ArrayList<String>();
							this.FAcess.getRoots(concpDest, concpDestroots);//Obtener todas las raices o palabras primitivas del destino
							if(concpDestroots.size()>0){
							
								Iterator<String> iterorig=concpOrigroots.iterator();
								while (iterorig.hasNext()&&!existCycRel){
									String rootorig=iterorig.next();
									
									Iterator<String> iterdest=concpDestroots.iterator();
									while (iterdest.hasNext()&&!existCycRel){
										String rootdest = iterdest.next();
										
										if(OpenCyc.getHierarchyRelation(rootorig, rootdest)){//Verificar si existe alguna relación jerarquica en Cyc
											existCycRel=true;
											CmapConcept orig= new CmapConcept(concpOrig);
											CmapConcept dest= new CmapConcept(concpDest);
											CmapLink link= new CmapLink(prepItem.getLink().getLinkName());
											link.setCategory("Subclasificacion");
											CmapProposition prepHerchy= new CmapProposition();
											prepHerchy.setOrigConcept(orig);
											prepHerchy.setLink(link);
											prepHerchy.setDestConcept(dest);
											prepHerchy.setType("");
											cycherarchys.add(prepHerchy);
											prepLoad.add(prepItem);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		for (CmapProposition preposition : prepLoad) 
			mapPrepToRel.remove(preposition);
		
		return cycherarchys;
	}
	/**@author Alejandro R.C
	 * @return Por cada preposición del mapa a procesar verifica si entre sus conceptos(origen-dest)existe alguna relación de instancia en OpenCyc..
	 */
	public ArrayList<CmapProposition> cycInstances(){
		ArrayList<CmapProposition> cycinstances = new ArrayList<CmapProposition>();
		ArrayList<CmapProposition>prepLoad = new ArrayList<CmapProposition>();

		for (CmapProposition prepItem : mapPrepToRel){
			boolean existCycRel=false;

			if(prepItem.getOrigConcept()!=null){
				String	concpOrig = prepItem.getOrigConcept().getConceptName();

				if(prepItem.getDestConcept()!=null){
					String	concpDest = prepItem.getDestConcept().getConceptName();

					if((!concpOrig.equalsIgnoreCase("")) && (!concpDest.equalsIgnoreCase(""))){

						ArrayList<String> concpOrigroots=new ArrayList<String>();
						this.FAcess.getRoots(concpOrig, concpOrigroots);//Obtener todas las raices o palabras primitivas del origen...
						if(concpOrigroots.size()>0){ 

							ArrayList<String> concpDestroots=new ArrayList<String>();
							this.FAcess.getRoots(concpDest, concpDestroots);//Obtener todas las raices o palabras primitivas del destino...
							if(concpDestroots.size()>0){

								Iterator<String> iterorig=concpOrigroots.iterator();
								while (iterorig.hasNext()&&!existCycRel){//Buscar por cada rootorig una rootdest...
									String rootorig=iterorig.next();

									Iterator<String> iterdest=concpDestroots.iterator();
									while (iterdest.hasNext()&&!existCycRel){//Buscar por cada rootdest una relacion en Cyc...
										String rootdest = iterdest.next();

										if(OpenCyc.getInstanceRelation(rootorig, rootdest)){//Verificar si existe alguna relación de instancia en Cyc
											existCycRel=true;
											CmapConcept orig= new CmapConcept(concpOrig);
											CmapConcept dest= new CmapConcept(concpDest);
											CmapLink link= new CmapLink(prepItem.getLink().getLinkName());
											link.setCategory("Instancia");
											CmapProposition prepInst= new CmapProposition();
											prepInst.setOrigConcept(orig);
											prepInst.setLink(link);
											prepInst.setDestConcept(dest);
											prepInst.setType("");
											cycinstances.add(prepInst);
											prepLoad.add(prepItem);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		for (CmapProposition preposition : prepLoad) 
			mapPrepToRel.remove(preposition);

		return cycinstances;
	}

//:::::::::::::::::::::::::::::::::::::::::::::::::Reglas de Inferencia::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	
	public void codifyStatistics(int rule,int conjunctID){	
		
		switch (conjunctID) {//si se codifica por algun conjuto, es que se ha inferido alguna relación por "semantica"
		case 1:	result.setHypeHypo(result.getHypeHypo() +1);result.setSemanticCod(result.getSemanticCod()+1);break;//conjunctID=1;
		case 2:	result.setCycHierarchy(result.getCycHierarchy() +1);result.setSemanticCod(result.getSemanticCod()+1);break;//	conjunctID=2;
		case 3:	result.setOwlClassSubClass(result.getOwlClassSubClass() +1);result.setSemanticCod(result.getSemanticCod()+1);break;//	conjunctID=3;
		case 4:	result.setCycInstance(result.getCycInstance() +1);result.setSemanticCod(result.getSemanticCod()+1);break;//	conjunctID=4;
		case 5:	result.setOwlInstance(result.getOwlInstance() +1);result.setSemanticCod(result.getSemanticCod()+1);break;//	conjunctID=5;
		case 6:	result.setMeroHolo(result.getMeroHolo() +1);result.setSemanticCod(result.getSemanticCod()+1);break;//	conjunctID=6;
		case 7:	result.setMeroHoloType(result.getMeroHoloType() +1);result.setSemanticCod(result.getSemanticCod()+1);break;//	conjunctID=7
		case 8:	result.setOwlObjectProperty(result.getOwlObjectProperty() +1);result.setSemanticCod(result.getSemanticCod()+1);break;//	conjunctID=8
		case 9:	result.setOwlRestricProperty(result.getOwlRestricProperty() +1);result.setSemanticCod(result.getSemanticCod()+1);break;//	conjunctID=9
		default:
			break;
		}
		switch (rule) {//si se codifica por alguna regla, se registra que se ha inferido una relacion (ya sea por "semantica" o f.e)
		case 1:	result.setCodRul1(result.getCodRul1()+1); result.setCantCodificadas(result.getCantCodificadas()+1);break;
		case 2:	result.setCodRul2(result.getCodRul2()+1); result.setCantCodificadas(result.getCantCodificadas()+1); break;
		case 3:	result.setCodRul3(result.getCodRul3()+1); break;
		case 4:	result.setCodRul4(result.getCodRul4()+1); break;
		case 5:	result.setCodRul5(result.getCodRul5()+1); break;
		case 6:	result.setCodRul6(result.getCodRul6()+1); result.setCantCodificadas(result.getCantCodificadas()+1); break;
		case 7:	result.setCodRul7(result.getCodRul7()+1); result.setCantCodificadas(result.getCantCodificadas()+1); break;
		case 8:	result.setCodRul8(result.getCodRul8()+1); result.setCantCodificadas(result.getCantCodificadas()+1); break;
		case 9:	result.setCodRul9(result.getCodRul9()+1);/*result.setCantCodificadas(result.getCantCodificadas()+1)*/;break;
		case 10:result.setCodRul10(result.getCodRul10()+1);/* result.setCantCodificadas(result.getCantCodificadas()+1)*/; break;
		case 11:result.setCodRul11(result.getCodRul11()+1);/*result.setCantCodificadas(result.getCantCodificadas()+1);*/; break;
		default:
			break;
		}
	}
	
	/**
	 * Regla 1 (salience = 8)//arreglar
	 * Analiza solamnete las semantica por las f.e
	 * */
	public void Rule1(ArrayList<CmapProposition> prepositions){
		ArrayList<CmapProposition>resCod = new ArrayList<CmapProposition>();
		String catSubgeneric="Subclasificacion";//.... es mejor una variable apra cambiar la comparacion de igles-español(Subclasification-Subclasificacion)..etc..etc
		
		for (CmapProposition prepItem : prepositions){
			CmapProposition inverse = null;
			
			String linkName = prepItem.getLink().getLinkName();
			String category = linkCategory(linkName.trim());//cambiar metodo
//								Subclasificación
			if(category.equalsIgnoreCase(catSubgeneric)){
				prepItem.getLink().setCategory(catSubgeneric);
				
				if(linkName.equalsIgnoreCase("is one")||linkName.equalsIgnoreCase("is a")||linkName.equalsIgnoreCase("type of")||linkName.equalsIgnoreCase("a type of")||linkName.equalsIgnoreCase("are")||linkName.equalsIgnoreCase("that are")){
					 inverse = new CmapProposition(prepItem.getDestConcept(),prepItem.getLink(),prepItem.getOrigConcept());
				}
				
				if(inverse !=null)
				 C_CS.add(inverse);
				else
				 C_CS.add(prepItem);	
				
				resCod.add(prepItem);
				result.setLinkCodify(result.getLinkCodify()+1);
				codifyStatistics(1, 0);//agregar estadistica a los a los resultados
			}
		}//eliminar las que proceso
		for (CmapProposition preposition : resCod) 
			prepositions.remove(preposition);
	}

	/**
	 * Regla 2 (salience = 8)
	 * Analiza la semantica de los conjuntos(Clase-SubClase)
	 * */
	public void Rule2(ArrayList<CmapProposition> prepositions){
	
		ArrayList<CmapProposition> resCod = new ArrayList<CmapProposition>();
		String 	 catSubgeneric="Subclasificacion";//.... es mejor una variable apra cambiar la comparacion de igles-español(Subclasification-Subclasificacion)..etc..etc="Subclasificacion";//.... es mejor una variable apra cambiar la comparacion de igles-español(Subclasification-Subclasificacion)..etc..etc
		String catInsgeneric="Instancia";//.... es mejor una variable apra cambiar la comparacion de igles-español(Instance-Instancia)..etc..etc
		
		for(CmapProposition prepItem : prepositions){
			int conjunctID=0;
			CmapProposition prepTemp= null;
			String linkName = prepItem.getLink().getLinkName();
			String category = linkCategory(linkName.trim());
//	SI						Ninguna			NO es ni Subclasficación 			ni Instancia 					ENTONCES analizar la semantica de los conjuntos(Clase-SubClase)				
			if( category.equalsIgnoreCase("") ||	( !category.equalsIgnoreCase(catSubgeneric) && (!category.equalsIgnoreCase(catInsgeneric) ))){
				
				/*.............................busco la directa.......................*/	
				if(findProp(prepItem,this.hypeHypoRels)!=null)
					conjunctID=1;
				else if(findProp(prepItem,this.CycHerarchys)!=null)
					conjunctID=2;
				else if(findProp(prepItem,this.owlClassSubClass)!=null)
					conjunctID=3;
				
				if(conjunctID>0){//...si se codifico al menos algun tipo de relacion por algun conjunto de los 9 posibles...
					prepItem.getLink().setCategory(catSubgeneric);
					prepTemp=prepItem;
				}
				else{/*................busco la inversa.......................*/

					CmapProposition propInverse = new CmapProposition(prepItem.getDestConcept(),prepItem.getLink(),prepItem.getOrigConcept());
					if(findProp(propInverse,this.hypeHypoRels)!=null)
						conjunctID=1;
					else if(findProp(propInverse,this.CycHerarchys)!=null)
						conjunctID=2;
					else if(findProp(propInverse,this.owlClassSubClass)!=null)
						conjunctID=3;
					
					if(conjunctID>0){//...si se codifico al menos algún tipo de relacion por algun conjunto de los 9 posibles...
						prepItem.getLink().setCategory(catSubgeneric);
						prepTemp=propInverse;
					}
//				... Si se encontro la relación inversa en algun conjunto quiere decir que la directa es clase-subclase, y esa es la que se codifica
				}
				if(prepTemp!=null){//...alguna preposición inferida por algún conjunto(Clase-SubClase);no interesa la catgegoría de la f.e...			

					if(category.equalsIgnoreCase("")){//..si la categoría no fue encontrada adicionar la nueva f.e al fichero link por su categoría correspondiente..
						prepTemp.getLink().setCategory(catSubgeneric);
						insertLinkWord(prepTemp.getLink().getLinkName(),catSubgeneric);
					}
					C_CS.add(prepTemp);
					resCod.add(prepItem);
					codifyStatistics(2, conjunctID);//agregar estadistica a los a los resultados
				}
			}
		}
		for (CmapProposition preposition : resCod)//eliminar las que proceso 
			prepositions.remove(preposition);
	}

	/**
	 * Regla 3 (salience = 6)
	 * */
	public void Rule3(){//Buscar todas las uniones posibles
		for (CmapProposition prepositionItem : C_CS) {

			Union unionItem = new Union(prepositionItem.getOrigConcept());
			unionItem.getUnionList().add(prepositionItem.getDestConcept());

			if(findIntersection(unionItem.getConceptUnion().getConceptName(), unionClass)== false){
				int i=0;
				for(CmapProposition prepDest: C_CS) {
					i++;
					if(unionItem.getConceptUnion().getConceptName().equalsIgnoreCase(prepDest.getOrigConcept().getConceptName())){

						if(!(prepositionItem.getDestConcept().getConceptName().equalsIgnoreCase(prepDest.getDestConcept().getConceptName()))){

							unionItem.getUnionList().add(prepDest.getDestConcept());
						}
					}
				}
				if((unionItem.getUnionList().size() > 1)){
					unionClass.add(unionItem);
					codifyStatistics(3,0); //agregar estadistica a los a los resultados
					//result.setSemanticCod(result.getSemanticCod()+1);
				}
			}
		}
	}

	/**
	 * Regla 4 (salience = 6)
	 * A pesar de que se trabaja con elementos union 
	 * es para  obtener inferencias
	 * */
	public void Rule4() {

		// Identificacion de clases intersecciones

		for (CmapProposition prepositionItem : C_CS) {

			Union intersectionItem = new Union(prepositionItem.getOrigConcept());
			intersectionItem.getUnionList().add(prepositionItem.getDestConcept());

			if(findIntersection(intersectionItem.getConceptUnion().getConceptName(), intersectionClass)== false){

				for(CmapProposition propDest: C_CS) {

					if(intersectionItem.getConceptUnion().getConceptName().equalsIgnoreCase(propDest.getDestConcept().getConceptName())){

						if(!(prepositionItem.getDestConcept().getConceptName().equalsIgnoreCase(propDest.getOrigConcept().getConceptName()))){

							intersectionItem.getUnionList().add(propDest.getOrigConcept());
						}
					}
				}
				if((intersectionItem.getUnionList().size() > 1)){
					intersectionClass.add(intersectionItem);
					codifyStatistics(4,0);//agregar estadistica a los a los resultados
					//result.setSemanticCod(result.getSemanticCod()+1);
				}
			}
		}
	}

	/**
	 * Regla 5 (salience = 2)
	 * */
	public void Rule5() {
		boolean found = false;
		CmapProposition resultProp = null;

		for (CmapProposition prepItem : C_CS) {

			CmapProposition propTemp = new CmapProposition(prepItem.getDestConcept(),prepItem.getLink(),prepItem.getOrigConcept());
			resultProp = findProp(propTemp, getC_CS());

			if(resultProp != null){
				// no hay una proposicion con concepto destino igual al origen de la proposicion analizada
				// verificar la existencia de la propiedad
				ArrayList<CmapProposition> prep = getC_CPVHasValue();
				CmapProposition pResult = null;

				for(int index = 0; (index < prep.size()) && !found; index++){

					pResult = prep.get(index);

					if(pResult.getOrigConcept().getConceptName().equalsIgnoreCase(prepItem.getOrigConcept().getConceptName()))
						found = true;
				}
				if(found == true){
					found = false;
					IntersectionProperty intersect = new IntersectionProperty(prepItem.getOrigConcept(),prepItem.getDestConcept(),
																		pResult.getLink().getLinkName(),pResult.getDestConcept());
					intersectionClassProperty.add(intersect);
					codifyStatistics(5,0);//agregar estadistica a los a los resultados
					//result.setSemanticCod(result.getSemanticCod()+1);
				}
			}		
		}
	}

	/**
	 * Regla 6 (salience = 8)
	 * */
	public void Rule6(ArrayList<CmapProposition> prepositions){

		ArrayList<CmapProposition>	resCod = new ArrayList<CmapProposition>();
		int i=0;
		String catInsgeneric="Instancia";//.... es mejor una variable para cambiar la comparacion de igles-español(Instance-Instancia)..etc..etc
		for (CmapProposition prepItem : prepositions) {
			i++;
			int conjunctID=0;
			CmapProposition prepTemp1= null;
			String linkName = prepItem.getLink().getLinkName();
			String category = linkCategory(linkName.trim());
			CmapProposition prepInverse = new CmapProposition(prepItem.getDestConcept(),prepItem.getLink(),prepItem.getOrigConcept());
			
//............................................Buscar inferir por f.e...........................................................................a)
//					f.e es Instancia			NO se encontro(directa o inderectamente) en los conjuntos semanticos de Clase-SubClase... 	Ni el concepto destino es "subclase",codificado ant por f.e en R1
			if(category.equalsIgnoreCase(catInsgeneric) && (findProp(prepItem, CRSClassSubClass)==null) && (findProp(prepInverse, CRSClassSubClass)==null) && (findDestConcep(prepItem.getDestConcept(), C_CS)==null) ){
//				...si el destino se encuentra como origen entre las clases-subclases...				
				if(findOrigConcep(prepItem.getDestConcept(), C_CS)!=null)//...quiere decir que en realidad el concepto origen es la instancia, la f.e es de catg. istancia pero inversa...
					prepTemp1=prepInverse;//...y el concepto destino es la clase, en este caso, a diferencia de la R2 y sus relaciones inversas...
				else// 	si se selecciona  la relacciones inversa de la forma concepto-f.e-instancia por que asi se utiliza en el codificador
					prepTemp1=prepItem;
				result.setLinkCodify(result.getLinkCodify()+1);
			}			
			else{//...........................Buscar inferir por una relación semantica........................................................b)	
	
				if(findProp(prepItem,this.CycInstances)!=null)//...se busca primero haber si existe una relación(semantica) directa	¿hay k buscar en la inversa?	
					conjunctID=4;
				else if(findProp(prepItem,this.owlInstance)!=null)
					conjunctID=5;

//				... si f.e NO es instancia(por ejemplo es "")... y ...Si se codifico al menos algun tipo de relación por algún conjunto de los 9 posibles...
				if(!category.equalsIgnoreCase(catInsgeneric)&& conjunctID>0){//...quiere decir que la semantica sugiere una relación de instancia...																						//¿ver k semantica tiene prioridad Clase-SubClase(&&) ó Instancia(||)?
					prepItem.getLink().setCategory(catInsgeneric);
					prepTemp1=prepItem;
					
//									84(instancia de)
					if(linkName.equalsIgnoreCase("instance of")){
						prepInverse.getLink().setCategory(catInsgeneric);
					//	prepTemp=prepInverse;
					}
					/*else if(category.equalsIgnoreCase("")){//...si NO EXISTE categoría para la f.e, adicionar la nueva f.e al fichero link con su categoría correspondiente...
						prepItem.getLink().setCategory(catInsgeneric);
						prepTemp=prepItem;
						insertLinkWord(prepItem.getLink().getLinkName().trim(),catInsgeneric);
					}*/
				}	
			}
			if(prepTemp1!=null){//...si se logro inferir alguna preposición por f.e o conjunto (Clase-Instacnia)...
				resCod.add(prepItem);
				C_CI.add(prepTemp1);
				codifyStatistics(6,conjunctID);
			}


//				else{//verficar si son las PE 80( ejemplo) o 113(por ejemplo) para.. 
				//..ver si har relación de hyperonimia o no..
//				if(linkName.equalsIgnoreCase("example")||linkName.equalsIgnoreCase("for example")){

				/*Preposition propTemp = findProp(propItem, hypeHypoRel); 
						if(propTemp != null){

							S_CS.add(propItem);
							resCod.add(propItem);

							//agregar estadistica a los a los resultados
							result.setCodRul6(result.getCodRul6()+1);
							result.setLinkCodify(result.getLinkCodify()+1);
							result.setCantCodificadas(result.getCantCodificadas()+1);

						}
						else{*/
				/*		S_CI.add(propItem);

							resCod.add(propItem);

							//agregar estadistica a los a los resultados
							result.setCodRul6(result.getCodRul6()+1);
							result.setLinkCodify(result.getLinkCodify()+1);
							result.setCantCodificadas(result.getCantCodificadas()+1);
//						}
				 *///					} 
//				}
			}
		for (CmapProposition preposition : resCod)	//eliminar las que proceso 
			prepositions.remove(preposition);
	}

	/**
	 * Regla 7 (salience = 8)
	 * 
	 * */
	public void Rule7(ArrayList<CmapProposition> prepositions) {
		ArrayList<CmapProposition>resCod = new ArrayList<CmapProposition>();
	
		String catPropgeneric="Propiedad";//.... es mejor una variable para cambiar la comparacion de igles-español(Property-Propiedad)..etc..etc	
		
		for(CmapProposition prepItem : prepositions){
			int conjunctID=0;
			String linkName = prepItem.getLink().getLinkName();
			String category = linkCategory(linkName.trim());
			CmapProposition prepTemp= null;
//			...Buscar inferir por f.e de categoría Propiedad...........................................................................			
			if(category.equalsIgnoreCase(catPropgeneric)){
				prepTemp=prepItem;
				result.setLinkCodify(result.getLinkCodify()+1);
			}
			else{//...........................Buscar inferir por una relación semantica........................................................	

				if(findProp(prepItem,this.meroHoloRels)!=null)
					conjunctID=6;
				else if(findProp(prepItem,this.owlObjectProperty)!=null)
					conjunctID=8;

//				...Si se codifico al menos alguntipo de relacion por algun conjunto de los 9 posibles...
				if(conjunctID>0 ){//...quiere decir que la semantica sugiere una relación de propiedad...	
					prepTemp=prepItem;
//					...si NO EXISTE categoría para la f.e, adicionar la nueva f.e al fichero link con su categoría correspondiente...
					if(category.equalsIgnoreCase("")){
						prepItem.getLink().setCategory(catPropgeneric);
						insertLinkWord(prepItem.getLink().getLinkName().trim(),catPropgeneric);
					}
				}
			}
			if(prepTemp!=null){//...si se logro inferir alguna preposición por f.e o conjunto (Clase-Instacnia)...
				C_CP.add(prepItem);
				resCod.add(prepItem);
				codifyStatistics(7, conjunctID);//agregar estadistica a los a los resultados
			}
		}
		for (CmapProposition preposition : resCod)//eliminar las que proceso 
			prepositions.remove(preposition);
	}

	/**
	 * Regla 8 (salience = 8)
	 * @param propositions
	 **/
	public void Rule8(ArrayList<CmapProposition> prepositions) {
		ArrayList<CmapProposition>resCod = new ArrayList<CmapProposition>();
		int i=0;
		String catPropgeneric="Valor de Propiedad";//.... es mejor una variable para cambiar la comparacion de igles-español(Property Value-Valor de Propiedad)..etc..etc	

		for (CmapProposition prepItem : prepositions) {
			i++;
			int conjunctID=0;
			String linkName = prepItem.getLink().getLinkName();
			String category = linkCategory(linkName.trim());//arreglar metodo
			CmapProposition prepTemp= null;

			//if(FindSourceConcep(propItem.getOrigConcept(),S_CI).getConceptName()==null){

//			...Buscar inferir por f.e de categoría Valor de Propiedad.............................................8a)	
			if(category.equalsIgnoreCase(catPropgeneric)){
				prepItem.getLink().setCategory(catPropgeneric);
				prepTemp=prepItem;
				result.setLinkCodify(result.getLinkCodify()+1);
			}
			else if(this.FAcess.isNounWord(linkName.trim())){//.............Otra variante por f.e: Verificar si la f.e es un sustantivo
				prepItem.getLink().setCategory(catPropgeneric);
				prepTemp=prepItem;
				insertLinkWord(prepTemp.getLink().getLinkName().trim(),catPropgeneric);//,tratar de insertar la f.e del mapa
				result.setLinkCodify(result.getLinkCodify()+1);
			}
			else{//...............Buscar inferir por una relación semantica..................................	8b)
				prepTemp=findProp(prepItem,this.meroHoloTypeRels);
				if(prepTemp!=null)
					conjunctID=7;
				else{//.........................................................................................	8c)
					prepTemp=findProp(prepItem,this.owlRestricProperty);
					if(prepTemp!=null)
						conjunctID=9;
				}

				if(conjunctID >0){//...Si se codifico al menos algun tipo de relacion por algun conjunto de los 9 posibles...
//					..quiere decir que la semantica sugiere una relación de Valor de Propiedad..
					String phraseLink = linkRelMeroHolo(prepTemp.getType());//¿ES estricto k la f.e enlace tenga estos nombres de WN?

					if(phraseLink!=null){
						prepTemp.getLink().setCategory(catPropgeneric);

						//...si la categoría no fue encontrada adicionar la nueva f.e al fichero link con su categoría correspondiente...
						if(category.equalsIgnoreCase("")){
							if(insertLinkWord(phraseLink.trim(),catPropgeneric))//Tratar de insertar la f.e formal:has member,has part...etc
								prepTemp.getLink().setLinkName(phraseLink);
							else if(insertLinkWord(prepTemp.getLink().getLinkName().trim(),catPropgeneric))//si ya esta frase,tratar de insertar la f.e del mapa
								prepTemp.getLink().setLinkName(phraseLink);
						}	
					}
				}
			}
			if(prepTemp!=null){
				C_CPV.add(prepTemp);
				resCod.add(prepTemp);
				codifyStatistics(8, conjunctID);// agregar estadistica a los a los resultados
			}
		}
		for (CmapProposition preposition : resCod)//eliminar las que proceso
			prepositions.remove(preposition);
	}

	/**
	 * Regla 9 (salience = 6)
	 * */
	public void Rule9() {
		boolean found = false;
		ArrayList<CmapProposition>resCod = new ArrayList<CmapProposition>();
		
		for (CmapProposition prepItem : C_CPV) {

			
			ArrayList<CmapProposition> instancesP = this.C_CI;

			for (int i = 0; i < instancesP.size() && !found; i++){//... Buscar un (C',I) en S_CI...
				//...Existe un(C’, I)del conunto C_CI tal que V = I ...
				if(prepItem.getDestConcept().getConceptName().equalsIgnoreCase(instancesP.get(i).getDestConcept().getConceptName()))
					if(!(prepItem.getOrigConcept().getConceptName().equalsIgnoreCase(instancesP.get(i).getOrigConcept().getConceptName())))
						found = true;	
			} 
			if(found){
				found = false;
				C_CPVHasValue.add(prepItem); 
				resCod.add(prepItem);

				codifyStatistics(9, 0); //agregar estadistica a los a los resultados
				//result.setSemanticCod(result.getSemanticCod()+1);¿Aqui se codifica una iferencia semantica?
			}	
		}
		for (CmapProposition preposition : resCod)//eliminar las que proceso
			C_CPV.remove(preposition);			
	}

	/**
	 * Regla 10 (salience = 6)
	 * */
	public void Rule10() {
		for (CmapProposition prepItem : C_CPVHasValue) {

			if(isFunctionalProperty(prepItem)){//Verficar en PP-BC si hay f.e funcionales

				functionalProperty.add(prepItem);
				codifyStatistics(10,0);// agregar estadistica a los a los resultados
				//result.setSemanticCod(result.getSemanticCod()+1);
			}
		}
	}

	/**
	 * Regla 11 (salience = 6)
	 * */
	public void Rule11() {
		for (CmapProposition prepItem : C_CPV) {
			
			if(isSymetricProperty(prepItem)){//Verficar en PP-BC si hay sinonimos de f.e o son iguales 

				symetricProperty.add(prepItem);
				codifyStatistics(11,0);//agregar estadistica a los a los resultados
				//result.setSemanticCod(result.getSemanticCod()+1);
			}
		}
	}
//	::::::::::::::::::::::::::::::::::::::Metodos de Codificacion con Protege::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	public void subClassCode(){

		for (CmapProposition prepItem : C_CS) {

			String origConcept = (prepItem.getOrigConcept().getConceptName()).trim().replace(' ','_');
			String destConcept = (prepItem.getDestConcept().getConceptName()).trim().replace(' ','_');
			String linkName = prepItem.getLink().getLinkName().trim();

			OWLNamedClass superClass = owlModel.getOWLNamedClass(origConcept);
			OWLNamedClass subClass = owlModel.getOWLNamedClass(destConcept);

			if(superClass == null) 				
				superClass = owlModel.createOWLNamedClass(origConcept);

			if(subClass == null)
				subClass = owlModel.createOWLNamedClass(destConcept);

			//verificar si la PE es directa o inversa
			//verificar cual es el id de la palabra
			String category = linkCategory(linkName.trim());
			//Subclasificación
			if(category.equalsIgnoreCase("Subclasificacion")){
				// 					68(es uno)				69(es un)				74(tipo de)   		 			75(un tipo de)  			139(son)        		147(que son)  
				/*if(linkName.equalsIgnoreCase("is one")||linkName.equalsIgnoreCase("is a")||linkName.equalsIgnoreCase("type of")||linkName.equalsIgnoreCase("a type of")||linkName.equalsIgnoreCase("are")||linkName.equalsIgnoreCase("that are"))
					superClass.addSuperclass(subClass);
				else*/
					subClass.addSuperclass(superClass);	
			}
		}
	}   

	public void instanceClassCode() {

		OWLNamedClass superClass = null;
		OWLIndividual origInstance = null;
		for (CmapProposition instanceItem : C_CI) {

			String origConcept = instanceItem.getOrigConcept().getConceptName().trim().replace(' ','_'); 
			String destConcept = instanceItem.getDestConcept().getConceptName().trim().replace(' ','_');
			superClass = owlModel.getOWLNamedClass(origConcept);
			// OWLNamedClass inst = owlModel.getOWLNamedClass(instanceItem.getDestConcept().getConceptName());
			if(superClass == null)
				origInstance = owlModel.getOWLIndividual(origConcept);

			if(superClass == null && origInstance==null){
				superClass = owlModel.createOWLNamedClass(origConcept);
			}
			if(superClass != null)	
				superClass.createOWLIndividual(destConcept);			
		}
	}

	public void propertyCode(){

		for (CmapProposition prptyItem : C_CP) {
			OWLIndividual indDomain = null;

			String property = prptyItem.getLink().getLinkName().trim().replace(' ', '_');
			String propertyName = "";
			// si la f-e es "tiene" entonces la propiedad es  y k hay con "made of"
			if((property.equalsIgnoreCase("has")) ||(property.equalsIgnoreCase("contains"))||(property.equalsIgnoreCase("contain"))||(property.equalsIgnoreCase("possesses"))||(property.equalsIgnoreCase("possess"))||(property.equalsIgnoreCase("have"))){
				propertyName = property +"_" + prptyItem.getDestConcept().getConceptName().trim().replace(' ','_');	
			}
			else
				propertyName = property;

			String origConcept = prptyItem.getOrigConcept().getConceptName().trim().replace(' ','_'); 
			String destConcept = prptyItem.getDestConcept().getConceptName().trim().replace(' ','_');

			OWLObjectProperty prop = owlModel.getOWLObjectProperty(propertyName);
			//OWLNamedClass linkClass = owlModel.getOWLNamedClass(prptyItem.getLinkWord().getLinkName().trim()); 
			OWLNamedClass domain = owlModel.getOWLNamedClass(origConcept);
			OWLNamedClass range = owlModel.getOWLNamedClass(destConcept);

			if(domain == null)
				indDomain = owlModel.getOWLIndividual(origConcept);

			if(indDomain==null){

				if(domain==null)
					domain = owlModel.createOWLNamedClass(origConcept);

				if(range == null)
					range = owlModel.createOWLNamedClass(destConcept);

				if(prop == null){

					prop = owlModel.createOWLObjectProperty(propertyName);

					prop.setDomain(domain);
					prop.setRange(range);
				}
				else{
					//verificar los dominios
					if(!(prop.getDomain(true).equals(domain))){

						if(!(prop.getUnionDomain().contains(domain)))
							prop.addUnionDomainClass(domain);
					}
					//verificar los rangos
					if(!(prop.getRange().equals(range))){

						if(!(prop.getUnionRangeClasses().contains(range)))
							prop.addUnionRangeClass(range);
					}
				}
			}
		}
	}

	public void	propertyValueCode() {//entender

		OWLIndividual indOrig = null;
		OWLIndividual indDest = null;
		for (CmapProposition valuePropItem : C_CPV) {

			String origConcept = valuePropItem.getOrigConcept().getConceptName().trim().replace(' ','_'); 
			String destConcept = valuePropItem.getDestConcept().getConceptName().trim().replace(' ','_');
			String linkWord = valuePropItem.getLink().getLinkName().trim().replace(' ','_');

			OWLNamedClass classOrig = owlModel.getOWLNamedClass(origConcept);
			OWLNamedClass classDest = owlModel.getOWLNamedClass(destConcept);

			if(classOrig == null)
				indOrig = owlModel.getOWLIndividual(origConcept);

			if(classDest == null)
				indDest = owlModel.getOWLIndividual(destConcept);

			OWLObjectProperty property = owlModel.getOWLObjectProperty(linkWord);

			if((indOrig == null) && (indDest==null)){

				if(classOrig == null)
					classOrig = owlModel.createOWLNamedClass(origConcept);

				if(classDest == null)
					classDest = owlModel.createOWLNamedClass(destConcept);

				if(property == null)
					property = owlModel.createOWLObjectProperty(linkWord);


				OWLSomeValuesFrom restriction = owlModel.createOWLSomeValuesFrom(property, classDest);
				classOrig.addSuperclass(restriction);
			}
		}
	}

	public void propertyHasValueCode(){//entender

		for (CmapProposition propertyItem : C_CPVHasValue) {

			String origConcept = propertyItem.getOrigConcept().getConceptName().trim().replace(' ','_'); 
			String destConcept = propertyItem.getDestConcept().getConceptName().trim().replace(' ','_');
			String linkWord = propertyItem.getLink().getLinkName().trim().replace(' ','_');

			OWLNamedClass classOrig = owlModel.getOWLNamedClass(origConcept);
			OWLIndividual indDest = owlModel.getOWLIndividual(propertyItem.getDestConcept().getConceptName());
			OWLObjectProperty property = owlModel.getOWLObjectProperty(destConcept);

			if(classOrig == null)
				classOrig = owlModel.createOWLNamedClass(origConcept);

			if(indDest != null){

				if(property == null)
					property = owlModel.createOWLObjectProperty(linkWord);

				OWLHasValue hasValue = owlModel.createOWLHasValue(property, indDest);
				classOrig.addSuperclass(hasValue);	
			}
		}
	}

	public void symmetricPropertiesCode() {//entender

		for (CmapProposition propertyItem : symetricProperty) {

			String linkWord = propertyItem.getLink().getLinkName().trim().replace(' ','_');
			OWLObjectProperty property = owlModel.getOWLObjectProperty(linkWord);

			if(property == null)
				property = owlModel.createOWLObjectProperty(linkWord);

			property.setSymmetric(true);
		}
	}

	public void functionalPropertiesCode() {//entender

		for (CmapProposition propertyItem : functionalProperty) {

			String linkWord = propertyItem.getLink().getLinkName().trim().replace(' ','_');
			OWLObjectProperty property = owlModel.getOWLObjectProperty(linkWord);

			if(property == null)
				property = owlModel.createOWLObjectProperty(linkWord);

			property.setFunctional(true);
		}
	}

	public void intersectionClassesCode() {//entender

		for (Union intersectItem : intersectionClass) {

			String unionClass = intersectItem.getConceptUnion().getConceptName().trim().replace(' ','_');
			OWLNamedClass classIntersect = owlModel.getOWLNamedClass(unionClass);

			if(classIntersect == null)
				classIntersect = owlModel.createOWLNamedClass(unionClass);

			OWLIntersectionClass intersectionList = owlModel.createOWLIntersectionClass();

			for (CmapConcept conceptItem : intersectItem.getUnionList()) {

				String concept = conceptItem.getConceptName().trim().replace(' ','_');
				OWLNamedClass classItem = owlModel.getOWLNamedClass(concept);

				if(classItem == null)
					classItem = owlModel.createOWLNamedClass(concept);

				intersectionList.addOperand(classItem);
			}
			classIntersect.addSuperclass(intersectionList);
		}
	}

	public void intersectionClassPropertiesCode() {//entender

		for (IntersectionProperty intersectItem : intersectionClassProperty) {

			String instCls= intersectItem.getInstConcept().getConceptName().trim().replace(' ','_');
			String conceptCls = intersectItem.getConcept().getConceptName().trim().replace(' ','_');
			String valueInst = intersectItem.getConceptValue().getConceptName().trim().replace(' ','_');
			String prop = intersectItem.getProperty().trim().replace(' ','_');

			OWLNamedClass instClass = owlModel.getOWLNamedClass(instCls);
			OWLNamedClass conceptClass = owlModel.getOWLNamedClass(conceptCls);
			OWLIndividual valueInstance = owlModel.getOWLIndividual(valueInst);
			OWLObjectProperty property = owlModel.getOWLObjectProperty(prop);

			if(valueInstance != null){

				if(instClass == null)
					instClass = owlModel.createOWLNamedClass(instCls);

				if(conceptClass == null)
					conceptClass = owlModel.createOWLNamedClass(conceptCls);

				if(property == null)
					property = owlModel.createOWLObjectProperty(prop);

				OWLHasValue hasValue = owlModel.createOWLHasValue(property, valueInstance);
				OWLIntersectionClass intersect = owlModel.createOWLIntersectionClass();
				intersect.addOperand(conceptClass);
				intersect.addOperand(hasValue);
				instClass.addSuperclass(intersect);
			}
		}
	}

	public void unionClassCode(){//entender

		for (Union unionItem : unionClass) {

			String unionCl =unionItem.getConceptUnion().getConceptName().trim().replace(' ','_'); 

			OWLNamedClass unionClass = owlModel.getOWLNamedClass(unionCl);

			if(unionClass == null)
				unionClass = owlModel.createOWLNamedClass(unionCl);

			OWLUnionClass unionCollection = owlModel.createOWLUnionClass();

			for (CmapConcept conceptItem : unionItem.getUnionList()) {

				String unionIt = conceptItem.getConceptName().trim().replace(' ','_');
				OWLNamedClass unionItemConcept = owlModel.getOWLNamedClass(unionIt);

				if(unionItemConcept == null)
					unionItemConcept = owlModel.createOWLNamedClass(unionIt);

				unionCollection.addOperand(unionItemConcept);
			}
			unionClass.addSuperclass(unionCollection);
		}
	}

	public void codifyRestClasses(ArrayList<CmapProposition> restPropositions){//entender

		for (CmapProposition preposition : restPropositions) {

			OWLIndividual origInstance = null;
			OWLIndividual destinyInstance = null;
			String origCl = preposition.getOrigConcept().getConceptName().trim().replace(' ','_');
			String destCl = preposition.getDestConcept().getConceptName().trim().replace(' ','_');

			OWLNamedClass origClass = owlModel.getOWLNamedClass(origCl);

			if(origClass == null)
				origInstance = owlModel.getOWLIndividual(origCl);

			OWLNamedClass destinyClass = owlModel.getOWLNamedClass(destCl);
			if(destinyClass == null)
				destinyInstance = owlModel.getOWLIndividual(destCl);


			if(origClass == null && origInstance==null) 				
				origClass = owlModel.createOWLNamedClass(origCl);

			if(destinyClass == null && destinyInstance==null)
				destinyClass = owlModel.createOWLNamedClass(destCl);

		}
	}

	/*(PP-MC)propociciones nuevas k contienen al menos u cocepto del mapa
	 * y se obtienen de consultar a servimap
	 */
	public void createConceptRepository() {

		for (CmapConcept conceptItem : desConcepts) {
			//nuevas prociciones apartir del concepto destino

			try {	
				ResultSet origProp = prepositionbyOrigConcep(conceptItem.getConceptName());

				if(origProp != null){

					while (origProp.next()) {
						String linkName = origProp.getString("PalabraEnlace");

						CmapLink linkItem = new CmapLink(linkName);
						String conceptName = origProp.getString("Nombre");
						CmapConcept destinyConcept = new CmapConcept(conceptName);

						CmapProposition newPreposition = new CmapProposition(conceptItem,linkItem,destinyConcept);
						prepositionRepository.add(newPreposition);
					}
				}
//				nuevas prociciones apartir del concepto destino
				ResultSet destinyPrep = prepositionbyDestinyConcep(conceptItem.getConceptName());

				if(destinyPrep != null){
					while (destinyPrep.next()) {

						String conceptName = destinyPrep.getString("Nombre");
						String linkName = destinyPrep.getString("PalabraEnlace");

						CmapConcept origConcept = new CmapConcept(conceptName);
						CmapLink linkItem = new CmapLink(linkName);

						CmapProposition newProposition = new CmapProposition(origConcept,linkItem,conceptItem);
						prepositionRepository.add(newProposition);
					}
				}
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public ArrayList<CmapProposition> executeRules() {
		ArrayList<CmapProposition> propositions = this.mapPrepToInfer; 
	
		Rule1(propositions);
 		Rule2(propositions);
		Rule6(propositions); 
		Rule7(propositions);
		Rule8(propositions);

		Rule3();
		Rule4();
		Rule9();
		Rule11();

		Rule5();
		Rule10();

		return propositions; // las proposiciones que no codificaron en ninguna regla
	}

	//Para trabjar en batch
	public String codifyRules(ArrayList<CmapProposition> restPropositions){
		String owlResult = ""; 
		
		try {//Convertir los Conjuntos de Preposiciones en elementos del Mapa
			subClassCode();
			instanceClassCode();
			propertyCode();
			propertyValueCode();
			propertyHasValueCode();
			symmetricPropertiesCode();
			functionalPropertiesCode();
			intersectionClassesCode();
			intersectionClassPropertiesCode();
			unionClassCode();
			codifyRestClasses(restPropositions);

			saveOntology( "OWLProcess/"+map.getTitle());

		//	... se lee el fihero donde se salva la ontología y se guarda en una cadena...
			File owlFile = new File("OWLProcess/"+map.getTitle()+ ".owl");
			FileReader entryFile = new FileReader(owlFile);
			BufferedReader salida = new BufferedReader(entryFile);
			String lineReader = salida.readLine();

			while(lineReader != null ){
				owlResult += lineReader;
				lineReader = salida.readLine();
			}
			
			entryFile.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		storeTotalRels();//Guadar los datos estadisticos de el total de cada conjunto
		result.report(map.getTitle());//crear fichero de resutados estadisticos

		return owlResult;
	}
	
	public void saveOntology(String name) {
		try {
			((JenaOWLModel) owlModel).save(new File( name + ".owl").toURI());
		}
		catch (Exception e) {	// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @author Alejandro R.C
	 * @return ...Guadar los datos estadisticos de el total de cada conjunto(WN, OpenCyc, OWLRepository)...
	 * */
	public void storeTotalRels(){
//		 ..por WN
		result.setHypeHypoRels(this.hypeHypoRels!=null ? this.hypeHypoRels.size():0);
		result.setMeroHoloRels(this.meroHoloRels!=null ? this.meroHoloRels.size():0);
		result.setMeroHoloTypeRels(this.meroHoloTypeRels!=null ? this.meroHoloTypeRels.size():0);
//		 ..por OpenCyc			
		result.setCycHierarchyRels(this.CycHerarchys!=null ? this.CycHerarchys.size():0);
		result.setCycInstanceRels(this.CycInstances!=null ? this.CycInstances.size():0);
//		 ..por OWLRepository			
		result.setOwlClassSubClassRels(this.owlClassSubClass!=null ? this.owlClassSubClass.size():0);
		result.setOwlInstanceRels(this.owlInstance!=null ? this.owlInstance.size():0);
		result.setOwlObjectPropertyRels(this.owlObjectProperty!=null ? this.owlObjectProperty.size():0);
		result.setOwlRestricPropertyRels(this.owlRestricProperty!=null ? this.owlRestricProperty.size():0);
	}
	
	public void run() {
		// TODO Auto-generated method stub
	}

	public boolean isOwlRepositoryLoaded() {
		return owlRepositoryLoaded;
	}

	public void setOwlRepositoryLoaded(boolean owlRepositoryLoaded) {
		this.owlRepositoryLoaded = owlRepositoryLoaded;
	}
}
