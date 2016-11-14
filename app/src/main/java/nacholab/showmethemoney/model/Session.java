package nacholab.showmethemoney.model;

import com.google.gson.annotations.Expose;

public class Session extends BaseModel{

    @Expose
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
