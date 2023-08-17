package plurallid.codecs;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Updates;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.Document;
import org.bson.conversions.Bson;
import plurallid.entity.actions.*;
import java.util.*;

import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.*;


@ApplicationScoped
public class ChangesCodec {

    public void addNewChanges(Action action, MongoCollection<Document> collection) {
        int count = this.getChangesTotalCount(action.getEntityValue(), action.getEntityId(), collection);
        this.recordChangesData(action, collection);
        this.recordWhoData(action, collection, count);
    }

    private void recordChangesData(Action action, MongoCollection<Document> collection) {
        Document documentChanges = new Document()
                .append("applicationId", action.getChanges().get(0).getApplicationId())
                .append("message", action.getChanges().get(0).getMessage())
                .append("when", new Date());
        collection.updateOne(eq("entityValue", action.getEntityValue()),
                Updates.addToSet("changes", documentChanges));
    }

    private void recordWhoData(Action action, MongoCollection<Document> collection, Integer count) {
        Document documentWho = new Document()
                .append("personId", action.getChanges().get(0).getWho().getPersonId())
                .append("admId", action.getChanges().get(0).getWho().getAdmId());
        collection.updateMany(eq("entityValue", action.getEntityValue()),  
            Updates.set("changes." + count + ".who", documentWho));
    }

    public int getChangesTotalCount(Integer entityValue, Integer entityId, MongoCollection<Document> collection) {
        Bson countChanges = project(fields(excludeId(),
                computed("entityValue", entityValue),
                computed("entityId", entityId),
                computed("count", new Document("$size", "$changes"))
        ));

        Iterable<Document> results = collection.aggregate(Arrays.asList(countChanges));
        HashMap<String, Integer> countSummary = new HashMap<>();
        MongoCursor<Document> iterator = (MongoCursor<Document>) results.iterator();
        while (iterator.hasNext()) {
            Document next = iterator.next();
            countSummary.put("count", (Integer) next.get("count"));
        }
        return countSummary.get("count");
    }

}
