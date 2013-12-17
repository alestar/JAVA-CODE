package classes;

import java.util.ArrayList;

import cmaps.CmapConcept;

public class Union {

	private CmapConcept conceptUnion;
	private ArrayList<CmapConcept> unionList;
	
	public Union(CmapConcept conceptUnion) {
		super();
		this.conceptUnion = conceptUnion;
		this.unionList = new ArrayList<CmapConcept>();
	}
	
	public CmapConcept getConceptUnion() {
		return conceptUnion;
	}
	
	public void setConceptUnion(CmapConcept conceptUnion) {
		this.conceptUnion = conceptUnion;
	}
	
	public ArrayList<CmapConcept> getUnionList() {
		return unionList;
	}
	
	public void setUnionList(ArrayList<CmapConcept> unionList) {
		this.unionList = unionList;
	}
}
