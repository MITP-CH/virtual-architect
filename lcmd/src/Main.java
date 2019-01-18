package net.leanix.pathfinder.samples.simpleCalls;

import net.leanix.api.FactSheetsApi;
import net.leanix.api.GraphqlApi;
import net.leanix.api.common.ApiClient;
import net.leanix.api.common.ApiClientBuilder;
import net.leanix.api.models.FactSheetResponse;
import net.leanix.api.models.GraphQLRequest;
import net.leanix.api.models.GraphQLResult;

import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {

        try {
            ApiClient apiClient = new ApiClientBuilder()
                    .withApiToken("zaU8S2nVrg5NfMrazAU7S8DGNJr6qEzKfSgAsUOW")
                    .withTokenProviderHost("app.leanix.net")
                    .withBasePath("https://app.leanix.net/services/pathfinder/v1")
                    .withDebugging(false)
                    .build();

            FactSheetsApi factSheetsApi = new FactSheetsApi(apiClient);

            FactSheetResponse response = factSheetsApi.getFactSheet("6cc22c5e-24b8-4d90-ba58-ca8991e18a9c", "*", true);
            System.out.println(response.getData().getDisplayName());

            GraphqlApi graphqlApi = new GraphqlApi(apiClient);

            GraphQLRequest graphqlRequest = new GraphQLRequest();
            graphqlRequest.setQuery("{factSheet(id:\"6cc22c5e-24b8-4d90-ba58-ca8991e18a9c\"){displayName}}");
            GraphQLResult graphqlResult = graphqlApi.processGraphQL(graphqlRequest);
            if (graphqlResult.getErrors() != null) {
                System.out.println("GraphQL response includes errors");
            }

            if (graphqlResult.getData() != null) {
                Map<String, Object> data = (Map<String, Object>) graphqlResult.getData();
                Map<String, Object> factSheet = (Map<String, Object>) data.get("factSheet");
                System.out.println(factSheet.get("displayName"));
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
