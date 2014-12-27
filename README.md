Gobang-Online
=============

My First Repository in GitHub
##The Project contains:
###GobangServer.java
两个五子棋客户端之间的多线程中间服务器，目前无法实现主线程监听，其他线程处理客户机之间的通信问题。
- 两个五子棋客户机之间传递的是当前下的棋子的行列值
###ChessFrame.java
- 这是五子棋的界面框架
###FiveChess.java
- 这是五子棋的主体代码
- FiveChess(int) //构造函数,构造窗体框架
- paint(Graphics) // 画棋子函数
- mousePressed(MouseEvent) //鼠标监听
- isGameOver()//判断游戏是否结束
- Check_Win()//判断是否有人赢
- Check_Count(int, int, int)//计算连在一起的棋子个数
- get()//获取当前对方下的新增棋子的行列值
- run()//线程运行函数,如果对方新下了棋子,刷新棋盘状态
###ChessQueue.java
- 这是负责处理五子棋客户机通信与多线程的代码
- chessQueue(boolean)//构造函数
- init()//初始化,创建客户机
- putWhite(String)//下一步白色棋子
- putBlack(String)//下一步黑色棋子
- isBlackTurn()//获取当前轮到的棋子的颜色

