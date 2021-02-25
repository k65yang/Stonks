package persistence;

import model.Investor;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest {

    @Test
    public void testJsonReaderGeneralInvestor() {
        JsonReader reader = new JsonReader("./data/testWriterGeneralInvestor.json");
        try {
            Investor i = reader.readInvestor();
        } catch (IOException e) {
            fail("Ya dun goofed");
        }
    }
}
