/**
 * 
 */
package edu.ucdenver.ccp.datasource.identifiers.network.neo4j.skos;

import org.neo4j.graphdb.RelationshipType;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public enum Neo4jSkosMatchRelationshipType implements RelationshipType {
	SKOS_EXACT_MATCH,
	SKOS_CLOSE_MATCH,
	SKOS_RELATED_MATCH;
}
