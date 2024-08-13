package adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import model.Epic;
import service.TaskManager;

import java.io.IOException;

public class EpicAdapter extends TypeAdapter<Epic> {

    private final TaskManager tm;

    public EpicAdapter(TaskManager tm) {
        this.tm = tm;
    }

    @Override
    public void write(JsonWriter jsonWriter, Epic epic) throws IOException {
        jsonWriter.value(epic.getId());
    }

    @Override
    public Epic read(JsonReader jsonReader) throws IOException {
        return tm.getEpicById(Integer.parseInt(jsonReader.nextString()));
    }
}
