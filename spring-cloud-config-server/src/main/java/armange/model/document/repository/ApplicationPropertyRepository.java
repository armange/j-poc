package armange.model.document.repository;

import armange.model.document.ApplicationPropertyDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationPropertyRepository extends MongoRepository<ApplicationPropertyDocument, String> {

    List<ApplicationPropertyDocument> findByApplicationAndProfileAndLabel(
            String application, String profile, String label);
}
