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
package org.eclipse.ice.datastructures.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.datastructures.form.emf.EMFComponent;
import org.eclipse.ice.datastructures.form.emf.EMFTreeComposite;
import org.eclipse.ice.datastructures.form.iterator.BreadthFirstTreeCompositeIterator;
import org.junit.*;

/**
 * 
 * @author Alex McCaskey
 */
public class EMFComponentTester {
	/**
	 * 
	 */
	private EMFComponent emfComponent;

	/**
	 * 
	 */
	@Before
	public void before() {
		// Local Declarations
		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "datastructuresData";
		String filePath = userDir + separator + "shiporder.xsd";

		// Create the DataComponent
		emfComponent = new EMFComponent(new File(filePath));
	}

	/**
	 */
	@Test
	public void checkCreation() {
		// begin-user-code

		// Local declarations
		DataComponent component = null;
		int id = 20110901;
		String name = "September 1st 2011";
		String description = "The 1st day of the ninth month in the year of "
				+ "our Lord 2011";

		// Set the id, name and description
		emfComponent.setId(id);
		emfComponent.setDescription(description);
		emfComponent.setName(name);

		// Check the id, name and description
		assertEquals(emfComponent.getDescription(), description);
		assertEquals(emfComponent.getId(), id);
		assertEquals(emfComponent.getName(), name);

		// Get the RootNode, this should be the first child
		// of the actual TreeComposite root node, which is DocumentRoot
		TreeComposite emfTree = emfComponent.getEMFTreeComposite();
		assertNotNull(emfTree);

		// Check its tree structure
		assertTrue("DocumentRoot".equals(emfTree.getName()));
		assertEquals(0, emfTree.getNumberOfChildren());
		assertEquals(1, emfTree.getChildExemplars().size());
		// assertEquals(1, emfTree.getNumberOfDataNodes());
		// component = (DataComponent) emfTree.getActiveDataNode();
		// assertEquals("ShiporderType Data", component.getName());
		// assertEquals(2, component.retrieveAllEntries().size());
		// assertEquals("orderperson", component.retrieveAllEntries().get(0)
		// .getName());
		// assertEquals("orderperson", component.retrieveAllEntries().get(0)
		// .getValue());
		// assertEquals("orderid",
		// component.retrieveAllEntries().get(1).getName());
		//
		// // Check the first child of RootNode's tree structure
		// TreeComposite child1 = emfTree.getChildAtIndex(0);
		// assertEquals("ShiptoType", child1.getName());
		// assertEquals(0, child1.getNumberOfChildren());
		// assertEquals(1, child1.getNumberOfDataNodes());
		// component = (DataComponent) child1.getActiveDataNode();
		// assertEquals("ShiptoType Data", component.getName());
		// assertEquals(4, component.retrieveAllEntries().size());
		//
		// // Check the second child of RootNode's tree structure
		// TreeComposite child2 = emfTree.getChildAtIndex(1);
		// assertEquals("ItemType", child2.getName());
		// assertEquals(0, child2.getNumberOfChildren());
		// assertEquals(1, child2.getNumberOfDataNodes());
		// component = (DataComponent) child2.getActiveDataNode();
		// assertEquals("ItemType Data", component.getName());
		// assertEquals(4, component.retrieveAllEntries().size());

		return;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	@Test
	public void checkTreeModification() {

		EMFTreeComposite docRoot = (EMFTreeComposite) emfComponent
				.getEMFTreeComposite();
		EMFTreeComposite shipOrder = (EMFTreeComposite) docRoot
				.getChildExemplars().get(0).clone();

		docRoot.setNextChild(shipOrder);
		assertEquals(1, docRoot.getNumberOfChildren());

		assertEquals(1, shipOrder.getNumberOfDataNodes());
		DataComponent component = (DataComponent) shipOrder.getActiveDataNode();
		assertEquals("ShiporderType Data", component.getName());
		assertEquals(2, component.retrieveAllEntries().size());
		assertEquals("orderperson", component.retrieveAllEntries().get(0)
				.getName());
		assertEquals("orderid", component.retrieveAllEntries().get(1).getName());
		assertTrue(component.retrieveEntry("orderperson").setValue("McCaskey"));

		// Check the first child of RootNode's tree structure
		EMFTreeComposite shipTo = (EMFTreeComposite) shipOrder
				.getChildExemplars().get(0).clone();
		assertEquals(0, shipOrder.getNumberOfChildren());
		shipOrder.setNextChild(shipTo);
		assertEquals(1, shipOrder.getNumberOfChildren());
		assertEquals("ShiptoType", shipTo.getName());
		assertEquals(0, shipTo.getNumberOfChildren());
		assertEquals(1, shipTo.getNumberOfDataNodes());
		component = (DataComponent) shipTo.getActiveDataNode();
		assertEquals("ShiptoType Data", component.getName());
		assertEquals(4, component.retrieveAllEntries().size());
		assertTrue(component.retrieveEntry("name").setValue("name"));
		assertTrue(component.retrieveEntry("address").setValue(
				"1600 Pennsylvania Ave"));
		assertTrue(component.retrieveEntry("city").setValue("city"));
		assertTrue(component.retrieveEntry("country").setValue("country"));

		// Check the second child of RootNode's tree structure
		TreeComposite item = (EMFTreeComposite) shipOrder.getChildExemplars()
				.get(1).clone();
		shipOrder.setNextChild(item);
		assertEquals(2, shipOrder.getNumberOfChildren());
		assertEquals("ItemType", item.getName());
		assertEquals(0, item.getNumberOfChildren());
		assertEquals(1, item.getNumberOfDataNodes());
		component = (DataComponent) item.getActiveDataNode();
		assertEquals("ItemType Data", component.getName());
		assertEquals(4, component.retrieveAllEntries().size());
		assertTrue(component.retrieveEntry("title").setValue("POTUS"));
		assertTrue(component.retrieveEntry("note").setValue("NOTES"));
		assertTrue(component.retrieveEntry("quantity").setValue("1"));
		assertTrue(component.retrieveEntry("price").setValue("0.0"));

		EMFTreeComposite anotherItem = (EMFTreeComposite) shipOrder
				.getChildExemplars().get(1).clone();
		shipOrder.setNextChild(anotherItem);
		assertEquals(3, shipOrder.getNumberOfChildren());
		assertEquals("ItemType", anotherItem.getName());
		assertEquals(0, anotherItem.getNumberOfChildren());
		assertEquals(1, anotherItem.getNumberOfDataNodes());
		component = (DataComponent) anotherItem.getActiveDataNode();
		assertEquals("ItemType Data", component.getName());
		assertEquals(4, component.retrieveAllEntries().size());
		assertTrue(component.retrieveEntry("title").setValue("FLOTUS"));
		assertTrue(component.retrieveEntry("note").setValue("NOTES"));
		assertTrue(component.retrieveEntry("quantity").setValue("1"));
		assertTrue(component.retrieveEntry("price").setValue("0.0"));

		emfComponent.save();

	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	@Test
	public void checkBatML() {
		// Local Declarations
		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "datastructuresData";
		
		String filePath1 = userDir + separator
				+ "electrical.xsd";
		String filePath2 = userDir + separator
				+ "electrical.xml";
		ArrayList<String> treeNames = new ArrayList<String>();
		HashMap<String, Integer> verificationMap = new HashMap<String, Integer>();

		EMFComponent batmlEMFComponent = new EMFComponent(null);

		assertTrue(batmlEMFComponent.save());

		assertTrue(batmlEMFComponent.load(new File(filePath1), new File(filePath2)));
		//System.exit(0);

		batmlEMFComponent.save();
		
		assertNotNull(batmlEMFComponent.getEMFTreeComposite().getChildAtIndex(0));
		System.out.println("NAME: " + batmlEMFComponent.getEMFTreeComposite().getName());
		assertTrue(batmlEMFComponent.getEMFTreeComposite().getChildAtIndex(0)
				.getChildAtIndex(0).getName().equals("ModelDBType1"));

		BreadthFirstTreeCompositeIterator iter = new BreadthFirstTreeCompositeIterator(
				batmlEMFComponent.getEMFTreeComposite());

		while (iter.hasNext()) {
			EMFTreeComposite tree = (EMFTreeComposite) iter.next();
			String treeName = tree.getName();
			treeNames.add(treeName);
			if (verificationMap.keySet().contains(treeName)) {
				int newValue = verificationMap.get(treeName) + 1;
				verificationMap.put(treeName, newValue);
			} else {
				verificationMap.put(treeName, 1);
			}
		}

		// Not sure how else to test this except that we have
		// the correct tree nodes.
		assertEquals((Integer) 141,
				(Integer) verificationMap.get("AnySingleValueType"));
		assertEquals((Integer) 141,
				(Integer) verificationMap.get("ParameterType"));
		assertEquals((Integer) 10,
				(Integer) verificationMap.get("ParameterSetType"));
		assertEquals((Integer) 1,
				(Integer) verificationMap.get("ParametersType"));
		assertEquals((Integer) 1,
				(Integer) verificationMap.get("BatteryMLDocType"));
		assertEquals((Integer) 1, (Integer) verificationMap.get("ModelDBType1"));
		assertEquals((Integer) 1, (Integer) verificationMap.get("ModelDBType"));
		assertEquals((Integer) 1,
				(Integer) verificationMap.get("DefinitionType"));
		assertEquals((Integer) 1, (Integer) verificationMap.get("CategoryType"));

	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	@Test
	public void checkSave() {

		// Local Declarations
		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "datastructuresData";
		String filePath = userDir + separator + "shipOrderSave.xml";
		String expectedFilePath = userDir + separator
				+ "expectedShipOrderSave.xml";
		File saveFile = new File(filePath);

		// Modify the Tree to make things interesting
		// This mimics a user filling out the ICE Form Entries
		checkTreeModification();

		// Save the File, make sure it was successful
		assertTrue(emfComponent.save(saveFile));
		assertTrue(saveFile.exists());

		try {
			ArrayList<String> exptectedFileContents = (ArrayList<String>) Files
					.readAllLines(Paths.get(expectedFilePath),
							Charset.defaultCharset());
			ArrayList<String> actualFileContents = (ArrayList<String>) Files
					.readAllLines(Paths.get(filePath), Charset.defaultCharset());
			assertTrue(actualFileContents.equals(exptectedFileContents));
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}

	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	@Test
	public void checkLoad() {
		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "datastructuresData";
		String expectedFilePath = userDir + separator
				+ "expectedShipOrderSave.xml";
		File loadFile = new File(expectedFilePath);

		emfComponent.save();
		assertTrue(emfComponent.load(loadFile));
		emfComponent.save();

		// Get the RootNode, this should be the first child
		// of the actual TreeComposite root node, which is DocumentRoot
		EMFTreeComposite emfTree = (EMFTreeComposite) emfComponent
				.getEMFTreeComposite();
		assertNotNull(emfTree);
		assertEquals("DocumentRoot", emfTree.getName());
		assertEquals(1, emfTree.getNumberOfChildren());

		// Check its tree structure
		EMFTreeComposite shipOrder = (EMFTreeComposite) emfTree
				.getChildAtIndex(0);
		assertEquals(3, shipOrder.getNumberOfChildren());
		assertEquals("ShiporderType", shipOrder.getName());
		assertEquals(1, shipOrder.getNumberOfDataNodes());
		DataComponent component = (DataComponent) shipOrder.getActiveDataNode();
		assertEquals("ShiporderType Data", component.getName());
		assertEquals(2, component.retrieveAllEntries().size());

		// Check the first child of RootNode's tree structure
		TreeComposite child1 = shipOrder.getChildAtIndex(0);
		assertEquals("ShiptoType", child1.getName());
		assertEquals(0, child1.getNumberOfChildren());
		assertEquals(1, child1.getNumberOfDataNodes());
		component = (DataComponent) child1.getActiveDataNode();
		assertEquals("ShiptoType Data", component.getName());
		assertEquals(4, component.retrieveAllEntries().size());

		// Check the second child of RootNode's tree structure
		TreeComposite child2 = shipOrder.getChildAtIndex(1);
		assertEquals("ItemType", child2.getName());
		assertEquals(0, child2.getNumberOfChildren());
		assertEquals(1, child2.getNumberOfDataNodes());
		component = (DataComponent) child2.getActiveDataNode();
		assertEquals("ItemType Data", component.getName());
		assertEquals(4, component.retrieveAllEntries().size());

		TreeComposite child3 = shipOrder.getChildAtIndex(2);
		assertEquals("ItemType", child3.getName());
		assertEquals(0, child3.getNumberOfChildren());
		assertEquals(1, child3.getNumberOfDataNodes());
		component = (DataComponent) child3.getActiveDataNode();
		assertEquals("ItemType Data", component.getName());
		assertEquals(4, component.retrieveAllEntries().size());

		String xmlString = emfComponent.saveToString();
		assertNotNull(xmlString);
		assertEquals(2, xmlString.split(Pattern.quote("<item>"), -1).length - 1);

	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the DataComponent to insure that its equals() and
	 * hashcode() operations work.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkEquality() {
		// begin-user-code
		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "datastructuresData";
		String filePath1 = userDir + separator + "shiporder.xsd";

		// Create DataComponents to test
		EMFComponent component = emfComponent;
		EMFComponent equalComponent = new EMFComponent(new File(filePath1));
		EMFComponent unEqualComponent = new EMFComponent();
		EMFComponent transitiveComponent = new EMFComponent(new File(filePath1));

		// Set ICEObject data
		component.setId(1);
		equalComponent.setId(1);
		transitiveComponent.setId(1);
		unEqualComponent.setId(2);

		component.setName("EMF Equal");
		equalComponent.setName("EMF Equal");
		transitiveComponent.setName("EMF Equal");
		unEqualComponent.setName("EMF UnEqual");

		// Assert equals() is reflexive
		assertTrue(component.equals(component));

		// Assert two equal DataComponents return true
		assertTrue(component.equals(equalComponent));

		// Assert two unequal DataComponents return false
		assertFalse(component.equals(unEqualComponent));

		// Assert the equals() is Symmetric
		assertTrue(component.equals(equalComponent)
				&& equalComponent.equals(component));

		// Assert equals() is transitive
		if (component.equals(equalComponent)
				&& equalComponent.equals(transitiveComponent)) {
			assertTrue(component.equals(transitiveComponent));
		} else {
			fail();
		}

		// Assert equals is consistent
		assertTrue(component.equals(equalComponent)
				&& component.equals(equalComponent)
				&& component.equals(equalComponent));
		assertTrue(!component.equals(unEqualComponent)
				&& !component.equals(unEqualComponent)
				&& !component.equals(unEqualComponent));

		// Assert checking equality with null is false
		assertFalse(component == null);

		// Assert that two equal objects return same hashcode
		assertTrue(component.equals(equalComponent)
				&& component.hashCode() == equalComponent.hashCode());

		// Assert that hashcode is consistent
		assertTrue(component.hashCode() == component.hashCode());

		// Assert that hashcodes from unequal objects are different
		assertTrue(component.hashCode() != unEqualComponent.hashCode());
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the DataComponent to ensure that its copy() and
	 * clone() operations work as specified.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Ignore
	public void checkCopying() {
		// begin-user-code

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the ability of the DataComponent to persist itself
	 * to XML and to load itself from an XML input stream.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkLoadingFromXML() {
		// begin-user-code
		// Local declarations
		int id = 5;
		String name = "Bob";
		String description = "I am Bob! 1.0";
		ArrayList<Double> values = new ArrayList<Double>();

		// Add allowedvalues to arraylist
		values.add(1.0);
		values.add(10.0);

		// set ICEObject info
		emfComponent.setId(id);
		emfComponent.setName(name);
		emfComponent.setDescription(description);

		// Load it into XML
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		emfComponent.persistToXML(outputStream);

		assertNotNull(outputStream);
		String xmlFile2 = new String(outputStream.toByteArray());
		System.err.println(xmlFile2);

		// convert information inside of outputStream to inputStream
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				outputStream.toByteArray());

		// load contents into xml
		EMFComponent loadEMF = new EMFComponent();
		loadEMF.loadFromXML(inputStream);

		// Check contents
		// assertTrue(loadEMF.equals(emfComponent));

		// Try to pass null into the operations

		loadEMF.loadFromXML(null);
		// Nothing happens - check comparison

		// Check contents
		// assertTrue(loadEMF.equals(emfComponent));

		// Pass a bad file
		String xmlFile = "I AM NOT AN XML FILE!  NO LEFT OR RIGHT CARROTS!";

		inputStream = new ByteArrayInputStream(xmlFile.getBytes());

		// Run operation
		loadEMF.loadFromXML(inputStream);

		// Check contents
		// assertTrue(loadEMF.equals(emfComponent));
		// end-user-code
	}
}