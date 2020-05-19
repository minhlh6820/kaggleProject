package play;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;

import java.io.IOException;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import client.Client;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;

public class CreateRoomWindow {

	protected Shell shell;
	private String clientName;
	private Text roomTxt;
	
	public void setShell(Shell shell) {
		this.shell = shell;
	}
	
	public void setClientName(String name) {
		this.clientName = name;
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CreateRoomWindow window = new CreateRoomWindow();
			window.setClientName("abcd");
			//window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open(Client client) {
		Display display = Display.getDefault();
		String sRep = null;
		List topicList;
		try {
			client.dos.writeUTF(client.getTopicList(clientName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			sRep = client.dis.readUTF();
			System.out.println(sRep);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] parts = sRep.split("-");
		createContents(display, client, parts);
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents(Display display, Client client, String[] parts) {
		if(shell == null) shell = new Shell();
		shell.setSize(450, 392);
		shell.setText("Create room");
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(10, 10, 424, 64);
		
		Label lblUser = new Label(composite, SWT.NONE);
		lblUser.setBounds(137, 21, 121, 15);
		lblUser.setText("User: " + clientName);
		
		Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setBounds(10, 78, 414, 265);
		
		Label lblChooseTopic = new Label(composite_1, SWT.NONE);
		lblChooseTopic.setBounds(26, 36, 83, 15);
		lblChooseTopic.setText("Choose topic: ");
		
		List list = new List(composite_1, SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE);
		list.setBounds(137, 40, 248, 68);
		
		//CODE HERE
		//Add list of topics
		//Sample code
		//for(String topic: topicList)
		//   List.add(topic)
		if (parts[0].contains("TL")) {
			for (int i = 1; i < parts.length; i++) {
				list.add(parts[i]);
				System.out.println(parts[i]);
			}
		}
		else {
			System.out.println("Problem at getting question");
		}
		
		Color red = new Color(display, 255, 0, 0);
		
		Label lblPleaseChooseTopic = new Label(composite_1, SWT.NONE);
		lblPleaseChooseTopic.setBounds(26, 138, 178, 15);
		lblPleaseChooseTopic.setText("");
		
		Button btnNext = new Button(composite_1, SWT.NONE);
		btnNext.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = list.getSelectionIndex();
				if(index == -1) {
					lblPleaseChooseTopic.setText("Please choose topic!");
					lblPleaseChooseTopic.setForeground(red);
				} else {
					lblPleaseChooseTopic.setText("");
					
					//Get selected topic name
					Random random = new Random();
					String room = Integer.toString(random.nextInt(10000));
					String topic = list.getItem(index);
					try {
						client.dos.writeUTF(client.createRoomMsg(room, topic));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					//***********************************************
					//CODE HERE
					//Create room and get roomID with selected topic
					
					
					//Go to start window
					try {
						for (Control kid : shell.getChildren()) {
							kid.dispose();
						}
						StartWindow window = new StartWindow();
						window.setShell(shell);
						window.setClientName(clientName);
						window.setRoom(room);
						window.open(client);
					} catch (Exception ex) {
						ex.printStackTrace();
					} 
				}
			}

			private String createRoom(String topic) {
				// TODO Auto-generated method stub
				return null;
			}
		});
		btnNext.setBounds(271, 182, 75, 25);
		btnNext.setText("Next");

	}
}
