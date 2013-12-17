package similarities;

import classes.Nodes;
import cmaps.CMap;
import cmaps.CmapConcept;

public class CmapSimilarity {
		
		/**
		 * Metodo correspondiente a Combining Concept Mapping with CBR: Towards Experiences-Based Support for Knowledge Modeling
		 * se usa 0.5 como dice el trabajo para ver el peso que tienen los nodos hubs y authorities
		 * @param cmapTarget
		 * @param cmapSource
		 * @return la metrica de similitud S
		 */
		public static double cmapSimilarities(CMap cmapTarget,CMap cmapSource) {
			double result = 0.75*similarityDegree(cmapTarget, cmapSource, Nodes.hub)+0.5*similarityDegree(cmapTarget, cmapSource, Nodes.authority)+0.25*similarityDegree(cmapTarget, cmapSource, Nodes.upper);
			result = Math.round(result*Math.pow(10,2))/Math.pow(10,2);
			return result;
		}
		/**
		 * Metodo para obtener el valor de similitud entre 2 MCs. 
		 * @param cmapTarget: mapa conceptual meta
		 * @param cmapSource: mapa conceptual origen
		 * valor de la K
		 * @return
		 */
		public static double similarityDegree(CMap cmapTarget,CMap cmapSource, Nodes type) {
			double result =0;
			
			for (CmapConcept conceptS : cmapSource.getConcepts()) {
				
				for (CmapConcept conceptT : cmapTarget.getConcepts()) {
				
					int wordCount = similarityBetweenConcept(conceptS, conceptT);
					double similarity = 0;
					
					if(type.equals(Nodes.authority)){
						similarity = wordCount*conceptS.getAWeightLast()*conceptT.getAWeightLast();
						 result += similarity;	
					}
					if(type.equals(Nodes.hub)){
						similarity = wordCount*conceptS.getHWeightLast()*conceptT.getHWeightLast();
						 result += similarity;	
					}
					if(type.equals(Nodes.upper)){
						similarity = wordCount*conceptS.getUWeightLast()*conceptT.getUWeightLast();
						 result += similarity;	
					}
					 
				}
			}
			result = Math.round(result*Math.pow(10,2))/Math.pow(10,2);
			return result;
		}
		
		/**
		 * Metodo para obtener la cantidad de palabras que hay entre 2 conceptos
		 * @param conceptS: concepto fuente
		 * @param conceptT: concepto destino
		 * @return cantidad de terminos en comun entre 2 conceptos
		 */
		public static int similarityBetweenConcept(CmapConcept conceptS, CmapConcept conceptT) {
			int result = 0;
			
			for (String wordSource : conceptS.getConceptWords()) {
				for (String wordTarget : conceptT.getConceptWords()) {
					if (wordSource.equalsIgnoreCase(wordTarget)) 
						result++;		
				}
			}
			return result;
		}
}
