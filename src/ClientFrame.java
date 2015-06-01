import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;


public class ClientFrame extends JFrame implements ActionListener{
	static final int WHITE = 1;
	static final int BLACK = 0;
	static final int EMPTY = -1;
	static final int SIZEX = 530;
	static final int SIZEY = 530;
	
	private static ChessClient client;
	private JMenu game = new JMenu("游戏");
	//private JMenu fit = new JMenu("设置");
	private JMenu help = new JMenu("帮助");
	private JMenu newGame = new JMenu("新游戏");
	//private JMenu level = new JMenu("等级");
	private JMenuItem pFirst = new JMenuItem("傻逼逼");
	private JMenuItem cFirst = new JMenuItem("傻积极");
	private JMenuItem back = new JMenuItem("悔棋");
	private JMenuItem exit = new JMenuItem("退出");
	private JMenuItem save = new JMenuItem("电脑存棋");
	private JMenuItem about=new JMenuItem("想知道关于很腻害的Sookie的事情咩");
	private JMenuBar bar=new JMenuBar();
	private ButtonGroup group = new ButtonGroup();
	

	public ClientFrame(int color)
	{
		super("五子棋");
		
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
		client = new ChessClient(color);
		add(client);
		
		//this.setBounds(0,0,538,585);

		this.setTitle("五子棋");
		this.setSize(SIZEX, SIZEY);
		this.setLocation(30, 50);
		//this.addMouseListener(this);
		this.setResizable(false);//窗体不可变
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		
	}


	public static void main(String args[])
	{
		
		ClientFrame c = new ClientFrame(WHITE);
		Thread t = new Thread(c.client);
		//t1.start();
		t.start();
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
