%dw 2.0
output application/java
import substringAfterLast from dw::core::Strings
---
substringAfterLast(attributes.requestPath, ".") default ""