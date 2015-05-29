package com.ls.drupalconapp.app;

import com.ls.drupalconapp.R;
import com.ls.drupalconapp.model.vo.AbstractEvent;
import com.ls.drupalconapp.model.vo.BoFsEvent;
import com.ls.drupalconapp.model.vo.BreakEvent;
import com.ls.drupalconapp.model.vo.ProgramEvent;
import com.ls.drupalconapp.model.vo.Speaker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestData {

    public static final String[] TRACKS = new String[] {
            "Coding and Development",
            "Core Conversations: Achieving sustainability",
            "DevOps: Breaking down the silos",
            "Drupal Business: Drupal's Golden Age",
            "Frontend: Futuristic tools and techniques",
            "Site Building: Building sites fast without needing to code!",
            "Mini tracks: Case Studies and PHP",
            "Business Showcase"
    };

    public static final String[] EXP_LEVELS = new String[] {
            "Advanced",
            "Beginner",
            "Intermediate"
    };

    private TestData() {
    }

    public static List<Speaker> generateSpeakers() {
        String[] names = new String[] {"Jenn Sramek", "Frank Baele", "Alexander Ward", "Jozef Toth",
                                       "Boris Baldinger", "Vesa Palmu", "Roel De Meester", "Daniel Sipos",
                                        "Jo Wouters", "Helena Nordenfelt", "Markus Broman", "Malin Gernandt"};
        String[] organizations = new String[] {"Acquia,  Inc", "XIO", "Acquia", "Mogdesign",
                                        "Amazee Labs", "Wunderkraut", "Wunderkraut", "Wunderkraut", "Wunderkraut",
                "Wunderkraut", "Wunderkraut", "Wunderkraut", "Wunderkraut"};
        String[] jobTitles = new String[] {"Sr. Engagement Manager", "Frontend engineer", "Migratin Consultant",
                                        "CEO", "Developer/Sitebuilder", "CEO", "CTO Benelux", "Support Engineer",
                                        "Business Administrator", "Business Consultant", "Software Developer",
                                        "Software Developer"};
        int[] photos = new int[] {R.drawable.jenn_sramek, R.drawable.fran_baele, R.drawable.alexander_ward,
                                  R.drawable.jozef_toth, R.drawable.boris_baldinger, R.drawable.vesa_palmu,
                                  R.drawable.roel_de_meester, R.drawable.daniel_sipos, R.drawable.jo_wouters,
                                  R.drawable.helena_nordenfelt, R.drawable.markus_broman, R.drawable.malin_gernandt};

        List<Speaker> speakers = new ArrayList<Speaker>();

        for(int i = 0; i < names.length; i++) {
            Speaker speaker = new Speaker();

            speaker.setId(i);
            speaker.setName(names[i]);
            speaker.setDescription("Description " + i);
            speaker.setArticle("Article " + i);
            speaker.setDate("29 September 14:05-16:00");
            speaker.setTrack("Coding and Development " + i);
            speaker.setExpLevel("Intermediate " + i);
            speaker.setOrganization(organizations[i]);
            speaker.setJobTitle(jobTitles[i]);
            speaker.setPhotoId(photos[i]);

            speakers.add(speaker);
        }

        return speakers;
    }

    public static List<AbstractEvent> generateEvents() {
        String[] tracks = new String[] {
                "Frontend",
                "Business Showcase [Sponsors only]",
                "Site Building",
                "",
                "Drupal.org",
                "",
                "Core Conversations",
                "Core Conversations",
                "Frontend",
                "",
                "DevOps",
                "Site Building",
                "DevOps"
        };
        String[] expLevels = new String[] {
                "Intermediate",
                "Beginner",
                "Advanced",
                "",
                "Intermediate",
                "",
                "Advanced",
                "Intermediate",
                "Intermediate",
                "",
                "Intermediate",
                "Intermediate",
                "Intermediate"
        };

        String[] rooms = new String[] {
                "Holland Complex | 8 Hall",
                "Holland Complex | 9 Hall",
                "Holland Complex | 10 Hall",
                "",
                "Holland Complex | 11 Hall",
                "",
                "Europe Complex | 7 Hall",
                "Europe Complex | 6 Hall",
                "Europe Complex | 1 Hall",
                "",
                "Europe Complex | 2 Hall",
                "Holland Complex | 10 Hall",
                "Holland Complex | 9 Hall"
        };

        String[] reportsName = new String[] {
                "Axure Prototyping for Drupal",
                "12 Best Practices from Wunderkraut",
                "Content Staging in Drupal 8, continued!",
                "",
                "Modernizing Testbot: The Future of Drupal.org Automated Testing",
                "",
                "Q&A with Dries",
                "The Future of Drupal Functional Testing",
                "The Future of HTML and CSS",
                "",
                "Turning Drupal Into a Machine for Automated Deployment",
                "Drupal 8 Breakpoints and Responsive images",
                "Using Open Source Logging and Monitoring Tools"
        };
        String[] names = new String[] {
                "Jenn Sramek",
                "Frank Baele",
                "Alexander Ward",
                "",
                "Jozef Toth",
                "",
                "Roel De Meester",
                "Daniel Sipos",
                "Jo Wouters",
                "",
                "Helena Nordenfelt",
                "Vesa Palmu",
                "Markus Broman"
        };

        boolean[] isBreak = new boolean[] {
                false,
                false,
                false,
                true,
                false,
                true,
                false,
                false,
                false,
                true,
                false,
                false,
                false
        };

        boolean[] isKeynote = new boolean[]{
                false,
                false,
                false,
                false,
                true,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false
        };

        boolean[] isLanchBreak = new boolean[] {
                false,
                true,
                true
        };

        String[] beginTimes = new String[] { "9:00", "10:55", "11:55", "13:00", "14:35", "15:35", "16:35"};
        String[] endTimes = new String[] { "10:55", "11:55", "12:55", "14:30", "15:30", "16:30", "17:55"};

        int timeCounter = 0;
        int breakEventTimeCounter = 0;
        List<AbstractEvent> result = new ArrayList<AbstractEvent>();
        Random random = new Random();

        for(int i = 0; i < names.length; i++) {
            if(timeCounter >= beginTimes.length) {
                break;
            }

            if(isBreak[i]) {
                timeCounter++;
                result.add(generateBreakEvent(i, beginTimes[timeCounter], endTimes[timeCounter], isLanchBreak[breakEventTimeCounter]));
                breakEventTimeCounter++;
            } else {
                result.add(generateProgramEvent(i, names[i], reportsName[i], tracks[i], expLevels[i],
                        "", beginTimes[timeCounter], endTimes[timeCounter], rooms[i], random.nextBoolean(), isKeynote[i]));
                timeCounter--;
            }

            timeCounter++;
        }

        return result;
    }

    public static List<BoFsEvent> generateBoFsEvents() {
        //2 3 4 2
        int[] eventsNumber = new int[] { 2, 3, 4, 2 };

        String[] eventsName = new String[] {
                "QA - Testing Procedures in Drupal",
                "Conference Organizing Distribution",
                "Homeowner Associations Love Drupal",
                "Mentor Orientation",
                "Automated Frontend Regression Testing",
                "Marketing HTML5 Outside the app stores",
                "Point North: Modern Standards & Best Practices",
                "Triage Novice Issues with Mentors",
                "Doing Open Data with Drupal: Nudata and the Dkan Distro",
                "Designing Drupal Training",
                "Drupal in Libraries"
        };

        String[] rooms = new String[] {
                "Holland Complex | 8 Hall",
                "Holland Complex | 9 Hall",
                "Holland Complex | 10 Hall",
                "Holland Complex | 11 Hall",
                "Europe Complex | 7 Hall",
                "Europe Complex | 6 Hall",
                "Europe Complex | 1 Hall",
                "Europe Complex | 2 Hall",
                "Holland Complex | 10 Hall",
                "Holland Complex | 9 Hall",
                "Europe Complex | 6 Hall"
        };

        String[] beginTimes = new String[] { "9:00", "11:00", "12:30", "14:00" };
        String[] endTimes = new String[] { "10:55", "12:25", "13:55", "15:25" };

        List<BoFsEvent> result = new ArrayList<BoFsEvent>();

        Random random = new Random();
        int counter = 0;
        for(int i = 0; i < eventsName.length; i++) {
            for(int j = 0; j < eventsNumber[counter]; j++) {
                BoFsEvent event = new BoFsEvent();

                event.setBeginTime(beginTimes[counter]);
                event.setEndTime(endTimes[counter]);
                event.setDate("");
                event.setAddToSchedule(random.nextBoolean());
                event.setId(i);
                event.setName(eventsName[i]);
                event.setRoom(rooms[i]);

                result.add(event);
                if(j != eventsNumber[counter] - 1) {
                    i++;
                }
            }
            counter++;
        }

        return result;
    }

    public static List<AbstractEvent> generateMyScheduleEvent() {
        String[] tracks = new String[] {
                "Frontend",
                "Business Showcase [Sponsors only]",
                "Site Building",
                "Drupal.org",
                "Coding and Development"
        };
        String[] expLevels = new String[] {
                "Intermediate",
                "Beginner",
                "Advanced",
                "Intermediate",
                "Intermediate"
        };

        String[] reportsName = new String[] {
                "Axure Prototyping for Drupal",
                "12 Best Practices from Wunderkraut",
                "Content Staging in Drupal 8, continued!",
                "Modernizing Testbot: The Future of Drupal.org Automated Testing",
                "OOP For Drupal Developers"};

        String[] speakersName = new String[] {
                "Jenn Sramek",
                "Frank Baele",
                "Alexander Ward",
                "Jozef Toth",
                "Boris Baldinger"
        };

        String[] rooms = new String[] {
                "Holland Complex | 8 Hall",
                "Holland Complex | 9 Hall",
                "Holland Complex | 10 Hall",
                "Holland Complex | 11 Hall",
                "Holland Complex | 8 Hall",
        };

        String[] boFsNames = new String[] {
                "QA - Testing Procedures in Drupal",
                "Conference Organizing Distribution",
                "Homeowner Associations Love Drupal",
                "Mentor Orientation",
                "Automated Frontend Regression Testing",
        };

        String[] dates = new String[] { "29/09", "30/09", "01/10", "02/10", "03/10"};
        String[] beginTimes = new String[] { "9:00",  "9:00", "10:55", "11:55", "13:00"};
        String[] endTimes = new String[] { "10:55", "10:55", "11:55", "12:55", "14:30"};
        boolean[] isProgramEvent = new boolean[] { true, true, false, true, false };

        List<AbstractEvent> result = new ArrayList<AbstractEvent>();

        for(int i = 0; i < dates.length; i++) {
            int boFsEventsCounter = 0;
            for(int j = 0; j < beginTimes.length; j++) {
                if(isProgramEvent[j]) {
                    result.add(generateProgramEvent(i, speakersName[j], reportsName[j], tracks[j], expLevels[j],
                            dates[i], beginTimes[j], endTimes[j], rooms[i], true, false));
                } else {
                    result.add(generateBoFsEvent(i, boFsNames[boFsEventsCounter], dates[i], beginTimes[j], endTimes[j], rooms[i], true));
                    boFsEventsCounter++;
                }
            }
        }

        return result;
    }

    public static BoFsEvent getBoFsEventForDisplay() {
        BoFsEvent event = new BoFsEvent();

        event.setName("Static SITE Generators and Wireframes");
        event.setRoom("Holland Complex | 11 Hall");
        event.setAddToSchedule(false);
        event.setBeginTime("9:00");
        event.setEndTime("10:55");
        event.setDescription("A website wireframe, also known as a page schematic or screen blueprint, is a visual guide that represents the skeletal framework of a website.\n\nWireframes are created for the purpose of arranging elements to best accomplish a particular purpose. The purpose is usually being informed by a business objective and a creative idea. The wireframe depicts the page layout or arrangement of the website’s content, including interface elements and navigational systems, and how they work together.\n\nThe wireframe usually lacks typographic style, color, or graphics, since the main focus lies in functionality, behavior, and priority of content.\n\nIn other words, it focuses on what a screen does, not what it looks like.\n\nWireframes can be pencil drawings or sketches on a whiteboard, or they can be produced by means of a broad array of free or commercial software applications. Wireframes are generally created by business analysts, user experience designers, developers, visual designers and other roles with expertise in interaction design, information architecture and user research.\n" +
                "\n" +
                "Wireframes focus on:\n" +
                "\n" +
                "- The kinds of information displayed\n" +
                "- The range of functions available\n" +
                "- The relative priorities of the information and functions\n" +
                "- The rules for displaying certain kinds of information\n" +
                "- The effect of different scenarios on the display\n" +
                "- The website wireframe connects the underlying conceptual structure, or information architecture, to the surface, or visual design of the website.\n\nWireframes help establish functionality, and the relationships between different screen templates of a website. An iterative process, creating wireframes is an effective way to make rapid prototypes of pages, while measuring the practicality of a design concept. Wireframing typically begins between “high-level structural work—like flowcharts or site maps—and screen designs.”\n\nWithin the process of building a website, wireframing is where thinking becomes tangible.\n" +
                "\n" +
                "Aside from websites, wireframes are utilized for the prototyping of mobile sites, computer applications, or other screen-based products that involve human-computer interaction");

        return event;
    }

    public static ProgramEvent getEventForDisplay() {
        ProgramEvent event = new ProgramEvent();

        event.setAddToSchedule(true);
        event.setBeginTime("9:00");
        event.setEndTime("10:55");
        event.setName("12 Best Practices from Wunderkraut");
        event.setTrack("Business Showcase [Sponsors only]");
        event.setExpLevel("Beginner");
        event.setRoom("Holland Complex | 8 Hall");
        event.setDescription("In an effort to start a tradition we will repeat last year's session in Prague and we bring you 12 best practices from Wunderkraut presented by 12 of our experienced employees.\n\n" +
                "Wunderkraut does many things differently, and we would like to share some of our insights by using an open space technology method called Ignite.\n\n" +
                "Each speaker has exactly 5 very much practiced minutes to share their topic. In last year's session the topics varied from employee happiness to remote collaboration, for this year we still need to brainstorm but there might be something in their like how to handle cultural differences or the power of saying 'No' .\n\n" +
                "Disclaimer: The actual topics might still change, but rest assure, we'll make sure that quality and format is as good as last year.");

        Speaker speakerOne = new Speaker();
        speakerOne.setName("Vesa Palmu");
        speakerOne.setDescription("Wunderkraut / CEO");
        speakerOne.setPhotoId(R.drawable.vesa_palmu);

        Speaker speakerTwo = new Speaker();
        speakerTwo.setName("Roel De Meester");
        speakerTwo.setDescription("XIO / Managing Director");
        speakerTwo.setPhotoId(R.drawable.roel_de_meester);

        List<Speaker> speakers = new ArrayList<Speaker>();
        speakers.add(speakerOne);
        speakers.add(speakerTwo);

        event.setSpeakers(speakers);

        return event;
    }

    public static Speaker getSpeakerForDisplay() {
        Speaker speaker = new Speaker();

        speaker.setName("Vesa Palmu");
        speaker.setOrganization("Wunderkraut");
        speaker.setJobTitle("CEO");
        speaker.setPhotoId(R.drawable.vesa_palmu_big_photo);
        speaker.setArticle("12 Best Practice from Wunderkraut");
        speaker.setTrack("Coding and Development");
        speaker.setExpLevel("Intermediate");
        speaker.setDate("29 September\n14:05-16:00");
        speaker.setDescription("Wunderkraut CEO Vesa Palmu helps clients solve their business problems, and sometimes computers are involved.\n" +
                "\n" +
                "For him, there’s no debate about IT consulting versus business consulting: IT is business. If companies want to improve their productivity, they need to use the power of IT tools to improve their processes.\n" +
                "\n" +
                "Vesa started his professional career in 1995 while still a computer science student. He went on to create, found, own, run, sell, merge and manage a host of technology businesses throughout the 1990s and on into the 2000s.\n" +
                "\n" +
                "In the mid-2000s, while producing proprietary technologies for global clients, Vesa saw that their technology strategies were not the future. He went completely open source. It was the right decision, although it took a few years for the industry prove it.\n" +
                "\n" +
                "Vesa loves the challenge of a complex project, sings hallelujahs about Drupal and evangelises Agile project management – he teaches it in every enterprise setting he finds himself in.\n" +
                "\n" +
                "He’s a certified Scrum professional, has an EMBA from Helsinki’s Aalto University and is also a qualified diving instructor with more than a thousand dives under his weight-belt.");



        return speaker;
    }

    private static ProgramEvent generateProgramEvent(int position, String speakerName, String reportName, String track, String expLevel, String date, String beginTime,
                                                   String endTime, String room, boolean isAddedToSchedule, boolean isKeynote) {
        ProgramEvent programEvent = new ProgramEvent();

        programEvent.setId(position);
        programEvent.setTrack(track);
        programEvent.setExpLevel(expLevel);
        programEvent.setName(reportName);
        programEvent.setSpeakersName(speakerName);
        programEvent.setBeginTime(beginTime);
        programEvent.setEndTime(endTime);
        programEvent.setAddToSchedule(isAddedToSchedule);
        programEvent.setDate(date);
        programEvent.setKeynote(isKeynote);
        programEvent.setRoom(room);

        return programEvent;
    }

    private static BreakEvent generateBreakEvent(int position, String beginTime, String endTime, boolean isLanchBreak) {
        BreakEvent breakEvent = new BreakEvent();

        breakEvent.setId(position);
        breakEvent.setBeginTime(beginTime);
        breakEvent.setEndTime(endTime);
        breakEvent.setLanchBreak(isLanchBreak);

        return breakEvent;
    }

    private static BoFsEvent generateBoFsEvent(int position, String name, String date, String beginTime,
                                               String endTime, String room, boolean isAddedToSchedule) {
        BoFsEvent event = new BoFsEvent();

        event.setId(position);
        event.setName(name);
        event.setDate(date);
        event.setBeginTime(beginTime);
        event.setEndTime(endTime);
        event.setRoom(room);
        event.setAddToSchedule(isAddedToSchedule);

        return event;
    }
}
