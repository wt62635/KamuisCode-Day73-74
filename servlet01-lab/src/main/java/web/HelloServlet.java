package web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelloServlet extends HttpServlet {

	/**
	 * ������(����tomcat)�յ�����֮�� ������ǽ����������ݰ������ҽ��������� ���ݴ�ŵ�request��������(������Ա
	 * ֻ��Ҫ����request����ķ����Ϳ��Ի�� �������ݰ��е�����),ͬʱ���������ᴴ�� response����(������Աֻ��Ҫ������������
	 * ��response�����ϣ������������Ǵ�����Ӧ���� ��������)�� �����������������service���������ҽ�
	 * request�����response������Ϊ�����Ĳ��� ��������
	 */
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * ��ȡ�������ֵ ������������ܴ����������nullֵ��
		 */
		String name = request.getParameter("name");
		String age = request.getParameter("age");
		int ageInt = Integer.parseInt(age) + 1;
		/*
		 * ����content-type��Ϣͷ������ ����������������ص��������͡�
		 */
		response.setContentType("text/html");
		/*
		 * ͨ��response�������������
		 */
		PrintWriter out = response.getWriter();
		/*
		 * �����ݴ�ŵ�response�����ϡ� �������response�����ϻ�ȡ���ݣ� ������Ӧ���ݰ���Ȼ����Ӧ���ݰ� ���͸��������
		 */
		out.println("<h1>Hello " + name + "</h1>");
		out.println("  Age:" + ageInt + "</h1>");

		out.close();
	}
}