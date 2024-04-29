{
  "$schema": "https://raw.githubusercontent.com/oasis-tcs/sarif-spec/master/Schemata/sarif-schema-2.1.0.json",
  "version": "2.1.0",
  "runs": [
    {
      "tool": {
        "driver": {
          "name": "Trivy",
          "version": "0.12.0"
        }
      },
      "results": [
        {
          "ruleId": "Vulnerability",
          "level": "error",
          "message": {
            "text": "Critical vulnerability found in the Docker image."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "https://example.com/repo/docker-image.tar.gz"
                }
              }
            }
          ],
          "properties": {
            "severity": "Critical",
            "vulnerabilityId": "CVE-2021-12345",
            "description": "Description of the vulnerability."
          }
        },
        {
          "ruleId": "Vulnerability",
          "level": "warning",
          "message": {
            "text": "Medium severity vulnerability found in the Docker image."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "https://example.com/repo/docker-image.tar.gz"
                }
              }
            }
          ],
          "properties": {
            "severity": "Medium",
            "vulnerabilityId": "CVE-2021-67890",
            "description": "Description of the vulnerability."
          }
        }
      ]
    }
  ]
}