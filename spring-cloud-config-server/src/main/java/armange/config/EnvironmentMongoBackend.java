package armange.config;

import armange.model.document.ApplicationPropertyDocument;
import armange.model.document.repository.ApplicationPropertyRepository;
import armange.service.ApplicationPropertyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// spotless:off
@Slf4j
@Profile("mongo-backend")
@Component
@RequiredArgsConstructor
public class EnvironmentMongoBackend implements EnvironmentRepository {

    public static final String DEFAULT_LABEL = "master";
    public static final String DEFAULT_APPLICATION_NAME = "application";

    private final ApplicationPropertyRepository applicationPropertyRepository;
    private final ApplicationPropertyService applicationPropertyService;

    @Override
    public Environment findOne(final String application, final String profile, final String label) {
        final List<String> applications = prepareApplicationNames(application);
        final List<String> profiles = prepareProfiles(profile);
        final var nonEmptyLabel = StringUtils.hasText(label) ? label : DEFAULT_LABEL;
        final Environment environment = new Environment(
                application, profiles.toArray(new String[]{}), nonEmptyLabel, null, null);

        for (final String currentProfile : profiles) {
            for (final String currentApp : applications) {
                addPropertySource(environment, currentApp, currentProfile, label);
            }
        }

        for (final String currentApp : applications) {
            addPropertySource(environment, currentApp, null, label);
        }

        return environment;
    }

    private static List<String> prepareApplicationNames(final String application) {
        final String appString;

        if (!application.startsWith(DEFAULT_APPLICATION_NAME)) {
            appString = "application," + application;
        } else {
            appString = application;
        }

        final String[] appsArray = StringUtils.commaDelimitedListToStringArray(appString);
        final List<String> applications = new ArrayList<>(new LinkedHashSet<>(Arrays.asList(appsArray)));

        Collections.reverse(applications);

        return applications;
    }

    private List<String> prepareProfiles(final String profile) {
        final String profilesString = StringUtils.hasText(profile) ? profile : "default," + profile;
        final String[] profilesArray = StringUtils.commaDelimitedListToStringArray(profilesString);
        final List<String> profiles = new ArrayList<>(new LinkedHashSet<>(Arrays.asList(profilesArray)));

        Collections.reverse(profiles);

        return profiles;
    }

    private void addPropertySource(final Environment environment,
                                   final String application,
                                   final String profile,
                                   final String label) {
        try {
            final Map<String, Object> source;
            final String name;

            if (profile != null) {
                source = applicationPropertyService.findApplicationProperties(application, profile, label);
                name = application + "-" + profile;
            } else {
                source = applicationPropertyService.findApplicationProperties(application, null, label);
                name = application;
            }

            if (source != null && !source.isEmpty()) {
                environment.add(new PropertySource(name, source));
            }
        }
        catch (final Exception e) {
            log.error("Failed to retrieve configuration from Mongo Repository", e);
        }
    }
}
// spotless:on