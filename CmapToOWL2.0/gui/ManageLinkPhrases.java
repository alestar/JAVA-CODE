package gui;


import java.awt.Dimension;
import java.awt.Font;
import java.awt.Label;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import cmaps.CmapLink;

import main.MainDesktop;
import main.ObjectOwl;
import wordnetAccess.WNAccess;
import java.awt.Rectangle;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.ListSelectionModel;
//import javax.swing.border.TitledBorder;


public class ManageLinkPhrases extends JDialog {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel linkContentPane = null;
	private JTextField linkTextField = null;
	private JComboBox categoryComboBox = null;
	private JButton insertLinkPhraseBtn = null;
	private JButton cancelManageLinkBt = null;
	private JPanel buttonsPanel = null;
	private JPanel linkPanel = null;
	private JFrame mainFrame = null;
    private UneditableTableModel listLinks = null;  //  @jve:decl-index=0:visual-constraint="583,52"
    ArrayList<CmapLink> cmaplinks=new ArrayList<CmapLink>();  //  @jve:decl-index=0:
	private JScrollPane linksScrollPane = null;
	private JTable linkTable =  null;
	private int selectedRow = -1;

	private Label labelFrase = null;

	private Label labelCategory = null;
	private JButton modifyLinkPhraseBtn = null;
	private JButton deleteLinkPhrase = null;

	/**
	 * @param owner
	 */
	public ManageLinkPhrases(JFrame owner) {
		super(owner);
		mainFrame = owner; 
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(406, 324);
		this.setTitle("Gestionar Frases de Enlace");
		this.setName("ManageLinkPhrases");
		this.setContentPane(getLinkContentPane());
		
		this.addWindowListener(new WindowAdapter()
		{        	
			public void windowClosing(WindowEvent e)
			{
				insertAllLinkWord(cmaplinks);//Borra el fichero y reinserta todos las tuplas en el fichero...
				mainFrame.setEnabled(true);
				dispose();
			}
		});
		this.setContentPane(getLinkContentPane());
		this.setTitle("Gestionar Frases de Enlace");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);
        
