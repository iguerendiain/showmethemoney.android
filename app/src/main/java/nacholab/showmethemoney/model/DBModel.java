package nacholab.showmethemoney.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

public abstract class DBModel extends Model{

    @Column
    private boolean synced = true;

    @Column
    private long lastSync;

    @Column
    private boolean deleted;

    @Column
    @Expose
    @SerializedName("id")
    private int remoteId;

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public long getLastSync() {
        return lastSync;
    }

    public void setLastSync(long lastSync) {
        this.lastSync = lastSync;
    }

    public int getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(int remoteId) {
        this.remoteId = remoteId;
    }
}
