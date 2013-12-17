package classes;

import cmaps.CmapConcept;

public class Triple {

	private CmapConcept origConcept;
	private CmapConcept destConcept;
	private String relation;
	
	public Triple() {
		super();
	}
	public Triple(CmapConcept origConcept, CmapConcept destConcept, String relation) {
		super();
		this.origConcept = origConcept;
		this.destConcept = destConcept;
		this.relation = relation;
	}
	public CmapConcept getDestConcept() {
		return destConcept;
	}
	public void setDestConcept(CmapConcept destConcept) {
		this.destConcept = destConcept;
	}
	public CmapConcept getOrigConcept() {
		return origConcept;
	}
	public void setOrigConcept(CmapConcept origConcept) {
		this.origConcept = origConcept;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
}
