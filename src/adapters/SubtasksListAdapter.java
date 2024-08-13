package adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import model.Subtask;

import java.io.IOException;

public class SubtasksListAdapter extends TypeAdapter<Subtask> {

    @Override
    public void write(JsonWriter jsonWriter, Subtask subtask) throws IOException {
        if (subtask == null) {
            jsonWriter.value("");
        } else {
            jsonWriter.value(subtask.toString());
        }
    }

    @Override
    public Subtask read(JsonReader jsonReader) throws IOException {
        return null;
    }
}
