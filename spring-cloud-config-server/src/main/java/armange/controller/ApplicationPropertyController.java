package armange.controller;

import armange.config.EnvironmentMongoBackend;
import armange.model.document.ApplicationPropertyDocument;
import armange.model.document.repository.ApplicationPropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Function;

import static armange.config.EnvironmentMongoBackend.DEFAULT_LABEL;

@RestController
@RequestMapping("/{app}/{profile}")
@RequiredArgsConstructor
public class ApplicationPropertyController {

    private final ApplicationPropertyRepository applicationPropertyRepository;
    private final EnvironmentMongoBackend environmentMongoBackend;

    @PutMapping("/{label}")
    public ResponseEntity<Environment> save(@PathVariable final String app,
                                            @PathVariable final String profile,
                                            @PathVariable final String label,
                                            @RequestParam("key") final String key,
                                            @RequestParam("value") final String value) {
        final List<ApplicationPropertyDocument> applicationProperty = applicationPropertyRepository
                .findByApplicationAndProfileAndLabel(app, profile, label);
        final HttpStatus httpStatus;

        if (applicationProperty == null) {
            applicationPropertyRepository.save(ApplicationPropertyDocument
                    .builder()
                    .application(app)
                    .profile(profile)
                    .label(label)
                    .key(key)
                    .value(value)
                    .build());

            httpStatus = HttpStatus.CREATED;
        } else {
            applicationPropertyRepository.save(
                    findAndUpdateOrCreatesNew(app, profile, label, key, value, applicationProperty));

            httpStatus = HttpStatus.OK;
        }

        return ResponseEntity.status(httpStatus).body(environmentMongoBackend.findOne(app, profile, label));
    }

    @PutMapping
    public ResponseEntity<Environment> save(@PathVariable final String app,
                                            @PathVariable final String profile,
                                            @RequestParam("key") final String key,
                                            @RequestParam("value") final String value) {
        final List<ApplicationPropertyDocument> applicationProperty = applicationPropertyRepository
                .findByApplicationAndProfileAndLabel(app, profile, DEFAULT_LABEL);
        final HttpStatus httpStatus;

        if (applicationProperty == null) {
            applicationPropertyRepository.save(ApplicationPropertyDocument
                    .builder()
                    .application(app)
                    .profile(profile)
                    .label(DEFAULT_LABEL)
                    .key(key)
                    .value(value)
                    .build());

            httpStatus = HttpStatus.CREATED;
        } else {
            applicationPropertyRepository.save(
                    findAndUpdateOrCreatesNew(app, profile, DEFAULT_LABEL, key, value, applicationProperty));

            httpStatus = HttpStatus.OK;
        }

        return ResponseEntity.status(httpStatus).body(environmentMongoBackend.findOne(app, profile, DEFAULT_LABEL));
    }

    private ApplicationPropertyDocument findAndUpdateOrCreatesNew(
            final String app,
            final String profile,
            final String label,
            final String key,
            final String value,
            final List<ApplicationPropertyDocument> applicationProperty) {
        return applicationProperty
                .stream()
                .filter(prop -> prop.getKey().equalsIgnoreCase(key))
                .map(setValue(value))
                .findFirst()
                .orElseGet(() -> ApplicationPropertyDocument
                        .builder()
                        .application(app)
                        .profile(profile)
                        .label(label)
                        .key(key)
                        .value(value)
                        .build());
    }

    private Function<ApplicationPropertyDocument, ApplicationPropertyDocument> setValue(final String value) {
        return prop -> {
            prop.setValue(value);

            return prop;
        };
    }
}
