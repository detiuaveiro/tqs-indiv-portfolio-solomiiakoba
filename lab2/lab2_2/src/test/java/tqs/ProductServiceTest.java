package tqs;

import org.junit.jupiter.api.Disabled;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)  // For integration of Mockito framework
@MockitoSettings(strictness = Strictness.LENIENT)
public class ProductServiceTest {
    @Mock
    ISimpleHttpClient client; // Mock do serviço externo
    @InjectMocks
    ProductFinderService service;
    @Test
    void testProductFound() {
        String json = """
            {"id":3,"title":"Mens Cotton Jacket","price":55.99}
        """;
        when(client.doHttpGet("https://fakestoreapi.com/products/3")).thenReturn(json);
        Optional<Product> res = service.findProductDetails(3);
        assertThat(res.isPresent()).isTrue();
        assertEquals(3, res.get().getId());
        assertEquals("Mens Cotton Jacket", res.get().getTitle());
    }
    @Test
    void testProductNotFound() {
        when(client.doHttpGet("https://fakestoreapi.com/products/300")).thenReturn("{}");
        Optional<Product> res = service.findProductDetails(300);
        assertThat(res.isPresent()).isFalse();
    }
}
