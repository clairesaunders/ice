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

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.ice.datastructures.form.Entry;

/**
 * EMFEntry is a subclass of Entry that modifies a corresponding EAttribute
 * instance each time its value is changed.
 * 
 * @author Alex McCaskey
 * 
 */
public class EMFEntry extends Entry {

	/**
	 * Reference to the EObject that contains the EAttribute that this EMFEntry
	 * corresponds to.
	 * 
	 */
	@XmlTransient
	private EObject containingEcoreNode;

	/**
	 * Reference to the EAttribute metadata that this EMFEntry wraps.
	 * 
	 */
	@XmlTransient
	private EAttribute entryMetaData;

	/**
	 * Reference to the EAttribute instance type
	 */
	@XmlTransient
	private String typeName;

	/**
	 * The nullary constructor
	 * 
	 */
	public EMFEntry() {
		super();
	}

	/**
	 * The constructor
	 * 
	 */
	public EMFEntry(EAttribute attribute, EObject ecoreNode) {
		super();
		// Set the data
		entryMetaData = attribute;
		containingEcoreNode = ecoreNode;
		setName(entryMetaData.getName());
		typeName = attribute.getEType().getInstanceClassName();

		// Initialize this EAttribute on the containing Ecore node
		if (containingEcoreNode != null) {
			if (typeName.equals("java.lang.String")) {
				containingEcoreNode.eSet(entryMetaData, "");
			} else if (typeName.equals("java.math.BigInteger")) {
				BigInteger b = new BigInteger("0");
				containingEcoreNode.eSet(entryMetaData, b);
			} else if (typeName.equals("java.math.BigDecimal")) {
				BigDecimal d = new BigDecimal(0.0);
				containingEcoreNode.eSet(entryMetaData, d);
			}
		}
	}

	/**
	 * The constructor
	 * 
	 */
	public EMFEntry(EAttribute attribute, EObject ecoreNode,
			boolean initializeDefaultValues) {
		super();
		// Set the data
		entryMetaData = attribute;
		containingEcoreNode = ecoreNode;
		setName(entryMetaData.getName());
		typeName = attribute.getEType().getInstanceClassName();

		if (containingEcoreNode != null) {
			if (typeName != null && initializeDefaultValues
					&& entryMetaData.getUpperBound() != -1) {
				// Initialize this EAttribute on the containing Ecore node
				if (typeName.equals("java.lang.String")) {
					// System.out.println(entryMetaData.getName());
					containingEcoreNode.eSet(entryMetaData, "");
				} else if (typeName.equals("java.math.BigInteger")) {
					BigInteger b = new BigInteger("0");
					containingEcoreNode.eSet(entryMetaData, b);
				} else if (typeName.equals("java.math.BigDecimal")) {
					BigDecimal d = new BigDecimal(0.0);
					containingEcoreNode.eSet(entryMetaData, d);
				}
			}
		}
	}

	/**
	 * This method overrides Entry.setValue to additionally modify the
	 * EAttribute in the EMF Ecore model tree.
	 * 
	 */
	@Override
	public boolean setValue(String newValue) {
		if (super.setValue(newValue)) {
			try {
				if (typeName.equals("java.lang.String")) {
					containingEcoreNode.eSet(entryMetaData, newValue);
				} else if (typeName.equals("java.math.BigInteger")) {
					BigInteger b = new BigInteger(newValue);
					containingEcoreNode.eSet(entryMetaData, b);
				} else if (typeName.equals("java.math.BigDecimal")) {
					BigDecimal d = new BigDecimal(newValue);
					containingEcoreNode.eSet(entryMetaData, d);
				} else {
					throw new IllegalArgumentException(
							"Unsupported Data Type for the EMFEntry.");
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}

		return false;
	}

	/**
	 * This method provides a copy of the EMFEntry.
	 * 
	 */
	public void copy(EMFEntry otherEntry) {
		if (otherEntry == null) {
			return;
		}

		super.copy((Entry) otherEntry);

		// containingEcoreNode = EcoreUtil.copy(otherEntry.containingEcoreNode);
		// entryMetaData = EcoreUtil.copy(otherEntry.entryMetaData);
		typeName = otherEntry.typeName;

		return;
	}

	/**
	 * This operation provides a deep copy of the EMFEntry.
	 * 
	 */
	public Object clone() {
		// begin-user-code
		EMFEntry entry = new EMFEntry(entryMetaData,
				EcoreUtil.create(entryMetaData.getEContainingClass()), false);
		entry.copy(this);
		return entry;
		// end-user-code
	}
}
