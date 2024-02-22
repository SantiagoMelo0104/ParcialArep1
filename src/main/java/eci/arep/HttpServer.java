package eci.arep;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class HttpServer {
    public static void main(String[] args) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, URISyntaxException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(36000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }

        Socket clientSocket = null;
        boolean running = true;
        while (running) {
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Recibí: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }


            String pru = "Consulta?comando=(9,10)";
            URI tm = URI.create(pru);

            System.out.println( " +++++++++ " + tm.getPath());

            if(tm.getPath().contains("Consulta")){
                System.out.println("------------ " + tm);
                String query = tm.getQuery().replace("comando=", "").replace("(", "").replace(")","");
                String[] parametros = query.split(",");

                Double parametro1 = Double.parseDouble(parametros[0]);
                Double parametro2 = Double.parseDouble(parametros[1]);

                Class c= Math.class;
                Method m = c.getMethod("max", double.class, double.class);
                Object res =m.invoke(null, parametro1, parametro2);

                outputLine = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: text/html\r\n"
                        + "\r\n"
                        + "<!DOCTYPE html>\n"
                        + "<html>\n"
                        + "<head>\n"
                        + "<meta charset=\"UTF-8\">\n"
                        + "<title>Calculo</title>\n"
                        + "</head>\n"
                        + "<body>\n"
                        + "<h1>Respuesta: " + res.toString() +"</h1>\n"
                        + "</body>\n"
                        + "</html>\n";


            } else {
                outputLine = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: text/html\r\n"
                        + "\r\n"
                        + "<!DOCTYPE html>\n"
                        + "<html>\n"
                        + "<head>\n"
                        + "<meta charset=\"UTF-8\">\n"
                        + "<title>ERROR</title>\n"
                        + "</head>\n"
                        + "<body>\n"
                        + "<h1>A MOMIR :( </h1>\n"
                        + "</body>\n"
                        + "</html>\n";
            }

            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }


//    public String response(){
//        String outputline =
//        "<!DOCTYPE html>\n"
//        + "<html>\n"
//        +  "<head>\n"
//                <title>Form Example</title>
//                <meta charset="UTF-8">
//                <meta name="viewport" content="width=device-width, initial-scale=1.0">
//            </head>
//            <body>
//                <h1>Form with GET</h1>
//                <form action="/hello">
//                    <label for="name">Name:</label><br>
//                    <input type="text" id="name" name="name" value="John"><br><br>
//                    <input type="button" value="Submit" onclick="loadGetMsg()">
//                </form>
//                <div id="getrespmsg"></div>
//
//                <script>
//                        function loadGetMsg() {
//                    let nameVar = document.getElementById("name").value;
//                        const xhttp = new XMLHttpRequest();
//                    xhttp.onload = function() {
//                        document.getElementById("getrespmsg").innerHTML =
//                                this.responseText;
//                    }
//                    xhttp.open("GET", "/hello?name="+nameVar);
//                    xhttp.send();
//                }
//                </script>
//            </body>
//        </html> "
//    }
}