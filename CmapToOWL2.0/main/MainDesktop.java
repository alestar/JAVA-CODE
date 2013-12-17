package main;




import generateFiles.MapGenerate;
import gui.About;
import gui.ProgressBarGenereting;
import gui.SelectKnowlogedModel;
import gui.ManageLinkPhrases;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Label;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;

import mdservice.ServiceRunner;
import Filter.CXLFilesFilter;
import Filter.OwlFilesFilter;
import Filter.XMLFilter;
import cmaps.CMap;
import cmaps.CMapHandler;
import java.awt.ComponentOrientation;
import java.awt.GridBagLayout;
import java.awt.Insets;



public class MainDesktop extends JFrame {

	private ObjectOwl owlOntology;  //  @jve:decl-index=0:	

	private MainDesktop instance;  //  @jve:decl-index=0:visual-constraint="124,39"

	private static final long serialVersionUID = 1L;

	private JPanel mainContentPane = null;

	private JPanel clientPanel = null;

	private JScrollPane mapScrollPane = null;

	private JScrollPane outScrollPane = null;

	private JTextPane mapTextPane = null;

	private JTextPane outTextPane = null;

	private JToolBar toolBar = null;
	
	//private String iconImgPath="/images/";  //  @jve:decl-index=0:
	
	private int iconDim = 40;

	private JButton openMapJButton = null;

	private JButton generateOWLJButton = null;

	private JButton saveOutJButton = null;

	private JButton clearMapJButton = null;

	
	ByteArrayOutputStream baos = new ByteArrayOutputStream();  //  @jve:decl-index=0:

	private JPanel dialogContentPane = null;
	
	ServiceRunner serviceRunner;
	
	private Thread progressBarT ;  //  @jve:decl-index=0:
	
	XMLFilter xmlfilter= new XMLFilter();
	CXLFilesFilter cxlfilter= new CXLFilesFilter();  //  @jve:decl-index=0:
	OwlFilesFilter owlFilter= new OwlFilesFilter();  //  @jve:decl-index=0:
	
	JFileChooser fileChooserOpen = new JFileChooser();  //  @jve:decl-index=0:visual-constraint="1033,1716"
	JFileChooser fileChooserSave = new JFileChooser();  //  @jve:decl-index=0:visual-constraint="69,1716"
	JFileChooser fileRepChooser = new JFileChooser();  //  @jve:decl-index=0:visual-constraint="553,1716"
	
	String mapPath = "";  //  @jve:decl-index=0:

	String mapName = "";  //  @jve:decl-index=0:
	
	String unformattedXmlText = "";  //  @jve:decl-index=0:
	
	CMapHandler HanParser= new CMapHandler();;//Clases parseadora  //  @jve:decl-index=0:
	
	CMap mapparsed = new CMap();//Clase con lista de Conceptos(C-MC) y Frases de Enlace(PP-MC)  //  @jve:decl-index=0:
	

	private JMenu jMenuCmapToOWL = null;

	private JMenuBar jMenuBarCmapToOWL = null;  //  @jve:decl-index=0:visual-constraint="10,664"

	private JMenuItem jMenuCargarMC = null;

	private JMenuItem jMenuItemSave = null;

	private Label labelOWLGenerate = null;

	private Label labelMapOn = null;

	private JMenu jMenuOptions = null;

	private JMenuItem jMenuItemLinkPhrases = null;

	private JMenuItem jMenuItemSelectOWL = null;

	private JMenuItem jMenuItemKnowModel = null;

	private JMenuItem jMenuItemExit = null;

	private JMenu jMenuHelp = null;

	private JMenuItem jMenuItemAbout = null;

	private JMenuItem jMenuItemGenerateOWL = null;

	private JMenuItem jMenuItemNuevoProc = null;

	private JButton manageLinkPhraseButton = null;

	private JButton seleccOWLRepositoryJButton = null;

	private JButton seleccKnowledgeModelJButton = null;

