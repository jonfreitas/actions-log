package plurallid.entity.actions;

import io.quarkus.mongodb.panache.PanacheMongoEntityBase;

import java.time.LocalDateTime;
import java.util.List;


public class Changes extends PanacheMongoEntityBase {

    private String oldData;
    private String newData;
    private List<WhoChange> whoChange;
    private String message;
    private LocalDateTime when;
    private String applicationId;

    public String getOldData() {
        return oldData;
    }

    public void setOldData(String oldData) {
        this.oldData = oldData;
    }

    public String getNewData() {
        return newData;
    }

    public void setNewData(String newData) {
        this.newData = newData;
    }

    public List<WhoChange> getWho() {
        return whoChange;
    }

    public void setWho(List<WhoChange> whoChange) {
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
