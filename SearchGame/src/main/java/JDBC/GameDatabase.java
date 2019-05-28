package JDBC;
/**
 * 面向对象理解练习
 */

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

import org.apache.commons.dbcp.BasicDataSource;


/*需求：有3种游戏类型，游戏类型分为ACT、RTS、RPG。
*现有以下游戏：鬼泣（ACT），命令与征服（RTS），龙与地下城（RPG），勇者斗恶龙（RPG），红色警戒（RTS）
*，怪物猎人（RPG·ATC），噬神者（ACT·RPG），神界（RPG·RTS），如龙（RPG·ACT），帝国时代（RTS）。
*一个游戏可能同时属于2种游戏类型。
*这些游戏又分别由4个不同用户所拥有一部分 ，每个用户分别拥有自己的用户名及密码:
*Kamui:鬼泣，怪物猎人。		ID:Kamui  PW:001	Genders：male  Age:25
*Subaru:命令与征服，红色警戒，帝国时代，龙与地下城。	ID:Subaru  PW:002	Genders：male  Age:18
*Luna:神界，如龙，勇者斗恶龙，龙与地下城。		ID:Luna  PW:003		Genders：female  Age:17
*Nerd:鬼泣，命令与征服，龙与地下城，勇者斗恶龙，红色警戒，怪物猎人，噬神者，神界，如龙，帝国时代。  
*ID:Nerd  PW:999	Genders：unknow  Age:0
*现要求在输入查找游戏类型、游戏名称、用户，这3个条件中的任意一项时可以输出另外两项的信息。
*当通过用户名进行查询时，必须输入密码。并在成功后输出用户所有信息。
*在用户名，游戏类型，游戏名输入有误时提供相关信息反馈用户。
*程序运行时需提供用户选择：1.通过游戏名查找，2.通过游戏类型查找，3.登录用户ID-输出用户所拥有的游戏。
*/

/**
 * 程序分析： 属性关系：类型-游戏 相关，游戏-用户相关，类型-用户相关。
 * 
 */

//---------------------------------------------------------------------------------------
/**
 * 数据库类
 * 
 * @author Administrator
 *
 */
