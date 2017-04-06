# Squanchy

Squanchy is an open source schedule platform for conferences.

It was born as a fork of [Connfa](http://connfa.com), but later diverged to embrace different design decision and user needs.
 
Documentation is available on [http://squanchy.net/](http://squanchy.net/). The project is maintained by independent contributors (see CONTRIBUTORS.md).

# Setting up the app

Starting your own conference app is easy and requires very little effort. See [`docs/setup.md`](docs/setup.md) for detailed instructions.

Please note that this app uses some third party services:
 * Firebase (Realtime DB, Push messages, etc)
 * Fabric: Crashlytics and Twitter
 * NearIt (proximity/location services)
 
While not all of them are strictly necessary for the app to work (with the exception of Firebase's RTDB), it is currently not possible for the code to work without them.
We plan on eventually abstracting away the implementations so that they would simply be disabled if there is no API configured, but we haven't done it yet.
If you need to use Squanchy without some of those implementations, please feel free to make them optional and contribute back to mainline your changes.  
