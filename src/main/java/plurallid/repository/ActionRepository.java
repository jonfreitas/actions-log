package plurallid.repository;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;
import org.eclipse.microprofile.graphql.Source;
import plurallid.auth.exceptions.RequiredFieldError;
import plurallid.codecs.ChangesCodec;
import plurallid.entity.actions.*;
import plurallid.helpers.Filters;

import java.util.*;

@ApplicationScoped
public class ActionRepository extends Filters implements PanacheMongoRepository<Action> {

    @Inject
    MongoClient mongoClient;

    public MongoCollection<Document> getCollection() {
        return mongoClient.getDatabase("local").getCollection("actions");
    }

    public void setActions(List<Action> actions) {}

    public List<Action> getActions(
        Integer entityId,
        Integer entityValue,
        Integer first,
        Integer page,
        Integer skip,
        String sort,
        String firstPage,
        String previousPage,
        String nextPage,
        String lastPage
    ) {
        PanacheQuery<Action> allActions = findAll().page(Page.ofSize(getPageSize()));
        if (entityId != null ||
            entityValue != null ||
            first != null ||
            page != null ||
            skip != null ||
            sort != null ||
            firstPage != null ||
            previousPage != null ||
            nextPage != null ||
            lastPage != null
        ) {
            Action action = new Action();
            List<Action> filteredActions = initFilters(
                entityId,
                entityValue,
                first,
                page,
                skip,
                sort,
                firstPage,
                previousPage,
                nextPage,
                lastPage,
                allActions
            );
            action.setChanges(filteredActions.get(0).getChanges().stream().toList());
            return filteredActions;
        }
        return allActions.stream().sorted(Comparator.comparing(Action::getEntityValue).reversed()).toList();
    }

    public List<Changes> getChanges(
        @Source Action action,
         String applicationId,
         Integer first,
         Integer skip,
         String sort
    ) {
        if (applicationId != null ||
            first != null ||
            skip != null ||
            sort != null
        ) {
            return initChangesFilters(
                applicationId,
                first,
                skip,
                sort,
                action.getChanges());
        }
        return action.getChanges().stream().sorted(Comparator.comparing(Changes::getWhen).reversed()).toList();
    }

    public void addAction(Action action) {
        this.validateChangesFields(action);
        if (this.entityIdExists(action.getEntityId()) != null && this.entityValueExists(action.getEntityValue()) != null) {
            ChangesCodec changesCodec = new ChangesCodec();
            changesCodec.addNewChanges(action, getCollection());
            return;
        }
        action.persist();
    }

    private Action entityIdExists(Integer entityId){
        return find("entityId", entityId).firstResult();
    }

    private Action entityValueExists(Integer entityValue){
        return find("entityValue", entityValue).firstResult();
    }

    private void validateChangesFields(Action action) {
        if (action.getChanges().get(0).getWho() == null) {
            throw new RequiredFieldError("who");
        }
        if (action.getChanges().get(0).getMessage() == null) {
            throw new RequiredFieldError("message");
        }
        if (action.getChanges().get(0).getWhen() == null) {
            throw new RequiredFieldError("when");
        }
        if (action.getChanges().get(0).getApplicationId() == null) {
            throw new RequiredFieldError("applicationId");
        }
    }

}