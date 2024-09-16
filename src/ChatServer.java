import java.io.IOException;
import java.net.ServerSocket;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.io.PrintWriter;

public class ChatServer {

    private static final int PORTA = 1234;
    private static ChatServer instancia;
    private static Set<PrintWriter> escritores = new CopyOnWriteArraySet<>();

    // Padrão Singleton para garantir que só exista um ChatServer
    private ChatServer() {
    }

    public static synchronized ChatServer getInstancia() {
        if (instancia == null) {
            instancia = new ChatServer();
        }
        return instancia;
    }

    public static void main(String[] args) {
        ChatServer server = ChatServer.getInstancia();
        server.iniciar();
    }

    private void iniciar() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORTA);
            System.out.println("Servidor rodando na porta " + PORTA + "...");
            while (true) {
                new ChatClient(serverSocket.accept(), escritores).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
