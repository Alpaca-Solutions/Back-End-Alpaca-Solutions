package conexao_banco_dados;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class ConexaoNuvem extends Conexao{
    public ConexaoNuvem(boolean usarMySQL) {
        super();
    }
    private JdbcTemplate conexaoDoBanco;

    public ConexaoNuvem() {

        BasicDataSource dataSource = new BasicDataSource();


            dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");


             dataSource.setUrl("jdbc:sqlserver://107.22.118.178;" +
                "database=AlpacaDb;" +
                "user=sa;" +
                "password=sptech123456789;" +
                "trustServerCertificate=true;");
                dataSource.setUsername("sa");
                dataSource.setPassword("sptech123456789");



            conexaoDoBanco = new JdbcTemplate(dataSource);
        }
    public JdbcTemplate getConexaoDoBanco() {
        return conexaoDoBanco;
    }
}
