/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.client.widgets.mesh.properties;

import org.eclipse.ice.datastructures.form.mesh.BezierEdge;
import org.eclipse.ice.datastructures.form.mesh.BoundaryCondition;
import org.eclipse.ice.datastructures.form.mesh.BoundaryConditionType;
import org.eclipse.ice.datastructures.form.mesh.Edge;
import org.eclipse.ice.datastructures.form.mesh.Hex;
import org.eclipse.ice.datastructures.form.mesh.IMeshPart;
import org.eclipse.ice.datastructures.form.mesh.IMeshPartVisitor;
import org.eclipse.ice.datastructures.form.mesh.MeshComponent;
import org.eclipse.ice.datastructures.form.mesh.Polygon;
import org.eclipse.ice.datastructures.form.mesh.PolynomialEdge;
import org.eclipse.ice.datastructures.form.mesh.Quad;
import org.eclipse.ice.datastructures.form.mesh.Vertex;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ISection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

/**
 * This class provides an {@link ISection} for displaying the information of an
 * {@link BoundaryCondition} in a modifiable manner.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class BoundaryConditionSection extends AbstractPropertySection {

	/**
	 * The BoundaryCondition whose settings are being displayed and/or modified.
	 */
	private BoundaryCondition condition;

	/**
	 * This is an enum for supported types of BoundaryConditions, e.g., Fluid,
	 * Thermal, or Other (passive scalar).
	 * 
	 * @author Jordan
	 * 
	 */
	protected enum Type {
		Fluid, Thermal, Other;
	}

	// ---- Section configuration ---- //
	/**
	 * The type of boundary condition, e.g., fluid, thermal, or passive scalar.
	 */
	private final Type type;
	/**
	 * The index of the boundary condition if the type is other (passive
	 * scalar).
	 */
	private final int otherIndex;
	/**
	 * The ID of the associated Polygon or the index of the associated Edge in
	 * the selected polygon.
	 */
	private final int id;
	// ------------------------------- //

	// ---- BoundaryConditionType property ---- //
	/**
	 * A Combo widget for selecting from the BoundaryConditionType enum.
	 */
	private Combo typeCombo;
	/**
	 * A SelectionListener that listens to changes in {@link #typeCombo}.
	 */
	private final SelectionListener typeListener;
	/**
	 * A Label that displays the number of required parameters for the current
	 * BoundaryConditionType.
	 */
	private Label numberLabel;
	// ---------------------------------------- //

	// ---- BoundaryCondition parameter values ---- //
	/**
	 * An array of Text widgets for the BoundaryCondition parameters.
	 */
	private final Text[] valueTexts = new Text[5];
	/**
	 * A ModifyListener that updates the BoundaryCondition's values based on
	 * {@link #valueTexts}.
	 */
	private final ModifyListener modifyListener;
	/**
	 * A VerifyListener that ensures that the text in a Text widget is a
	 * parseable Float.
	 */
	private final VerifyListener verifyListener;
	// -------------------------------------------- //

	/**
	 * A List of disposable widgets used in the Section.
	 */
	private final List<Control> disposableControls = new ArrayList<Control>();

	/**
	 * The default constructor for a BoundaryConditionSection. This is used when
	 * either an Edge or a Polygon is selected.
	 * 
	 * @param type
	 *            The type of boundary condition, e.g., fluid, thermal, or
	 *            passive scalar.
	 * @param otherIndex
	 *            The index of the boundary condition if the type is other
	 *            (passive scalar).
	 * @param id
	 *            The ID of the associated Polygon or the index of the
	 *            associated Edge in the selected polygon.
	 */
	public BoundaryConditionSection(Type type, int otherIndex, int id) {
		// Set the necessary settings for this BoundaryConditionSection. These
		// will be used in the setInput section to determine the appropriate
		// BoundaryCondition instance to expose.
		this.type = (type != null ? type : Type.Fluid);
		this.otherIndex = (otherIndex >= 0 ? otherIndex : 0);
		this.id = (id >= 0 ? id : 0);

		// Initialize the SelectionListener for typeCombo.
		typeListener = new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				// Convert the String from typeCombo to a BoundaryConditionType.
				String typeString = typeCombo.getText();
				BoundaryConditionType type = BoundaryConditionType
						.valueOf(typeString);

				// If possible, set the type of the BoundaryCondition.
				if (condition != null && type != null) {
					condition.setType(type);
					// Also update the number of required parameters.
					numberLabel.setText(Integer
							.toString(type.numberOfParameters));
					numberLabel.pack();
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		};

		// Initialize the ModifyListener to set the values of the
		// BoundaryCondition.
		modifyListener = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (condition != null) {
					// Create an ArrayList of the appropriate size.
					int size = valueTexts.length;
					ArrayList<Float> values = new ArrayList<Float>(size);

					// Add the current text values to the ArrayList as floats.
					for (int i = 0; i < size; i++) {
						values.add(Float.parseFloat(valueTexts[i].getText()));
					}

					// Set the values for the condition.
					condition.setValues(values);
				}
				return;
			}
		};

		// Initialize the VerifyListener to validate the input in the value Text
		// fields.
		verifyListener = new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				// Get the character. If it's not a digit and the current text
				// is not a valid float, then cancel the action.
				if (!Character.isDigit(e.character)) {
					try {
						Float.parseFloat(e.text);
					} catch (NumberFormatException ex) {
						e.doit = false;
					}
				}
				return;
			}
		};

		return;
	}

	/**
	 * Creates the controls displayed in the section.
	 */
	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		// Call the super method first.
		super.createControls(parent, aTabbedPropertySheetPage);

		// parent has a FillLayout!
		GridData gridData;

		// Get the background color.
		Color bg = parent.getBackground();

		// Create a Composite to contain the boundary condition information.
		Composite composite = new Group(parent, SWT.NONE);
		disposableControls.add(composite);
		composite.setLayout(new GridLayout(1, false));
		composite.setBackground(bg);

		// ---- Create the header label. ---- //
		// Create the header label, e.g. "Fluid Boundary Condition".
		Label label = new Label(composite, SWT.LEFT);
		disposableControls.add(label);
		if (type == Type.Other) {
			label.setText("Passive Scalar Boundary Condition " + otherIndex);
		} else {
			label.setText(type.toString() + " Boundary Condition");
		}
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		label.setBackground(bg);
		// ---------------------------------- //

		// ---- Create the type section. ---- //
		// Create the row of data showing the boundary condition type.
		Composite typeComposite = new Group(composite, SWT.NONE);
		disposableControls.add(typeComposite);
		typeComposite.setBackground(bg);
		// Set the GridData for the typeComposite to fill horizontally and be
		// centered vertically.
		gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		typeComposite.setLayoutData(gridData);
		// Set a default RowLayout for the typeComposite.
		typeComposite.setLayout(new GridLayout(4, false));

		// Create a label for the BoundaryConditionType.
		label = new Label(typeComposite, SWT.RIGHT);
		disposableControls.add(label);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		label.setText("Type:");
		label.setBackground(bg);

		// Create a Combo for the BoundaryConditionType.
		typeCombo = new Combo(typeComposite, SWT.DROP_DOWN | SWT.BORDER);
		disposableControls.add(typeCombo);
		typeCombo
				.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		// Get the items for the Combo from the BoundaryConditionType enum.
		BoundaryConditionType[] types = BoundaryConditionType.values();
		String[] items = new String[types.length];
		for (int i = 0; i < types.length; i++) {
			items[i] = types[i].toString();
		}
		typeCombo.setItems(items);
		// Add the SelectionListener to typeCombo.
		typeCombo.addSelectionListener(typeListener);

		// Create a label for the number of parameters required for the current
		// BoundaryConditionType.
		label = new Label(typeComposite, SWT.RIGHT);
		disposableControls.add(label);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		label.setText("# of required parameters:");
		label.setBackground(bg);

		// Create a label that shows the current number of required parameters.
		numberLabel = new Label(typeComposite, SWT.LEFT);
		disposableControls.add(numberLabel);
		numberLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
				false));
		numberLabel.setText("N/A");
		numberLabel.setBackground(bg);
		// ---------------------------------- //

		// ---- Create the float value section. ---- //
		// Create the row of float values. This is a row of Text fields.
		Composite valueComposite = new Group(composite, SWT.NONE);
		disposableControls.add(valueComposite);
		valueComposite.setBackground(bg);
		// Set the GridData for the valueComposite to fill horizontally and be
		// centered vertically.
		valueComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));
		// Set a RowLayout for the valueComposite with a little extra spacing
		// between the widgets.
		valueComposite.setLayout(new GridLayout(6, false));

		// Create a label that says "Values:".
		label = new Label(valueComposite, SWT.LEFT);
		disposableControls.add(label);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		label.setText("Values:");
		label.setBackground(bg);

		// Create the Texts for the float values.
		for (int i = 0; i < valueTexts.length; i++) {
			Text value = new Text(valueComposite, SWT.SINGLE | SWT.BORDER);
			value.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			value.setText("0.0");
			disposableControls.add(value);
			valueTexts[i] = value;
			// Add the Verify and Modify listeners to the Text field.
			value.addVerifyListener(verifyListener);
			value.addModifyListener(modifyListener);
		}
		// ----------------------------------------- //

		return;
	}

	/**
	 * This should get a BoundaryCondition instance from the current selection
	 * and set {@link #condition}.
	 */
	@Override
	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);

		// Get the BoundaryCondition from the selection if possible.
		getBoundaryConditionFromSelection(selection);
	}

	private boolean getBoundaryConditionFromSelection(ISelection selection) {
		boolean changed = false;

		// First, make sure the selection is valid. It should be some type of
		// IStructuredSelection.
		if (selection != null && selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;

			// TODO Incorporate a selection of multiple mesh parts.

			// For now, we are only dealing with the first available element in
			// the
			// selection.
			if (!structuredSelection.isEmpty()
					&& structuredSelection.getFirstElement() instanceof MeshSelection) {

				// Get the MeshSelection.
				MeshSelection meshSelection = (MeshSelection) structuredSelection
						.getFirstElement();

				// Get the selected IMeshPart and mesh from the selection.
				IMeshPart meshPart = meshSelection.selectedMeshPart;
				final MeshComponent mesh = meshSelection.mesh;

				// Create a visitor that can determine the appropriate Vertex
				// instance whose properties are being exposed based on the type
				// of
				// IMeshPart passed in through the selection.
				IMeshPartVisitor visitor = new IMeshPartVisitor() {
					public void visit(MeshComponent mesh) {
						// Do nothing.
					}

					public void visit(Polygon polygon) {
						// First, get the corresponding edge ID.
						ArrayList<Edge> edges = polygon.getEdges();
						if (id < edges.size()) {
							int edgeId = edges.get(id).getId();

							// Now get the condition based on the type of
							// boundary
							// condition we are displaying.
							if (type == Type.Fluid) {
								condition = polygon
										.getFluidBoundaryCondition(edgeId);
							} else if (type == Type.Thermal) {
								condition = polygon
										.getThermalBoundaryCondition(edgeId);
							} else {
								condition = polygon.getOtherBoundaryCondition(
										edgeId, otherIndex);
							}
						}
						return;
					}

					public void visit(Quad quad) {
						// Re-direct to the standard polygon operation for now.
						visit((Polygon) quad);
					}

					public void visit(Hex hex) {
						// Re-direct to the standard polygon operation for now.
						visit((Polygon) hex);
					}

					public void visit(Edge edge) {
						// Get the ID of the edge.
						int edgeId = edge.getId();

						// First, get the polygon according to the id variable.
						ArrayList<Polygon> polygons = mesh
								.getPolygonsFromEdge(edgeId);
						if (id < polygons.size()) {
							Polygon polygon = polygons.get(id);

							// Now get the condition based on the type of
							// boundary
							// condition we are displaying.
							if (type == Type.Fluid) {
								condition = polygon
										.getFluidBoundaryCondition(edgeId);
							} else if (type == Type.Thermal) {
								condition = polygon
										.getThermalBoundaryCondition(edgeId);
							} else {
								condition = polygon.getOtherBoundaryCondition(
										edgeId, otherIndex);
							}
						}
					}

					public void visit(BezierEdge edge) {
						// Re-direct to the standard edge operation for now.
						visit((Edge) edge);
					}

					public void visit(PolynomialEdge edge) {
						// Re-direct to the standard edge operation for now.
						visit((Edge) edge);
					}

					public void visit(Vertex vertex) {
						// Do nothing.
					}

					public void visit(Object object) {
						// Do nothing.
					}
				};

				// Reset the condition and set it based on the visited
				// IMeshPart.
				BoundaryCondition tmpCondition = condition;
				condition = null;
				meshPart.acceptMeshVisitor(visitor);

				// Set the flag to true if the references are different.
				changed = (condition != tmpCondition);
			}
		}

		return changed;
	}

	@Override
	public void dispose() {
		// Reset the condition.
		condition = null;

		// Dispose of all disposable controls. If they are in the list, they
		// were created in createControls.
		for (Control control : disposableControls) {
			if (!control.isDisposed()) {
				control.dispose();
			}
		}
		// Clear the list so that they are not disposed again.
		disposableControls.clear();

		return;
	}

	@Override
	public void refresh() {

		// If there is a valid BoundaryCondition, refresh all of the
		// editable controls.
		if (condition != null) {
			BoundaryConditionType type = condition.getType();

			// Update the BoundaryConditionType Combo.
			if (typeCombo != null) {
				typeCombo.setText(type.toString());
				typeCombo.setEnabled(true);
			}

			// Update the label for the number of required parameters.
			if (numberLabel != null) {
				numberLabel.setText(Integer.toString(type.numberOfParameters));
			}
			// Update the value Texts.
			ArrayList<Float> values = condition.getValues();
			for (int i = 0; i < values.size(); i++) {
				if (valueTexts[i] != null) {
					valueTexts[i].setText(values.get(i).toString());
					valueTexts[i].setEnabled(true);
				}
			}
		}
		// Otherwise, disable all of the editable controls.
		else {
			if (typeCombo != null) {
				typeCombo.setText("");
				typeCombo.setEnabled(false);
			}
			if (numberLabel != null) {
				numberLabel.setText("N/A");
				numberLabel.pack();
			}
			for (int i = 0; i < valueTexts.length; i++) {
				Text text = valueTexts[i];
				if (text != null) {
					text.setText("0.0");
					text.setEnabled(false);
				}
			}
		}

		return;
	}
}
