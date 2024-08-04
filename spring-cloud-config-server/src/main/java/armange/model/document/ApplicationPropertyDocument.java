package armange.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document
@NoArgsConstructor
@AllArgsConstructor
@CompoundIndex(name = "IX_configuration_fields", def = "{'application':1, 'profile':1, 'label':1, 'key':1}", unique = true)
public class ApplicationPropertyDocument {

    private String id;
    private String application;
    private String profile;
    private String label;
    private String key;
    private String value;
}
