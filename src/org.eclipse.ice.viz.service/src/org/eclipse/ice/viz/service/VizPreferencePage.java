package org.eclipse.ice.viz.service;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.ui.IWorkbench;

/**
 * This class manages the main page of visualization service preferences in the
 * Eclipse Window -> Preferences dialog, specifically under the node
 * "Visualization".
 * <p>
 * In addition to the code here, the extension point
 * {@code org.eclipse.ui.preferencePage} must be used. In this case, the
 * extension point in {@code plugin.xml} looks like:
 * 
 * <pre>
 * <code>
 * {@literal<plugin>}
 *    {@literal<extension}
 *          id="org.eclipse.ice.viz.service.preferencePage"
 *          name="Visualization Preferences"
 *          point="org.eclipse.ui.preferencePages"{@literal>}
 *       {@literal<page}
 *             class="org.eclipse.ice.viz.service.VizPreferencePage"
 *             id="org.eclipse.ice.viz.service.preferences"
 *             name="Visualization"{@literal>}
 *       {@literal</page>}
 *    {@literal</extension>}
 * {@literal</plugin>}
 * </code>
 * </pre>
 * 
 * The same method should be used for other preference pages.
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
public class VizPreferencePage extends AbstractVizPreferencePage {

	/**
	 * The default constructor.
	 */
	public VizPreferencePage() {
		super(GRID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	@Override
	public void init(IWorkbench workbench) {
		setDescription("Visualization Preferences");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors
	 * ()
	 */
	@Override
	protected void createFieldEditors() {
		// TODO Add preferences...
		addField(new BooleanFieldEditor("BOOLEAN_VALUE",
				"&This statement is false", getFieldEditorParent()));
	}

}
