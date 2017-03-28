# Gamification

Squanchy supports adding gamification to your event. You can access the feature through `ContestService.java`.

Use achievements to break down the contest progress and then add them to the user history when you detect that the user has obtained them.
Every achievement must be represented by an unique id. The proximity-based achievements are created with the NearIT platform with a customJSON content.
The json strucute is the following:
```json
{
  "action": "stand",
  "subject": "ACHIEVEMENT_ID_FOR_THE_STAND"
}
```
For every stand we have a beacon. For every beacon there's a NearIT recipe with a different CustomJSON to represent a different achievement.
`ContestService.java` has methods for adding achievements and for getting the current user progress.
In the remote configuration of your firebase project (or just in the local default value) you can set the goal of the contest as the total number of achievements to obtain.