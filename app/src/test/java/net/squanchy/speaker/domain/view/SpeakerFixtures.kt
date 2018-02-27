package net.squanchy.speaker.domain.view

import net.squanchy.support.lang.Optional

fun aSpeaker(
    id: String = "bananana",
    numericId: Long = 5466L,
    name: String = "Banana Joe",
    bio: String = """Joe ¡oh Banana Joe!
Tu tienes, ¡oh Banana nana Joe!
Corazón gigante y alma soñan,te
¡Oh Banana nana Joe!
Joe ¡oh Banana Joe!
Tu eres, ¡oh Banana na na Joe!
Un gran marinero con puños de acero""",
    photoUrl: Optional<String> = Optional.of("https://i.ytimg.com/vi/-1HB26ko2H8/hqdefault.jpg"),
    companyName: Optional<String> = Optional.of("Amantido"),
    companyUrl: Optional<String> = Optional.of<String>("http://banana.joe"),
    personalUrl: Optional<String> = Optional.of("https://en.wikipedia.org/wiki/Banana_Joe_(film)"),
    twitterUsername: Optional<String> = Optional.of("@bananaJoe1982")
) = Speaker(
    id = id,
    numericId = numericId,
    name = name,
    bio = bio,
    photoUrl = photoUrl,
    companyName = companyName,
    companyUrl = companyUrl,
    personalUrl = personalUrl,
    twitterUsername = twitterUsername
)
