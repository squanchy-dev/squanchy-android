package net.squanchy.about.licenses;

import java.util.Arrays;
import java.util.List;

final class Libraries {

    static final List<Library> LIBRARIES = Arrays.asList(
            Library.builder()
                    .name("Squanchy")
                    .author("the Squanchy Contributors")
                    .license(License.APACHE_2)
                    .build(),
            Library.builder()
                    .name("Android")
                    .author("Google Inc. and the Open Handset Alliance")
                    .license(License.APACHE_2)
                    .build(),
            Library.builder()
                    .name("Calligraphy")
                    .author("Christopher Jenkins")
                    .license(License.APACHE_2)
                    .build(),
            Library.builder()
                    .name("Glide")
                    .author("Google Inc.")
                    .license(License.GLIDE)
                    .build(),
            Library.builder()
                    .name("Joda-Time")
                    .author("Joda.org")
                    .license(License.APACHE_2)
                    .build(),
            Library.builder()
                    .name("Joda-Time-Android")
                    .author("Daniel Lew")
                    .license(License.APACHE_2)
                    .build(),
            Library.builder()
                    .name("RxJava")
                    .author("RxJava Contributors")
                    .license(License.APACHE_2)
                    .build(),
            Library.builder()
                    .name("RxAndroid")
                    .author("the RxAndroid authors")
                    .license(License.APACHE_2)
                    .build(),
            Library.builder()
                    .name("Android Support Library")
                    .author("Google Inc. and the Open Handset Alliance")
                    .license(License.APACHE_2)
                    .build(),
            Library.builder()
                    .name("Timber")
                    .author("Jake Wharton")
                    .license(License.APACHE_2)
                    .build(),
            Library.builder()
                    .name("Twitter Kit for Android")
                    .author("Twitter Inc.")
                    .license(License.APACHE_2)
                    .build(),
            Library.builder()
                    .name("ViewPagerAdapter")
                    .author("Novoda Ltd.")
                    .license(License.APACHE_2)
                    .build(),
            Library.builder()
                    .name("AutoValue")
                    .author("Google Inc.")
                    .license(License.APACHE_2)
                    .build(),
            Library.builder()
                    .name("Dagger")
                    .author("the Dagger Authors")
                    .license(License.APACHE_2)
                    .build(),
            Library.builder()
                    .name("Retrolambda")
                    .author("Esko Luontola and other Retrolambda contributors")
                    .license(License.APACHE_2)
                    .build(),
            Library.builder()
                    .name("Gradle Retrolambda Plugin")
                    .author("Evan Tatarka")
                    .license(License.APACHE_2)
                    .build(),
            Library.builder()
                    .name("gradle-build-properties-plugin")
                    .author("Novoda, Ltd.")
                    .license(License.APACHE_2)
                    .build(),
            Library.builder()
                    .name("gradle-play-publisher")
                    .author("Christian Becker and Björn Hurling")
                    .license(License.MIT)
                    .build(),
            Library.builder()
                    .name("Fest Assertions 2.x")
                    .author("the Fest Assertions Authors")
                    .license(License.APACHE_2)
                    .build(),
            Library.builder()
                    .name("JUnit 4")
                    .author("JUnit.org")
                    .license(License.ECLIPSE_PUBLIC_LICENSE)
                    .build(),
            Library.builder()
                    .name("Mockito")
                    .author("the Mockito Contributors")
                    .license(License.MIT)
                    .build()
    );
}
