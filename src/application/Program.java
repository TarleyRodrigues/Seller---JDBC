package application;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import db.DB;

public class Program {

	public static void main(String[] args) {
		
		Connection conn = null;
		Statement st = null;
		try {
			conn = DB.getConnection();
			
			st = conn.createStatement();
			
			//saber o número de linhas afetadas
			//Todo vendedor que pertencer ao Department 1, o salário será atualizado para 2090:
			int rows1 = st.executeUpdate("UPDATE seller SET BaseSalary = 2090 WHERE DepartmentId = 1");
			//Todo vendedor que pertencer ao Department 2, o salário será atualizado para 3090:
			int rows2 = st.executeUpdate("UPDATE seller SET BaseSalary = 3090 WHERE DepartmentId = 2");

			System.out.println("rows1 " + rows1);
			System.out.println("rows2 " + rows2);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			DB.closeStatement(st);
			DB.closeConnection();
		}
	}
}
