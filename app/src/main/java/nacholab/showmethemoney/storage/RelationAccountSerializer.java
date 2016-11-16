package nacholab.showmethemoney.storage;

import com.activeandroid.serializer.TypeSerializer;

import nacholab.showmethemoney.model.Relation;
import nacholab.showmethemoney.model.RelationAccount;

public class RelationAccountSerializer extends TypeSerializer {

    @Override
    public Class<?> getDeserializedType() {
        return RelationAccount.class;
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
        return new RelationAccount(null, -1, (long)data);
    }
}
