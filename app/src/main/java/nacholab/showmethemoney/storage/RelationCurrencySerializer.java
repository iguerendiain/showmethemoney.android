package nacholab.showmethemoney.storage;

import nacholab.showmethemoney.model.Relation;
import nacholab.showmethemoney.model.RelationCurrency;

public class RelationCurrencySerializer extends RelationAccountSerializer {

    @Override
    public Class<?> getDeserializedType() {
        return RelationCurrency.class;
    }

    @Override
    public Class<?> getSerializedType() {
        return Long.class;
    }

    @Override
    public Long serialize(Object data) {
        return ((RelationCurrency)data).getLocalId();
    }

    @Override
    public Relation deserialize(Object data) {
        return new RelationCurrency(null, -1, (long)data);
    }

}
