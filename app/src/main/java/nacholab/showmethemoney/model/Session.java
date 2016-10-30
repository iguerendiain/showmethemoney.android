package nacholab.showmethemoney.model;

import com.google.gson.annotations.Expose;

public class Session extends BaseModel{

    @Expose
    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
