package persistence;

import org.json.JSONObject;

// Interface for all objects that can be written to file
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
