package me.dablakbandit.skinfetcher;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import me.dablakbandit.skinfetcher.json.JSONArray;
import me.dablakbandit.skinfetcher.json.JSONObject;

public class SkinFetcher{
	
	private JFrame		frmSkinFetcher;
	private JTextField	txtUUID;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				try{
					SkinFetcher window = new SkinFetcher();
					window.frmSkinFetcher.setVisible(true);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the application.
	 */
	public SkinFetcher(){
		initialize();
	}
	
	private JTextArea url = new JTextArea();;
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(){
		frmSkinFetcher = new JFrame();
		frmSkinFetcher.setTitle("Skin Fetcher");
		frmSkinFetcher.setBounds(100, 100, 450, 252);
		frmSkinFetcher.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSkinFetcher.getContentPane().setLayout(null);
		
		txtUUID = new JTextField();
		txtUUID.setText("dc7a1b3808aa450b87c7db0722802826");
		txtUUID.setBounds(98, 13, 322, 22);
		frmSkinFetcher.getContentPane().add(txtUUID);
		txtUUID.setColumns(10);
		
		JLabel lblUuid = new JLabel("UUID");
		lblUuid.setBounds(12, 16, 56, 16);
		frmSkinFetcher.getContentPane().add(lblUuid);
		
		JLabel lblUrl = new JLabel("URL");
		lblUrl.setBounds(12, 86, 56, 16);
		frmSkinFetcher.getContentPane().add(lblUrl);
		
		url.setFont(new Font("Monospaced", Font.PLAIN, 10));
		url.setLineWrap(true);
		url.setBounds(98, 86, 322, 118);
		frmSkinFetcher.getContentPane().add(url);
		
		JButton btnGet = new JButton("Get");
		btnGet.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				get();
			}
		});
		btnGet.setBounds(323, 48, 97, 25);
		frmSkinFetcher.getContentPane().add(btnGet);
	}
	
	private void get(){
		JSONObject jo = get(txtUUID.getText());
		System.out.println(jo.toString());
		if(jo != null){
			try{
				if(jo.has("properties")){
					JSONArray prop = jo.getJSONArray("properties");
					JSONObject p = prop.getJSONObject(0);
					if(p.getString("name").equals("textures")){
						url.setText(p.getString("value"));
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private JSONObject get(String uuid){
		StringBuilder content = new StringBuilder();
		try{
			URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
			URLConnection urlConnection = url.openConnection();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			String line;
			while((line = bufferedReader.readLine()) != null){
				content.append(line);
			}
			bufferedReader.close();
		}catch(Exception e){
			return null;
		}
		try{
			String lortu = content.toString();
			return new JSONObject(lortu);
		}catch(Exception e){
			return null;
		}
	}
}
