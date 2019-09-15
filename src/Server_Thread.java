import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class Server_Thread extends Thread {

	public ServerSocket server_socket;
	private Socket connection_socket;


	static ArrayList<Socket_Thread> socket_threads = new ArrayList<Socket_Thread>();
	static ArrayList<String> socket_threads_ips = new ArrayList<String>();

	boolean stop = false;



	public Server_Thread(){

	}


	public void run(){

		startServer();

		while(true) {

			if(stop) {
				System.out.println("durdum");
				return;
			}
			if(Server_Frame.iswaiting) {
				openConnection();
			}


			try {
				TimeUnit.MILLISECONDS.sleep(1000);
			} 
			catch (InterruptedException e) {
				continue;
			}

		}

	}



	//-------------------------------------------connection functions-----------------------------------------------------



	public void startServer(){
		try {
			server_socket = new ServerSocket(12349, 100);
		} 
		catch (IOException e) {
			System.out.println("\nIOexception\n");
		}
	}



	private void openConnection(){
		System.out.println("\nWaiting for connection\n");

		try {
			connection_socket = server_socket.accept();

			socket_threads.add(new Socket_Thread(connection_socket));

			socket_threads.get(socket_threads.size() - 1).start();

			Server_Frame.connected_ips.addItem(connection_socket.getInetAddress().getHostName().toString());


			System.out.println("Connection received from: " + connection_socket.getInetAddress().getHostName());
			System.out.println("Connected clients: " + socket_threads.size());
				
			Server_Frame.connection_count_info_label.setText(socket_threads.size() + "");
			
		} 
		catch (Exception e) {
			//e.printStackTrace();
			System.out.println("cant accept connection");
		}

	}



	/*
	 * this function closes all existing connections by calling closeConnection() function in a loop
	 * than closes the server
	 */
	public void closeServer(){
		System.out.println("\nTerminating connection\n");

		try{
			//closeConnection() function removes elements from the list so regular loop does not works
			int temp_size = socket_threads.size();
			for (int i = 0; i < temp_size; i++) {
				socket_threads.get(0).closeConnection();
			}	

			server_socket.close();

		}
		catch (Exception e) {
			System.out.println("\nconnection either already closed or can not be closed\n");
		}

	}


	
	void stop_thread() {
		stop = true;
	}
	
	
	/*
	private void displayMessage(final String messageToDisplay){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				System.out.println(messageToDisplay);
			}
		});
	}
	 */

	//----------------------------------------------------------------------------------------------------------------------





}
