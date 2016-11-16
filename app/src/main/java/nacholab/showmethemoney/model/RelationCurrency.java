package nacholab.showmethemoney.model;

public class RelationCurrency extends Relation<Currency> {
    public RelationCurrency(Currency object, int remoteId, long localId) {
        super(object, remoteId, localId);
    }
}
