package classes;

import cmaps.CmapConcept;

public class IntersectionProperty {

	private CmapConcept instConcept;
	private CmapConcept concept;
	private String property;
	private CmapConcept conceptValue;
	public IntersectionProperty(CmapConcept instConcept, CmapConcept concept, String property, CmapConcept conceptValue) {
		super();
		this.instConcept = instConcept;
		this.concept = concept;
		this.property = property;
		this.conceptValue = conceptValue;
	}
	public CmapConcept getConcept() {
		return concept;
	}
	public void setConcept(CmapConcept concept) {
		this.concept = concept;
	}
	public CmapConcept getConceptValue() {
		return conceptValue;
	}
	public void setConceptValue(CmapConcept conceptValue) {
		this.conceptValue = conceptValue;
	}
	public CmapConcept getInstConcept() {
		return instConcept;
	}
	public void setInstConcept(CmapConcept instConcept) {
		this.instConcept = instConcept;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
}
