package net.squanchy.search.algolia

val algoliaSpeakerResponse = """
{
    "hits": [
    {
        "name": "Tim Messerschmidt",
        "objectID": "955",
        "_highlightResult": {
        "name": {
        "value": "Tim <em>Me</em>sserschmidt",
        "matchLevel": "full",
        "fullyHighlighted": false,
        "matchedWords": [
        "me"
        ]
    }
    },
        "_rankingInfo": {
        "nbTypos": 0,
        "firstMatchedWord": 1,
        "proximityDistance": 0,
        "userScore": 150,
        "geoDistance": 0,
        "geoPrecision": 1,
        "nbExactWords": 0,
        "words": 1,
        "filters": 0
    }
    },
    {
        "name": "Jeremy Meiss",
        "objectID": "622",
        "_highlightResult": {
        "name": {
        "value": "Jeremy <em>Me</em>iss",
        "matchLevel": "full",
        "fullyHighlighted": false,
        "matchedWords": [
        "me"
        ]
    }
    },
        "_rankingInfo": {
        "nbTypos": 0,
        "firstMatchedWord": 1,
        "proximityDistance": 0,
        "userScore": 75,
        "geoDistance": 0,
        "geoPrecision": 1,
        "nbExactWords": 0,
        "words": 1,
        "filters": 0
    }
    }
    ]
}
""".trimIndent()

val MATCHING_SPEAKER_IDS = listOf("955", "622")

