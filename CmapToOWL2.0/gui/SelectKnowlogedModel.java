package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import main.MainDesktop;
import main.ObjectOwl;
import java.awt.Rectangle;
import java.awt.Point;
import javax.swing.JPasswordField;

import connection.Connection_MC;

public class SelectKnowlogedModel extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPaneKnowModel = null;
	private Label labelRepository = null;
	private Label labelUser = null;
	private Label labelPassword = null;
	private JTextField jTextFieldRepository = null;
	private JTextField jTextFieldUser = null;
	private JButton jButtonAceptar = null;
	private JButton jButtonCancelar = null;
	private JFrame mainFrame = null;

	private JPanel jPanelEditModel = null;

	private JPanel jPanelAuntentification = null;

	private JPasswordField jPasswordFieldContraseña = null;

	private JTextField jTextFieldServidor = null;

	private Label jLabelServidor = null;
	/**
	 * @param owner
	 */
	public SelectKnowlogedModel(JFrame owner) {
		//super(owner);
		mainFrame=owner;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(342, 251);
		this.setResizable(false);
		this.setResizable(false);
		this.setTitle("Seleccionar Modelo de Conocimiento");
		this.setContentPane(getJContentPaneKnowModel());
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.out.println("windowClosing()"); // TODO Auto-generated Event stub windowClosing()
				mainFrame.setEnabled(true);
				dispose();
			}
		});
	}

	/**
	 * This method initializes jContentPaneKnowModel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPaneKnowModel() {
		if (jContentPaneKnowModel == null) {
			labelPassword = new Label();
			labelPassword.setText("Contraseña :");
			labelPassword.setBounds(new Rectangle(23, 64, 96, 24));
			labelPassword.setFont(new Font("Dialog", Font.PLAIN, 14));
			labelUser = new Label();
			labelUser.setText("Usuario :");
			labelUser.setBounds(new Rectangle(49, 22, 70, 24));
			labelUser.setFont(new Font("Dialog", Font.PLAIN, 14));
			labelRepository = new Label();
			labelRepository.setText("Base de Datos:");
			labelRepository.setBounds(new Rectangle(21, 56, 104, 25));
			labelRepository.setFont(new Font("Dialog", Font.PLAIN, 14));
			jContentPaneKnowModel = new JPanel();
			jContentPaneKnowModel.setLayout(null);
			jContentPaneKnowModel.setName("KnowModelPane");
			jContentPaneKnowModel.add(getJPanelEditModel(), null);
		}
		return jContentPaneKnowModel;
	}

	/**
	 * This method initializes jTextFieldRepository	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldRepository() {
		if (jTextFieldRepository == null) {
			jTextFieldRepository = new JTextField();
			jTextFieldRepository.setText("");
			jTextFieldRepository.setBounds(new Rectangle(151, 56, 142, 25));
			jTextFieldRepository.setPreferredSize(new Dimension(33, 20));
			jTextFieldRepository.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) { 
					char caracter;
					caracter = e.getKeyChar();
					// Verificar si la tecla pulsada no es un digito

					if(caracter !='_') {	   
						if ((caracter != ' ') /*|| (caracter !='_')*/) {
							if(((!(Character.isLetter(caracter)) ||
									(caracter == KeyEvent.VK_BACK_SPACE) ||
									(caracter == KeyEvent.VK_DELETE)||(caracter==KeyEvent.VK_SPACE)||(caracter==KeyEvent.VK_MINUS)))) {
								getToolkit().beep();

								e.consume();
							}	
						} 
					}	
				}
			});
		}
		return jTextFieldRepository;
	}

	/**
	 * This method initializes jTextFieldUser	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldUser() {
		if (jTextFieldUser == null) {
			jTextFieldUser = new JTextField();
			jTextFieldUser.setText("");
			jTextFieldUser.setBounds(new Rectangle(145, 22, 142, 25));
			jTextFieldUser.setPreferredSize(new Dimension(33, 20));
			jTextFieldUser.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) { 
					char caracter;
					caracter = e.getKeyChar();
					// Verificar si la tecla pulsada no es un digito

					if(caracter !='_') {	   
						if ((caracter != ' ') /*|| (caracter !='_')*/) {
							if(((!(Character.isLetter(caracter)) ||
									(caracter == KeyEvent.VK_BACK_SPACE) ||
									(caracter == KeyEvent.VK_DELETE)||(caracter==KeyEvent.VK_SPACE)||(caracter==KeyEvent.VK_MINUS)))) {
								getToolkit().beep();

								e.consume();
							}	
						} 
					}	
				}
			});
			
		}
		return jTextFieldUser;
	}

	/**

	/**
	 * This method initializes jButtonAceptar	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonAceptar() {
		if (jButtonAceptar == null) {
			jButtonAceptar = new JButton();
			jButtonAceptar.setText("Aceptar");
			jButtonAceptar.setSize(new Dimension(85, 26));
			jButtonAceptar.setLocation(new Point(82, 98));
			jButtonAceptar.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if(jTextFieldServidor.getText().equals("")){
						JOptionPane.showMessageDialog(getJContentPaneKnowModel(), "Debe entrar el nombre del Servidor correcto","Error",JOptionPane.ERROR_MESSAGE);
						jTextFieldServidor.setText("localhost");
						getJTextFieldRepository().requestFocus(true);
					}
					else if(jTextFieldRepository.getText().equals("")){
						JOptionPane.showMessageDialog(getJContentPaneKnowModel(), "Debe entrar el nombre del Repositorio correcto","Error",JOptionPane.ERROR_MESSAGE);
						getJTextFieldRepository().requestFocus(true);
					}
					else if(jTextFieldUser.getText().equals("")){
						JOptionPane.showMessageDialog(getJContentPaneKnowModel(), "Debe entrar el nombre del Usuario correcto","Error",JOptionPane.ERROR_MESSAGE);
						getJTextFieldUser().requestFocus(true);
					}
					else if(jPasswordFieldContraseña.getPassword().equals("")){
						JOptionPane.showMessageDialog(getJContentPaneKnowModel(), "Debe entrar la contraseña del usuario correcta","Error",JOptionPane.ERROR_MESSAGE);
						jPasswordFieldContraseña.requestFocus(true);
					}
					else{
						Connection_MC conn = new Connection_MC(jTextFieldRepository.getText(),jTextFieldUser.getText(),jPasswordFieldContraseña.getPassword().toString());		
						if(conn.setConnection()){
							getoOWL().setRepositoryDB(jTextFieldRepository.getText());
							getoOWL().setUser(jTextFieldUser.getText());
							getoOWL().setPass(jPasswordFieldContraseña.getPassword().toString());
							JOptionPane.showMessageDialog(getJContentPaneKnowModel(), "Se ha conectado satisfactoriamente con la Base de Dato","Aviso",1);
						} 
						else
							JOptionPane.showMessageDialog(getJContentPaneKnowModel(), "Error en la conexión verifique los datos de conexión","Error",JOptionPane.ERROR_MESSAGE);
					}
				}
			});
		}
		return jButtonAceptar;
	}

	/**
	 * This method initializes jButtonCancelar	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonCancelar() {
		if (jButtonCancelar == null) {
			jButtonCancelar = new JButton();
			jButtonCancelar.setText("Cancelar");
			jButtonCancelar.setBounds(new Rectangle(191, 98, 85, 26));
			jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					mainFrame.setEnabled(true);
					dispose();
				}
			});
		}
		return jButtonCancelar;
	}
	public ObjectOwl getoOWL(){
		return ((MainDesktop) mainFrame).getObjectOwl();
	}

	/**
	 * This method initializes jPanelEditModel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelEditModel() {
		if (jPanelEditModel == null) {
			jLabelServidor = new Label();
			jLabelServidor.setText("Servidor :");
			jLabelServidor.setBounds(new Rectangle(61, 22, 62, 25));
			jLabelServidor.setFont(new Font("Dialog", Font.PLAIN, 14));
			jPanelEditModel = new JPanel();
			jPanelEditModel.setLayout(null);
			jPanelEditModel.setToolTipText("\"Datos del Servidor\"");
			jPanelEditModel.setBorder(BorderFactory.createTitledBorder(null, "Datos del Servidor", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			jPanelEditModel.setBounds(new Rectangle(3, 2, 333, 221));
			jPanelEditModel.add(getJTextFieldRepository(), null);
			jPanelEditModel.add(labelRepository, null);
			jPanelEditModel.add(getJPanelAuntentification(), null);
			jPanelEditModel.add(getJTextFieldServidor(), null);
			jPanelEditModel.add(jLabelServidor, null);
		}
		return jPanelEditModel;
	}

	/**
	 * This method initializes jPanelAuntentification	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelAuntentification() {
		if (jPanelAuntentification == null) {
			jPanelAuntentification = new JPanel();
			jPanelAuntentification.setLayout(null);
			jPanelAuntentification.setBorder(BorderFactory.createTitledBorder(null, "Autentificación", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			jPanelAuntentification.setBounds(new Rectangle(5, 85, 322, 131));
			jPanelAuntentification.add(getJTextFieldUser(), null);
			jPanelAuntentification.add(labelUser, null);
			jPanelAuntentification.add(labelPassword, null);
			jPanelAuntentification.add(getJButtonAceptar(), null);
			jPanelAuntentification.add(getJButtonCancelar(), null);
			jPanelAuntentification.add(getJPasswordFieldContraseña(), null);
		}
		return jPanelAuntentification;
	}

	/**
	 * This method initializes jPasswordFieldContraseña	
	 * 	
	 * @return javax.swing.JPasswordField	
	 */
	private JPasswordField getJPasswordFieldContraseña() {
		if (jPasswordFieldContraseña == null) {
			jPasswordFieldContraseña = new JPasswordField();
			jPasswordFieldContraseña.setLocation(new Point(145, 63));
			jPasswordFieldContraseña.setSize(new Dimension(142, 25));
		}
		return jPasswordFieldContraseña;
	}

	/**
	 * This method initializes jTextFieldServidor	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldServidor() {
		if (jTextFieldServidor == null) {
			jTextFieldServidor = new JTextField();
			jTextFieldServidor.setBounds(new Rectangle(151, 21, 142, 25));
		}
		return jTextFieldServidor;
	}
	

}  //  @jve:decl-index=0:visual-constraint="355,144"
