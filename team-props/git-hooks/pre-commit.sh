#!/bin/sh

echo "Running static analysis..."

# Validate Kotlin code with detekt and KtLint before committing
./gradlew app:detektCheck app:ktlintCheck --daemon

status=$?

if [ "$status" = 0 ] ; then
    echo "Static analysis found no problems."
    exit 0
else
    echo 1>&2 "Static analysis found style violations it could not fix."
    exit 1
fi
