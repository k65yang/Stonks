package model;

import java.util.*;
import java.io.*;

public class StockMarketDatabase {

    HashMap<String, Double> hm;

    public StockMarketDatabase() {
        hm = new HashMap<>();

        hm.put("D1", 100.00);
        hm.put("D2", 100.00);
        hm.put("D3", 100.00);

        writeToFile(hm);
    }

    @SuppressWarnings({"checkstyle:WhitespaceAround", "checkstyle:EmptyCatchBlock"})
    public void writeToFile(HashMap<String, Double> map) {
        //write to file : "fileone"
        try {
            File fileOne = new File("fileone");
            FileOutputStream fos = new FileOutputStream(fileOne);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(map);
            oos.flush();
            oos.close();
            fos.close();
        } catch (Exception e) { }
    }
}
