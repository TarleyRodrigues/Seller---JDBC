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
			
			//Para não confirmar as operações no automático:
			conn.setAutoCommit(false);
			
			st = conn.createStatement();
			
			//saber o número de linhas afetadas
			//Todo vendedor que pertencer ao Department 1, o salário será atualizado para 1999:
			int rows1 = st.executeUpdate("UPDATE seller SET BaseSalary = 1999 WHERE DepartmentId = 1");
			
			int quebrar = 1;
			if(quebrar < 2) {
				throw new SQLException("Fake error");
			}
			
			//Todo vendedor que pertencer ao Department 2, o salário será atualizado para 2550:
			int rows2 = st.executeUpdate("UPDATE seller SET BaseSalary = 2550 WHERE DepartmentId = 2");

			conn.commit();//Para confirmar que agora sim minha transação terminou
			
			System.out.println("rows1 " + rows1);
			System.out.println("rows2 " + rows2);
		}catch(SQLException e) {
			//lógica para voltar a transação caso ela se interrompa no meio:
			//não mudará nada no banco caso ocorra!
			try {
				conn.rollback();
				throw new DbException("Transação não concluida! Caused by: " + e.getMessage());
			} catch (SQLException e1) {
				//Caso Rollback quebre:
				throw new DbException("Error ao tentar voltar a transação! Caused by: " + e1.getMessage());
			}
		}
		finally {
			DB.closeStatement(st);
			DB.closeConnection();
		}
	}
}
