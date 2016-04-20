package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import org.xml.sax.SAXException;

public class Server {
	ServerSocket serverSocket;
	int port = 1239;
	Socket clientSocket;
	ObjectOutputStream outputToClient;
	public XMLReader myReader;
	public List<VideoFile> videoList;

	public Server() {
		myReader = new XMLReader("videoList.xml");
		Thread socketThread = new Thread("Socket") {
			@Override
			public void run() {
				try {
					openSocket();
					writeListToSocket();
					clientSocket.close();
					System.out.println("Server: Client connection closed");
					serverSocket.close();
					System.out.println("Server: Server connection closed");
				} catch (IOException e) {
					System.out.println("ERROR on socket connection.");
					e.printStackTrace();
				}
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

	public void populateList() {
		this.videoList = myReader.getList();
	}

	public List<VideoFile> getPopulatedList() {
		System.out.println("Returning Populated List");
		populateList();
		return videoList;
	}

	public static void main(String[] args) {
		new Server();
	}
}
