package dados;

public class Maquina {

    private String Sistema_Operacional;

    public Maquina(String sistema_Operacional) {
        Sistema_Operacional = sistema_Operacional;
    }

    public String getSistema_Operacional() {
        return Sistema_Operacional;
    }

    public void setSistema_Operacional(String sistema_Operacional) {
        Sistema_Operacional = sistema_Operacional;
    }

    @Override
    public String toString() {
        return String.format("Dados do Sistema Operacional\n" +
                "Sistema: %s\n" , Sistema_Operacional);
    }
}
