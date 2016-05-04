package client;

import java.util.List;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JComboBox;
import javax.swing.JFrame;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import server.VideoFile;
import uk.co.caprica.vlcj.*;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.test.basic.PlayerControlsPanel;

public class Client extends JFrame implements ActionListener {

	Container contentPane;
	Socket clientSocket;
	JComboBox<VideoFile> selectionBox;
	JFrame mainFrame;
	EmbeddedMediaPlayerComponent mediaPlayerComponent;
	public List<VideoFile> videoList;
	public Socket serverSocket;
	private String host = "127.0.0.1";
	String media = "rtp://@127.0.0.1:5555";
	int port = 1352;
	public ObjectInputStream inputFromServer;
	public ObjectOutputStream outputToServer;

	public Client() {
		String vlcLibraryPath = "M:/GitHub/JavaMEME/vlc-2.0.1";
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(),
				vlcLibraryPath);
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		try {
			openSocket();
			getListFromSocket();
			setupGUI();
			try {
				sendSelectedVideo();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mediaPlayerComponent.getMediaPlayer().playMedia(media);
			serverSocket.close();
			System.out.println("Client : Connection closed");
		} catch (UnknownHostException e) {
			System.out.println("Don't know about host : " + host);
			System.exit(-1);
		} catch (IOException e) {
			System.out.println("Couldn't open I/O connection : " + host + ":"
					+ port);
			// e.printStackTrace();
			System.exit(-1);
		} catch (ClassNotFoundException e) {
			System.out
					.println("Class definition not found for incoming object.");
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private void setupGUI() {
		mainFrame = new JFrame();
		setTitle("A Client");
		setSize(600, 400);
		setVisible(true);
		contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		populateSelectionBox();
		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		createMediaPlayer();
	}

	private void populateSelectionBox() {
		VideoFile[] selectionListData = { new VideoFile() };
		if (validateVideoList() == true) {
			if (videoList.size() != 0) {
				selectionListData = new VideoFile[videoList.size()];
				for (int i = 0; i < videoList.size(); i++) {
					selectionListData[i] = videoList.get(i);
				}
			} else {
				selectionListData[0] = new VideoFile("00000000x1",
						"No Videos Available", "");
			}
		} else {
			selectionListData[0] = new VideoFile("00000000x1",
					"Oops! Something went wrong on our end :(", "");
		}
		selectionBox = new JComboBox<VideoFile>(selectionListData);
		selectionBox.setSelectedIndex(0);
		add(selectionBox, BorderLayout.NORTH);
		selectionBox.addActionListener(this);
		validate();
	}

	public void createMediaPlayer() {
		mainFrame.setContentPane(mediaPlayerComponent);
		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				mediaPlayerComponent.release();
				try {
					serverSocket.close();
					System.out.println("Client : Connection closed");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		contentPane.add(mediaPlayerComponent, BorderLayout.CENTER);
		EmbeddedMediaPlayer mediaPlayer = mediaPlayerComponent.getMediaPlayer();
		PlayerControlsPanel controlsPanel = new PlayerControlsPanel(mediaPlayer);
		mainFrame.add(controlsPanel, BorderLayout.SOUTH);
	}

	public void actionPerformed(ActionEvent e) {
		VideoFile selectedVideo = new VideoFile();
		JComboBox comboBox = (JComboBox) e.getSource();
		selectedVideo = (VideoFile) comboBox.getSelectedItem();
		System.out.println("Selected title : " + selectedVideo.getTitle());
	}

	public void openSocket() throws IOException {
		serverSocket = new Socket(host, port);
		inputFromServer = new ObjectInputStream(serverSocket.getInputStream());
	}

	private void getListFromSocket() throws IOException, ClassNotFoundException {
		videoList = (List<VideoFile>) inputFromServer.readObject();
	}

	private void sendSelectedVideo() throws IOException {
		outputToServer = new ObjectOutputStream(serverSocket.getOutputStream());
		VideoFile sendVideoFile = (VideoFile) selectionBox.getSelectedItem();
		outputToServer.writeObject(sendVideoFile);
		System.out.println("Client: Sent "+ sendVideoFile.toString());
	}

	private boolean validateVideoList() {
		Boolean result = true;

		for (VideoFile video : videoList) {
			result &= video.checkValid();
		}
		return result;
	}

	public static void main(String[] args) {
		new Client();
	}
}
