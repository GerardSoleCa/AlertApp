package com.grayditch.systemtray;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

import com.google.gson.Gson;
import com.grayditch.classes.CALL;
import com.grayditch.classes.SMS;
import com.grayditch.constants.Const;
import com.grayditch.functions.Singleton;
import com.grayditch.multicast.Server;
import com.grayditch.systemtray.core.AlertEvent;
import com.grayditch.systemtray.core.AlertEventAdapter;
import com.grayditch.systemtray.notification.notifier.NotifierDialog;
import com.grayditch.update.UpdateCheck;

public class AlertDeskMainUI {

	private Display display = null;
	private Shell shell = null;
	private Image image = null;
	private Gson gson = null;

	public AlertDeskMainUI() throws IOException {

		gson = new Gson();

		Singleton.getInstance().addAlertEventListener(new AlertEventAdapter() {

			@Override
			public void testAlert(AlertEvent e, String jsonInfo) {
				// TODO Auto-generated method stub
				AlertDeskMainUI.this.testAlert(jsonInfo);
			}

			@Override
			public void whatsappAlert(AlertEvent e, String jsonInfo) {
				AlertDeskMainUI.this.whatsappAlert(jsonInfo);
			}

			@Override
			public void smsAlert(AlertEvent e, String jsonInfo) {
				AlertDeskMainUI.this.smsAlert(jsonInfo);
			}

			@Override
			public void callAlert(AlertEvent e, String jsonInfo) {
				// TODO Auto-generated method stub
				AlertDeskMainUI.this.callAlert(jsonInfo);
			}

		});

		// ///////////////////////////////////
		Display.setAppName("AlertApp");
		display = new Display();

		shell = new Shell(display);

		image = new Image(display, 16, 16);
		final Tray tray = display.getSystemTray();
		if (tray == null) {
			System.out.println("The system tray is not available");
		} else {

			Thread t = new Thread(new Runnable() {

				public void run() {
					try {
						new Server().startServer();
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			t.start();

			final TrayItem item = new TrayItem(tray, SWT.NONE);
			item.setToolTipText("AlertApp");

			InputStream icon = AlertDeskMainUI.class.getResource(
					"icon_64x64.png").openStream();

			item.setImage(new Image(display, icon));
			item.addListener(SWT.Show, new Listener() {
				public void handleEvent(Event event) {
				}
			});
			item.addListener(SWT.Hide, new Listener() {
				public void handleEvent(Event event) {
				}
			});
			item.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
				}
			});
			item.addListener(SWT.DefaultSelection, new Listener() {
				public void handleEvent(Event event) {
				}
			});
			final Menu menu = new Menu(shell, SWT.POP_UP);
			
			if (UpdateCheck.updateCheck()) {
				NotifierDialog.notify(Const.UPDATE_TITLE + "\n"
						+ Const.UPDATE_TEXT, Const.NULL, Const.UPDATE_IMG,
						display);
				MenuItem updateMenu = new MenuItem(menu, SWT.PUSH);
				updateMenu.setText("Update");
				updateMenu.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event event) {
						Program.launch("http://alertapp.grayditch.com");
					}
				});
			}


			MenuItem configMenu = new MenuItem(menu, SWT.PUSH);
			configMenu.setText("Configure");
			configMenu.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					ConfigForm loginForm = new ConfigForm(shell);
					loginForm.open();
				}
			});

			MenuItem exitMenu = new MenuItem(menu, SWT.PUSH);
			exitMenu.setText("Exit");
			exitMenu.addListener(SWT.Selection, new Listener() {
				public void handleEvent(Event event) {
					shell.dispose();
				}
			});

			menu.setDefaultItem(exitMenu);

			item.addListener(SWT.MenuDetect, new Listener() {
				public void handleEvent(Event event) {
					menu.setVisible(true);
				}
			});

			
		}

	}

	public void execute() {
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		image.dispose();
		display.dispose();
		System.exit(0);
	}

	public void smsAlert(final String jsonInfo) {
		if (!display.isDisposed()) {
			display.syncExec(new Runnable() {

				@Override
				public void run() {
					try {
						SMS message = gson.fromJson(jsonInfo, SMS.class);
						String sender = message.senderName.equals("Unknown") ? message.senderNumber
								: message.senderName;
						NotifierDialog.notify(sender, message.smsBody,
								Const.SMS_IMG, display);
					} catch (IOException e) {
					} catch (Exception e) {
					}
				}
			});

		}
	}

	public void callAlert(final String jsonInfo) {
		if (!display.isDisposed()) {
			display.syncExec(new Runnable() {

				@Override
				public void run() {
					try {
						CALL message = gson.fromJson(jsonInfo, CALL.class);

						NotifierDialog.notify(message.senderName + "\n"
								+ message.senderNumber, Const.NULL,
								Const.CALL_IMG, display);
					} catch (IOException e) {
					} catch (Exception e) {
					}
				}
			});

		}
	}

	public void whatsappAlert(final String jsonInfo) {
		if (!display.isDisposed()) {
			display.syncExec(new Runnable() {

				@Override
				public void run() {
					try {
						if (jsonInfo.equals(Const.NULL)) {
							NotifierDialog.notify(Const.WHATSAPP_TITLE,
									Const.NULL, Const.WHATSAPP_IMG, display);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

		}
	}

	public void testAlert(final String jsonInfo) {
		if (!display.isDisposed()) {
			display.syncExec(new Runnable() {

				@Override
				public void run() {
					try {
						if (jsonInfo.equals(Const.NULL)) {
							NotifierDialog.notify(Const.TEST_TITLE, Const.NULL,
									Const.TEST_IMG, display);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

		}
	}
}