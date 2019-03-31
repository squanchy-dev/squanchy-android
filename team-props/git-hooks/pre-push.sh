#!/bin/sh

echo "Running static analysis..."

# Validate Kotlin code with detekt and KtLint before committing
./gradlew detekt ktlintCheck --daemon

status=$?

if [ "$status" = 0 ] ; then
    echo "Static analysis found no issues. Proceeding with push."
    exit 0
else
    echo 1>&2 "Static analysis found issues you need to fix before pushing."
    exit 1
fi
