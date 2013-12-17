/**
 * 
 */
package parse;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import similarities.CmapSimilarity;
import sintax.CheckSintax;
import cmaps.CMap;
import cmaps.CmapConcept;
import cmaps.CmapLink;
import cmaps.CmapProposition;

import com.hp.hpl.jena.util.FileUtils;

import edu.stanford.smi.protegex.owl.ProtegeOWL;
import edu.stanford.smi.protegex.owl.jena.JenaOWLModel;
import edu.stanford.smi.protegex.owl.model.OWLIndividual;
import edu.stanford.smi.protegex.owl.model.OWLModel;
import edu.stanford.smi.protegex.owl.model.OWLNamedClass;
import edu.stanford.smi.protegex.owl.model.OWLRestriction;
import edu.stanford.smi.protegex.owl.model.RDFProperty;
import edu.stanford.smi.protegex.owl.model.RDFSClass;
import edu.stanford.smi.protegex.owl.model.impl.DefaultOWLNamedClass;
import edu.stanford.smi.protegex.owl.model.impl.DefaultRDFProperty;
import file.FileConcepsManage;

/**
 *@author Alejandro
 *@ Esta Clase se encargara de parsear el repositorio de Ontologías
 */
public class ParseOWL {
	private String owlRepositoryPath ;
	private FileConcepsManage owlRepConcepsFile;
	private FileConcepsManage owlNormalizedRepConcepsFile;

	private CheckSintax Sintax;
	private Collection namedClases;
	private Collection subClases;
	private Collection Instance;
	private Collection objectProperty;
	private Collection Properties;
	private Collection Restrictions;
	private Collection unionRange;
	private Collection unionDomains;

	private OWLModel owlModel;
	
	private CMap mapLanded ;
	private ArrayList<CMap> mapsLanded;
	//private CmapSimilarity equalizer;
	private ArrayList<CMap> mapsTopSimilar;

	private ArrayList<CmapProposition> owlSubclass;
	private ArrayList<CmapProposition> owlInstannce;
	private ArrayList<CmapProposition> owlObjectproperty;
	private ArrayList<CmapProposition> owlProperty;



//	:::::::::::::::::::::Constructor:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::



