package persistence;

import exceptions.InvalidClassTypeException;
import exceptions.IncompatibleStockMarketException;
import model.Investor;
import model.StockMarket;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads workroom from JSON data stored in file
// Code template for JsonReader from
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonReader {
    private String source;
    private String classType;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String type, String source) {
        this.classType = type.toUpperCase();
        this.source = source;
    }

    // EFFECTS: reads the classType from the JSON file and returns it;
    //          throws IOException if an error occurs reading data from file
    //          throws IncompatibleStockMarketException if the stock market JSON does not
    //                 match the systems predefined stock market class
    //          throws InvalidClassTypeException if the specified class type is not valid
    public Writable read() throws IOException,
            IncompatibleStockMarketException, InvalidClassTypeException {
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

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }
}
