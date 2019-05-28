package web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelloServlet extends HttpServlet {

	/**
	 * 当容器(比如tomcat)收到请求之后， 会帮我们解析请求数据包，并且将解析到的 数据存放到request对象里面(开发人员
	 * 只需要调用request对象的方法就可以获得 请求数据包中的数据),同时，容器还会创建 response对象(开发人员只需要将处理结果存放
	 * 到response对象上，容器会用我们创建响应数据 包并发送)。 接下来，容器会调用service方法，并且将
	 * request对象和response对象作为方法的参数 传进来。
	 */
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 读取请求参数值 请求参数名不能错，否则会获得null值。
		 */
		String name = request.getParameter("name");
		String age = request.getParameter("age");
		int ageInt = Integer.parseInt(age) + 1;
		/*
		 * 设置content-type消息头，告诉 浏览器，服务器返回的数据类型。
		 */
		response.setContentType("text/html");
		/*
		 * 通过response对象获得输出流。
		 */
		PrintWriter out = response.getWriter();
		/*
		 * 将数据存放到response对象上。 容器会从response对象上获取数据， 创建响应数据包，然后将响应数据包 发送给浏览器。
		 */
		out.println("<h1>Hello " + name + "</h1>");
		out.println("  Age:" + ageInt + "</h1>");

		out.close();
	}
}
