package plurallid.codecs;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Updates;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.Document;
import org.bson.conversions.Bson;
import plurallid.entity.actions.*;
import plurallid.repository.actions.ActionRepository;

import java.time.LocalDateTime;
import java.util.*;

import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Projections.computed;


@ApplicationScoped
public class ChangesCodec {

    private final ActionRepository actionRepository = new ActionRepository();

    public List<Changes> listChanges() {
        List<Changes> list = new ArrayList<>();

        try (MongoCursor<Document> cursor = actionRepository.getCollection().find().iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                Changes changes = new Changes();
                changes.setOldData(document.getString("oldData"));
                changes.setNewData(document.getString("newData"));
                changes.setApplicationId(document.getString("message"));
                changes.setWhen(LocalDateTime.parse(document.getString("when")));
                changes.setMessage(document.getString("applicationId"));
                list.add(changes);
            }
        }
        return list;
    }

    public List<WhoChange> listWho() {
        List<WhoChange> list = new ArrayList<>();

        try (MongoCursor<Document> cursor = actionRepository.getCollection().find().iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                WhoChange who = new WhoChange();
                who.setPersonId(document.getInteger("personId"));
                who.setAdmId(document.getInteger("admId"));
                list.add(who);
            }
        }
        return list;
    }
    
    public void addNewChanges(Action action, MongoCollection<Document> collection) {
        int count = this.getChangesTotalCount(action.getEntityValue(), action.getEntityId(), collection);
        this.recordChangesData(action, collection);
        this.recordWhoData(action, collection, count);
    }

    private void recordChangesData(Action action, MongoCollection<Document> collection) {
        Document documentChanges = new Document()
                .append("oldData", action.getChanges().get(0).getOldData())
                .append("newData", action.getChanges().get(0).getNewData())
                .append("message", action.getChanges().get(0).getMessage())
                .append("when", new Date())
                .append("applicationId", action.getChanges().get(0).getApplicationId());
        collection.updateOne(eq("entityValue", action.getEntityValue()),
                Updates.addToSet("changes", documentChanges));
    }

    private void recordWhoData(Action action, MongoCollection<Document> collection, Integer count) {
        Document documentWho = new Document()
                .append("personId", action.getChanges().get(0).getWho().get(0).getPersonId())
                .append("admId", action.getChanges().get(0).getWho().get(0).getAdmId());
        collection.updateMany(eq("entityValue", action.getEntityValue()),
                Updates.addToSet("changes." + count + ".who", documentWho));
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
