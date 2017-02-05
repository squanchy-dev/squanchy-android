#!/usr/bin/env bash

## Never run this on your dev machine or it will override your valid google-services.json file!

echo '{
  "project_info": {
    "project_number": "0",
    "project_id": "mock-project-id"
  },
  "client": [
    {
      "client_info": {
        "mobilesdk_app_id": "0",
        "android_client_info": {
          "package_name": "net.squanchy.example"
        }
      },
      "api_key": [
        {
          "current_key": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
        }
      ],
      "services": {
        "analytics_service": {
          "status": 2,
          "analytics_property": {
            "tracking_id": "UA-123456-78"
          }
        }
      }
    }
  ],
  "configuration_version": "1"
}' > app/google-services.json
