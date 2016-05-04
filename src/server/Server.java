package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import org.xml.sax.SAXException;

import client.Client;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class Server {
	ServerSocket serverSocket;
	int port = 1352;
	Socket clientSocket;
	ObjectOutputStream outputToClient;
	ObjectInputStream inputFromClient;
	public XMLReader myReader;
	public List<VideoFile> videoList;
	String serverAddress = "127.0.0.1";

	public Server(String videoListPath) {
		String vlcLibraryPath = "M:/GitHub/JavaMEME/vlc-2.0.1";
	    
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(),vlcLibraryPath);
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		myReader = new XMLReader(videoListPath);
		
		Thread socketThread = new Thread("Socket") {
			//VideoFile videoToStream = new VideoFile();
			
			@Override
			public void run() {
				System.out.println("Thread Running");
				try {
					VideoFile selection = null;
					openSocket();
					writeListToSocket();
					try{
						selection = getSelectionFromClient();
						clientSocket.close();
						serverSocket.close();
						System.out.println("Server: Client connection closed");
						broadcastSelectedVideo(selection);
					} catch (IOException e) {
						e.printStackTrace();
					}
					//videoToStream = selection;
					
					
					//serverSocket.close();
					//System.out.println("Server: Server connection closed");
				} catch (IOException e) {
					System.out.println("ERROR on socket connection.");
					e.printStackTrace();
				}
				System.out.println("Thread Ending");
			}
		};
		socketThread.start();
	}

	private void openSocket() throws IOException {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("Could not listen on port : " + port);
			System.exit(-1);
		}
		System.out.println("Opened socket on : " + port
				+ ", waiting for client.");

		try {
			clientSocket = serverSocket.accept();
			System.out.println("Server: Client connected");
		} catch (IOException e) {
			System.out.println("Could not accept client.");
			System.exit(-1);
		}
		outputToClient = new ObjectOutputStream(clientSocket.getOutputStream());
	}

	private void writeListToSocket() throws IOException {
		populateList();
		outputToClient.writeObject(videoList);
	}

	private VideoFile getSelectionFromClient() throws IOException {
		inputFromClient = new ObjectInputStream(clientSocket.getInputStream());
		VideoFile selection = new VideoFile();
		try {
		  selection = (VideoFile) inputFromClient.readObject();
		  System.out.println("Server: Recieved " + selection.toString());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(selection.getTitle());
		return selection;
	}

	public void broadcastSelectedVideo(VideoFile selection) {
		MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory(selection.getFilename());
		HeadlessMediaPlayer mediaPlayer = mediaPlayerFactory.newHeadlessMediaPlayer();
		String options = formatRtpStream(serverAddress, 5555);
		String media = selection.getFilename();
		mediaPlayer.playMedia(media, options, ":no-sout-rtp-sap",":no-sout-standardsap", ":sout-all", ":sout-keep");
		// Continue running - "join" waits for current executing thread to finish
		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			System.out.println("Exception thrown whilst streaming.");
			e.printStackTrace();
		}
	}

	public void populateList() {
		this.videoList = myReader.getList();
	}

	public List<VideoFile> getPopulatedList() {
		System.out.println("Returning Populated List");
		populateList();
		return videoList;
	}

	private String formatRtpStream(String serverAddress, int serverPort) {
		StringBuilder sb = new StringBuilder(60);
		sb.append(":sout=#rtp{dst=");
		sb.append(serverAddress);
		sb.append(",port=");
		sb.append(serverPort);
		sb.append(",mux=ts}");
		return sb.toString();
	}

	public static void main(String[] args) {
		new Server("videoList.xml");
	}
}
