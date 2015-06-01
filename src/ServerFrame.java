import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class ServerFrame extends JFrame implements ActionListener{
	static final int WHITE = 1;
	static final int BLACK = 0;
	static final int EMPTY = -1;
	static final int SIZEX = 530;
	static final int SIZEY = 530;
	
	private static ChessServer server;
	private JMenu game = new JMenu("��Ϸ");
	//private JMenu fit = new JMenu("����");
	private JMenu help = new JMenu("����");
	private JMenu newGame = new JMenu("����Ϸ");
	//private JMenu level = new JMenu("�ȼ�");
	private JMenuItem pFirst = new JMenuItem("");
	private JMenuItem cFirst = new JMenuItem("");
	private JMenuItem back = new JMenuItem("����");
	private JMenuItem exit = new JMenuItem("�˳�");
	private JMenuItem save = new JMenuItem("���Դ���");
	private JMenuItem about=new JMenuItem("");
	private JMenuBar bar=new JMenuBar();
	private ButtonGroup group = new ButtonGroup();
	

	public ServerFrame(int color)
	{
		super("������");
		
		pFirst.addActionListener(this);
		cFirst.addActionListener(this);
		back.addActionListener(this);
		exit.addActionListener(this);
		save.addActionListener(this);
		about.addActionListener(this);
		game.add(newGame);
		game.add(back);
		game.add(save);
		game.add(exit);		
		//fit.add(level);
		help.add(about);
		newGame.add(pFirst);
		newGame.add(cFirst);
		bar.add(game);
		//bar.add(fit);
		bar.add(help);
		this.setJMenuBar(bar);
		server = new ChessServer(color);
		add(server);
		
		//this.setBounds(0,0,538,585);

		this.setTitle("������");
		this.setSize(SIZEX, SIZEY);
		this.setLocation(30, 50);
		//this.addMouseListener(this);
		this.setResizable(false);//���岻�ɱ�
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		
	}


	public static void main(String args[])
	{
		ServerFrame c = new ServerFrame(BLACK);
		Thread t = new Thread(c.server);
		t.start();		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
