# Net Tools API

The Net-Tools-Api app is a deployable Mule app that you can deploy to CloudHub. The app will then expose a very simple UI that will allow you to do basic networking commands - the supported commands are DNS lookups, Ping, TraceRoute, opening a TCP socket, and doing a simple request with curl. The idea is that most networking related issues with your CloudHub VPC and VPN are related to connectivity to your on-prem systems, and most of those issues end up being resolved on the customer end. If you have this tool available to you, you can work with your Networking team to test connectivity to various on-prem systems and verify that firewall and routing rules are working (and if not, you can generate some traffic that can help with diagnosing the issue).

## Last build

Last build can be found here: https://github.com/mulesoft-labs/net-tools-api/releases

## Usage

The UI can be access hitting *http://{app-name}.cloudhub.io*. It is protected by Basic Auth, the default credentials are "vpc-tools"/"SomePass". You can change those credentials setting "user" and "pass" properties in the CloudHub UI while deploying.

You can also use the API Console accessing *http://{app-name}.cloudhub.io/api/console*.
