# Net Tools API

The Net Tools API is a deployable Mule app that you can deploy to CloudHub. The app will then expose a very simple UI that will allow you to do basic networking commands. The idea is that most networking related issues with your CloudHub VPC and VPN are related to connectivity to your on-prem systems, and most of those issues end up being resolved on the customer end. If you have this tool available to you, you can work with your Networking team to test connectivity to various on-prem systems and verify that firewall and routing rules are working.  It can also be used to generate some traffic that can help with diagnosing networking issues.

This supports HTTP and HTTPS connections with a configurable port for each.

## Features

- DNS lookups
- Ping
- TraceRoute
- Opening a TCP socket
- Simple curl request
- Pull SSL certificates
- Check supported ciphers for a given SSL/TLS endpoint

## Latest build

Latest build can be found here: https://github.com/mulesoft-labs/net-tools-api/releases

# Usage

The UI can be accessed by using the base URL for the app.  The options are listed below.

- CloudHub Shared Load Balancer: `http://{app-name}.{region}.cloudhub.io` where the app-name and region are specific to the deployed app.
- Dedicated Load Balancer: `custom url`.  See *Configuration* section to update settings.

The API Console is available at the `/console` path.

The UI is protected by Basic Authentication, and the default credentials are listed in the *Configuration* section.

# Configuration
The properties below can be set on the app to override the default settings.  The proper port and protocol must be set to accommodate load balancer and VPC firewall rule settings.  The default settings are for the CloudHub shared load balancer HTTP endpoint.  See the [CloudHub Load Balancer documentation](https://docs.mulesoft.com/runtime-manager/lb-architecture) for which port and protocol to use for your configuration.

- `user`: User name for login; defaults to `vpc-tools`
- `pass`: Password for login; defaults to `SomePass`
- `protocol`: The protocol to use; defaults to `http`.  Options: `http` or `https`
- `httpPort`: Sets the listener port for http; defaults to `8081`
- `httpsPort`: Sets the listener port for https; defaults to `8082`
