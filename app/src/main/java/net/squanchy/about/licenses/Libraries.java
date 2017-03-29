package net.squanchy.about.licenses;

import java.util.Arrays;
import java.util.List;

final class Libraries {

    static final List<Library> LIBRARIES = Arrays.asList(
            Library.builder()
                    .name("Android")
                    .author("Google Inc. and the Open Handset Alliance")
                    .license(License.APACHE_2)
                    .build()
    );
}
