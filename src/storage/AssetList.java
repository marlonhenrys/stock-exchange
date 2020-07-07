package storage;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.Vector;

public class AssetList {

    @SuppressWarnings("deprecation")
    public static Vector<String> load() {

        Vector<String> assetsCodes = new Vector<String>();

        try {
            FileInputStream file = new FileInputStream("acoes_bovespa.csv");
            DataInputStream data = new DataInputStream(file);

            data.readLine();
            while (data.available() != 0) {
                String line = data.readLine();
                String code = line.split(";")[0];

                assetsCodes.add(code);
            }
            data.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return assetsCodes;
    }
}