	public ParseOWL() {
		Sintax=new CheckSintax();
		owlRepConcepsFile=new FileConcepsManage("StaticsResult/RepositoryOWL/RepositoryOWL");
		owlRepConcepsFile.loadConcep();
		owlNormalizedRepConcepsFile=new FileConcepsManage("StaticsResult/RepositoryOWL/NormalizedRepositoryOWL");
		owlNormalizedRepConcepsFile.loadConcep();
		namedClases= new ArrayList();
		subClases= new ArrayList();
		Instance= new ArrayList();
		objectProperty= new ArrayList();
		mapLanded= new CMap();
		mapsLanded=new ArrayList<CMap>();
		mapsTopSimilar=new ArrayList<CMap>();
		unionRange= new ArrayList();
		owlSubclass= new ArrayList<CmapProposition>();
		owlInstannce= new ArrayList<CmapProposition>();
		owlObjectproperty= new ArrayList<CmapProposition>();
		owlProperty= new ArrayList<CmapProposition>();
	}
	public ParseOWL(String prepositoryPath) {
		if(prepositoryPath != null && prepositoryPath.equals("") == false){
			Sintax=new CheckSintax();
			owlRepConcepsFile=new FileConcepsManage("StaticsResult/RepositoryOWL/RepositoryOWL");
			owlRepConcepsFile.loadConcep();
			owlNormalizedRepConcepsFile=new FileConcepsManage("StaticsResult/RepositoryOWL/NormalizedRepositoryOWL");
			owlNormalizedRepConcepsFile.loadConcep();
			owlRepositoryPath = prepositoryPath;
			namedClases= new ArrayList();
			subClases= new ArrayList();
			Instance= new ArrayList();
			objectProperty= new ArrayList();
			mapLanded= new CMap();
			mapsLanded=new ArrayList<CMap>();
			mapsTopSimilar=new ArrayList<CMap>();
			unionRange= new ArrayList();
			owlSubclass= new ArrayList<CmapProposition>();
			owlInstannce= new ArrayList<CmapProposition>();
			owlObjectproperty= new ArrayList<CmapProposition>();
			owlProperty= new ArrayList<CmapProposition>();

		}

	}
//	:::::::::::::::::::::GET & SET:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::	
	public Collection getInstance() {
		return Instance;
	}
	public void setInstance(Collection instance) {
		Instance = instance;
	}
	public CMap getMapLanded() {
		return mapLanded;
	}
	public void setMapLanded(CMap mapLanded) {
		this.mapLanded = mapLanded;
	}
	public Collection getNamedClases() {
		return namedClases;
	}
	public void setNamedClases(Collection namedClases) {
		this.namedClases = namedClases;
	}
	public Collection getObjectProperty() {
		return objectProperty;
	}
	public void setObjectProperty(Collection objectProperty) {
		this.objectProperty = objectProperty;
	}
	public Collection getSubClases() {
		return this.subClases;
	}
	public void setSubClases(Collection subClases) {
		this.subClases = subClases;
	}
	public OWLModel getOwlModel() {
		return owlModel;
	}
	public void setOwlModel(OWLModel owlModel) {
		this.owlModel = owlModel;
	}
	public String getOwlRepositoryPath() {
		return owlRepositoryPath;
	}
	public void setOwlRepositoryPath(String repositoryPath) {
		owlRepositoryPath = repositoryPath;
	}
	public Collection getProperty() {
		return this.Properties;
	}
	public void setProperty(Collection property) {
		this.Properties = property;
	}
	public Collection getRestrictions() {
		return Restrictions;
	}
	public void setRestrictions(Collection restrictions) {
		this.Restrictions = restrictions;
	}
	public Collection getUnionDomains() {
		return unionDomains;
	}
	public void setUnionDomains(Collection unionDomains) {
		this.unionDomains = unionDomains;
	}
	public Collection getUnionRange() {
		return unionRange;
	}
	public void setUnionRange(Collection unionRange) {
		this.unionRange = unionRange;
	}
	public ArrayList<CmapProposition> getOwlSubclass() {
		return owlSubclass;
	}
	public void setOwlSubclass(ArrayList<CmapProposition> owlclasssubclass) {
		this.owlSubclass = owlclasssubclass;
	}
	public ArrayList<CmapProposition> getOwlInstannce() {
		return owlInstannce;
	}
	public void setOwlInstannce(ArrayList<CmapProposition> owlinstannce) {
		this.owlInstannce = owlinstannce;
	}
	public ArrayList<CmapProposition> getOwlObjectproperty() {
		return owlObjectproperty;
	}
	public void setOwlObjectproperty(ArrayList<CmapProposition> owlobjectproperty) {
		this.owlObjectproperty = owlobjectproperty;
	}
	public ArrayList<CmapProposition> getOwlProperty() {
		return owlProperty;
	}
	public void setOwlProperty(ArrayList<CmapProposition> owlproperty) {
		this.owlProperty = owlproperty;
	}
	public Collection getProperties() {
		return Properties;
	}
	public void setProperties(Collection properties) {
		Properties = properties;
	}
//	:::::::::::::::::::::MEtodos:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	/**@author Alejandro
	 * @return  Carga todos los 25% mapas mas similares a "cmapSource"  de todos los mapas del repositorio
	 */
	public void loadOWLMapsSimilars(CMap cmapTarget){
		 ArrayList<CMap> mapsSource=new ArrayList<CMap>();
		 double similTotal =0;
		 for (CMap cmapSource : mapsLanded) {
			cmapTarget.setAuthorityWeight();
			cmapTarget.setHubWeight();
			cmapTarget.setUpperWeight();
			cmapTarget.normalizeAuthorityWeight();

			cmapSource.setAuthorityWeight();
			cmapSource.setHubWeight();
			cmapSource.setUpperWeight();
			cmapSource.normalizeAuthorityWeight();
			
			double similarity = CmapSimilarity.cmapSimilarities(cmapTarget, cmapSource);
			if(similarity>0){
				cmapSource.setSimilarity(similarity);
				mapsSource.add(cmapSource);
				similTotal+=similarity;
			}
		}
		 int cantMap=mapsSource.size();
		
		if(cantMap>0){		
			@SuppressWarnings("unused")
			double relevance=similTotal/cantMap;
			cmapTarget.setRepositoryRelv(relevance);
			OrderTopMapSimilar(mapsSource);
			}
	}
	