val algoliaEventResponse = """
{
  "hits": [
    {
      "description": "Applications grow so fast and with a lot of people working on the same project...",
      "speakers": [
        "Yahya Bayramoglu"
      ],
      "title": "Atomic Design in Android",
      "objectID": "27",
      "_highlightResult": {
        "description": {
          "value": "Applications grow so fast and with a lot of people working on the <em>sa</em>me project, you mostly lose track of UI elements...",
          "matchLevel": "full",
          "fullyHighlighted": false,
          "matchedWords": [
            "sa"
          ]
        },
        "speakers": [
          {
            "value": "Yahya Bayramoglu",
            "matchLevel": "none",
            "matchedWords": []
          }
        ],
        "title": {
          "value": "Atomic Design in Android",
          "matchLevel": "none",
          "matchedWords": []
        }
      },
      "_rankingInfo": {
        "nbTypos": 0,
        "firstMatchedWord": 13,
        "proximityDistance": 0,
        "userScore": 14,
        "geoDistance": 0,
        "geoPrecision": 1,
        "nbExactWords": 0,
        "words": 1,
        "filters": 0
      }
    },
    {
      "description": "Android Instant Apps has been available to developers since Google I/O 2017.<br />A lot has happened in the meantime...",
      "speakers": [
        "Ben Weiss"
      ],
      "title": "Migrating from installed to instant app, a retrospective",
      "objectID": "33",
      "_highlightResult": {
        "description": {
          "value": "Android Instant Apps has been available to developers since Google I/O 2017.<br />A lot has happened in the meantime...",
          "matchLevel": "full",
          "fullyHighlighted": false,
          "matchedWords": [
            "sa"
          ]
        },
        "speakers": [
          {
            "value": "Ben Weiss",
            "matchLevel": "none",
            "matchedWords": []
          }
        ],
        "title": {
          "value": "Migrating from installed to instant app, a retrospective",
          "matchLevel": "none",
          "matchedWords": []
        }
      },
      "_rankingInfo": {
        "nbTypos": 0,
        "firstMatchedWord": 48,
        "proximityDistance": 0,
        "userScore": 21,
        "geoDistance": 0,
        "geoPrecision": 1,
        "nbExactWords": 0,
        "words": 1,
        "filters": 0
      }
    },
    {
      "description": "Developers have multiple opportunities to publicly engage with their users, whether it&rsquo;s through reviews in the ...",
      "speakers": [
        "Modupe Akinnawonu"
      ],
      "title": "Managing User Feedback: Setting Expectations and Translating Feedback into Features   ",
      "objectID": "3",
      "_highlightResult": {
        "description": {
          "value": "Developers have multiple opportunities to publicly engage with their users, whether it&rsquo;s through review...",
          "matchLevel": "full",
          "fullyHighlighted": false,
          "matchedWords": [
            "sa"
          ]
        },
        "speakers": [
          {
            "value": "Modupe Akinnawonu",
            "matchLevel": "none",
            "matchedWords": []
          }
        ],
        "title": {
          "value": "Managing User Feedback: Setting Expectations and Translating Feedback into Features   ",
          "matchLevel": "none",
          "matchedWords": []
        }
      },
      "_rankingInfo": {
        "nbTypos": 0,
        "firstMatchedWord": 49,
        "proximityDistance": 0,
        "userScore": 17,
        "geoDistance": 0,
        "geoPrecision": 1,
        "nbExactWords": 0,
        "words": 1,
        "filters": 0
      }
    },
    {
      "description": "Products may be or become complex for many reasons, and teams are often asked to make them \"simpler and better.\" How do ...",
      "speakers": [
        "David Hogue"
      ],
      "title": "Simplicity is Not Simple",
      "objectID": "25",
      "_highlightResult": {
        "description": {
          "value": "Products may be or become complex for many reasons, and teams are often asked to make them \"simpler and better.\" How do we...",
          "matchLevel": "full",
          "fullyHighlighted": false,
          "matchedWords": [
            "sa"
          ]
        },
        "speakers": [
          {
            "value": "David Hogue",
            "matchLevel": "none",
            "matchedWords": []
          }
        ],
        "title": {
          "value": "Simplicity is Not Simple",
          "matchLevel": "none",
          "matchedWords": []
        }
      },
      "_rankingInfo": {
        "nbTypos": 0,
        "firstMatchedWord": 71,
        "proximityDistance": 0,
        "userScore": 12,
        "geoDistance": 0,
        "geoPrecision": 1,
        "nbExactWords": 0,
        "words": 1,
        "filters": 0
      }
    },
    {
      "description": "An application ID might define your app among all others, but its signature is what proves and confirms its identity and ...",
      "speakers": [
        "Ana Baotić"
      ],
      "title": "Sign here please!",
      "objectID": "39",
      "_highlightResult": {
        "description": {
          "value": "An application ID might define your app among all others, but its signature is what proves and confirms its identity and ...",
          "matchLevel": "full",
          "fullyHighlighted": false,
          "matchedWords": [
            "sa"
          ]
        },
        "speakers": [
          {
            "value": "Ana Baotić",
            "matchLevel": "none",
            "matchedWords": []
          }
        ],
        "title": {
          "value": "Sign here please!",
          "matchLevel": "none",
          "matchedWords": []
        }
      },
      "_rankingInfo": {
        "nbTypos": 0,
        "firstMatchedWord": 89,
        "proximityDistance": 0,
        "userScore": 26,
        "geoDistance": 0,
        "geoPrecision": 1,
        "nbExactWords": 0,
        "words": 1,
        "filters": 0
      }
    },
    {
      "description": "Medical Internet of Things (IoT) solutions are already paving the way towards the realization of Smart Hospitals. Following...",
      "speakers": [
        "Francesca Stradolini"
      ],
      "title": "An Iot Cloud-Based Architecture for Anesthesia Monitoring",
      "objectID": "15",
      "_highlightResult": {
        "description": {
          "value": "Medical Internet of Things (IoT) solutions are already paving the way towards the realization of Smart Hospitals. Following...",
          "matchLevel": "full",
          "fullyHighlighted": false,
          "matchedWords": [
            "sa"
          ]
        },
        "speakers": [
          {
            "value": "Francesca Stradolini",
            "matchLevel": "none",
            "matchedWords": []
          }
        ],
        "title": {
          "value": "An Iot Cloud-Based Architecture for Anesthesia Monitoring",
          "matchLevel": "none",
          "matchedWords": []
        }
      },
      "_rankingInfo": {
        "nbTypos": 0,
        "firstMatchedWord": 121,
        "proximityDistance": 0,
        "userScore": 5,
        "geoDistance": 0,
        "geoPrecision": 1,
        "nbExactWords": 0,
        "words": 1,
        "filters": 0
      }
    }
  ],
  "nbHits": 11,
  "page": 0,
  "nbPages": 2,
  "hitsPerPage": 10,
  "processingTimeMS": 1,
  "exhaustiveNbHits": true,
  "query": "sa",
  "params": "query=sa&hitsPerPage=10&page=0&analytics=false&attributesToRetrieve=*&highlightPreTag=%3Cais-highlight-0000000000%3E&highlightPostTag",
  "index": "squanchy_dev-events",
  "serverUsed": "c5-eu-2.algolia.net",
  "parsedQuery": "sa",
  "timeoutCounts": false,
  "timeoutHits": false
}
""".trimIndent()

val MATCHING_EVENT_IDS = listOf("27", "33", "3", "25", "39", "15")
