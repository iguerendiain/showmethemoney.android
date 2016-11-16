package nacholab.showmethemoney.api;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import nacholab.showmethemoney.model.Relation;

public class RelationTypeAdapter extends TypeAdapter<Relation>{

    @Override
    public void write(JsonWriter out, Relation value) throws IOException {
        if (value!=null){
            out.beginObject();
            out.name("localId").value(value.getLocalId());
            out.name("remoteId").value(value.getRemoteId());
            out.endObject();
        }
    }

    @Override
    public Relation read(JsonReader in) throws IOException {
        int remoteId = in.nextInt();
        return new Relation(null, remoteId, -1);
    }
}