	/**@author Alejandro
	 * @return  Ordena los mapas descendentemente y obtiene solamente el 25% de los mapas más similares
	 */
	public void OrderTopMapSimilar( ArrayList<CMap> CMaps){

		ArrayList<CMap> orderCMaps=new ArrayList<CMap>();
		CMap piboMap= new CMap();
		orderCMaps.add(piboMap);
		
		for (int i = 0; i < CMaps.size();i++) {
			boolean add=false;
			int j = 0;
			while (j < orderCMaps.size()&&!add) {

				CMap temp=CMaps.get(i);
				CMap maporder= orderCMaps.get(j);
				if(maporder.getSimilarity()<=temp.getSimilarity()){
					orderCMaps.add(j, temp);
					add=true;
				}
				else
					j++;
			}
		}
		
		mapsTopSimilar.addAll(orderCMaps);//Si Mapsimilarty esta al 100% se usa todo el repositorio que contenga maspas con similaridad > 0

		/*		int newIndex=orderCMaps.size()/2;
		for (int i = 0; i < newIndex; i++) {
			mapsTopSimilar.add(orderCMaps.get(i));
		}*/
		
		
	}
	
	/**@author Alejandro
	 * @return  Carga todos los conjuntos de una ontología(Clase-SubClase,Instancia,etc) de los 25% mapas mas similares a "cmapSource"  
	 * de todos los mapas del repositorio
	 */
	public void loadOWLConjunctions(){
		
		for (CMap mapTopSimilar : mapsTopSimilar) {
			owlSubclass.addAll(mapTopSimilar.getOwlSubclassProps());
			owlInstannce.addAll(mapTopSimilar.getOwlInstannceProps());
			owlObjectproperty.addAll(mapTopSimilar.getOwlObjectpropertyProps());
			owlProperty.addAll(mapTopSimilar.getOwlPropertyProps());
		}
	}
	
	/**@author Alejandro
	 * @return  Carga todos los elemtos o conjuntos de una ontología y los guarda en un mapa(i) del conjunto de mpas
	 */
	public void loadOWLtoMap()
	{
		Collection errors = new ArrayList();
		this.owlModel = ProtegeOWL.createJenaOWLModel();
		((JenaOWLModel) owlModel).load(new File(owlRepositoryPath).toURI(),FileUtils.langXMLAbbrev, errors);

		namedClases= owlModel.getUserDefinedOWLNamedClasses();	// 2)todas las clases de la ontología y apartir de aqui(2.1)instancias y (2.2)subclases  
		objectProperty=owlModel.getUserDefinedOWLObjectProperties();// Todas las propiedades del tipo ObjecProperty C_CP(R7)
		Properties=owlModel.getRDFProperties();// Todas las propiedades con restriciones "someValuefrom" C_CPV(R8)y "hasValue" C_CPVtiene_valor(R9)

		this.loadConcep();
		this.loadClass_Sub_Inst();// Carga Clases,SubClases e Instancias
		this.loadObjectProperty();// Carga propiedades simples sin restricción
		this.loadProperty();//  Carga propiedades con restricción
		mapsLanded.add(this.mapLanded);
		this.mapLanded= new CMap();
	}
	public boolean addMapLandedConcep(CmapConcept concepNew ){

		boolean found=false;
		Iterator<CmapConcept> iterator=this.mapLanded.getConcepts().iterator();
		while (iterator.hasNext()&&!found) {
			CmapConcept concep = iterator.next();
			if(concepNew.getConceptName().equals(concep.getConceptName()))
				found=true;
		}
		if(!found){
			this.mapLanded.getConcepts().add(concepNew);
			return true;
		}
		else
			return false;
	}

