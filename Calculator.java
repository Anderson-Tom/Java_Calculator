import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public final class Calculator extends JFrame implements ActionListener {

	private JButton buttons[] = new JButton[25];
	private String btnText[] = {"\u2190","CE","C","\u00B1","\u221A","7","8","9","/","\u215fx","4","5","6","*","X\u00b2","1","2","3","-","=","0"," ",".","+"};
	private JPanel keyPad = new JPanel();
	private JPanel display = new JPanel();
	private JPanel calc = new JPanel();
	private JPanel topBar = new JPanel();
	private JLabel lblShort = new JLabel();
	private JLabel lblLong = new JLabel();
	private GridBagConstraints gbcDefault, gbcTall, gbcWide;
	public Calculator() {
		
		setTitle("Calculator");
		setResizable(false);
		keyPad.setLayout(new GridBagLayout());	
		
		gbcTall = new GridBagConstraints();
		gbcTall.gridx = 4;
		gbcTall.gridy = 3;
		gbcTall.gridheight =2;
		gbcTall.insets = new Insets( 2, 2, 2, 2 );
		gbcTall.fill = GridBagConstraints.BOTH;
		
		gbcWide = new GridBagConstraints();
		gbcWide.gridx = 0;
		gbcWide.gridy = 4;	
		gbcWide.gridwidth = 2;
		gbcWide.insets = new Insets( 2, 2, 2, 2 );
		gbcWide.fill = GridBagConstraints.BOTH;
		
		gbcDefault = new GridBagConstraints();
		gbcDefault.fill = GridBagConstraints.BOTH;
		gbcDefault.insets = new Insets( 2, 2, 2, 2 );
		
		for (int i =0; i < btnText.length; i++) {
			buttons [i] = new JButton(btnText[i]);
			buttons[i].setFont(new Font("MONOSPACE", Font.PLAIN, 12));
			buttons[i].setMargin(new Insets(5, 7, 5, 7));
			switch (i) {
			case 19:
			keyPad.add(buttons[i], gbcTall);
			buttons[i].addActionListener(this);
			break;
			
			case 20:
			keyPad.add(buttons[i], gbcWide);
			buttons[i].addActionListener(this);
			i++;
			break;
			
			default:	
			gbcDefault.gridy = i/5;
			gbcDefault.gridx = i %5;
			keyPad.add(buttons[i], gbcDefault);	
			buttons[i].addActionListener(this);
			break;
			}
		}
		calc.setBorder(BorderFactory.createLineBorder(this.getBackground(), 8));
		calc.setLayout(new BorderLayout(0, 8));
		display.setBackground(Color.WHITE);
		display.setSize(100, 50);
		display.setBorder(BorderFactory.createEtchedBorder());
		lblShort.setFont(new Font("serif", Font.PLAIN, 18));
		lblShort.setHorizontalAlignment(JLabel.TRAILING);
		lblShort.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 8));
		lblLong.setFont(new Font("serif", Font.PLAIN, 12));
		lblLong.setHorizontalAlignment(JLabel.TRAILING);
		lblLong.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
		display.setLayout(new BorderLayout());	
		display.add(lblLong, "Center");
		display.add(lblShort, "South");
		display.setPreferredSize(new Dimension (45, 60));
		calc.add(topBar, "North");
		calc.add(display, "Center");
		calc.add(keyPad,"South");
		add(calc);
		pack();
		setVisible(true);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);}});
	}
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		lblShort.setText(CalcEngine.inputChar(command));
		lblLong.setText( CalcEngine.getCalculation());	
	}
	public static void main(String[] args) {
		Calculator calc = new Calculator();
	}
}