class DataBaseDrive {
	/*
	 * 数据库初始化 读取配置文件、设置数据库连接信息。
	 */
	private static BasicDataSource ds;
	static {
		// 创建数据源对象
		ds = new BasicDataSource();
		// 读取配置文件
		Properties p = new Properties();
		InputStream ips = GameDatabase.class.getClassLoader().getResourceAsStream("jdbc.properties");
		try {
			p.load(ips);
			String driver = p.getProperty("driver");
			String url = p.getProperty("url");
			String username = p.getProperty("username");
			String password = p.getProperty("password");
			// 设置连接信息
			ds.setDriverClassName(driver);
			ds.setUrl(url);
			ds.setUsername(username);
			ds.setPassword(password);

			ds.setInitialSize(3);// 设置初始链接数量
			ds.setMaxActive(5);// 设置最大连接数量
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 获取连接对象并返回
	 */
	public static Connection getConn() throws Exception {
		Connection conn = ds.getConnection();
		return conn;
	}
}
//---------------------------------------------------------------------------------------
/**
 * 查询功能
 * @author Administrator
 *
 */
class Search {
	private Connection conn;
	private PreparedStatement ps ;
	private ResultSet rs;
	public Search(String choice,Connection conn) {
			this.conn = conn ;
			switch (choice) {
			case "1":
				System.out.println("---→已选择通过游戏名查询！");
				Gname();
				break;
			case "2":
				System.out.println("---→已选择通过游戏类型查询！");
				Gtype();
				break;
			case "3":
				System.out.println("---→已选择通过登录玩家账号查询！");
				User();
				break;
			}
	}
	//游戏名查询
	private void Gname() {
		Scanner sc = new Scanner(System.in);
		System.out.println("游戏名：");
		String input = sc.next();
		String sql = "select * from Game where name='"+input+"';";
		Gsearch(sql);
	}
	//游戏类型查询
	private void Gtype() {
		Scanner sc = new Scanner(System.in);
		System.out.println("游戏类型：");
		String input = sc.next();
		String sql = "select * from Game where type like '%"+input+"%';";
		Gsearch(sql);
	}
	//游戏表查询方法
	private void Gsearch(String sql) {
		try {
			this.ps = this.conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				System.out.println("游戏名称："+ rs.getString("name") + 
						"  游戏类型：" +rs.getString("type") + 
						"  游戏玩家：" + rs.getString("user"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//用户表查询方法
	private void User() {
		Scanner sc = new Scanner(System.in);
		System.out.println("用户名：");
		String name = sc.next();
		System.out.println("密码：");
		String password = sc.next();
		String sql = "select * from User where name='" + name + "'and password='" + password + "'";
		try {
			this.ps = this.conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()) {
				System.out.println("用户名："+ rs.getString("name") + 
						"  性别：" +rs.getString("gender") + 
						"  年龄：" + rs.getInt("age") + 
						"已拥有游戏:" + rs.getString("game"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}


//---------------------------------------------------------------------------------------
/**
 * 主程序
 * 
 * @author Administrator
 *
 */
public class GameDatabase {
	static {
		try (Connection conn = DataBaseDrive.getConn()) {
			// 取得数据库连接
			DatabaseMetaData meta = conn.getMetaData();
			ResultSet rs1 = meta.getTables(null, null, "Game", null);
			ResultSet rs2 = meta.getTables(null, null, "User", null);
			//仅首次启动时初始化创建表插入数据。
			if (!rs1.next() && !rs2.next()) {
				conn.setAutoCommit(false);
				String sql[] = new String[4];
				// 创建游戏/用户表
				sql[0] = "create table Game(name varchar(10),type varchar(100),user varchar(100)) character set utf8;";
				sql[1] = "create table User(name varchar(10),password varchar(10),gender varchar(10),age int,game varchar(100)) character set utf8;";
				// 插入游戏数据
				sql[2] = "insert into Game(name,type,user) values" + 
						"('鬼泣','ACT','Kamui,Nerd')," + 
						"('命令与征服','RTS','Subaru,Nerd')," + 
						"('龙与地下城','RPG','Subaru,Luna,Nerd')," + 
						"('勇者斗恶龙','RPG','Luna,Nerd')," + 
						"('红色警戒','RTS','Subaru,Nerd')," + 
						"('怪物猎人','RPG,ACT','Kamui,Nerd')," + 
						"('噬神者','ACT,RPG','Nerd')," + 
						"('神界','RPG,RTS','Nerd')," + 
						"('如龙','RPG,ACT','Luna,Nerd')," + 
						"('帝国时代','RTS','Subaru,Nerd');";
				// 插入用户数据
				sql[3] = "insert into User(name,password,gender,age,game) values"
						+ "('Kamui','001','male',25,'鬼泣,怪物猎人'),"
						+ "('Subaru','002','male',18,'命令与征服，红色警戒，帝国时代，龙与地下城'),"
						+ "('Luna','003','female',17,'如龙,勇者斗恶龙,龙与地下城'),"
						+ "('Nerd','999','unknow',0,'鬼泣,命令与征服,龙与地下城,勇者斗恶龙,红色警戒,怪物猎人,噬神者,神界,如龙,帝国时代');";
				Statement st = conn.createStatement();
				for (int i = 0; i < sql.length; i++) {
					st.addBatch(sql[i]);
				}
				st.executeBatch();
				conn.commit();
				System.out.println("数据库首次初始化完毕！");
			} else {
				System.out.println("数据库已初始化！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//主方法
	public static void main(String[] args) {
		try (Connection conn = DataBaseDrive.getConn()) {
			select(conn);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//选择
	public static void select(Connection conn) {
		System.out.println("------------------------------------------");
		System.out.println("请选择查询模式：");
		System.out.println("已进入游戏信息库查询程序！请选择查询方法：（请输入整数1/2/3）");
		System.out.println("1.通过游戏名查询");
		System.out.println("2.通过游戏类型查询");
		System.out.println("3.通过登录玩家账号查询");
		Scanner sc = new Scanner(System.in);
		String choice = sc.next();
		new Search(choice,conn);
	}
}
