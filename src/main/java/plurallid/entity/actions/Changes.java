package plurallid.entity.actions;

import io.quarkus.mongodb.panache.PanacheMongoEntityBase;

import java.time.LocalDateTime;


public class Changes extends PanacheMongoEntityBase {

    private WhoChange whoChange;
    private String message;
    private LocalDateTime when;
    private String applicationId;

    public WhoChange getWho() {
        return whoChange;
    }

    public void setWho(WhoChange whoChange) {
        this.whoChange = whoChange;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getWhen() {
        return when;
    }

    public void setWhen(LocalDateTime when) {
        this.when = when;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

}
