package gui;

import generateFiles.MapGenerate;

import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import main.MainDesktop;
import mdservice.ServiceRunner;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class ProgressBarGenereting extends JDialog implements Runnable {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;
	private JProgressBar procProgressBar = null;
	private JButton jButtonCancelar = null;
	
	private ServiceRunner processGen = null;
	@SuppressWarnings("unused")
	private Thread serviceRun = null;
	private MainDesktop main = null;
	
	/**
	 * @param owner
	 */
	public ProgressBarGenereting(ServiceRunner processgen, MainDesktop pmain, String fileName, Thread threadService) {
		super(pmain);
		initialize(fileName);
		
		processGen = processgen;
		serviceRun=threadService;
		main = pmain;
	}
	/**
	 * @param owner
	 */
	public ProgressBarGenereting(ServiceRunner processgen, MainDesktop pmain, String fileName) {
		super(pmain);
		initialize(fileName);
		
		processGen = processgen;
		main = pmain;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize(String fileName) {
		this.setSize(312, 128);
		this.setTitle("Procesando el mapa " + fileName );
		this.setContentPane(getJContentPane());
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);
		//this.setUndecorated(true);
		this.procProgressBar.setIndeterminate(true);
		// this.setModal(true);
		this.addWindowListener(new WindowAdapter()
		{        	
			@SuppressWarnings("deprecation")
			public void windowClosing(WindowEvent e)
			{

				setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				processGen.setStoped(true);
				serviceRun.stop();
				main.getClearMapJButton().setEnabled(true);
				main.getJMenuItemNuevoProc().setEnabled(true);
				
				main.getOpenMapJButton().setEnabled(true);
				main.getJMenuCargarMC().setEnabled(true);
				
				main.getJMenuItemGenerateOWL().setEnabled(true);
				main.getGenerateOWLJButton().setEnabled(true);
				
				main.setEnabled(true);
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
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new Insets(11, 102, 12, 109);
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.gridx = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(7, 29, 11, 23);
			gridBagConstraints.gridy = 0;
			gridBagConstraints.ipadx = 96;
			gridBagConstraints.ipady = 4;
			gridBagConstraints.gridx = 0;
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getprocProgressBar(), gridBagConstraints);
			jContentPane.add(getJButtonCancelar(), gridBagConstraints1);
		}
		return jContentPane;
	}

	/**
	 * This method initializes procProgressBar	
	 * 	
	 * @return javax.swing.procProgressBar	
	 */
	private JProgressBar getprocProgressBar() {
		if (procProgressBar == null) {
			procProgressBar = new JProgressBar();
			procProgressBar.setString("Espere, por favor...");
			procProgressBar.setStringPainted(true);
		}
		return procProgressBar;
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
			jButtonCancelar.setEnabled(true);
			jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					processGen.setStoped(true);
					main.getClearMapJButton().setEnabled(true);
					main.getJMenuItemNuevoProc().setEnabled(true);
					
					main.getOpenMapJButton().setEnabled(true);
					main.getJMenuCargarMC().setEnabled(true);
					
					main.getJMenuItemGenerateOWL().setEnabled(true);
					main.getGenerateOWLJButton().setEnabled(true);
					
					main.setEnabled(true);
					dispose();
				}//action performed
			});
		}
		return jButtonCancelar;
	}

	@SuppressWarnings("static-access")
	public void run() {
		main.getOpenMapJButton().setEnabled(false);
		main.getJMenuCargarMC().setEnabled(false);
		
		main.getGenerateOWLJButton().setEnabled(false);
		main.getJMenuItemGenerateOWL().setEnabled(true);
		
		main.getSaveOutJButton().setEnabled(false);
		main.getJMenuItemSave().setEnabled(false);
		
		main.getClearMapJButton().setEnabled(false);
		
		main.setEnabled(false);
		this.requestFocus();

		while (!processGen.isEnded() && !processGen.isStoped()){//...Este ciclo es infinito hasta que se finalice o se detenga progressGen 
			Thread.currentThread().yield();
			try {
				Thread.currentThread().sleep(10);
			} catch (InterruptedException e) {//esto devuelve una excepción de que el hilo es interrumpido cuando processGen.isStoped()==true... 
				e.printStackTrace();//...pero no se trata por que la ideas es en algun momento que se desee interrumpirlo
				//Thread.currentThread().wait();
			}
			Thread.currentThread().yield();
		}

		String resultProcess = processGen.getResult();//Se obtiene la ontología generada en una cadena de texto para posteriormente imprimirla...

		if(resultProcess.equals("")) {//si no se obtuvo ningun resultado y la ontología es nula...
			JOptionPane.showMessageDialog(getContentPane(), "El mapa escogido no ha podido ser procesado","Aviso",1);
			main.getSaveOutJButton().setEnabled(false);
			main.getJMenuItemSave().setEnabled(false);
		}  
		else{
			main.getOutTextPane().setText(MapGenerate.showMapFromText(resultProcess));
			main.getSaveOutJButton().setEnabled(true);
			main.getJMenuItemSave().setEnabled(true);
		}
		main.setEnabled(true);
		
		main.getClearMapJButton().setEnabled(true);
		main.getJMenuItemNuevoProc().setEnabled(true);
		
		main.getOpenMapJButton().setEnabled(true);
		main.getJMenuCargarMC().setEnabled(true);
		
		main.getJMenuItemGenerateOWL().setEnabled(true);
		main.getGenerateOWLJButton().setEnabled(true);
		
		dispose();		
		
	}

}  //  @jve:decl-index=0:visual-constraint="218,63"
