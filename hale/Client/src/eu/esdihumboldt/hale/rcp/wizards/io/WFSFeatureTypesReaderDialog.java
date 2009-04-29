/*
 * HUMBOLDT: A Framework for Data Harmonisation and Service Integration.
 * EU Integrated Project #030962                  01.10.2006 - 30.09.2010
 * 
 * For more information on the project, please refer to the this web site:
 * http://www.esdi-humboldt.eu
 * 
 * LICENSE: For information on the license under which this program is 
 * available, please refer to http:/www.esdi-humboldt.eu/license.html#core
 * (c) the HUMBOLDT Consortium, 2007 to 2010.
 */
package eu.esdihumboldt.hale.rcp.wizards.io;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;

/**
 * The {@link WFSFeatureTypesReaderDialog} enables a user to select a WFS from
 * which to read capabilities and will confirm communication by displaying
 * {@link FeatureType} names that are found.
 * 
 * @author Thorsten Reitz 
 * @partner 01 / Fraunhofer Institute for Computer Graphics Research
 * @version $Id$ 
 */
public class WFSFeatureTypesReaderDialog 
	extends Dialog {
	
	private final static Logger _log = Logger.getLogger(WFSFeatureTypesReaderDialog.class);

	URL url_result;
	
	public WFSFeatureTypesReaderDialog(Shell parent, int style) {
		super(parent, style);
	}
	
	public WFSFeatureTypesReaderDialog(Shell parent, String title) {
		super(parent, 0);
		this.setText(title);
	}

	/**
	 * @see org.eclipse.swt.widgets.Dialog
	 * @return any Object.
	 */
	public URL open () {
		Shell parent = super.getParent();
		Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setSize(500, 400);
		shell.setLayout(new GridLayout());
		shell.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL
                | GridData.HORIZONTAL_ALIGN_FILL));
		shell.setText(super.getText());
		
		this.createControls(shell);
		
		shell.open();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		_log.debug("returning result.");
		return url_result;
	}

	private void createControls(Shell shell) {
		_log.debug("Creating Controls");
		
		// Create Fields for URL entry.
		final Composite c = new Composite(shell, SWT.NONE);
		c.setLayout(new GridLayout());
		c.setLayoutData(new GridData(
				GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL |
				GridData.GRAB_VERTICAL | GridData.FILL_VERTICAL));
		final Group urlDefinitionArea = new Group(c, SWT.NONE);
		urlDefinitionArea.setText("Enter the URL of your WFS");
		urlDefinitionArea.setLayoutData( new GridData(
				GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));

		GridLayout fileSelectionLayout = new GridLayout();
		fileSelectionLayout.numColumns = 2;
		fileSelectionLayout.makeColumnsEqualWidth = false;
		urlDefinitionArea.setLayout(fileSelectionLayout);
		
		// Host + Port
		final Label hostPortLabel = new Label(urlDefinitionArea, SWT.NONE);
		hostPortLabel.setText("Host + Port:");
		hostPortLabel.setToolTipText("Enter the GetCapabilities URL of the " +
				"WFS you want to query here.");
		final Text hostPortText = new Text (urlDefinitionArea, SWT.BORDER | SWT.SINGLE);
		hostPortText.setLayoutData(new GridData(
				GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));
		/*hostPortText.setText(
				"http://staging-esdi-humboldt.igd.fraunhofer.de:8080/" +
				"geoserver/ows?service=WFS&request=GetCapabilities");*/
		hostPortText.setText(
				"http://car2.esrin.esa.int:8080/" +
				"geoserver/ows?service=WFS&request=GetCapabilities");
		hostPortText.setEditable(true);
		hostPortText.addListener (SWT.FocusOut, new Listener () {
			public void handleEvent (Event e) {
				String string = hostPortText.getText();
				try {
					new URL(string);
				}
				catch (Exception ex) {
					_log.warn("Exception occured in parsing " + string 
							+ " to URL: " + ex.getMessage());
					e.doit = false;
					return;
				}
			}
		});
		
		// Protocol Version & Type
		final Label protocolVersionLabel = new Label(urlDefinitionArea, SWT.NONE);
		protocolVersionLabel.setText("WFS Version/Protocol:");
		protocolVersionLabel.setToolTipText("Select one of the offered combinations of service version and protocol.");
		final Combo combo = new Combo (urlDefinitionArea, SWT.READ_ONLY);
		combo.setItems (new String [] {"1.1.0, HTTP GET", "1.0.0 XML POST", "1.1.0 XML POST"});
		combo.setLayoutData(new GridData(
				GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));

		// Validation Group
		final Group urlValidationArea = new Group(c, SWT.NONE);
		urlValidationArea.setText("Validate your WFS settings");
		urlValidationArea.setLayoutData( new GridData(
				GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL 
				| GridData.GRAB_VERTICAL | GridData.FILL_VERTICAL));
		GridLayout urlValidationLayout = new GridLayout();
		urlValidationLayout.numColumns = 1;
		urlValidationLayout.makeColumnsEqualWidth = false;
		urlValidationArea.setLayout(urlValidationLayout);
		
		Composite urlValidationStatusArea = new Composite(urlValidationArea, SWT.NONE);
		urlValidationStatusArea.setLayoutData( new GridData(
				GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));
		GridLayout urlValidationStatusLayout = new GridLayout();
		urlValidationStatusLayout.numColumns = 2;
		urlValidationStatusArea.setLayout(urlValidationStatusLayout);
		
		Button testUrl = new Button(urlValidationStatusArea, SWT.PUSH);
		testUrl.setText("Validate settings");
		final Label currentStatusLabel = new Label(urlValidationStatusArea, SWT.NONE);
		currentStatusLabel.setText("No Validation performed yet.");
		currentStatusLabel.setLayoutData(new GridData(
				GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));
		currentStatusLabel.setAlignment(SWT.RIGHT);
		
		final Text testResultText = new Text(urlValidationArea, 
				SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		testResultText.setLayoutData(new GridData(GridData.FILL_BOTH));
		testResultText.setEditable(false);
		
		// Cancel/Finish buttons
		Composite buttons = new Composite(c, SWT.BOTTOM);
		buttons.setLayoutData( new GridData(SWT.RIGHT, SWT.CENTER, true, false));
		GridLayout buttonsLayout = new GridLayout();
		buttonsLayout.numColumns = 3;
		buttonsLayout.makeColumnsEqualWidth = false;
		buttons.setLayout(buttonsLayout);
		
		final Button finish = new Button(buttons, SWT.NONE);
		finish.setAlignment(SWT.RIGHT);
		finish.setText("   Use this WFS    ");
		finish.setEnabled(false);
		finish.setSize(100, 24);
		finish.addListener(SWT.Selection, new Listener () {
			public void handleEvent(Event event) {
				if (finish.isEnabled()) {
					_log.debug("saving result: " + hostPortText.getText());
					try {
						url_result = new URL(hostPortText.getText());
					} catch (MalformedURLException e) {
						_log.error("An error occured when parsing the " +
								"selected host to a URL:", e);
					}
				}
				finish.getParent().getParent().getShell().dispose();
			}
		});
		
		Button cancel = new Button(buttons, SWT.NONE);
		cancel.setAlignment(SWT.RIGHT);
		cancel.setText("      Cancel       ");
		cancel.setSize(100, 24);
		cancel.addListener(SWT.Selection, new Listener () {
			public void handleEvent(Event event) {
				finish.getParent().getParent().getShell().dispose();
			}
		});
		
		// add complex Listeners
		testUrl.addListener(SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				URL url = null;
				try {
					url = new URL(hostPortText.getText());
				} catch (Exception e1) {
					currentStatusLabel.setText("Validation FAILED.");
					testResultText.setText("Capabilities URL could not " +
							"be built: " + e1.getMessage());
					finish.setEnabled(false);
				}
				if (url != null) {
					List<FeatureType> result = null;
					try {
						result = GetCapabilititiesRetriever.readFeatureTypes(url.toString());
						StringBuffer ft_names = new StringBuffer();
						for (FeatureType ft : result){
							if (ft instanceof SimpleFeatureType) {
								SimpleFeatureType sft = (SimpleFeatureType) ft;
								ft_names.append(sft.getTypeName() + " \n");
							}
							else {
								ft_names.append(ft.getName() + " \n");
							}
						}
						testResultText.setText(ft_names.toString());
						currentStatusLabel.setText("Validation OK - " 
								+ result.size() + " FeatureTypes!");
						finish.setEnabled(true);
					} catch (IOException e1) {
						currentStatusLabel.setText("Validation FAILED.");
						testResultText.setText("Capabilities document " +
								"could not be read: " + e1.getMessage());
						finish.setEnabled(false);
						_log.warn(e1.getMessage());
						e1.printStackTrace();
					}
				}
			}
		});
		
		// finish drawing
		urlDefinitionArea.moveAbove(null);
	}

}
