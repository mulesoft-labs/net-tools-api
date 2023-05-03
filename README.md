# Net Tools API

The Net Tools API is a deployable Mule app that you can deploy to CloudHub or any worker cloud. The app will then expose a very simple UI that will allow you to do basic networking commands. The idea is that most networking related issues with your CloudHub VPC and VPN are related to connectivity to your on-prem systems, and most of those issues end up being resolved on the customer end. If you have this tool available to you, you can work with your Networking team to test connectivity to various on-prem systems and verify that firewall and routing rules are working.  It can also be used to generate some traffic that can help with diagnosing networking issues.

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

The UI is protected by Basic Authentication, and the default credentials are listed in the *Configuration* section.

# Configuration
The properties below can be set on the app to override the default settings.  The proper ports must be set to accommodate load balancer and VPC firewall rule settings.  The default settings are for the CloudHub shared load balancer HTTP endpoint.

- `user`: User name for login.  Defaults to `vpc-tools`
- `pass`: Password for login.  Defaults to `SomePass`
- `httpPort`: Sets the listener port for HTTP.  Defaults to `8081`
- `httpsPort`: Sets the listener port for HTTPS.  Defaults to `8082`
- `httpListener`: The running state of the HTTP endpoint flows.  Defaults to `started`.  Options: `started` or `stopped`.  Stop this to disable HTTP endpoint on CloudHub 1.0 or non-RTF infrastructure.  This doesn't affect RTF or CloudHub 2.0 because only a single HTTP port is used.
- `ignoreFiles`: Comma-delimited list of browser-requested resource files for this app to ignore.  Defaults to `favicon.ico`.

## Network Considerations

- `httpsPort` and `httpPort` **must always** be different numbers, even if `httpListener=stopped`.  This is because both HTTP and HTTPS listener configurations are always created, even if the HTTP endpoint is not enabled.
- CloudHub 2.0 and RTF only use a single port for the HTTP listener.  This means you can only run either HTTP or HTTPS, but not both at the same time.  Make sure the property you want to use is set to the proper port and the other is set to another unused port.
- When using CloudHub 2.0 and RTF, you must enable *Last-Mile Security* in the app's Ingress tab if you want to use HTTPS.
- This does not use `http.port` and `https.port` properties since those are overrriden on Cloudhub 2.0 and RTF to the same port and will prevent the app from starting because of a port conflict.

# References
- [CloudHub 2.0 Infrastructure Considerations](https://docs.mulesoft.com/cloudhub-2/ch2-comparison#infrastructure-considerations)
- [CloudHub 1.0 Load Balancer Architecture](https://docs.mulesoft.com/cloudhub-1/lb-architecture)
- [Enable Last Mile Security in RTF](https://help.mulesoft.com/s/article/How-to-Enable-both-Last-Mile-Security-and-Mutual-TLS-in-Runtime-Fabric)

# Maintenance
This uses the JS libraries below.
- jQuery 1.11.3 [min](https://code.jquery.com/jquery-1.11.3.min.js) and [map](https://code.jquery.com/jquery-1.11.3.min.map).
- [Toastr](https://github.com/CodeSeven/toastr) 2.1.4 [min, map, and css](https://cdnjs.com/libraries/toastr.js).

