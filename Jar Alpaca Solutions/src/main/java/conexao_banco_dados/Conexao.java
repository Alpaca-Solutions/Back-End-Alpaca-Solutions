package conexao_banco_dados;


import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class Conexao {
    private JdbcTemplate conexaoDoBanco;

    public Conexao(boolean usarMySQL) {
        BasicDataSource dataSource = new BasicDataSource();

        if (usarMySQL) {
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUrl("jdbc:mysql://localhost:3306/AlpacaDB");
            dataSource.setUsername("aluno");
            dataSource.setPassword("aluno");
        } else {
            dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            dataSource.setUrl("jdbc:sqlserver://localhost:1433;databaseName=AlpacaDB");
            dataSource.setUsername("seuUsuario");
            dataSource.setPassword("suaSenha");
        }

        conexaoDoBanco = new JdbcTemplate(dataSource);
    }

    public JdbcTemplate getConexaoDoBanco() {
        return conexaoDoBanco;
    }
}
