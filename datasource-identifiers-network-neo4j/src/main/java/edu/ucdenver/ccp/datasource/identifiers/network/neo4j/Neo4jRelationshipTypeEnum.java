/**
 * 
 */
package edu.ucdenver.ccp.datasource.identifiers.network.neo4j;

import org.neo4j.graphdb.RelationshipType;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 *
 */
public interface Neo4jRelationshipTypeEnum extends RelationshipType {

	
	public RelationshipType valueOf(String name);
	
}