	/**
	 * @author Alejandro
	 * @return	Carga para cada Clase de las ontologías obtener los CMapConcep correspondientes
	 * 
	 */
	public void loadConcep(){
		for (Iterator iter = this.namedClases.iterator(); iter.hasNext();) {

			DefaultOWLNamedClass Class = (DefaultOWLNamedClass) iter.next();
			CmapConcept concepNew =new CmapConcept(Class.getBrowserText());

			concepNew.addConceptWords();
			this.addMapLandedConcep(concepNew);
			
			if(!owlRepConcepsFile.findConcep(concepNew.getConceptName())){
				owlRepConcepsFile.addConcep(concepNew.getConceptName());
				owlRepConcepsFile.loadConcep();
				String normalNewConcep=	Sintax.normalizeTerms(concepNew.getConceptName());
					owlNormalizedRepConcepsFile.addConcep(normalNewConcep);
			}
		}
	}
	/**
	 * @author Alejandro
	 * @return	Carga para cada Clase de las ontologías sus SubClases e Instancias
	 * 
	 */
	public void loadClass_Sub_Inst(){//Como hacer type cast
		for (Iterator iter = this.namedClases.iterator(); iter.hasNext();) {
			DefaultOWLNamedClass Class = (DefaultOWLNamedClass) iter.next();
			CmapConcept SuperConcep =new CmapConcept(Class.getBrowserText());
			SuperConcep.addConceptWords();
			SuperConcep.setCatgTag("SuperClass");
			// todas las subclases de cada clase
			subClases = Class.getSubclasses(false);
			for (Object object : subClases) {

				CmapConcept SubConcep =new CmapConcept( ((OWLNamedClass)object).getBrowserText() ) ;
				SubConcep.addConceptWords();
				SubConcep.setCatgTag("SubClass");
				CmapLink link= new CmapLink("is a");

				CmapProposition prepSub =new CmapProposition();
				prepSub.setOrigConcept(SubConcep);
				addMapLandedConcep(SubConcep);
				prepSub.setLink(link);
				prepSub.setDestConcept(SuperConcep);
				addMapLandedConcep(SuperConcep);

		//		if(findProp(prepSub,owlSubclass)==null){
					mapLanded.getPropositions().add(prepSub);
					mapLanded.getOwlSubclassProps().add(prepSub);
		//		}
			}
			// todas las instancias de cada clase
			Instance = Class.getInstances(false);
			for (Object object : Instance) {

				CmapConcept InstConcep =new CmapConcept( ((OWLIndividual)object).getBrowserText() );
				InstConcep.addConceptWords();
				InstConcep.setCatgTag("Instancia");
				CmapLink link= new CmapLink("is instance of");

				CmapProposition prepInst =new CmapProposition();
				prepInst.setOrigConcept(InstConcep);
				addMapLandedConcep(InstConcep);
				prepInst.setLink(link);
				prepInst.setDestConcept(SuperConcep);
				addMapLandedConcep(SuperConcep);

		//		if(findProp(prepInst,owlInstannce)==null){
					mapLanded.getPropositions().add(prepInst);
					mapLanded.getOwlInstannceProps().add(prepInst);
			//	}
			}
		}
	}

