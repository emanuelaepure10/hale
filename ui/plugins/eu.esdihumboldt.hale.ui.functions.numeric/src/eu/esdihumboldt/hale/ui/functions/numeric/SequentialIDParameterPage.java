/*
 * Copyright (c) 2012 Data Harmonisation Panel
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     HUMBOLDT EU Integrated Project #030962
 *     Data Harmonisation Panel <http://www.dhpanel.eu>
 */

package eu.esdihumboldt.hale.ui.functions.numeric;

import java.util.List;

import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import eu.esdihumboldt.cst.functions.numeric.sequentialid.SequentialIDConstants;
import eu.esdihumboldt.hale.common.align.extension.function.FunctionUtil;
import eu.esdihumboldt.hale.common.align.model.Cell;
import eu.esdihumboldt.hale.common.align.model.Entity;
import eu.esdihumboldt.hale.common.align.model.ParameterValue;
import eu.esdihumboldt.hale.common.align.transformation.function.TransformationVariables;
import eu.esdihumboldt.hale.common.align.transformation.function.impl.DefaultTransformationVariables;
import eu.esdihumboldt.hale.common.core.io.project.ProjectInfoService;
import eu.esdihumboldt.hale.common.core.io.project.ProjectVariables;
import eu.esdihumboldt.hale.common.schema.model.Definition;
import eu.esdihumboldt.hale.common.schema.model.PropertyDefinition;
import eu.esdihumboldt.hale.common.schema.model.TypeDefinition;
import eu.esdihumboldt.hale.common.schema.model.constraint.type.ValidationConstraint;
import eu.esdihumboldt.hale.ui.HaleUI;
import eu.esdihumboldt.hale.ui.HaleWizardPage;
import eu.esdihumboldt.hale.ui.function.generic.pages.AbstractParameterPage;
import eu.esdihumboldt.hale.ui.service.project.ProjectVariablesContentProposalProvider;
import eu.esdihumboldt.hale.ui.util.viewer.EnumContentProvider;
import eu.esdihumboldt.util.validator.Validator;

/**
 * Parameter page for the sequential ID function.
 * 
 * @author Simon Templer
 */