	/**
	 * This method initializes clientPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getClientPanel() {
		if (clientPanel == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.insets = new Insets(22, 197, 4, 183);
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.ipadx = 17;
			gridBagConstraints4.ipady = -3;
			gridBagConstraints4.gridx = 0;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.insets = new Insets(22, 190, 4, 195);
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.ipadx = 15;
			gridBagConstraints3.ipady = -3;
			gridBagConstraints3.gridx = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.BOTH;
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.ipadx = 462;
			gridBagConstraints2.ipady = 481;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.weighty = 1.0;
			gridBagConstraints2.insets = new Insets(5, 12, 5, 20);
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.BOTH;
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.gridy = 1;
			gridBagConstraints11.ipadx = 462;
			gridBagConstraints11.ipady = 481;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.weighty = 1.0;
			gridBagConstraints11.insets = new Insets(5, 16, 5, 11);
			labelMapOn = new Label();
			labelMapOn.setFont(new Font("Dialog", Font.PLAIN, 14));
			labelMapOn.setText(" Mapa Activo");
			labelOWLGenerate = new Label();
			labelOWLGenerate.setFont(new Font("Dialog", Font.PLAIN, 14));
			labelOWLGenerate.setName("labelOWLfile");
			labelOWLGenerate.setEnabled(true);
			labelOWLGenerate.setText("Archivo OWL");
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.weighty = 1.0;
			gridBagConstraints1.gridheight = 2;
			gridBagConstraints1.weightx = 1.0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.weightx = 1.0;
			clientPanel = new JPanel();
			clientPanel.setLayout(new GridBagLayout());
			clientPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			clientPanel.setAlignmentY(0.5F);
			clientPanel.setAlignmentX(0.5F);
			clientPanel.setPreferredSize(new Dimension(536, 350));
			clientPanel.setBackground(SystemColor.control);
			clientPanel.add(getMapScrollPane(), gridBagConstraints11);
			clientPanel.add(getOutScrollPane(), gridBagConstraints2);
			clientPanel.add(labelOWLGenerate, gridBagConstraints3);
			clientPanel.add(labelMapOn, gridBagConstraints4);
		}
		return clientPanel;
	}

	/**
	 * This method initializes mapScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getMapScrollPane() {
		if (mapScrollPane == null) {
			mapScrollPane = new JScrollPane();
			mapScrollPane.setAutoscrolls(true);
			mapScrollPane.setViewportView(getMapTextPane());
		}
		return mapScrollPane;
	}

	/**
	 * This method initializes outScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getOutScrollPane() {
		if (outScrollPane == null) {
			outScrollPane = new JScrollPane();
			outScrollPane.setAutoscrolls(true);
			outScrollPane.setViewportView(getOutTextPane());
		}
		return outScrollPane;
	}

	/**
	 * This method initializes mapTextPane	
	 * 	
	 * @return javax.swing.JTextPane	
	 */
	private JTextPane getMapTextPane() {
		if (mapTextPane == null) {
			mapTextPane = new JTextPane();
			mapTextPane.setEditable(false);
			mapTextPane.setContentType("text/plain");
			mapTextPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			mapTextPane.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 10));
			mapTextPane.setToolTipText("Mapa Conceptual cargado para generar la Ontología");
			mapTextPane.setPreferredSize(new Dimension(0, 0));
		}
		return mapTextPane;
	}

	@SuppressWarnings("static-access")
	private boolean askSave(String message) 
	{
		//Componente que muestra ventanas de opciones
		JOptionPane closeOptionPane=new JOptionPane();
		Object[] opciones={"Aceptar","Cancelar"};
		//Dialogo modal SI_NO
		int ret=closeOptionPane.showOptionDialog(this,message,"Confirmación",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,opciones,opciones[0]);
		//Si la opcion escogida es Si
		if(ret==JOptionPane.YES_OPTION)
			return true;
		else
			return false;

	}

	/**
	 * This method initializes outTextPane	
	 * 	
	 * @return javax.swing.JTextPane	
	 */
	public JTextPane getOutTextPane() {
		if (outTextPane == null) {
			outTextPane = new JTextPane();
			outTextPane.setEditable(false);
			outTextPane.setPreferredSize(new Dimension(0, 0));
			outTextPane.setToolTipText("Ontología generada apartir del Mapa Conceptual Activo");
			outTextPane.setFont(new Font("Microsoft Sans Serif", Font.PLAIN, 10));
		}
		return outTextPane;
	}

	/**
	 * This method initializes toolBar	
	 * 	
	 * @return javax.swing.JToolBar	
	 */
	public JToolBar getToolBar() {
		if (toolBar == null) {
			toolBar = new JToolBar();
			//toolBar.setLayout(new BoxLayout(getToolBar(), BoxLayout.X_AXIS));
			toolBar.setPreferredSize(new Dimension(0, 45));
			toolBar.setMaximumSize(new Dimension(0, 0));
			toolBar.setMinimumSize(new Dimension(0, 0));
			toolBar.setToolTipText("Gestiones para Ontologías y  Mapas conceptuales");
			toolBar.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			toolBar.setFloatable(true);
			toolBar.setName("toolbar");
			toolBar.add(getClearMapJButton());
			toolBar.add(getOpenMapJButton());
			toolBar.add(getSaveOutJButton());
			toolBar.add(getGenerateOWLJButton());
			toolBar.add(getManageLinkPhraseButton());
			toolBar.add(getSeleccOWLRepositoryJButton());
			toolBar.add(getSeleccKnowledgeModelJButton());
		}
		return toolBar;
	}

	/**
	 * This method initializes openMapJButton	
	 * 	Abre un MapaFichero.xml pa realizar el parseo
	 * @return javax.swing.JButton	
	 */
	public JButton getOpenMapJButton() {
		if (openMapJButton == null) {
			openMapJButton = new JButton();
			openMapJButton.setSize(new Dimension(iconDim, iconDim));
			openMapJButton.setIcon(new ImageIcon(getClass().getResource("/images/Folder36.png")));
			openMapJButton.setPreferredSize(new Dimension(iconDim, iconDim));
			openMapJButton.setMaximumSize(new Dimension(iconDim, iconDim));
			openMapJButton.setMinimumSize(new Dimension(iconDim, iconDim));
			openMapJButton.setMnemonic(KeyEvent.VK_UNDEFINED);
			openMapJButton.setToolTipText("Abrir mapa");
			openMapJButton.setEnabled(true);
			openMapJButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

					if(fileChooserOpen.showOpenDialog(MainDesktop.this) == JFileChooser.APPROVE_OPTION){
						StringBuffer lineaTotal= new StringBuffer();
						if(!mapPath.equals(fileChooserOpen.getSelectedFile().getAbsolutePath())){
							try {
								FileReader reader = new FileReader(fileChooserOpen.getSelectedFile().getAbsolutePath());
								BufferedReader read = new BufferedReader(reader);
								String line = read.readLine();
								//Cargo el fichero XML n una gran cadena con etiquetas y todo
								while(line != null ){
									lineaTotal.append(line);
									line = read.readLine();
								}
								if(fileChooserOpen.getFileFilter().getDescription().equals("Formato XML1.0"))
									mapparsed= HanParser.loadXMLMaCoSoft(lineaTotal.toString());
								else if(fileChooserOpen.getFileFilter().getDescription().equals("Formato CXL1.0"))
									mapparsed=  HanParser.loadCXL(lineaTotal.toString());

								if(mapparsed!=null && mapparsed.getPropositions().size()>0){
									owlOntology.setMap(mapparsed);
									unformattedXmlText=lineaTotal.toString();
								}
								else{
									JOptionPane.showMessageDialog(getMainContentPane(), "El mapa seleccionado no es sintaticamente correcto, por favor seleccione un mapa valido","Error",JOptionPane.ERROR_MESSAGE);		
									unformattedXmlText="";
								}
							} 
							catch (Exception exc) {
								exc.printStackTrace();
								JOptionPane.showMessageDialog(getMainContentPane(), "El mapa seleccionado no es sintaticamente correcto, por favor seleccione un mapa valido","Error",JOptionPane.ERROR_MESSAGE);		
							}
							String map  = MapGenerate.showMapFromText(unformattedXmlText);	
							getMapTextPane().setText(map);

							if(map.equalsIgnoreCase("")){
								mapPath = "";
								mapName = "";
								clearMapJButton.setEnabled(false);
								jMenuItemNuevoProc.setEnabled(false);

								generateOWLJButton.setEnabled(false);
								jMenuItemGenerateOWL.setEnabled(false);

								saveOutJButton.setEnabled(false);
								jMenuItemSave.setEnabled(false);
							}
							else{
								mapPath = fileChooserOpen.getSelectedFile().getAbsolutePath();
								mapName = fileChooserOpen.getSelectedFile().getName();

								//getOutTextPane().setText("");

								clearMapJButton.setEnabled(true);
								jMenuItemNuevoProc.setEnabled(true);

								generateOWLJButton.setEnabled(true);
								jMenuItemGenerateOWL.setEnabled(true);

								saveOutJButton.setEnabled(false);
								jMenuItemSave.setEnabled(false);

							}
						}
					}
				}
			});
		}
		return openMapJButton;
	}
	/**
	 * This method initializes generateOWLJButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	public JButton getGenerateOWLJButton() {
		if (generateOWLJButton == null) {
			generateOWLJButton = new JButton();
			generateOWLJButton.setSize(new Dimension(iconDim,iconDim));
			generateOWLJButton.setIcon(new ImageIcon(getClass().getResource("/images/owl36.png")));
			generateOWLJButton.setPreferredSize(new Dimension(iconDim,iconDim));
			generateOWLJButton.setMaximumSize(new Dimension(iconDim,iconDim));
			generateOWLJButton.setMinimumSize(new Dimension(iconDim,iconDim));
			generateOWLJButton.setToolTipText("Generar Ontología OWL");
			generateOWLJButton.setEnabled(false);
			generateOWLJButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

					baos.reset();
					if(unformattedXmlText.equalsIgnoreCase("") == false){
						try{
							serviceRunner = new ServiceRunner(unformattedXmlText,owlOntology);
						
							//se crea un objeto de la clase Thread pasándolo el objeto Runnable como argumento
							Thread serviceRun = new Thread(serviceRunner);
													
							//myThread.setPriority(Thread.MAX_PRIORITY);
							// se arranca el objeto de la clase Thread
							serviceRun.start();
							
							// deshabilito el menu principal y empieza a ejecutarse el run() de la barra de proceso;cuando termina muestra el resultado
							ProgressBarGenereting progBar = new ProgressBarGenereting(serviceRunner,instance, mapName,serviceRun);
							progBar.setVisible(true);

							progressBarT = new Thread(progBar);
							progressBarT.start();
						}
						catch (Exception e2) {
							// TODO: handle exception
							e2.printStackTrace();
						}
					} 
				}
			});
		}
		return generateOWLJButton;
	}

	/**
	 * This method initializes saveOutJButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	public JButton getSaveOutJButton() {
		if (saveOutJButton == null) {
	
			saveOutJButton = new JButton();
			saveOutJButton.setSize(new Dimension(iconDim,iconDim));
			saveOutJButton.setIcon(new ImageIcon(getClass().getResource("/images/floppy36.png")));
			saveOutJButton.setPreferredSize(new Dimension(iconDim,iconDim));
			saveOutJButton.setMinimumSize(new Dimension(iconDim,iconDim));
			saveOutJButton.setMaximumSize(new Dimension(iconDim,iconDim));
			saveOutJButton.setToolTipText("Salvar archivo .owl");
			saveOutJButton.setEnabled(false);
			saveOutJButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					if(fileChooserSave.showSaveDialog(MainDesktop.this) == JFileChooser.APPROVE_OPTION){
						String fileName = fileChooserSave.getSelectedFile().getAbsolutePath();
						String fileSave =fileName;
						try {
							
							File temp = null;
							if(!fileName.substring(fileName.length()-4, fileName.length()).equalsIgnoreCase(".owl"))
								fileName += ".owl";	
								
							  temp = new File(fileName);

								boolean save = true;
	
								if(temp.exists()) {
									String message = "El fichero ya existe. ¿Desea sobreescribirlo?";
									int choose = JOptionPane.showOptionDialog(dialogContentPane, message, "Advertencia", JOptionPane.YES_NO_OPTION, 
											JOptionPane.WARNING_MESSAGE, null, new Object[]{"Aceptar", "Cancelar"}, "Aceptar");
	
									if (choose == JOptionPane.NO_OPTION){
										save = false;
									}
								} 
								if(save){
									owlOntology.saveOntology(fileSave);
								}							
						
						}catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}//action performed
			});
		}
		return saveOutJButton;
	}

	/**
	 * This method initializes clearMapJButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	public JButton getClearMapJButton() {
		if (clearMapJButton == null) {
			clearMapJButton = new JButton();
			clearMapJButton.setIcon(new ImageIcon(getClass().getResource("/images/Limpiar36.png")));
			clearMapJButton.setSize(new Dimension(iconDim, iconDim));
			clearMapJButton.setPreferredSize(new Dimension(iconDim, iconDim));
			clearMapJButton.setMinimumSize(new Dimension(iconDim, iconDim));
			clearMapJButton.setMaximumSize(new Dimension(iconDim, iconDim));
			clearMapJButton.setToolTipText("Nuevo Procesamiento");
			clearMapJButton.setLocation(new Point(0, 0));
			clearMapJButton.setEnabled(false);
			clearMapJButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					String message = "¿Desea relalizar un nuevo procesamiento y borrar todos los datos?";
					int choose = JOptionPane.showOptionDialog(dialogContentPane, message, "Advertencia", JOptionPane.YES_NO_OPTION, 
							JOptionPane.WARNING_MESSAGE, null, new Object[]{"Aceptar", "Cancelar"}, "Aceptar");

					if (choose == JOptionPane.YES_OPTION){
						mapPath = "";
						mapName="";
						getMapTextPane().setText("");
						getOutTextPane().setText("");
	
						jMenuItemNuevoProc.setEnabled(false);
						jMenuItemSave.setEnabled(false);
						jMenuItemGenerateOWL.setEnabled(false);
						
						clearMapJButton.setEnabled(false);
						saveOutJButton.setEnabled(false);				
						generateOWLJButton.setEnabled(false);
					}
				}
			});
		}
		return clearMapJButton;
	}


	/**
	 * This method initializes jMenuCmapToOWL	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getJMenuCmapToOWL() {
		if (jMenuCmapToOWL == null) {
			jMenuCmapToOWL = new JMenu();
			jMenuCmapToOWL.setBackground(SystemColor.controlHighlight);
			jMenuCmapToOWL.setText("Archivo");
			jMenuCmapToOWL.setToolTipText("Gestionar archivos MC/OWL");
			jMenuCmapToOWL.add(getJMenuItemNuevoProc());
			jMenuCmapToOWL.add(getJMenuCargarMC());
			jMenuCmapToOWL.add(getJMenuItemGenerateOWL());
			jMenuCmapToOWL.add(getJMenuItemSave());
			jMenuCmapToOWL.add(getJMenuItemExit());
		}
		return jMenuCmapToOWL;
	}

	/**
	 * This method initializes jMenuBarCmapToOWL	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */
	private JMenuBar getJMenuBarCmapToOWL() {
		if (jMenuBarCmapToOWL == null) {
			jMenuBarCmapToOWL = new JMenuBar();
			jMenuBarCmapToOWL.setPreferredSize(new Dimension(0, 23));
			jMenuBarCmapToOWL.setSize(new Dimension(184, 23));
			jMenuBarCmapToOWL.add(getJMenuCmapToOWL());
			jMenuBarCmapToOWL.add(getJMenuOptions());
			jMenuBarCmapToOWL.add(getJMenuHelp());
		}
		return jMenuBarCmapToOWL;
	}

	/**
	 * This method initializes jMenuCargarMC	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	public JMenuItem getJMenuCargarMC() {
		if (jMenuCargarMC == null) {
			jMenuCargarMC = new JMenuItem();
			jMenuCargarMC.setName("");
			jMenuCargarMC.setToolTipText("");
			jMenuCargarMC.setMnemonic(KeyEvent.VK_UNDEFINED);
			jMenuCargarMC.setEnabled(true);
			jMenuCargarMC.setText("Cargar Mapa Conceptual");
			jMenuCargarMC.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					if(fileChooserOpen.showOpenDialog(MainDesktop.this) == JFileChooser.APPROVE_OPTION){
						StringBuffer lineaTotal= new StringBuffer();
						if(!mapPath.equalsIgnoreCase(fileChooserOpen.getSelectedFile().getAbsolutePath())){
							try {
								FileReader reader = new FileReader(fileChooserOpen.getSelectedFile().getAbsolutePath());
								BufferedReader read = new BufferedReader(reader);
								String line = read.readLine();
								//Cargo el fichero XML n una gran cadena con etiquetas y todo
								while(line != null ){
									lineaTotal.append(line);
									line = read.readLine();
								}
								if(fileChooserOpen.getFileFilter().getDescription().equalsIgnoreCase("Formato XML1.0"))
									mapparsed= HanParser.loadXMLMaCoSoft(lineaTotal.toString());
								else if(fileChooserOpen.getFileFilter().getDescription().equalsIgnoreCase("Formato CXL1.0"))
									mapparsed=  HanParser.loadCXL(lineaTotal.toString());

								if(mapparsed!=null && mapparsed.getPropositions().size()>0){
									owlOntology.setMap(mapparsed);
									unformattedXmlText=lineaTotal.toString();
								}
								else{
									JOptionPane.showMessageDialog(getMainContentPane(), "El mapa seleccionado no es sintaticamente correcto, por favor seleccione un mapa valido","Error",JOptionPane.ERROR_MESSAGE);		
									unformattedXmlText="";
								}
							} 
							catch (Exception exc) {
								exc.printStackTrace();
								JOptionPane.showMessageDialog(getMainContentPane(), "El mapa seleccionado no es sintaticamente correcto, por favor seleccione un mapa valido","Error",JOptionPane.ERROR_MESSAGE);		
							}
							String map  = MapGenerate.showMapFromText(unformattedXmlText);	
							getMapTextPane().setText(map);

							if(map.equalsIgnoreCase("")){
								mapPath = "";
								mapName = "";
								clearMapJButton.setEnabled(false);
								jMenuItemNuevoProc.setEnabled(false);

								generateOWLJButton.setEnabled(false);
								jMenuItemGenerateOWL.setEnabled(false);

								saveOutJButton.setEnabled(false);
								jMenuItemSave.setEnabled(false);
							}
							else{
								mapPath = fileChooserOpen.getSelectedFile().getAbsolutePath();
								mapName = fileChooserOpen.getSelectedFile().getName();

								//getOutTextPane().setText("");

								clearMapJButton.setEnabled(true);
								jMenuItemNuevoProc.setEnabled(true);

								generateOWLJButton.setEnabled(true);
								jMenuItemGenerateOWL.setEnabled(true);

								saveOutJButton.setEnabled(false);
								jMenuItemSave.setEnabled(false);

							}
						}
					}
				}
			});
		}
		return jMenuCargarMC;
	}

	/**
	 * This method initializes jMenuItemSave	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	public JMenuItem getJMenuItemSave() {
		if (jMenuItemSave == null) {
			jMenuItemSave = new JMenuItem();
			jMenuItemSave.setText("Salvar Ontología");
			jMenuItemSave.setEnabled(false);
			jMenuItemSave.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					if(fileChooserSave.showSaveDialog(MainDesktop.this) == JFileChooser.APPROVE_OPTION){
						String fileName = fileChooserSave.getSelectedFile().getAbsolutePath();
						String fileSave =fileName;
						try {
							
							File temp = null;
							if(!fileName.substring(fileName.length()-4, fileName.length()).equalsIgnoreCase(".owl"))
								fileName += ".owl";	
								
							  temp = new File(fileName);

								boolean save = true;
	
								if(temp.exists()) {
									String message = "El fichero ya existe. ¿Desea sobreescribirlo?";
									int choose = JOptionPane.showOptionDialog(dialogContentPane, message, "Advertencia", JOptionPane.YES_NO_OPTION, 
											JOptionPane.WARNING_MESSAGE, null, new Object[]{"Aceptar", "Cancelar"}, "Aceptar");
	
									if (choose == JOptionPane.NO_OPTION){
										save = false;
									}
								} 
								if(save){
									owlOntology.saveOntology(fileSave);
								}							
						
						}catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}//action performed
			});
		}
		return jMenuItemSave;
	}

	/**
	 * This method initializes jMenuOptions	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getJMenuOptions() {
		if (jMenuOptions == null) {
			jMenuOptions = new JMenu();
			jMenuOptions.setText("Opciones");
			jMenuOptions.setToolTipText("Trabajo con modulos independientes");
			jMenuOptions.add(getJMenuItemLinkPhrases());
			jMenuOptions.add(getJMenuItemSelectOWL());
			jMenuOptions.add(getJMenuItemKnowModel());
		}
		return jMenuOptions;
	}

	/**
	 * This method initializes jMenuItemLinkPhrases	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItemLinkPhrases() {
		if (jMenuItemLinkPhrases == null) {
			jMenuItemLinkPhrases = new JMenuItem();
			jMenuItemLinkPhrases.setText("Gestionar Frases de Enlaces");
			jMenuItemLinkPhrases.setToolTipText("Gestionar Frases de Enlaces");
			jMenuItemLinkPhrases.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
				
					ManageLinkPhrases fe = new ManageLinkPhrases(getMainDesktop());
					getMainDesktop().setEnabled(false);
					fe.setVisible(true);
				}
			});
		}
		return jMenuItemLinkPhrases;
	}

	/**
	 * This method initializes jMenuItemSelectOWL	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItemSelectOWL() {
		if (jMenuItemSelectOWL == null) {
			jMenuItemSelectOWL = new JMenuItem();
			jMenuItemSelectOWL.setText("Seleccionar Repositorio de Ontologías");
			jMenuItemSelectOWL.setToolTipText("Seleccionar Repositorio de Ontologías");
			jMenuItemSelectOWL.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

					if(fileRepChooser.showOpenDialog(MainDesktop.this)==JFileChooser.APPROVE_OPTION){
//						...selecionar solo el directorio, donde se encuentra fichero owl seleccionado...
						File dir=fileRepChooser.getSelectedFile();
						if(dir!=null){
							//File dir=fileChooser.getCurrentDirectory();
							String d= dir.getPath();
							File[] owls = (new File(d)).listFiles();//Carga las ontologías para convertirlas en mapas
							if(owls == null||owls.length==0)
								JOptionPane.showMessageDialog(getMainContentPane(), "El directorio esta vacio, por favor seleccione un directorio valido","Error",JOptionPane.ERROR_MESSAGE);
							else{	
								boolean owl=true;
								int i =0;
								while (i<owls.length && owl){//se comprueban que todos los ficheros sean de exyensión .owl
									String filepath = owls[i].getAbsolutePath();//aki se obtine el nombre del fichero
									if(filepath.length()>4 && filepath.substring(filepath.length()-4, filepath.length()).equalsIgnoreCase(".owl")){//check if .owl
										i++;
									}
									else{
										owl=false;
										JOptionPane.showMessageDialog(getMainContentPane(), "EL directorio es invalido o existen fichero con extensión diferente a owl, por favor seleccione un directorio valido","Error",JOptionPane.ERROR_MESSAGE);
									}
								}
								if(owl &&!d.equalsIgnoreCase(owlOntology.getOwlRepositoryPath())){
									owlOntology.setOwlRepositoryPath(d);
									owlOntology.setOwlRepositoryLoaded(false);
									JOptionPane.showMessageDialog(getMainContentPane(), "Se ha seleccionado un nuevo repositorio satisfactoriamente","Aviso",1);
								}
							}
						}
					}
				}
			});
		}
		return jMenuItemSelectOWL;
	}

	/**
	 * This method initializes jMenuItemKnowModel	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItemKnowModel() {
		if (jMenuItemKnowModel == null) {
			jMenuItemKnowModel = new JMenuItem();
			jMenuItemKnowModel.setText("Seleccionar Modelo de Conocimiento");
			jMenuItemKnowModel.setToolTipText("Seleccionar Modelo de Conocimiento");
			jMenuItemKnowModel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					SelectKnowlogedModel knowModel= new SelectKnowlogedModel(getMainDesktop());
					getMainDesktop().setEnabled(false);
					knowModel.setVisible(true);
				}
			});
		}
		return jMenuItemKnowModel;
	}

	/**
	 * This method initializes jMenuItemExit	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItemExit() {
		if (jMenuItemExit == null) {
			jMenuItemExit = new JMenuItem();
			jMenuItemExit.setText("Salir");
			jMenuItemExit.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
						if (askSave("¿Desea salir totalmente?")==true){
							System.exit(0);
						}
						else
							setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				}
			});
		}
		return jMenuItemExit;
	}

	/**
	 * This method initializes jMenuHelp	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getJMenuHelp() {
		if (jMenuHelp == null) {
			jMenuHelp = new JMenu();
			jMenuHelp.setText("Ayuda");
			jMenuHelp.setToolTipText("Acerca de los autores ");
			jMenuHelp.setFont(new Font("Dialog", Font.BOLD, 12));
			jMenuHelp.setForeground(new Color(51, 51, 51));
			jMenuHelp.add(getJMenuItemAbout());
		}
		return jMenuHelp;
	}

	/**
	 * This method initializes jMenuItemAbout	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getJMenuItemAbout() {
		if (jMenuItemAbout == null) {
			jMenuItemAbout = new JMenuItem();
			jMenuItemAbout.setText("Acerca CmapToOWL 2.0");
			jMenuItemAbout.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					About aboutAutors= new About(getMainDesktop());
					getMainDesktop().setEnabled(false);
					aboutAutors.setVisible(true);
				}
			});
		}
		return jMenuItemAbout;
	}

	/**
	 * This method initializes jMenuItemGenerateOWL	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	public JMenuItem getJMenuItemGenerateOWL() {
		if (jMenuItemGenerateOWL == null) {
			jMenuItemGenerateOWL = new JMenuItem();
			jMenuItemGenerateOWL.setText("Generar Ontología");
			jMenuItemGenerateOWL.setEnabled(false);
			jMenuItemGenerateOWL.setToolTipText("Generar Ontología");
			jMenuItemGenerateOWL.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
		    	
					baos.reset();
					if(unformattedXmlText.equalsIgnoreCase("") == false){
						try{
							serviceRunner = new ServiceRunner(unformattedXmlText,owlOntology);
						
							//se crea un objeto de la clase Thread pasándolo el objeto Runnable como argumento
							Thread myThread = new Thread(serviceRunner);
													
							//myThread.setPriority(Thread.MAX_PRIORITY);
							// se arranca el objeto de la clase Thread
							myThread.start();
							
							// deshabilito el menu principal y empieza a ejecutarse el run() de la barra de proceso;cuando termina muestra el resultado
							ProgressBarGenereting progBar = new ProgressBarGenereting(serviceRunner,instance, mapName);
							progBar.setVisible(true);

							progressBarT = new Thread(progBar);
							progressBarT.start();
						}
						catch (Exception e2) {
							// TODO: handle exception
							e2.printStackTrace();
						}
					} 
				}
			});
		}
		return jMenuItemGenerateOWL;
	}

	/**
	 * This method initializes jMenuItemNuevoProc	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	public JMenuItem getJMenuItemNuevoProc() {
		if (jMenuItemNuevoProc == null) {
			jMenuItemNuevoProc = new JMenuItem();
			jMenuItemNuevoProc.setText("Nuevo procesamiento");
			jMenuItemNuevoProc.setEnabled(false);
			jMenuItemNuevoProc.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					String message = "¿Desea relalizar un nuevo procesamiento y borrar todos los datos?";
					int choose = JOptionPane.showOptionDialog(dialogContentPane, message, "Advertencia", JOptionPane.YES_NO_OPTION, 
							JOptionPane.WARNING_MESSAGE, null, new Object[]{"Aceptar", "Cancelar"}, "Aceptar");

					if (choose == JOptionPane.YES_OPTION){
						mapPath = "";
						getMapTextPane().setText("");
						getOutTextPane().setText("");
					
						jMenuItemNuevoProc.setEnabled(false);
						jMenuItemSave.setEnabled(false);
						jMenuItemGenerateOWL.setEnabled(false);
						
						clearMapJButton.setEnabled(false);
						saveOutJButton.setEnabled(false);				
						generateOWLJButton.setEnabled(false);
					}
				}
			});
		}
		return jMenuItemNuevoProc;
	}

	/**
	 * This method initializes manageLinkPhraseButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getManageLinkPhraseButton() {
		if (manageLinkPhraseButton == null) {
			manageLinkPhraseButton = new JButton();
			manageLinkPhraseButton.setEnabled(true);
			manageLinkPhraseButton.setMaximumSize(new Dimension(iconDim,iconDim));
			manageLinkPhraseButton.setMinimumSize(new Dimension(iconDim,iconDim));
			manageLinkPhraseButton.setPreferredSize(new Dimension(iconDim,iconDim));
			manageLinkPhraseButton.setIcon(new ImageIcon(getClass().getResource("/images/fraseEnlace36.png")));
			manageLinkPhraseButton.setSize(new Dimension(iconDim,iconDim));
			manageLinkPhraseButton.setToolTipText("Gestionar Frases de Enlace");
			manageLinkPhraseButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

					ManageLinkPhrases fe = new ManageLinkPhrases(getMainDesktop());
					getMainDesktop().setEnabled(false);
					fe.setVisible(true);
				}
			});
		}
		return manageLinkPhraseButton;
	}

	/**
	 * This method initializes seleccOWLRepositoryJButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSeleccOWLRepositoryJButton() {
		if (seleccOWLRepositoryJButton == null) {
			seleccOWLRepositoryJButton = new JButton();
			seleccOWLRepositoryJButton.setEnabled(true);
			seleccOWLRepositoryJButton.setMaximumSize(new Dimension(iconDim,iconDim));
			seleccOWLRepositoryJButton.setMinimumSize(new Dimension(iconDim,iconDim));
			seleccOWLRepositoryJButton.setPreferredSize(new Dimension(iconDim,iconDim));
			seleccOWLRepositoryJButton.setIcon(new ImageIcon(getClass().getResource("/images/owlRep36.png")));
			seleccOWLRepositoryJButton.setSize(new Dimension(iconDim,iconDim));
			seleccOWLRepositoryJButton.setToolTipText("Seleccionar Repositorio de Ontologías");
			seleccOWLRepositoryJButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					
					if(fileRepChooser.showOpenDialog(MainDesktop.this)==JFileChooser.APPROVE_OPTION){
//						...selecionar solo el directorio, donde se encuentra fichero owl seleccionado...
						File dir=fileRepChooser.getSelectedFile();
						if(dir!=null){
							//File dir=fileChooser.getCurrentDirectory();
							String d= dir.getPath();
							File[] owls = (new File(d)).listFiles();//Carga las ontologías para convertirlas en mapas
							if(owls == null||owls.length==0)
								JOptionPane.showMessageDialog(getMainContentPane(), "El directorio esta vacio, por favor seleccione un directorio valido","Error",JOptionPane.ERROR_MESSAGE);
							else{	
								boolean owl=true;
								int i =0;
								while (i<owls.length && owl){//se comprueban que todos los ficheros sean de exyensión .owl
									String filepath = owls[i].getAbsolutePath();//aki se obtine el nombre del fichero
									if(filepath.length()>4 && filepath.substring(filepath.length()-4, filepath.length()).equalsIgnoreCase(".owl")){//check if .owl
										i++;
									}
									else{
										owl=false;
										JOptionPane.showMessageDialog(getMainContentPane(), "EL directorio es invalido o existen fichero con extensión diferente a owl, por favor seleccione un directorio valido","Error",JOptionPane.ERROR_MESSAGE);
									}
								}
								if(owl &&!d.equalsIgnoreCase(owlOntology.getOwlRepositoryPath())){
									owlOntology.setOwlRepositoryPath(d);
									owlOntology.setOwlRepositoryLoaded(false);
									JOptionPane.showMessageDialog(getMainContentPane(), "Se ha seleccionado un nuevo repositorio satisfactoriamente","Aviso",1);
								}
							}
						}
					}
				}
			});
		}
		return seleccOWLRepositoryJButton;
	}

	/**
	 * This method initializes seleccKnowledgeModelJButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSeleccKnowledgeModelJButton() {
		if (seleccKnowledgeModelJButton == null) {
			seleccKnowledgeModelJButton = new JButton();
			seleccKnowledgeModelJButton.setEnabled(true);
			seleccKnowledgeModelJButton.setMaximumSize(new Dimension(iconDim,iconDim));
			seleccKnowledgeModelJButton.setMinimumSize(new Dimension(iconDim,iconDim));
			seleccKnowledgeModelJButton.setPreferredSize(new Dimension(iconDim,iconDim));
			seleccKnowledgeModelJButton.setIcon(new ImageIcon(getClass().getResource("/images/modeloConocimiento36.png")));
			seleccKnowledgeModelJButton.setSize(new Dimension(iconDim,iconDim));
			seleccKnowledgeModelJButton.setToolTipText("Seleccionar Modelo de Conociento");
			seleccKnowledgeModelJButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					SelectKnowlogedModel knowModel= new SelectKnowlogedModel(getMainDesktop());
					getMainDesktop().setEnabled(false);
					knowModel.setVisible(true);
				}
			});
		}
		return seleccKnowledgeModelJButton;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainDesktop thisClass = new MainDesktop();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);

				File[] libraries = (new File("lib")).listFiles();
				if(libraries != null)
					for (File file : libraries) {
						try {
							DynamicPackagesLoader.addFile(file);
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
				libraries = (new File("xfire")).listFiles();
				if(libraries != null)
					for (File file : libraries) {
						try {
							DynamicPackagesLoader.addFile(file);
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
		
			}
		});
	}

	/**
	 * This is the default constructor
	 */
	public MainDesktop() {
		
		//super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
	
		  //  @jve:decl-index=0:
		File[] libraries = (new File("lib")).listFiles();
		if(libraries != null)
			for (File file : libraries) {
				try {
					DynamicPackagesLoader.addFile(file);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		
		File[] librariesWebService = (new File("xfire")).listFiles();
		if(librariesWebService != null)
			for (File file : librariesWebService) {
				try {
					DynamicPackagesLoader.addFile(file);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

		fileChooserOpen.setDialogType(JFileChooser.OPEN_DIALOG);
		fileChooserOpen.setName("fileChooserOpen");
		fileChooserOpen.setDialogTitle("Abrir Mapa Conceptual");
		fileChooserOpen.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileChooserOpen.setApproveButtonText("Abrir");
		fileChooserOpen.setSize(new Dimension(486, 386));
		fileChooserOpen.addChoosableFileFilter(xmlfilter);
		fileChooserOpen.addChoosableFileFilter(cxlfilter);

		
		fileChooserSave.setDialogType(JFileChooser.SAVE_DIALOG);
		fileChooserSave.setName("fileChooserSave");
		fileChooserSave.setSize(new Dimension(477, 386));
		fileChooserSave.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileChooserSave.setApproveButtonText("Salvar");
		fileChooserSave.setDialogTitle("Salvar Ontología");
		fileChooserSave.addChoosableFileFilter(owlFilter);
		
		fileRepChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileRepChooser.setToolTipText("Seleccionar Repositorio de Ontologías");
		fileRepChooser.setName("fileRepChooser");
		fileRepChooser.setDialogTitle("Seleccionar Repositorio de Ontologías");
		fileRepChooser.setApproveButtonText("Seleccionar");
		fileRepChooser.setSize(new Dimension(477, 387));

		this.setSize(1226, 720);
		this.setResizable(true);
		this.setContentPane(getMainContentPane());
		this.setJMenuBar(getJMenuBarCmapToOWL());
		
		//this.setLocation(new Point(0, 0));
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/oficial.png")));
		this.setTitle("CmapToOWL 2.0");
		this.setFont(new Font("Dialog", Font.PLAIN, 12));
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				if (askSave("¿Desea salir totalmente?")==true){
					System.exit(0);
				}
				else
					setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			}
		});
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);

		PrintStream out = new PrintStream(baos);
		System.setOut(out);
		instance = this;
		instance.setSize(new Dimension(1009,649));
		instance.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/oficial.png")));
		instance.setPreferredSize(new Dimension(0, 0));
		owlOntology= new ObjectOwl();
	}

	/**
	 * This method initializes mainContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMainContentPane() {
		if (mainContentPane == null) {
			mainContentPane = new JPanel();
			mainContentPane.setLayout(new BorderLayout());
			mainContentPane.setPreferredSize(new Dimension(536, 500));
			mainContentPane.setBackground(SystemColor.activeCaptionBorder);
			mainContentPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			mainContentPane.add(getClientPanel(), BorderLayout.CENTER);
			mainContentPane.add(getToolBar(), BorderLayout.NORTH);
		}
		return mainContentPane;
	}

	public void setOutTextPane(JTextPane outTextPane) {
		this.outTextPane = outTextPane;
		outTextPane.setBounds(new Rectangle(-2147483648, -2147483648, -1, -1));
	}

	public ByteArrayOutputStream getBaos() {
		return baos;
	}
	public JFrame getMainDesktop(){
		return this;
	}
	public ObjectOwl getObjectOwl(){
		return owlOntology;
	}

	public Thread getProgressBarT() {
		return progressBarT;
	}

	public void setProgressBarT(Thread progressBarT) {
		this.progressBarT = progressBarT;
	}

}  //  @jve:decl-index=0:visual-constraint="271,721"
