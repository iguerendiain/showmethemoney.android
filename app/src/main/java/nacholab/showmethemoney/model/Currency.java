package nacholab.showmethemoney.model;

import com.google.gson.annotations.Expose;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.facebook.stetho.common.StringUtil;

import nacholab.showmethemoney.utils.StringUtils;

public class Currency extends DBModel {

    @Expose
    @Column
    private String name;

    @Expose
    @Column
    private float factor;

    @Expose
    @Column
    private String symbol;

    @Expose
    @Column
    private String code;

    public Currency() {
    }

    public String getSymbol() {
        return symbol;
    }

    public String getDisplaySymbol(){
        if (StringUtils.isNotBlank(symbol)) {
            return symbol;
        }else return code;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public void update(Currency currency) {
        name = currency.name;
        factor = currency.factor;
        code = currency.code;
        symbol = currency.symbol;
    }
}
