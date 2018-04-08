# Squanchy Android
[![Master CI](https://img.shields.io/circleci/project/github/squanchy-dev/squanchy-android/develop.svg?style=for-the-badge)](https://circleci.com/gh/squanchy-dev/squanchy-android/tree/develop) [![Apache 2 license](https://img.shields.io/github/license/squanchy-dev/squanchy-android.svg?style=for-the-badge)](https://github.com/squanchy-dev/squanchy-android/blob/develop/LICENSE)

Squanchy is an open source platform for conferences. The source code for the [Firebase backend](https://github.com/squanchy-dev/squanchy-firebase) and for the [Flutter port of the app](https://github.com/squanchy-dev/squanchy-flutter) is available in other repositories of this organisation.
 
Documentation is available on [http://squanchy.net/](http://squanchy.net/). The project is maintained by independent contributors (see
[CONTRIBUTORS.md](CONTRIBUTORS.md)).

# Setting up the app
Starting your own conference app is easy and requires little effort, but you need to prepare some files. See [`docs/setup.md`](docs/setup.md) for
detailed instructions.

Please note that this app uses some third party services:
 * Firebase (Firestore, Push messages, etc.)
 * Fabric: Crashlytics
 
While not all of them are strictly necessary for the app to work (with the exception of Firebase's RTDB), it is currently not possible for the code to
work without them. We plan on eventually abstracting away the implementations so that they would simply be disabled if there is no API configured, but
we haven't done it yet. If you need to use Squanchy without some of those implementations, please feel free to make them optional and contribute back 
to mainline your changes.  

## Git hooks
The project uses Detekt and KtLint to validate Kotlin code before committing. This is done with a Git `pre-commit`
hook, which is automatically installed by Gradle when the `clean` or `assemble` tasks are run. You can also install the hook manually from Gradle by
executing the `installGitHooks` task on the root project: `$ ./gradlew installGitHooks`.

If you wish to commit code that is failing this test for whatever reason, you can use the `--no-verify` flag when committing with `git`. Please note
that if you don't address the issues before pushing, the CI will fail the build.
