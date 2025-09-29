package tqs;

import java.util.Optional;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ProductFinderService {
    private static final String API_PRODUCTS = "API_PRODUCTS"; // Defina a URL real aqui
    private final ISimpleHttpClient httpClient;

    public ProductFinderService(ISimpleHttpClient httpClient) {
        this.httpClient = httpClient;
    }
    public Optional<Product> findProductDetails(Integer id) {
        try {
            String url = "https://fakestoreapi.com/products/" + id;
            String json = httpClient.doHttpGet(url);
            if (json == null || json.trim().equals("{}")) {
                return Optional.empty();
            }
            System.out.println("JSON recebido: " + json);
            ObjectMapper mapper = new ObjectMapper();
            Product product = mapper.readValue(json, Product.class);
            return Optional.ofNullable(product);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
