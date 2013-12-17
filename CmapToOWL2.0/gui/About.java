package gui;


import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;
import javax.swing.ImageIcon;

public class About extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JLabel jLabel = null;

	private JLabel jLabelAutor = null;

	private JLabel jLabelAutor1 = null;

	private JLabel jLabelAutor3 = null;

	private JLabel jLabelPlace = null;

	private JLabel jLabelTool = null;

	private JButton jButtonCancelar = null;

	private JLabel jLabelEmail = null;

	private JLabel jLabelemail1 = null;

	private JLabel jLabelEmail3 = null;
	private JFrame mainFrame = null;

	private JLabel jLabel1 = null;

	private JLabel jLabelEmail2 = null;

	private JLabel jLabelPicture = null;


	/**
	 * @param owner
	 */
	public About(JFrame owner) {
		super(owner);
		mainFrame=owner;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(499, 289);
		this.setModal(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);
		//this.setLocation(new Point(500, 0));
		this.setResizable(false);
		this.setName("About");
		this.setTitle("Acerca de los autores");
		this.setContentPane(getJContentPane());
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				mainFrame.setEnabled(true);
				dispose();
			}
		});
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.insets = new Insets(13, 29, 4, 25);
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.gridy = 1;
			gridBagConstraints13.ipadx = -42;
			gridBagConstraints13.ipady = 9;
			gridBagConstraints13.gridheight = 5;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.insets = new Insets(2, 20, 1, 5);
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.gridy = 7;
			gridBagConstraints11.ipadx = 140;
			gridBagConstraints11.gridwidth = 3;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.insets = new Insets(8, 11, 7, 20);
			gridBagConstraints10.gridy = 4;
			gridBagConstraints10.ipadx = 14;
			gridBagConstraints10.gridx = 2;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.insets = new Insets(5, 11, 7, 20);
			gridBagConstraints9.gridy = 1;
			gridBagConstraints9.ipadx = 42;
			gridBagConstraints9.gridx = 2;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.insets = new Insets(7, 11, 12, 20);
			gridBagConstraints8.gridy = 5;
			gridBagConstraints8.ipadx = 12;
			gridBagConstraints8.gridx = 2;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.insets = new Insets(10, 11, 8, 20);
			gridBagConstraints7.gridy = 3;
			gridBagConstraints7.ipadx = 24;
			gridBagConstraints7.gridx = 2;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.insets = new Insets(10, 25, 8, 10);
			gridBagConstraints6.gridy = 3;
			gridBagConstraints6.ipadx = 4;
			gridBagConstraints6.gridx = 1;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.insets = new Insets(1, 79, 1, 9);
			gridBagConstraints5.gridy = 8;
			gridBagConstraints5.ipadx = 22;
			gridBagConstraints5.ipady = -1;
			gridBagConstraints5.gridx = 2;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.insets = new Insets(7, 36, 5, 42);
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.ipadx = 2;
			gridBagConstraints4.ipady = 4;
			gridBagConstraints4.gridx = 0;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.insets = new Insets(4, 20, 2, 5);
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 6;
			gridBagConstraints3.ipadx = 64;
			gridBagConstraints3.gridwidth = 3;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.insets = new Insets(7, 11, 9, 20);
			gridBagConstraints2.gridy = 2;
			gridBagConstraints2.ipadx = 25;
			gridBagConstraints2.gridx = 2;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new Insets(8, 11, 11, 20);
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.ipadx = 46;
			gridBagConstraints1.gridx = 2;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(8, 27, 11, 10);
			gridBagConstraints.gridy = 0;
			gridBagConstraints.ipadx = 2;
			gridBagConstraints.gridx = 1;
			jLabelPicture = new JLabel();
			jLabelPicture.setIcon(new ImageIcon(getClass().getResource("/images/Aplication.jpg")));
			jLabelPicture.setText("JLabel");
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.insets = new Insets(10, 8, 13, 7);
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.gridy = 0;
			gridBagConstraints12.ipadx = -43;
			gridBagConstraints12.ipady = 29;
			gridBagConstraints12.gridheight = 4;
			jLabelEmail2 = new JLabel();
			jLabelEmail2.setText("asimon@ceis.cujae.edu.cu");
			jLabelEmail2.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel1 = new JLabel();
			jLabel1.setText("Alfredo Simón Cuevas");
			jLabel1.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel1.setHorizontalAlignment(SwingConstants.LEFT);
			jLabelEmail3 = new JLabel();
			jLabelEmail3.setText("asuarez@ceis.cujae.edu.cu");
			jLabelEmail3.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabelemail1 = new JLabel();
			jLabelemail1.setText("aruizc@ceis.cujae.edu.cu");
			jLabelemail1.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabelEmail = new JLabel();
			jLabelEmail.setText("Correos:");
			jLabelEmail.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabelTool = new JLabel();
			jLabelTool.setFont(new Font("Dialog", Font.BOLD, 14));
			jLabelTool.setText("Cmap-To-OWL v2.0");
			jLabelPlace = new JLabel();
			jLabelPlace.setText("Desarrollado por el GRIAL en el Centro de Estudio de Ingeniería y Sitema ");
			jLabelPlace.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabelAutor3 = new JLabel();
			jLabelAutor3.setText("Amhed Suaréz Rodríguez");
			jLabelAutor3.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabelAutor3.setHorizontalAlignment(SwingConstants.LEFT);
			jLabelAutor1 = new JLabel();
			jLabelAutor1.setText("Alejandro Ruiz Coutin");
			jLabelAutor1.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabelAutor1.setHorizontalAlignment(SwingConstants.LEFT);
			jLabelAutor = new JLabel();
			jLabelAutor.setText("Autores :");
			jLabelAutor.setFont(new Font("Dialog", Font.PLAIN, 12));
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.setToolTipText("");
			jContentPane.add(jLabelAutor, gridBagConstraints);
			jContentPane.add(jLabelAutor1, gridBagConstraints1);
			jContentPane.add(jLabelAutor3, gridBagConstraints2);
			jContentPane.add(jLabelPlace, gridBagConstraints3);
			jContentPane.add(jLabelTool, gridBagConstraints4);
			jContentPane.add(getJButtonCancelar(), gridBagConstraints5);
			jContentPane.add(jLabelEmail, gridBagConstraints6);
			jContentPane.add(jLabelemail1, gridBagConstraints7);
			jContentPane.add(jLabelEmail3, gridBagConstraints8);
			jContentPane.add(jLabel1, gridBagConstraints9);
			jContentPane.add(jLabelEmail2, gridBagConstraints10);
			jContentPane.add(getJLabel(), gridBagConstraints11);
			jContentPane.add(jLabelPicture, gridBagConstraints13);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	@SuppressWarnings("unused")
	private JLabel getJLabel() {
		if (jLabel == null) {
			jLabel = new JLabel();
			jLabel.setText(" Instituto Superior Politécnico Jose Antonio Echeverría, 2010");
			jLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabel;
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
			jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					mainFrame.setEnabled(true);
					dispose();
				}
			});
		}
		return jButtonCancelar;
	}


}  //  @jve:decl-index=0:visual-constraint="147,14"
