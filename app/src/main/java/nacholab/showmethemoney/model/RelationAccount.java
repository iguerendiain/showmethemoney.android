package nacholab.showmethemoney.model;

public class RelationAccount extends Relation<MoneyAccount> {
    public RelationAccount(MoneyAccount object, int remoteId, long localId) {
        super(object, remoteId, localId);
    }
}
