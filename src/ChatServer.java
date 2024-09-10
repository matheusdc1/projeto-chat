import java.io.IOException;
import java.net.ServerSocket;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.io.PrintWriter;

public class ChatServer {

    private static final int PORTA = 1234;

    // Usando CopyOnWriteArraySet para evitar problemas de concorrência
    private static Set<PrintWriter> escritores = new CopyOnWriteArraySet<>();

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(PORTA);
            System.out.println("Servidor rodando na porta " + PORTA + "...");
            while (true) {
                new ChatClient(server.accept(), escritores).start();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}