package nacholab.showmethemoney.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.activeandroid.annotation.Column;

public class Currency extends DBModel {
    @Expose
    @SerializedName("id")
    @Column
    private int DBid;

    @Expose
    @Column
    private String name;

    @Expose
    @Column
    private String slug;

    @Expose
    @Column
    private float factor;

    public Currency() {
    }

    public int getDBid() {
        return DBid;
    }

    public void setDBid(int DBid) {
        this.DBid = DBid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public float getFactor() {
        return factor;
    }

    public void setFactor(float factor) {
        this.factor = factor;
    }

    @Override
    public String toString() {
        String render = name+" ("+slug+") x"+factor;
        if (!isSynced()){
            render+=" NOT SYNCED";
        }
        return render;

    }
}
