package nacholab.showmethemoney.storage;

import com.activeandroid.serializer.TypeSerializer;

import nacholab.showmethemoney.model.Relation;

public class RelationSerializer extends TypeSerializer {

    @Override
    public Class<?> getDeserializedType() {
        return Relation.class;
    }

    @Override
    public Class<?> getSerializedType() {
        return Long.class;
    }

    @Override
    public Long serialize(Object data) {
        return ((Relation)data).getLocalId();
    }

    @Override
    public Relation deserialize(Object data) {
        return new Relation(null, -1, (long)data);
    }
}
