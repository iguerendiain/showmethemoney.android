package nacholab.showmethemoney.model;

public class Relation<O> {

    private O object = null;
    private int remoteId = -1;
    private long localId = -1;

    public Relation(O object, int remoteId, long localId) {
        this.object = object;
        this.remoteId = remoteId;
        this.localId = localId;
    }

    public void setObject(O object) {
        this.object = object;
    }

    public void setRemoteId(int remoteId) {
        this.remoteId = remoteId;
    }

    public void setLocalId(long localId) {
        this.localId = localId;
    }

    public O getObject() {
        return object;
    }

    public long getId(){
        if (remoteId>0){
            return remoteId;
        }else{
            return localId;
        }
    }

    public int getRemoteId() {
        return remoteId;
    }

    public long getLocalId() {
        return localId;
    }

    @Override
    public String toString() {
        String result = "LID: "+localId+" -- RID: "+remoteId+"\nOBJ: ";
        if (object!=null){
            result+=object.toString();
        }else{
            result+="null";
        }

        return result;
    }
}
