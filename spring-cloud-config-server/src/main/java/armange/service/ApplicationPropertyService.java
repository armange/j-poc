package armange.service;

import armange.model.document.ApplicationPropertyDocument;
import armange.model.document.repository.ApplicationPropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationPropertyService {

    private final ApplicationPropertyRepository applicationPropertyRepository;

    public Map<String, Object> findApplicationProperties(final String application,
                                                         final String profile,
                                                         final String label) {
        return applicationPropertyRepository
                .findByApplicationAndProfileAndLabel(application, profile, label)
                .stream()
                .collect(Collectors.toMap(ApplicationPropertyDocument::getKey, ApplicationPropertyDocument::getValue));
    }

}
