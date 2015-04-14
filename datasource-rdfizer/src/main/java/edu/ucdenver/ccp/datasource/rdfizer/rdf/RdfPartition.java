package edu.ucdenver.ccp.rdfizer.rdf;

/**
 * RDF partition to be used as hierarchical element in generating RDF URIs.
 *
 */
public enum RdfPartition {
//	/** Information content entities (ICEs) will be organized into a unique namespaces per source.<p> 
//	 * For example, the OMIM would have the following namespace: {@literal http://kabob.ucdenver.edu/ice/omim/}
//	 */
//	ICE,
	
	/** Information Artifact Ontology (IAO) - {@link http://code.google.com/p/information-artifact-ontology/} */
	IAO, 
	
	/** Produced concepts relating to biology will have a namespace that starts with {@literal http://kabob.ucdenver.edu/bio/ } */
	BIO,
	
	/** Concepts relating to Relation Ontology (RO) will have a namespace that starts with {@literal http://kabob.ucdenver.edu/ro/ } */
	RO;
}
