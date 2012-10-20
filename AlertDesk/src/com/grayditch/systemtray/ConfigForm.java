package com.grayditch.systemtray;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.grayditch.classes.User;
import com.grayditch.functions.Singleton;

public class ConfigForm extends Dialog {

	private String value;
	private Label infoLabel;
	private Display display = null;
	private User user;

	public ConfigForm(Shell parent) {
		super(parent);
		user = Singleton.getInstance().getUser();
	}

	public String open() {
		Shell parent = getParent();
		final Shell shell = new Shell(parent, SWT.TITLE | SWT.APPLICATION_MODAL);

		shell.setText("Configure Server");

		shell.setSize(200, 150);

		GridLayout gl = new GridLayout();
		gl.numColumns = 3;
		gl.marginTop = 10;
		gl.marginLeft = 10;
		gl.marginRight = 10;
		gl.marginBottom = 10;
		shell.setLayout(gl);

		GridData gd = null;

		gd = new GridData();
		gd.horizontalAlignment = SWT.LEFT;
		gd.verticalAlignment = SWT.CENTER;

		Label userLabel = new Label(shell, SWT.NULL);
		userLabel.setText("Username:");
		userLabel.setLayoutData(gd);

		gd = new GridData();
		gd.horizontalSpan = 2;
		gd.horizontalAlignment = SWT.FILL;
		gd.verticalAlignment = SWT.CENTER;
		final Text userName = new Text(shell, SWT.SINGLE | SWT.BORDER);
		userName.setText(this.user.user);
		userName.setLayoutData(gd);

		gd = new GridData();
		gd.horizontalAlignment = SWT.LEFT;
		gd.verticalAlignment = SWT.CENTER;
		Label passLabel = new Label(shell, SWT.NULL);
		passLabel.setText("Pin:");
		passLabel.setLayoutData(gd);

		gd = new GridData();
		gd.horizontalSpan = 2;
		gd.horizontalAlignment = SWT.FILL;
		gd.verticalAlignment = SWT.CENTER;
		final Text userPass = new Text(shell, SWT.SINGLE | SWT.BORDER
				| SWT.PASSWORD);
		userPass.setText(this.user.pass);
		userPass.setLayoutData(gd);

		// gd = new GridData();
		// gd.horizontalAlignment = SWT.FILL;
		// gd.verticalAlignment = SWT.CENTER;
		// gd.horizontalSpan = 3;
		// gd.grabExcessHorizontalSpace = true;
		// infoLabel = new Label(shell, SWT.NULL);
		// infoLabel.setText("");
		// infoLabel.setLayoutData(gd);

		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.verticalAlignment = SWT.CENTER;
		gd.grabExcessHorizontalSpace = true;
		final Button buttonOK = new Button(shell, SWT.PUSH);
		buttonOK.setText("Save");
		buttonOK.setLayoutData(gd);

		buttonOK.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				Singleton.getInstance().setUser(userName.getText(),
						userPass.getText());
				shell.dispose();
			}
		});

		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.verticalAlignment = SWT.CENTER;
		gd.grabExcessHorizontalSpace = true;
		Button buttonCancel = new Button(shell, SWT.PUSH);
		buttonCancel.setText("Cancel");
		buttonCancel.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				shell.dispose();
			}
		});
		buttonCancel.setLayoutData(gd);

		shell.addListener(SWT.Traverse, new Listener() {
			public void handleEvent(Event event) {
				if (event.detail == SWT.TRAVERSE_ESCAPE)
					event.doit = false;
			}
		});

		// userName.setText("");
		// shell.pack();
		shell.open();

		display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		return value;
	}

}
