package nacholab.showmethemoney.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

public abstract class DBModel extends Model/*SugarRecord*/{

    @Column
    private boolean synced = true;

    @Column
    private long lastSync;

    @Column
    private boolean deleted;

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
}
