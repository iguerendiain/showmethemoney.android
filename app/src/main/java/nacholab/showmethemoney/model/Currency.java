package nacholab.showmethemoney.model;

import com.google.gson.annotations.Expose;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

public class Currency extends DBModel {

    @Expose
    @Column
    private String name;

    @Expose
    @Column
    private float factor;

    public Currency() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getFactor() {
        return factor;
    }

    public void setFactor(float factor) {
        this.factor = factor;
    }

    @Override
    public String toString() {
        String render = name+" // x"+factor;
        if (!isSynced()){
            render+=" NOT SYNCED";
        }
        return render;

    }
}
