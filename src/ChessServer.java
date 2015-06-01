import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ChessServer extends JPanel implements MouseListener, Runnable {

	/**
	 * ��ָ���˿�������һ��������
	 * 
	 * @param port
	 *            :���������ԵĶ˿�
	 */
	static final int WHITE = 1;
	static final int BLACK = 0;
	static final int EMPTY = -1;
	static final int chessSIZE = 16;
	public static int[][] chess = new int[chessSIZE][chessSIZE];// ���̴�С
	int currRow = 0;
	int currCol = 0;
	static final int SIZEX = 530;
	static final int SIZEY = 530;
	static final int graphLOCATEX = 20;
	static final int graphLOCATEY = 20;
	static final int graphSIZEX = 500;
	static final int graphSIZEY = 480;
	static final int graphOFFSET = 35;
	private BufferedImage chessBoardImage = null, whiteImage = null,
			blackImage = null;// ͼƬ
	static final boolean isBlack = true;
	// ��ʶ��ǰ�Ǻ��廹�ǰ���
	boolean isMyTurn = true;
	// ��ʾ��ǰ��Ϸ�Ƿ���Լ���
	boolean canPlay = true;

	//private chessQueue queue;
	private ArrayList<Point> chessProcess = new ArrayList<Point>();// ��ʾÿһ�������µ��Ⱥ�˳�����ڻ���

	Socket client;
	ServerSocket server;
	OutputStream out;
	InputStream in;
	PrintStream ps;
	Reader r;
	Writer w;
	BufferedReader br;
	
	
	public ChessServer(int mycolor) {
		super();
		// ��ʼ�����ù�
				for (int i = 0; i < chess.length; i++)
					for (int j = 0; j < chess.length; j++) {
						chess[i][j] = EMPTY;
					}
				
				try {
					chessBoardImage = ImageIO.read(new File(
							"F:/learning/Java/image/chessboard.jpg"));
					blackImage = ImageIO.read(new File("F:/learning/Java/image/b.jpg"));
					whiteImage = ImageIO.read(new File("F:/learning/Java/image/w.jpg"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				this.addMouseListener(this);
				//queue = new chessQueue(true);
	}
	
	
	@Override
	public void paint(Graphics g) {

		// TODO Auto-generated method stub
		g.drawImage(chessBoardImage, 0, 0, this);
		// g.setColor(Color.orange);
		// g.setFont(new Font("����",Font.BOLD,20));
		// g.drawString("��Ϸ��Ϣ", 120, 60);
		// g.setFont(new Font("����",Font.BOLD,12));
		// g.drawString("�ڷ�ʱ�䣺", 60, 470);
		// g.drawString("�׷�ʱ�䣺", 260, 470);
		/*
		 * g.drawRect(graphLOCATEX, graphLOCATEY, graphSIZEX, graphSIZEY);
		 * 
		 * for(int i=graphLOCATEY;i<=graphLOCATEY+graphSIZEY;i+=graphOFFSET) {
		 * g.drawLine(graphLOCATEX, i, graphLOCATEX+graphSIZEX, i);
		 * g.drawLine(graphLOCATEX, i+1, graphLOCATEX+graphSIZEX, i+1); }
		 * for(int j=graphLOCATEX;j<=graphLOCATEX+graphSIZEX;j+=graphOFFSET) {
		 * g.drawLine(j, graphLOCATEY, j, graphLOCATEY+graphSIZEY);
		 * g.drawLine(j+1, graphLOCATEY, j+1, graphLOCATEY+graphSIZEY); }
		 * g.fillOval(graphLOCATEX+graphOFFSET*3-1,
		 * graphLOCATEY+graphOFFSET*3-1, 4, 4);
		 * g.fillOval(graphLOCATEX+graphOFFSET*12-1,
		 * graphLOCATEY+graphOFFSET*3-1, 4, 4);
		 * g.fillOval(graphLOCATEX+graphOFFSET*3-1,
		 * graphLOCATEY+graphOFFSET*12-1, 4, 4);
		 * g.fillOval(graphLOCATEX+graphOFFSET*12-1,
		 * graphLOCATEY+graphOFFSET*12-1, 4, 4);
		 */
		// ÿ����һ��paint����repaint��ɨ���������̣���������״̬��������
		for (int i = 0; i < chessSIZE; i++)
			for (int j = 0; j < chessSIZE; j++) {
				int tempX = i * graphOFFSET + graphLOCATEX;
				int tempY = j * graphOFFSET + graphLOCATEY;
				if (chess[i][j] == WHITE) {
					g.setColor(Color.WHITE);
					g.fillOval(tempX - 5, tempY - 5, 13, 13);
					g.setColor(Color.BLACK);
					g.drawOval(tempX - 5, tempY - 5, 13, 13);
				} else if (chess[i][j] == BLACK) {
					g.setColor(Color.BLACK);
					g.fillOval(tempX - 5, tempY - 5, 13, 13);

				}
			}
	}
	
	
	private String get() {// �������Ϊɶ�����⣡����������
		String str = null;
		
		try {
			str = br.readLine();
			System.out.println("������һ�����꣺" + str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
	public void put(String str) {
		
		while(isMyTurn == false);
		
			ps.print(str + "\n");
			ps.flush();
		
		System.out.println("����������һ����ɫ����");
		isMyTurn = false;

}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String str;
		try {
			// ��������ָ���˿��ϵķ���������
			server = new ServerSocket(7777);
			System.out.println("�����������ɹ���" + 7777);
			System.out.println("�ȴ������С�����");
				client = server.accept();
				// �����Ӷ����ϵõ��������������q
				out = client.getOutputStream();
				// String s = "���,��ӭ��javaKe.com\r\n";
				// byte[] data = s.getBytes();// ȡ���������ַ������ֽ�
				ps = new PrintStream(out);
				
				//String strIn = br.readLine();
				//System.out.println(strIn);
				//ps.print(strIn); // ��������������ݣ�
				//ps.flush();// ǿ�����
			// �÷���������ȴ�״̬:����״̬
			while (true) {
				br = new BufferedReader(new InputStreamReader(client.getInputStream()));
				if(br.ready()) {
					str = br.readLine();
					System.out.println("������һ�����꣺" + str);
					String temp[] = str.split(",");
					currRow = Integer.valueOf(temp[0]);
					currCol = Integer.valueOf(temp[1]);
					System.out.println("�ֵ�������" + isMyTurn + " --���ǲ��Ǻ�ɫ��"
							+ isBlack);
					if (this.isBlack)
						chess[currRow][currCol] = WHITE;
					else
						chess[currRow][currCol] = BLACK;
					this.repaint();
					this.isGameOver();
					isMyTurn = true;
				/*
				while (!"null".equals(str = this.get())) {
					String temp[] = str.split(",");
					currRow = Integer.valueOf(temp[0]);
					currCol = Integer.valueOf(temp[1]);
					System.out.println("�ֵ�������" + isMyTurn + " --���ǲ��Ǻ�ɫ��"
							+ isBlack);
					if (this.isBlack)
						chess[currRow][currCol] = WHITE;
					else
						chess[currRow][currCol] = BLACK;
					this.repaint();
					this.isGameOver();
					isMyTurn = true;
					*/
				}
			}
		} catch (Exception ef) {
			ef.printStackTrace();
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// �����ͻ���������
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				if (canPlay == false) {
					return;
				}
				System.out.println(e.getX() + "---" + e.getY());
				this.currRow = e.getX();// ��ȡ��ǰ�е�����
				this.currCol = e.getY();// ��ȡ��ǰ�е�����

				
				if (this.isMyTurn) {// ����ֵ�����
					if (this.currRow >= graphLOCATEX
							&& this.currRow <= graphLOCATEX + graphSIZEX + 5
							&& this.currCol >= graphLOCATEY
							&& this.currCol <= graphLOCATEY + graphSIZEY + 5) {
						// ������е������Ƿ������̵ķ�Χ��
						// System.out.println(Thread.currentThread().getName()+"IsMyTurn:"
						// + isMyTurn + " --isBlack:"
						// + isBlack);
						// ��ӡ�ǲ��ǲ��ֵ�����and���ǲ��Ǻ�ɫ

						// �ѵ�ǰ�õ��ĺ��������õ����̵���ֵ,ѡ��ȽϽ�����ֵ
						if ((float) ((float) (this.currRow - graphLOCATEX) / (float) graphOFFSET)
								- ((this.currRow - graphLOCATEX) / graphOFFSET) <= 0.3) {
							this.currRow = (this.currRow - graphLOCATEX) / graphOFFSET;
						} else if ((float) ((float) (this.currRow - graphLOCATEX) / (float) graphOFFSET)
								- ((this.currRow - graphLOCATEX) / graphOFFSET) >= 0.7) {
							this.currRow = (this.currRow - graphLOCATEX) / graphOFFSET
									+ 1;
						} else
							return;

						//
						// �ѵ�ǰ�õ������������õ����̵���ֵ,ѡ��ȽϽ�����ֵ
						if ((float) ((float) (this.currCol - graphLOCATEY) / (float) graphOFFSET)
								- ((this.currCol - graphLOCATEY) / graphOFFSET) <= 0.3) {
							this.currCol = (this.currCol - graphLOCATEY) / graphOFFSET;
						} else if ((float) ((float) (this.currCol - graphLOCATEY) / (float) graphOFFSET)
								- ((this.currCol - graphLOCATEY) / graphOFFSET) >= 0.7) {
							this.currCol = (this.currCol - graphLOCATEY) / graphOFFSET
									+ 1;
						} else
							return;

						// System.out.println(currRow + "  " + currCol);
						System.out.println("���ǲ��Ǻ�ɫ?"+isBlack + "======�����ڵ�״̬��?"
								+ chess[currRow][currCol]);

						// ��鵱ǰ���е�����״̬�ǲ��ǿյ�
						if (chess[currRow][currCol] == EMPTY) {
							System.out.println(chess[currRow][currCol]);
							// ����ǿյ�,��鵱ǰ��ҵ������ǲ��Ǻ�ɫ
							if (this.isBlack == false) {
								// ���ǵĻ�,��ǰ����״̬��Ϊ��ɫ
								chess[currRow][currCol] = WHITE;
								// �ѵ�ǰ����ֵ��ջ
								chessProcess.add(new Point(currRow, currCol));
								// this.isBlack = true;
							} else {
								// �ǵĻ�,״̬��Ϊ��ɫ
								chess[currRow][currCol] = BLACK;
								chessProcess.add(new Point(currRow, currCol));
								// this.isBlack = false;
							}
							// ���»�����,(���������������ǵ�״̬,�൱�����µ���paint()����)
							
							// if(isBlack)
							this.repaint();
							//System.out.println("�ֵ�������" + isMyTurn + " --���ǲ��Ǻ�ɫ��"
							//		+ isBlack);

							// ��һ���ַ�����¼�������ֵ,��,�Ÿ���
							String putstr = currRow + "," + currCol;
							// �Ѵ�����ֵ������������
							put(putstr);
							
							//this.Check();
							// �����Ϸ������û��,��˭Ӯ
							this.isGameOver();
							
						} else {
							// ����״̬���ǿյ�,�����㲻�����������ֵ�ĵط���
							JOptionPane.showMessageDialog(this, "��ǰ��������,����������!");
						}
						this.repaint();
					}

				} else {
					// ��������ֵ���,��ô�Ͳ��ܸı�����״̬,�������ֵ��Է���!(���Ǵ˴�û����!)
					JOptionPane.showMessageDialog(this, "�ֵ��Է����壡");
				}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	private void isGameOver() {
		// �ж���������Ƿ��������������5��
		boolean winFlag = this.Check_Win();
		if (winFlag) {
			JOptionPane.showMessageDialog(this, "��Ϸ����,"
					+ (chess[currRow][currCol] == BLACK ? "�ڷ�" : "�׷�") + "��ʤ");
			canPlay = false;
		}
	}
	private boolean Check_Win() {
		boolean flag = false;
		// ���湲�ж�����ͬ��ɫ����������
		int count = 1;
		// ���жϺ���,�ص㣬y������ͬ����chess[x][y]��y��ͬ
		// �ж�������ӵ���ɫ
		int color = chess[currRow][currCol];
		// �жϺ���
		count = Check_Count(1, 0, color);
		if (count >= 5) {
			flag = true;
		} else {
			// �ж�����
			count = Check_Count(0, 1, color);
			if (count >= 5) {
				flag = true;
			} else {
				// �ж����ϣ�����
				count = Check_Count(1, -1, color);
				if (count >= 5) {
					flag = true;
				} else {
					// �ж����ϣ�����
					count = Check_Count(1, 1, color);
					if (count >= 5) {
						flag = true;
					}
				}
			}
		}
		return flag;
	}

	// �ж��������ӵ�����
	private int Check_Count(int xChange, int yChange, int color) {
		int count = 1;
		int tempX = xChange;
		int tempY = yChange;
		while (color == chess[currRow + xChange][currCol + yChange]) {
			count++;
			if (xChange != 0) {
				xChange++;
			}
			if (yChange != 0) {
				if (yChange > 0)
					yChange++;
				if (yChange < 0)
					yChange--;
			}
		}

		xChange = tempX;
		yChange = tempY;

		while (color == chess[currRow - xChange][currCol - yChange]) {
			count++;
			if (xChange != 0) {
				xChange++;
			}
			if (yChange != 0) {
				if (yChange > 0)
					yChange++;
				if (yChange < 0)
					yChange--;
			}
		}

		return count;
	}


}
