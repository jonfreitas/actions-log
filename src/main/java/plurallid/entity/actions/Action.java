package plurallid.entity.actions;

import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.eclipse.microprofile.graphql.NonNull;

import java.util.*;

@MongoEntity(collection = "actions")
public class Action extends PanacheMongoEntityBase {

    @NonNull private Integer entityId;
    @NonNull private String entityName;
    @NonNull private Integer entityValue;
    private List<Changes> changes = new ArrayList<>();

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Integer getEntityValue() {
        return entityValue;
    }

    public void setEntityValue(Integer entityValue) {
        this.entityValue = entityValue;
    }

    public List<Changes> getChanges() {
        return changes;
    }

    public void setChanges(List<Changes> changes) {
        this.changes = changes;
    }

}