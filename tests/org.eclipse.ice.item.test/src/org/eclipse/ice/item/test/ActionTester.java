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
package org.eclipse.ice.item.test;

import static org.junit.Assert.*;

import org.junit.Test;

import org.eclipse.ice.datastructures.form.Form;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The ActionTester is responsible for testing Actions.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ActionTester {
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private TestAction testAction;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the Action accessors for Forms using a FakeAction.
	 * It only tests the getForm and submitForm() operations, which are
	 * implemented by Action, not FakeAction.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkFormAccessors() {
		// begin-user-code

		// Local Declarations
		Form returnedForm = null, updatedForm = new Form(), secondReturnedForm = null;

		// Initialize the action
		testAction = new TestAction();

		// Retrieve the Form and make sure it is not null
		returnedForm = testAction.getForm();
		assertNotNull(returnedForm);

		// Add something to the Form and re-submit it
		updatedForm.setName("Nurse Chapel");
		testAction.submitForm(updatedForm);

		// Retrieve the Form again and make sure it is correct
		secondReturnedForm = testAction.getForm();
		assertNotNull(secondReturnedForm);
		assertEquals(updatedForm.getName(), secondReturnedForm.getName());

		return;
		// end-user-code
	}
}