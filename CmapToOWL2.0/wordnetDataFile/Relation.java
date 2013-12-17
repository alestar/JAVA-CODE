package wordnetDataFile;

public class Relation {
	private String pointer_symbol;//tipo de relacion entre los sentidos origen/destino (ej:Hypernym Instance, Hypernym, Hyponym)
	private String source_target;//Es un campo de 4 byte que, contiene 2 pares de 2 dígitos hexadecimales enteros. 
								//Los dos primeros dígitos indican el “número de la palabra” (word) en el actual sentido origen.
							   //Los dos últimos indican lo mismo pero para el sentido destino. 
							//Un valor 0000 significa que el pointer_symbol representa una relación semántica entre el sentido-origen (indicado por synset_offset)/sentido-destino. 
	private String target_offset;//sysnset destino al cual apunta el sentido origen
	private char target_pos;//categoría gramatical del sentido destino
	
	public Relation(String pointer_symbol, String target_offset, char target_pos, String source_target) {
		this.pointer_symbol = pointer_symbol;
		this.source_target = source_target;
		this.target_offset = target_offset;
		this.target_pos = target_pos;
	}
	
	public boolean equals(Object object) {
		if(object == null || (object instanceof Relation == false))
			return false;
		Relation rel = (Relation) object;
		return pointer_symbol.equals(rel.pointer_symbol) && source_target.equals(rel.source_target) 
		&& target_offset.equals(rel.target_offset) && target_pos == rel.target_pos;
	}
	
	public boolean equalType(Object object) {
		if(object == null)
			return false;
		Relation rel = (Relation) object;
		return pointer_symbol.equals(rel.pointer_symbol) && source_target.equals(rel.source_target);
	}
	
	public String getPointer_symbol() {
		return pointer_symbol;
	}
	
	public void setPointer_symbol(String pointer_symbol) {
		this.pointer_symbol = pointer_symbol;
	}
	
	public String getSource_target() {
		return source_target;
	}
	
	public void setSource_target(String source_target) {
		this.source_target = source_target;
	}

	public String getTarget_offset() {
		return target_offset;
	}

	public void setTarget_offset(String target_offset) {
		this.target_offset = target_offset;
	}

	public char getTarget_pos() {
		return target_pos;
	}

	public void setTarget_pos(char target_pos) {
		this.target_pos = target_pos;
	}
	
	public String toString() {
		return shortDesc() + " " + target_offset + "-" + target_pos;
	}
	
	public String shortDesc() {
		return pointer_symbol + "-" + source_target;
	}


}
