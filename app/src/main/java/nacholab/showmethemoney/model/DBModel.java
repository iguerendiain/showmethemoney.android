package nacholab.showmethemoney.model;

import com.orm.SugarRecord;

public abstract class DBModel extends SugarRecord{
    private boolean synced = true;
    private long lastSync;

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