	/**
	 * @author Alejandro
	 * @return	Carga los Objeto-Propiedad(las propiedades sin restriccion propiedades simples) de las ontologías.
	 * 
	 */
	public void loadObjectProperty(){
		for (Object object : objectProperty) {
			DefaultRDFProperty property=((DefaultRDFProperty)object);

			if(property.isDomainDefined()){//Verificar si tiene un dominio al menos
				if(property.hasRange(false)){//Verificar si tiene un rango al menos

					unionDomains= property.getUnionDomain();//Si es un solo dominio se obtiene ese unico elemento de la union 1u1=1
					unionRange= property.getUnionRangeClasses();//Si es un solo range se obtiene ese unico elemento de la union 1u1=1

					if(unionDomains.size()>0){	
						for (Object dom : unionDomains) {//capturar los datos del dominio(s) correspondiente(s)(Caso de UNION)	
							RDFSClass domain=((RDFSClass)dom);


							if(unionRange.size()>0){//capturar los datos del rango(s) correspondiente(s)(Caso de UNION)	
								for (Object rang : unionRange) {
									RDFSClass range=((RDFSClass)rang);

									CmapConcept DomainConcep =new CmapConcept( domain.getBrowserText() );
									DomainConcep.addConceptWords();
									DomainConcep.setCatgTag("Dominio");
									CmapLink link= new CmapLink(property.getBrowserText());
									CmapConcept RangeConcep= new CmapConcept( range.getBrowserText() );
									RangeConcep.addConceptWords();
									RangeConcep.setCatgTag("Rango");

									CmapProposition prepOP= new CmapProposition();
									prepOP.setOrigConcept(DomainConcep);
									addMapLandedConcep(DomainConcep);
									prepOP.setLink(link);
									prepOP.setDestConcept(RangeConcep);
									addMapLandedConcep(RangeConcep);
									prepOP.setFuntiona(property.isFunctional());

							//		if(findProp(prepOP,owlObjectproperty)==null){
										mapLanded.getPropositions().add(prepOP);
										mapLanded.getOwlObjectpropertyProps().add(prepOP);
							//		}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * @author Alejandro
	 * @return	Carga las propiedades con restriccion del tipo "someValueFrom" y "hasValue"
	 * 
	 */
	public void loadProperty(){
		Iterator iter = this.Properties.iterator();
		boolean found =false;
		int propindex=0;
		while ( iter.hasNext()&& !found) {
			DefaultRDFProperty Proper = (DefaultRDFProperty) iter.next();

			if(!Proper.getName().equals(":FROM"))//..Es apartir de aqui donde realmente comienzan las propiedades especificas de la ontología tratada
				propindex++ ;
			else
				found=true;
		}
		if(!found)//...si se encontro este tope, entonces comenzar a analizar las propiedades desde aqui...
			propindex=50;

		Object[] properties=this.Properties.toArray();
		for (int i = propindex + 1; i < properties.length; i++) {
			RDFProperty  property=((DefaultRDFProperty )properties[i]);

			Restrictions=owlModel.getOWLRestrictionsOnProperty(property);
			for (Object restric : this.Restrictions) {
				
			//	DefaultCls clase =((DefaultCls )restric);
			//	String name= clase.getName();
				OWLRestriction  restriction=((OWLRestriction )restric);
				if(restriction!=null){
					
					OWLNamedClass owner= restriction.getOwner();
					if(owner!=null){
						
						CmapConcept srcPropertyconcep =new CmapConcept( owner.getBrowserText() );//Gas(OrigConcep)
						srcPropertyconcep.addConceptWords();
						srcPropertyconcep.setCatgTag("Clase");
						CmapLink link= new CmapLink(property.getBrowserText());//propiedad made_of
						CmapConcept dstPropertyconcep =new CmapConcept( restriction.getFillerText());//Moleculas(DestConcep)//restriction.getBrowserText()Made of some molecules) 
						dstPropertyconcep.addConceptWords();
						srcPropertyconcep.setCatgTag("ValorProiedad");
						
						CmapProposition prepProperty= new CmapProposition();
						prepProperty.setOrigConcept(srcPropertyconcep);
						addMapLandedConcep(srcPropertyconcep);
						prepProperty.setLink(link);
						prepProperty.setDestConcept(dstPropertyconcep);
						addMapLandedConcep(dstPropertyconcep);

						prepProperty.setFuntiona(property.isFunctional());

						//	if(findProp(prepProperty,owlProperty)==null){
						mapLanded.getPropositions().add(prepProperty);
						mapLanded.getOwlPropertyProps().add(prepProperty);
						//	}
					}
				}
			}
		}
	}

	
	public CmapProposition findProp(CmapProposition p, ArrayList<CmapProposition> propList){

		boolean found = false;
		int index = 0;
		if(propList!=null){
			while ((index <propList.size())&&(!found)) {

				String orig = propList.get(index).getOrigConcept().getConceptName();
				String dest = propList.get(index).getDestConcept().getConceptName();
				CmapLink linkName = propList.get(index).getLink();

				if(orig.equals( p.getOrigConcept().getConceptName() ) )
					if(dest.equals(p.getDestConcept().getConceptName()))
						if(linkName.equals(p.getLink().getLinkName()))
							return propList.get(index);
						else
							index ++;
			}
		}
		return null;
	}
	public FileConcepsManage getOwlNormalizedRepConcepsFile() {
		return owlNormalizedRepConcepsFile;
	}
	public void setOwlNormalizedRepConcepsFile(
			FileConcepsManage owlNormalizedRepConcepsFile) {
		this.owlNormalizedRepConcepsFile = owlNormalizedRepConcepsFile;
	}
	
}










