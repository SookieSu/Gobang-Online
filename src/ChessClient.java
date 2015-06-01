import java.awt.Color;
import java.awt.Font;
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
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ChessClient extends JPanel implements MouseListener, Runnable {
	static final int WHITE = 1;
	static final int BLACK = 0;
	static final int EMPTY = -1;
	static final int chessSIZE = 16;
	public static int[][] chess = new int[chessSIZE][chessSIZE];// 棋盘大小
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
			blackImage = null;// 图片
	static final boolean isBlack = false;
	// 标识当前是黑棋还是白棋
	boolean isMyTurn = true;
	// 表示当前游戏是否可以继续
	boolean canPlay = true;

	InputStream in;
	OutputStream out;
	Reader r;
	Writer w;
	BufferedReader br;
	PrintStream ps;
	Socket client;
	// int whoWin = EMPTY;//表示赢家的颜色，初始为空
	//private chessQueue queue;
	private ArrayList<Point> chessProcess = new ArrayList<Point>();// 表示每一个棋子下的先后顺序，用于悔棋

	public ChessClient(int mycolor) {
		// 初始化不用管
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

	// 不用管
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		g.drawImage(chessBoardImage, 0, 0, this);
		// g.setColor(Color.orange);
		// g.setFont(new Font("黑体",Font.BOLD,20));
		// g.drawString("游戏信息", 120, 60);
		// g.setFont(new Font("宋体",Font.BOLD,12));
		// g.drawString("黑方时间：", 60, 470);
		// g.drawString("白方时间：", 260, 470);
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
		// 每调用一次paint或者repaint，扫描整个棋盘，更新棋子状态，画棋子
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

	// 不用管
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if (canPlay == false) {
			return;
		}
		System.out.println(e.getX() + "---" + e.getY());
		this.currRow = e.getX();// 获取当前行的坐标
		this.currCol = e.getY();// 获取当前列的坐标


		if (this.isMyTurn) {// 如果轮到我了
			if (this.currRow >= graphLOCATEX
					&& this.currRow <= graphLOCATEX + graphSIZEX + 5
					&& this.currCol >= graphLOCATEY
					&& this.currCol <= graphLOCATEY + graphSIZEY + 5) {
				// 检查行列的坐标是否在棋盘的范围内
				// System.out.println(Thread.currentThread().getName()+"IsMyTurn:"
				// + isMyTurn + " --isBlack:"
				// + isBlack);
				// 打印是不是不轮到我了and我是不是黑色

				// 把当前得到的横坐标计算得到棋盘的行值,选择比较近的行值
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
				// 把当前得到的纵坐标计算得到棋盘的列值,选择比较近的列值
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
				System.out.println("我是不是黑色?" + isBlack + "======我现在的状态是?"
						+ chess[currRow][currCol]);

				// 检查当前行列的棋子状态是不是空的
				if (chess[currRow][currCol] == EMPTY) {
					System.out.println(chess[currRow][currCol]);
					// 如果是空的,检查当前玩家的棋子是不是黑色
					if (this.isBlack == false) {
						// 不是的话,当前棋子状态设为白色
						chess[currRow][currCol] = WHITE;
						// 把当前行列值入栈
						chessProcess.add(new Point(currRow, currCol));
						// this.isBlack = true;
					} else {
						// 是的话,状态设为黑色
						chess[currRow][currCol] = BLACK;
						chessProcess.add(new Point(currRow, currCol));
						// this.isBlack = false;
					}
					// 重新画棋盘,(更新棋盘上棋子们的状态,相当于重新调用paint()函数)

					this.repaint();
					// System.out.println("轮到我了吗？" + isMyTurn + " --我是不是黑色？"
					// + isBlack);

					// 用一个字符串记录这个行列值,用,号隔开
					String putstr = currRow + "," + currCol;
					// 把此行列值传到服务器上
					put(putstr);
					
				
					// 检查游戏结束了没有,看谁赢
					this.isGameOver();
					

				} else {
					// 棋子状态不是空的,告诉你不能下这个行列值的地方啦
					JOptionPane.showMessageDialog(this, "当前已有棋子,不能再下了!");
				}
				this.repaint();
			}

		} else {
			// 如果不是轮到我,那么就不能改变棋子状态,告诉你轮到对方拉!(可是此处没有用!)
			JOptionPane.showMessageDialog(this, "轮到对方下棋！");
		}
	}

	// 不用管
	private void isGameOver() {
		// 判断这个棋子是否和其他棋子连成5个
		boolean winFlag = this.Check_Win();
		if (winFlag) {
			JOptionPane.showMessageDialog(this, "游戏结束,"
					+ (chess[currRow][currCol] == BLACK ? "黑方" : "白方") + "获胜");
			canPlay = false;
		}
	}

	// 不用管
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	// 不用管
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	// 不用管
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	// 不用管
	private boolean Check_Win() {
		boolean flag = false;
		// 保存共有多少相同颜色的棋子相连
		int count = 1;
		// 先判断横向,特点，y坐标相同，即chess[x][y]中y相同
		// 判断这个棋子的颜色
		int color = chess[currRow][currCol];
		// 判断横向
		count = Check_Count(1, 0, color);
		if (count >= 5) {
			flag = true;
		} else {
			// 判断纵向
			count = Check_Count(0, 1, color);
			if (count >= 5) {
				flag = true;
			} else {
				// 判断右上，左下
				count = Check_Count(1, -1, color);
				if (count >= 5) {
					flag = true;
				} else {
					// 判断左上，右下
					count = Check_Count(1, 1, color);
					if (count >= 5) {
						flag = true;
					}
				}
			}
		}
		return flag;
	}

	// 判断棋子连接的数量
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

	// 不用管

	private String get() {// 这个函数为啥有问题！！！！！！
		String str = null;
		
		try {
			str = br.readLine();
			System.out.println("读到了一次坐标：" + str);
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
		System.out.println("客户端走了一步白色棋子");
		isMyTurn = false;

}
	public void run() {// 这个函数为啥有问题！！！！！！
		// TODO Auto-generated method stub

		String str = null;
		try {
			client = new Socket("127.0.0.1", 7777);
					System.out.println("连接成功！");
					// 从连接对象上得到输入输出流对象
					out = client.getOutputStream();


					ps = new PrintStream(out);
				
					//
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
				
		while (true) {

			try {
				br = new BufferedReader(new InputStreamReader(client.getInputStream()));
				if(br.ready()) {
					str = br.readLine();
					System.out.println("读到了一次坐标：" + str);
					String temp[] = str.split(",");
					currRow = Integer.valueOf(temp[0]);
					currCol = Integer.valueOf(temp[1]);
					System.out.println("轮到我了吗？" + isMyTurn + " --我是不是黑色？"
							+ isBlack);
					if (this.isBlack)
						chess[currRow][currCol] = WHITE;
					else
						chess[currRow][currCol] = BLACK;
					this.repaint();
					this.isGameOver();
					isMyTurn = true;
				}

				//String strIn = br.readLine();
				//System.out.println(strIn);

				//ps.print(strIn); // 用输出对象发送数据！
				//ps.flush();// 强制输出

			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally
			{
//				try {
//					client.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}

		}
	}
}
