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
package org.eclipse.ice.client.widgets.test;

import static org.junit.Assert.*;

import org.junit.Test;

import org.eclipse.ice.client.widgets.ExtraInfoDialog;
import org.eclipse.ice.datastructures.form.DataComponent;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class is responsible for testing the ExtraInfoDialog class.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ExtraInfoDialogTester {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The dialog to test.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ExtraInfoDialog extraInfoDialog;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the DataComponent accessors on the ExtraInfoDialog
	 * class.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkAccessors() {
		// begin-user-code

		// Local Declarations
		DataComponent comp = new DataComponent();

		// Initialize the dialog
		extraInfoDialog = new ExtraInfoDialog(null);

		// Setup the DataComponent
		comp.setId(536);
		comp.setName("Extra Info Dialog Test Data Component");
		comp.setName("A component for the ExtraInfoDialogTester that Jay "
				+ "had to create after being the first person to break Bones' "
				+ "new Maven+Tycho build.");

		// Check the dialog's accessors
		assertNull(extraInfoDialog.getDataComponent());
		extraInfoDialog.setDataComponent(comp);
		assertNotNull(extraInfoDialog.getDataComponent());
		assertEquals(comp, extraInfoDialog.getDataComponent());

		return;
		// end-user-code
	}
}