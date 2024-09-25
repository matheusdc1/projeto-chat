import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Set;

public class ChatClient extends Thread {

    private Socket socket;
    private PrintWriter escritor;
    private Set<PrintWriter> listaDeEscritores;

    ChatClient(Socket socket, Set<PrintWriter> escritores) {
        this.socket = socket;
        listaDeEscritores = escritores;
    }

    @Override
    public void run() {

        BufferedReader leitor = null;

        try {
            leitor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            escritor = new PrintWriter(socket.getOutputStream(), true);

            listaDeEscritores.add(escritor);

            String usuario = leitor.readLine();
            transmitir(usuario + " entrou no chat!");
            System.out.println(usuario + " conectou-se ao servidor!");

            String mensagem;
            while ((mensagem = leitor.readLine()) != null) {

                if (mensagem.equals("sair")) {
                    break;
                }

                transmitir(usuario + ": " + mensagem);
            }

            listaDeEscritores.remove(escritor);
            transmitir(usuario + " saiu do chat!");
            System.out.println(usuario + " desconectou-se!");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if (leitor != null)
                    leitor.close();
                if (escritor != null)
                    escritor.close();
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void transmitir(String mensagem) {
        listaDeEscritores.forEach(escritor -> {
            escritor.println(mensagem);
        });
    }
}
