/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.datastructures.form.emf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.util.XMLProcessor;
import org.xml.sax.SAXException;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * ICEXMLProcessor is a subclass of the Eclipse Modeling Framework's (EMF)
 * utility XMLProcessor class. It performs all the same functions as
 * XMLProcessor and lets clients create empty XML instance Resources from a valid 
 * XML schema definition.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Alex McCaskey
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ICEXMLProcessor extends XMLProcessor {

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Reference to the XML schema file for the Ecore domain model.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private URI schema;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor, takes an XML schema Java File.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ICEXMLProcessor(URI schemaFile) throws SAXException {
		super(schemaFile);
		// Set the main schema, the one that reference 
		// or includes the others. 
		schema = schemaFile;
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This method creates and returns a new empty XML Resource instance that conforms to 
	 * the given XML schema definition. 
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
//	public Resource createXMLInstance() {
//		// Better way to do this?
//		try {
//			File f = new File(schema.toFileString().replaceAll(".xsd",
//					".xml"));
//			return load(new FileInputStream(f), loadOptions);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

}