        linkContentPane.getRootPane().setDefaultButton(insertLinkPhraseBtn);
        this.setResizable(false);
        loadCategories();
        loadLinks(this.cmaplinks);
	}

	
	
	private void loadCategories() {
			getCategoryComboBox().addItem("Subclasificacion");
			getCategoryComboBox().addItem("Instancia");
			getCategoryComboBox().addItem("Propiedad");
			getCategoryComboBox().addItem("Valor de Propiedad");
	}
	
	private void insertAllLinkWord(ArrayList<CmapLink> newcmaplinks) {
		 getWNAccess().addAllLinks(newcmaplinks);
	}
	
	private ArrayList<CmapLink> insertLinkWord(String linkName, String category,int pos) {
		boolean found=false;
		ArrayList<CmapLink> cmapLinks= this.cmaplinks;
		if(pos==-1)
			pos=this.cmaplinks.size();
		
		Iterator<CmapLink> iter= cmapLinks.iterator();
		while (iter.hasNext() && !found) {
			CmapLink cmaplink =  iter.next();
			
			if(cmaplink.getLinkName().equals(linkName))
				found=true;
		}
		if(!found){
			CmapLink cmaplink=new CmapLink();
			cmaplink.setLinkName(linkName);
			cmaplink.setCategory(category);
			cmapLinks.add(pos, cmaplink);
		}
		else
			cmapLinks=null;
		return cmapLinks;
		//return getWNAccess().addLink(linkName, category);
	}
	
	private ArrayList<CmapLink> deleteLinkWord(String linkName) {
		boolean del=false; 
		int i=0;
		ArrayList<CmapLink> cmapLinks= this.cmaplinks;
		while (i <cmapLinks.size()&& !del) {
			
			CmapLink cmaplink =  cmapLinks.get(i);
			if(cmaplink.getLinkName().equals(linkName)){
				cmapLinks.remove(i);
				del=true;
			}
			else
				i++;
		}
		return cmapLinks;
	}
	
	private ArrayList<CmapLink> changeLinkWord(String newlinkName, String newcategory,int pos ) {
		ArrayList<CmapLink> cmapLinks= this.cmaplinks;

		cmapLinks.get(pos).setLinkName(newlinkName);
		cmapLinks.get(pos).setCategory(newcategory);
		
		return cmapLinks;
	}
	
	private void loadLinks(ArrayList<CmapLink> cmaplinks) {
		ArrayList<String> links=new ArrayList<String>();
		ArrayList<String> linkCategories = new ArrayList<String>();
		if(cmaplinks==null)
			cmaplinks=getWNAccess().getCmapLinks();
		else if(cmaplinks.size()==0){
			cmaplinks=getWNAccess().getCmapLinks();
			this.cmaplinks.addAll(cmaplinks);
		}
		
		for (CmapLink cmaplinksItem : cmaplinks) {
			links.add(cmaplinksItem.getLinkName());
			linkCategories.add(cmaplinksItem.getCategory());
		}
		listLinks = new UneditableTableModel();
		listLinks.setRowCount(links.size());
		listLinks.addColumn("Frase-Enlace", links.toArray());
		listLinks.addColumn("Categorias", linkCategories.toArray());

		linkTable.setModel(listLinks); 
	}
	
	/**
	 * This method initializes linkTable	
	 * 	LLena la tabla con todos las frases de enlaces y sus respectivas categorías
	 * @return javax.swing.JTable	
	 */
	@SuppressWarnings("unchecked")
	private JTable getLinkTable() {
		if (linkTable == null) {
			linkTable = new JTable();
			linkTable.setCellSelectionEnabled(true);
			linkTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			linkTable.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					selectedRow = linkTable.getSelectedRow();
					getLinkTextField().setText((String)(linkTable.getValueAt(selectedRow, 0)));
					getCategoryComboBox().setSelectedItem(linkTable.getValueAt(selectedRow, 1));					
				}
			});
		}
		return linkTable;
	}
	
	/**
	 * This method initializes cancelManageLinkBt	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCancelManageLinkBt() {
		if (cancelManageLinkBt == null) {
			cancelManageLinkBt = new JButton();
			cancelManageLinkBt.setText("Cancelar");
			cancelManageLinkBt.setBounds(new Rectangle(299, 6, 87, 26));
			cancelManageLinkBt.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					insertAllLinkWord(cmaplinks);//Borra el fichero y reinserta todos las tuplas en el fichero...
					mainFrame.setEnabled(true);
					dispose();
				}
			});
		}
		return cancelManageLinkBt;
	}
	

		
	/**
	 * This method initializes linkContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getLinkContentPane() {
		if (linkContentPane == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.BOTH;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.ipadx = -63;
			gridBagConstraints2.ipady = -319;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.weighty = 1.0;
			gridBagConstraints2.insets = new Insets(5, 7, 3, 3);
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new Insets(14, 6, 4, 4);
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.ipadx = 5;
			gridBagConstraints1.ipady = 5;
			gridBagConstraints1.gridx = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(3, 6, 12, 4);
			gridBagConstraints.gridy = 2;
			gridBagConstraints.ipadx = 389;
			gridBagConstraints.ipady = 37;
			gridBagConstraints.gridx = 0;
			linkContentPane = new JPanel();
			linkContentPane.setLayout(new GridBagLayout());
			linkContentPane.setFont(new Font("Dialog", Font.PLAIN, 12));
			linkContentPane.add(getButtonsPanel(), gridBagConstraints);
			linkContentPane.add(getLinkPanel(), gridBagConstraints1);
			linkContentPane.add(getLinksScrollPane(), gridBagConstraints2);
			
		}
		return linkContentPane;
	}
	private JTextField getLinkTextField() {
		if (linkTextField == null) {
			linkTextField = new JTextField();
			linkTextField.addKeyListener(new java.awt.event.KeyAdapter() {
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
		return linkTextField;
	}

	/**
	 * This method initializes categoryComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getCategoryComboBox() {
		if (categoryComboBox == null) {
			categoryComboBox = new JComboBox();
			categoryComboBox.setPreferredSize(new Dimension(4, 20));
		}
		return categoryComboBox;
	}
	
	/**
	 * This method initializes buttonsPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */	
	private JPanel getButtonsPanel() {
		if (buttonsPanel == null) {
			buttonsPanel = new JPanel();
			buttonsPanel.setLayout(null);
			buttonsPanel.setBorder(BorderFactory.createLineBorder(SystemColor.activeCaptionText, 1));
			buttonsPanel.setBackground(SystemColor.activeCaptionBorder);
			buttonsPanel.add(getInsertLinkPhraseBtn(), null);
			buttonsPanel.add(getCancelManageLinkBt(), null);
			buttonsPanel.add(getModifyLinkPhraseBtn(), null);
			buttonsPanel.add(getDeleteLinkPhrase(), null);
		}
		return buttonsPanel;
	}

	/**
	 * This method initializes linkPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getLinkPanel() {
		if (linkPanel == null) {
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.insets = new Insets(16, 127, 22, 15);
			gridBagConstraints7.gridy = 1;
			gridBagConstraints7.ipadx = -7;
			gridBagConstraints7.ipady = 2;
			gridBagConstraints7.gridx = 0;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.insets = new Insets(10, 83, 12, 15);
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.ipadx = -8;
			gridBagConstraints6.ipady = 2;
			gridBagConstraints6.gridx = 0;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.gridy = 1;
			gridBagConstraints5.ipadx = 113;
			gridBagConstraints5.ipady = 10;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.insets = new Insets(12, 16, 22, 34);
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints4.gridx = 1;
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.ipadx = 113;
			gridBagConstraints4.ipady = 5;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.insets = new Insets(11, 16, 12, 34);
			labelCategory = new Label();
			labelCategory.setText("Categoría :");
			labelCategory.setFont(new Font("Dialog", Font.PLAIN, 14));
			labelFrase = new Label();
			labelFrase.setText("Frase de Enlace :");
			labelFrase.setFont(new Font("Dialog", Font.PLAIN, 14));
			linkPanel = new JPanel();
			linkPanel.setLayout(new GridBagLayout());
			linkPanel.setToolTipText("Datos de la Frase de Enlace");
			//linkPanel.setBorder(BorderFactory.createTitledBorder(null, "Datos de la frase de enlace", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			linkPanel.add(getLinkTextField(), gridBagConstraints4);
			linkPanel.add(getCategoryComboBox(), gridBagConstraints5);
			linkPanel.add(labelFrase, gridBagConstraints6);
			linkPanel.add(labelCategory, gridBagConstraints7);
		}
		return linkPanel;
	}

	/**
	 * This method initializes linksScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getLinksScrollPane() {
		if (linksScrollPane == null) {
			linksScrollPane = new JScrollPane();
			linksScrollPane.setViewportView(getLinkTable());
		}
		return linksScrollPane;
	}

	@SuppressWarnings("unused")
	private String findSeparator(String linkWord) {
		// TODO Auto-generated method stub
       String result ="";
       char[] res = linkWord.toCharArray();
       int index =0;
        while (index<res.length) {
			char word = res[index];
        	
        	if (word == ' ') {
        		res [index] = '_';
        		result = result + "_"; 
        	}	
        	else
        		result = result + res[index];	
        	index++;
		}
       // result = res.toString();
		return result;
	}
	
	public ObjectOwl getoOWL(){
		return ((MainDesktop) mainFrame).getObjectOwl();
	}
	
	public WNAccess getWNAccess(){
		return ((MainDesktop) mainFrame).getObjectOwl().getWnAccessfiles21();
	}
	
	/**
	 * This method initializes insertLinkPhraseBtn	
	 * 	Aqui es donde se insertan las frases de enlace
	 * @return javax.swing.JButton	
	 */
	private JButton getInsertLinkPhraseBtn() {
		if (insertLinkPhraseBtn == null) {
			insertLinkPhraseBtn = new JButton();
			insertLinkPhraseBtn.setText("Insertar");
			insertLinkPhraseBtn.setBounds(new Rectangle(4, 6, 88, 26));
			insertLinkPhraseBtn.addActionListener(new java.awt.event.ActionListener() {
				@SuppressWarnings("unchecked")
				public void actionPerformed(java.awt.event.ActionEvent e) {
					String linkName = getLinkTextField().getText();

					if(linkName.equals("")) {
						JOptionPane.showMessageDialog(getLinkContentPane(), "Debe entrar alguna frase de enlace","Error",JOptionPane.ERROR_MESSAGE);
						getLinkTextField().requestFocus(true);
					}
					else {
						String linkCateg = getCategoryComboBox().getSelectedItem().toString(); 
						ArrayList<CmapLink> cmapLinks=insertLinkWord(linkName, linkCateg,selectedRow);
						if(cmapLinks!=null){
							cmaplinks=cmapLinks;
							if (cmaplinks.size()>0) {//...Ver la posibilidad de insertar una f.e si no se ha insertado anteriormente...
								loadLinks(cmaplinks);//refrescar el JTable con la frase-enlace insertada
								JOptionPane.showMessageDialog(getLinkContentPane(), "Frase de enlace insertada satisfactoriamente","Confirmación",1);
							}
						}
						else{
							JOptionPane.showMessageDialog(getLinkContentPane(), "Ya existe esa frase de enlace enlace","Aviso",1);
						}
						getLinkTextField().setText("");
						getLinkTextField().requestFocus(true);
					}
				}
			});
		}
		return insertLinkPhraseBtn;
	}

	/**
	 * This method initializes modifyLinkPhraseBtn	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getModifyLinkPhraseBtn() {
		if (modifyLinkPhraseBtn == null) {
			modifyLinkPhraseBtn = new JButton();
			modifyLinkPhraseBtn.setText("Modificar");
			modifyLinkPhraseBtn.setBounds(new Rectangle(99, 6, 88, 26));
			modifyLinkPhraseBtn.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					String newlinkName = getLinkTextField().getText();

					if(newlinkName.equals("")) {
						JOptionPane.showMessageDialog(getLinkContentPane(), "Debe entrar alguna nueva frase de enlace para modificar la anterior","Error",JOptionPane.ERROR_MESSAGE);
						getLinkTextField().requestFocus(true);
					}
					else {
						String newlinkCateg = getCategoryComboBox().getSelectedItem().toString(); 
						cmaplinks=changeLinkWord(newlinkName, newlinkCateg,selectedRow);
						if (cmaplinks.size()>0) {//...Ver la posibilidad de insertar una f.e si no se ha insertado anteriormente...
							loadLinks(cmaplinks);//... efresca el JTable con las frases-enlace que estan el ficero fisic
							JOptionPane.showMessageDialog(getLinkContentPane(), "Frase de enlace modificada satisfactoriamente","Confirmación",1);
						}
						else{
							JOptionPane.showMessageDialog(getLinkContentPane(), "Ya existe esa frase de enlace","Aviso",1);
						}
						getLinkTextField().setText("");
						getLinkTextField().requestFocus(true);
					}
				
				}
			});
		}
		return modifyLinkPhraseBtn;
	}

	/**
	 * This method initializes deleteLinkPhrase	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getDeleteLinkPhrase() {
		if (deleteLinkPhrase == null) {
			deleteLinkPhrase = new JButton();
			deleteLinkPhrase.setText("Eliminar");
			deleteLinkPhrase.setBounds(new Rectangle(194, 6, 98, 26));
			deleteLinkPhrase.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					String linkName = getLinkTextField().getText();
					if(linkName.equals("")) {

						JOptionPane.showMessageDialog(getLinkContentPane(), "Seleccione la frase de enlace que desea eliminar","Error",JOptionPane.ERROR_MESSAGE);
						getLinkTextField().requestFocus(true);
					}
					else {
						cmaplinks=deleteLinkWord(linkName);
						if(cmaplinks.size()>0){
							loadLinks(cmaplinks);//... efresca el JTable con las frases-enlace que estan el ficero fisic
							JOptionPane.showMessageDialog(getLinkContentPane(), "Frase de enlace eliminada satisfactoriamente","Confirmación",1);
						}
						else{
							JOptionPane.showMessageDialog(getLinkContentPane(), "No se ha eliminado la frase de enlace porque no existe","Aviso",1);
						}
						getLinkTextField().setText("");
						getLinkTextField().requestFocus(true);
					}
				}
			});
		}
		return deleteLinkPhrase;
	}
	

}  //  @jve:decl-index=0:visual-constraint="58,121"
