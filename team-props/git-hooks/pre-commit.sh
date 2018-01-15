#!/bin/sh

echo "Running static analysis..."

# Format code using KtLint
./gradlew app:ktlintFormat app:detektCheck app:ktlint --daemon

status=$?

if [ "$status" = 0 ] ; then
    echo "Static analysis found no problems."
    exit 0
else
    echo 1>&2 "Static analysis found style violations it could not fix."
    exit 1
fi
