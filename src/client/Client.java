package client;

import java.util.List;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import server.VideoFile;

public class Client extends JFrame implements ActionListener {
	Container contentPane;
	JComboBox selectionBox;
	public List<VideoFile> videoList;
	public Socket serverSocket;
	private String host = "127.0.0.1";
	int port = 1239;
	public ObjectInputStream inputFromServer;

	public Client() {
		
		try {
			openSocket();
			getListFromSocket();
			setupGUI();
			serverSocket.close();
			System.out.println("Client : Connection closed");
		} catch (UnknownHostException e) {
			System.out.println("Don't know about host : " + host);
			System.exit(-1);
		} catch (IOException e) {
			System.out.println("Couldn't open I/O connection : " + host + ":"
					+ port);
			//e.printStackTrace();
			System.exit(-1);
		} catch (ClassNotFoundException e) {
			System.out
					.println("Class definition not found for incoming object.");
			e.printStackTrace();
			System.exit(-1);
		}
		System.out.println("Client : Connection closed");
	}
	
	private void setupGUI() {
			setTitle("A Client");
			setSize(600, 400);
			setVisible(true);
			contentPane = getContentPane();
			contentPane.setLayout(new BorderLayout());
			populateSelectionBox();
		}
	
	private void populateSelectionBox (){
		String [] selectionListData;
		if(validateVideoList()==true){	
			if(videoList.size() != 0) {	
				selectionListData = new String[videoList.size()];
				for (int i = 0; i < videoList.size(); i++) {
					selectionListData[i] = videoList.get(i).getTitle();
				}
			} else {
				selectionListData = new String[1];
				selectionListData[0] = "No Videos Available";
			}
		} else {
			selectionListData = new String[1];
			selectionListData[0] = "Oops! Something went wrong at our end :(";
		}
		selectionBox = new JComboBox(selectionListData);
		selectionBox.setSelectedIndex(0);
		add(selectionBox, BorderLayout.NORTH);
		selectionBox.addActionListener(this);
		validate();
	}
	
	public void actionPerformed(ActionEvent e) {
		JComboBox comboBox = (JComboBox)e.getSource();
		String selectedTitle = (String)comboBox.getSelectedItem();
		System.out.println("Selected title : " + selectedTitle);
		}

	public void openSocket() throws IOException {
		serverSocket = new Socket(host, port);
		inputFromServer = new ObjectInputStream(serverSocket.getInputStream());
	}

	private void getListFromSocket() throws IOException, ClassNotFoundException {
		videoList = (List<VideoFile>) inputFromServer.readObject();
	}
	
	private boolean validateVideoList() {
		Boolean result = true;
		
		for (VideoFile video : videoList) {
			result &= video.checkValid();
		}
		return result;
	}
	
	public static void main(String[] args){
		new Client();
	}
}
