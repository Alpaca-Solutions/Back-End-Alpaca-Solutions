package conexao_banco_dados;

import cliente.Cliente;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class QueryBanco {

    List<Cliente> clienteList;

    public Integer Busca_Cliente(Cliente cliente){
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();
        Integer resultado =0;

        List<Cliente> resultado_select = con.query("SELECT 'Empresa' AS TipoCliente\n" +
                        "FROM Empresa\n" +
                        "WHERE email_cliente = ? AND senha_cliente = ?\n" +
                        "UNION\n" +
                        "SELECT 'Empregado' AS TipoCliente\n" +
                        "FROM Usuario\n" +
                        "WHERE email = ? AND senha = ?;",
                new Object[]{cliente.getEmail(), cliente.getSenha(), cliente.getEmail(), cliente.getSenha()},
                new BeanPropertyRowMapper<>(Cliente.class));


        if (resultado_select.size() > 0){
            resultado = 200;
        }
        else{
            resultado = 404;
        }

        return resultado;
    }


    public void inserir_Componentes(){

    }
}
