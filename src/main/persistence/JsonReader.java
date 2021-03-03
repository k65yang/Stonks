package persistence;

import exceptions.InvalidClassTypeException;
import exceptions.IncompatiableStockMarketException;
import model.Investor;
import model.StockMarket;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;
    private String classType;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String type, String source) {
        this.classType = type.toUpperCase();
        this.source = source;
    }

//    // EFFECTS: reads workroom from file and returns it;
//    // throws IOException if an error occurs reading data from file
//    public Investor readInvestor() throws IOException {
//        String jsonData = readFile(source);
//        JSONObject jsonObject = new JSONObject(jsonData);
//        return parseInvestor(jsonObject);
//    }

    public Writable read() throws IOException, IncompatiableStockMarketException, InvalidClassTypeException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);

        if (classType.equals("INVESTOR")) {
            String name = jsonObject.getString("Investor");
            double funds = jsonObject.getDouble("Total funds");
            Investor i = new Investor(name, funds);
            i.loadInvestorFromFile(jsonObject);
            return i;
        } else if (classType.equals("STOCKMARKET")) {
            StockMarket sm = new StockMarket();
            sm.loadFromFile(jsonObject);
            return sm;
        } else {
            throw new InvalidClassTypeException();
        }
    }

//    public StockMarket readStockMarket() throws IOException, IncompatiableStockMarketException {
//        String jsonData = readFile(source);
//        JSONObject jsonObject = new JSONObject(jsonData);
//        return parseStockMarket(jsonObject);
//    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }
//
//    // EFFECTS: parses workroom from JSON object and returns it
//    private Investor parseInvestor(JSONObject jsonObject) {
//        String name = jsonObject.getString("Investor");
//        double funds = jsonObject.getDouble("Total funds");
//        Investor i = new Investor(name, funds);
//        i.setInvestorFromFile(jsonObject);
//        return i;
//    }
//
//    private StockMarket parseStockMarket(JSONObject jsonObject) throws IncompatiableStockMarketException {
//        StockMarket sm = new StockMarket();
//        sm.loadFromFile(jsonObject);
//        return sm;
//    }
}
