/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EjerciciosAPI {

    public static String getRaw() throws Exception {
        URL url = new URL("https://wger.de/api/v2/exerciseinfo/?limit=10&language=2");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(8000);
        con.setReadTimeout(8000);

        int status = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                status >= 200 && status < 300 ? con.getInputStream() : con.getErrorStream(), "UTF-8"));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        return content.toString();
    }

    public static List<String> extraerNombres(String rawJson) {
        List<String> nombres = new ArrayList<>();
        Pattern p = Pattern.compile("\"name\"\\s*:\\s*\"(.*?)\"");
        Matcher m = p.matcher(rawJson);
        while (m.find()) {
            String nombre = m.group(1);
            if (!nombres.contains(nombre)) nombres.add(nombre);
        }
        return nombres;
    }
}
