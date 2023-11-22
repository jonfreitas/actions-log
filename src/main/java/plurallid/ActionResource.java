package plurallid;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.graphql.*;

import io.quarkus.vertx.http.runtime.CurrentVertxRequest;
import plurallid.auth.services.AuthorizationService;
import plurallid.entity.actions.Changes;
import plurallid.entity.actions.Action;
import plurallid.helpers.Paging;
import plurallid.repository.ActionRepository;

import java.util.*;


@GraphQLApi
@ApplicationScoped
public class ActionResource extends Paging implements AuthorizationService {

    @Inject
    ActionRepository actionRepository;

    @Inject
    CurrentVertxRequest request;

    public ActionResource(CurrentVertxRequest request) {
        hasToken(request);
    }

    @Query("actions")
    @Description("Get actions")
    public List<Action> getActions(
        @NonNull Integer entityId,
        @NonNull Integer entityValue,
        Integer first,
        Integer page,
        Integer skip,
        String sort,
        String firstPage,
        String previousPage,
        String nextPage,
        String lastPage
    ) throws Exception {
        isAuthorized(request);
        return actionRepository.getActions(entityId, entityValue, first, page, skip, sort, firstPage, previousPage, nextPage, lastPage);
    }

    public List<Changes> getChanges(
        @Source Action action,
         String applicationId,
         Integer first,
         Integer skip,
         String sort
    ) throws Exception {
        isAuthorized(request);
        return actionRepository.getChanges(action, applicationId, first, skip, sort);
    }

    @Mutation
    @Description("Create a new action")
    public Action createAction(Action action) throws Exception {
        isAuthorized(request);
        actionRepository.addAction(action);
        return action;
    }

}