public class SequentialIDParameterPage extends AbstractParameterPage
		implements SequentialIDConstants {

	private ComboViewer sequence;

	private Text prefix;

	private Text suffix;

	private Text startValue;

	private Label example;

	private ControlDecoration exampleDecoration;

	private final ProjectVariablesContentProposalProvider contentProposalProvider = new ProjectVariablesContentProposalProvider(
			true);

	/**
	 * Default constructor.
	 */
	public SequentialIDParameterPage() {
		super(FunctionUtil.getPropertyFunction(ID, HaleUI.getServiceProvider()),
				"Please configure the identifier generation");
	}

	/**
	 * @see AbstractParameterPage#getConfiguration()
	 */
	@Override
	public ListMultimap<String, ParameterValue> getConfiguration() {
		ListMultimap<String, ParameterValue> result = ArrayListMultimap.create(3, 1);

		if (sequence != null) {
			ISelection sel = sequence.getSelection();
			if (!sel.isEmpty() && sel instanceof IStructuredSelection) {
				result.put(PARAM_SEQUENCE, new ParameterValue(
						((Sequence) ((IStructuredSelection) sel).getFirstElement()).name()));
			}
		}

		if (prefix != null) {
			result.put(PARAM_PREFIX, new ParameterValue(prefix.getText()));
		}

		result.put(PARAM_START_VALUE, new ParameterValue(startValue.getText()));

		if (suffix != null) {
			result.put(PARAM_SUFFIX, new ParameterValue(suffix.getText()));
		}

		return result;
	}

	/**
	 * @see HaleWizardPage#createContent(Composite)
	 */
	@Override
	protected void createContent(Composite page) {
		page.setLayout(GridLayoutFactory.swtDefaults().numColumns(2).spacing(10, 8).create());

		Label label;
		GridDataFactory labelLayout = GridDataFactory.swtDefaults().align(SWT.END, SWT.CENTER);
		GridDataFactory controlLayout = GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER)
				.grab(true, false);

		// select sequence type
		if (getParametersToHandle().containsKey(PARAM_SEQUENCE)) {
			label = new Label(page, SWT.NONE);
			label.setText("Sequence");
			labelLayout.applyTo(label);

			sequence = new ComboViewer(page);
			sequence.setContentProvider(EnumContentProvider.getInstance());
			sequence.setLabelProvider(new LabelProvider() {

				@Override
				public String getText(Object element) {
					if (element instanceof Sequence) {
						switch ((Sequence) element) {
						case overall:
							return "Over all sequential IDs";
						case type:
							return "Per target instance type";
						}
					}

					return super.getText(element);
				}

			});
			sequence.setInput(Sequence.class);
			controlLayout.applyTo(sequence.getControl());

			Sequence initialValue = Sequence.valueOf(getOptionalInitialValue(PARAM_SEQUENCE,
					new ParameterValue(Sequence.type.name())).as(String.class));
			sequence.setSelection(new StructuredSelection(initialValue));

			sequence.addSelectionChangedListener(new ISelectionChangedListener() {

				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					updateStatus();
				}
			});
		}

		// specify prefix
		if (getParametersToHandle().containsKey(PARAM_PREFIX)) {
			label = new Label(page, SWT.NONE);
			label.setText("Prefix");
			labelLayout.applyTo(label);

			prefix = new Text(page, SWT.SINGLE | SWT.BORDER);
			controlLayout.applyTo(prefix);

			prefix.setText(
					getOptionalInitialValue(PARAM_PREFIX, new ParameterValue("")).as(String.class));

			prefix.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					updateStatus();
				}
			});

			ContentProposalAdapter adapter = new ContentProposalAdapter(prefix,
					new TextContentAdapter(), contentProposalProvider,
					ProjectVariablesContentProposalProvider.CTRL_SPACE, new char[] { '{' });
			adapter.setAutoActivationDelay(0);

			final ControlDecoration infoDeco = new ControlDecoration(prefix, SWT.TOP | SWT.LEFT);
			infoDeco.setDescriptionText("Type Ctrl+Space for project variable content assistance");
			infoDeco.setImage(FieldDecorationRegistry.getDefault()
					.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
			infoDeco.setMarginWidth(2);
		}

		// specify suffix
		if (getParametersToHandle().containsKey(PARAM_SUFFIX)) {
			label = new Label(page, SWT.NONE);
			label.setText("Suffix");
			labelLayout.applyTo(label);

			suffix = new Text(page, SWT.SINGLE | SWT.BORDER);
			controlLayout.applyTo(suffix);

			suffix.setText(
					getOptionalInitialValue(PARAM_SUFFIX, new ParameterValue("")).as(String.class));

			suffix.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					updateStatus();
				}
			});

			ContentProposalAdapter adapter = new ContentProposalAdapter(suffix,
					new TextContentAdapter(), contentProposalProvider,
					ProjectVariablesContentProposalProvider.CTRL_SPACE, new char[] { '{' });
			adapter.setAutoActivationDelay(0);

			final ControlDecoration infoDeco = new ControlDecoration(suffix, SWT.TOP | SWT.LEFT);
			infoDeco.setDescriptionText("Type Ctrl+Space for project variable content assistance");
			infoDeco.setImage(FieldDecorationRegistry.getDefault()
					.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
			infoDeco.setShowOnlyOnFocus(true);
		}

		// specify startValue for the sequence generation
		if (getParametersToHandle().containsKey(PARAM_START_VALUE)) {
			label = new Label(page, SWT.NONE);
			label.setText("StartValue");
			labelLayout.applyTo(label);

			startValue = new Text(page, SWT.SINGLE | SWT.BORDER);
			controlLayout.applyTo(startValue);

			startValue.setText(getOptionalInitialValue(PARAM_START_VALUE, new ParameterValue("1"))
					.as(String.class));

			startValue.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					updateStatus();
				}
			});

			ContentProposalAdapter adapter = new ContentProposalAdapter(startValue,
					new TextContentAdapter(), contentProposalProvider,
					ProjectVariablesContentProposalProvider.CTRL_SPACE, new char[] { '{' });
			adapter.setAutoActivationDelay(0);

			final ControlDecoration infoDeco = new ControlDecoration(startValue,
					SWT.TOP | SWT.LEFT);
			infoDeco.setDescriptionText("Type Ctrl+Space for project variable content assistance");
			infoDeco.setImage(FieldDecorationRegistry.getDefault()
					.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION).getImage());
			infoDeco.setShowOnlyOnFocus(true);
		}

		if (sequence != null && prefix != null && suffix != null && startValue != null) {
			label = new Label(page, SWT.NONE);
			label.setText("ID Example");
			labelLayout.applyTo(label);

			example = new Label(page, SWT.NONE);
			example.setFont(JFaceResources.getTextFont());
			controlLayout.applyTo(example);

			// error decoration
			exampleDecoration = new ControlDecoration(example, SWT.LEFT | SWT.TOP, page);
			FieldDecoration fieldDecoration = FieldDecorationRegistry.getDefault()
					.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR);
			exampleDecoration.setImage(fieldDecoration.getImage());
			exampleDecoration.hide();
		}

		updateStatus();
	}

	/**
	 * Update the example and the page status
	 */
	protected void updateStatus() {
		setErrorMessage(null);

		try {

			int id = Integer.parseInt(startValue.getText());
			if (id < 0) {
				setErrorMessage(
						"ERROR: the start value for ID creation should be a positive integer!");
				example.setText("Cannot generate a valid example");
				setPageComplete(false);
				return;
			}

		} catch (Exception e) {
			setErrorMessage("ERROR: the start value for ID creation should be an integer!");
			example.setText("Cannot generate a valid example");
			setPageComplete(false);
			return;
		}

		if (example != null) {
			String exampleStr = generateExample();
			example.setText(exampleStr);

			boolean valid = validateValue(exampleStr);
			if (!valid) {
				setPageComplete(false);
				setErrorMessage("The generated identifier is not valid for the target property");
			}
			else {
				setErrorMessage(null);
			}
		}

		if (sequence != null && sequence.getSelection().isEmpty()) {
			setPageComplete(false);
		}

		setPageComplete(true);
	}

	/**
	 * @see HaleWizardPage#onShowPage(boolean)
	 */
	@Override
	protected void onShowPage(boolean firstShow) {
		super.onShowPage(firstShow);

		// the target property might have changed
		updateStatus();
	}

	/**
	 * Validates if the given value is valid for the target property.
	 * 
	 * @param value the value to validate
	 * @return if the value is valid
	 */
	protected boolean validateValue(String value) {
		Cell cell = getWizard().getUnfinishedCell();
		List<? extends Entity> targets = cell.getTarget().get(null);

		if (!targets.isEmpty()) {
			Entity entity = targets.get(0);
			Definition<?> def = entity.getDefinition().getDefinition();
			if (def instanceof PropertyDefinition) {
				TypeDefinition propertyType = ((PropertyDefinition) def).getPropertyType();
				Validator validator = propertyType.getConstraint(ValidationConstraint.class)
						.getValidator();
				// TODO conversion to binding needed?
				String error = validator.validate(value);
				// update the example decoration
				if (error == null) {
					exampleDecoration.hide();
				}
				else {
					exampleDecoration.setDescriptionText(error);
					exampleDecoration.show();
				}

				return error == null;
			}
		}

		// no validation possible
		return true;
	}

	/**
	 * Generate an identifier example from the current configuration.
	 * 
	 * @return the identifier example
	 * @throws NumberFormatException if ID cannot be parsed as integer
	 */
	protected String generateExample() throws NumberFormatException {
		String prefix = (this.prefix == null) ? ("") : (this.prefix.getText());
		String suffix = (this.suffix == null) ? ("") : (this.suffix.getText());
		String startValue = (this.startValue == null) ? ("") : (this.startValue.getText());

		// replace variables in prefix and suffix
		TransformationVariables variables = new DefaultTransformationVariables(new ProjectVariables(
				HaleUI.getServiceProvider().getService(ProjectInfoService.class)));

		prefix = variables.replaceVariables(prefix);
		suffix = variables.replaceVariables(suffix);

		int ID = 0;

		if (startValue != null && !startValue.isEmpty()) {
			try {
				ID = Integer.parseInt(startValue);
			} catch (Exception e) {
				return "";
			}

		}

		return prefix + ID + suffix;

	}

}
