#!/usr/bin/env bash

export APP_MODULE_PATH="workspace/repo/app"
export JSON_PATH="$APP_MODULE_PATH/google-services.json"

if [ ! -e ${JSON_PATH} ]; then
    echo "Writing mock google-services.json file to $JSON_PATH..."

    echo '{
      "project_info": {
        "project_number": "012345678912",
        "firebase_url": "https://squanchy-mock.firebaseio.com",
        "project_id": "squanchy-mock",
        "storage_bucket": "squanchy-mock.appspot.com"
      },
      "client": [
        {
          "client_info": {
            "mobilesdk_app_id": "1:012345678912:android:1337deadbeef",
            "android_client_info": {
              "package_name": "net.squanchy.example"
            }
          },
          "oauth_client": [
            {
              "client_id": "XXXXXXXXXXXX-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX.apps.googleusercontent.com",
              "client_type": 1,
              "android_info": {
                "package_name": "net.squanchy.example",
                "certificate_hash": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
              }
            },
            {
              "client_id": "XXXXXXXXXXXX-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX.apps.googleusercontent.com",
              "client_type": 1,
              "android_info": {
                "package_name": "net.squanchy.example",
                "certificate_hash": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
              }
            },
            {
              "client_id": "XXXXXXXXXXXX-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX.apps.googleusercontent.com",
              "client_type": 3
            },
            {
              "client_id": "XXXXXXXXXXXX-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX.apps.googleusercontent.com",
              "client_type": 3
            }
          ],
          "api_key": [
            {
              "current_key": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
            }
          ],
          "services": {
            "appinvite_service": {
              "status": 1,
              "other_platform_oauth_client": []
            },
            "ads_service": {
              "status": 2
            }
          }
        }
      ],
      "configuration_version": "1"
    }' > ${JSON_PATH}

    echo "Done."
else
    echo "The $JSON_PATH file already exists, skipping..."
fi
