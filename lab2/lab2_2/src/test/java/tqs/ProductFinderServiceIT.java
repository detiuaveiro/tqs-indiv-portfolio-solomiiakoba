package tqs;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductFinderServiceIT {
    @Test
    void findProductDetails() {
        ISimpleHttpClient client = new TqsBasicHttpClient();
        ProductFinderService service = new ProductFinderService(client);
        Optional<Product> product = service.findProductDetails(3);

        assertThat(product).isPresent();
        assertThat(product.get().getId()).isEqualTo(3);
        assertThat(product.get().getTitle()).containsIgnoringCase("Jacket");
    }

    @Test
    void testFindProductNotFound() {
        ISimpleHttpClient realClient = new TqsBasicHttpClient();
        ProductFinderService service = new ProductFinderService(realClient);

        Optional<Product> product = service.findProductDetails(300);
        assertThat(product).isEmpty();
    }
}
