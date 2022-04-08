package application;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import db.DB;
import db.DbException;

public class Program {

	public static void main(String[] args) {
		
		Connection conn = null;
		Statement st = null;
		try {
			conn = DB.getConnection();
			
			//Para n�o confirmar as opera��es no autom�tico:
			conn.setAutoCommit(false);
			
			st = conn.createStatement();
			
			//saber o n�mero de linhas afetadas
			//Todo vendedor que pertencer ao Department 1, o sal�rio ser� atualizado para 1999:
			int rows1 = st.executeUpdate("UPDATE seller SET BaseSalary = 1999 WHERE DepartmentId = 1");
			
			int quebrar = 1;
			if(quebrar < 2) {
				throw new SQLException("Fake error");
			}
			
			//Todo vendedor que pertencer ao Department 2, o sal�rio ser� atualizado para 2550:
			int rows2 = st.executeUpdate("UPDATE seller SET BaseSalary = 2550 WHERE DepartmentId = 2");

			conn.commit();//Para confirmar que agora sim minha transa��o terminou
			
			System.out.println("rows1 " + rows1);
			System.out.println("rows2 " + rows2);
		}catch(SQLException e) {
			//l�gica para voltar a transa��o caso ela se interrompa no meio:
			//n�o mudar� nada no banco caso ocorra!
			try {
				conn.rollback();
				throw new DbException("Transa��o n�o concluida! Caused by: " + e.getMessage());
			} catch (SQLException e1) {
				//Caso Rollback quebre:
				throw new DbException("Error ao tentar voltar a transa��o! Caused by: " + e1.getMessage());
			}
		}
		finally {
			DB.closeStatement(st);
			DB.closeConnection();
		}
	}
}
